# Change Log

## Version 0.3 <29122023>

+ Ported the project to Android Studio
+ Updated kotlin to 1.9.20
+ Updated ejml-simple to 0.43.1
+ Updated AGP to 8.2.0
+ Added tinyexpr and wavelib libraries as submodules
+ `isVector` now replaced by `isVector`
+ `numRows()` and `numCols()` are now `numRows` and `numCols`
+ `length()` is now `length`
+ `size()` is now `size`
+ `isrow()` is now `isRow`
+ `iscolumn()` is now `isColumn`
+ `isempty()` is now `isEmpty()`
+ `isScalar` is now `isScalar`
+ `circshift()` is now `circShift`
+ `repmat()` is now `repMat()`
+ `csvread()` is now `csvRead()`
+ `transpose()` is now `transpose`
+ `movsum()` is now `movSum()`
+ `movmean()` is now `movMean()`
+ `filtfilt()` is now `filtfilt()`
+ `sgolayFilter()` is now `sgolayFilter()`
+ `findpeaks()` is now `findPeaks()`
+ `cumsum()` is now `cumSum()`

## Version 0.2.2 <13042022>

+ Updated Kotlin to 1.6.20.

## Version 0.2.1 <24032022>

+ Added `minI` and `maxI` funcs.

## Version 0.2 <23032022>

+ Updated kotlin to 1.6.10
+ Updated dokka to 1.6.10
+ Updated NDK to 23.0.7599858
+ Updated espresso-core to 3.4.0
+ Updated ejml-simple to 0.41
+ Added `quantile` func and its unit test.
+ Added `round` func.
+ Updated Android sdk to 31.
+ Simplified package structure.

## Version 0.1 <21052021>

+ Ported code from older SincMaths libraries in kotlin/jvm (v0.3) and swift (v0.3) to a single
  kotlin/jvm/native library.
+ Added new functions: `unaryMinus`, `and`, `or` and `not`.
+ Added new property: `scalar` and `t`.
+ Cleaned up tests and unified test tolerances.
+ Added `matrixFrom` func and package names.
+ Added dokka for documentation.
+ Added `et` and `net` for comparing two matrices.
+ Added `circshift` for vectors.
+ Added tests for `and` and `or` functions.
+ Updated kotlin and gradle.
+ Removed `set2(index: Int, value: Double)` function.
+ Renamed all `set2` functions to `setWithIndices`.
+ All set functions now mutate in place.
+ Added `setRow` and `setCol` functions.
+ Reimplemented `numel` and `length` functions to cater to empty matrix.
+ Added `cat` function and its corresponding test.
+ Added `A[9, 9]` type indexing.
+ Arithmetic and bool functions with scalar matrices now don't throw exceptions.
+ Added arithmetic and bool functions on Double for matrices as rhs.
+ Cleaned up tests to use newer patterns.
+ Added `map`, `mapColumns` and `mapRows`. As a result a number of functions now also run on
  matrices.
+ Added more functional extensions.
+ Added `testMatrixMedian` unit test.
+ Added `reshape` function and `testMatrixDiff` test.
+ Renamed `get` to `getWithLV`.
+ Added `repmat` and its corresponding test.
+ Added `getRows` with IntRange.
+ Added `emptySincMatrix` and `emptySincMatrices`.
+ Added `Array<SincMatrix>.asRowVector()`.
+ Added `iqr` compatible with MATLAB 2020b.
+ Added more tests for `iqr` and removed a bug.
+ Added `get(selector:...` functions.
+ Updated kotlin to 1.5.0-RC.
+ Updated kotlin to 1.5.0.
+ Changed `isScalar` definition to m x n == 1.
+ Improved type conversion for empty SincMatrix.
+ Improved `getWithLV` for empty SincMatrix.
+ Changed package name to `com.github.gallvp.sincmathsmp`.