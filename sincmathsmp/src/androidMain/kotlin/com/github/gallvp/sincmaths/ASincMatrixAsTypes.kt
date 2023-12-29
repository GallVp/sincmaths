package com.github.gallvp.sincmaths

import org.ejml.simple.SimpleMatrix

actual fun SincMatrix.asArray(): DoubleArray {

    if(this.isempty()) {
        return doubleArrayOf()
    }

    require(this.isVector) { "Matrix is not a vector and conversion is invalid" }
    return this.matrixData
}

actual fun SincMatrix.asRowMajorArray() = this.matrixData
internal fun SincMatrix.asSimpleMatrix() = SimpleMatrix(this.numRows(), this.numCols(), true,
    *this.matrixData)