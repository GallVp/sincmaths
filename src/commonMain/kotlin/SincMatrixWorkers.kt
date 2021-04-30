package sincmaths.sincmatrix.workers

import sincmaths.ConvolutionShape
import sincmaths.SincMatrix
import sincmaths.sincmatrix.*

internal expect fun diffCWTFTWorker(
    signalVector: DoubleArray,
    signalLength: Int,
    scale: Double,
    dt: Double
): DoubleArray

internal expect fun convWorker(A: DoubleArray, B: DoubleArray): DoubleArray
internal expect fun parseToInt(expression: String): Int?

/**
 * Takes date and date format string to produce a time stamp in seconds which represents time since 1970
 */
internal expect fun dateToTimeStampWorker(dateFormat: String, date: String): Double
internal expect fun fileReadWorker(filePath: String, bundleID: String?): String?

internal fun sgolayWorker(vectorIn: SincMatrix, B: SincMatrix): SincMatrix {
    var vector = vectorIn.copyOf()
    val vectorIsRow = vector.isrow()
    val filterLength = B.size().first()
    if (vectorIsRow) {
        vector = vector.transpose()
    }
    var startsAt = (filterLength - 1) / 2 + 2
    var endsAt = B.size().first()
    val initialSegment = B.getRows(
        mlRows = (startsAt..endsAt).reversed().toList().toIntArray()
    ) * vector.getRows(mlRows = (1..filterLength).reversed().toList().toIntArray())
    val convCoeffs = B.getRow(mlRow = (filterLength - 1) / 2 + 1)
    val middleSegment = vector.conv(B = convCoeffs, shape = ConvolutionShape.valid)
    startsAt = 1
    endsAt = (filterLength - 1) / 2
    val finalSegment = B.getRows(
        mlRows = (startsAt..endsAt).reversed().toList().toIntArray()
    ) * vector.getRows(
        mlRows = ((vector.numel() - (filterLength - 1))..vector.numel()).reversed()
            .toList().toIntArray()
    )
    val computedVector =
        initialSegment.asRowMajorArray() + middleSegment.asRowMajorArray() + finalSegment.asRowMajorArray()
    var returnVector =
        SincMatrix(rowMajArray = computedVector, m = vector.numRows(), n = vector.numCols())
    if (vectorIsRow) {
        returnVector = returnVector.transpose()
    }
    return returnVector
}

internal fun filtfiltWorker(
    xCoefs: DoubleArray,
    yCoefs: DoubleArray,
    data: DoubleArray
): DoubleArray {
    // Step I: Find initial conditions and specify transients length
    // Solve for initial conditions using a solver <Ax = b>

    val matrixA = SincMatrix(doubleArrayOf(1.0 + yCoefs[1], -1.0, yCoefs[2], 1.0), 2, 2)
    val b = SincMatrix(
        doubleArrayOf(
            xCoefs[1] - yCoefs[1] * xCoefs[0],
            xCoefs[2] - yCoefs[2] * xCoefs[0]
        ), 2, 1
    )
    val x = matrixA.solve(b)
    val initialConditions = x.asRowMajorArray()
    val transientsLength = 6
    // Extend data vector for transients
    val firstSegment =
        data.slice(1..transientsLength).toDoubleArray().reversedArray().map { value ->
            (2.0 * data[0] - value)
        }.toDoubleArray()
    val lastSegment =
        data.slice(data.size - transientsLength - 1 until data.size - 1).map { value ->
            2.0 * data[data.size - 1] - value
        }.reversed().toDoubleArray()
    val extendedData = firstSegment + data + lastSegment
    // + is array join
    // Filter, reverse, filter, reverse
    val firstPassOutput = filterWorker(
        xCoefs,
        yCoefs,
        extendedData,
        initialConditions.map { it * extendedData[0] }.toDoubleArray()
    )
    val firstReverseOutput = firstPassOutput.reversed().toDoubleArray()
    val secondPassOutput = filterWorker(
        xCoefs,
        yCoefs,
        firstReverseOutput,
        initialConditions.map { it * firstReverseOutput[0] }.toDoubleArray()
    )
    val secondReverseOutput = secondPassOutput.reversed().toDoubleArray()
    return secondReverseOutput.slice(transientsLength until secondReverseOutput.size - transientsLength)
        .toDoubleArray()
}

internal fun filterWorker(
    xCoefs: DoubleArray,
    yCoefs: DoubleArray,
    data: DoubleArray,
    initialConditions: DoubleArray
): DoubleArray {
    // Normalise coefficients if a0 is not 1
    val yNormCoefs: DoubleArray
    val xNormCoefs: DoubleArray
    if (yCoefs[0] != 1.0) {
        yNormCoefs = yCoefs.map {
            it / yCoefs[0]
        }.toDoubleArray()
        xNormCoefs = xCoefs.map {
            it / yCoefs[0]
        }.toDoubleArray()
    } else {
        yNormCoefs = yCoefs
        xNormCoefs = xCoefs
    }
    // Setup a's and b's
    val a1 = yNormCoefs[1]
    val a2 = yNormCoefs[2]
    val b0 = xNormCoefs[0]
    val b1 = xNormCoefs[1]
    val b2 = xNormCoefs[2]
    var s1Delay = initialConditions[0]
    var s2Delay = initialConditions[1]
    return data.map { value ->
        val outValue = b0 * value + s1Delay
        s1Delay = s2Delay + b1 * value - a1 * outValue
        s2Delay = b2 * value - a2 * outValue
        outValue
    }.toDoubleArray()
}

internal fun medianWorker(sortedVector: DoubleArray): Double {
    require(sortedVector.isNotEmpty()) { "SMError: For median, the number of elements should be greater than zero" }

    return if (sortedVector.size % 2 < 1) {
        //even
        (sortedVector[sortedVector.size / 2 - 1] + sortedVector[sortedVector.size / 2]) / 2.0
    } else {
        //odd
        sortedVector[(sortedVector.size - 1) / 2]
    }
}

internal fun vectorPercentileWorker(sortedVector: SincMatrix, p: SincMatrix): SincMatrix {

    val n = sortedVector.numel()

    var r = (p / 100.0) * n.toDouble()
    val k = (r + 0.5).floor()
    val kp1 = k + 1.0
    r -= k

    k.setWithLV(k lt 1.0, 1.0)
    kp1.indices.map {
        kp1[it] = if(kp1[it] < n.toDouble()) {kp1[it]} else {n.toDouble()}
    }

    val y = (0.5 + r).elMul(sortedVector[kp1.asIntArray()]) + (0.5 - r).elMul(sortedVector[k.asIntArray()])

    val exact = r et -0.5
    if(exact.any()) {
        y.setWithLV(exact, sortedVector.getWithLV(k.getWithLV(exact)))
    }

    val same = sortedVector[k.asIntArray()] et sortedVector[kp1.asIntArray()]
    if(same.any()) {
        val xValues = sortedVector[k.asIntArray()]
        y.setWithLV(same, xValues.getWithLV(same))
    }

    return y
}