import org.ejml.simple.SimpleMatrix

actual class SincMatrix actual constructor(rowMajArray: DoubleArray, private val m: Int, private val n: Int) {

    private val matrixData = rowMajArray

    actual fun numRows(): Int = m

    actual fun numCols(): Int = n

    actual fun asArray(): DoubleArray {
        require(this.isvector()) { "SMError: Matrix is not a vector and conversion is invalid" }
        return this.matrixData
    }

    actual fun transpose(): SincMatrix = this.asSimpleMatrix().transpose().asSincMatrix()

    actual operator fun times(rhs: SincMatrix): SincMatrix {

        val lhsN = this.size().last()
        val rhsM = rhs.size().first()
        require(lhsN == rhsM) { "SMError: Dimension mismatch. In A*B, ncols(A) == nrows(B)" }

        return this.asSimpleMatrix().mult(rhs.asSimpleMatrix()).asSincMatrix()
    }

    actual operator fun times(rhs: Double): SincMatrix = this.asSimpleMatrix().scale(rhs).asSincMatrix()

    actual operator fun plus(rhs: SincMatrix): SincMatrix = this.asSimpleMatrix().plus(rhs.asSimpleMatrix()).asSincMatrix()

    actual operator fun plus(rhs: Double): SincMatrix = this.asSimpleMatrix().plus(rhs).asSincMatrix()

    actual operator fun minus(rhs: SincMatrix): SincMatrix = this.asSimpleMatrix().minus(rhs.asSimpleMatrix()).asSincMatrix()

    actual operator fun minus(rhs: Double): SincMatrix = this.asSimpleMatrix().minus(rhs).asSincMatrix()

    actual operator fun set(mlRow: Int, mlCol: Int, value: Double) {
    }

    private fun asSimpleMatrix() = SimpleMatrix(this.numRows(), this.numCols(), true, this.matrixData)

    actual companion object {
        init {
            System.loadLibrary("wavelib")
            System.loadLibrary("tinyexpr")
        }
    }
}