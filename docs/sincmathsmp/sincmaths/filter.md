//[sincmathsmp](../../index.md)/[sincmaths](index.md)/[filter](filter.md)

# filter

[common]\
fun [SincMatrix](-sinc-matrix/index.md).[filter](filter.md)(B: [DoubleArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double-array/index.html), A: [DoubleArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double-array/index.html), dim: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 1): [SincMatrix](-sinc-matrix/index.md)

Only second order filters are supported. Thus, length(B) == length(A) == 3 is assumed.
