#ifndef __INCLUDED_ORG_KARLBENNETT_JNI_TEST_JNITESTCLASSTWO_CPP__
#define __INCLUDED_ORG_KARLBENNETT_JNI_TEST_JNITESTCLASSTWO_CPP__

#include <org_karlbennett_jni_test_JniTestClassTwo.h>

/*
 * Class:     org_karlbennett_jni_test_JniTestClassTwo
 * Method:    nativeMethod
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_org_karlbennett_jni_test_JniTestClassTwo_nativeMethod (JNIEnv *env, jclass jniTestClassTwo) {

    return env->NewStringUTF("Test string from native JniTestClassTwo.");
}

#endif /* __INCLUDED_ORG_KARLBENNETT_JNI_TEST_JNITESTCLASSTWO_CPP__ */
