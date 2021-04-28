package sincmaths.test

import sincmaths.SincMatrix
import sincmaths.coefficients.SGCoeffs
import sincmaths.sincmatrix.*
import sincmaths.test.SincMathsTests.Companion.convTestTol

class SincMatrixIOTests {

    private fun testMatrixCSVRead() {

        val filePath = "test_csv.csv"
        val A = SincMatrix.csvread(
            filePath = filePath,
            separator = ",",
            headerInfo = listOf("t", "d", "d", "d", "d", "d", "d", "d", "d", "d")
        )
        val resultMATLAB = 1.040499533820819
        val sgMatrix = SincMatrix.from(SGCoeffs.sgo3x41)
        val result = A.getCols(2..10)
            .sgolayfilter(sgMatrix)
            .sum()
            .diff()
            .abs()
            .asRowVector()
            .cumsum()
            .asRowVector()
            .flip()
            .rms() / 10000.0
        SincMathsTests.assert((resultMATLAB - result).absoluteValue lt convTestTol)
    }

    fun performAll() {
        testMatrixCSVRead()
    }

}