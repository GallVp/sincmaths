package com.github.gallvp.sincmaths

actual class SincMatrix actual constructor(rowMajArray: DoubleArray, internal var m: Int, internal var n: Int) {
    internal var matrixData: DoubleArray

    init {
        require(rowMajArray.size == m * n) { "length(rowMajArray) should be equal to m*n" }
        matrixData = rowMajArray
    }

    actual override fun toString(): String = this.description

    actual companion object {
        init {
            System.loadLibrary("wavelib")
            System.loadLibrary("tinyexpr")
        }
    }
}

actual val SincMatrix.numRows: Int
    get() = m
actual val SincMatrix.numCols: Int
    get() = n
actual val SincMatrix.numel
    get() = this.matrixData.size

actual val SincMatrix.transpose: SincMatrix
    get() = this.asSimpleMatrix().transpose().asSincMatrix()

actual fun SincMatrix.find(): SincMatrix {
    val array = this.matrixData
    val actualIndices = array.indices.filter { array[it] != 0.0 }
    val actualCount = actualIndices.size

    return if (this.isRow) {
        SincMatrix(
            rowMajArray = actualIndices.map { it.toDouble() + 1.0 }.toDoubleArray(),
            m = 1,
            n = actualCount,
        )
    } else {
        SincMatrix(
            rowMajArray = actualIndices.map { it.toDouble() + 1.0 }.toDoubleArray(),
            m = actualCount,
            n = 1,
        )
    }
}
