import kotlin.math.abs

class SincMatrixSignalProc {

    private fun testVectorConv() {
        // Octave code
        //  format long
        //  A = -10:10;
        //  B = 1:0.3:6;
        //  M = conv(A, B, 'full') * conv(A', B', 'full');
        //  N = conv(A, B, 'same') * conv(A', B', 'same');
        //  O = conv(A, B, 'valid') * conv(A', B', 'valid');
        //  P = conv(B, A, 'full') * conv(B', A', 'full');
        //  Q = conv(B, A, 'same') * conv(B', A', 'same');
        //  result = M + N + O + P + Q
        val resultOctave = 3086686.720000000
        val testTol = 1E-8
        val A = SincMatrix.init(mlScript = "-10:10")
        val B = SincMatrix.init(mlScript = "1:0.3:6")
        val M = (A.conv(B = B, shape = ConvolutionShape.full) * A.transpose()
            .conv(B = B.transpose(), shape = ConvolutionShape.full))
        val N = (A.conv(B = B, shape = ConvolutionShape.same) * A.transpose()
            .conv(B = B.transpose(), shape = ConvolutionShape.same))
        val O = (A.conv(B = B, shape = ConvolutionShape.valid) * A.transpose()
            .conv(B = B.transpose(), shape = ConvolutionShape.valid))
        val P = (B.conv(B = A, shape = ConvolutionShape.full) * B.transpose()
            .conv(B = A.transpose(), shape = ConvolutionShape.full))
        val Q = (B.conv(B = A, shape = ConvolutionShape.same) * B.transpose()
            .conv(B = A.transpose(), shape = ConvolutionShape.same))
        val result = (((M + N) + (O + P)) + Q).asArray().firstOrNull()!!
        SincMathsTests.assert(abs(resultOctave - result) < testTol) { "testVectorConv failed..." }
    }

    fun runSignalProcTests() {
        testVectorConv()
    }
}