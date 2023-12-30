package com.github.gallvp.sincmaths

import com.github.gallvp.sincmaths.SincMathsTest.Companion.convTestTol
import com.github.gallvp.sincmaths.SincMathsTest.Companion.convTestTolAndroid
import com.github.gallvp.sincmaths.SincMathsTest.Companion.multSumTestTol
import com.github.gallvp.sincmaths.SincMathsTest.Companion.testTol

class SincMatrixSignalTest {
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
        val A = matrixFrom(script = "-10:10")
        val B = matrixFrom(script = "1:0.3:6")
        val M = (
            A.conv(bVector = B, shape = ConvolutionShape.FULL) *
                A.transpose
                    .conv(bVector = B.transpose, shape = ConvolutionShape.FULL)
        )
        val N = (
            A.conv(bVector = B, shape = ConvolutionShape.SAME) *
                A.transpose.conv(
                    bVector = B.transpose,
                    shape = ConvolutionShape.SAME,
                )
        )
        val O = (
            A.conv(bVector = B, shape = ConvolutionShape.VALID) *
                A.transpose.conv(
                    bVector = B.transpose,
                    shape = ConvolutionShape.VALID,
                )
        )
        val P = (
            B.conv(bVector = A, shape = ConvolutionShape.FULL) *
                B.transpose.conv(
                    bVector = A.transpose,
                    shape = ConvolutionShape.FULL,
                )
        )
        val Q = (
            B.conv(bVector = A, shape = ConvolutionShape.SAME) *
                B.transpose.conv(
                    bVector = A.transpose,
                    shape = ConvolutionShape.SAME,
                )
        )
        val result = (M + N + O + P + Q) elDiv (B.sum() elPow 3.5)
        SincMathsTest.assert((resultOctave - result).absoluteValue lt testTol)
    }

    private fun testVectorDiff() {
        // Octave code
        //  format long
        //  rand("seed", 25)
        //  A = rand(1, 5);
        //  diff(A)*diff(A')
        val resultOctave = 1.080192559976076
        val A =
            matrixFrom(
                script = """[4.418510198593140e-02,
                4.279963076114655e-01,
                8.155888915061951e-01,
                7.815348356962204e-02,
                5.668686628341675e-01]""",
            )
        val result = A.diff() * A.t.diff()
        SincMathsTest.assert((resultOctave - result).absoluteValue lt testTol)
    }

    private fun testMatrixDiff() {
        // MATLAB code
        //  format long
        //  x = reshape(0.1:0.1:11, 10, 11)';
        //  y = sin(x);
        //  mean(iqr(diff(y, 1, 1), 1)) = 1.299495936567463
        //  mean(iqr(diff(100 * y, 1, 2), 2)) = 2.889079408634358
        val x = matrixFrom("0.1:0.1:11").reshape(11, 10)
        val y = x.sin()

        SincMathsTest.assert(
            (y.diff(1).iqr(1).mean() - 1.299495936567463).absoluteValue lt multSumTestTol,
        )

        SincMathsTest.assert(
            ((100.0 * y).diff(2).iqr(2).mean() - 2.889079408634358).absoluteValue lt convTestTol,
        )
    }

    private fun testVectorFilter() {
        // Octave code
        //  format long
        //  rand("seed", 101)
        //  testVector = rand(1, 5);
        //  B = [1 2 3];
        //  A = [4 5 6];
        //  filter(B, A, testVector)*filter(B, A, testVector')
        val resultOctave = 2.429411901632733e-01
        val testVector =
            matrixFrom(
                script = """[1.650966703891754e-01,
                9.907181560993195e-02,
                9.253824949264526e-01,
                5.843927264213562e-01,
                2.296017855405807e-01]""",
            )
        val B = doubleArrayOf(1.0, 2.0, 3.0)
        val A = doubleArrayOf(4.0, 5.0, 6.0)
        val result =
            testVector.filter(
                bVector = B, aVector = A,
            ) * testVector.transpose.filter(bVector = B, aVector = A)
        SincMathsTest.assert((resultOctave - result).absoluteValue lt testTol)
    }

    private fun testMatrixFilter() {
        // Octave code
        //  format long
        //  x = reshape(0.1:0.1:11, 10, 11)';
        //  y = cos(x);
        //  B = [1 2 3];
        //  A = [4 5 6];
        //  median(std(filter(B, A, 10*y, [], 1), 0, 1)) = 6.422826367520498
        //  median(std(filter(B, A, 10*y, [], 2), 0, 2)) = 3.542838989457845
        val x = matrixFrom("0.1:0.1:11").reshape(11, 10)
        val y = x.cos()
        val B = doubleArrayOf(1.0, 2.0, 3.0)
        val A = doubleArrayOf(4.0, 5.0, 6.0)

        SincMathsTest.assert(
            (
                (10.0 * y).filter(B, A, 1).std(1)
                    .median() - 6.422826367520498
            ).absoluteValue lt testTol,
        )

        SincMathsTest.assert(
            (
                (10.0 * y).filter(B, A, 2).std(2)
                    .median() - 3.542838989457845
            ).absoluteValue lt multSumTestTol,
        )
    }

    private fun testMatrixFiltfilt() {
        // MATLAB code
        //  format long
        //  B = [0.013359200027856, 0.026718400055713, 0.013359200027856];
        //  A = [1.000000000000000, -1.647459981076977, 0.700896781188403];
        //  testMat = sgolay(3, 41)
        //  sum(sum(filtfilt(B, A, testMat)*filtfilt(B, A, testMat'))) / 10.0
        val resultMATLAB = 4.0999999999996817
        val testMat = matrixFrom(script = SGCoeffs.SGO3X41)
        val B = doubleArrayOf(0.013359200027856, 0.026718400055713, 0.013359200027856)
        val A = doubleArrayOf(1.000000000000000, -1.647459981076977, 0.700896781188403)
        val R =
            testMat.filtFilt(bVector = B, aVector = A) * (
                testMat.transpose.filtFilt(
                    bVector = B, aVector = A,
                )
            )
        val result = R.sum().sum() / 10.0
        SincMathsTest.assert((resultMATLAB - result).absoluteValue lt convTestTol)
    }

    private fun testVectorSgolayFilter() {
        // MATLAB code
        //  format long
        //  A = csvread('test_csv.csv');
        //  testVector = A(:, 2);
        //  sgolayfilt(testVector', 3, 41) * sgolayfilt(testVector, 3, 41)
        val resultMATLAB = 6.740799259697040
        val filePath = "test_csv.csv"
        val A =
            SincMatrix.csvRead(
                filePath = filePath,
                separator = ",",
                headerInfo = listOf("t", "d", "d", "d", "d", "d", "d", "d", "d", "d"),
            )
        val testVector = A.getCol(2)
        val B = matrixFrom(SGCoeffs.SGO3X41)
        val result = testVector.t.sgolayFilter(B) * testVector.sgolayFilter(B)
        SincMathsTest.assert((resultMATLAB - result).absoluteValue lt convTestTol)
    }

    private fun testMatrixSgolayFilter() {
        // MATLAB code
        //  format long
        //  A = csvread('test_csv.csv');
        //  testMatrix = A(:, 2:4);
        //  sum(sum(sgolayfilt(testMatrix, 3, 7), 2)) / 1000.0
        val resultMATLAB = -2.426886809212823
        val filePath = "test_csv.csv"
        val A =
            SincMatrix.csvRead(
                filePath = filePath,
                separator = ",",
                headerInfo = listOf("t", "d", "d", "d", "d", "d", "d", "d", "d", "d"),
            )
        val testMatrix = A[":,2:4"]
        val B = matrixFrom(SGCoeffs.SGO3X7)
        val result = testMatrix.sgolayFilter(B).sum(2).sum() / 1000.0
        SincMathsTest.assert((resultMATLAB - result).absoluteValue lt convTestTol)
    }

    private fun testMovSum() {
        // Octave code
        //  format long
        //  rand("seed", 101)
        //  testVector = rand(1, 10);
        //  (movsum(testVector, 3) * movsum(testVector', 3)) * (movsum(testVector, 3, "Endpoints", "discard")...
        //  * movsum(testVector', 3, "Endpoints", "discard")) / (sum(testVector .* 35.03))
        val resultOctave = 1.493351200742660
        val testVector =
            matrixFrom(
                """[1.650966703891754e-01,
                9.907181560993195e-02,
                9.253824949264526e-01,
                5.843927264213562e-01,
                2.296017855405807e-01,
                7.710580229759216e-01,
                1.801824271678925e-01,
                3.308660686016083e-01,
                2.962400913238525e-01,
                5.188712477684021e-02]""",
            )
        val result =
            (
                (testVector.movSum(3) * testVector.transpose.movSum(3)) * (
                    testVector.movSum(
                        3, MovWinShape.DISCARD,
                    ) * testVector.transpose.movSum(3, MovWinShape.DISCARD)
                )
            ) / (testVector elMul 35.03).elSum()
        SincMathsTest.assert((resultOctave - result).absoluteValue lt testTol)
    }

    private fun testVectorMovMean() {
        // Octave code
        //  format long
        //  rand("seed", 101)
        //  testVector = rand(1, 10);
        //  (movmean(testVector, 3) * movmean(testVector', 3))...
        //  * (movmean(testVector, 3, "Endpoints", "discard") * movmean(testVector', 3, "Endpoints", "discard"))
        val resultOctave = 2.387150841604956
        val testVector =
            matrixFrom(
                """[1.650966703891754e-01,
                9.907181560993195e-02,
                9.253824949264526e-01,
                5.843927264213562e-01,
                2.296017855405807e-01,
                7.710580229759216e-01,
                1.801824271678925e-01,
                3.308660686016083e-01,
                2.962400913238525e-01,
                5.188712477684021e-02]""",
            )
        val result =
            (testVector.movMean(3) * testVector.transpose.movMean(3)) * (
                testVector.movMean(
                    3, MovWinShape.DISCARD,
                ) * testVector.transpose.movMean(3, MovWinShape.DISCARD)
            )
        SincMathsTest.assert((resultOctave - result).absoluteValue lt testTol)
    }

    private fun testMatrixMovMean() {
        // Octave code
        //  format long
        //  x = reshape(0.1:0.1:11, 10, 11)';
        //  y = cos(x);
        //  median(std(movmean(10*y, 7, 1), 0, 1)) = 1.459065564134439
        //  median(std(movmean(10*y, 7, 2), 0, 2)) = 1.442356744207888
        val x = matrixFrom("0.1:0.1:11").reshape(11, 10)
        val y = x.cos()

        SincMathsTest.assert(
            (
                (10.0 * y).movMean(7, MovWinShape.SHRINK, 1).std(1)
                    .median() - 1.459065564134439
            ).absoluteValue lt testTol,
        )

        SincMathsTest.assert(
            (
                (10.0 * y).movMean(7, MovWinShape.SHRINK, 2).std(2)
                    .median() - 1.442356744207888
            ).absoluteValue lt testTol,
        )
    }

    private fun testMovSumEvenDiscard() {
        // MATLAB code
        //  format long
        //  testVector = -97:0.31:97;
        //  sum(movsum(testVector, 42, "Endpoints", "discard")) / sum(testVector.*10)
        val resultMatlab = 3.924920127795551
        val testVector = matrixFrom("-97:0.31:97")
        val result =
            testVector.movSum(42, MovWinShape.DISCARD).sum() elDiv (testVector elMul 10.0).sum()
        SincMathsTest.assert((resultMatlab - result).absoluteValue lt convTestTolAndroid)
    }

    private fun testMovSumOddDiscard() {
        // Octave code
        //  format long
        //  testVector = -97:0.31:97;
        //  sum(movsum(testVector, 7)) / 100.0
        val resultOctave = -5.425000000000064
        val testVector = matrixFrom(script = "-97:0.31:97")
        val result = testVector.movSum(wLen = 7, endpoints = MovWinShape.DISCARD).sum() / 100.0
        SincMathsTest.assert((resultOctave - result).absoluteValue lt convTestTol)
    }

    private fun testMovSumOddShrink() {
        // Octave code
        //  format long
        //  testVector = -97:0.31:97;
        //  sum(movsum(testVector, 7)) / 100.0
        val resultOctave = -5.462500000000020
        val testVector = matrixFrom("-97:0.31:97")
        val result = testVector.movSum(7, MovWinShape.SHRINK).sum() / 100.0
        SincMathsTest.assert((resultOctave - result).absoluteValue lt convTestTol)
    }

    private fun testMovSumEvenShrink() {
        // Octave code
        //  format long
        //  testVector = -97:0.31:97;
        //  sum(movsum(testVector, 42)) / 1000.0
        val resultOctave = -5.200649999999969
        val testVector = matrixFrom("-97:0.31:97")
        val result = testVector.movSum(42, MovWinShape.SHRINK).sum() / 1000.0
        SincMathsTest.assert((resultOctave - result).absoluteValue lt convTestTol)
    }

    private fun testMovMeanEvenShrink() {
        // Octave code
        //  format long
        //  testVector = -97:0.31:97;
        //  sum(movmean(testVector, 42)) / 100.0
        val resultOctave = -1.720249999999999
        val testVector = matrixFrom("-97:0.31:97")
        val result = testVector.movMean(42, MovWinShape.SHRINK).sum() / 100.0
        SincMathsTest.assert((resultOctave - result).absoluteValue lt convTestTol)
    }

    /**
     * Citation: [Matlab central](https://au.mathworks.com/matlabcentral/fileexchange/30540-autocorrelation-function-acf)
     */
    private fun testVectorAcf() {
        // Octave code
        //  format long
        //  x = 1:100;
        //  y = acf(x', 15);
        //  y'*y
        val resultOctave = 8.951995481744394
        val testVector = matrixFrom("1:100")
        val acfResult = testVector.acf(15)
        val result = acfResult * acfResult.t
        SincMathsTest.assert((resultOctave - result).absoluteValue lt testTol)
    }

    private fun testFindPeaks() {
        // Octave code
        //  format long
        //  x = 0:0.001:30;
        //  y = sin(x) + 1;
        //  [~, locs] = findpeaks(y);
        //  locs * locs'
        val resultOctave = 1394229519.0
        val x = matrixFrom("0:0.001:30")
        val y = x.sin() + 1.0
        val locs = y.findPeaks()
        val result = locs * locs.t
        SincMathsTest.assert((resultOctave - result).absoluteValue lt testTol)
    }

    private fun testDenoiseCwtft() {
        val filePath = "example_signal.csv"
        val noisySignal = SincMatrix.csvRead(filePath)
        val diffSignal = noisySignal.diffWithWavelet(16.0, 0.01)
        val resultMATLAB = 1.757498083459348
        val result = (diffSignal.t * diffSignal) / 10000.0
        SincMathsTest.assert((resultMATLAB - result).absoluteValue lt testTol)
    }

    fun performAll() {
        testVectorConv()
        testVectorDiff()
        testMatrixDiff()
        testVectorFilter()
        testMatrixFilter()
        testMatrixFiltfilt()
        testVectorSgolayFilter()
        testMatrixSgolayFilter()
        testMovSum()
        testVectorMovMean()
        testMatrixMovMean()
        testMovSumOddShrink()
        testMovSumOddDiscard()
        testMovSumEvenDiscard()
        testMovSumEvenShrink()
        testMovMeanEvenShrink()
        testVectorAcf()
        testFindPeaks()
        testDenoiseCwtft()
    }
}
