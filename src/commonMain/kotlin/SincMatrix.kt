expect class SincMatrix(rowMajArray: DoubleArray, m: Int, n: Int) {

    fun numRows():Int
    fun numCols():Int

    fun asArray():DoubleArray

    /**
     * Indexing starts at 1, like Octave/MATLAB.
     */
    operator fun set(mlRow: Int, mlCol: Int, value: Double)

    fun transpose():SincMatrix

    operator fun times(rhs: SincMatrix): SincMatrix
    operator fun times(rhs: Double): SincMatrix
    operator fun plus(rhs: SincMatrix): SincMatrix
    operator fun plus(rhs: Double): SincMatrix
    operator fun minus(rhs: SincMatrix): SincMatrix
    operator fun minus(rhs: Double): SincMatrix

    companion object {}
}