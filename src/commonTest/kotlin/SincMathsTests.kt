import kotlin.test.Test

class SincMathsTests {

    @Test
    fun runAllTests() {
        SincMatrixIOTests().runSincMatrixIOTests()
        SincMatrixSignalProcTests().runSignalProcTests()
    }

    companion object {
        fun assert(condition: Boolean, message: () -> String) {
            if (!condition) {
                throw Exception(message())
            }
        }
    }
}