//[sincmathsmp](../../index.md)/[com.github.gallvp.sincmaths](index.md)/[sgolayFilter](sgolay-filter.md)

# sgolayFilter

[common]\
fun [SincMatrix](-sinc-matrix/index.md).[sgolayFilter](sgolay-filter.md)(bMatrix: [SincMatrix](-sinc-matrix/index.md), dim: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 1): [SincMatrix](-sinc-matrix/index.md)

This function is like Octave's sgolayfilt, except that instead of order and filter length Savitzky-Golay a pre-computed projection matrix ([bMatrix](sgolay-filter.md)) is supplied.
