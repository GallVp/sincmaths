/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
fun SincMatrix.setWithLV(logicalVect: SincMatrix, values: DoubleArray) : SincMatrix {
    require (this.numel() == logicalVect.numel()) {
        "SMError: numel(logicalVect) == numel(this matrix) is violated"
    }
    val indicesAsArray = logicalVect.find().asIntArray()
    return if ((values.isEmpty())) {
        val includedIndices = logicalVect.equalsTo(0.0)
        this[includedIndices]
    } else {
        require (indicesAsArray.count() == values.count()) {
            "SMError: Number of elements selected by logicalVect is not equal to length of values array"
        }
        val mutatedData = this.asRowMajorArray().copyOf()
        for (i in indicesAsArray.indices) {
            mutatedData[indicesAsArray[i] - 1] = values[i - 1]
        }
        SincMatrix(rowMajArray = mutatedData, m = this.numRows(), n = this.numCols())
    }
}

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
fun SincMatrix.setWithLV(logicalVect: SincMatrix, value: Double) : SincMatrix {
    val indicesAsArray = logicalVect.find().asIntArray()
    val mutatedData = this.asRowMajorArray().copyOf()
    for (i in indicesAsArray.indices) {
        mutatedData[indicesAsArray[i] - 1] = value
    }
    return SincMatrix(rowMajArray = mutatedData, m = this.numRows(), n = this.numCols())
}

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
fun SincMatrix.set2(indices: IntArray, values: DoubleArray) : SincMatrix {
    return if ((values.isEmpty())) {
        val matIndices = SincMatrix.ones(m = this.numel(), n = 1)
        val selectorMat = matIndices.set2(indices, 0.0)
        this[selectorMat]
    } else {
        require (indices.count() == values.count()) {
            "SMError: numel(indices) is not equal to numel(values)"
        }
        val mutatedData = this.asRowMajorArray().copyOf()
        for (i in indices.indices) {
            mutatedData[indices[i] - 1] = values[i]
        }
        SincMatrix(rowMajArray = mutatedData, m = this.numRows(), n = this.numCols())
    }
}

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
fun SincMatrix.setWithLV(logicalVect: SincMatrix, values: SincMatrix) : SincMatrix {
    val valuesArray = values.asArray()
    return this.setWithLV(logicalVect = logicalVect, values = valuesArray)
}

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
fun SincMatrix.set2(index: Int, value: Double) : SincMatrix =
    this.set2(indices = intArrayOf(index), values = doubleArrayOf(value))

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
fun SincMatrix.set2(indices: IntArray, value: Double) : SincMatrix =
    this.set2(indices, DoubleArray(indices.count()) { value })

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
fun SincMatrix.set2(indices: SincMatrix, value: Double) : SincMatrix {
    val indicesAsArray = indices.asIntArray()
    return this.set2(indices = indicesAsArray, value = value)
}

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
fun SincMatrix.set2(indices: SincMatrix, values: DoubleArray) : SincMatrix {
    val indicesAsArray = indices.asIntArray()
    return this.set2(indices = indicesAsArray, values = values)
}

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
fun SincMatrix.set2(indices: SincMatrix, values: SincMatrix) : SincMatrix {
    val valuesArray = values.asArray()
    return this.set2(indices = indices, values = valuesArray)
}