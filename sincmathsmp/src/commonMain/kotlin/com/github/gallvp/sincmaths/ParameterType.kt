package com.github.gallvp.sincmaths

enum class ConvolutionShape(val rawValue: Int) {
    FULL(1), SAME(2), VALID(3);

    companion object {
        operator fun invoke(rawValue: Int) =
            entries.firstOrNull { it.rawValue == rawValue }
    }
}

enum class MovWinShape(val rawValue: Int) {
    SHRINK(1), DISCARD(2);

    companion object {
        operator fun invoke(rawValue: Int) =
            entries.firstOrNull { it.rawValue == rawValue }
    }
}

enum class AngleSequence(val rawValue: Int) {
    ZYX(1), ZYZ(2), XYZ(3);

    companion object {
        operator fun invoke(rawValue: Int) =
            entries.firstOrNull { it.rawValue == rawValue }
    }
}
