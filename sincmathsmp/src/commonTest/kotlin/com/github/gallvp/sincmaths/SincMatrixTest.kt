package com.github.gallvp.sincmaths

import kotlin.math.absoluteValue
import kotlin.test.assertFailsWith

class SincMatrixTest {

    private fun testMatrixInput() {
        val A = matrixFrom("[1, 2, 3; 4, 5, 6]")
        val B = matrixFrom("[1, 2, 3, 4]")
        val C = matrixFrom("[5;6;7;8;9;10]")
        val D = matrixFrom("1:10")
        val E = matrixFrom("-1.5:-1:-7.9")
        val resultOctave = 1.502765035409749
        val result = (A.sum().min() + B.max() + C.median() + D.std() + E.mean()) / 10.0
        SincMathsTest.assert((resultOctave - result).absoluteValue lt SincMathsTest.testTol)
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

        // SincMatrix is row-major
        val M: SincMatrix = (1..110).asSincMatrix(11, 10)

        val A = M["1:5,4:7"].sum().sum()
        val B = M["1:5,4"].sum()
        val C = M["1:5,:"].sum().sum()
        val D = M["1,4:7"].sum()
        val E = M[":,4:7"].sum().sum()
        val F = M[":"].sum()
        val G = M["1:end,end:-1:end-1"].sum().sum()
        val H = M["1:5"].sum()
        val I = M["1:end-1"].sum()
        val J = M["end:-1:end-1"].sum()
        // val K = M["1:end,end:end-1"].sum().sum().scalar; Skipped due to native times operation
        val L = M["end:end-1"].sum()
        val result = A + B + C + D + E + F + G + H + I + J + L
        SincMathsTest.assert((resultOctave - result).absoluteValue lt SincMathsTest.testTol)
    }

    private fun testMatrixIndexingViaKotlin() {

        val M: SincMatrix = (1..110).asSincMatrix(11, 10)

        SincMathsTest.assert(
            (M["1:5,4:7"] et M.get { _, _, _, _ ->
                Pair(1..5, 4..7)
            }).all()
        )
        SincMathsTest.assert(
            (M["1:5,4"] et M.get { _, _, _, _ ->
                Pair(1..5, 4..4)
            }).all()
        )
        SincMathsTest.assert(
            (M["1:5,:"] et M.get { _, _, _, allC ->
                Pair(1..5, allC)
            }).all()
        )
        SincMathsTest.assert(
            (M["1,4:7"] et M.get { _, _, _, _ ->
                Pair(1..1, 4..7)
            }).all()
        )
        SincMathsTest.assert(
            (M[":,4:7"] et M.get { _, _, allR, _ ->
                Pair(allR, 4..7)
            }).all()
        )
        SincMathsTest.assert(
            (M[":"] et M.get { _, all ->
                all
            }).all()
        )
        SincMathsTest.assert(
            (M["1:5"] et M.get { _, _ ->
                1..5
            }).all()
        )
        SincMathsTest.assert(
            (M["1:end-1"] et M.get { end, _ ->
                1 until end
            }).all()
        )
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
        var X = A.copyOf()
        X.setWithIndices(selectorA, valueA)
        SincMathsTest.assert(
            (X.mean().sum() / 100.0 - 2.923636363636364).absoluteValue lt SincMathsTest.testTol
        )
        // Octave code
        //  A = reshape(1:110, 10, 11);
        //  selectorB = [3, 30, 60, 90, 110]
        //  valuesB = [-3, -30, -60.9, -90, -110.0001]
        //  A(selectorB) = valuesB
        //  mean(max(A'))
        X = A.copyOf()
        X.setWithIndices(selectorB, valuesB)
        SincMathsTest.assert(
            (X.max().mean() - 104.5).absoluteValue lt SincMathsTest.testTol
        )
        // Octave code
        //  A = reshape(1:110, 10, 11);
        //  selectorC = [3, 30, 60, 90, 110]
        //  A(selectorC) = []
        //  std(A) / 10.0
        X = A.copyOf()
        X.setWithIndices(selectorC, valuesC)
        SincMathsTest.assert(
            (X.std() / 10.0 - 3.151891402621514).absoluteValue lt SincMathsTest.testTol
        )
        // Octave code
        //  A = reshape(1:110, 10, 11);
        //  selectorD = A < 55
        //  A(selectorD) = []
        //  std(A) / 10.0
        X = A.copyOf()
        X.setWithLV(selectorD, valuesD)
        SincMathsTest.assert(
            (X.std() / 10.0 - 1.630950643030009).abs() lt SincMathsTest.testTol
        )
        // Octave code
        //  A = reshape(1:110, 10, 11);
        //  selectorE = A > 23
        //  A(selectorE) = 22.101
        //  sum(mean(A')) / 100.0
        X = A.copyOf()
        X.setWithLV(selectorE, valuesE)
        SincMathsTest.assert(
            (X.mean().sum() / 100.0 - 1.998897272727273).absoluteValue lt SincMathsTest.testTol
        )
    }

