import kotlin.test.Test

class SincMathsTests {

    @Test
    fun runAllTests() {
        SincMatrixSignalProc().SincMatrixSignalProcTests()
    }

    companion object {
        fun assert(condition: Boolean, message: () -> String) {
            if (!condition) {
                throw Exception(message())
            }
        }
    }
}