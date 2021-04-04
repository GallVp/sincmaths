import kotlin.test.Test

class SincMathsTests {

    @Test
    fun runAllTests() {
        SincMatrixIOTests().performAll()
        SincMatrixRobotics().performAll()
        SincMatrixMathsAndStats().performAll()
        SincMatrixSignalProcTests().performAll()
    }

    companion object {
        fun assert(condition: Boolean) {
            if (!condition) {
                throw Exception()
            }
        }
        const val testTol = 1.0E-15
        const val multSumTestTol = 1.0E-14
        const val convTestTol = 1.0E-13
    }
}