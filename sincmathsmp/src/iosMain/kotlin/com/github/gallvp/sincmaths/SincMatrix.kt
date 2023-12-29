package com.github.gallvp.sincmaths

actual class SincMatrix actual constructor(rowMajArray: DoubleArray, internal var m: Int, internal var n: Int) {

    internal var matrixData: DoubleArray

    init {
        require(rowMajArray.size == m * n) { "length(rowMajArray) should be equal to m*n" }
        matrixData = rowMajArray
    }

    actual override fun toString(): String = this.description

    actual companion object
}

