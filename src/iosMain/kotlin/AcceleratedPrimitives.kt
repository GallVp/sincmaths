import kotlinx.cinterop.*
import platform.Accelerate.*

internal fun createRampVector(startFrom: Double, increment: Double, totalCount: Int): DoubleArray {
    val resultVector = nativeHeap.allocArray<DoubleVar>(totalCount)
    vDSP_vrampD(
        doubleArrayOf(startFrom).toCValues(),
        doubleArrayOf(increment).toCValues(),
        resultVector,
        1L,
        totalCount.toULong()
    )
    val returnVector = resultVector.createCopyArray(totalCount)
    nativeHeap.free(resultVector)
    return returnVector
}

internal fun createZerosVector(totalCount: Int): DoubleArray {
    val resultVector = nativeHeap.allocArray<DoubleVar>(totalCount)
    vDSP_vclrD(
        resultVector,
        1L,
        totalCount.toULong()
    )
    val returnVector = resultVector.createCopyArray(totalCount)
    nativeHeap.free(resultVector)
    return returnVector
}

internal fun createVector(fillValue: Double, totalCount: Int): DoubleArray {
    val resultVector = nativeHeap.allocArray<DoubleVar>(totalCount)
    vDSP_vfillD(
        doubleArrayOf(fillValue).toCValues(),
        resultVector,
        1L,
        totalCount.toULong()
    )
    val returnVector = resultVector.createCopyArray(totalCount)
    nativeHeap.free(resultVector)
    return returnVector
}

internal fun createOnesVector(totalCount: Int) = createVector(1.0, totalCount)

internal fun findNonZeroIndices(ofVector: DoubleArray, isZeroIndexed: Boolean = true): DoubleArray {
    val vectorLen = ofVector.size
    val vectorOfOnes = createVector(1.0, vectorLen)
    val resultVector = nativeHeap.allocArray<DoubleVar>(vectorLen)
    for (i in 0 until vectorLen) {
        resultVector[i] = 0.0
    }
    vDSP_vcmprsD(vectorOfOnes.toCValues(), 1L, ofVector.toCValues(), 1, resultVector, 1, vectorLen.toULong())
    val numberOfElements = sumOfVectorElements(resultVector.createCopyArray(vectorLen)).toInt()
    val indices = if (isZeroIndexed) {
        createRampVector(0.0, 1.0, vectorLen)
    } else {
        createRampVector(1.0, 1.0, vectorLen)
    }
    vDSP_vcmprsD(indices.toCValues(), 1L, ofVector.toCValues(), 1, resultVector, 1, vectorLen.toULong())
    val nonZeroIndices = resultVector.createCopyArray(vectorLen).slice(0 until numberOfElements).toDoubleArray()
    nativeHeap.free(resultVector)
    return nonZeroIndices
}

internal fun minOfVectorElements(vector: DoubleArray): Double {
    val vectorLen = vector.size
    val resultValue = nativeHeap.allocArray<DoubleVar>(1L)
    vDSP_minvD(vector.toCValues(), 1L, resultValue, vectorLen.toULong())

    val returnValue = resultValue[0]
    nativeHeap.free(resultValue)
    return returnValue
}

internal fun maxOfVectorElements(vector: DoubleArray): Double {
    val vectorLen = vector.size
    val resultValue = nativeHeap.allocArray<DoubleVar>(1L)
    vDSP_maxvD(vector.toCValues(), 1L, resultValue, vectorLen.toULong())

    val returnValue = resultValue[0]
    nativeHeap.free(resultValue)
    return returnValue
}

internal fun sumOfVectorElements(vector: DoubleArray): Double {
    val vectorLen = vector.size
    val resultValue = nativeHeap.allocArray<DoubleVar>(1L)
    vDSP_sveD(vector.toCValues(), 1L, resultValue, vectorLen.toULong())

    val returnValue = resultValue[0]
    nativeHeap.free(resultValue)
    return returnValue
}

internal fun addVectors(vectorA: DoubleArray, vectorB: DoubleArray): DoubleArray {
    val vectorLen = vectorA.size
    val resultVector = nativeHeap.allocArray<DoubleVar>(vectorLen)
    vDSP_vaddD(
        vectorA.toCValues(),
        1L,
        vectorB.toCValues(),
        1L,
        resultVector,
        1L,
        vectorLen.toULong()
    )
    val returnVector = resultVector.createCopyArray(vectorLen)
    nativeHeap.free(resultVector)
    return returnVector
}

