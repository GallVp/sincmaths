import java.text.SimpleDateFormat

private external fun convWithWavelib(
    signalVector: DoubleArray,
    signalLength: Int,
    kernelVector: DoubleArray,
    kernelLength: Int
): DoubleArray

private external fun parseToDouble(expr: String): Double

internal external fun diffCWTFT(
    signalVector: DoubleArray,
    signalLength: Int,
    scale: Double,
    dt: Double
): DoubleArray

internal actual fun convWorker(A: DoubleArray, B: DoubleArray): DoubleArray = convWithWavelib(A, A.count(), B, B.count())

internal actual fun parseToInt(expression: String): Int? {
    val ans = parseToDouble(expression)

    return if(ans.isNaN()) {null} else {ans.toInt()}
}

internal actual fun dateToTimeStampWorker(dateFormat: String, date:String): Double {
    val dateFormatter = SimpleDateFormat(dateFormat)
    val timeMillis = dateFormatter.parse(date)
    return if(timeMillis != null) {
        timeMillis.time / 1000.0 // date.time is in milliseconds.
    } else {
        -1.0
    }
}
internal actual fun fileReadWorker(filePath: String): String? = SincMatrix.readFileAsTextUsingInputStream(filePath)

private fun SincMatrix.Companion.readFileAsTextUsingInputStream(fileName: String): String? = SincMatrix::class.java.getResourceAsStream(fileName)?.readBytes()?.toString(Charsets.UTF_8)