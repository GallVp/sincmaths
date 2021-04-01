fun SincMatrix.sum(dim: Int = 1): SincMatrix {
    return if (this.isvector()) {
        SincMatrix(rowMajArray = doubleArrayOf(this.elSum()), m = 1, n = 1)
    } else {
        if (dim == 1) {
            SincMatrix.ones(m = 1, n = this.numRows()) * this
        } else {
            this * SincMatrix.ones(m = this.numCols(), n = 1)
        }
    }
}

fun SincMatrix.mean(dim: Int = 1): SincMatrix {
    return if (this.isvector()) {
        (this.elSum() / this.numel()).asSincMatrix()
    } else {
        if (dim == 1) {
            this.sum(1) / this.numRows().toDouble()
        } else {
            this.sum(2) / this.numCols().toDouble()
        }
    }
}

fun SincMatrix.std(): SincMatrix {
    require(this.isvector()) { "SMError: This function works only for vectors" }

    return (((this - this.mean().asScalar()).elPow(2.0)) / (this.numel() - 1).toDouble()).sum().sqrt()
}


fun SincMatrix.median(): SincMatrix {

    require(this.isvector()) { "SMError: This function works only for vectors" }
    require(this.numel() > 0) { "SMError: Number of elements should be greater than zero" }

    val sortedVector = this.asRowMajorArray().sortedArray()
    return if (sortedVector.size % 2 < 1) {
        //even
        doubleArrayOf((sortedVector[sortedVector.size / 2 - 1] + sortedVector[sortedVector.size / 2]) / 2.0).asSincMatrix(
            m = 1,
            n = 1
        )
    } else {
        //odd
        doubleArrayOf(sortedVector[(sortedVector.size - 1) / 2]).asSincMatrix(m = 1, n = 1)
    }
}

fun SincMatrix.rms(dim: Int = 1): SincMatrix {
    return if (this.isvector()) {
        this.elPow(2.0).mean().sqrt()
    } else {
        this.elPow(2.0).mean(dim).sqrt()
    }
}