operator fun SincMatrix.minus(rhs: SincMatrix): SincMatrix = this + (rhs * -1.0)
operator fun SincMatrix.minus(rhs: Double): SincMatrix = this + (rhs * -1.0)