expect class SincMatrix(rowMajArray: DoubleArray, m: Int, n: Int) {

    fun numRows():Int
    fun numCols():Int

    //fun asArray():DoubleArray
    fun asRowMajorArray():DoubleArray

    /**
     * Indexing starts at 1, like Octave/MATLAB.
     */
    operator fun set(mlRow: Int, mlCol: Int, value: Double)

    /**
     * Indexing starts at 1, like Octave/MATLAB.
     * @return A column vector.
     */
    operator fun SincMatrix.get(indices: IntArray): SincMatrix

    /**
     * Indexing starts at 1, like Octave/MATLAB.
     * @return A column vector.
     */
    operator fun SincMatrix.get(indices: IntRange): SincMatrix

    fun transpose():SincMatrix
    fun cross(ontoVector: SincMatrix):SincMatrix
    fun dot(rhs: SincMatrix):Double

    operator fun times(rhs: SincMatrix): SincMatrix
    operator fun times(rhs: Double): SincMatrix
    operator fun plus(rhs: SincMatrix): SincMatrix
    operator fun plus(rhs: Double): SincMatrix

    infix fun elDiv(rhs: SincMatrix):SincMatrix
    infix fun elMul(rhs: SincMatrix):SincMatrix
    fun elSum():Double
    infix fun elPow(power:Double):SincMatrix

    fun floor():SincMatrix
    fun abs():SincMatrix

    fun sin():SincMatrix
    fun cos():SincMatrix

    fun flip():SincMatrix
    fun find():SincMatrix

    /**
     * Only second order filters are supported. Thus, length(B) == length(A) == 3 is assumed.
     */
    fun filter(B: DoubleArray, A: DoubleArray):SincMatrix
    /**
     * Differentiates the vector by wavelet transformation using the mexican-hat wavelet.
     * @param scale Wavelet scale parameter
     * @param dt Sampling time
     */
    fun diffWithWavelet(scale:Double, dt:Double):SincMatrix

    companion object {
        /**
         * For example usage see SincMathsTests/SincMatrixIO.
         * @param headerInfo An array of column types. Following column types are allowed:
         * d: Double
         * t: Date which is then converted to double as time interval since 1970.
         * If headerInfo is empty, no header row is assumed. Default date format: "yyyy-MM-dd HH:mm:ss.SSS"
         */
        fun csvread(
            filePath: String,
            separator: String = ",",
            headerInfo: List<String> = listOf(),
            dateFormat: String = "yyyy-MM-dd HH:mm:ss.SSS"
        ): SincMatrix
    }
}