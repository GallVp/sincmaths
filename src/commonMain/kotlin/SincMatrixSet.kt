package sincmaths.sincmatrix

import sincmaths.SincMatrix

/**
 * Indexing with a logical vector containing 0's and 1's.
 */
fun SincMatrix.setWithLV(logicalVector: SincMatrix, values: DoubleArray) {
    require(this.numel() == logicalVector.numel()) {
        "SMError: numel(logicalVect) == numel(this matrix) is violated"
    }
    val indicesAsArray = logicalVector.find().asIntArray()
    if (values.isEmpty()) {
        this.removeAt(indicesAsArray)
    } else {
        this.setWithIndices(indicesAsArray, values)
    }
}

/**
 * Indexing with a logical vector containing 0's and 1's.
 */
fun SincMatrix.setWithLV(logicalVector: SincMatrix, value: Double) {
    val indicesAsArray = logicalVector.find().asIntArray()
    for (i in indicesAsArray.indices) {
        this[indicesAsArray[i]] = value
    }
}

/**
 * Indexing with a logical vector containing 0's and 1's.
 */
fun SincMatrix.setWithLV(logicalVector: SincMatrix, values: SincMatrix) {
    val valuesArray = values.asArray()
    this.setWithLV(logicalVector, valuesArray)
}

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
fun SincMatrix.setWithIndices(indices: IntArray, values: DoubleArray) {
    if (values.isEmpty()) {
        this.removeAt(indices)
    } else {
        require(indices.count() == values.count()) {
            "SMError: numel(indices) is not equal to numel(values)"
        }
        for (i in indices.indices) {
            this[indices[i]] = values[i]
        }
    }
}

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
fun SincMatrix.setWithIndices(indices: IntArray, value: Double) {
    this.setWithIndices(indices, DoubleArray(indices.count()) { value })
}

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
fun SincMatrix.setWithIndices(indices: SincMatrix, value: Double) {
    val indicesAsArray = indices.asIntArray()
    this.setWithIndices(indicesAsArray, value)
}

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
fun SincMatrix.setWithIndices(indices: SincMatrix, values: DoubleArray) {
    val indicesAsArray = indices.asIntArray()
    this.setWithIndices(indicesAsArray, values)
}

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
fun SincMatrix.setWithIndices(indices: SincMatrix, values: SincMatrix) {
    val valuesArray = values.asArray()
    this.setWithIndices(indices, valuesArray)
}

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
fun SincMatrix.setRow(mlRow:Int, values: DoubleArray) {
    val indices = this.indexBuilder(intArrayOf(mlRow), this.colIndices)
    this.setWithIndices(indices, values)
}

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
fun SincMatrix.setCol(mlCol:Int, values: DoubleArray) {
    val indices = this.indexBuilder(this.rowIndices, intArrayOf(mlCol))
    this.setWithIndices(indices, values)
}

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
fun SincMatrix.setRow(mlRow:Int, values: SincMatrix) {
    this.setRow(mlRow, values.asArray())
}

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
fun SincMatrix.setCol(mlCol:Int, values: SincMatrix) {
    this.setCol(mlCol, values.asArray())
}

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
fun SincMatrix.setRow(mlRow:Int, value: Double) {
    this.setRow(mlRow, DoubleArray(this.numCols()){value})
}

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
fun SincMatrix.setCol(mlCol:Int, value: Double) {
    this.setCol(mlCol, DoubleArray(this.numRows()){value})
}