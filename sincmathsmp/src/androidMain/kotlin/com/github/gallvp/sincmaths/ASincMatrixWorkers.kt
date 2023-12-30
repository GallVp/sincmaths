package com.github.gallvp.sincmaths

import java.text.SimpleDateFormat
import java.util.Locale

private external fun convWithWavelib(
    signalVector: DoubleArray,
    signalLength: Int,
    kernelVector: DoubleArray,
    kernelLength: Int,
): DoubleArray

private external fun parseToDouble(expr: String): Double

internal actual fun diffCWTFTWorker(
    signalVector: DoubleArray,
    signalLength: Int,
    scale: Double,
    dt: Double,
) = diffCWTFT(signalVector, signalLength, scale, dt)

private external fun diffCWTFT(
    signalVector: DoubleArray,
    signalLength: Int,
    scale: Double,
    dt: Double,
): DoubleArray

internal actual fun convWorker(
    aArray: DoubleArray,
    bArray: DoubleArray,
): DoubleArray = convWithWavelib(aArray, aArray.count(), bArray, bArray.count())

internal actual fun parseToInt(expression: String): Int? {
    val ans = parseToDouble(expression)

    return if (ans.isNaN()) {
        null
    } else {
        ans.toInt()
    }
}

internal actual fun dateToTimeStampWorker(
    dateFormat: String,
    date: String,
): Double {
    val dateFormatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    val timeMillis = dateFormatter.parse(date)
    return if (timeMillis != null) {
        timeMillis.time / 1000.0 // date.time is in milliseconds.
    } else {
        -1.0
    }
}

internal actual fun fileReadWorker(
    filePath: String,
    bundleID: String?,
): String? = SincMatrix.readFileAsTextUsingInputStream(filePath)

private fun SincMatrix.Companion.readFileAsTextUsingInputStream(fileName: String): String? =
    SincMatrix.Companion::class.java.getResourceAsStream(fileName)?.readBytes()
        ?.toString(Charsets.UTF_8)
