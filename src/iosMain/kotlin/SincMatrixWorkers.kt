import kotlinx.cinterop.*
import platform.Accelerate.vDSP_convD
import platform.Accelerate.vDSP_vrvrsD
import platform.Accelerate.vDSP_vsmulD
import tinyexpr.te_interp
import wavelib.diff_cwtft

internal actual fun convWorker(A: DoubleArray, B: DoubleArray): DoubleArray {

    val initArray = DoubleArray(B.size - 1) {0.0}
    val midArray = A
    val finalArray = DoubleArray(B.size){0.0}
    val paddedArray = initArray + midArray + finalArray

    val bufferLength = intArrayOf(A.size + B.size - 1, A.size, B.size).maxOrNull() ?: 0
    val buffer = nativeHeap.allocArray<DoubleVar>(bufferLength)
    val negativeReverseBuffer = nativeHeap.allocArray<DoubleVar>(bufferLength)

    vDSP_convD(paddedArray.toCValues(), 1L, B.toCValues(), 1L, buffer, 1L, bufferLength.toULong(), B.size.toULong())
    vDSP_vrvrsD(buffer, 1, bufferLength.toULong())
    vDSP_vsmulD(buffer, 1L, doubleArrayOf(-1.0).toCValues(), negativeReverseBuffer, 1L, bufferLength.toULong())

    val returnData = negativeReverseBuffer.createCopyArray(bufferLength)
    nativeHeap.free(buffer)
    nativeHeap.free(negativeReverseBuffer)

    return returnData
}

internal actual fun parseToInt(expression: String): Int? {
    val doubleValue = parseToDouble(expr = expression) ?: return null
    return doubleValue.toInt()

}

private fun parseToDouble(expr: String) : Double? {
    val error = nativeHeap.alloc<IntVar>()
    error.value = -1
    val doubleValue = te_interp(expr, error.ptr)

    val errorVal = error.value

    nativeHeap.free(error)

    return if (errorVal == 0) {
        doubleValue
    } else {
        null
    }
}

/**
 * Takes date and date format string to produce a time stamp in seconds which represents time since 1970
 */
internal actual fun dateToTimeStampWorker(dateFormat: String, date: String): Double {
    TODO("Not yet implemented")
}

internal actual fun fileReadWorker(filePath: String): String? {
    TODO("Not yet implemented")
}

internal actual fun diffCWTFTWorker (
    signalVector: DoubleArray,
    signalLength: Int,
    scale: Double,
    dt: Double
): DoubleArray {
    val outVector = nativeHeap.allocArray<DoubleVar>(signalLength)
    diff_cwtft(signalVector.toCValues(), outVector, signalLength, scale, dt)

    val returnArray = outVector.createCopyArray(signalLength)
    nativeHeap.free(outVector)

    return returnArray
}