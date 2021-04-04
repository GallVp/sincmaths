import kotlin.math.abs

class SincMatrixMathsAndStats {

    private val testCaseIIMatrices: Pair<SincMatrix, SincMatrix>
        get() {
            // Octave code
            //  format long
            //  rand("seed", 27)
            //  A = rand(1, 5);
            //  B = -7:0.3890210:401;
            val A =
                SincMatrix.from(script = "[7.301949858665466e-01, 8.112192153930664e-02, 5.081893205642700e-01, 1.602365076541901e-01, 4.644139707088470e-01]")
            val B = SincMatrix.from(script = "-7:0.3890210:401")

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
                SincMatrix.from(script = "[1.650966703891754e-01, 9.907181560993195e-02, 9.253824949264526e-01, 5.843927264213562e-01, 2.296017855405807e-01]")
            val B = SincMatrix.from(script = "-7:0.00001:-0.00001")

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
        val testTol = 1E-15
        // Start with two row vectors
        val A =
            SincMatrix.from(script = "[3.248022496700287e-01, 8.384426236152649e-01, 4.031754136085510e-01, 9.351466298103333e-01, 1.077670305967331e-01]")
        val B = SincMatrix.from(script = "[1, 2, 3, 4, 5]")
        val result = ((B * (A.t * B)) * A.t) elDiv B.sum()
        SincMathsTests.assert(abs(resultOctave - result.asScalar()) < testTol) { "testVectorMatrixMultiply failed..." }
    }

    private fun testVectorMatrixMultiplyII() {
        // Octave code
        //  testCaseIIMatrices
        //  A*(A'*B)*B'
        val resultOctave = 57378931.49934319
        val testTol = 1E-12

        val A = testCaseIIMatrices.first
        val B = testCaseIIMatrices.second
        val result = (A * (A.transpose() * B) * B.transpose()).asScalar()
        SincMathsTests.assert(abs(resultOctave - result) < testTol) { "testVectorMatrixMultiplyII failed..." }
    }

    private fun testMatrixSum() {
        // Octave code
        //  format long
        //  rand("seed", 12)
        //  A = rand(1, 5);
        //  B = 1:5;
        //  M = A'*B;
        //  sum(M)*sum(M, 2)*sum(A)
        val resultOctave = 765.0137609214315
        val testTol = 1E-12
        // Start with two row vectors
        val A =
            SincMatrix.from(script = "[3.248022496700287e-01, 8.384426236152649e-01, 4.031754136085510e-01, 9.351466298103333e-01, 1.077670305967331e-01]")
        val B = SincMatrix.from(script = "[1, 2, 3, 4, 5]")
        val M = A.transpose() * B
        val result = ((M.sum() * M.sum(dim = 2)) * A.sum()).asArray().firstOrNull()!!
        SincMathsTests.assert(abs(resultOctave - result) < testTol) { "testMatrixSum failed..." }
    }

    private fun testMatrixSumII() {
        // Octave code
        //  testCaseIIMatrices
        //  M = A'*B;
        //  A * sum(M, 2) * sum(M) * B'
        val resultOctave = 23034989669915.60
        val testTol = 1E-12
        val resultMagRange = 1E13

        val A = testCaseIIMatrices.first
        val B = testCaseIIMatrices.second
        val M = A.transpose() * B
        val result = (A * M.sum(2) * M.sum() * B.transpose()).asScalar()
        SincMathsTests.assert(abs(resultOctave - result) / resultMagRange < testTol) { "testMatrixSumII failed..." }
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
        val testTol = 1E-12
        // Start with two row vectors
        val A =
            SincMatrix.from(script = "[3.248022496700287e-01, 8.384426236152649e-01, 4.031754136085510e-01, 9.351466298103333e-01, 1.077670305967331e-01]")
        val B = SincMatrix.from(script = "[1, 2, 3, 4, 5]")
        val M = A.transpose() * B
        val result =
            ((M.mean() * M.mean(dim = 2)) * A.mean()).asArray().firstOrNull()!!
        SincMathsTests.assert(abs(resultOctave - result) < testTol) { "testMatrixMean failed..." }
    }

