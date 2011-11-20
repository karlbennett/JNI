#ifndef __INCLUDED_ORG_KARLBENNETT_JNI_TEST_JNITESTCLASSONE_CPP__
#define __INCLUDED_ORG_KARLBENNETT_JNI_TEST_JNITESTCLASSONE_CPP__

#include <org_karlbennett_jni_test_JniTestClassOne.h>

/*
 * Class:     org_karlbennett_jni_test_JniTestClassOne
 * Method:    nativeMethod
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_org_karlbennett_jni_test_JniTestClassOne_nativeMethod (JNIEnv *env, jclass jniTestClassOne) {

    return env->NewStringUTF("Test string from native JniTestClassOne.");
}

#endif /* __INCLUDED_ORG_KARLBENNETT_JNI_TEST_JNITESTCLASSONE_CPP__ */
