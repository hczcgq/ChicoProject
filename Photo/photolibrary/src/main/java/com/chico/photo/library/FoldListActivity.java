package com.chico.photo.library;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chico.photo.library.adapter.FolderAdapter;
import com.chico.photo.library.entity.FoldEntity;
import com.chico.photo.library.entity.MediaEntity;
import com.chico.photo.library.util.ItemDivider;

import java.io.Serializable;
import java.util.List;

/**
 * Created on 2016/12/15.
 * Author Chico Chen
 */
public class FoldListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TextView cancelText;

    private FolderAdapter mAdapter;
    private List<FoldEntity> array;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fold);
        getIntentData();
        initView();
        registerListener();
    }

    /**
     * 初始化事件
     */
    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_view);
        cancelText = (TextView) findViewById(R.id.tv_cancle);

        mRecyclerView.addItemDecoration(new ItemDivider(this));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new FolderAdapter(this);
        mAdapter.setDatas(array);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 注册控件监听事件
     */
    private void registerListener() {
        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK,null);
                finish();
            }
        });

        mAdapter.setOnItemClickListener(new FolderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String folderName, List<MediaEntity> images) {
                Intent intent = new Intent();
                intent.putExtra("array", (Serializable) images);
                intent.putExtra("name",folderName);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void getIntentData() {
        array = (List<FoldEntity>) getIntent().getExtras().getSerializable("array");
    }

    public static void startFold(Activity activity, List<FoldEntity> foldArray) {
        Intent intent = new Intent(activity, FoldListActivity.class);
        intent.putExtra("array", (Serializable) foldArray);
        activity.startActivityForResult(intent, ImageActivity.REQUEST_FOLD);
    }
}
