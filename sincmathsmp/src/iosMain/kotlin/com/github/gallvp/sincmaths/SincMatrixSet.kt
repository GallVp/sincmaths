package com.github.gallvp.sincmaths

actual operator fun SincMatrix.set(
    mlRow: Int,
    mlCol: Int,
    value: Double,
) {
    this.matrixData[this.getIndex(mlRow, mlCol) - 1] = value
}

actual operator fun SincMatrix.set(
    index: Int,
    value: Double,
) {
    this.matrixData[index - 1] = value
}

actual fun SincMatrix.removeAt(index: Int) {
    val includedIndices = (0 until this.matrixData.size).toMutableList()
    includedIndices.removeAt(index - 1)
    this.matrixData = this.matrixData.sliceArray(includedIndices)
    m = 1
    n = this.matrixData.size
}

actual fun SincMatrix.removeAt(indices: IntArray) {
    val includedIndices = (0 until this.matrixData.size).toMutableList()
    includedIndices.removeAll(indices.map { it - 1 })
    this.matrixData = this.matrixData.sliceArray(includedIndices)
    m = 1
    n = this.matrixData.size
}
