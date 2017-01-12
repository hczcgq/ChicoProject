// MyAidl.aidl
package com.chico.android.aidl;

// Declare any non-default types here with import statements

interface MyAidl {
    // say hello to name
   String sayHello(String name);
   // 获取进程ID
   int getPid();
}
