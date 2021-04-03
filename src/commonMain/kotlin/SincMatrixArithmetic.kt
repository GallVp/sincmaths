operator fun SincMatrix.minus(rhs: SincMatrix): SincMatrix = this + (rhs * -1.0)
operator fun SincMatrix.minus(rhs: Double): SincMatrix = this + (rhs * -1.0)
operator fun SincMatrix.div(rhs: Double): SincMatrix = this * (1/rhs)
operator fun SincMatrix.unaryMinus(): SincMatrix {
    return this * -1.0
}