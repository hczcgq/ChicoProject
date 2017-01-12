package com.chico.android.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created on 2017/1/11.
 * Author Chico Chen
 */

public class AidlService extends Service{

    private String TAG = "AidlService";

    private MyAidl.Stub binder=new MyAidl.Stub(){

        @Override
        public String sayHello(String name) throws RemoteException {
            return "Hello, " + name;
        }

        @Override
        public int getPid() throws RemoteException {
            return android.os.Process.myPid();
        }
    };

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        return binder;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }
}
