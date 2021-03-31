val SincMatrix.description: String
get() {
    val dispString = StringBuilder()
    if (this.isempty()) {
        dispString.append("Empty matrix")
    } else {
        dispString.append("")
        for (ithRow in 1 .. this.numRows()) {
            val rowString = this.getRow(ithRow).asRowMajorArray().contentToString().drop(1).dropLast(1)
            dispString.append(rowString + "\n")
        }
    }
    return dispString.toString()
}

fun SincMatrix.disp() = this.description

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

val SincMatrix.colIndices: IntArray
    get() {
        return IntArray(this.numCols()) { it + 1 }
    }

val SincMatrix.rowIndices: IntArray
    get() {
        return IntArray(this.numRows()) { it + 1 }
    }

val SincMatrix.indices: IntArray
    get() {
        return IntArray(this.numel()) { it + 1 }
    }

val SincMatrix.colIndicesRange: IntRange
    get() {
        return 1..this.numCols()
    }

val SincMatrix.rowIndicesRange: IntRange
    get() {
        return 1..this.numRows()
    }

val SincMatrix.indicesRange: IntRange
    get() {
        return 1..this.numel()
    }

fun SincMatrix.copyOf():SincMatrix = this.asRowMajorArray().copyOf().asSincMatrix(this.numRows(), this.numCols())