package com.github.gallvp.sincmaths

import kotlin.test.Test

class SincMathsTest {
    @Test
    fun performAll() {
        SincMatrixTest().performAll()
        SincMatrixCSVTest().performAll()
        SincMatrixRotMatTest().performAll()
        SincMatrixMathsStatsTest().performAll()
        SincMatrixSignalTest().performAll()
    }

    companion object {
        fun assert(condition: Boolean) {
            if (!condition) {
                throw Exception("Test failure...")
            }
        }

        fun assert(condition: SincMatrix) {
            if (!condition.boolean) {
                throw Exception("Test failure...")
            }
        }

        const val testTol = 1.0E-15
        const val multSumTestTol = 1.0E-14
        const val convTestTol = 1.0E-13
        const val convTestTolAndroid = 1.0E-12
    }
}
