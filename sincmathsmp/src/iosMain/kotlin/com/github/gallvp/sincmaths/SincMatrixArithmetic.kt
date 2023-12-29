package com.github.gallvp.sincmaths

actual operator fun SincMatrix.times(rhs: SincMatrix): SincMatrix {

    if(this.isScalar) {
        return rhs * this.scalar
    }

    if(rhs.isScalar) {
        return this * rhs.scalar
    }

    val lhsM = this.size.first()
    val lhsN = this.size.last()
    val rhsM = rhs.size.first()
    val rhsN = rhs.size.last()

    require(lhsN == rhsM) { "Dimension mismatch. In A*B, ncols(A) == nrows(B)" }
    return multiplyRowMajorMatrices(this.matrixData, lhsM, lhsN, rhs.matrixData, rhsM, rhsN).asSincMatrix(
        lhsM,
        rhsN
    )
}

actual operator fun SincMatrix.times(rhs: Double): SincMatrix =
    multiplyVectorToScalar(this.matrixData, rhs).asSincMatrix(this.numRows, this.numCols)

actual operator fun SincMatrix.plus(rhs: SincMatrix): SincMatrix {

    if(this.isScalar) {
        return rhs + this.scalar
    }

    if(rhs.isScalar) {
        return this + rhs.scalar
    }

    require(this.size == rhs.size) { "Dimension mismatch. In A + B, size(A) == size(B)" }
    return addVectors(this.asRowMajorArray(), rhs.asRowMajorArray()).asSincMatrix(this.numRows, this.numCols)
}

actual operator fun SincMatrix.plus(rhs: Double): SincMatrix =
    addScalarToVector(this.matrixData, rhs).asSincMatrix(this.numRows, this.numCols)

actual infix fun SincMatrix.elMul(rhs: SincMatrix): SincMatrix {

    if(this.isScalar) {
        return rhs * this.scalar
    }

    if(rhs.isScalar) {
        return this * rhs.scalar
    }

    require(this.size == rhs.size) { "Dimension mismatch. In A elMul B, size(A) == size(B)" }

    return multiplyElementsOfVectors(this.matrixData, rhs.matrixData).asSincMatrix(this.numRows, this.numCols)
}

actual infix fun SincMatrix.elDiv(rhs: SincMatrix): SincMatrix {

    if(this.isScalar && rhs.isScalar) {
        return this / rhs.scalar
    }

    if (this.isScalar) {
        return (SincMatrix.ones(rhs.numRows, rhs.numCols) * this.scalar).elDiv(rhs)
    }

    if (rhs.isScalar) {
        return this * (1 / rhs.scalar)
    }

    require(this.size == rhs.size) { "Dimension mismatch. In A elDiv B, size(A) == size(B)" }

    return divideElementsOfVectors(this.matrixData, rhs.matrixData).asSincMatrix(this.numRows, this.numCols)
}

actual fun SincMatrix.elSum(): Double {
    return if (this.isEmpty()) {
        0.0
    } else {
        sumOfVectorElements(this.matrixData)
    }
}

actual infix fun SincMatrix.elPow(power: Double): SincMatrix =
    exponentOfVector(this.matrixData, power).asSincMatrix(this.numRows, this.numCols)