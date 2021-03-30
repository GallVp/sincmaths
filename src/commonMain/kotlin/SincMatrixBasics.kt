fun SincMatrix.length(): Int =
    this.size().maxOrNull() ?: 0

fun SincMatrix.size(): List<Int> =
    listOf(this.numRows(), this.numCols())

fun SincMatrix.numel(): Int =
    this.numRows() * this.numCols()

fun SincMatrix.isrow(): Boolean {
    val matSize = this.size()
    return matSize.first() == 1
}

fun SincMatrix.iscolumn(): Boolean {
    val matSize = this.size()
    return matSize.last() == 1
}

fun SincMatrix.isvector(): Boolean {
    val matSize = this.size()
    return matSize.first() == 1 || matSize.last() == 1
}

fun SincMatrix.isscalar(): Boolean {
    return this.isvector() && this.numel() <= 1
}

fun SincMatrix.isempty(): Boolean = this.numel() == 0

val SincMatrix.rowIndices: IntArray
    get() {
        return IntArray(this.numRows()) { it + 1 }
    }