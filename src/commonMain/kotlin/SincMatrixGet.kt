/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
fun SincMatrix.getCols(mlCols: IntArray): SincMatrix =
    this.get(mlRows = this.rowIndices, mlCols = mlCols)

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
operator fun SincMatrix.get(mlRows: IntArray, mlCols: IntArray): SincMatrix {
    val indices = this.indexBuilder(mlRows = mlRows, mlCols = mlCols)
    return SincMatrix(
        rowMajArray = this[indices].asArray(),
        m = mlRows.size,
        n = mlCols.size
    )
}

/**
 * Indexing starts at 1, like Octave/MATLAB.
 * @return A column vector.
 */
operator fun SincMatrix.get(indices: IntArray): SincMatrix {
    val numE = indices.size
    val subset = this.asArray().slice(indices.map { it - 1 }.toList()).toDoubleArray()
    return SincMatrix(subset, m = numE, n = 1)
}

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
fun SincMatrix.getRows(mlRows: IntArray): SincMatrix = this.get(mlRows = mlRows, mlCols = this.colIndices)

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
fun SincMatrix.getRow(mlRow: Int): SincMatrix =
    this.getRows(mlRows = intArrayOf(mlRow))

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
internal fun SincMatrix.indexBuilder(mlRows: IntArray, mlCols: IntArray): IntArray {

    val vectIndices: ArrayList<Int> = ArrayList()
    vectIndices.ensureCapacity(mlRows.size * mlCols.size)
    for (row in mlRows) {
        val numElementsBehind: Int = (row - 1) * this.numCols()
        vectIndices.addAll(mlCols.map { it + numElementsBehind })
    }
    return vectIndices.toIntArray()
}

internal fun SincMatrix.getIndex(mlRow: Int, mlCol: Int): Int {
    val numElementsBehind: Int = (mlRow - 1) * this.numCols()
    return mlCol + numElementsBehind
}