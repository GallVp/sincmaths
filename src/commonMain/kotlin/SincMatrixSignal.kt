package sincmaths

import kotlin.math.roundToInt

fun SincMatrix.conv(B: SincMatrix, shape: ConvolutionShape = ConvolutionShape.full): SincMatrix {

    require(this.isvector() && B.isvector()) {
        "SMError: This function works only for vectors"
    }

    var isColumnFlag = false

    if (this.iscolumn()) {
        isColumnFlag = true
    }

    val convResult = convWorker(this.asRowMajorArray(), B.asRowMajorArray())
    val convSegmentSize = intArrayOf(this.numel() + B.numel() - 1, this.numel(), B.numel()).maxOrNull() ?: 0
    val convSegment = convResult.sliceArray(0 until convSegmentSize)

    when (shape) {
        ConvolutionShape.full -> {
            val resultMat = SincMatrix(rowMajArray = convSegment, m = 1, n = convSegmentSize)
            return if (isColumnFlag) {
                resultMat.transpose()
            } else {
                resultMat
            }
        }
        ConvolutionShape.same -> {
            val startPoint =
                ((convSegmentSize - this.numel()).toDouble() / 2.0).roundToInt() + 1
            val resultMat = SincMatrix(
                rowMajArray = convSegment,
                m = 1,
                n = convSegmentSize
            ).getCols((startPoint until startPoint + this.numel()).toList().toIntArray())
            return if (isColumnFlag) {
                resultMat.transpose()
            } else {
                resultMat
            }
        }
        ConvolutionShape.valid -> {

            val validArray = convSegment.drop(B.numel() - 1).dropLast(B.numel() - 1).toDoubleArray()
            val resultMat = SincMatrix(rowMajArray = validArray, m = 1, n = validArray.size)
            return if (isColumnFlag) {
                resultMat.transpose()
            } else {
                resultMat
            }
        }
    }
}

fun SincMatrix.diff(dim: Int = 1): SincMatrix = if (this.isvector()) {
    val kernel = SincMatrix(doubleArrayOf(1.0, -1.0), 1, 2)
    this.conv(kernel, ConvolutionShape.valid)
} else {
    if (dim == 1) {
        this.mapColumns(this.numRows() - 1) {
            it.diff()
        }
    } else {
        this.mapRows(this.numCols() - 1) {
            it.diff()
        }
    }
}

fun SincMatrix.movsum(wlen: Int, endpoints: MovWinShape = MovWinShape.shrink, dim: Int = 1): SincMatrix =
    if (this.isvector()) {
        require(this.numel() > 0) { "SMError: Number of elements should be greater than zero" }
        require(wlen > 0) { "SMError: Length of moving window should be > 0" }

        val convKernel = SincMatrix.ones(m = 1, n = wlen)
        when (endpoints) {
            MovWinShape.discard -> {
                this.conv(B = convKernel, shape = ConvolutionShape.valid)
            }
            MovWinShape.shrink -> {
                if (wlen % 2 < 1) { //Even
                    val computedSegment = this.conv(B = convKernel, shape = ConvolutionShape.same)
                    val computedArray = computedSegment.asRowMajorArray()
                    val movSumTwoToEnd = computedArray.sliceArray(0 until (computedArray.size - 1))
                    //Guaranteed: len(computedArray) >= 2
                    val windowEndpoint = wlen / 2 - 1
                    require(windowEndpoint < this.numel()) { "SMError: Window length is incompatible with shape = shrink" }
                    val firstElement =
                        this[(1..(windowEndpoint + 1))].sum().asScalar()
                    doubleArrayOf(
                        firstElement,
                        *movSumTwoToEnd
                    ).asSincMatrix(computedSegment.numRows(), computedSegment.numCols())
                } else {
                    //Odd
                    this.conv(B = convKernel, shape = ConvolutionShape.same)
                }
            }
        }
    } else {
        if (dim == 1) {
            this.mapColumnsToList {
                it.movsum(wlen, endpoints)
            }.makeMatrixFrom(false)
        } else {
            this.mapRowsToList {
                it.movsum(wlen, endpoints)
            }.makeMatrixFrom()
        }
    }

fun SincMatrix.movmean(wlen: Int, endpoints: MovWinShape = MovWinShape.shrink, dim: Int = 1): SincMatrix = if(this.isvector()) {
    when (endpoints) {
        MovWinShape.discard -> {
            val convKernel = SincMatrix.ones(1, wlen) / wlen.toDouble()
            this.conv(B = convKernel, shape = ConvolutionShape.valid)
        }
        MovWinShape.shrink -> {
            val movSumResult = this.movsum(wlen = wlen, endpoints = MovWinShape.shrink)
            val numPoints =
                SincMatrix.ones(1, this.numel()).movsum(wlen = wlen, endpoints = MovWinShape.shrink)
            if (movSumResult.isrow()) {
                (movSumResult elDiv numPoints)
            } else {
                (movSumResult elDiv numPoints.transpose())
            }
        }
    }
} else {
    if (dim == 1) {
        this.mapColumnsToList {
            it.movmean(wlen, endpoints)
        }.makeMatrixFrom(false)
    } else {
        this.mapRowsToList {
            it.movmean(wlen, endpoints)
        }.makeMatrixFrom()
    }
}

/**
 * Only second order filters are supported. Thus, length(B) == length(A) == 3 is assumed.
 */
