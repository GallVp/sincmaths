package sincmaths

actual fun SincMatrix.transpose(): SincMatrix = this.asSimpleMatrix().transpose().asSincMatrix()
actual fun SincMatrix.floor(): SincMatrix =
    SincMatrix(this.matrixData.map {
        kotlin.math.floor(it)
    }.toDoubleArray(), numRows(), numCols())

actual fun SincMatrix.abs(): SincMatrix =
    SincMatrix(this.matrixData.map { kotlin.math.abs(it) }.toDoubleArray(), numRows(), numCols())

actual fun SincMatrix.find(): SincMatrix {
    val array = this.matrixData
    val actualIndices = array.indices.filter { array[it] != 0.0 }
    val actualCount = actualIndices.size

    return if (this.isrow()) {
        SincMatrix(
            rowMajArray = actualIndices.map { it.toDouble() + 1.0 }.toDoubleArray(),
            m = 1,
            n = actualCount
        )
    } else {
        SincMatrix(
            rowMajArray = actualIndices.map { it.toDouble() + 1.0 }.toDoubleArray(),
            m = actualCount,
            n = 1
        )
    }
}