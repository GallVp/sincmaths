//[sincmathsmp](../../index.md)/[io.github.gallvp.sincmaths](index.md)/[filtFilt](filt-filt.md)

# filtFilt

[common]\
fun [SincMatrix](-sinc-matrix/index.md).[filtFilt](filt-filt.md)(bVector: [DoubleArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double-array/index.html), aVector: [DoubleArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double-array/index.html)): [SincMatrix](-sinc-matrix/index.md)

Only second order filters are supported. Thus, length(B) == length(A) == 3 is assumed.
