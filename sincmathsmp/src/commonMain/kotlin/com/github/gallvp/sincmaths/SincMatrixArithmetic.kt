package com.github.gallvp.sincmaths

expect operator fun SincMatrix.times(rhs: SincMatrix): SincMatrix
expect operator fun SincMatrix.times(rhs: Double): SincMatrix
expect operator fun SincMatrix.plus(rhs: SincMatrix): SincMatrix
expect operator fun SincMatrix.plus(rhs: Double): SincMatrix
expect infix fun SincMatrix.elDiv(rhs: SincMatrix): SincMatrix
expect infix fun SincMatrix.elMul(rhs: SincMatrix): SincMatrix
expect fun SincMatrix.elSum(): Double
expect infix fun SincMatrix.elPow(power: Double): SincMatrix

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