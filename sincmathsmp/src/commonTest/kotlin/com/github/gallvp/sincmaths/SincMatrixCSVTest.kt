package com.github.gallvp.sincmaths

import com.github.gallvp.sincmaths.SincMathsTest.Companion.convTestTol


class SincMatrixCSVTest {

    private fun testMatrixCSVRead() {

        val filePath = "test_csv.csv"
        val A = SincMatrix.csvRead(
            filePath = filePath,
            separator = ",",
            headerInfo = listOf("t", "d", "d", "d", "d", "d", "d", "d", "d", "d")
        )
        val resultMATLAB = 1.040499533820819
        val sgMatrix = SincMatrix.from(SGCoeffs.sgo3x41)
        val result = A.getCols(2..10)
            .sgolayFilter(sgMatrix)
            .sum()
            .diff()
            .abs()
            .asRowVector()
            .cumSum()
            .asRowVector()
            .flip()
            .rms() / 10000.0
        SincMathsTest.assert((resultMATLAB - result).absoluteValue lt convTestTol)
    }

    fun performAll() {
        testMatrixCSVRead()
    }

}