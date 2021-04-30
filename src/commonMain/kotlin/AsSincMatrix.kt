package sincmaths

import sincmaths.sincmatrix.*

fun Double.asSincMatrix(): SincMatrix {
    return SincMatrix(doubleArrayOf(this), 1, 1)
}

fun DoubleArray.asSincMatrix(asRowVector: Boolean = true): SincMatrix = if (asRowVector) {
    SincMatrix(this, 1, this.size)
} else {
    SincMatrix(this, this.size, 1)
}

fun DoubleArray.asSincMatrix(m: Int, n: Int): SincMatrix {
    return SincMatrix(this, m, n)
}

fun List<Double>.asSincMatrix(asRowVector: Boolean = true): SincMatrix = if (asRowVector) {
    SincMatrix(this.toDoubleArray(), 1, this.size)
} else {
    SincMatrix(this.toDoubleArray(), this.size, 1)
}

fun List<Double>.asSincMatrix(m: Int, n: Int): SincMatrix {
    return SincMatrix(this.toDoubleArray(), m, n)
}

fun IntArray.asSincMatrix(asRowVector: Boolean = true): SincMatrix {
    return this.map { it.toDouble() }.asSincMatrix(asRowVector)
}

fun IntArray.asSincMatrix(m: Int, n: Int): SincMatrix {
    return this.map { it.toDouble() }.asSincMatrix(m, n)
}

fun IntRange.asSincMatrix(m: Int, n: Int): SincMatrix {
    return this.map { it.toDouble() }.asSincMatrix(m, n)
}

fun IntRange.asSincMatrix(asRowVector: Boolean = true): SincMatrix {
    return this.map { it.toDouble() }.asSincMatrix(asRowVector)
}

fun List<Int>.asSincMatrix(asRowVector: Boolean = true, intType: Any? = null): SincMatrix {
    return this.map { it.toDouble() }.asSincMatrix(asRowVector)
}

fun List<Int>.asSincMatrix(m: Int, n: Int, intType: Any? = null): SincMatrix {
    return this.map { it.toDouble() }.asSincMatrix(m, n)
}

fun DoubleArray.asSincMatrix(size: List<Int>): SincMatrix {
    require(size.count() == 2) { "SMError: Size should have two elements." }

    return this.asSincMatrix(size[0], size[1])
}

fun List<SincMatrix>.makeMatrixFrom(rowVectors:Boolean = true):SincMatrix  {

    if(this.isEmpty()) {
        return SincMatrix(doubleArrayOf(), 0, 0)
    }

    val sz = this.first().size()

    this.map {
        require(it.isvector()) {"SMError: Only vectors can be joined." }
        require(it.size() == sz) {"SMError: Size across vectors must be invariant." }
    }

    val vecLen = this.first().numel()

    return if(rowVectors) {
        this.flatMap {
            it.asRowMajorArray().toList()
        }.asSincMatrix(this.size, vecLen)
    } else {
        this.flatMap {
            it.asRowMajorArray().toList()
        }.asSincMatrix(this.size, vecLen).t
    }
}

fun Array<SincMatrix>.asRowVector():SincMatrix = when {
    this.isEmpty() -> {
        emptySincMatrix()
    }
    this.size == 1 -> {
        this[0].reshape(1, this[0].numel())
    }
    else -> {
        this.flatMap {
            it.asRowMajorArray().asList()
        }.asSincMatrix()
    }
}