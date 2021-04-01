import java.text.SimpleDateFormat
import java.io.IOException

import android.util.Log

import java.io.InputStreamReader

import java.io.BufferedReader

import java.io.InputStream
import java.nio.charset.Charset


private external fun convWithWavelib(
    signalVector: DoubleArray,
    signalLength: Int,
    kernelVector: DoubleArray,
    kernelLength: Int
): DoubleArray

private external fun parseToDouble(expr: String): Double

internal external fun diffCWTFT(
    signalVector: DoubleArray,
    signalLength: Int,
    scale: Double,
    dt: Double
): DoubleArray

internal actual fun convWorker(A: DoubleArray, B: DoubleArray): DoubleArray = convWithWavelib(A, A.count(), B, B.count())

internal actual fun parseToInt(expression: String): Int? {
    val ans = parseToDouble(expression)

    return if(ans.isNaN()) {null} else {ans.toInt()}
}

internal actual fun filtfiltWorker(
    xCoefs: DoubleArray,
    yCoefs: DoubleArray,
    data: DoubleArray
): DoubleArray {
    // Step I: Find initial conditions and specify transients length
    // Solve for initial conditions using a solver <Ax = b>

    val matrixA = SincMatrix(doubleArrayOf(1.0 + yCoefs[1], -1.0, yCoefs[2], 1.0), 2, 2).asSimpleMatrix()
    val b = SincMatrix(
        doubleArrayOf(
            xCoefs[1] - yCoefs[1] * xCoefs[0],
            xCoefs[2] - yCoefs[2] * xCoefs[0]
        ), 2, 1
    ).asSimpleMatrix()
    val x = matrixA.solve(b).asSincMatrix()
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

internal actual fun dateToTimeStampWorker(dateFormat: String, date:String): Double {
    val dateFormatter = SimpleDateFormat(dateFormat)
    val timeMillis = dateFormatter.parse(date)
    return if(timeMillis != null) {
        timeMillis.time / 1000.0 // date.time is in milliseconds.
    } else {
        -1.0
    }
}
internal actual fun fileReadWorker(filePath: String): String? = SincMatrix.readFileAsTextUsingInputStream(filePath)

private fun SincMatrix.Companion.readFileAsTextUsingInputStream(fileName: String): String? = SincMatrix::class.java.getResourceAsStream(fileName)?.readBytes()?.toString(Charsets.UTF_8)