package sincmaths.test

import sincmaths.SincMatrix
import sincmaths.asSincMatrix
import sincmaths.sincmatrix.*
import kotlin.math.abs
import kotlin.test.assertFailsWith

class SincMatrixBasicTests {

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

        // SincMatrix is row-major
        val M: SincMatrix = (1..110).asSincMatrix(11, 10)

        val A = M["1:5,4:7"].sum().sum().scalar
        val B = M["1:5,4"].sum().scalar
        val C = M["1:5,:"].sum().sum().scalar
        val D = M["1,4:7"].sum().scalar
        val E = M[":,4:7"].sum().sum().scalar
        val F = M[":"].sum().scalar
        val G = M["1:end,end:-1:end-1"].sum().sum().scalar
        val H = M["1:5"].sum().scalar
        val I = M["1:end-1"].sum().scalar
        val J = M["end:-1:end-1"].sum().scalar
        // val K = M["1:end,end:end-1"].sum().sum().scalar; Skipped due to native times operation
        val L = M["end:end-1"].sum().scalar
        val result = A + B + C + D + E + F + G + H + I + J + L
        SincMathsTests.assert(abs(resultOctave - result) < SincMathsTests.testTol)
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
        // SincMatrix is row-major; Octave/MATLAB are column-major
        val A: SincMatrix = matrixOf(11, 10, 1..110)
        val selectorA = rowVectorOf(10, 50, 89, 32)
        val valueA = -677.0
        val selectorB = rowVectorOf(3, 30, 60, 90, 110)
        val valuesB = rowVectorOf(-3.0, -30.0, -60.9, -90.0, -110.0001)
        val selectorC = matrixOf(1, 5, 3, 30, 60, 90, 110)
        val valuesC = doubleArrayOf().asSincMatrix()
        val selectorD = A lt 55.0
        val valuesD = doubleArrayOf().asSincMatrix()
        val selectorE = A gt 23.0
        val valuesE = 22.101
        // Octave code
        //  A = reshape(1:110, 10, 11);
        //  selectorA = [10, 50, 89, 32]
        //  valueA = -677.0
        //  A(selectorA) = valueA
        //  sum(mean(A')) / 100.0
        SincMathsTests.assert(
            abs(
                A.set2(selectorA, valueA).mean().sum().scalar / 100.0 - 2.923636363636364
            ) < SincMathsTests.testTol
        )
        // Octave code
        //  A = reshape(1:110, 10, 11);
        //  selectorB = [3, 30, 60, 90, 110]
        //  valuesB = [-3, -30, -60.9, -90, -110.0001]
        //  A(selectorB) = valuesB
        //  mean(max(A'))
        SincMathsTests.assert(
            abs(
                A.set2(indices = selectorB, values = valuesB).max().mean().scalar - 104.5
            ) < SincMathsTests.testTol
        )
        // Octave code
        //  A = reshape(1:110, 10, 11);
        //  selectorC = [3, 30, 60, 90, 110]
        //  A(selectorC) = []
        //  std(A) / 10.0
        SincMathsTests.assert(
            abs(
                A.set2(indices = selectorC, values = valuesC).std().scalar / 10.0 - 3.151891402621514
            ) < SincMathsTests.testTol
        )
        // Octave code
        //  A = reshape(1:110, 10, 11);
        //  selectorD = A < 55
        //  A(selectorD) = []
        //  std(A) / 10.0
        SincMathsTests.assert(
            abs(
                A.setWithLV(logicalVect = selectorD, values = valuesD).std().scalar / 10.0 - 1.630950643030009
            ) < SincMathsTests.testTol
        )
        // Octave code
        //  A = reshape(1:110, 10, 11);
        //  selectorE = A > 23
        //  A(selectorE) = 22.101
        //  sum(mean(A')) / 100.0
        SincMathsTests.assert(
            abs(
                A.setWithLV(logicalVect = selectorE, value = valuesE).mean().sum().scalar / 100.0 - 1.998897272727273
            ) < SincMathsTests.testTol
        )
    }

    private fun testMatrixInput() {
        val A = matrixFrom("[1, 2, 3; 4, 5, 6]")
        val B = matrixFrom("[1, 2, 3, 4]")
        val C = matrixFrom("[5;6;7;8;9;10]")
        val D = matrixFrom("1:10")
        val E = matrixFrom("-1.5:-1:-7.9")
        val resultOctave = 1.502765035409749
        val result = (A.sum().min() + B.max() + C.median() + D.std() + E.mean()).scalar / 10.0
        SincMathsTests.assert(abs(resultOctave - result) < SincMathsTests.testTol)
    }

    private fun circShiftTest() {
        val A = matrixFrom("1:10")

        SincMathsTests.assert(A.sum().scalar == A.circshift(3).sum().scalar)
        SincMathsTests.assert(A.sum().scalar == A.circshift(-3).sum().scalar)
        SincMathsTests.assert(A.sum().scalar == A.circshift(-10).sum().scalar)
        SincMathsTests.assert(A.circshift(-11).sum().scalar == A.circshift(-1).sum().scalar)
        SincMathsTests.assert(A.circshift(73).sum().scalar == A.circshift(-73).sum().scalar)

        SincMathsTests.assert(A.circshift(2).net(A.circshift(-2)).all())
        SincMathsTests.assert(A.circshift(10).et(A.circshift(-10)).all())
        SincMathsTests.assert(A.circshift(2).et(matrixFrom("[9,10,1,2,3,4,5,6,7,8]")).all())
        SincMathsTests.assert(A.circshift(-12).et(matrixFrom("[3,4,5,6,7,8,9,10,1,2]")).all())
    }

    fun performAll() {
        testMatrixInput()
        testMatrixIndexing()
        testMatrixIndexingEdges()
        testMatrixMutations()
    }
}