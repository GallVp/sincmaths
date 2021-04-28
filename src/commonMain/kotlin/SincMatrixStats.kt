package sincmaths.sincmatrix

import sincmaths.SincMatrix
import sincmaths.asSincMatrix

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

fun SincMatrix.std(dim: Int = 1) : SincMatrix = if(this.isvector()) {
    (((this - this.mean().asScalar()).elPow(2.0)) / (this.numel() - 1).toDouble()).sum().sqrt()
} else {
    if (dim == 1) {
        this.mapColumnsToDouble {
                it.std().scalar
        }
    } else {
        this.mapRowsToDouble {
            it.std().scalar
        }
    }
}


fun SincMatrix.median(dim: Int = 1): SincMatrix = if(this.isvector()) {
    require(this.numel() > 0) { "SMError: Number of elements should be greater than zero" }

    val sortedVector = this.asRowMajorArray().sortedArray()
    if (sortedVector.size % 2 < 1) {
        //even
        doubleArrayOf((sortedVector[sortedVector.size / 2 - 1] + sortedVector[sortedVector.size / 2]) / 2.0).asSincMatrix(
            m = 1,
            n = 1
        )
    } else {
        //odd
        doubleArrayOf(sortedVector[(sortedVector.size - 1) / 2]).asSincMatrix(m = 1, n = 1)
    }
} else {
    if (dim == 1) {
        this.mapColumnsToDouble {
            it.median().scalar
        }
    } else {
        this.mapRowsToDouble {
            it.median().scalar
        }
    }
}

fun SincMatrix.rms(dim: Int = 1): SincMatrix {
    return if (this.isvector()) {
        this.elPow(2.0).mean().sqrt()
    } else {
        this.elPow(2.0).mean(dim).sqrt()
    }
}