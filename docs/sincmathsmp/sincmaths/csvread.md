//[sincmathsmp](../../index.md)/[sincmaths](index.md)/[csvread](csvread.md)

# csvread

[common]\
fun [SincMatrix.Companion](-sinc-matrix/-companion/index.md).[csvread](csvread.md)(filePath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), separator: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) = ",", headerInfo: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)&gt; = listOf(), dateFormat: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) = "yyyy-MM-dd HH:mm:ss.SSS", bundleID: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null): [SincMatrix](-sinc-matrix/index.md)

For example usage see SincMathsTests/SincMatrixIO.

## Parameters

common

| | |
|---|---|
| headerInfo | An array of column types. Following column types are allowed: d: Double t: Date which is then converted to double as time interval in seconds since 1970. If headerInfo is empty, no header row is assumed. Default date format: "yyyy-MM-dd HH:mm:ss.SSS" |
| bundleID | A string identifier for the iOS bundle which contains the file. |
