package com.chico.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ListAdapter(getDatas());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new ListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0:
                        create();
                        break;
                    case 1:
                        from();
                        break;
                    case 2:
                        just();
                        break;
                    case 3:
                        defer();
                        break;
                    case 4:
                        range();
                        break;
                    case 5:
                        timer();
                        break;
                    case 6:
                        interval();
                        break;
                    default:
                        break;
                }
            }
        });


    }

    private List<String> getDatas() {
        List<String> array = new ArrayList<>();
        array.add("create");
        array.add("from");
        array.add("just");
        array.add("defer");
        array.add("range");
        array.add("timer");
        array.add("interval");
        return array;
    }

    //使用一个函数从头创建一个Observable
    private void create() {
        Observable
                .create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext("ChicoChen");
                        subscriber.onCompleted();
                    }
                })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onStart() {
                        Log.e("====>", "onStart");
                    }

                    @Override
                    public void onCompleted() {
                        Log.e("====>", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("====>", "onError");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.e("====>", "onNext:" + s);
                    }
                });
    }

    private void from() {
        List<String> names = new ArrayList<>();
        names.add("ChicoChen");
        names.add("FeiLi");
        names.add("SheYa");
        Observable.from(names)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.e("====>", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("====>", e.getMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        Log.e("====>", "name:" + s);
                    }
                });
    }

    private void just(){
        Observable.just(1,2,3,4,5,"a","b")
                .subscribe(new Subscriber<Serializable>() {
                    @Override
                    public void onCompleted() {
                        Log.e("====>", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("====>", e.getMessage());
                    }

                    @Override
                    public void onNext(Serializable serializable) {
                        Log.e("====>", "Item:" + serializable.toString());
                    }
                });
    }

    int i;
    //只有当订阅者订阅才创建Observable；为每个订阅创建一个新的Observable
    private void defer(){
        i=10;
        Observable justObservable = Observable.just(i);
        i=12;
        Observable deferObservable = Observable.defer(new Func0<Observable<Integer>>() {
            @Override
            public Observable<Integer> call() {
                return Observable.just(i);
            }
        });
        i=15;
        justObservable.subscribe(new Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {
                Log.e("====>", "just result:" + o.toString());
            }
        });
        deferObservable.subscribe(new Subscriber() {
            @Override
            public void onCompleted() {

            }
            @Override
            public void onError(Throwable e) {

            }
            @Override
            public void onNext(Object o) {
                Log.e("====>", "defer result:" + o.toString());
            }
        });
    }

    private void range(){
        Observable.range(1,20)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.e("====>", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("====>", e.getMessage());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e("====>", "Item:"+integer);
                    }
                });
    }

    private void timer(){
        Observable.timer(2, TimeUnit.SECONDS)
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        Log.e("====>", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("====>", e.getMessage());
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.e("====>", "Item:"+aLong);
                    }
                });
    }

    private void interval(){
        Observable.interval(2,2,TimeUnit.SECONDS)
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        Log.e("====>", "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("====>", e.getMessage());
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.e("====>", "Item:"+aLong);
                    }
                });

        Observable.empty();
    }



}
