package com.github.gallvp.sincmaths

actual fun SincMatrix.sin(): SincMatrix =
    sinOfElementsOfVector(this.matrixData).asSincMatrix(this.numRows(), this.numCols())

actual fun SincMatrix.cos(): SincMatrix =
    cosOfElementsOfVector(this.matrixData).asSincMatrix(this.numRows(), this.numCols())