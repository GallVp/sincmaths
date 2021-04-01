import kotlinx.cinterop.*
import platform.Accelerate.*

actual class SincMatrix actual constructor(rowMajArray: DoubleArray, private val m: Int, private val n: Int) {

    private val matrixData = rowMajArray
    actual fun numRows(): Int = m
    actual fun numCols(): Int = n

    // ************************************************************************* SincMatrixAsTypes

    actual fun asArray(): DoubleArray {
        require(this.isvector()) { "SMError: Matrix is not a vector and conversion is invalid" }
        return this.matrixData
    }

    actual fun asRowMajorArray(): DoubleArray = this.matrixData

    // ************************************************************************* SincMatrixSet

    actual operator fun set(mlRow: Int, mlCol: Int, value: Double) {
        this.matrixData[this.getIndex(mlRow, mlCol) - 1] = value
    }

    actual operator fun set(index: Int, value: Double) {
        this.matrixData[index - 1] = value
    }

    // ************************************************************************* SincMatrixArithmetic

    actual operator fun times(rhs: SincMatrix): SincMatrix {
        val lhsM = this.size().first()
        val lhsN = this.size().last()
        val rhsM = rhs.size().first()
        val rhsN = rhs.size().last()

        require(lhsN == rhsM) { "SMError: Dimension mismatch. In A*B, ncols(A) == nrows(B)" }
        val resultMatrix = nativeHeap.allocArray<DoubleVar>(lhsM * rhsN)
        vDSP_mmulD(
            this.matrixData.toCValues(),
            1,
            rhs.matrixData.toCValues(),
            1,
            resultMatrix,
            1L,
            lhsM.toULong(),
            rhsN.toULong(),
            lhsN.toULong()
        )
        val returnData = resultMatrix.createCopyArray(lhsM * rhsN).asSincMatrix(lhsM, rhsN)
        nativeHeap.free(resultMatrix)
        return returnData
    }

    actual operator fun times(rhs: Double): SincMatrix {
        val resultMatrix = nativeHeap.allocArray<DoubleVar>(this.numel())
        vDSP_vsmulD(
            this.matrixData.toCValues(),
            1L,
            doubleArrayOf(rhs).toCValues(),
            resultMatrix,
            1L,
            this.numel().toULong()
        )
        val returnData = resultMatrix.createCopyArray(this.numel()).asSincMatrix(this.numCols(), this.numRows())
        nativeHeap.free(resultMatrix)
        return returnData
    }

    actual operator fun plus(rhs: SincMatrix): SincMatrix {
        val resultMatrix = nativeHeap.allocArray<DoubleVar>(this.numel())
        vDSP_vaddD(
            this.matrixData.toCValues(),
            1L,
            rhs.matrixData.toCValues(),
            1L,
            resultMatrix,
            1L,
            this.numel().toULong()
        )
        val returnData = resultMatrix.createCopyArray(this.numel()).asSincMatrix(this.numCols(), this.numRows())
        nativeHeap.free(resultMatrix)
        return returnData
    }

    actual operator fun plus(rhs: Double): SincMatrix {
        val resultMatrix = nativeHeap.allocArray<DoubleVar>(this.numel())
        vDSP_vsaddD(
            this.matrixData.toCValues(),
            1L,
            doubleArrayOf(rhs).toCValues(),
            resultMatrix,
            1L,
            this.numel().toULong()
        )
        val returnData = resultMatrix.createCopyArray(this.numel()).asSincMatrix(this.numCols(), this.numRows())
        nativeHeap.free(resultMatrix)
        return returnData
    }

    actual infix fun elMul(rhs: SincMatrix): SincMatrix {
        require(this.size() == rhs.size()) { "SMError: Dimension mismatch. In A elMul B, size(A) == size(B)" }

        val resultArray = nativeHeap.allocArray<DoubleVar>(this.numel())
        vDSP_vmulD(this.matrixData.toCValues(), 1L, rhs.matrixData.toCValues(), 1L, resultArray, 1L, this.numel().toULong())

        val returnMatrix = resultArray.createCopyArray(this.numel()).asSincMatrix(this.numRows(), this.numCols())
        nativeHeap.free(resultArray)

        return returnMatrix
    }

    actual infix fun elDiv(rhs: SincMatrix): SincMatrix {
        require(this.size() == rhs.size()) { "SMError: Dimension mismatch. In A elDiv B, size(A) == size(B)" }

        val resultArray = nativeHeap.allocArray<DoubleVar>(this.numel())
        vDSP_vdivD(this.matrixData.toCValues(), 1L, rhs.matrixData.toCValues(), 1L, resultArray, 1L, this.numel().toULong())

        val returnMatrix = resultArray.createCopyArray(this.numel()).asSincMatrix(this.numRows(), this.numCols())
        nativeHeap.free(resultArray)

        return returnMatrix
    }

    actual fun elSum(): Double {

        val resultValue = nativeHeap.allocArray<DoubleVar>(1L)
        vDSP_sveD(this.matrixData.toCValues(), 1L, resultValue, this.numel().toULong())

        val returnValue = resultValue[0]
        nativeHeap.free(resultValue)
        return returnValue
    }

    actual infix fun elPow(power: Double): SincMatrix {

        val resultArray = nativeHeap.allocArray<DoubleVar>(this.numel())
        vvpow(resultArray, doubleArrayOf(power).toCValues(), this.matrixData.toCValues(), this.numel().toLong().toCPointer())

        val returnMatrix = resultArray.createCopyArray(this.numel()).asSincMatrix(this.numRows(), this.numCols())
        nativeHeap.free(resultArray)
        return returnMatrix
    }

    // ************************************************************************* SincMatrixMaths

    actual fun transpose(): SincMatrix {
        val transposedMatrix = nativeHeap.allocArray<DoubleVar>(this.numel())
        vDSP_mtransD(
            this.matrixData.toCValues(),
            1,
            transposedMatrix,
            1,
            this.numCols().toULong(),
            this.numRows().toULong()
        )
        val returnData = transposedMatrix.createCopyArray(this.numel()).asSincMatrix(this.numCols(), this.numRows())
        nativeHeap.free(transposedMatrix)
        return returnData
    }

    actual fun floor(): SincMatrix {
        val outputArray = nativeHeap.allocArray<DoubleVar>(this.numel())
        vvfloor(outputArray, this.matrixData.toCValues(), this.numel().toLong().toCPointer())

        val returnMatrix = outputArray.createCopyArray(this.numel()).asSincMatrix(this.numRows(), this.numCols())

        nativeHeap.free(outputArray)
        return returnMatrix
    }

    actual fun abs(): SincMatrix {
        val outputArray = nativeHeap.allocArray<DoubleVar>(this.numel())
        vvfabs(outputArray, this.matrixData.toCValues(), this.numel().toLong().toCPointer())

        val returnMatrix = outputArray.createCopyArray(this.numel()).asSincMatrix(this.numRows(), this.numCols())

        nativeHeap.free(outputArray)
        return returnMatrix
    }

    // ************************************************************************* SincMatrixStats

    actual fun min(dim: Int): SincMatrix {
        TODO("Not yet implemented")
    }

    actual fun max(dim: Int): SincMatrix {
        TODO("Not yet implemented")
    }

    // ************************************************************************* SincMatrixTrigonometry

    actual fun sin(): SincMatrix {
        val outputArray = nativeHeap.allocArray<DoubleVar>(this.numel())
        vvsin(outputArray, this.matrixData.toCValues(), this.numel().toLong().toCPointer())

        val returnMatrix = outputArray.createCopyArray(this.numel()).asSincMatrix(this.numRows(), this.numCols())

        nativeHeap.free(outputArray)
        return returnMatrix
    }

    actual fun cos(): SincMatrix {
        val outputArray = nativeHeap.allocArray<DoubleVar>(this.numel())
        vvcos(outputArray, this.matrixData.toCValues(), this.numel().toLong().toCPointer())

        val returnMatrix = outputArray.createCopyArray(this.numel()).asSincMatrix(this.numRows(), this.numCols())

        nativeHeap.free(outputArray)
        return returnMatrix
    }

    // ************************************************************************* SincMatrixSignal

    actual fun find(): SincMatrix {
        TODO("Not yet implemented")
    }

    actual fun filter(B: DoubleArray, A: DoubleArray): SincMatrix {
        TODO("Not yet implemented")
    }

    actual fun diffWithWavelet(scale: Double, dt: Double): SincMatrix {
        TODO("Not yet implemented")
    }

    actual companion object {}
}