package com.github.gallvp.sincmaths

actual fun SincMatrix.numRows(): Int = m
actual fun SincMatrix.numCols(): Int = n
actual val SincMatrix.numel
    get() = this.matrixData.size