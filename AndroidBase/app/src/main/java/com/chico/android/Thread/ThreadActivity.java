package com.chico.android.Thread;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.chico.android.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2017/1/11.
 * Author Chico Chen
 */

public class ThreadActivity extends AppCompatActivity {

    private ThreadPoolExecutor executor;
    private ExecutorService executorService;
    private ScheduledExecutorService scheduledExecutorService;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

        executor = new ThreadPoolExecutor(3, 5,
                1, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(128));

//        executorService = Executors.newFixedThreadPool(3);
//        executorService= Executors.newSingleThreadExecutor();
        executorService = Executors.newCachedThreadPool();
        scheduledExecutorService = Executors.newScheduledThreadPool(3);

        Button start = (Button) findViewById(R.id.btn_start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                threadPool();
//                fixedAndSinglethreadPool();
//                cachedThreadPool();
//                secheduleThreadPool();
            }
        });

        Button stop = (Button) findViewById(R.id.btn_stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executor.shutdownNow();
            }
        });
    }

    private void threadPool() {
        for (int i = 0; i < 30; i++) {
            final int finalI = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(2000);
                    Log.e("google_lenve_fb", "run: " + finalI);
                }
            };
            executor.execute(runnable);
        }
    }


    private void fixedAndSinglethreadPool() {
        for (int i = 0; i < 30; i++) {
            final int finalI = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(2000);
                    Log.e("google_lenve_fb", "run: " + finalI);
                }
            };
            executor.execute(runnable);
        }
    }

    private void cachedThreadPool() {
        for (int i = 0; i < 30; i++) {
            final int finalI = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(2000);
                    Log.e("google_lenve_fb", "run: " + Thread.currentThread().getName() + "----" + finalI);
                }
            };
            executorService.execute(runnable);
            SystemClock.sleep(1000);
        }
    }

    private void secheduleThreadPool(){
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                Log.e("google_lenve_fb", "run: ----");
            }
        };
        scheduledExecutorService.schedule(runnable, 1, TimeUnit.SECONDS);
    }
}
