#include <jni.h>
#include <string>
#include "include/wt_helpers.h"
#include "conv.h"

extern "C"
JNIEXPORT jdoubleArray JNICALL
Java_com_github_gallvp_sincmaths_ASincMatrixWorkersKt_convWithWavelib(JNIEnv *env, jclass clazz,
                                                                       jdoubleArray signal_vector,
                                                                       jint signal_length,
                                                                       jdoubleArray kernel_vector,
                                                                       jint kernel_length) {

    jdouble *input_signal = env->GetDoubleArrayElements(signal_vector, 0);
    jdouble *kernel_signal = env->GetDoubleArrayElements(kernel_vector, 0);
    jdouble output_signal[signal_length + 2 * kernel_length];

    conv_direct(input_signal,signal_length, kernel_signal, kernel_length,output_signal);

    jdoubleArray returnArray = env->NewDoubleArray(signal_length + 2 * kernel_length);

    env->SetDoubleArrayRegion(returnArray, 0, signal_length + 2 * kernel_length, output_signal);

    return returnArray;
}
extern "C"
JNIEXPORT jdoubleArray JNICALL
Java_com_github_gallvp_sincmaths_ASincMatrixWorkersKt_diffCWTFT(JNIEnv *env, jclass clazz,
                                                                 jdoubleArray signal_vector,
                                                                 jint signal_length, jdouble scale,
                                                                 jdouble dt) {
    jdouble *input_signal = env->GetDoubleArrayElements(signal_vector, 0);
    jdouble output_signal[signal_length];

    diff_cwtft(input_signal, output_signal, signal_length, scale, dt);

    jdoubleArray returnArray = env->NewDoubleArray(signal_length);

    env->SetDoubleArrayRegion(returnArray, 0, signal_length, output_signal);

    return returnArray;
}