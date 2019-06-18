cmake_minimum_required(VERSION 3.6)

add_library( # Sets the name of the library.
        native-lib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        src/main/jni/MD5.cpp
        src/main/jni/native-lib.cpp)


