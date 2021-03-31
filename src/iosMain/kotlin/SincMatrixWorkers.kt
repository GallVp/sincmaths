import kotlinx.cinterop.*
import platform.Accelerate.vDSP_convD
import platform.Accelerate.vDSP_vrvrsD
import platform.Accelerate.vDSP_vsmulD

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