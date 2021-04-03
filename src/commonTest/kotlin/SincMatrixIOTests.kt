import kotlin.math.abs
import kotlin.test.assertFailsWith

class SincMatrixIOTests {

    private fun testMatrixInput() {
        val A = SincMatrix.init(mlScript = "[1, 2, 3; 4, 5, 6]")
        val B = SincMatrix.init(mlScript = "[1, 2, 3, 4]")
        val C = SincMatrix.init(mlScript = "[5;6;7;8;9;10]")
        val D = SincMatrix.init(mlScript = "1:10")
        val E = SincMatrix.init(mlScript = "-1.5:-1:-7.9")
        val resultOctave = 15.02765035409749
        val testTol = 1E-12
        val result = A.sum().min() + B.max() + C.median() + D.std() + E.mean()
        SincMathsTests.assert(abs(resultOctave - result.asScalar()) < testTol) { "testMatrixInput failed..." }
    }

    private fun testMatrixCSVRead() {

        val filePath = "test_csv.csv"
        val A = SincMatrix.csvread(
            filePath = filePath,
            separator = ",",
            headerInfo = listOf("t", "d", "d", "d", "d", "d", "d", "d", "d", "d")
        )
        val resultMATLAB = 1.040499533820819e+04
        val testTol = 1E-10
        val sgMatrix = SincMatrix.init(mlScript = SGCoeffs.sgo3x41)
        val result = A.getCols(mlCols = 2..10)
            .sgolayfilter(B = sgMatrix)
            .sum()
            .diff()
            .abs()
            .asRowVector()
            .cumsum()
            .asRowVector()
            .flip()
            .rms()
            .asScalar()
        SincMathsTests.assert(abs(resultMATLAB - result) < testTol) { "testMatrixCSVRead failed..." }
    }

    private fun testMatrixIndexing() {
        // Supported Octave scripts: 1:5,4:7 and 1:5,4 and 1:5,: and 1,4:7 and :,4:7 and : and
        // 1:end,end:end-1 and 1:5 and 1:end-1 and end:end-1
        //
        // Octave code
        //  format long
        //  M = reshape(1:110, 10, 11)';
        //  A = sum(sum(M(1:5,4:7))) % 510
        //  B = sum(M(1:5,4)) % 120
        //  C = sum(sum(M(1:5,:))) % 1275
        //  D = sum(sum(M(1,4:7))) % 22
        //  E = sum(sum(M(:,4:7))) % 2442
        //  F = sum(M(:)) % 6105
        //  G = sum(sum(M(1:end,end:-1:end-1))) % 1309
        //  H = sum(M(1, 1:5)) % 15; Octave is column-major
        //  I = sum(M(1:end-1)) % 5995
        //  J = sum(M(end, end:-1:end-1)) % 219; Octave is column-major
        //  K = sum(sum(M(1:end,end:end-1))) % 0
        //  L = sum(M(end:end-1)) % 0

        val resultOctave = 18012.0

        val M: SincMatrix = (1..110).asSincMatrix(11, 10) // SincMatrix is row-major

        val testTol = 1E-12
        val A = M.get(mlScript = "1:5,4:7").sum().sum().asScalar()
        val B = M.get(mlScript = "1:5,4").sum().asScalar()
        val C = M.get(mlScript = "1:5,:").sum().sum().asScalar()
        val D = M.get(mlScript = "1,4:7").sum().asScalar()
        val E = M.get(mlScript = ":,4:7").sum().sum().asScalar()
        val F = M.get(mlScript = ":").sum().asScalar()
        val G = M.get(mlScript = "1:end,end:-1:end-1").sum().sum().asScalar()
        val H = M.get(mlScript = "1:5").sum().asScalar()
        val I = M.get(mlScript = "1:end-1").sum().asScalar()
        val J = M.get(mlScript = "end:-1:end-1").sum().asScalar()
        val K = M.get(mlScript = "1:end,end:end-1").sum().sum().asScalar()
        val L = M.get(mlScript = "end:end-1").sum().asScalar()
        val result = A + B + C + D + E + F + G + H + I + J + K + L
        SincMathsTests.assert(abs(resultOctave - result) < testTol) { "testMatrixIndexing failed..." }
    }

