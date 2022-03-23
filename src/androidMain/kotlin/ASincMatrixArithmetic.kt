package sincmaths

actual operator fun SincMatrix.times(rhs: SincMatrix): SincMatrix {

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

actual operator fun SincMatrix.times(rhs: Double): SincMatrix = this.asSimpleMatrix().scale(rhs).asSincMatrix()
actual operator fun SincMatrix.plus(rhs: SincMatrix): SincMatrix {

    if (this.isscalar()) {
        return rhs + this.scalar
    }

    if (rhs.isscalar()) {
        return this + rhs.scalar
    }

    return this.asSimpleMatrix().plus(rhs.asSimpleMatrix()).asSincMatrix()
}


actual operator fun SincMatrix.plus(rhs: Double): SincMatrix = this.asSimpleMatrix().plus(rhs).asSincMatrix()
actual infix fun SincMatrix.elMul(rhs: SincMatrix): SincMatrix {

    if (this.isscalar()) {
        return rhs * this.scalar
    }

    if (rhs.isscalar()) {
        return this * rhs.scalar
    }

    return this.asSimpleMatrix().elementMult(rhs.asSimpleMatrix()).asSincMatrix()
}

actual infix fun SincMatrix.elDiv(rhs: SincMatrix): SincMatrix {

    if (this.isscalar() && rhs.isscalar()) {
        return this / rhs.scalar
    }

    if (this.isscalar()) {
        return (SincMatrix.ones(rhs.numRows(), rhs.numCols()) * this.scalar).elDiv(rhs)
    }

    if (rhs.isscalar()) {
        return this * (1 / rhs.scalar)
    }

    return this.asSimpleMatrix().elementDiv(rhs.asSimpleMatrix()).asSincMatrix()
}

actual fun SincMatrix.elSum(): Double = this.asSimpleMatrix().elementSum()
actual infix fun SincMatrix.elPow(power: Double): SincMatrix = this.asSimpleMatrix().elementPower(power).asSincMatrix()