import SincMathsTests.Companion.testTol
import kotlin.math.abs

class SincMatrixSignalProcTests {

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
        //  result = (M + N + O + P + Q) / sum(B)^3.5
        val resultOctave = 2.102544125534404
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
        val result = (((M + N) + (O + P)) + Q) elDiv (B.sum() elPow 3.5)
        SincMathsTests.assert(abs(resultOctave - result.scalar) < testTol)
    }

    private fun testMatrixDiff() {
        // Octave code
        //  format long
        //  rand("seed", 25)
        //  A = rand(1, 5);
        //  diff(A)*diff(A')
        val resultOctave = 1.080192559976076
        val A =
            SincMatrix.init(
                mlScript = "[4.418510198593140e-02, 4.279963076114655e-01, 8.155888915061951e-01, " +
                        "7.815348356962204e-02, 5.668686628341675e-01]"
            )
        val result = (A.diff() * A.t.diff()).scalar
        SincMathsTests.assert(abs(resultOctave - result) < testTol)
    }

    private fun testMatrixFilter() {
        // Octave code
        //  format long
        //  rand("seed", 101)
        //  testVector = rand(1, 5);
        //  B = [1 2 3];
        //  A = [4 5 6];
        //  filter(B, A, testVector)*filter(B, A, testVector')
        val resultOctave = 2.429411901632733e-01
        val testTol = 1E-12
        val testVector =
            SincMatrix.init(
                mlScript = "[1.650966703891754e-01, 9.907181560993195e-02, 9.253824949264526e-01, " +
                        "5.843927264213562e-01, 2.296017855405807e-01]"
            )
        val B = doubleArrayOf(1.0, 2.0, 3.0)
        val A = doubleArrayOf(4.0, 5.0, 6.0)
        val result = (testVector.filter(
            B = B,
            A = A
        ) * testVector.transpose().filter(B = B, A = A)).asArray().firstOrNull()!!
        SincMathsTests.assert(abs(resultOctave - result) < testTol) { "testMatrixFilter failed..." }
    }

    private fun testMatrixFiltfilt() {
        // MATLAB code
        //  format long
        //  B = [0.013359200027856, 0.026718400055713, 0.013359200027856];
        //  A = [1.000000000000000, -1.647459981076977, 0.700896781188403];
        //  testMat = sgolay(3, 41)
        //  sum(sum(filtfilt(B, A, testMat)*filtfilt(B, A, testMat')))
        val resultMATLAB = 40.999999999996817
        val testTol = 1E-12
        val testMat = SincMatrix.init(mlScript = SGCoeffs.sgo3x41)
        val B = doubleArrayOf(0.013359200027856, 0.026718400055713, 0.013359200027856)
        val A = doubleArrayOf(1.000000000000000, -1.647459981076977, 0.700896781188403)
        val R = testMat.filtfilt(B = B, A = A) * (testMat.transpose().filtfilt(B = B, A = A))
        val result = R.sum().sum().asScalar()
        SincMathsTests.assert(abs(resultMATLAB - result) < testTol) { "testMatrixFiltfilt failed..." }
    }

    private fun testVectorSgolayFilter() {
        // MATLAB code
        //  format long
        //  A = csvread('test_csv.csv');
        //  testVector = A(:, 2);
        //  sgolayfilt(testVector', 3, 41) * sgolayfilt(testVector, 3, 41)
        val resultMATLAB = 6.740799259697040
        val testTol = 1E-12
        val filePath = "test_csv.csv"
        val A = SincMatrix.csvread(
            filePath = filePath,
            separator = ",",
            headerInfo = listOf("t", "d", "d", "d", "d", "d", "d", "d", "d", "d")
        )
        val testVector = A.getCol(mlCol = 2)
        val B = SincMatrix.init(mlScript = SGCoeffs.sgo3x41)
        val result = (testVector.transpose()
            .sgolayfilter(B = B) * testVector.sgolayfilter(B = B)).asArray().firstOrNull()!!
        SincMathsTests.assert(
            abs(resultMATLAB - result) < testTol
        ) { "testMatrixCSVRead failed..." }
    }

    private fun testMatrixSgolayFilter() {
        // Octave code
        //  format long
        //  A = csvread('test_csv.csv');
        //  testMatrix = A(:, 2:4);
        //  sum(sum(sgolayfilt(testMatrix, 3, 7), 2))
        val resultMATLAB = -2.426886809212823e+03
        val testTol = 1E-11
        val filePath = "test_csv.csv"
        val A = SincMatrix.csvread(
            filePath = filePath,
            separator = ",",
            headerInfo = listOf("t", "d", "d", "d", "d", "d", "d", "d", "d", "d")
        )
        val testMatrix = A.get(mlScript = ":,2:4")
        val B = SincMatrix.init(mlScript = SGCoeffs.sgo3x7)
        val result = testMatrix.sgolayfilter(B = B).sum(dim = 2).sum().asScalar()
        SincMathsTests.assert(abs(resultMATLAB - result) < testTol) { "testMatrixSgolayFilter failed..." }
    }

    private fun testMovSum() {
        // Octave code
        //  format long
        //  rand("seed", 101)
        //  testVector = rand(1, 10);
        //  (movsum(testVector, 3) * movsum(testVector', 3)) * (movsum(testVector, 3, "Endpoints", "discard") * movsum(testVector', 3, "Endpoints", "discard"))
        val resultOctave = 190.0905953111295
        val testTol = 1E-12
        val testVector =
            SincMatrix.init(
                mlScript = "[1.650966703891754e-01, 9.907181560993195e-02, 9.253824949264526e-01, " +
                        "5.843927264213562e-01, 2.296017855405807e-01, 7.710580229759216e-01, 1.801824271678925e-01, " +
                        "3.308660686016083e-01, 2.962400913238525e-01, 5.188712477684021e-02]"
            )
        val result = ((testVector.movsum(wlen = 3) * testVector.transpose()
            .movsum(wlen = 3)) * (testVector.movsum(
            wlen = 3,
            endpoints = MovWinShape.discard
        ) * testVector.transpose()
            .movsum(wlen = 3, endpoints = MovWinShape.discard))).asArray().firstOrNull()!!
        SincMathsTests.assert(abs(resultOctave - result) < testTol) { "testMovSum failed..." }
    }

    private fun testMovMean() {
        // Octave code
        //  format long
        //  rand("seed", 101)
        //  testVector = rand(1, 10);
        //  (movmean(testVector, 3) * movmean(testVector', 3)) * (movmean(testVector, 3, "Endpoints", "discard") * movmean(testVector', 3, "Endpoints", "discard"))
        val resultOctave = 2.387150841604956
        val testTol = 1E-12
        val testVector =
            SincMatrix.init(
                mlScript = "[1.650966703891754e-01, 9.907181560993195e-02, 9.253824949264526e-01, " +
                        "5.843927264213562e-01, 2.296017855405807e-01, 7.710580229759216e-01, 1.801824271678925e-01, " +
                        "3.308660686016083e-01, 2.962400913238525e-01, 5.188712477684021e-02]"
            )
        val result = ((testVector.movmean(wlen = 3) * testVector.transpose()
            .movmean(wlen = 3)) * (testVector.movmean(
            wlen = 3,
            endpoints = MovWinShape.discard
        ) * testVector.transpose()
            .movmean(wlen = 3, endpoints = MovWinShape.discard))).asArray().firstOrNull()!!
        SincMathsTests.assert(abs(resultOctave - result) < testTol) { "testMovMean failed..." }
    }

    private fun testMovSumEvenDiscard() {
        // Octave code
        //  format long
        //  testVector = -97:0.31:97;
        //  sum(movsum(testVector, 42))
        val resultOctave = -3071.250000000089
        val testTol = 1E-7
        val testVector = SincMatrix.init(mlScript = "-97:0.31:97")
        val result = testVector.movsum(wlen = 42, endpoints = MovWinShape.discard).sum().asScalar()
        SincMathsTests.assert(
            abs(resultOctave - result) < testTol
        ) { "testMovSumEvenDiscard failed..." }
    }

    private fun testMovSumOddDiscard() {
        // Octave code
        //  format long
        //  testVector = -97:0.31:97;
        //  sum(movsum(testVector, 7))
        val resultOctave = -542.5000000000064
        val testTol = 1E-8
        val testVector = SincMatrix.init(mlScript = "-97:0.31:97")
        val result = testVector.movsum(wlen = 7, endpoints = MovWinShape.discard).sum().asScalar()
        SincMathsTests.assert(
            abs(resultOctave - result) < testTol
        ) { "testMovSumOddDiscard failed..." }
    }

    private fun testMovSumOddShrink() {
        // Octave code
        //  format long
        //  testVector = -97:0.31:97;
        //  sum(movsum(testVector, 7))
        val resultOctave = -546.2500000000020
        val testTol = 1E-8
        val testVector = SincMatrix.init(mlScript = "-97:0.31:97")
        val result = testVector.movsum(wlen = 7, endpoints = MovWinShape.shrink).sum().asScalar()
        SincMathsTests.assert(
            abs(resultOctave - result) < testTol
        ) { "testMovSumOddShrink failed..." }
    }

    private fun testMovSumEvenShrink() {
        // Octave code
        //  format long
        //  testVector = -97:0.31:97;
        //  sum(movsum(testVector, 42))
        val resultOctave = -5200.649999999969
        val testTol = 1E-7
        val testVector = SincMatrix.init(mlScript = "-97:0.31:97")
        val result = testVector.movsum(wlen = 42, endpoints = MovWinShape.shrink).sum().asScalar()
        SincMathsTests.assert(
            abs(resultOctave - result) < testTol
        ) { "testMovSumEvenShrink failed..." }
    }

    private fun testMovMeanEvenShrink() {
        // Octave code
        //  format long
        //  testVector = -97:0.31:97;
        //  sum(movmean(testVector, 42))
        val resultOctave = -172.0249999999999
        val testTol = 1E-9
        val testVector = SincMatrix.init(mlScript = "-97:0.31:97")
        val result = testVector.movmean(wlen = 42, endpoints = MovWinShape.shrink).sum().asScalar()
        SincMathsTests.assert(
            abs(resultOctave - result) < testTol
        ) { "testMovMeanEvenShrink failed..." }
    }

    /**
     * Citation: [Matlab central](https://au.mathworks.com/matlabcentral/fileexchange/30540-autocorrelation-function-acf)
     */
    private fun testAcf() {
        // Octave code
        //  format long
        //  x = 1:100;
        //  y = acf(x', 15);
        //  y'*y
        val resultOctave = 8.951995481744394
        val testTol = 1E-12
        val testVector = SincMatrix.init(mlScript = "1:100")
        val acfResult = testVector.acf(numLags = 15)
        val result = (acfResult * acfResult.transpose()).asScalar()
        SincMathsTests.assert(abs(resultOctave - result) < testTol) { "testAcf failed..." }
    }

    private fun testFindPeaks() {
        // Octave code
        //  format long
        //  x = 0:0.001:30;
        //  y = sin(x) + 1;
        //  [~, locs] = findpeaks(y);
        //  locs * locs'
        val resultOctave = 1394229519.0
        val testTol = 1E-12
        val x = SincMatrix.init(mlScript = "0:0.001:30")
        val y = x.sin() + 1.0
        val locs = y.findpeaks()
        val result = (locs * locs.transpose()).asScalar()
        SincMathsTests.assert(abs(resultOctave - result) < testTol) { "testFindPeaks failed..." }
    }

    private fun testDenoiseCwtft() {
        val filePath = "example_signal.csv"
        val noisySignal = SincMatrix.csvread(filePath = filePath)
        val diffSignal = noisySignal.diffWithWavelet(scale = 16.0, dt = 0.01)
        val resultMATLAB = 1.757498083459348e+04
        val testTol = 1E-11
        val result = (diffSignal.transpose() * diffSignal).asScalar()
        SincMathsTests.assert(abs(resultMATLAB - result) < testTol) { "testDenoiseCwtft failed..." }
    }

    fun performAll() {
        testVectorConv()
        testMatrixDiff()
        testMatrixFilter()
        testMatrixFiltfilt()
        testVectorSgolayFilter()
        testMatrixSgolayFilter()
        testMovSum()
        testMovMean()
        testMovSumOddShrink()
        testMovSumOddDiscard()
        testMovSumEvenDiscard()
        testMovSumEvenShrink()
        testMovMeanEvenShrink()
        testAcf()
        testFindPeaks()
        testDenoiseCwtft()
    }
}