    private fun testMatrixMeanII() {
        // Octave code
        //  testCaseIIMatrices
        //  M = A'*B;
        //  mean(mean(M, 1)) / mean(mean(M, 2))
        val resultOctave = 1.000000000000001
        val testTol = 1E-12

        val A = testCaseIIMatrices.first
        val B = testCaseIIMatrices.second
        val M = A.transpose() * B
        val result = (M.mean(1).mean() elDiv M.mean(2).mean()).asScalar()
        SincMathsTests.assert(abs(resultOctave - result) < testTol) { "testMatrixMeanII failed..." }
    }

    private fun testMatrixMeanIII() {
        // Octave code
        //  testCaseIIIMatrices
        //  M = A'*B;
        //  mean(mean(M, 1)) / mean(mean(M, 2))
        val resultOctave = 1
        val testTol = 1E-12

        val A = testCaseIIIMatrices.first
        val B = testCaseIIIMatrices.second
        val M = A.transpose() * B
        val result = (M.mean(1).mean() elDiv M.mean(2).mean()).asScalar()
        SincMathsTests.assert(abs(resultOctave - result) < testTol) { "testMatrixMeanIII failed..." }
    }

    private fun testMatrixMedian() {
        val testTol = 1E-15
        val exampleA = doubleArrayOf(1.0, 3.0, 3.0, 6.0, 7.0, 8.0, 9.0).asSincMatrix()
        SincMathsTests.assert(
            abs(exampleA.median().asScalar() - 6.0) < testTol
        ) { "testMatrixMedian failed..." }
        val exampleB = doubleArrayOf(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 8.0, 9.0).asSincMatrix()
        SincMathsTests.assert(
            abs(exampleB.median().asScalar() - 4.5) < testTol
        ) { "testMatrixMedian failed..." }
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
        val computed = A.median().asScalar() - B.median().asScalar()
        SincMathsTests.assert(abs(computed - resultOctave) < testTol) { "testMatrixMedian failed..." }
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
        val testTol = 1E-12
        // Start with two row vectors
        val A =
            SincMatrix.from(script = "[3.248022496700287e-01, 8.384426236152649e-01, 4.031754136085510e-01, 9.351466298103333e-01, 1.077670305967331e-01]")
        val B = SincMatrix.from(script = "[1, 2, 3, 4, 5]")
        // Perform the test operations Vector x Vector -> sum(r:Matrix) -> r:Vector x Vector -> Scalar
        val M = A.transpose() * B
        val result = ((M.rms() * M.rms(dim = 2)) * A.rms()).asArray().firstOrNull()!!
        SincMathsTests.assert(abs(resultOctave - result) < testTol) { "testMatrixRMS failed..." }
    }

    private fun testMatrixMax() {
        // Octave code
        //  format long
        //  rand("seed", 12)
        //  A = rand(1, 5);
        //  B = 1:5;
        //  M = A'*B;
        //  max(M)*max(M, [], 2)*max(A)
        val resultOctave = 32.75277408827981
        val testTol = 1E-12
        // Start with two row vectors
        val A =
            SincMatrix.from(script = "[3.248022496700287e-01, 8.384426236152649e-01, 4.031754136085510e-01, 9.351466298103333e-01, 1.077670305967331e-01]")
        val B = SincMatrix.from(script = "[1, 2, 3, 4, 5]")
        // Perform the test operations Vector x Vector -> sum(r:Matrix) -> r:Vector x Vector -> Scalar
        val M = A.transpose() * B
        val result = ((M.max() * M.max(dim = 2)) * A.max()).asArray().firstOrNull()!!
        SincMathsTests.assert(abs(resultOctave - result) < testTol) { "testMatrixMax failed..." }
    }

