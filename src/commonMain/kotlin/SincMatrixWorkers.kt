internal expect fun convWorker(A:DoubleArray, B:DoubleArray):DoubleArray
internal expect fun parseToInt(expression: String):Int?
internal expect fun filtfiltWorker(xCoefs: DoubleArray, yCoefs: DoubleArray, data: DoubleArray): DoubleArray

/**
 * Takes date and date format string to produce a time stamp in seconds which represents time since 1970
 */
internal expect fun dateToTimeStampWorker(dateFormat: String, date:String): Double
internal expect fun fileReadWorker(filePath: String): String?

internal fun SincMatrix.Companion.sgolayWorker(vectorIn: SincMatrix, B: SincMatrix): SincMatrix {
    var vector = vectorIn.copyOf()
    val vectorIsRow = vector.isrow()
    val filterLength = B.size().first()
    if (vectorIsRow) {
        vector = vector.transpose()
    }
    var startsAt = (filterLength - 1) / 2 + 2
    var endsAt = B.size().first()
    val initialSegment = B.getRows(
        mlRows = (startsAt..endsAt).reversed().toList().toIntArray()
    ) * vector.getRows(mlRows = (1..filterLength).reversed().toList().toIntArray())
    val convCoeffs = B.getRow(mlRow = (filterLength - 1) / 2 + 1)
    val middleSegment = vector.conv(B = convCoeffs, shape = ConvolutionShape.valid)
    startsAt = 1
    endsAt = (filterLength - 1) / 2
    val finalSegment = B.getRows(
        mlRows = (startsAt..endsAt).reversed().toList().toIntArray()
    ) * vector.getRows(
        mlRows = ((vector.numel() - (filterLength - 1))..vector.numel()).reversed()
            .toList().toIntArray()
    )
    val computedVector =
        initialSegment.asRowMajorArray() + middleSegment.asRowMajorArray() + finalSegment.asRowMajorArray()
    var returnVector =
        SincMatrix(rowMajArray = computedVector, m = vector.numRows(), n = vector.numCols())
    if (vectorIsRow) {
        returnVector = returnVector.transpose()
    }
    return returnVector
}