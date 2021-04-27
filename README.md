# SincMathsMP Library

This library provides a 2D matrix which stores data in row-major format as opposed to column-major format used by Octave/MATLAB. The library aims to provide functions with names similar to Octave/MATLAB.

## Change Log

### Version 0.1 <27042021>

+ Ported code from older SincMaths libraries in kotlin/jvm (v0.3) and swift (v0.3) to a single kotlin/jvm/native library.
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
