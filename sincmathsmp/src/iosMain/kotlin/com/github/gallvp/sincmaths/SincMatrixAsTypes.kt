package com.github.gallvp.sincmaths

actual fun SincMatrix.asArray(): DoubleArray {

    if(this.isempty()) {
        return doubleArrayOf()
    }

    require(this.isVector) { "Matrix is not a vector and conversion is invalid" }
    return this.matrixData
}

actual fun SincMatrix.asRowMajorArray(): DoubleArray = this.matrixData