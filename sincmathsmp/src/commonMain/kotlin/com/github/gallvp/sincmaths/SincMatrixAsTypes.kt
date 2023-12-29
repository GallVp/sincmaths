package com.github.gallvp.sincmaths


expect fun SincMatrix.asArray(): DoubleArray
expect fun SincMatrix.asRowMajorArray(): DoubleArray

fun SincMatrix.asIntArray(): IntArray {

    if(this.isEmpty()) {
        return intArrayOf()
    }

    require(this.isVector) { "Matrix is not a vector and conversion is invalid" }

    return this.asRowMajorArray().map {
        it.toInt()
    }.toIntArray()
}

fun SincMatrix.asScalar(): Double {
    require(this.isScalar) { "Matrix is not a scalar and conversion is invalid" }

    return this[1]
}

fun SincMatrix.asBoolean(): Boolean {
    require(this.isScalar) { "Matrix is not a scalar and conversion is invalid" }

    return this[1] != 0.0
}

/**
 * Create a row vector by indexing elements row by row
 */
fun SincMatrix.asRowVector(): SincMatrix =
    SincMatrix(rowMajArray = this.asRowMajorArray(), m = 1, n = this.numel)

fun SincMatrix.asBoolArray(): BooleanArray {

    if(this.isEmpty()) {
        return booleanArrayOf()
    }

    require(this.isVector) { "Matrix is not a vector and conversion is invalid" }

    return this.asRowMajorArray().map {
        it != 0.0
    }.toBooleanArray()
}