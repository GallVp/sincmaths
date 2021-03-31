import kotlinx.cinterop.*

internal fun CArrayPointer<DoubleVar>.createCopyArray(length: Int):DoubleArray {
    val doubleArray = DoubleArray(length) { Double.NaN}
    for(i in 0 until length){
        doubleArray[i] = this[i]
    }
    return doubleArray
}