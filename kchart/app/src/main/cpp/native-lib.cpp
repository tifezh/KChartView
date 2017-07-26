#include <jni.h>
#include <string>
//#include <sstream>

//extern "C"
//JNIEXPORT jstring JNICALL
//Java_com_silladus_ndkproject_JNIClass_stringFromJNI(
//        JNIEnv* env,
//        jobject /* this */) {
//    std::string hello = "Hello from C++";
//    return env->NewStringUTF(hello.c_str());
//}

//extern "C"
//JNIEXPORT void JNICALL
//Java_com_silladus_ndkproject_JNIClass_calculate(
//        JNIEnv* env,
//        jobject /* this */,
//        jobject bean) {
//    jclass cls = env->GetObjectClass(bean);
//    if (cls == NULL) {
//        return;
//    }
//    jfieldID year = env->GetFieldID(cls, "year", "I");
//    jfieldID name = env->GetFieldID(cls, "name", "Ljava/lang/String;");
//    env->SetObjectField(bean, name, env->NewStringUTF("xxxx"));
//    env->SetIntField(bean, year, 21);
//}

//extern "C"
//JNIEXPORT void JNICALL
//Java_com_silladus_stock_JNIClass_calculates(
//        JNIEnv* env,
//        jobject /* this */,
//        jobjectArray array) {
//    jclass cls_arraylist = env->GetObjectClass(array);
//    if (cls_arraylist == NULL) {
//        return;
//    }
//    jmethodID arraylist_get = env->GetMethodID(cls_arraylist, "get", "(I)Ljava/lang/Object;");
//    jmethodID arraylist_size = env->GetMethodID(cls_arraylist,"size","()I");
//    jint len = env->CallIntMethod(array, arraylist_size);
//    for (int i = 0; i < len; ++i) {
//        jobject bean = env->CallObjectMethod(array, arraylist_get, i);
//        jclass cls_bean = env->GetObjectClass(bean);
//        jfieldID int_year = env->GetFieldID(cls_bean, "year", "I");
//        jfieldID str_name = env->GetFieldID(cls_bean, "name", "Ljava/lang/String;");
//        jfieldID float_high = env->GetFieldID(cls_bean, "high", "F");
//        jfieldID double_hairs = env->GetFieldID(cls_bean, "hairs", "D");
//        jfieldID long_money = env->GetFieldID(cls_bean, "money", "J");
//        jfieldID bool_man = env->GetFieldID(cls_bean, "man", "Z");
//        std::stringstream ss;
//        std::string str;
//        ss<<i;
//        ss>>str;
//        env->SetObjectField(bean, str_name, env->NewStringUTF(str.c_str()));
//        jint y = 21 + i;
//        env->SetIntField(bean, int_year, y);
//        printf("year:%d; ", y);
//        printf("name:%s; ", str.c_str());
//        jfloat f = 172.3 + i;
//        jdouble d = 10000.5 + i;
//        jlong l = 200000 + i;
//        jboolean b;
//        if ((i & 1) == 0) {
//            b = true;
//        } else {
//            b = false;
//        }
//        env->SetFloatField(bean, float_high, f);
//        env->SetDoubleField(bean, double_hairs, d);
//        env->SetLongField(bean, long_money, l);
//        env->SetBooleanField(bean, bool_man, b);
//    }
//}

extern "C"
JNIEXPORT void JNICALL
Java_com_silladus_stock_DataHelper_jcalculateVOL(JNIEnv* env, jobject /* this */, jobjectArray array) {
    jclass cls_arraylist = env->GetObjectClass(array);
    if (cls_arraylist == NULL) {
        return;
    }
    jmethodID arraylist_get = env->GetMethodID(cls_arraylist, "get", "(I)Ljava/lang/Object;");
    jmethodID arraylist_size = env->GetMethodID(cls_arraylist,"size","()I");
    jint len = env->CallIntMethod(array, arraylist_size);
    jfloat ma5 = 0;
    jfloat ma10 = 0;
    for (int i = 0; i < len; ++i) {
        jobject bean = env->CallObjectMethod(array, arraylist_get, i);
        jclass cls_bean = env->GetObjectClass(bean);
        jmethodID vol_get = env->GetMethodID(cls_bean, "getVOL", "()F");
        jfloat float_vol = env->CallFloatMethod(bean, vol_get);
        ma5 += float_vol;
        ma10 += float_vol;

        jfieldID float_vma5 = env->GetFieldID(cls_bean, "vMA5", "F");
        if (i >= 5) {
            ma5 -= env->CallFloatMethod(env->CallObjectMethod(array, arraylist_get, i - 5), vol_get);
            env->SetFloatField(bean, float_vma5, ma5 / 5.0f);
        } else {
            env->SetFloatField(bean, float_vma5, ma5 / (i + 1.0f));
        }
        jfieldID float_vma10 = env->GetFieldID(cls_bean, "vMA10", "F");
        if (i >= 10) {
            ma10 -= env->CallFloatMethod(env->CallObjectMethod(array, arraylist_get, i - 10), vol_get);
            env->SetFloatField(bean, float_vma10, ma10 / 10.0f);
        } else {
            env->SetFloatField(bean, float_vma10, ma10 / (i + 1.0f));
        }
        //  释放类 、对象(这里不释放的话超过128个就会死（内存栈溢出？）)
        env->DeleteLocalRef(cls_bean);
        env->DeleteLocalRef(bean);
    }
    env->DeleteLocalRef(cls_arraylist);
}