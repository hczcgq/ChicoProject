package com.chico.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chico.android.Thread.ThreadActivity;
import com.chico.android.aidl.AIDLClient;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ListItemAdapter mAdapter;
    private List<IntentEntity> mDatas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_recycler_view);
        mDatas = getDatas();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ListItemAdapter(this, mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new ListItemAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, mDatas.get(position).getClassName());
                startActivity(intent);
            }
        });

    }

    private List<IntentEntity> getDatas() {
        List<IntentEntity> array = new ArrayList<>();
        array.add(new IntentEntity("AIL", AIDLClient.class));
        array.add(new IntentEntity("线程池", ThreadActivity.class));
        return array;
    }
}
