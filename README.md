# SincMaths

SincMaths is a Kotlin Multiplatform library which provides a 2D matrix `SincMatrix` to
facilitate translation of signal processing code written in Octave/MATLAB to mobile applications.
Originally written for the [Gait&Balance app](https://doi.org/10.3390/s22010124).
See [docs](https://gallvp.github.io/sincmaths/index.html) for a comprehensive list of functions.
Key implementation aspects:

+ Stores data in row-major format as opposed to column-major format used by Octave/MATLAB
+ Uses [ejml-simple](https://github.com/lessthanoptimal/ejml) for Android side optimisations
+ Uses [Apple Accelerate](https://developer.apple.com/accelerate/) for iOS side optimisations
+ Uses [rafat/wavelib](https://github.com/rafat/wavelib) to provide wavelet-based demonising
+ Uses [codeplea/tinyexpr](https://github.com/codeplea/tinyexpr) to provide Octave/MATLAB style
  indexing

## Examples

### Initialisation

```kotlin
val matrixA = matrixFrom("[1, 2, 3; 4, 5, 6]")
val matrixB = matrixFrom("[1, 2, 3, 4]")
val matrixC = matrixFrom("[5;6;7;8;9;10]")
val matrixD = matrixFrom("1:10")
val matrixE = matrixFrom("-1.5:-1:-7.9")

val matrixF = matrixOf(2, 10, 1..20)
val matrixG = colVectorOf(1.0, 0.5, 2.9, 10.1, 15.4)
```

### Indexing

```kotlin
val matrixM: SincMatrix = (1..110).asSincMatrix(11, 10)

val matrixA = matrixM["1:5,4:7"]
val matrixB = matrixM["1:end,end:-1:end-1"]

val matrixC = matrixM[3, 3]
val matrixD = matrixM[1]

val matrixE = matrixM[1..4]

val matrixF = matrixM.get { endR, endC, allR, allC ->
    Pair(allR, 4..7)
} // same as matrixM[":,4:7"]
```

### Implementation of `acf` from MATLAB Central

Reference: <https://au.mathworks.com/matlabcentral/fileexchange/30540-autocorrelation-function-acf>

```kotlin
fun SincMatrix.acf(numLags: Int): SincMatrix {

    require(this.isVector) { "This function works only for vectors" }
    require(numLags < this.numel) {
        "No. of lags should be smaller than the length of the vector"
    }

    val zeroMeanVector = this - this.mean().scalar
    val convSum = zeroMeanVector.conv(bVector = zeroMeanVector.flip())
    val scale = 1.0 / zeroMeanVector.dot(zeroMeanVector).scalar
    val scaledConvSum = convSum * scale
    val acfElements = this.numel + 1..this.numel + numLags
    return if (this.isRow) {
        scaledConvSum[this.rowIndicesRange, acfElements]
    } else {
        scaledConvSum[acfElements, this.colIndicesRange]
    }
}
```

## Usage

### Kotlin Project: [MavenCentral](https://central.sonatype.com/artifact/io.github.gallvp/sincmathsmp)

> **Note:** This library does not work with `iosSimulatorArm64` target. Android tests have to be
> performed on an emulator or a connected device, otherwise, the underlying JAVA JNI libraries
> fail to load.

Add dependency:

```kotlin
implementation("io.github.gallvp:sincmathsmp:0.3")
```

Try a test:

```kotlin
expect(5050.0) {
    matrixFrom("1:100").sum().scalar
}
```

### Swift Project: [CocoaPods](https://kotlinlang.org/docs/native-cocoapods.html#update-podfile-for-xcode)

> **Note:** Global functions such as `matrixFrom` are converted into open class functions as
> `SincMatrixInitKt.matrixFrom`. Use 'Jump to Definition' on 'SincMaths' to locate the class and
> function definitions.

Clone this project and add it to your project's Podfile:

```pod
pod 'SincMaths', :path => '/path/to/cloned/sincmaths/sincmathsmp'
```

Import it in your project and try a test:

```swift
import SincMaths

XCTAssertEqual(
    SincMatrixInitKt.matrixFrom(script: "1:100").sum(dim: 1).scalar,
    5050.0
)
```
