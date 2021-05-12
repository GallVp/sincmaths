package sincmaths.sincmatrix

import sincmaths.SincMatrix

fun SincMatrix.asIntArray(): IntArray {

    if(this.isempty()) {
        return intArrayOf()
    }

    require(this.isvector()) { "SMError: Matrix is not a vector and conversion is invalid" }

    return this.asRowMajorArray().map {
        it.toInt()
    }.toIntArray()
}

fun SincMatrix.asScalar(): Double {
    require(this.isscalar()) { "SMError: Matrix is not a scalar and conversion is invalid" }

    return this[1]
}

fun SincMatrix.asBoolean(): Boolean {
    require(this.isscalar()) { "SMError: Matrix is not a scalar and conversion is invalid" }

    return this[1] != 0.0
}

/**
 * Create a row vector by indexing elements row by row
 */
fun SincMatrix.asRowVector(): SincMatrix =
    SincMatrix(rowMajArray = this.asRowMajorArray(), m = 1, n = this.numel())

fun SincMatrix.asBoolArray(): BooleanArray {

    if(this.isempty()) {
        return booleanArrayOf()
    }

    require(this.isvector()) { "SMError: Matrix is not a vector and conversion is invalid" }

    return this.asRowMajorArray().map {
        it != 0.0
    }.toBooleanArray()
}