internal fun addScalarToVector(vector: DoubleArray, scalar: Double): DoubleArray {
    val vectorLen = vector.size
    val resultVector = nativeHeap.allocArray<DoubleVar>(vectorLen)
    vDSP_vsaddD(
        vector.toCValues(),
        1L,
        doubleArrayOf(scalar).toCValues(),
        resultVector,
        1L,
        vectorLen.toULong()
    )
    val returnVector = resultVector.createCopyArray(vectorLen)
    nativeHeap.free(resultVector)
    return returnVector
}

internal fun multiplyVectorToScalar(vector: DoubleArray, scalar: Double): DoubleArray {
    val vectorLen = vector.size
    val resultVector = nativeHeap.allocArray<DoubleVar>(vectorLen)
    vDSP_vsmulD(
        vector.toCValues(),
        1L,
        doubleArrayOf(scalar).toCValues(),
        resultVector,
        1L,
        vectorLen.toULong()
    )
    val returnVector = resultVector.createCopyArray(vectorLen)
    nativeHeap.free(resultVector)
    return returnVector
}

internal fun multiplyElementsOfVectors(vectorA: DoubleArray, vectorB: DoubleArray): DoubleArray {
    val vectorLen = vectorA.size
    val resultVector = nativeHeap.allocArray<DoubleVar>(vectorLen)
    vDSP_vmulD(
        vectorA.toCValues(),
        1L,
        vectorB.toCValues(),
        1L,
        resultVector,
        1L,
        vectorLen.toULong()
    )

    val returnVector = resultVector.createCopyArray(vectorLen)
    nativeHeap.free(resultVector)

    return returnVector
}

internal fun divideElementsOfVectors(vectorA: DoubleArray, vectorB: DoubleArray): DoubleArray {
    val vectorLen = vectorA.size
    val resultVector = nativeHeap.allocArray<DoubleVar>(vectorLen)
    vDSP_vdivD(
        vectorB.toCValues(),
        1L,
        vectorA.toCValues(),
        1L,
        resultVector,
        1L,
        vectorLen.toULong()
    )

    val returnVector = resultVector.createCopyArray(vectorLen)
    nativeHeap.free(resultVector)

    return returnVector
}

internal fun exponentOfVector(vector: DoubleArray, exponent: Double): DoubleArray {
    val vectorLen = vector.size
    val resultVector = nativeHeap.allocArray<DoubleVar>(vectorLen)
    vvpow(
        resultVector,
        createVector(exponent, vectorLen).toCValues(),
        vector.toCValues(),
        intArrayOf(vectorLen).toCValues()
    )

    val returnVector = resultVector.createCopyArray(vectorLen)
    nativeHeap.free(resultVector)
    return returnVector
}

internal fun floorOfElementsOfVector(vector: DoubleArray): DoubleArray {
    val vectorLen = vector.size
    val resultVector = nativeHeap.allocArray<DoubleVar>(vectorLen)
    vvfloor(resultVector, vector.toCValues(), intArrayOf(vectorLen).toCValues())

    val returnVector = resultVector.createCopyArray(vectorLen)
    nativeHeap.free(resultVector)
    return returnVector
}

internal fun absOfElementsOfVector(vector: DoubleArray): DoubleArray {
    val vectorLen = vector.size
    val resultVector = nativeHeap.allocArray<DoubleVar>(vectorLen)
    vvfabs(resultVector, vector.toCValues(), intArrayOf(vectorLen).toCValues())

    val returnVector = resultVector.createCopyArray(vectorLen)
    nativeHeap.free(resultVector)
    return returnVector
}

internal fun sinOfElementsOfVector(vector: DoubleArray): DoubleArray {
    val vectorLen = vector.size
    val resultVector = nativeHeap.allocArray<DoubleVar>(vectorLen)
    vvsin(resultVector, vector.toCValues(), intArrayOf(vectorLen).toCValues())

    val returnVector = resultVector.createCopyArray(vectorLen)
    nativeHeap.free(resultVector)
    return returnVector
}

