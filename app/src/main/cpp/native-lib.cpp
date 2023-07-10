#include <jni.h>
#include <string>
#include <libavcodec/jni.h>

extern "C"{
#include <libavcodec/avcodec.h>
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_matrix_matrixtool_Ui_Activity_MainActivity_stringFromJNI(JNIEnv *env, jobject thiz) {
    // TODO: implement stringFromJNI()

    std::string hello = "Hello from C++";
    std::string ffmpeg_info=avcodec_configuration();
    return env->NewStringUTF(ffmpeg_info.c_str());
    return env->NewStringUTF(av_version_info());
}