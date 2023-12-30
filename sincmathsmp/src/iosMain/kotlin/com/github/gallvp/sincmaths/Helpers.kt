package com.github.gallvp.sincmaths

import kotlinx.cinterop.CArrayPointer
import kotlinx.cinterop.DoubleVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.get

@OptIn(ExperimentalForeignApi::class)
internal fun CArrayPointer<DoubleVar>.createCopyArray(length: Int): DoubleArray {
    val doubleArray = DoubleArray(length) { Double.NaN }
    for (i in 0 until length) {
        doubleArray[i] = this[i]
    }
    return doubleArray
}
