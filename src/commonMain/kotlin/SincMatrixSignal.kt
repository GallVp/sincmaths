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

            val validArray =
                convSegment.drop(B.numel() - 1).dropLast(B.numel() - 1).toDoubleArray()
            val resultMat = SincMatrix(rowMajArray = validArray, m = 1, n = validArray.size)
            return if (isColumnFlag) {
                resultMat.transpose()
            } else {
                resultMat
            }
        }
    }
}

fun SincMatrix.diff(): SincMatrix {
    require(this.isvector()) { "SMError: This function works only for vectors" }

    val kernel = SincMatrix(rowMajArray = doubleArrayOf(1.0, -1.0), m = 1, n = 2)
    return this.conv(B = kernel, shape = ConvolutionShape.valid)
}

fun SincMatrix.movsum(wlen: Int, endpoints: MovWinShape = MovWinShape.shrink): SincMatrix {

    require(this.numel() > 0) { "SMError: Number of elements should be greater than zero" }
    require(wlen > 0) { "SMError: Length of moving window should be > 0" }
    require(this.isvector()) { "SMError: This function works only for vectors" }

    val convKernel = SincMatrix.ones(m = 1, n = wlen)
    when (endpoints) {
        MovWinShape.discard -> {
            return this.conv(B = convKernel, shape = ConvolutionShape.valid)
        }
        MovWinShape.shrink -> {
            return if (wlen % 2 < 1) {
                //Even
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
}

fun SincMatrix.movmean(wlen: Int, endpoints: MovWinShape = MovWinShape.shrink): SincMatrix {

    require(this.isvector()) { "SMError: This function works only for vectors" }

    return when (endpoints) {
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
        SincMatrix.createMatFromColumns(this.getMatrixCols().map {
            SincMatrix(filtfiltWorker(B, A, it.asRowMajorArray()), numRows(), 1)
        })
    }
}

/**
 * This function is like Octave's sgolayfilt, except that instead of order and filter length
 * Savitzky-Golay a pre-computed projection matrix ([B]) is supplied.
 */
fun SincMatrix.sgolayfilter(B: SincMatrix): SincMatrix {

    return if (this.isvector()) {
        SincMatrix.sgolayWorker(this, B)
    } else {
        SincMatrix.createMatFromColumns(this.getMatrixCols().map {
            SincMatrix.sgolayWorker(it, B)
        })
    }
}

fun SincMatrix.acf(numLags: Int): SincMatrix {

    require(this.isvector()) { "SMError: This function works only for vectors" }
    require(numLags < this.numel()) {
        "SMError: No. of lags should be smaller than the length of the vector"
    }

    val detrendMatrix = (this + (-this.mean().asScalar()))
    val convSum = detrendMatrix.conv(B = detrendMatrix.flip())
    val scale = 1.0 / detrendMatrix.dot(detrendMatrix)
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