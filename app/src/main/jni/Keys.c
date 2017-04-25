#include <string.h>
#include <jni.h>

Java_co_techmagic_hr_presentation_util_JniUtils_getKey(JNIEnv* env, jobject thiz )
{
    return (*env)->NewStringUTF(env, "12345678yealieEE");
}

jstring
Java_co_techmagic_hr_presentation_util_JniUtils_getInitializationVector(JNIEnv* env, jobject thiz )
{
    return (*env)->NewStringUTF(env, "87654321yealieEE");
}