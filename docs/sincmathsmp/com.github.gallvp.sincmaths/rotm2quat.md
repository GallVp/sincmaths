//[sincmathsmp](../../index.md)/[com.github.gallvp.sincmaths](index.md)/[rotm2quat](rotm2quat.md)

# rotm2quat

[common]\
fun [SincMatrix](-sinc-matrix/index.md).[rotm2quat](rotm2quat.md)(): [SincMatrix](-sinc-matrix/index.md)

[common]\
fun [SincMatrix.Companion](-sinc-matrix/-companion/index.md).[rotm2quat](rotm2quat.md)(rotm: [DoubleArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double-array/index.html)): [DoubleArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double-array/index.html)

Citation: Mike Day, Insomniac Games, &quot;Converting a Rotation Matrix to a Quaternion&quot;. The code in this document is for post-multiplying rotation matrix.

#### Parameters

common

| | |
|---|---|
| rotm | A row major pre-multiplying 3 x 3 matrix. quat<w, x, y, z> |
