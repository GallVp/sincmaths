package io.github.gallvp.sincmaths

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
fun SincMatrix.getWithLV(logicalVect: SincMatrix): SincMatrix {
    if (this.isEmpty() || logicalVect.isEmpty()) {
        return emptySincMatrix()
    }

    require(this.isVector == logicalVect.isVector) {
        "In indexing by a logical vector, numel(self) == numel(logicalVect)"
    }

    return this.get(indices = logicalVect.find().asIntArray())
}

/**
 * Supported Octave scripts: 1:5,4:7 and 1:5,4 and 1:5,: and 1,4:7 and :,4:7 and : and
 * 1:end,end:end-1 and 1:5 and 1:end-1 and end:end-1
 *
 * Indexing starts at 1, like Octave/MATLAB.
 */
operator fun SincMatrix.get(mlScript: String): SincMatrix =
    if (mlScript.contains(",")) {
        val stringLiterals = mlScript.split(",")

        require(stringLiterals.count() == 2) { "With comma usage, mlScript must contain both row and column indices" }

        val rowsScript = stringLiterals.first().replace("end", this.numRows.toString(), false)
        val colsScript = stringLiterals.last().replace("end", this.numCols.toString(), false)
        val mlRows =
            if (rowsScript == ":") {
                this.rowIndices
            } else {
                SincMatrix.createIndexRange(mlScript = rowsScript)
            }
        val mlCols =
            if (colsScript == ":") {
                this.colIndices
            } else {
                SincMatrix.createIndexRange(mlScript = colsScript)
            }
        this.get(mlRows = mlRows, mlCols = mlCols)
    } else {
        val numericScript = mlScript.replace("end", this.numel.toString(), false)
        val indices =
            if (numericScript == ":") {
                this.indices
            } else {
                SincMatrix.createIndexRange(mlScript = numericScript)
            }
        this[indices]
    }

/**
 * kotlin-way of doing get(mlScript: String).
 */
fun SincMatrix.get(selector: (endR: Int, endC: Int, allR: IntRange, allC: IntRange) -> Pair<IntRange, IntRange>): SincMatrix {
    val indices = selector(this.numRows, this.numCols, this.rowIndicesRange, this.colIndicesRange)
    return this[indices.first, indices.second]
}

/**
 * kotlin-way of doing get(mlScript: String).
 */
fun SincMatrix.get(selector: (end: Int, all: IntRange) -> IntRange): SincMatrix {
    val indices = selector(this.numel, this.indicesRange)
    return this[indices]
}

/**
 * Indexing starts at 1, like Octave/MATLAB.
 * @return A column vector.
 */
operator fun SincMatrix.get(indices: IntArray): SincMatrix {
    val numE = indices.size
    val subset = this.asRowMajorArray().slice(indices.map { it - 1 }.toList()).toDoubleArray()
    return SincMatrix(subset, m = numE, n = 1)
}

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
fun SincMatrix.getCols(mlCols: IntArray): SincMatrix = this.get(mlRows = this.rowIndices, mlCols = mlCols)

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
fun SincMatrix.getCols(mlCols: IntRange): SincMatrix = this.get(mlRows = this.rowIndicesRange, mlCols = mlCols)

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
fun SincMatrix.getCol(mlCol: Int): SincMatrix = this.getCols(mlCols = intArrayOf(mlCol))

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
operator fun SincMatrix.get(
    mlRows: IntArray,
    mlCols: IntArray,
): SincMatrix {
    val indices = this.indexBuilder(mlRows = mlRows, mlCols = mlCols)
    return SincMatrix(
        rowMajArray = this[indices].asRowMajorArray(),
        m = mlRows.size,
        n = mlCols.size,
    )
}

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
operator fun SincMatrix.get(
    mlRows: IntRange,
    mlCols: IntRange,
): SincMatrix {
    val indices = this.indexBuilder(mlRows = mlRows, mlCols = mlCols)
    return SincMatrix(
        rowMajArray = this[indices].asRowMajorArray(),
        m = mlRows.count(),
        n = mlCols.count(),
    )
}

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
operator fun SincMatrix.get(index: Int): Double = this.asRowMajorArray()[index - 1]

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
operator fun SincMatrix.get(
    mlRow: Int,
    mlCol: Int,
): Double = this[getIndex(mlRow, mlCol)]

