internal actual fun convWorker(A: DoubleArray, B: DoubleArray): DoubleArray = convWithWavelib(A, A.count(), B, B.count())

private external fun convWithWavelib(
    signalVector: DoubleArray,
    signalLength: Int,
    kernelVector: DoubleArray,
    kernelLength: Int
): DoubleArray