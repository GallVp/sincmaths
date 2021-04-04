fun SincMatrix.equalsTo(rhs: Double): SincMatrix {
    val signedResult = (this - rhs).sign().abs()
    return (signedResult - 1.0) * -1.0
}

fun SincMatrix.notEqualsTo(rhs: Double) : SincMatrix = (this - rhs).sign().abs()

fun SincMatrix.lessThan(rhs: Double): SincMatrix {
    val signedResult = (this - rhs).sign()
    val result = ((signedResult - 1.0) elMul signedResult)
    return result / 2.0
}

fun SincMatrix.greaterThan(rhs: Double): SincMatrix {
    val signedResult = (this - rhs).sign()
    val result = ((signedResult + 1.0) elMul signedResult)
    return result / 2.0
}

fun SincMatrix.any(): Boolean = this.asRowVector().asBoolArray().contains(true)
fun SincMatrix.all(): Boolean = !this.asRowVector().asBoolArray().contains(false)

infix fun SincMatrix.and(rhs: SincMatrix): SincMatrix = this.elMul(rhs).abs() net 0.0
infix fun SincMatrix.or(rhs: SincMatrix): SincMatrix = (this.abs() + rhs.abs()) net 0.0

/**
 * Greater than as in A > B
 */
infix fun SincMatrix.gt(rhs: Double): SincMatrix = this.greaterThan(rhs)
/**
 * Less than as in A < B
 */
infix fun SincMatrix.lt(rhs: Double): SincMatrix = this.lessThan(rhs)
/**
 * Equals to as in A == B
 */
infix fun SincMatrix.et(rhs: Double): SincMatrix = this.equalsTo(rhs)
/**
 * Not equals to as in A != B or A ~= B
 */
infix fun SincMatrix.net(rhs: Double): SincMatrix = this.notEqualsTo(rhs)
/**
 * Equals to 0 as in !A or ~A
 */
operator fun SincMatrix.not(): SincMatrix {
    return this et 0.0
}