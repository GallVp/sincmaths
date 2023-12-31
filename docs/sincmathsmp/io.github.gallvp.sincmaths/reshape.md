//[sincmathsmp](../../index.md)/[io.github.gallvp.sincmaths](index.md)/[reshape](reshape.md)

# reshape

[common]\
fun [SincMatrix](-sinc-matrix/index.md).[reshape](reshape.md)(m: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), n: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [SincMatrix](-sinc-matrix/index.md)

As SincMatrix is row-major, reshape picks data from rows first and also fills rows first. Thus, a reshape such as reshape(A, 4, 3) on SincMatrix is equal to reshape(A', 3, 4)' on MATLAB/Octave.