fun SincMatrix.filter(B: DoubleArray, A: DoubleArray, dim: Int = 1): SincMatrix = if (this.isvector()) {
    require((B.size == 3) && (A.size == 3)) {
        "SMError: Only 2nd order coefficients are allowed. Thus, length(B) == length(A) == 3"
    }

    SincMatrix(
        rowMajArray = filterWorker(B, A, this.asRowMajorArray(), doubleArrayOf(0.0, 0.0)),
        m = this.numRows(),
        n = this.numCols()
    )
} else {
    if(dim == 1) {
        this.mapColumns {
            SincMatrix(
                rowMajArray = filterWorker(B, A, it.asRowMajorArray(), doubleArrayOf(0.0, 0.0)),
                m = it.numRows(),
                n = it.numCols()
            )
        }
    } else {
        this.mapRows {
            SincMatrix(
                rowMajArray = filterWorker(B, A, it.asRowMajorArray(), doubleArrayOf(0.0, 0.0)),
                m = it.numRows(),
                n = it.numCols()
            )
        }
    }
}

/**
 * Only second order filters are supported. Thus, length(B) == length(A) == 3 is assumed.
 */
fun SincMatrix.filtfilt(B: DoubleArray, A: DoubleArray): SincMatrix {
    require((B.size == 3) && (A.size == 3)) {
        "SMError: Only 2nd order coefficients are allowed. Thus, length(B) == length(A) == 3"
    }

    return if (this.isvector()) {
        SincMatrix(filtfiltWorker(B, A, this.asRowMajorArray()), numRows(), numCols())
    } else {
        this.mapColumns {
            filtfiltWorker(B, A, it.asRowMajorArray()).asSincMatrix(false)
        }
    }
}

/**
 * This function is like Octave's sgolayfilt, except that instead of order and filter length
 * Savitzky-Golay a pre-computed projection matrix ([B]) is supplied.
 */
fun SincMatrix.sgolayfilter(B: SincMatrix, dim: Int = 1): SincMatrix {

    return if (this.isvector()) {
        sgolayWorker(this, B)
    } else {
        if (dim == 1) {
            this.mapColumns {
                sgolayWorker(it, B)
            }
        } else {
            this.mapRows {
                sgolayWorker(it, B)
            }
        }
    }
}

fun SincMatrix.acf(numLags: Int): SincMatrix {

    require(this.isvector()) { "SMError: This function works only for vectors" }
    require(numLags < this.numel()) {
        "SMError: No. of lags should be smaller than the length of the vector"
    }

    val detrendMatrix = (this + (-this.mean().asScalar()))
    val convSum = detrendMatrix.conv(B = detrendMatrix.flip())
    val scale = 1.0 / detrendMatrix.dot(detrendMatrix).asScalar()
    val scaledConvSum = convSum * scale
    val numElements = ((numel() + 1)..(numel() + numLags)).toList().toIntArray()
    return if (this.isrow()) {
        scaledConvSum.getCols(mlCols = numElements)
    } else {
        scaledConvSum.getRows(mlRows = numElements)
    }
}

fun SincMatrix.findpeaks(): SincMatrix {
    require(this.isvector()) { "SMError: This function works only for vectors" }
    val manipulatedData = doubleArrayOf(Double.NaN) + this.asRowMajorArray() + Double.NaN
    val manipulatedMatrix =
        SincMatrix(rowMajArray = manipulatedData, m = manipulatedData.size, n = 1)
    val resultA = manipulatedMatrix.diff().sign().diff().lessThan(0.0)
    val result =
        resultA.getRows(mlRows = (2 until this.numel()).toList().toIntArray()).find() + 1.0
    return if (this.isrow()) {
        result.transpose()
    } else {
        result
    }
}

fun SincMatrix.cumsum(dim: Int = 1): SincMatrix = if (this.isvector()) {
    val inputVector = this.asRowMajorArray()
    val resultVector = inputVector.copyOf()
    for (i in 1 until resultVector.size) {
        resultVector[i] = resultVector[i] + resultVector[i - 1]
    }
    SincMatrix(resultVector, this.numRows(), this.numCols())
} else {
    if (dim == 1) {
        this.mapColumns {
            it.cumsum()
        }
    } else {
        this.mapRows {
            it.cumsum()
        }
    }
}

fun SincMatrix.flip(dim: Int = 1): SincMatrix = if (this.isvector()) {
    SincMatrix(this.asRowMajorArray().reversedArray(), this.numRows(), this.numCols())
} else {
    if (dim == 1) {
        this.mapColumns {
            it.flip()
        }
    } else {
        this.mapRows {
            it.flip()
        }
    }
}

/**
 * Differentiates the vector by wavelet transformation using the mexican-hat wavelet.
 * @param scale Wavelet scale parameter
 * @param dt Sampling time
 */
fun SincMatrix.diffWithWavelet(scale: Double, dt: Double, dim: Int = 1): SincMatrix = if (this.isvector()) {
    val signal = this.asRowMajorArray()
    SincMatrix(
        rowMajArray = diffCWTFTWorker(signal, signal.size, scale, dt),
        m = this.numRows(),
        n = this.numCols()
    )
} else {
    if (dim == 1) {
        this.mapColumns {
            it.diffWithWavelet(scale, dt)
        }
    } else {
        this.mapRows {
            it.diffWithWavelet(scale, dt)
        }
    }
}
