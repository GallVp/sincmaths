# SincMaths

This Kotlin Multiplatform library provides a 2D matrix `SincMatrix` with the aim to facilitate
translation of Octave/MATLAB code to mobile applications. `SincMatrix` stores data in row-major
format as opposed to column-major format used by Octave/MATLAB. See [docs](./docs/index.md) for 
a comprehensive list of functions.

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

### Implementation of `acf`

Reference: https://au.mathworks.com/matlabcentral/fileexchange/30540-autocorrelation-function-acf

```kotlin
require(this.isVector) { "This function works only for vectors" }
require(numLags < this.numel) {
    "No. of lags should be smaller than the length of the vector"
}

val zeroMeanVector = this - this.mean().scalar
val convSum = zeroMeanVector.conv(B = zeroMeanVector.flip())
val scale = 1.0 / zeroMeanVector.dot(zeroMeanVector).scalar
val scaledConvSum = convSum * scale
val numElements = ((this.numel + 1)..(this.numel + numLags)).toList().toIntArray()
return if (this.isrow()) {
    scaledConvSum.getCols(mlCols = numElements)
} else {
    scaledConvSum.getRows(mlRows = numElements)
}
```