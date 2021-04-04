import kotlinx.cinterop.*
import platform.Accelerate.vDSP_convD
import platform.Accelerate.vDSP_vrvrsD
import platform.Accelerate.vDSP_vsmulD
import platform.Foundation.*
import tinyexpr.te_interp
import wavelib.diff_cwtft

internal actual fun convWorker(A: DoubleArray, B: DoubleArray): DoubleArray {

    val initArray = createZerosVector(B.size - 1)
    val midArray = A
    val finalArray = createZerosVector(B.size)
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
    val dateFormatter = NSDateFormatter()
    dateFormatter.dateFormat = dateFormat
    val dateValue = dateFormatter.dateFromString(date)
    return dateValue?.timeIntervalSince1970() ?: -1.0
}

internal actual fun fileReadWorker(filePath: String, bundleID:String?): String? {

    val filePathInBundle = getFilePath(filePath, bundleID)

    return if(filePathInBundle != null) {
        NSString.stringWithContentsOfFile(filePathInBundle, NSUTF8StringEncoding, null)
    } else {
        null
    }
}

private fun getFilePath(filePath: String, bundleID:String?):String? {

    val fileTokens = filePath.split(".")
    if(fileTokens.count() != 2) {
        return null
    }

    // Check for the named bundle
    var selectedBundle: NSBundle? = null
    if(bundleID != null) {
        for(bundle in NSBundle.allBundles()) {
            if((bundle as NSBundle).bundleIdentifier == bundleID) {
                selectedBundle = bundle
                break
            }
        }
    }
    if(selectedBundle == null) {
        selectedBundle = NSBundle.mainBundle
    }

    if(!selectedBundle.loaded) {
        selectedBundle.load()
    }

    return NSBundle.mainBundle.pathForResource(fileTokens.first(), fileTokens.last())
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