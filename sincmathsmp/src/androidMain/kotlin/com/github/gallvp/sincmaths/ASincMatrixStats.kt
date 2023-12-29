package com.github.gallvp.sincmaths

actual fun SincMatrix.min(dim: Int): SincMatrix {
    if (this.isVector) {
        return SincMatrix(
            rowMajArray = doubleArrayOf(this.matrixData.minOrNull()!!),
            m = 1,
            n = 1
        )
    } else {
        return if (dim == 1) {
            val result = SincMatrix.nans(m = 1, n = this.numCols())
            for (i in 1..this.numCols()) {
                result[i] = this.getCol(mlCol = i).matrixData.minOrNull()!!
            }
            result
        } else {
            val result = SincMatrix.nans(m = this.numRows(), n = 1)
            for (i in 1..this.numRows()) {
                result[i] = this.getRow(mlRow = i).matrixData.minOrNull()!!
            }
            result
        }
    }
}

actual fun SincMatrix.max(dim: Int): SincMatrix {
    if (this.isVector) {
        return SincMatrix(
            rowMajArray = doubleArrayOf(this.matrixData.maxOrNull()!!),
            m = 1,
            n = 1
        )
    } else {
        return if (dim == 1) {
            val result = SincMatrix.nans(m = 1, n = this.numCols())
            for (i in 1..this.numCols()) {
                result[i] = this.getCol(mlCol = i).matrixData.maxOrNull()!!
            }
            result
        } else {
            val result = SincMatrix.nans(m = this.numRows(), n = 1)
            for (i in 1..this.numRows()) {
                result[i] = this.getRow(mlRow = i).matrixData.maxOrNull()!!
            }
            result
        }
    }
}