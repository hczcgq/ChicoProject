package com.chico.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.chico.photo.library.ImageActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button image=(Button) findViewById(R.id.btn_image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageActivity.start(MainActivity.this,ImageActivity.TYPE_IMAGE,9,ImageActivity.MODE_MULTIPLE,true,true,false);
            }
        });

        Button video=(Button) findViewById(R.id.btn_video);
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageActivity.start(MainActivity.this,ImageActivity.TYPE_VIDEO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==ImageActivity.REQUEST_IMAGE){
                List<String> image=data.getStringArrayListExtra(ImageActivity.REQUEST_OUTPUT);
                if(image!=null&&image.size()>0){
                    for (String name:image) {
                        Log.e("=======>","图片地址："+name);
                    }
                }
            }else if(requestCode==ImageActivity.REQUEST_VIDEO){
                List<String> image=data.getStringArrayListExtra(ImageActivity.REQUEST_OUTPUT);
                if(image!=null&&image.size()>0){
                    for (String name:image) {
                        Log.e("=======>","视频地址："+name);
                    }
                }
            }
        }
    }
}
