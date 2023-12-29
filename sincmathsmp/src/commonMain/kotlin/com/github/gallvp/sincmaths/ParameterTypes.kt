package com.github.gallvp.sincmaths

enum class ConvolutionShape(val rawValue: Int) {
    full(1), same(2), valid(3);

    companion object {
        operator fun invoke(rawValue: Int) =
            ConvolutionShape.values().firstOrNull { it.rawValue == rawValue }
    }
}

enum class MovWinShape(val rawValue: Int) {
    shrink(1), discard(2);

    companion object {
        operator fun invoke(rawValue: Int) =
            MovWinShape.values().firstOrNull { it.rawValue == rawValue }
    }
}

enum class AngleSequence(val rawValue: Int) {
    ZYX(1), ZYZ(2), XYZ(3);

    companion object {
        operator fun invoke(rawValue: Int) =
            AngleSequence.values().firstOrNull { it.rawValue == rawValue }
    }
}
