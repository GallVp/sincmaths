//[sincmathsmp](../../index.md)/[sincmaths](index.md)/[filtfilt](filtfilt.md)

# filtfilt

[common]\
fun [SincMatrix](-sinc-matrix/index.md).[filtfilt](filtfilt.md)(B: [DoubleArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double-array/index.html), A: [DoubleArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double-array/index.html)): [SincMatrix](-sinc-matrix/index.md)

Only second order filters are supported. Thus, length(B) == length(A) == 3 is assumed.
