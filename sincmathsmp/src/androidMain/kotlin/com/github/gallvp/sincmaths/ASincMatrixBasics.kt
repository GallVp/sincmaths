package com.github.gallvp.sincmaths

actual val SincMatrix.numRows: Int
    get() = m
actual val SincMatrix.numCols: Int
    get() = n
actual val SincMatrix.numel
    get() = this.matrixData.size