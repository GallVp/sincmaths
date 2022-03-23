package sincmaths

actual fun SincMatrix.transpose(): SincMatrix =
    transposeOfRowMajorMatrix(this.matrixData, this.numRows(), this.numCols()).asSincMatrix(
        this.numCols(),
        this.numRows()
    )

actual fun SincMatrix.floor(): SincMatrix =
    floorOfElementsOfVector(this.matrixData).asSincMatrix(this.numRows(), this.numCols())

actual fun SincMatrix.abs(): SincMatrix =
    absOfElementsOfVector(this.matrixData).asSincMatrix(this.numRows(), this.numCols())

actual fun SincMatrix.find(): SincMatrix {
    val actualIndices = findNonZeroIndices(this.matrixData, false)
    val actualCount = actualIndices.size
    if (actualCount < 1) {
        return SincMatrix(doubleArrayOf(), 0, 0)
    }
    return if (this.isrow()) {
        SincMatrix(rowMajArray = actualIndices, m = 1, n = actualCount)
    } else {
        SincMatrix(rowMajArray = actualIndices, m = actualCount, n = 1)
    }
}