//[sincmathsmp](../../index.md)/[com.github.gallvp.sincmaths](index.md)/[get](get.md)

# get

[common]\
operator fun [SincMatrix](-sinc-matrix/index.md).[get](get.md)(mlScript: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [SincMatrix](-sinc-matrix/index.md)

Supported Octave scripts: 1:5,4:7 and 1:5,4 and 1:5,: and 1,4:7 and :,4:7 and : and 1:end,end:end-1 and 1:5 and 1:end-1 and end:end-1

Indexing starts at 1, like Octave/MATLAB.

[common]\
fun [SincMatrix](-sinc-matrix/index.md).[get](get.md)(selector: (endR: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), endC: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), allR: [IntRange](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.ranges/-int-range/index.html), allC: [IntRange](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.ranges/-int-range/index.html)) -&gt; [Pair](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)&lt;[IntRange](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.ranges/-int-range/index.html), [IntRange](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.ranges/-int-range/index.html)&gt;): [SincMatrix](-sinc-matrix/index.md)

fun [SincMatrix](-sinc-matrix/index.md).[get](get.md)(selector: (end: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), all: [IntRange](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.ranges/-int-range/index.html)) -&gt; [IntRange](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.ranges/-int-range/index.html)): [SincMatrix](-sinc-matrix/index.md)

kotlin-way of doing get(mlScript: String).

[common]\
operator fun [SincMatrix](-sinc-matrix/index.md).[get](get.md)(indices: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html)): [SincMatrix](-sinc-matrix/index.md)

operator fun [SincMatrix](-sinc-matrix/index.md).[get](get.md)(indices: [IntRange](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.ranges/-int-range/index.html)): [SincMatrix](-sinc-matrix/index.md)

Indexing starts at 1, like Octave/MATLAB.

#### Return

A column vector.

[common]\
operator fun [SincMatrix](-sinc-matrix/index.md).[get](get.md)(mlRows: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html), mlCols: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html)): [SincMatrix](-sinc-matrix/index.md)

operator fun [SincMatrix](-sinc-matrix/index.md).[get](get.md)(mlRows: [IntRange](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.ranges/-int-range/index.html), mlCols: [IntRange](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.ranges/-int-range/index.html)): [SincMatrix](-sinc-matrix/index.md)

operator fun [SincMatrix](-sinc-matrix/index.md).[get](get.md)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)

operator fun [SincMatrix](-sinc-matrix/index.md).[get](get.md)(mlRow: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), mlCol: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)

Indexing starts at 1, like Octave/MATLAB.
