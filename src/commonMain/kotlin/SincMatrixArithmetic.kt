package sincmaths.sincmatrix

import sincmaths.SincMatrix

operator fun SincMatrix.minus(rhs: SincMatrix): SincMatrix = this + (rhs * -1.0)
operator fun SincMatrix.minus(rhs: Double): SincMatrix = this + (rhs * -1.0)
operator fun SincMatrix.div(rhs: Double): SincMatrix = this * (1 / rhs)
operator fun SincMatrix.unaryMinus(): SincMatrix {
    return this * -1.0
}

infix fun SincMatrix.elMul(rhs: Double): SincMatrix = this * rhs

operator fun Double.plus(rhs: SincMatrix) = rhs + this
operator fun Double.minus(rhs: SincMatrix) = -rhs + this
operator fun Double.times(rhs: SincMatrix) = rhs * this
operator fun Double.div(rhs: SincMatrix) = (SincMatrix.ones(rhs.numRows(), rhs.numCols()) * this) elDiv rhs