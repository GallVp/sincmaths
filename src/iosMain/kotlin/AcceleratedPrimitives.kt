import kotlinx.cinterop.*
import platform.Accelerate.*

internal fun sumOfVectorElements(vector:DoubleArray):Double {
    val vectorLen = vector.size
    val resultValue = nativeHeap.allocArray<DoubleVar>(1L)
    vDSP_sveD(vector.toCValues(), 1L, resultValue, vectorLen.toULong())

    val returnValue = resultValue[0]
    nativeHeap.free(resultValue)
    return returnValue
}

internal fun addVectors(vectorA:DoubleArray, vectorB:DoubleArray):DoubleArray {
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

internal fun addScalarToVector(vector:DoubleArray, scalar:Double):DoubleArray {
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

internal fun multiplyVectorToScalar(vector:DoubleArray, scalar:Double):DoubleArray {
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

internal fun multiplyElementsOfVectors(vectorA:DoubleArray, vectorB:DoubleArray):DoubleArray {
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

    val returnVector= resultVector.createCopyArray(vectorLen)
    nativeHeap.free(resultVector)

    return returnVector
}

internal fun divideElementsOfVectors(vectorA:DoubleArray, vectorB:DoubleArray):DoubleArray {
    val vectorLen = vectorA.size
    val resultVector = nativeHeap.allocArray<DoubleVar>(vectorLen)
    vDSP_vdivD(
        vectorA.toCValues(),
        1L,
        vectorB.toCValues(),
        1L,
        resultVector,
        1L,
        vectorLen.toULong()
    )

    val returnVector= resultVector.createCopyArray(vectorLen)
    nativeHeap.free(resultVector)

    return returnVector
}

internal fun exponentOfVector(vector:DoubleArray, exponent:Double):DoubleArray {
    val vectorLen = vector.size
    val resultVector = nativeHeap.allocArray<DoubleVar>(vectorLen)
    vvpow(
        resultVector,
        doubleArrayOf(exponent).toCValues(),
        vector.toCValues(),
        vectorLen.toLong().toCPointer()
    )

    val returnVector = resultVector.createCopyArray(vectorLen)
    nativeHeap.free(resultVector)
    return returnVector
}

internal fun floorOfElementsOfVector(vector:DoubleArray):DoubleArray {
    val vectorLen = vector.size
    val resultVector = nativeHeap.allocArray<DoubleVar>(vectorLen)
    vvfloor(resultVector, vector.toCValues(), vectorLen.toLong().toCPointer())

    val returnVector = resultVector.createCopyArray(vectorLen)
    nativeHeap.free(resultVector)
    return returnVector
}

internal fun absOfElementsOfVector(vector:DoubleArray):DoubleArray {
    val vectorLen = vector.size
    val resultVector = nativeHeap.allocArray<DoubleVar>(vectorLen)
    vvfabs(resultVector, vector.toCValues(), vectorLen.toLong().toCPointer())

    val returnVector = resultVector.createCopyArray(vectorLen)
    nativeHeap.free(resultVector)
    return returnVector
}

internal fun sinOfElementsOfVector(vector:DoubleArray):DoubleArray {
    val vectorLen = vector.size
    val resultVector = nativeHeap.allocArray<DoubleVar>(vectorLen)
    vvsin(resultVector, vector.toCValues(), vectorLen.toLong().toCPointer())

    val returnVector = resultVector.createCopyArray(vectorLen)
    nativeHeap.free(resultVector)
    return returnVector
}

internal fun cosOfElementsOfVector(vector:DoubleArray):DoubleArray {
    val vectorLen = vector.size
    val resultVector = nativeHeap.allocArray<DoubleVar>(vectorLen)
    vvcos(resultVector, vector.toCValues(), vectorLen.toLong().toCPointer())

    val returnVector = resultVector.createCopyArray(vectorLen)
    nativeHeap.free(resultVector)
    return returnVector
}

internal fun multiplyRowMajorMatrices(lhsMat:DoubleArray, lhsM:Int, lhsN:Int, rhsMat:DoubleArray, rhsM:Int, rhsN:Int):DoubleArray {
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

internal fun transposeOfRowMajorMatrix(mat:DoubleArray, m:Int, n:Int):DoubleArray {
    val matrixSize = m*n
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