package sincmaths.test

import sincmaths.SincMatrix
import sincmaths.asSincMatrix
import sincmaths.sincmatrix.*
import sincmaths.test.SincMathsTests.Companion.convTestTolAndroid
import sincmaths.test.SincMathsTests.Companion.multSumTestTol
import sincmaths.test.SincMathsTests.Companion.testTol


class SincMatrixMathsAndStats {

    private val testCaseIIMatrices: Pair<SincMatrix, SincMatrix>
        get() {
            // Octave code
            //  format long
            //  rand("seed", 27)
            //  A = rand(1, 5);
            //  B = -7:0.3890210:401;
            val A =
                matrixFrom(
                    "[7.301949858665466e-01, 8.112192153930664e-02, 5.081893205642700e-01," +
                            " 1.602365076541901e-01, 4.644139707088470e-01]"
                )
            val B = matrixFrom("-7:0.3890210:401")

            return Pair(A, B)
        }

    private val testCaseIIIMatrices: Pair<SincMatrix, SincMatrix>
        get() {
            // Octave code
            //  format long
            //  rand("seed", 101)
            //  A = rand(1, 5);
            //  B = -7:0.00001:-0.00001;
            val A =
                matrixFrom(
                    "[1.650966703891754e-01, 9.907181560993195e-02, 9.253824949264526e-01, " +
                            "5.843927264213562e-01, 2.296017855405807e-01]"
                )
            val B = matrixFrom("-7:0.00001:-0.00001")

            return Pair(A, B)
        }

    private fun testVectorMatrixMultiply() {
        // Octave code
        //  format long
        //  rand("seed", 12)
        //  A = rand(1, 5);
        //  B = 1:5;
        //  (B*(A'*B))*A' / sum(B)
        val resultOctave = 3.740641256320996
        // Start with two row vectors
        val A =
            matrixFrom(
                "[3.248022496700287e-01, 8.384426236152649e-01, 4.031754136085510e-01, " +
                        "9.351466298103333e-01, 1.077670305967331e-01]"
            )
        val B = matrixFrom("[1, 2, 3, 4, 5]")
        val result = ((B * (A.t * B)) * A.t) elDiv B.sum()
        SincMathsTests.assert((resultOctave - result).absoluteValue lt testTol)
    }

    private fun testVectorMatrixMultiplyII() {
        // Octave code
        //  testCaseIIMatrices
        //  A*(A'*B)*B' / sum(B.^2)
        val resultOctave = 1.039377943649220

        val A = testCaseIIMatrices.first
        val B = testCaseIIMatrices.second
        val result = (A * (A.t * B) * B.t) elDiv (B elPow 2.0).sum()
        SincMathsTests.assert((resultOctave - result).absoluteValue lt testTol)
    }

    private fun testMatrixSum() {
        // Octave code
        //  format long
        //  rand("seed", 12)
        //  A = rand(1, 5);
        //  B = 1:5;
        //  M = A'*B;
        //  sum(M)*sum(M, 2)*sum(A) / 100.0
        val resultOctave = 7.650137609214315
        // Start with two row vectors
        val A =
            matrixFrom(
                "[3.248022496700287e-01, 8.384426236152649e-01, 4.031754136085510e-01, " +
                        "9.351466298103333e-01, 1.077670305967331e-01]"
            )
        val B = matrixFrom("[1, 2, 3, 4, 5]")
        val M = A.transpose() * B
        val result = ((M.sum() * M.sum(2)) * A.sum()) / 100.0
        SincMathsTests.assert((resultOctave - result).absoluteValue lt testTol)
    }

    private fun testMatrixSumII() {
        // Octave code
        //  testCaseIIMatrices
        //  M = A'*B;
        //  (A * sum(M, 2) * sum(M) * B') / sum(B.^4)
        val resultOctave = 4.327263297502751
        val A = testCaseIIMatrices.first
        val B = testCaseIIMatrices.second
        val M = A.t * B
        val result = (A * M.sum(2) * M.sum() * B.t) elDiv (B elPow 4.0).sum()
        SincMathsTests.assert((resultOctave - result).absoluteValue lt multSumTestTol)
    }

