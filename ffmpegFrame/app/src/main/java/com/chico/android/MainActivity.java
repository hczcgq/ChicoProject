package com.chico.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FFmpeg ffmpeg = FFmpeg.getInstance(this);
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler(){
                @Override
                public void onStart() {
                    Log.e("=====>","onStart");
                }

                @Override
                public void onFailure() {
                    Log.e("=====>","onFailure");
                }

                @Override
                public void onSuccess() {
                    Log.e("=====>","onSuccess");
                }

                @Override
                public void onFinish() {
                    Log.e("=====>","onFinish");
                }
            });
        } catch (FFmpegNotSupportedException e) {
            e.printStackTrace();
        }

        try {

            String a="/storage/emulated/0/chico/image/camera/Chico_20161215_220504.mp4";
            String b="/storage/emulated/0/chico/image/camera/video111.mp4";

//          String cmd="-i /storage/emulated/0/chico/image/camera/Chico_20161215_220504.mp4 -vcodec mpeg4 /storage/emulated/0/chico/image/camera/test.mp4";

            //尺寸变换
            String cmd="-i /storage/emulated/0/chico/image/camera/Chico_20161215_220504.mp4 -s 480x800 /storage/emulated/0/chico/image/camera/video111111.mp4";

            String[] command = cmd.split(" ");
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {}

                @Override
                public void onProgress(String message) {
                    Log.e("onProgress=====>",message);
                }

                @Override
                public void onFailure(String message) {
                    Log.e("onFailure=====>",message);
                }

                @Override
                public void onSuccess(String message) {
                    Log.e("onSuccess=====>",message);
                }

                @Override
                public void onFinish() {}
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // Handle if FFmpeg is already running
        }
    }
}
