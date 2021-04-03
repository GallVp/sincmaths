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
        this.isvector() && ontoVector.isvector() &&
                this.numel() == 3 && ontoVector.numel() == 3
    ) {
        "SMError: This function works only for vectors of length 3"
    }

    val c1 = this[2] * ontoVector[3] - this[3] * ontoVector[2]
    val c2 = this[3] * ontoVector[1] - this[1] * ontoVector[3]
    val c3 = this[1] * ontoVector[2] - this[2] * ontoVector[1]

    return if (this.iscolumn() && ontoVector.iscolumn()) {
        doubleArrayOf(c1, c2, c3).asSincMatrix(m = 3, n = 1)
    } else {
        doubleArrayOf(c1, c2, c3).asSincMatrix(m = 1, n = 3)
    }
}

fun SincMatrix.dot(rhs: SincMatrix):SincMatrix = (this elMul rhs).sum()

/**
 * Matrix transpose
 */
val SincMatrix.t:SincMatrix
get() {
    return this.transpose()
}