    private fun testMatrixMean() {
        // Octave code
        //  format long
        //  rand("seed", 12)
        //  A = rand(1, 5);
        //  B = 1:5;
        //  M = A'*B;
        //  mean(M)*mean(M, 2)*mean(A)
        val resultOctave = 6.120110087371452
        val A =
            matrixFrom(
                "[3.248022496700287e-01, 8.384426236152649e-01, 4.031754136085510e-01, " +
                        "9.351466298103333e-01, 1.077670305967331e-01]"
            )
        val B = matrixFrom("[1, 2, 3, 4, 5]")
        val M = A.transpose() * B
        val result = ((M.mean() * M.mean(2)) * A.mean())
        SincMathsTests.assert((resultOctave - result).absoluteValue lt testTol)
    }

    private fun testMatrixMeanII() {
        // Octave code
        //  testCaseIIMatrices
        //  M = A'*B;
        //  mean(mean(M, 1)) / mean(mean(M, 2))
        val resultOctave = 1.000000000000001

        val A = testCaseIIMatrices.first
        val B = testCaseIIMatrices.second
        val M = A.transpose() * B
        val result = M.mean(1).mean() elDiv M.mean(2).mean()
        SincMathsTests.assert((resultOctave - result).absoluteValue lt testTol)
    }

    private fun testMatrixMeanIII() {
        // Octave code
        //  testCaseIIIMatrices
        //  M = A'*B;
        //  mean(mean(M, 1)) / mean(mean(M, 2))
        val resultOctave = 1.0
        val A = testCaseIIIMatrices.first
        val B = testCaseIIIMatrices.second
        val M = A.transpose() * B
        val result = M.mean(1).mean() elDiv M.mean(2).mean()
        SincMathsTests.assert((resultOctave - result).absoluteValue lt testTol)
    }

    private fun testVectorMedian() {
        val exampleA = doubleArrayOf(1.0, 3.0, 3.0, 6.0, 7.0, 8.0, 9.0).asSincMatrix()
        SincMathsTests.assert((exampleA.median() - 6.0).absoluteValue lt testTol)
        val exampleB = doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 8.0, 9.0).asSincMatrix()
        SincMathsTests.assert((exampleB.median() - 4.5).absoluteValue lt testTol)

