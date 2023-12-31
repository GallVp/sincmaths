package io.github.gallvp.sincmaths

import kotlin.math.roundToInt

fun SincMatrix.conv(
    bVector: SincMatrix,
    shape: ConvolutionShape = ConvolutionShape.FULL,
): SincMatrix {
    require(this.isVector && bVector.isVector) {
        "This function works only for vectors"
    }

    var isColumnFlag = false

    if (this.isColumn) {
        isColumnFlag = true
    }

    val convResult = convWorker(this.asRowMajorArray(), bVector.asRowMajorArray())
    val convSegmentSize =
        intArrayOf(this.numel + bVector.numel - 1, this.numel, bVector.numel).maxOrNull() ?: 0
    val convSegment = convResult.sliceArray(0 until convSegmentSize)

    when (shape) {
        ConvolutionShape.FULL -> {
            val resultMat =
                SincMatrix(
                    rowMajArray = convSegment,
                    m = 1,
                    n = convSegmentSize,
                )
            return if (isColumnFlag) {
                resultMat.transpose
            } else {
                resultMat
            }
        }

        ConvolutionShape.SAME -> {
            val startPoint =
                ((convSegmentSize - this.numel).toDouble() / 2.0).roundToInt() + 1
            val resultMat =
                SincMatrix(
                    rowMajArray = convSegment,
                    m = 1,
                    n = convSegmentSize,
                ).getCols((startPoint until startPoint + this.numel).toList().toIntArray())
            return if (isColumnFlag) {
                resultMat.transpose
            } else {
                resultMat
            }
        }

        ConvolutionShape.VALID -> {
            val validArray =
                convSegment.drop(bVector.numel - 1).dropLast(bVector.numel - 1).toDoubleArray()
            val resultMat =
                SincMatrix(
                    rowMajArray = validArray,
                    m = 1,
                    n = validArray.size,
                )
            return if (isColumnFlag) {
                resultMat.transpose
            } else {
                resultMat
            }
        }
    }
}

fun SincMatrix.diff(dim: Int = 1): SincMatrix =
    if (this.isVector) {
        val kernel = SincMatrix(doubleArrayOf(1.0, -1.0), 1, 2)
        this.conv(kernel, ConvolutionShape.VALID)
    } else {
        if (dim == 1) {
            this.mapColumns(this.numRows - 1) {
                it.diff()
            }
        } else {
            this.mapRows(this.numCols - 1) {
                it.diff()
            }
        }
    }

fun SincMatrix.movSum(
    wLen: Int,
    endpoints: MovWinShape = MovWinShape.SHRINK,
    dim: Int = 1,
): SincMatrix =
    if (this.isVector) {
        require(this.numel > 0) { "Number of elements should be greater than zero" }
        require(wLen > 0) { "Length of moving window should be > 0" }

        val convKernel = SincMatrix.ones(m = 1, n = wLen)
        when (endpoints) {
            MovWinShape.DISCARD -> {
                this.conv(bVector = convKernel, shape = ConvolutionShape.VALID)
            }

            MovWinShape.SHRINK -> {
                if (wLen % 2 < 1) { // Even
                    val computedSegment =
                        this.conv(bVector = convKernel, shape = ConvolutionShape.SAME)
                    val computedArray = computedSegment.asRowMajorArray()
                    val movSumTwoToEnd = computedArray.sliceArray(0 until (computedArray.size - 1))
                    // Guaranteed: len(computedArray) >= 2
                    val windowEndpoint = wLen / 2 - 1
                    require(windowEndpoint < this.numel) { "Window length is incompatible with shape = shrink" }
                    val firstElement =
                        this[(1..(windowEndpoint + 1))].sum().asScalar()
                    doubleArrayOf(
                        firstElement,
                        *movSumTwoToEnd,
                    ).asSincMatrix(computedSegment.numRows, computedSegment.numCols)
                } else {
                    // Odd
                    this.conv(bVector = convKernel, shape = ConvolutionShape.SAME)
                }
            }
        }
    } else {
        if (dim == 1) {
            this.mapColumnsToList {
                it.movSum(wLen, endpoints)
            }.makeMatrixFrom(false)
        } else {
            this.mapRowsToList {
                it.movSum(wLen, endpoints)
            }.makeMatrixFrom()
        }
    }

fun SincMatrix.movMean(
    wLen: Int,
    endpoints: MovWinShape = MovWinShape.SHRINK,
    dim: Int = 1,
): SincMatrix =
    if (this.isVector) {
        when (endpoints) {
            MovWinShape.DISCARD -> {
                val convKernel =
                    SincMatrix.ones(1, wLen) / wLen.toDouble()
                this.conv(bVector = convKernel, shape = ConvolutionShape.VALID)
            }

            MovWinShape.SHRINK -> {
                val movSumResult = this.movSum(wLen = wLen, endpoints = MovWinShape.SHRINK)
                val numPoints =
                    SincMatrix.ones(1, this.numel)
                        .movSum(wLen = wLen, endpoints = MovWinShape.SHRINK)
                if (movSumResult.isRow) {
                    (movSumResult elDiv numPoints)
                } else {
                    (movSumResult elDiv numPoints.transpose)
                }
            }
        }
    } else {
        if (dim == 1) {
            this.mapColumnsToList {
                it.movMean(wLen, endpoints)
            }.makeMatrixFrom(false)
        } else {
            this.mapRowsToList {
                it.movMean(wLen, endpoints)
            }.makeMatrixFrom()
        }
    }

