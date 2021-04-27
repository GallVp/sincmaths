package sincmaths

import sincmaths.sincmatrix.*
import sincmaths.primitives.*

actual class SincMatrix actual constructor(rowMajArray: DoubleArray, private var m: Int, private var n: Int) {

    private var matrixData:DoubleArray

    init {
        require(rowMajArray.size == m*n) { "SMError: length(rowMajArray) should be equal to m*n" }
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

    actual fun asRowMajorArray(): DoubleArray = this.matrixData

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
        includedIndices.removeAll(indices.map { it-1 })
        this.matrixData = this.matrixData.sliceArray(includedIndices)
        m = 1
        n = this.matrixData.size
    }

    // ************************************************************************* SincMatrixArithmetic

    actual operator fun times(rhs: SincMatrix): SincMatrix {
        val lhsM = this.size().first()
        val lhsN = this.size().last()
        val rhsM = rhs.size().first()
        val rhsN = rhs.size().last()

        require(lhsN == rhsM) { "SMError: Dimension mismatch. In A*B, ncols(A) == nrows(B)" }
        return multiplyRowMajorMatrices(this.matrixData, lhsM, lhsN, rhs.matrixData, rhsM, rhsN).asSincMatrix(
            lhsM,
            rhsN
        )
    }

    actual operator fun times(rhs: Double): SincMatrix =
        multiplyVectorToScalar(this.matrixData, rhs).asSincMatrix(this.numRows(), this.numCols())

    actual operator fun plus(rhs: SincMatrix): SincMatrix {
        require(this.size() == rhs.size()) { "SMError: Dimension mismatch. In A + B, size(A) == size(B)" }
        return addVectors(this.asRowMajorArray(), rhs.asRowMajorArray()).asSincMatrix(this.numRows(), this.numCols())
    }

    actual operator fun plus(rhs: Double): SincMatrix =
        addScalarToVector(this.matrixData, rhs).asSincMatrix(this.numRows(), this.numCols())

    actual infix fun elMul(rhs: SincMatrix): SincMatrix {
        require(this.size() == rhs.size()) { "SMError: Dimension mismatch. In A elMul B, size(A) == size(B)" }

        return multiplyElementsOfVectors(this.matrixData, rhs.matrixData).asSincMatrix(this.numRows(), this.numCols())
    }

    actual infix fun elDiv(rhs: SincMatrix): SincMatrix {
        require(this.size() == rhs.size()) { "SMError: Dimension mismatch. In A elDiv B, size(A) == size(B)" }

        return divideElementsOfVectors(this.matrixData, rhs.matrixData).asSincMatrix(this.numRows(), this.numCols())
    }

    actual fun elSum(): Double {
        return if (this.isempty()) {
            0.0
        } else {
            sumOfVectorElements(this.matrixData)
        }
    }

    actual infix fun elPow(power: Double): SincMatrix =
        exponentOfVector(this.matrixData, power).asSincMatrix(this.numRows(), this.numCols())

    // ************************************************************************* SincMatrixMaths

    actual fun transpose(): SincMatrix =
        transposeOfRowMajorMatrix(this.matrixData, this.numRows(), this.numCols()).asSincMatrix(
            this.numCols(),
            this.numRows()
        )

    actual fun floor(): SincMatrix =
        floorOfElementsOfVector(this.matrixData).asSincMatrix(this.numRows(), this.numCols())

    actual fun abs(): SincMatrix = absOfElementsOfVector(this.matrixData).asSincMatrix(this.numRows(), this.numCols())

    actual fun find(): SincMatrix {
        val actualIndices = findNonZeroIndices(this.matrixData, false)
        val actualCount = actualIndices.size
        if (actualCount < 1) {
            return SincMatrix(doubleArrayOf(), 0, 0)
        }
        return if (this.isrow()) {
            SincMatrix(rowMajArray = actualIndices, m = 1, n = actualCount)
        } else {
            SincMatrix(rowMajArray = actualIndices, m = actualCount, n = 1)
        }
    }

    // ************************************************************************* SincMatrixSolvers

    actual fun solve(b: SincMatrix): SincMatrix = solveNonsymSquareSystem(
        this.matrixData,
        this.numRows(),
        this.numCols(),
        b.matrixData
    ).asSincMatrix(b.numRows(), b.numCols())

    // ************************************************************************* SincMatrixStats

    actual fun min(dim: Int): SincMatrix {
        if (this.isvector()) {
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

    actual fun max(dim: Int): SincMatrix {
        if (this.isvector()) {
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

    // ************************************************************************* SincMatrixTrigonometry

    actual fun sin(): SincMatrix = sinOfElementsOfVector(this.matrixData).asSincMatrix(this.numRows(), this.numCols())

    actual fun cos(): SincMatrix = cosOfElementsOfVector(this.matrixData).asSincMatrix(this.numRows(), this.numCols())

    actual companion object
}