package com.github.gallvp.sincmaths

actual fun SincMatrix.sin(): SincMatrix =
    SincMatrix(
        this.matrixData.map {
            kotlin.math.sin(it)
        }.toDoubleArray(),
        numRows,
        numCols,
    )

actual fun SincMatrix.cos(): SincMatrix =
    SincMatrix(
        this.matrixData.map {
            kotlin.math.cos(it)
        }.toDoubleArray(),
        numRows,
        numCols,
    )