internal fun cosOfElementsOfVector(vector: DoubleArray): DoubleArray {
    val vectorLen = vector.size
    val resultVector = nativeHeap.allocArray<DoubleVar>(vectorLen)
    vvcos(resultVector, vector.toCValues(), intArrayOf(vectorLen).toCValues())

    val returnVector = resultVector.createCopyArray(vectorLen)
    nativeHeap.free(resultVector)
    return returnVector
}

internal fun multiplyRowMajorMatrices(
    lhsMat: DoubleArray,
    lhsM: Int,
    lhsN: Int,
    rhsMat: DoubleArray,
    rhsM: Int,
    rhsN: Int
): DoubleArray {
    val matrixSize = lhsM * rhsN
    val resultMatrix = nativeHeap.allocArray<DoubleVar>(matrixSize)
    vDSP_mmulD(
        lhsMat.toCValues(),
        1,
        rhsMat.toCValues(),
        1,
        resultMatrix,
        1L,
        lhsM.toULong(),
        rhsN.toULong(),
        lhsN.toULong()
    )
    val returnMatrix = resultMatrix.createCopyArray(matrixSize)
    nativeHeap.free(resultMatrix)
    return returnMatrix
}

internal fun transposeOfRowMajorMatrix(mat: DoubleArray, m: Int, n: Int): DoubleArray {
    val matrixSize = m * n
    val transposedMatrix = nativeHeap.allocArray<DoubleVar>(matrixSize)
    vDSP_mtransD(
        mat.toCValues(),
        1,
        transposedMatrix,
        1,
        n.toULong(),
        m.toULong()
    )
    val returnData = transposedMatrix.createCopyArray(matrixSize)
    nativeHeap.free(transposedMatrix)
    return returnData
}

internal fun convolveVectors(vectorA: DoubleArray, vectorB: DoubleArray): DoubleArray {
    val initArray = createZerosVector(vectorB.size - 1)
    val midArray = vectorA
    val finalArray = createZerosVector(vectorB.size)
    val paddedArray = initArray + midArray + finalArray

    val bufferLength = intArrayOf(vectorA.size + vectorB.size - 1, vectorA.size, vectorB.size).maxOrNull() ?: 0
    val buffer = nativeHeap.allocArray<DoubleVar>(bufferLength)

    val convKernel = nativeHeap.allocArray<DoubleVar>(vectorB.size)
    for (i in vectorB.indices) {
        convKernel[i] = vectorB[i]
    }

    vDSP_convD(
        paddedArray.toCValues(),
        1L,
        convKernel + (vectorB.size - 1),
        -1L,
        buffer,
        1L,
        bufferLength.toULong(),
        vectorB.size.toULong()
    )

    val returnData = buffer.createCopyArray(bufferLength)
    nativeHeap.free(buffer)
    nativeHeap.free(convKernel)

    return returnData
}

/**
 * Ref: https://linux.die.net/man/l/dsgesv
 */
internal fun solveNonsymSquareSystem(
    rowMajorMatrix: DoubleArray,
    m: Int,
    n: Int,
    bVector: DoubleArray
): DoubleArray {
    val colMajMatA = transposeOfRowMajorMatrix(rowMajorMatrix, m, n)
    val xVector = nativeHeap.allocArray<DoubleVar>(m)
    memScoped {

        val numEq = alloc<IntVar>()
        numEq.value = m

        val numColsB = alloc<IntVar>()
        numColsB.value = 1

        val mutableA = colMajMatA.toCValues()

        val leadDimOfA = intArrayOf(m).toCValues()

        val ipiv = IntArray(m) { 0 }.toCValues()

        val b = bVector.toCValues()

        val ldb = intArrayOf(m).toCValues()

        val ldx = intArrayOf(m).toCValues()

        val work = DoubleArray(m) {0.0}.toCValues()
        val swork = FloatArray(m*(m+1)) {0.0F}.toCValues()

        val iter = intArrayOf(0).toCValues()
        val info = intArrayOf(0).toCValues()

        dsgesv_(numEq.ptr, numColsB.ptr, mutableA, leadDimOfA, ipiv, b, ldb, xVector, ldx, work, swork, iter, info)
    }
    val returnArray = xVector.createCopyArray(m)
    nativeHeap.free(xVector)
    return returnArray
}