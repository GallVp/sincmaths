//[sincmathsmp](../../../../index.md)/[com.github.gallvp.sincmaths](../../index.md)/[SincMatrix](../index.md)/[Companion](index.md)

# Companion

[common]\
expect object [Companion](index.md)

[android, ios]\
actual object [Companion](index.md)

## Functions

| Name | Summary |
|---|---|
| [csvRead](../../csv-read.md) | [common]<br>fun [SincMatrix.Companion](index.md).[csvRead](../../csv-read.md)(filePath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), separator: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) = &quot;,&quot;, headerInfo: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; = listOf(), dateFormat: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) = &quot;yyyy-MM-dd HH:mm:ss.SSS&quot;, bundleID: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null): [SincMatrix](../index.md)<br>For example usage see SincMathsTests/SincMatrixIO. |
| [eul2rotm](../../eul2rotm.md) | [common]<br>fun [SincMatrix.Companion](index.md).[eul2rotm](../../eul2rotm.md)(xyzRadianAngles: [DoubleArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double-array/index.html), sequence: [AngleSequence](../../-angle-sequence/index.md)): [SincMatrix](../index.md) |
| [from](../../from.md) | [common]<br>fun [SincMatrix.Companion](index.md).[from](../../from.md)(script: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [SincMatrix](../index.md)<br>Creates a SincMatrix from Octave scripts, such as: |
| [nans](../../nans.md) | [common]<br>fun [SincMatrix.Companion](index.md).[nans](../../nans.md)(m: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), n: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [SincMatrix](../index.md) |
| [ones](../../ones.md) | [common]<br>fun [SincMatrix.Companion](index.md).[ones](../../ones.md)(m: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), n: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [SincMatrix](../index.md) |
| [quat2rotm](../../quat2rotm.md) | [common]<br>fun [SincMatrix.Companion](index.md).[quat2rotm](../../quat2rotm.md)(quat: [DoubleArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double-array/index.html)): [DoubleArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double-array/index.html) |
| [rotm2quat](../../rotm2quat.md) | [common]<br>fun [SincMatrix.Companion](index.md).[rotm2quat](../../rotm2quat.md)(rotm: [DoubleArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double-array/index.html)): [DoubleArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double-array/index.html)<br>Citation: Mike Day, Insomniac Games, &quot;Converting a Rotation Matrix to a Quaternion&quot;. The code in this document is for post-multiplying rotation matrix. |
| [zeros](../../zeros.md) | [common]<br>fun [SincMatrix.Companion](index.md).[zeros](../../zeros.md)(m: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), n: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [SincMatrix](../index.md) |
