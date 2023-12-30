package com.github.gallvp.sincmaths

/**
 * For example usage see SincMathsTests/SincMatrixIO.
 * @param headerInfo An array of column types. Following column types are allowed:
 * d: Double
 * t: Date which is then converted to double as time interval in seconds since 1970.
 * If headerInfo is empty, no header row is assumed. Default date format: "yyyy-MM-dd HH:mm:ss.SSS"
 * @param bundleID A string identifier for the iOS bundle which contains the file.
 */
fun SincMatrix.Companion.csvRead(
    filePath: String,
    separator: String = ",",
    headerInfo: List<String> = listOf(),
    dateFormat: String = "yyyy-MM-dd HH:mm:ss.SSS",
    bundleID: String? = null,
): SincMatrix {
    val contents: String? = fileReadWorker(filePath, bundleID)
    require(!contents.isNullOrEmpty()) { "File: $filePath is empty" }

    val text: ArrayList<ArrayList<String>> = ArrayList()
    val rows = contents.split("\n")
    for (row in rows) {
        val rowData = row.split(separator)
        text.add(rowData.toCollection(ArrayList()))
    }
    var removeRows = 0
    if (text.lastOrNull()?.firstOrNull() == "") {
        removeRows = 1
    }
    val data: ArrayList<Double> = ArrayList()
    data.ensureCapacity(text.size * text[0].size)
    val startRow: Int =
        if (headerInfo.isEmpty()) {
            0
        } else {
            1
        }
    for (i in startRow until (text.size - removeRows)) {
        val rowItem = text[i]
        for (j in 0 until rowItem.size) {
            val columnItem = rowItem[j]
            if (headerInfo.isEmpty()) {
                data.add(columnItem.toDouble())
            } else {
                when (headerInfo[j]) {
                    "t" -> {
                        data.add(
                            dateToTimeStampWorker(dateFormat, columnItem),
                        )
                    }
                    "d" -> {
                        data.add(columnItem.toDouble())
                    }
                    else -> data.add(columnItem.toDouble())
                }
            }
        }
    }
    return SincMatrix(data.toDoubleArray(), text.size - startRow - removeRows, text[0].size)
}
