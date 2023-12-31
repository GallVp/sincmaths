package io.github.gallvp.sincmaths

fun SincMatrix.equalsTo(rhs: Double): SincMatrix {
    val signedResult = (this - rhs).sign().abs()
    return (signedResult - 1.0) * -1.0
}

fun Double.equalsTo(rhs: SincMatrix) = rhs et this

fun SincMatrix.equalsTo(rhs: SincMatrix): SincMatrix {
    if (this.isScalar) {
        return rhs et this.scalar
    }

    if (rhs.isScalar) {
        return this et rhs.scalar
    }

    require(this.size == rhs.size) {
        "size(A) should be equal to size(B)"
    }

    return (this - rhs).abs() et 0.0
}

fun SincMatrix.notEqualsTo(rhs: Double): SincMatrix = (this - rhs).sign().abs()

fun Double.notEqualsTo(rhs: SincMatrix) = rhs net this

fun SincMatrix.notEqualsTo(rhs: SincMatrix): SincMatrix {
    if (this.isScalar) {
        return rhs net this.scalar
    }

    if (rhs.isScalar) {
        return this net rhs.scalar
    }

    require(this.size == rhs.size) {
        "size(A) should be equal to size(B)"
    }

    return (this - rhs).abs() net 0.0
}

fun SincMatrix.lessThan(rhs: Double): SincMatrix {
    val signedResult = (this - rhs).sign()
    val result = ((signedResult - 1.0) elMul signedResult)
    return result / 2.0
}

fun Double.lessThan(rhs: SincMatrix) = rhs gt this

fun SincMatrix.greaterThan(rhs: Double): SincMatrix {
    val signedResult = (this - rhs).sign()
    val result = ((signedResult + 1.0) elMul signedResult)
    return result / 2.0
}

fun Double.greaterThan(rhs: SincMatrix) = rhs lt this

fun SincMatrix.any(): Boolean = this.asRowVector().asBoolArray().contains(true)

fun SincMatrix.all(): Boolean = !this.asRowVector().asBoolArray().contains(false)

infix fun SincMatrix.and(rhs: SincMatrix): SincMatrix = this.elMul(rhs).abs() net 0.0

infix fun SincMatrix.or(rhs: SincMatrix): SincMatrix = (this.abs() + rhs.abs()) net 0.0

/**
 * Greater than as in A > b
 */
infix fun SincMatrix.gt(rhs: Double): SincMatrix = this.greaterThan(rhs)

/**
 * Greater than as in a > B
 */
infix fun Double.gt(rhs: SincMatrix): SincMatrix = this.greaterThan(rhs)

/**
 * Less than as in A < b
 */
infix fun SincMatrix.lt(rhs: Double): SincMatrix = this.lessThan(rhs)

/**
 * Less than as in a < B
 */
infix fun Double.lt(rhs: SincMatrix): SincMatrix = this.lessThan(rhs)

/**
 * Equals to as in A == B
 */
infix fun SincMatrix.et(rhs: SincMatrix): SincMatrix = this.equalsTo(rhs)

/**
 * Equals to as in A == b
 */
infix fun SincMatrix.et(rhs: Double): SincMatrix = this.equalsTo(rhs)

/**
 * Equals to as in a == B
 */
infix fun Double.et(rhs: SincMatrix): SincMatrix = rhs et this

/**
 * Not equals to as in A != B or A ~= B
 */
infix fun SincMatrix.net(rhs: SincMatrix): SincMatrix = this.notEqualsTo(rhs)

/**
 * Not equals to as in A != b or A ~= b
 */
infix fun SincMatrix.net(rhs: Double): SincMatrix = this.notEqualsTo(rhs)

/**
 * Not equals to as in a != B or a ~= B
 */
infix fun Double.net(rhs: SincMatrix): SincMatrix = this.notEqualsTo(rhs)

/**
 * !A or ~A
 */
operator fun SincMatrix.not(): SincMatrix {
    return this et 0.0
}
