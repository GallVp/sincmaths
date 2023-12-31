package io.github.gallvp.sincmaths

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSBundle
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringWithContentsOfFile
import platform.Foundation.timeIntervalSince1970

internal actual fun convWorker(
    aArray: DoubleArray,
    bArray: DoubleArray,
) = convolveVectors(aArray, bArray)

/**
 * Takes date and date format string to produce a time stamp in seconds which represents time since 1970
 */
internal actual fun dateToTimeStampWorker(
    dateFormat: String,
    date: String,
): Double {
    val dateFormatter = NSDateFormatter()
    dateFormatter.dateFormat = dateFormat
    val dateValue = dateFormatter.dateFromString(date)
    return dateValue?.timeIntervalSince1970() ?: -1.0
}

@OptIn(ExperimentalForeignApi::class)
internal actual fun fileReadWorker(
    filePath: String,
    bundleID: String?,
): String? {
    val filePathInBundle = getFilePath(filePath, bundleID)
    require(filePathInBundle != null) { "Failed to get path of file: $filePath in bundle: $bundleID" }
    return NSString.stringWithContentsOfFile(filePathInBundle, NSUTF8StringEncoding, null)
}

private fun getFilePath(
    filePath: String,
    bundleID: String?,
): String? {
    val fileTokens = filePath.split(".")
    require(fileTokens.count() == 2) {
        "File name: $filePath does not comply with format: " +
            "filename.ext"
    }
    // Check for the named bundle
    var selectedBundle: NSBundle? = null
    if (bundleID != null) {
        for (bundle in NSBundle.allBundles()) {
            if ((bundle as NSBundle).bundleIdentifier == bundleID) {
                selectedBundle = bundle
                break
            }
        }
    }
    if (selectedBundle == null) {
        selectedBundle = NSBundle.mainBundle
    }

    if (!selectedBundle.loaded) {
        selectedBundle.load()
    }

    return selectedBundle.pathForResource(fileTokens.first(), fileTokens.last())
}
