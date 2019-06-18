#include <jni.h>
#include "MD5.h"



extern "C"
//使用c中的md5加密，可以将盐加入不容易被反编译出来
JNIEXPORT jstring JNICALL Java_com_dengpan_MainActivity_getMd5(JNIEnv *env, jobject, jstring str) {
    const char *originStr;
    //将jstring转化成char *类型
    originStr = env->GetStringUTFChars(str, false);
    MD5 md5 = MD5(originStr);
    std::string md5Result = md5.hexdigest();
    //将char *类型转化成jstring返回给Java层
    return env->NewStringUTF(md5Result.c_str());
}




extern "C"
JNIEXPORT jstring JNICALL Java_com_tantuo_didicar_DriverRFIDMainActivity_getMd5(JNIEnv *env, jobject instance, jstring origin_) {
    const char *originStr;
    //将jstring转化成char *类型
    originStr = env->GetStringUTFChars(origin_, false);
    MD5 md5 = MD5(originStr);
    std::string md5Result = md5.hexdigest();
    //将char *类型转化成jstring返回给Java层
    return env->NewStringUTF(md5Result.c_str());
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_tantuo_didicar_DriverLicenseNFC_DriverRFIDMainActivity_getMd5(JNIEnv *env,
                                                                       jobject instance,
                                                                       jstring origin_) {
    const char *originStr;
    //将jstring转化成char *类型
    originStr = env->GetStringUTFChars(origin_, false);
    MD5 md5 = MD5(originStr);
    std::string md5Result = md5.hexdigest();
    //将char *类型转化成jstring返回给Java层
    return env->NewStringUTF(md5Result.c_str());
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_tantuo_didicar_utils_JniUtils_getMd5(JNIEnv *env, jobject instance, jstring origin_) {
    const char *originStr;
    //将jstring转化成char *类型
    originStr = env->GetStringUTFChars(origin_, false);
    MD5 md5 = MD5(originStr);
    std::string md5Result = md5.hexdigest();
    //将char *类型转化成jstring返回给Java层
    return env->NewStringUTF(md5Result.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_tantuo_didicar_utils_MD5JniUtils_getMd5(JNIEnv *env, jclass type, jstring origin_) {
    const char *origin = env->GetStringUTFChars(origin_, 0);
    const char *originStr;
    //将jstring转化成char *类型
    originStr = env->GetStringUTFChars(origin_, false);
    MD5 md5 = MD5(originStr);
    std::string md5Result = md5.hexdigest();
    //将char *类型转化成jstring返回给Java层
    return env->NewStringUTF(md5Result.c_str());
}