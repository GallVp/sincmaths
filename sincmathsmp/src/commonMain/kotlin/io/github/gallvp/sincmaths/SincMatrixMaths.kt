package io.github.gallvp.sincmaths

import kotlin.math.pow

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

operator fun Double.div(rhs: SincMatrix) = (SincMatrix.ones(rhs.numRows, rhs.numCols) * this) elDiv rhs

expect fun SincMatrix.floor(): SincMatrix

expect fun SincMatrix.abs(): SincMatrix

fun SincMatrix.sign(): SincMatrix {
    // Octave code: floor(t./(abs(t)+1)) - floor(-t./(abs(-t)+1))
    val negSelf = this * -1.0
    val firstPart = (this elDiv (this.abs() + 1.0)).floor()
    val secondPart = (negSelf elDiv (negSelf.abs() + 1.0)).floor()

    return firstPart - secondPart
}

fun SincMatrix.sqrt(): SincMatrix = this elPow (1.0 / 2.0)

fun SincMatrix.cross(ontoVector: SincMatrix): SincMatrix {
    require(
        this.isVector && ontoVector.isVector &&
            this.numel == 3 && ontoVector.numel == 3,
    ) {
        "This function works only for vectors of length 3"
    }

    val c1 = this[2] * ontoVector[3] - this[3] * ontoVector[2]
    val c2 = this[3] * ontoVector[1] - this[1] * ontoVector[3]
    val c3 = this[1] * ontoVector[2] - this[2] * ontoVector[1]

    return if (this.isColumn && ontoVector.isColumn) {
        doubleArrayOf(c1, c2, c3).asSincMatrix(m = 3, n = 1)
    } else {
        doubleArrayOf(c1, c2, c3).asSincMatrix(m = 1, n = 3)
    }
}

fun SincMatrix.dot(rhs: SincMatrix): SincMatrix = (this elMul rhs).sum()

fun SincMatrix.round(n: Int): SincMatrix =
    map {
        kotlin.math.round(it * 10.0.pow(n)) / 10.0.pow(n)
    }
