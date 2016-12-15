package com.chico.photo.library.util;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.chico.photo.library.ImageActivity;
import com.chico.photo.library.entity.FoldEntity;
import com.chico.photo.library.entity.MediaEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created on 2016/12/15.
 * Author Chico Chen
 */
public class ImageUtil {
    public static Observable<List<FoldEntity>> getAllMediaFold(final Context context, final int selectType) {
        return Observable
                .create(new Observable.OnSubscribe<List<FoldEntity>>() {
                    @Override
                    public void call(Subscriber<? super List<FoldEntity>> subscriber) {
                        List<MediaEntity> images = new ArrayList<>();
                        Cursor cursor = null;
                        if (selectType == ImageActivity.TYPE_IMAGE) {
                            cursor = context.getContentResolver().query(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
                        } else {
                            cursor = context.getContentResolver().query(
                                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
                        }
                        while (cursor.moveToNext()) {
                            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                            images.add(new MediaEntity(path));
                        }
                        Collections.reverse(images);

                        //获取所有图片目录
                        Set<String> dir = new HashSet<>();
                        for (MediaEntity entity : images) {
                            String parent = new File(entity.getPath()).getParentFile().getName();
                            dir.add(parent);
                        }

                        List<FoldEntity> folders = new ArrayList<>();
                        FoldEntity folder = new FoldEntity();
                        folder.setName(selectType == ImageActivity.TYPE_IMAGE?"所有图片":"所有视频");
                        folder.setCover(images.get(0).getPath());
                        folder.setNumber(images.size());
                        folder.setMedias(images);
                        folders.add(folder);

                        //得到媒体文件目录集合
                        for (String name : dir) {
                            FoldEntity mediaFolder = new FoldEntity();
                            mediaFolder.setName(name);
                            folders.add(mediaFolder);
                        }

                        //设置媒体文件集合数据
                        for (FoldEntity f : folders) {
                            for (MediaEntity media : images) {
                                String parent = new File(media.getPath()).getParentFile().getName();
                                if (parent.equals(f.getName())) {
                                    f.setCover(media.getPath());
                                    f.setPath(new File(media.getPath()).getParentFile().getAbsolutePath());
                                    List<MediaEntity> arr = f.getMedias();
                                    if (arr == null) {
                                        arr = new ArrayList<>();
                                    }
                                    arr.add(media);
                                    f.setNumber(arr.size());
                                    f.setMedias(arr);
                                }
                            }
                        }
                        cursor.close();
                        subscriber.onNext(folders);
                    }
                })
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread());
    }

}
