import SincMathsTests.Companion.testTol
import kotlin.math.PI

class SincMatrixRobotics {
    private fun testMatrixEul2Rotm() {
        // Check orthogonality. Result eye(3)
        // Check 360 degree rotation. Result eye(3)
        val matA = SincMatrix.eul2rotm(doubleArrayOf(PI / 2.0, -PI / 1.05, PI / 3.03), AngleSequence.XYZ)
        val matB = SincMatrix.eul2rotm(doubleArrayOf(2.0 * PI, 2.0 * PI, 2.0 * PI), AngleSequence.XYZ)
        val resultA = (matA * matA.t)
        val eyeMat = doubleArrayOf(1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0).asSincMatrix(m = 3, n = 3)
        SincMathsTests.assert(((resultA - eyeMat) lt testTol).all())
        SincMathsTests.assert(((matB - eyeMat) lt testTol).all())
    }

    private fun testMatrixQuatConverter() {
        val testMat = SincMatrix.eul2rotm(doubleArrayOf(PI / 2.0, -PI / 1.05, PI / 3.03), AngleSequence.XYZ)
        val testDiff = (testMat.rotm2quat().quat2rotm() - testMat).abs()
        val testResults = (testDiff lt testTol).all()
        SincMathsTests.assert(testResults)
    }

    fun performAll() {
        testMatrixEul2Rotm()
        testMatrixQuatConverter()
    }

}