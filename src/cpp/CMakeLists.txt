# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
             wavelib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
        wavelib/wavelib-jni.cpp
                wavelib/wt_helpers.c
                wavelib/conv.c
                wavelib/cwt.c
                wavelib/cwtmath.c
                wavelib/hsfft.c
                wavelib/real.c
                wavelib/wavefilt.c
                wavelib/wavefunc.c
                wavelib/wavelib.c
                wavelib/wtmath.c)

add_library( # Sets the name of the library.
             tinyexpr

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
        tinyexpr/tinyexpr-jni.cpp
                tinyexpr/tinyexpr.c)

include_directories("wavelib/include")
include_directories("tinyexpr/include")