package sincmaths

actual fun SincMatrix.asArray(): DoubleArray {

    if(this.isempty()) {
        return doubleArrayOf()
    }

    require(this.isvector()) { "SMError: Matrix is not a vector and conversion is invalid" }
    return this.matrixData
}

actual fun SincMatrix.asRowMajorArray(): DoubleArray = this.matrixData