    private fun testMatrixRowColMutations() {
        val A = matrixOf(11, 10, 1..110)
        for (row in A.rowIndices) {
            A.setRow(row, 1.0)
        }
        SincMathsTest.assert(
            (A.elSum() - A.numel.toDouble()).absoluteValue < SincMathsTest.testTol
        )

        for (col in A.colIndices) {
            A.setCol(col, 0.0)
        }
        SincMathsTest.assert(
            (A.elSum() - 0.0).absoluteValue < SincMathsTest.testTol
        )
    }

    private fun circShiftTest() {
        val A = matrixFrom("1:10")

        SincMathsTest.assert(A.sum() et A.circShift(3).sum())
        SincMathsTest.assert(A.sum() et A.circShift(-3).sum())
        SincMathsTest.assert(A.sum() et A.circShift(-10).sum())
        SincMathsTest.assert(A.circShift(-11).sum() et A.circShift(-1).sum())
        SincMathsTest.assert(A.circShift(73).sum() et A.circShift(-73).sum())

        SincMathsTest.assert(A.circShift(2).net(A.circShift(-2)).all())
        SincMathsTest.assert(A.circShift(10).et(A.circShift(-10)).all())
        SincMathsTest.assert(
            A.circShift(2).et(matrixFrom("[9,10,1,2,3,4,5,6,7,8]")).all()
        )
        SincMathsTest.assert(
            A.circShift(-12).et(matrixFrom("[3,4,5,6,7,8,9,10,1,2]")).all()
        )
    }

    private fun catTest() {
        // Octave code: Octave is column major/SincMatrix is row major
        // format long
        // A = reshape(1:20, 10, 2)'
        // B = reshape(25:48, 12, 2)'
        // C = reshape(0:83, 12, 7)'
        // sum(sum(cat(2, A, B))) / 1000 = 1.086000000000000
        // sum(sum(cat(1, B, C))) / 1000 = 4.362000000000000
        //
        // size(cat(2, A, B)) = [2, 22]
        // size(cat(1, B, C)) = [9, 12]
        //
        // cat(1, B, C)(9, 9) = 80
        val A = matrixOf(2, 10, 1..20)
        val B = matrixOf(2, 12, 25..48)
        val C = matrixOf(7, 12, 0 until 84)

        SincMathsTest.assert(
            (A.cat(2, B).sum()
                .sum() / 1000.0 - 1.086000000000000).absoluteValue lt SincMathsTest.testTol
        )
        SincMathsTest.assert(
            (B.cat(1, C).sum()
                .sum() / 1000.0 - 4.362000000000000).absoluteValue lt SincMathsTest.testTol
        )

        SincMathsTest.assert(A.cat(2, B).size == listOf(2, 22))
        SincMathsTest.assert(B.cat(1, C).size == listOf(9, 12))

        SincMathsTest.assert(
            (B.cat(
                1,
                C
            )[9, 9] - 80.0).absoluteValue < SincMathsTest.testTol
        )
    }

    private fun testMatrixRep() {
        // Octave code
        // format long
        // size(repmat([1;0], 20, 1)) = [40, 1]
        // sum(repmat([1;0], 20, 1)) = 20
        // repmat([1;0], 0, 0) = []
        // repmat([1 0], 1, 3) = [1 0 1 0 1 0]
        // size(repmat([1 0 0;0 1 0], 10, 7)) = [20 21]
        // sum(repmat([1 0 0;0 1 0], 10, 7))(1:3) = [10 10 0]
        // sum(repmat([1 0 0;0 1 0], 10, 7), 2)(1:3) = [7;7;7]

        SincMathsTest.assert(
            colVectorOf(1, 0).repMat(20, 1).size == listOf(40, 1)
        )

        SincMathsTest.assert(
            (colVectorOf(1, 0).repMat(20, 1).sum() - 20.0).absoluteValue lt SincMathsTest.testTol
        )

        SincMathsTest.assert(
            colVectorOf(1, 0).repMat(0, 0).isEmpty()
        )

        SincMathsTest.assert(
            (rowVectorOf(1, 0).repMat(1, 3) et rowVectorOf(1, 0, 1, 0, 1, 0)).all()
        )

        SincMathsTest.assert(
            matrixFrom("[1, 0, 0;0, 1, 0]").repMat(10, 7).size == listOf(20, 21)
        )

        SincMathsTest.assert(
            (matrixFrom("[1, 0, 0;0, 1, 0]").repMat(10, 7).sum()[1..3] et colVectorOf(
                10,
                10,
                0
            )).all()
        )

        SincMathsTest.assert(
            (matrixFrom("[1, 0, 0;0, 1, 0]").repMat(10, 7).sum(2)[1..3] et rowVectorOf(7).repMat(
                3,
                1
            )).all()
        )
    }

    fun performAll() {
        testMatrixInput()
        testMatrixIndexing()
        testMatrixIndexingViaKotlin()
        testMatrixIndexingEdges()
        testMatrixMutations()
        testMatrixRowColMutations()
        circShiftTest()
        catTest()
        testMatrixRep()
    }
}