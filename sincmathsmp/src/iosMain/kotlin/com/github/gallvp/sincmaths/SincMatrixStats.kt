package com.github.gallvp.sincmaths

actual fun SincMatrix.min(dim: Int): SincMatrix {
    if (this.isVector) {
        return minOfVectorElements(this.matrixData).asSincMatrix()
    } else {
        return if (dim == 1) {
            val result = createZerosVector(this.numCols())
            for (i in 1..result.size) {
                val column = this.getCol(i).asArray()
                result[i - 1] = minOfVectorElements(column)
            }
            SincMatrix(rowMajArray = result, m = 1, n = this.numCols())
        } else {
            val result = createZerosVector(this.numRows())
            for (i in 1..result.size) {
                val row = this.getRow(i).asArray()
                result[i - 1] = minOfVectorElements(row)
            }
            SincMatrix(rowMajArray = result, m = this.numRows(), n = 1)
        }
    }
}

actual fun SincMatrix.max(dim: Int): SincMatrix {
    if (this.isVector) {
        return maxOfVectorElements(this.matrixData).asSincMatrix()
    } else {
        return if (dim == 1) {
            val result = createZerosVector(this.numCols())
            for (i in 1..result.size) {
                val column = this.getCol(i).asArray()
                result[i - 1] = maxOfVectorElements(column)
            }
            SincMatrix(rowMajArray = result, m = 1, n = this.numCols())
        } else {
            val result = createZerosVector(this.numRows())
            for (i in 1..result.size) {
                val row = this.getRow(i).asArray()
                result[i - 1] = maxOfVectorElements(row)
            }
            SincMatrix(rowMajArray = result, m = this.numRows(), n = 1)
        }
    }
}