package sincmaths.sincmatrix.helpers

import kotlinx.cinterop.CArrayPointer
import kotlinx.cinterop.DoubleVar
import kotlinx.cinterop.get

internal fun CArrayPointer<DoubleVar>.createCopyArray(length: Int): DoubleArray {
    val doubleArray = DoubleArray(length) { Double.NaN }
    for (i in 0 until length) {
        doubleArray[i] = this[i]
    }
    return doubleArray
}