import kotlinx.cinterop.*
import platform.Accelerate.*
import platform.Accelerate.vDSP_vsaddD

actual class SincMatrix actual constructor(rowMajArray: DoubleArray, private val m: Int, private val n: Int) {

    private val matrixData = rowMajArray
    actual fun numRows(): Int = m
    actual fun numCols(): Int = n

    actual fun asArray(): DoubleArray {
        require(this.isvector()) { "SMError: Matrix is not a vector and conversion is invalid" }
        return this.matrixData
    }

    actual operator fun set(mlRow: Int, mlCol: Int, value: Double) {
        this.matrixData[this.getIndex(mlRow, mlCol) - 1] = value
    }

    actual fun transpose(): SincMatrix {
        val transposedMatrix = nativeHeap.allocArray<DoubleVar>(this.numel())
        vDSP_mtransD(this.matrixData.toCValues(), 1, transposedMatrix, 1, this.numCols().toULong(), this.numRows().toULong())
        val returnData = transposedMatrix.createCopyArray(this.numel()).asSincMatrix(this.numCols(), this.numRows())
        nativeHeap.free(transposedMatrix)
        return returnData
    }

    actual operator fun times(rhs: SincMatrix): SincMatrix {
        val lhsM = this.size().first()
        val lhsN = this.size().last()
        val rhsM = rhs.size().first()
        val rhsN = rhs.size().last()

        require(lhsN == rhsM) { "SMError: Dimension mismatch. In A*B, ncols(A) == nrows(B)" }
        val resultMatrix = nativeHeap.allocArray<DoubleVar>(lhsM * rhsN)
        vDSP_mmulD(this.matrixData.toCValues(), 1, rhs.matrixData.toCValues(), 1, resultMatrix, 1L, lhsM.toULong(), rhsN.toULong(), lhsN.toULong())
        val returnData = resultMatrix.createCopyArray(lhsM*rhsN).asSincMatrix(lhsM, rhsN)
        nativeHeap.free(resultMatrix)
        return returnData
    }

    actual operator fun times(rhs: Double): SincMatrix {
        val resultMatrix = nativeHeap.allocArray<DoubleVar>(this.numel())
        vDSP_vsmulD(this.matrixData.toCValues(), 1L, doubleArrayOf(rhs).toCValues(), resultMatrix, 1L, this.numel().toULong())
        val returnData = resultMatrix.createCopyArray(this.numel()).asSincMatrix(this.numCols(), this.numRows())
        nativeHeap.free(resultMatrix)
        return returnData
    }

    actual operator fun plus(rhs: SincMatrix): SincMatrix {
        val resultMatrix = nativeHeap.allocArray<DoubleVar>(this.numel())
        vDSP_vaddD(this.matrixData.toCValues(), 1L, rhs.matrixData.toCValues(), 1L, resultMatrix, 1L, this.numel().toULong())
        val returnData = resultMatrix.createCopyArray(this.numel()).asSincMatrix(this.numCols(), this.numRows())
        nativeHeap.free(resultMatrix)
        return returnData
    }

    actual operator fun plus(rhs: Double): SincMatrix {
        val resultMatrix = nativeHeap.allocArray<DoubleVar>(this.numel())
        vDSP_vsaddD(this.matrixData.toCValues(), 1L, doubleArrayOf(rhs).toCValues(), resultMatrix, 1L, this.numel().toULong())
        val returnData = resultMatrix.createCopyArray(this.numel()).asSincMatrix(this.numCols(), this.numRows())
        nativeHeap.free(resultMatrix)
        return returnData
    }

    actual companion object {

    }
}