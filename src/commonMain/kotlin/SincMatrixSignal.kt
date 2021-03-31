import kotlin.math.roundToInt

fun SincMatrix.conv(B: SincMatrix, shape: ConvolutionShape = ConvolutionShape.full): SincMatrix {

    require(this.isvector() && B.isvector()) {
        "SMError: This function works only for vectors"
    }

    var isColumnFlag = false

    if (this.iscolumn()) {
        isColumnFlag = true
    }

    val convResult = convWorker(this.asArray(), B.asArray())
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