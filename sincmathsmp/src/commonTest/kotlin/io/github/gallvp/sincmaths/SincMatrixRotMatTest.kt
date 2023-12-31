package io.github.gallvp.sincmaths

import kotlin.math.PI

class SincMatrixRotMatTest {
    private fun testMatrixEul2Rotm() {
        // Check orthogonality. Result eye(3)
        // Check 360 degree rotation. Result eye(3)
        val matA =
            SincMatrix.eul2rotm(
                doubleArrayOf(PI / 2.0, -PI / 1.05, PI / 3.03),
                AngleSequence.XYZ,
            )
        val matB =
            SincMatrix.eul2rotm(
                doubleArrayOf(2.0 * PI, 2.0 * PI, 2.0 * PI),
                AngleSequence.XYZ,
            )
        val resultA = (matA * matA.t)
        val eyeMat =
            doubleArrayOf(1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0).asSincMatrix(m = 3, n = 3)
        SincMathsTest.assert(((resultA - eyeMat) lt SincMathsTest.testTol).all())
        SincMathsTest.assert(((matB - eyeMat) lt SincMathsTest.testTol).all())
    }

    private fun testMatrixQuatConverter() {
        val testMat =
            SincMatrix.eul2rotm(
                doubleArrayOf(PI / 2.0, -PI / 1.05, PI / 3.03),
                AngleSequence.XYZ,
            )
        val testDiff = (testMat.rotm2quat().quat2rotm() - testMat).abs()
        val testResults = (testDiff lt SincMathsTest.testTol).all()
        SincMathsTest.assert(testResults)
    }

    fun performAll() {
        testMatrixEul2Rotm()
        testMatrixQuatConverter()
    }
}
