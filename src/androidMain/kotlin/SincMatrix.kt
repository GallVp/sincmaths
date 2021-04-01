import org.ejml.simple.SimpleMatrix
import kotlin.math.floor

actual class SincMatrix actual constructor(rowMajArray: DoubleArray, private val m: Int, private val n: Int) {

    private val matrixData = rowMajArray
    actual fun numRows(): Int = m
    actual fun numCols(): Int = n


    actual fun asArray(): DoubleArray {
        require(this.isvector()) { "SMError: Matrix is not a vector and conversion is invalid" }
        return this.matrixData
    }

    actual fun asRowMajorArray() = this.matrixData
    internal fun asSimpleMatrix() = SimpleMatrix(this.numRows(), this.numCols(), true, this.matrixData)

    actual operator fun set(mlRow: Int, mlCol: Int, value: Double) {
        this.matrixData[this.getIndex(mlRow, mlCol) - 1] = value
    }

    actual operator fun set(index: Int, value: Double) {
        this.matrixData[index - 1] = value
    }

    actual fun transpose(): SincMatrix = this.asSimpleMatrix().transpose().asSincMatrix()

    actual operator fun times(rhs: SincMatrix): SincMatrix {

        val lhsN = this.size().last()
        val rhsM = rhs.size().first()
        require(lhsN == rhsM) { "SMError: Dimension mismatch. In A*B, ncols(A) == nrows(B)" }

        return this.asSimpleMatrix().mult(rhs.asSimpleMatrix()).asSincMatrix()
    }

    actual operator fun times(rhs: Double): SincMatrix = this.asSimpleMatrix().scale(rhs).asSincMatrix()

    actual operator fun plus(rhs: SincMatrix): SincMatrix =
        this.asSimpleMatrix().plus(rhs.asSimpleMatrix()).asSincMatrix()

    actual operator fun plus(rhs: Double): SincMatrix = this.asSimpleMatrix().plus(rhs).asSincMatrix()

    actual fun cross(ontoVector: SincMatrix): SincMatrix {
        require(
            this.isvector() && ontoVector.isvector() &&
                    this.numel() == 3 && ontoVector.numel() == 3
        ) {
            "SMError: This function works only for vectors of length 3"
        }

        val c1 = this[2] * ontoVector[3] - this[3] * ontoVector[2]
        val c2 = this[3] * ontoVector[1] - this[1] * ontoVector[3]
        val c3 = this[1] * ontoVector[2] - this[2] * ontoVector[1]

        return if (this.iscolumn() && ontoVector.iscolumn()) {
            doubleArrayOf(c1, c2, c3).asSincMatrix(m = 3, n = 1)
        } else {
            doubleArrayOf(c1, c2, c3).asSincMatrix(m = 1, n = 3)
        }
    }

    actual fun dot(rhs: SincMatrix): Double = this.asSimpleMatrix().dot(rhs.asSimpleMatrix())

    actual infix fun elMul(rhs: SincMatrix): SincMatrix =
        this.asSimpleMatrix().elementMult(rhs.asSimpleMatrix()).asSincMatrix()

    actual infix fun elDiv(rhs: SincMatrix): SincMatrix =
        this.asSimpleMatrix().elementDiv(rhs.asSimpleMatrix()).asSincMatrix()

    actual fun elSum(): Double = this.asSimpleMatrix().elementSum()

    actual infix fun elPow(power: Double): SincMatrix = this.asSimpleMatrix().elementPower(power).asSincMatrix()

    actual fun floor(): SincMatrix =
        SincMatrix(this.matrixData.map {
            floor(it)
        }.toDoubleArray(), numRows(), numCols())

    actual fun abs(): SincMatrix =
        SincMatrix(this.matrixData.map { kotlin.math.abs(it) }.toDoubleArray(), numRows(), numCols())

    actual fun min(dim: Int): SincMatrix {
        if (this.isvector()) {
            return SincMatrix(
                rowMajArray = doubleArrayOf(this.matrixData.minOrNull()!!),
                m = 1,
                n = 1
            )
        } else {
            return if (dim == 1) {
                val result = nans(m = 1, n = this.numCols())
                for (i in 1..this.numCols()) {
                    result[i] = this.getCol(mlCol = i).matrixData.minOrNull()!!
                }
                result
            } else {
                val result = nans(m = this.numRows(), n = 1)
                for (i in 1..this.numRows()) {
                    result[i] = this.getRow(mlRow = i).matrixData.minOrNull()!!
                }
                result
            }
        }
    }

    actual fun max(dim: Int): SincMatrix {
        if (this.isvector()) {
            return SincMatrix(
                rowMajArray = doubleArrayOf(this.matrixData.maxOrNull()!!),
                m = 1,
                n = 1
            )
        } else {
            return if (dim == 1) {
                val result = nans(m = 1, n = this.numCols())
                for (i in 1..this.numCols()) {
                    result[i] = this.getCol(mlCol = i).matrixData.maxOrNull()!!
                }
                result
            } else {
                val result = nans(m = this.numRows(), n = 1)
                for (i in 1..this.numRows()) {
                    result[i] = this.getRow(mlRow = i).matrixData.maxOrNull()!!
                }
                result
            }
        }
    }

    actual fun sin(): SincMatrix = SincMatrix(this.matrixData.map {
        kotlin.math.sin(it)
    }.toDoubleArray(), numRows(), numCols())

    actual fun cos(): SincMatrix =
        SincMatrix(this.matrixData.map {
            kotlin.math.cos(it)
        }.toDoubleArray(), numRows(), numCols())

    actual fun flip(): SincMatrix {
        require(this.isvector()) { "SMError: This function works only for vectors" }

        return SincMatrix(this.matrixData.reversedArray(), this.numRows(), this.numCols())
    }

    actual fun find(): SincMatrix {
        val array = this.matrixData
        val actualIndices = array.indices.filter { array[it] != 0.0 }
        val actualCount = actualIndices.size

        return if (this.isrow()) {
            SincMatrix(
                rowMajArray = actualIndices.map { it.toDouble() + 1.0 }.toDoubleArray(),
                m = 1,
                n = actualCount
            )
        } else {
            SincMatrix(
                rowMajArray = actualIndices.map { it.toDouble() + 1.0 }.toDoubleArray(),
                m = actualCount,
                n = 1
            )
        }
    }

    actual fun filter(B: DoubleArray, A: DoubleArray): SincMatrix {
        require(this.isvector()) { "SMError: This function works only for vectors" }
        require((B.size == 3) && (A.size == 3)) {
            "SMError: Only 2nd order coefficients are allowed. Thus, length(B) == length(A) == 3"
        }

        return SincMatrix(
            rowMajArray = filterWorker(B, A, this.matrixData, doubleArrayOf(0.0, 0.0)),
            m = this.numRows(),
            n = this.numCols()
        )
    }

    actual fun diffWithWavelet(scale: Double, dt: Double): SincMatrix {
        require(this.isvector()) { "SMError: This function works only for vectors" }

        val signal = this.asRowMajorArray()
        return SincMatrix(
            rowMajArray = diffCWTFT(signal, signal.size, scale, dt),
            m = this.numRows(),
            n = this.numCols()
        )
    }

    actual companion object {
        init {
            System.loadLibrary("wavelib")
            System.loadLibrary("tinyexpr")
        }
    }
}