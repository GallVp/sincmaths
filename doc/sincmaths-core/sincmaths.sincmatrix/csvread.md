//[sincmaths-core](../../index.md)/[sincmaths.sincmatrix](index.md)/[csvread](csvread.md)



# csvread  
[common]  
Content  
fun [SincMatrix.Companion](../sincmaths/-sinc-matrix/-companion/index.md).[csvread](csvread.md)(filePath: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), separator: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) = ",", headerInfo: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)<[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)> = listOf(), dateFormat: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) = "yyyy-MM-dd HH:mm:ss.SSS", bundleID: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null): [SincMatrix](../sincmaths/-sinc-matrix/index.md)  
More info  


For example usage see SincMathsTests/SincMatrixIO.



## Parameters  
  
common  
  
| | |
|---|---|
| <a name="sincmaths.sincmatrix//csvread/sincmaths.SincMatrix.Companion#kotlin.String#kotlin.String#kotlin.collections.List[kotlin.String]#kotlin.String#kotlin.String?/PointingToDeclaration/"></a>headerInfo| <a name="sincmaths.sincmatrix//csvread/sincmaths.SincMatrix.Companion#kotlin.String#kotlin.String#kotlin.collections.List[kotlin.String]#kotlin.String#kotlin.String?/PointingToDeclaration/"></a><br><br>An array of column types. Following column types are allowed: d: Double t: Date which is then converted to double as time interval in seconds since 1970. If headerInfo is empty, no header row is assumed. Default date format: "yyyy-MM-dd HH:mm:ss.SSS"<br><br>|
| <a name="sincmaths.sincmatrix//csvread/sincmaths.SincMatrix.Companion#kotlin.String#kotlin.String#kotlin.collections.List[kotlin.String]#kotlin.String#kotlin.String?/PointingToDeclaration/"></a>bundleID| <a name="sincmaths.sincmatrix//csvread/sincmaths.SincMatrix.Companion#kotlin.String#kotlin.String#kotlin.collections.List[kotlin.String]#kotlin.String#kotlin.String?/PointingToDeclaration/"></a><br><br>A string identifier for the iOS bundle which contains the file.<br><br>|
  
  



