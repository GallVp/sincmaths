expect class SincMatrix(rowMajArray: DoubleArray, m: Int, n: Int) {

    fun numRows():Int
    fun numCols():Int
    override fun toString(): String

    // ************************************************************************* SincMatrixAsTypes

    fun asArray():DoubleArray
    fun asRowMajorArray():DoubleArray

    // ************************************************************************* SincMatrixSet

    /**
     * Indexing starts at 1, like Octave/MATLAB.
     */
    operator fun set(mlRow: Int, mlCol: Int, value: Double)
    /**
     * Indexing starts at 1, like Octave/MATLAB.
     */
    operator fun set(index: Int, value: Double)

    // ************************************************************************* SincMatrixArithmetic

    operator fun times(rhs: SincMatrix): SincMatrix
    operator fun times(rhs: Double): SincMatrix
    operator fun plus(rhs: SincMatrix): SincMatrix
    operator fun plus(rhs: Double): SincMatrix
    infix fun elDiv(rhs: SincMatrix):SincMatrix
    infix fun elMul(rhs: SincMatrix):SincMatrix
    fun elSum():Double
    infix fun elPow(power:Double):SincMatrix

    // ************************************************************************* SincMatrixMaths

    fun transpose():SincMatrix
    fun floor():SincMatrix
    fun abs():SincMatrix

    // ************************************************************************* SincMatrixSolvers
    fun solve(b:SincMatrix):SincMatrix

    // ************************************************************************* SincMatrixStats

    fun min(dim: Int = 1):SincMatrix
    fun max(dim: Int = 1):SincMatrix

    // ************************************************************************* SincMatrixTrigonometry

    fun sin():SincMatrix
    fun cos():SincMatrix

    // ************************************************************************* SincMatrixSignal

    fun find():SincMatrix

    companion object {}
}