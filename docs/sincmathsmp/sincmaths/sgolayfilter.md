//[sincmathsmp](../../index.md)/[sincmaths](index.md)/[sgolayfilter](sgolayfilter.md)

# sgolayfilter

[common]\
fun [SincMatrix](-sinc-matrix/index.md).[sgolayfilter](sgolayfilter.md)(B: [SincMatrix](-sinc-matrix/index.md), dim: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 1): [SincMatrix](-sinc-matrix/index.md)

This function is like Octave's sgolayfilt, except that instead of order and filter length Savitzky-Golay a pre-computed projection matrix ([B](sgolayfilter.md)) is supplied.
