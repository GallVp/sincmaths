package io.github.gallvp.sincmaths

import kotlinx.cinterop.DoubleVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.IntVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.ptr
import kotlinx.cinterop.toCValues
import kotlinx.cinterop.value
import tinyexpr.te_interp
import wavelib.diff_cwtft

internal actual fun parseToInt(expression: String): Int? {
    val doubleValue = parseToDouble(expr = expression) ?: return null
    return doubleValue.toInt()
}

@OptIn(ExperimentalForeignApi::class)
private fun parseToDouble(expr: String): Double? {
    memScoped {
        val error = nativeHeap.alloc<IntVar>()
        error.value = -1
        val doubleValue = te_interp(expr, error.ptr)

        val errorVal = error.value

        return if (errorVal == 0) {
            doubleValue
        } else {
            null
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun diffCWTFTWorker(
    signalVector: DoubleArray,
    signalLength: Int,
    scale: Double,
    dt: Double,
): DoubleArray {
    memScoped {
        val outVector = nativeHeap.allocArray<DoubleVar>(signalLength)
        diff_cwtft(signalVector.toCValues(), outVector, signalLength, scale, dt)
        return outVector.createCopyArray(signalLength)
    }
}
