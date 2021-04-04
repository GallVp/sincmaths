import kotlin.math.floor

/**
 * Creates a SincMatrix from Octave scripts, such as:
 *
 * SincMatrix.from("[1, 2, 3; 4, 5, 6]")
 *
 * SincMatrix.from("[1, 2, 3, 4]")
 *
 * SincMatrix.from("[5;6;7;8;9;10]")
 *
 * SincMatrix.from("1:10")
 *
 * SincMatrix.from("-1.5:-1:-7.9")
 */
fun SincMatrix.Companion.from(script: String): SincMatrix {
    if (script.contains(":")) {
        //2:5 or 1:2:7 type expression
        val range = createRange(script.trim())
        return SincMatrix(range, 1, range.size)
    } else {
        //[1, 2, 3; 4, 5, 6] type expression
        val mlScriptTrimmed = script.trim()
        val firstBrace = mlScriptTrimmed.first()
        val lastBrace = mlScriptTrimmed.last()
        require(firstBrace == '[' && lastBrace == ']') {
            "SMError: Incorrect input format. Braces ([, ]) are missing."
        }
        var parseData = mlScriptTrimmed.dropLast(1)
        parseData = parseData.drop(1)
        val dataRows = parseData.split(";")
        // Findout the size of the matrix
        val m = dataRows.size
        val n = dataRows.first().split(",").size
        // Initialise the matrix
        val rMat = SincMatrix(DoubleArray(m*n), m, n)
        var ithRow = 1
        var jthColumn: Int
        for (row in dataRows) {
            jthColumn = 1
            val rowValues = row.split(",")
            for (rowValue in rowValues) {
                val doubleValue = rowValue.trim().toDouble()
                rMat[ithRow, jthColumn] = doubleValue
                jthColumn += 1
            }
            ithRow += 1
        }

        return rMat
    }
}

private fun SincMatrix.Companion.createRange(mlScript: String): DoubleArray {
    val literals = mlScript.split(":")
    if (literals.size == 1) {
        return doubleArrayOf(literals[0].trim().toDouble())
    }
    val startPoint: Double
    val change: Double
    val endPoint: Double
    if (literals.size == 3) {
        // 1:2:7 type
        startPoint = literals[0].toDouble()
        change = literals[1].toDouble()
        endPoint = literals[2].toDouble()
    } else {
        //2:5 type
        startPoint = literals[0].toDouble()
        endPoint = literals[1].toDouble()
        change = if (endPoint < startPoint) {
            -1.0
        } else {
            1.0
        }
    }

    val numElements = floor((endPoint - startPoint) / change).toInt() + 1

    return (0 until numElements).map {
        it * change + startPoint
    }.toDoubleArray()
}

fun SincMatrix.Companion.zeros(m: Int, n: Int): SincMatrix = DoubleArray(m * n) {
    0.0
}.asSincMatrix(m, n)

fun SincMatrix.Companion.ones(m: Int, n: Int): SincMatrix = DoubleArray(m * n) {
    1.0
}.asSincMatrix(m, n)

fun SincMatrix.Companion.nans(m: Int, n: Int): SincMatrix {
    val data = DoubleArray(m * n) {
        Double.NaN
    }
    return SincMatrix(data, m, n)
}