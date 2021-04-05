import platform.Foundation.*

internal actual fun convWorker(A: DoubleArray, B: DoubleArray) = convolveVectors(A, B)

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