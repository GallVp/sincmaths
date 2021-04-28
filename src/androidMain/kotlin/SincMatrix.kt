package sincmaths

import org.ejml.simple.SimpleMatrix
import sincmaths.sincmatrix.*
import sincmaths.sincmatrix.helpers.asSincMatrix
import kotlin.math.floor

actual class SincMatrix actual constructor(rowMajArray: DoubleArray, private var m: Int, private var n: Int) {

    private var matrixData: DoubleArray

    init {
        require(rowMajArray.size == m * n) { "SMError: length(rowMajArray) should be equal to m*n" }
        matrixData = rowMajArray
    }

    actual fun numRows(): Int = m
    actual fun numCols(): Int = n
    actual fun numel() = this.matrixData.size
    actual override fun toString(): String = this.description

    // ************************************************************************* SincMatrixAsTypes

    actual fun asArray(): DoubleArray {
        require(this.isvector()) { "SMError: Matrix is not a vector and conversion is invalid" }
        return this.matrixData
    }

    actual fun asRowMajorArray() = this.matrixData
    private fun asSimpleMatrix() = SimpleMatrix(this.numRows(), this.numCols(), true, this.matrixData)

    // ************************************************************************* SincMatrixSet

    actual operator fun set(mlRow: Int, mlCol: Int, value: Double) {
        this.matrixData[this.getIndex(mlRow, mlCol) - 1] = value
    }

    actual operator fun set(index: Int, value: Double) {
        this.matrixData[index - 1] = value
    }

    actual fun removeAt(index: Int) {
        val includedIndices = (0 until this.matrixData.size).toMutableList()
        includedIndices.removeAt(index - 1)
        this.matrixData = this.matrixData.sliceArray(includedIndices)
        m = 1
        n = this.matrixData.size
    }

    actual fun removeAt(indices: IntArray) {
        val includedIndices = (0 until this.matrixData.size).toMutableList()
        includedIndices.removeAll(indices.map { it - 1 })
        this.matrixData = this.matrixData.sliceArray(includedIndices)
        m = 1
        n = this.matrixData.size
    }

    // ************************************************************************* SincMatrixArithmetic

    actual operator fun times(rhs: SincMatrix): SincMatrix {

        if (this.isscalar()) {
            return rhs * this.scalar
        }

        if (rhs.isscalar()) {
            return this * rhs.scalar
        }

        val lhsN = this.size().last()
        val rhsM = rhs.size().first()
        require(lhsN == rhsM) { "SMError: Dimension mismatch. In A*B, ncols(A) == nrows(B)" }

        return this.asSimpleMatrix().mult(rhs.asSimpleMatrix()).asSincMatrix()
    }

    actual operator fun times(rhs: Double): SincMatrix = this.asSimpleMatrix().scale(rhs).asSincMatrix()
    actual operator fun plus(rhs: SincMatrix): SincMatrix {

        if (this.isscalar()) {
            return rhs + this.scalar
        }

        if (rhs.isscalar()) {
            return this + rhs.scalar
        }

        return this.asSimpleMatrix().plus(rhs.asSimpleMatrix()).asSincMatrix()
    }


    actual operator fun plus(rhs: Double): SincMatrix = this.asSimpleMatrix().plus(rhs).asSincMatrix()
    actual infix fun elMul(rhs: SincMatrix): SincMatrix {

        if (this.isscalar()) {
            return rhs * this.scalar
        }

        if (rhs.isscalar()) {
            return this * rhs.scalar
        }

        return this.asSimpleMatrix().elementMult(rhs.asSimpleMatrix()).asSincMatrix()
    }

    actual infix fun elDiv(rhs: SincMatrix): SincMatrix {

        if(this.isscalar() && rhs.isscalar()) {
            return this / rhs.scalar
        }

        if (this.isscalar()) {
            return (ones(rhs.numRows(), rhs.numCols()) * this.scalar).elDiv(rhs)
        }

        if (rhs.isscalar()) {
            return this * (1 / rhs.scalar)
        }

        return this.asSimpleMatrix().elementDiv(rhs.asSimpleMatrix()).asSincMatrix()
    }

    actual fun elSum(): Double = this.asSimpleMatrix().elementSum()
    actual infix fun elPow(power: Double): SincMatrix = this.asSimpleMatrix().elementPower(power).asSincMatrix()

    // ************************************************************************* SincMatrixMaths

    actual fun transpose(): SincMatrix = this.asSimpleMatrix().transpose().asSincMatrix()
    actual fun floor(): SincMatrix =
        SincMatrix(this.matrixData.map {
            floor(it)
        }.toDoubleArray(), numRows(), numCols())

    actual fun abs(): SincMatrix =
        SincMatrix(this.matrixData.map { kotlin.math.abs(it) }.toDoubleArray(), numRows(), numCols())

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

    // ************************************************************************* SincMatrixSolvers

    actual fun solve(b: SincMatrix): SincMatrix = this.asSimpleMatrix().solve(b.asSimpleMatrix()).asSincMatrix()

    // ************************************************************************* SincMatrixStats

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

    // ************************************************************************* SincMatrixTrigonometry

    actual fun sin(): SincMatrix = SincMatrix(this.matrixData.map {
        kotlin.math.sin(it)
    }.toDoubleArray(), numRows(), numCols())

    actual fun cos(): SincMatrix =
        SincMatrix(this.matrixData.map {
            kotlin.math.cos(it)
        }.toDoubleArray(), numRows(), numCols())

    actual companion object {
        init {
            System.loadLibrary("wavelib")
            System.loadLibrary("tinyexpr")
        }
    }
}