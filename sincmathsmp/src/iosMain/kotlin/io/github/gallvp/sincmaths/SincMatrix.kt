package io.github.gallvp.sincmaths

actual class SincMatrix actual constructor(
    rowMajArray: DoubleArray,
    internal var m: Int,
    internal var n: Int,
) {
    internal var matrixData: DoubleArray

    init {
        require(rowMajArray.size == m * n) { "length(rowMajArray) should be equal to m*n" }
        matrixData = rowMajArray
    }

    actual override fun toString(): String = this.description

    actual companion object
}

actual val SincMatrix.numRows
    get() = m
actual val SincMatrix.numCols: Int
    get() = n
actual val SincMatrix.numel
    get() = this.matrixData.size

actual val SincMatrix.transpose: SincMatrix
    get() =
        transposeOfRowMajorMatrix(this.matrixData, this.numRows, this.numCols).asSincMatrix(
            this.numCols,
            this.numRows,
        )

actual fun SincMatrix.find(): SincMatrix {
    val actualIndices = findNonZeroIndices(this.matrixData, false)
    val actualCount = actualIndices.size
    if (actualCount < 1) {
        return SincMatrix(doubleArrayOf(), 0, 0)
    }
    return if (this.isRow) {
        SincMatrix(rowMajArray = actualIndices, m = 1, n = actualCount)
    } else {
        SincMatrix(rowMajArray = actualIndices, m = actualCount, n = 1)
    }
}
