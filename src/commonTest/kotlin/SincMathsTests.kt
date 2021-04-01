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
        fun assert(condition: Boolean, message: () -> String) {
            if (!condition) {
                throw Exception(message())
            }
        }
    }
}