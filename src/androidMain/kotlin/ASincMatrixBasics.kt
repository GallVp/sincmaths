package sincmaths

actual fun SincMatrix.numRows(): Int = m
actual fun SincMatrix.numCols(): Int = n
actual fun SincMatrix.numel() = this.matrixData.size