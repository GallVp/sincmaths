#include <jni.h>
#include "tinyexpr.h"

extern "C"
JNIEXPORT jdouble JNICALL
Java_com_github_gallvp_sincmaths__1SincMatrixWorkersKt_parseToDouble(JNIEnv *env, jclass clazz,
                                                                     jstring expr) {
    const char *nativeString = env->GetStringUTFChars(expr, 0);
    double ans = te_interp(nativeString, 0);
    return ans;
}