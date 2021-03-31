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