/**
 * Only second order filters are supported. Thus, length(B) == length(A) == 3 is assumed.
 */
fun SincMatrix.filter(
    bVector: DoubleArray,
    aVector: DoubleArray,
    dim: Int = 1,
): SincMatrix =
    if (this.isVector) {
        require((bVector.size == 3) && (aVector.size == 3)) {
            "Only 2nd order coefficients are allowed. Thus, length(B) == length(A) == 3"
        }

        SincMatrix(
            rowMajArray =
                filterWorker(
                    bVector,
                    aVector,
                    this.asRowMajorArray(),
                    doubleArrayOf(0.0, 0.0),
                ),
            m = this.numRows,
            n = this.numCols,
        )
    } else {
        if (dim == 1) {
            this.mapColumns {
                SincMatrix(
                    rowMajArray =
                        filterWorker(
                            bVector,
                            aVector,
                            it.asRowMajorArray(),
                            doubleArrayOf(0.0, 0.0),
                        ),
                    m = it.numRows,
                    n = it.numCols,
                )
            }
        } else {
            this.mapRows {
                SincMatrix(
                    rowMajArray =
                        filterWorker(
                            bVector,
                            aVector,
                            it.asRowMajorArray(),
                            doubleArrayOf(0.0, 0.0),
                        ),
                    m = it.numRows,
                    n = it.numCols,
                )
            }
        }
    }

/**
 * Only second order filters are supported. Thus, length(B) == length(A) == 3 is assumed.
 */
fun SincMatrix.filtFilt(
    bVector: DoubleArray,
    aVector: DoubleArray,
): SincMatrix {
    require((bVector.size == 3) && (aVector.size == 3)) {
        "Only 2nd order coefficients are allowed. Thus, length(B) == length(A) == 3"
    }

    return if (this.isVector) {
        SincMatrix(
            filtfiltWorker(
                bVector,
                aVector,
                this.asRowMajorArray(),
            ),
            numRows,
            numCols,
        )
    } else {
        this.mapColumns {
            filtfiltWorker(bVector, aVector, it.asRowMajorArray()).asSincMatrix(false)
        }
    }
}

/**
 * This function is like Octave's sgolayfilt, except that instead of order and filter length
 * Savitzky-Golay a pre-computed projection matrix ([bMatrix]) is supplied.
 */
fun SincMatrix.sgolayFilter(
    bMatrix: SincMatrix,
    dim: Int = 1,
): SincMatrix {
    return if (this.isVector) {
        sgolayWorker(this, bMatrix)
    } else {
        if (dim == 1) {
            this.mapColumns {
                sgolayWorker(it, bMatrix)
            }
        } else {
            this.mapRows {
                sgolayWorker(it, bMatrix)
            }
        }
    }
}

/**
 * Citation: [Matlab central](https://au.mathworks.com/matlabcentral/fileexchange/30540-autocorrelation-function-acf)
 */
fun SincMatrix.acf(numLags: Int): SincMatrix {
    require(this.isVector) { "This function works only for vectors" }
    require(numLags < this.numel) {
        "No. of lags should be smaller than the length of the vector"
    }

    val zeroMeanVector = this - this.mean().scalar
    val convSum = zeroMeanVector.conv(bVector = zeroMeanVector.flip())
    val scale = 1.0 / zeroMeanVector.dot(zeroMeanVector).scalar
    val scaledConvSum = convSum * scale
    val acfElements = this.numel + 1..this.numel + numLags
    return if (this.isRow) {
        scaledConvSum[this.rowIndicesRange, acfElements]
    } else {
        scaledConvSum[acfElements, this.colIndicesRange]
    }
}

fun SincMatrix.findPeaks(): SincMatrix {
    require(this.isVector) { "This function works only for vectors" }
    val manipulatedData = doubleArrayOf(Double.NaN) + this.asRowMajorArray() + Double.NaN
    val manipulatedMatrix =
        SincMatrix(
            rowMajArray = manipulatedData,
            m = manipulatedData.size,
            n = 1,
        )
    val resultA = manipulatedMatrix.diff().sign().diff().lessThan(0.0)
    val result =
        resultA.getRows(mlRows = (2 until this.numel).toList().toIntArray()).find() + 1.0
    return if (this.isRow) {
        result.transpose
    } else {
        result
    }
}

fun SincMatrix.cumSum(dim: Int = 1): SincMatrix =
    if (this.isVector) {
        val inputVector = this.asRowMajorArray()
        val resultVector = inputVector.copyOf()
        for (i in 1 until resultVector.size) {
            resultVector[i] = resultVector[i] + resultVector[i - 1]
        }
        SincMatrix(resultVector, this.numRows, this.numCols)
    } else {
        if (dim == 1) {
            this.mapColumns {
                it.cumSum()
            }
        } else {
            this.mapRows {
                it.cumSum()
            }
        }
    }

fun SincMatrix.flip(dim: Int = 1): SincMatrix =
    if (this.isVector) {
        SincMatrix(
            this.asRowMajorArray().reversedArray(),
            this.numRows,
            this.numCols,
        )
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
fun SincMatrix.diffWithWavelet(
    scale: Double,
    dt: Double,
    dim: Int = 1,
): SincMatrix =
    if (this.isVector) {
        val signal = this.asRowMajorArray()
        SincMatrix(
            rowMajArray = diffCWTFTWorker(signal, signal.size, scale, dt),
            m = this.numRows,
            n = this.numCols,
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