    private fun testMatrixSign() {
        // Octave code
        //  format long
        //  testVector = [1 -1 0.1 0.2 0.3 0 0 -0.12 0 1.2];
        //  sign(testVector) * sign(testVector')
        val resultOctave = 7.0
        val testTol = 1E-12
        // Start with two row vectors
        val testVector = SincMatrix.from(script = "[1, -1, 0.1, 0.2, 0.3, 0, 0, -0.12, 0, 1.2]")
        val result = (testVector.sign() * testVector.transpose().sign()).asScalar()
        SincMathsTests.assert(abs(resultOctave - result) < testTol) { "testMatrixSign failed..." }
    }

    private fun testMatrixComparisons() {
        // Start with a row vector
        val testVector = SincMatrix.from(script = "[1, -1, 0.1, 0.2, 0.3, 0, 0, -0.12, 0, 1.2]")
        // Compared against octave
        SincMathsTests.assert(
            (((testVector.lessThan(0.3)) - doubleArrayOf(
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
            ).asSincMatrix()).equalsTo(0.0)).all()
        ) { "testMatrixComparisons failed..." }
        SincMathsTests.assert(
            (((testVector.greaterThan(5.0)) - doubleArrayOf(
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
            ).asSincMatrix()).equalsTo(0.0)).all()
        ) { "testMatrixComparisons failed..." }
        SincMathsTests.assert(
            (((testVector.greaterThan(0.2)) - doubleArrayOf(
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
            ).asSincMatrix()).equalsTo(0.0)).all()
        ) { "testMatrixComparisons failed..." }
        SincMathsTests.assert(
            (((testVector.equalsTo(0.2)) - doubleArrayOf(
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
            ).asSincMatrix()).equalsTo(0.0)).all()
        ) { "testMatrixComparisons failed..." }
        SincMathsTests.assert(
            (((testVector.equalsTo(0.0)) - doubleArrayOf(
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
            ).asSincMatrix()).equalsTo(0.0)).all()
        ) { "testMatrixComparisons failed..." }
        SincMathsTests.assert(
            (((testVector.equalsTo(-0.12)) - doubleArrayOf(
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
            ).asSincMatrix()).equalsTo(0.0)).all()
        ) { "testMatrixComparisons failed..." }

        SincMathsTests.assert(
            (((testVector.notEqualsTo(-0.12)) - listOf(
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
            ).asSincMatrix()).equalsTo(0.0)).all()
        ) { "testMatrixComparisons failed..." }
        SincMathsTests.assert(
            (((testVector.notEqualsTo(0.0)) - listOf(
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
            ).asSincMatrix()).equalsTo(0.0)).all()
        ) { "testMatrixComparisons failed..." }
        SincMathsTests.assert(
            (((testVector.notEqualsTo(5.0)) - listOf(
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
            ).asSincMatrix()).equalsTo(0.0)).all()
        ) { "testMatrixComparisons failed..." }

    }

    private fun testMatrixFind() {
        // Octave code
        //  format long
        //  testVector = [1 -1 0.1 0.2 0.3 0 0 -0.12 0 1.2];
        //  find(testVector) * find(testVector')
        val resultOctave = 219.0
        val testTol = 1E-12
        // Start with two row vectors
        val testVector = SincMatrix.from(script = "[1, -1, 0.1, 0.2, 0.3, 0, 0, -0.12, 0, 1.2]")
        val result = (testVector.find() * testVector.transpose().find()).asScalar()
        SincMathsTests.assert(abs(resultOctave - result) < testTol) { "testMatrixFind failed..." }
    }

    fun performAll() {
        testVectorMatrixMultiply()
        testVectorMatrixMultiplyII()
        testMatrixSum()
        testMatrixSumII()
        testMatrixMean()
        testMatrixMeanII()
        testMatrixMeanIII()
        testMatrixMedian()
        testMatrixRMS()
        testMatrixMax()
        testMatrixSign()
        testMatrixComparisons()
        testMatrixFind()
    }

}