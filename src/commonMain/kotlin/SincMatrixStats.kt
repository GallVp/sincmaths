package sincmaths.sincmatrix

import sincmaths.SincMatrix
import sincmaths.asSincMatrix
import sincmaths.sincmatrix.workers.medianWorker
import sincmaths.sincmatrix.workers.vectorPercentileWorker

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

fun SincMatrix.std(dim: Int = 1): SincMatrix = if (this.isvector()) {
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


fun SincMatrix.median(dim: Int = 1): SincMatrix = if (this.isvector()) {
    matrixOf(1, 1, medianWorker(this.asRowMajorArray().sortedArray()))
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

/**
 * The IQR function is compatible with MATLAB 2020b and not with Octave 6.2.0.
 * C.F. [bug no. 59636](https://savannah.gnu.org/bugs/?59636)
 */
fun SincMatrix.iqr(dim: Int = 1): SincMatrix = if (this.isvector()) {
    require(this.numel() > 0) { "SMError: Number of elements should be greater than zero" }

    val sortedVector = this.asRowMajorArray().sortedArray().asSincMatrix(false)
    val percentiles = vectorPercentileWorker(sortedVector, colVectorOf(25, 75))
    percentiles.diff()
} else {
    if (dim == 1) {
        this.mapColumnsToDouble {
            it.iqr().scalar
        }
    } else {
        this.mapRowsToDouble {
            it.iqr().scalar
        }
    }
}

fun SincMatrix.quantile(P:DoubleArray, dim: Int = 1): SincMatrix = if (this.isvector()) {
    require(this.numel() > 0) { "SMError: Number of elements should be greater than zero" }
    require(P.isNotEmpty()) { "SMError: P should not be empty" }

    val sortedVector = this.asRowMajorArray().sortedArray().asSincMatrix(false)
    val quantiles = vectorPercentileWorker(sortedVector, P.asSincMatrix(false) * 100.0)
    quantiles.t
} else {
    if (dim == 1) {
        this.mapColumns(P.size) {
            it.quantile(P).t
        }
    } else {
        this.mapRows(P.size) {
            it.quantile(P)
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