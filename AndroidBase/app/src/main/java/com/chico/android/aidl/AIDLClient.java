package com.chico.android.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.chico.android.R;

/**
 * Created on 2017/1/11.
 * Author Chico Chen
 */

public class AIDLClient extends AppCompatActivity{
    private String TAG = "MainActivity";
    private TextView currentTv, serverTv;
    private Button sayBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);

        initService();
        initViews();
    }

    private void initViews() {
        currentTv = (TextView) findViewById(R.id.tv_current_pid);
        currentTv.setText(android.os.Process.myPid() + "");
        serverTv = (TextView) findViewById(R.id.tv_server_pid);
        sayBtn = (Button) findViewById(R.id.btn_say);
        sayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String result = myAidl.sayHello("Victor");
                    int pid = myAidl.getPid();
                    serverTv.setText(result + "服务端PID为:" + pid);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private MyAidl myAidl;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected");
            myAidl = MyAidl.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected");
        }
    };

    private void initService() {
        //传入Server端的service的名字供本进程绑定
        Intent intent = new Intent(this,AidlService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }
}
