#include <jni.h>
#include "tinyexpr.h"

extern "C"
JNIEXPORT jdouble JNICALL
Java_aut_libsinc_sincmaths_SincMatrix_00024Companion_parseToDouble(JNIEnv *env, jobject thiz,
                                                                   jstring expr) {

    const char *nativeString = env->GetStringUTFChars(expr, 0);
    double ans = te_interp(nativeString, 0);
    return ans;
}