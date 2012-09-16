/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

#ifndef _Included_com_example_hellojni_HelloJni
#define _Included_com_example_hellojni_HelloJni

#include <string.h>
#include <jni.h>
#include <android/log.h>

#ifdef __cplusplus
extern "C"
{
#endif

void testCccc()
{
}

char* ATC = "inC";

JNIEXPORT void JNICALL Java_com_example_hellojni_TestClass_accessField(JNIEnv * env, jobject obj)
{
	jfieldID fid;
	jstring jstr;
	const char *str;
	jclass cls = (*env)->GetObjectClass(env, obj);
	__android_log_print(ANDROID_LOG_INFO, ATC, "In c:\n");

	fid = (*env)->GetFieldID(env, cls, "s", "Ljava/lang/String;");
	if (fid == NULL)
	{
		return; // failed to find the field
	}

	jstr = (*env)->GetObjectField(env, obj, fid);
	str = (*env)->GetStringUTFChars(env, jstr, NULL);
	if (str == NULL)
	{
		return; // out of memory
	}
	__android_log_print(ANDROID_LOG_INFO, ATC, "c.s=\"%s\"", str);
	(*env)->ReleaseStringUTFChars(env, jstr, str);

	jstr = (*env)->NewStringUTF(env, "123钱");
	if (jstr == NULL)
	{
		return; // out of memory
	}
	(*env)->SetObjectField(env, obj, fid, jstr);

	fid = (*env)->GetStaticFieldID(env, cls, "a", "I");
	jint si = (*env)->GetStaticIntField(env, cls, fid);
	__android_log_print(ANDROID_LOG_INFO, ATC, "ori static a field is: %d", si);
	(*env)->SetStaticIntField(env, cls, fid, 924);

	jmethodID mid = (*env)->GetMethodID(env, cls, "priMethod", "()V");
	(*env)->CallVoidMethod(env, obj, mid);

	/**
	 * Runnable.run方法：
	 jobject thd = ...; // a java.lang.Thread instance
	 jmethodID mid;
	 jclass runnableIntf =
	 (*env)->FindClass(env, "java/lang/Runnable");
	 if (runnableIntf == NULL) {
	 ... // error handling
	 }
	 mid = (*env)->GetMethodID(env, runnableIntf, "run", "()V");
	 if (mid == NULL) {
	 ... /* error handling //
	 }
	 (*env)->CallVoidMethod(env, thd, mid);
	 ... /* check for possible exceptions
	 *
	 */

	/**
	 * jmethodID mid =
	 (*env)->GetStaticMethodID(env, cls, "callback", "()V");
	 if (mid == NULL) {
	 return;  // method not found
	 }
	 printf("In C\n");
	 (*env)->CallStaticVoidMethod(env, cls, mid);
	 *
	 */
}

/* This is a trivial JNI example where we use a native method
 * to return a new VM String. See the corresponding Java source
 * file located at:
 *
 *   apps/samples/hello-jni/project/src/com/example/hellojni/HelloJni.java
 */
jstring Java_com_example_hellojni_HelloJni_stringFromJNI(JNIEnv* env, jobject thiz)
{

	return (*env)->NewStringUTF(env, "Hello from JNI Kai5!");
}

JNIEXPORT jstring JNICALL Java_com_example_hellojni_HelloJni_stringFromJNIWithParam(JNIEnv * env, jobject thiz, jstring src)
{
	char uu[64];
	const char* srcStr = (*env)->GetStringUTFChars(env, src, 0);
	strcpy(uu, srcStr);

	char* destStr = strcat(uu, " cat C.");
	__android_log_print(ANDROID_LOG_ERROR, "NDK_Kai", destStr);
	return (*env)->NewStringUTF(env, destStr);

}

JNIEXPORT jint JNICALL Java_com_example_hellojni_HelloJni_sumArray(JNIEnv * env, jobject thiz, jintArray arr, jint len)
{
	jint buf[5], i, sum = 0;
	(*env)->GetIntArrayRegion(env, arr, 0, 5, buf);
	for (i = 0; i < 5; ++i)
	{
		sum += buf[i];
	}
	return sum;
}

JNIEXPORT jobjectArray JNICALL Java_com_example_hellojni_HelloJni_initInt2DArray(JNIEnv * env, jobject thiz, int size)
{
	jobjectArray result;
	int i;
	jclass intArrCls = (*env)->FindClass(env, "[I");
	if (intArrCls == NULL)
	{
		return NULL; /* exception thrown */
	}
	result = (*env)->NewObjectArray(env, size, intArrCls, NULL);
	if (result == NULL)
	{
		return NULL; /* out of memory error thrown */
	}
	for (i = 0; i < size; i++)
	{
		jint tmp[256]; /* make sure it is large enough! */
		int j;
		jintArray iarr = (*env)->NewIntArray(env, size);
		if (iarr == NULL)
		{
			return NULL; /* out of memory error thrown */
		}
		for (j = 0; j < size; j++)
		{
			tmp[j] = i + j;
		}
		(*env)->SetIntArrayRegion(env, iarr, 0, size, tmp);
		(*env)->SetObjectArrayElement(env, result, i, iarr);
		(*env)->DeleteLocalRef(env, iarr);
	}
	return result;

}

#ifdef __cplusplus
}
#endif

#endif