    private fun testMatrixIndexingEdges() {
        val M: SincMatrix = (1..110).asSincMatrix(m = 11, n = 10)

        assertFailsWith<IndexOutOfBoundsException> {
            M[0]
        }

        assertFailsWith<IndexOutOfBoundsException> {
            M[intArrayOf(0, 2, -1)]
        }

        assertFailsWith<IndexOutOfBoundsException> {
            M[intArrayOf(0)]
        }

        assertFailsWith<IndexOutOfBoundsException> {
            M["-1:end"]
        }

        assertFailsWith<IndexOutOfBoundsException> {
            M[intArrayOf(0), intArrayOf(0)]
        }

        assertFailsWith<IllegalArgumentException> {
            M["2:end-k*j2"]
        }
    }

    private fun testMatrixMutations() {
        val testTol = 1E-12
        // SincMatrix is row-major; Octave/MATLAB are column-major
        val A: SincMatrix = (1..110).asSincMatrix(m = 11, n = 10)
        val selectorA = listOf(10, 50, 89, 32).asSincMatrix()
        val valueA = -677.0
        val selectorB = listOf(3, 30, 60, 90, 110).asSincMatrix()
        val valuesB = listOf(-3.0, -30.0, -60.9, -90.0, -110.0001).asSincMatrix()
        val selectorC = listOf(3, 30, 60, 90, 110).asSincMatrix()
        val valuesC = listOf<Double>().asSincMatrix()
        val selectorD = (A.lessThan(55.0))
        val valuesD = listOf<Double>().asSincMatrix()
        val selectorE = (A.greaterThan(23.0))
        val valuesE = 22.101
        // Octave code
        //  A = reshape(1:110, 10, 11);
        //  selectorA = [10, 50, 89, 32]
        //  valueA = -677.0
        //  A(selectorA) = valueA
        //  sum(mean(A'))
        SincMathsTests.assert(
            A.set2(indices = selectorA, value = valueA).mean().sum().asScalar() == 292.3636363636364
        ) { "testMatrixMutations failed..." }
        // Octave code
        //  A = reshape(1:110, 10, 11);
        //  selectorB = [3, 30, 60, 90, 110]
        //  valuesB = [-3, -30, -60.9, -90, -110.0001]
        //  A(selectorB) = valuesB
        //  mean(max(A'))
        SincMathsTests.assert(
            A.set2(indices = selectorB, values = valuesB).max().mean().asScalar() == 104.5
        ) { "testMatrixMutations failed..." }
        // Octave code
        //  A = reshape(1:110, 10, 11);
        //  selectorC = [3, 30, 60, 90, 110]
        //  A(selectorC) = []
        //  std(A)
        SincMathsTests.assert(
            A.set2(indices = selectorC, values = valuesC).std().asScalar() == 31.51891402621514
        ) { "testMatrixMutations failed..." }
        // Octave code
        //  A = reshape(1:110, 10, 11);
        //  selectorD = A < 55
        //  A(selectorD) = []
        //  std(A)
        SincMathsTests.assert(
            A.setWithLV(logicalVect = selectorD, values = valuesD).std().asScalar() == 16.30950643030009
        ) { "testMatrixMutations failed..." }
        // Octave code
        //  A = reshape(1:110, 10, 11);
        //  selectorE = A > 23
        //  A(selectorE) = 22.101
        //  sum(mean(A'))
        SincMathsTests.assert(
            abs(
                A.setWithLV(logicalVect = selectorE, value = valuesE).mean().sum().asScalar() - 199.8897272727273
            ) < testTol
        ) { "testMatrixMutations failed..." }
    }


    fun performAll() {
        testMatrixInput()
        testMatrixCSVRead()
        testMatrixIndexing()
        testMatrixIndexingEdges()
        testMatrixMutations()
    }

}