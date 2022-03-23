package sincmaths

import kotlin.math.pow

val SincMatrix.description: String
    get() {
        val dispString = StringBuilder()
        if (this.isempty()) {
            dispString.append("Empty matrix")
        } else {
            dispString.append("")
            for (ithRow in 1..this.numRows()) {
                val rowString = this.getRow(ithRow).asRowMajorArray().contentToString().drop(1).dropLast(1)
                dispString.append(rowString + "\n")
            }
        }
        return dispString.toString()
    }

fun SincMatrix.disp() = this.description

fun SincMatrix.length(): Int {
    return if (numel() == 0) {
        0
    } else {
        this.size().maxOrNull() ?: 0
    }
}

fun SincMatrix.size(): List<Int> =
    listOf(this.numRows(), this.numCols())

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
    return (this.numRows() * this.numCols()) == 1
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

fun SincMatrix.copyOf(): SincMatrix = this.asRowMajorArray().copyOf().asSincMatrix(this.numRows(), this.numCols())

fun SincMatrix.circshift(n: Int): SincMatrix {

    require(this.isvector()) {
        "SMError: This function works only for vectors"
    }

    return if (n == 0) {
        this
    } else if (n > 0) {

        val normalisedN = n % this.length()

        val matrixData = this.asRowMajorArray()
        val mainSegmentIndices = 0 until this.length() - normalisedN
        val mainSegment = matrixData.slice(mainSegmentIndices)

        val rotatedSegmentIndices = this.length() - normalisedN until this.length()
        val rotatedSegment = matrixData.slice(rotatedSegmentIndices)

        val rotatedData = rotatedSegment + mainSegment
        rotatedData.asSincMatrix(this.numRows(), this.numCols())
    } else {
        val nPos = kotlin.math.abs(n)
        val normalisedN = nPos % this.length()
        val matrixData = this.asRowMajorArray()
        val mainSegmentIndices = normalisedN until this.length()
        val mainSegment = matrixData.slice(mainSegmentIndices)

        val rotatedSegmentIndices = 0 until normalisedN
        val rotatedSegment = matrixData.slice(rotatedSegmentIndices)

        val rotatedData = mainSegment + rotatedSegment
        rotatedData.asSincMatrix(this.numRows(), this.numCols())
    }
}

fun SincMatrix.cat(dim: Int = 1, vararg matrices: SincMatrix): SincMatrix {
    return if (dim == 1) {
        matrices.map {
            require(this.numCols() == it.numCols()) { "SMError: For dim = 1, numCols(A) = numCols(B) is violated." }
        }

        (this.asRowMajorArray() + matrices.flatMap {
            it.asRowMajorArray().toList()
        }).asSincMatrix(this.numRows() + matrices.sumOf { it.numRows() }, this.numCols())
    } else {
        matrices.map {
            require(this.numRows() == it.numRows()) { "SMError: For dim = 2, numRows(A) = numRows(B) is violated." }
        }

        (this.t.asRowMajorArray() + matrices.flatMap {
            it.t.asRowMajorArray().toList()
        }).asSincMatrix(this.numCols() + matrices.sumOf { it.numCols() }, this.numRows()).t
    }
}

/**
 * As SincMatrix is row-major, reshape picks data from rows first and also fills rows first.
 * Thus, a reshape such as reshape(A, 4, 3) on SincMatrix is equal to reshape(A', 3, 4)' on MATLAB/Octave.
 */
fun SincMatrix.reshape(m: Int, n: Int): SincMatrix = this.asRowMajorArray().asSincMatrix(m, n)

fun SincMatrix.repmat(m: Int, n: Int) : SincMatrix {

    if(m == 0 || n == 0) {
        return SincMatrix(doubleArrayOf(), 0, 0)
    }

    val rowRepeatedMat = if(m == 1) {
        this
    } else {
        (1..m).map {
            this.asRowMajorArray().copyOf()
        }.flatMap {
            it.toList()
        }.asSincMatrix(this.numRows()*m, this.numCols())
    }

    return if(n == 1) {
        rowRepeatedMat
    } else {
        rowRepeatedMat.cat(2, *Array(n-1){rowRepeatedMat.copyOf()})
    }
}

fun SincMatrix.round(n: Int):SincMatrix = map {
    kotlin.math.round(it * 10.0.pow(n)) / 10.0.pow(n)
}