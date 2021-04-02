import kotlin.math.PI

class SincMatrixRobotics {
    private fun testMatrixEul2Rotm() {
        val rotmA = SincMatrix.eul2rotm(
            xyzRadianAngles = doubleArrayOf(PI / 2.0, -PI / 1.05, PI / 3.03),
            sequence = AngleSequence.XYZ
        )
        val rotmB = SincMatrix.eul2rotm(
            xyzRadianAngles = doubleArrayOf(2.0 * PI, 2.0 * PI, 2.0 * PI),
            sequence = AngleSequence.XYZ
        )
        val resultA = (rotmA * rotmA.transpose())
        // Check orthognality. Result eye(3)
        // Check 360 degree rotation. Result eye(3)
        val testTol = 1.0E-15
        val eyeMat =
            doubleArrayOf(1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0).asSincMatrix(m = 3, n = 3)
        SincMathsTests.assert((((resultA - eyeMat)).lessThan(testTol)).all()) { "testMatrixEul2Rotm failed..." }
        SincMathsTests.assert((((rotmB - eyeMat)).lessThan(testTol)).all()) { "testMatrixEul2Rotm failed..." }
    }

    private fun testMatrixQuatConverter() {
        val testMat = SincMatrix.eul2rotm(
            xyzRadianAngles = doubleArrayOf(PI / 2.0, -PI / 1.05, PI / 3.03),
            sequence = AngleSequence.XYZ
        )
        val testTol = 1.0E-15
        val testDiff = (testMat.rotm2quat().quat2rotm() - testMat).abs()
        val testResults = (testDiff.lessThan(testTol)).all()
        SincMathsTests.assert(testResults) { "testMatrixQuatConverter failed..." }
    }

    fun performAll() {
        testMatrixEul2Rotm()
        testMatrixQuatConverter()
    }

}