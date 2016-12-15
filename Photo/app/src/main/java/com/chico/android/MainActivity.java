package com.chico.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.chico.photo.library.ImageActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button image=(Button) findViewById(R.id.btn_image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageActivity.start(MainActivity.this,ImageActivity.TYPE_IMAGE,1,ImageActivity.MODE_SINGLE,true,false,true);
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
}
