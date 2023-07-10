# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.18.1)

# Declares and names the project.

project("matrixtool")

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

#添加库的头文件.h
set(LIB_PATH ${PROJECT_SOURCE_DIR}/libs)
MESSAGE("Lib路径==" ${LIB_PATH})

include_directories(src/)
include_directories(src/ffmpeg/)
include_directories(src/ffmpeg/ffmpeg)
include_directories(src/ffmpeg/include)

#检测系统架构
if(${CMAKE_ANDROID_ARCH_ABI} MATCHES "armeabi-v7a")
    include_directories(src/ffmpeg/include/armeabi-v7a)
    message("This is armeabi-v7a")
elseif(${CMAKE_ANDROID_ARCH_ABI} MATCHES "arm64-v8a")
    include_directories(src/ffmpeg/include/arm64-v8a)
    message("This is arm64-v8a")
endif()


add_library( # Sets the name of the library.
        matrixtool

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        native-lib.cpp
        src/ffmpeg/ffmpeg/cmdutils.c
        src/ffmpeg/ffmpeg/ffmpeg.c
        src/ffmpeg/ffmpeg/ffmpeg_filter.c
        src/ffmpeg/ffmpeg/ffmpeg_opt.c
        src/ffmpeg/ffmpeg_cmd.c
        src/ffmpeg/ffmpeg/ffprobe.c
        src/ffmpeg/ffmpeg/ffmpeg_hw.c
        src/ffmpeg/audio_player.cpp
        src/ffmpeg/opensl_audio_player.cpp
        src/ffmpeg/video_player.cpp
        src/ffmpeg/ffmpeg_pusher.cpp
        src/ffmpeg/video_filter.c
        src/ffmpeg/ffprobe_cmd.cpp
        src/ffmpeg/cut_video.cpp
        src/ffmpeg/visualizer/fft.cpp
        src/ffmpeg/visualizer/fixed_fft.cpp
        src/ffmpeg/visualizer/block_queue.c
        src/ffmpeg/visualizer/frank_visualizer.cpp
        src/ffmpeg/visualizer/frank_visualizer_jni.cpp
        src/ffmpeg/visualizer/window.cpp
        src/ffmpeg/metadata/media_retriever.cpp
        src/ffmpeg/metadata/media_retriever_jni.cpp
        src/ffmpeg/metadata/metadata_util.c
        src/ffmpeg/metadata/ffmpeg_media_retriever.c
        )
add_library( ffmpeg
        SHARED
        IMPORTED )
set_target_properties(ffmpeg
        PROPERTIES IMPORTED_LOCATION
        ${LIB_PATH}/${CMAKE_ANDROID_ARCH_ABI}/libffmpeg.so)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        matrixtool

        ffmpeg
        #sox
        -landroid #native_window
        -ljnigraphics #bitmap
        -lOpenSLES #openSLES
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})