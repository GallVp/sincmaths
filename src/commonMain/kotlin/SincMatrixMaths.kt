fun SincMatrix.sign(): SincMatrix {
    // Octave code: floor(t./(abs(t)+1)) - floor(-t./(abs(-t)+1))
    val negSelf = this * -1.0
    val firstPart = (this elDiv (this.abs() + 1.0)).floor()
    val secondPart = (negSelf elDiv (negSelf.abs() + 1.0)).floor()

    return firstPart - secondPart
}

fun SincMatrix.sqrt(): SincMatrix = this elPow (1.0 / 2.0)