/**
 * Indexing starts at 1, like Octave/MATLAB.
 * @return A column vector.
 */
operator fun SincMatrix.get(indices: IntRange): SincMatrix {
    val numE = indices.count()
    val subset = this.asRowMajorArray().slice(indices.map { it - 1 }.toList()).toDoubleArray()
    return SincMatrix(subset, m = numE, n = 1)
}

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
fun SincMatrix.getRows(mlRows: IntArray): SincMatrix = this.get(mlRows = mlRows, mlCols = this.colIndices)

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
fun SincMatrix.getRows(mlRows: IntRange): SincMatrix = this.get(mlRows = mlRows, mlCols = this.colIndicesRange)

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
fun SincMatrix.getRow(mlRow: Int): SincMatrix = this.getRows(mlRows = intArrayOf(mlRow))

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
internal fun SincMatrix.indexBuilder(
    mlRows: IntArray,
    mlCols: IntArray,
): IntArray {
    val vectIndices: ArrayList<Int> = ArrayList()
    vectIndices.ensureCapacity(mlRows.size * mlCols.size)
    for (row in mlRows) {
        val numElementsBehind: Int = (row - 1) * this.numCols
        vectIndices.addAll(mlCols.map { it + numElementsBehind })
    }
    return vectIndices.toIntArray()
}

/**
 * Indexing starts at 1, like Octave/MATLAB.
 */
internal fun SincMatrix.indexBuilder(
    mlRows: IntRange,
    mlCols: IntRange,
): IntArray {
    val vectIndices: ArrayList<Int> = ArrayList()
    vectIndices.ensureCapacity(mlRows.count() * mlCols.count())
    for (row in mlRows) {
        val numElementsBehind: Int = (row - 1) * this.numCols
        vectIndices.addAll(mlCols.map { it + numElementsBehind })
    }
    return vectIndices.toIntArray()
}

internal fun SincMatrix.getIndex(
    mlRow: Int,
    mlCol: Int,
): Int {
    val numElementsBehind: Int = (mlRow - 1) * this.numCols
    return mlCol + numElementsBehind
}

internal fun SincMatrix.Companion.createIndexRange(mlScript: String): IntArray {
    val literals = mlScript.split(":")
    if (literals.size == 1) {
        return intArrayOf(literals[0].trim().toInt())
    }
    val startPoint: Int?
    val change: Int?
    val endPoint: Int?

    require(literals.count() >= 2) { "Incomplete mlScript" }

    if (literals.size == 3) {
        // 1:2:7 type
        startPoint = parseToInt(literals[0])
        change = parseToInt(literals[1])
        endPoint = parseToInt(literals[2])
    } else {
        // 2:5 type
        startPoint = parseToInt(literals[0])
        endPoint = parseToInt(literals[1])
        change = 1
    }

    require(startPoint != null && change != null && endPoint != null) { "Invalid mlScript" }

    if (change > 0 && (startPoint > endPoint)) {
        return IntArray(0)
    }

    return if (change > 0) {
        generateSequence(startPoint) { previous ->
            val next = previous + change
            if (next > endPoint) null else next
        }.toList().toIntArray()
    } else {
        generateSequence(startPoint) { previous ->
            val next = previous + change
            if (next < endPoint) null else next
        }.toList().toIntArray()
    }
}

val SincMatrix.first: Double
    get() = this[1]

/**
 * Returns a value if the matrix is scalar otherwise throws an error.
 */
val SincMatrix.scalar: Double
    get() = this.asScalar()

/**
 * Returns a value if the matrix is scalar otherwise throws an error.
 */
val SincMatrix.boolean: Boolean
    get() = this.asBoolean()

val SincMatrix.absoluteValue: SincMatrix
    get() = this.abs()
