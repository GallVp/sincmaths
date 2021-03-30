import kotlinx.cinterop.*
import platform.Accelerate.vDSP_mtransD

actual class SincMatrix actual constructor(rowMajArray: DoubleArray, private val m: Int, private val n: Int) {

    private val matrixData = rowMajArray

    actual fun numRows(): Int = m

    actual fun numCols(): Int = n

    actual fun asArray(): DoubleArray {
        require(this.isvector()) { "SMError: Matrix is not a vector and conversion is invalid" }
        return this.matrixData
    }

    actual fun transpose(): SincMatrix {
        val transposedMatrix = nativeHeap.allocArray<DoubleVar>(this.numel())
        vDSP_mtransD(this.matrixData.toCValues(), 1, transposedMatrix, 1, this.numCols().toULong(), this.numRows().toULong())

        val returnData = DoubleArray(this.numel()) { Double.NaN}

        for (i in returnData.indices){
            returnData[i] = transposedMatrix[i]
        }
        return SincMatrix(rowMajArray = returnData, m = this.numCols(), n = this.numRows())
    }

    actual companion object
}