        // Octave code
        //  format long
        //  rand("seed", 12)
        //  A = rand(1, 5);
        //  B = rand(1, 6);
        // median(A) - median(B)
        val resultOctave = -9.426563978195190e-02
        val A = listOf(
            3.248022496700287e-01,
            8.384426236152649e-01,
            4.031754136085510e-01,
            9.351466298103333e-01,
            1.077670305967331e-01
        ).asSincMatrix()
        val B = listOf(
            7.065400481224060e-02,
            3.377643823623657e-01,
            6.571177244186401e-01,
            1.300063729286194e-01,
            6.679516434669495e-01,
            8.918364644050598e-01
        ).asSincMatrix()
        val computed = A.median() - B.median()
        SincMathsTests.assert((computed - resultOctave).absoluteValue lt testTol)
    }

    private fun testMatrixMedian() {
        // Octave code
        //  format long
        //  A = reshape(1:120, 12, 10)'
        //  median(median(A, 2)) / 10 = 6.050000000000000
        //  sum(median(A, 1)) * sum(median(A, 2)) / 100000 = 4.392300000000000

        val A = matrixOf(10, 12, 1..120)

        SincMathsTests.assert((A.median(2).median() / 10.0 - 6.050000000000000).absoluteValue lt testTol)
        SincMathsTests.assert((A.median(1).sum() * A.median(2).sum() / 100000.0 - 4.392300000000000).absoluteValue lt testTol)

        SincMathsTests.assert(A.median(1).size() == listOf(1, 12))
        SincMathsTests.assert(A.median(2).size() == listOf(10, 1))
    }

    private fun testVectorIQR() {
        val exampleA = doubleArrayOf(1.0, 3.0, 6.0, 8.0, 9.0, 7.0, 3.0).asSincMatrix()
        SincMathsTests.assert((exampleA.iqr() - 4.75).absoluteValue lt testTol)
        val exampleB = doubleArrayOf(1.0, 3.0, 4.0, 5.0, 6.0, 9.0, 2.0, 8.0).asSincMatrix()
        SincMathsTests.assert((exampleB.iqr() - 4.5).absoluteValue lt testTol)
    }

    private fun testQuantile() {
        // MATLAB code
        //  format long
        //  resultA = quantile((1:1001), [0.1 0.2 0.3 0.55]) ./ 100
        //  resultB = quantile(reshape(1:1000, 2, 500)', [0.1 0.2]) ./ 100
        //  resultC = sum(quantile(reshape(1:1000, 2, 500)', [0.1 0.9], 2) ./ 100)

        val exampleA = (1..1001).asSincMatrix().quantile(doubleArrayOf(0.1, 0.2, 0.3, 0.55)) / 100.0
        val resultA = rowVectorOf(1.006, 2.007, 3.008, 5.5105)
        SincMathsTests.assert(exampleA.size() == resultA.size())
        SincMathsTests.assert(((exampleA - resultA) lt testTol).all())

        val exampleB = (1..1000).asSincMatrix().reshape(500, 2).quantile(doubleArrayOf(0.1, 0.2)) / 100.0
        val resultB = matrixOf(2, 2, 1.0, 1.01, 2.0, 2.01)

        SincMathsTests.assert(exampleB.size() == resultB.size())
        SincMathsTests.assert(((exampleB - resultB) lt testTol).all())

        val exampleC = ((1..1000).asSincMatrix().reshape(500, 2).quantile(doubleArrayOf(0.1, 0.9), 2) / 100.0).sum()
        val resultC = matrixOf(1, 2, 2500, 2505)

        SincMathsTests.assert(exampleC.size() == resultC.size())
        print(exampleC)
        SincMathsTests.assert(((exampleC - resultC) lt convTestTolAndroid).all())
    }

    private fun testMatrixRMS() {
        // Octave code
        //  format long
        //  rand("seed", 12)
        //  A = rand(1, 5);
        //  B = 1:5;
        //  M = A'*B;
        //  rms(M)*rms(M, 2)*rms(A)
        val resultOctave = 9.227648044294494
        val A =
            matrixFrom(
                "[3.248022496700287e-01, 8.384426236152649e-01, 4.031754136085510e-01, " +
                        "9.351466298103333e-01, 1.077670305967331e-01]"
            )
        val B = matrixFrom("[1, 2, 3, 4, 5]")
        val M = A.transpose() * B
        val result = (M.rms() * M.rms(2)) * A.rms()
        SincMathsTests.assert((resultOctave - result).absoluteValue lt multSumTestTol)
    }

    private fun testMatrixMax() {
        // Octave code
        //  format long
        //  rand("seed", 12)
        //  A = rand(1, 5);
        //  B = 1:5;
        //  M = A'*B;
        //  max(M)*max(M, [], 2)*max(A) / 10.0
        val resultOctave = 3.275277408827981
        val A =
            matrixFrom(
                "[3.248022496700287e-01, 8.384426236152649e-01, 4.031754136085510e-01, " +
                        "9.351466298103333e-01, 1.077670305967331e-01]"
            )
        val B = matrixFrom("[1, 2, 3, 4, 5]")
        val M = A.t * B
        val result = ((M.max() * M.max(2)) * A.max()) / 10.0
        SincMathsTests.assert((resultOctave - result).absoluteValue lt testTol)
    }

    private fun testMatrixSign() {
        // Octave code
        //  format long
        //  testVector = [1 -1 0.1 0.2 0.3 0 0 -0.12 0 1.2];
        //  sign(testVector) * sign(testVector')
        val resultOctave = 7.0
        val testVector = matrixFrom("[1, -1, 0.1, 0.2, 0.3, 0, 0, -0.12, 0, 1.2]")
        val result = testVector.sign() * testVector.transpose().sign()
        SincMathsTests.assert((resultOctave - result).absoluteValue lt testTol)
    }

    private fun testMatrixComparisons() {
        // Compared against octave
        val testVector = matrixFrom("[1, -1, 0.1, 0.2, 0.3, 0, 0, -0.12, 0, 1.2]")
        SincMathsTests.assert(
            (((testVector lt 0.3) - doubleArrayOf(
                0.0,
                1.0,
                1.0,
                1.0,
                0.0,
                1.0,
                1.0,
                1.0,
                1.0,
                0.0
            ).asSincMatrix()) net 1.0).all()
        )
        SincMathsTests.assert(
            (((testVector gt 5.0) - doubleArrayOf(
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0
            ).asSincMatrix()) et 0.0).all()
        )
        SincMathsTests.assert(
            (((testVector gt 0.2) - doubleArrayOf(
                1.0,
                0.0,
                0.0,
                0.0,
                1.0,
                0.0,
                0.0,
                0.0,
                0.0,
                1.0
            ).asSincMatrix()) net 1.0).all()
        )
        SincMathsTests.assert(
            (((testVector et 0.2) - doubleArrayOf(
                0.0,
                0.0,
                0.0,
                1.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0
            ).asSincMatrix()) net -1.0).all()
        )
        SincMathsTests.assert(
            (((testVector et 0.0) - doubleArrayOf(
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                1.0,
                1.0,
                0.0,
                1.0,
                0.0
            ).asSincMatrix()) et 0.0).all()
        )
        SincMathsTests.assert(
            (((testVector et -0.12) - doubleArrayOf(
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                1.0,
                0.0,
                0.0
            ).asSincMatrix()) net 0.1).all()
        )

        SincMathsTests.assert(
            (((testVector net -0.12) - listOf(
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                0,
                1,
                1
            ).asSincMatrix()) et 0.0).all()
        )
        SincMathsTests.assert(
            (((testVector net 0.0) - listOf(
                1,
                1,
                1,
                1,
                1,
                0,
                0,
                1,
                0,
                1
            ).asSincMatrix()) et 0.0).all()
        )
        SincMathsTests.assert(
            (((testVector net 5.0) - listOf(
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1,
                1
            ).asSincMatrix()) et 0.0).all()
        )

        SincMathsTests.assert(testVector.gt(0.0).any())
        SincMathsTests.assert(testVector.gt(5.0).not().all())
    }

    private fun testMatrixAndOr() {
        val vectorA = rowVectorOf(0, 1, 0, 0, 0, 1, 1, 1, 1)
        val vectorB = rowVectorOf(0, 1, 0, 1, 1, 1, -1, -1, 1)

        SincMathsTests.assert(
            vectorA.and(vectorB).et(
                rowVectorOf(0, 1, 0, 0, 0, 1, 1, 1, 1)
            ).all()
        )

        SincMathsTests.assert(
            vectorA.or(vectorB).et(
                rowVectorOf(0, 1, 0, 1, 1, 1, 1, 1, 1)
            ).all()
        )
    }

    private fun testEmptyVectorLogicOperators() {
        val emptyVector = SincMatrix.zeros(0, 1)
        SincMathsTests.assert((!emptyVector).all())
        SincMathsTests.assert(!((!emptyVector).any()))

        val testVector = matrixFrom("[1, -1, 0.1, 0.2, 0.3, 0, 0, -0.12, 0, 1.2]")
        SincMathsTests.assert(
            testVector.greaterThan(5.0).find().asIntArray().isEmpty()
        )

        SincMathsTests.assert(
            testVector.getWithLV(emptyVector).isempty()
        )

        val testVectorCopy = testVector.copyOf()
        testVectorCopy.setWithLV(emptyVector, 2.0)

        SincMathsTests.assert(
            (testVectorCopy et testVector).all()
        )
    }

    private fun testMatrixFind() {
        // Octave code
        //  format long
        //  testVector = [1 -1 0.1 0.2 0.3 0 0 -0.12 0 1.2];
        //  find(testVector) * find(testVector')
        val resultOctave = 219.0
        val testVector = matrixFrom("[1, -1, 0.1, 0.2, 0.3, 0, 0, -0.12, 0, 1.2]")
        val result = testVector.find() * testVector.transpose().find()
        SincMathsTests.assert((resultOctave - result).absoluteValue lt testTol)
    }

    fun performAll() {
        testVectorMatrixMultiply()
        testVectorMatrixMultiplyII()
        testMatrixSum()
        testMatrixSumII()
        testMatrixMean()
        testMatrixMeanII()
        testMatrixMeanIII()
        testVectorMedian()
        testMatrixMedian()
        testVectorIQR()
        testQuantile()
        testMatrixRMS()
        testMatrixMax()
        testMatrixSign()
        testMatrixComparisons()
        testMatrixAndOr()
        testEmptyVectorLogicOperators()
        testMatrixFind()
    }

}