package com.chico.photo.library.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by dee on 15/11/20.
 */
public class FileUtils {
    public static final String POSTFIX_IMAGE = ".JPEG";
    public static final String POSTFIX_VIDEO = ".mp4";
    public static final String APP_NAME = "Chico";
    public static final String CAMERA_PATH = Environment.getExternalStorageDirectory() + "/chico/image/camera/";
    public static final String CROP_PATH = Environment.getExternalStorageDirectory() + "/chico/image/crop/";
    public static final String VIDEO_PATH = Environment.getExternalStorageDirectory() + "/chico/video/";

    public static File createCameraFile(Context context) {
        return createMediaFile(context,CAMERA_PATH,POSTFIX_IMAGE);
    }

    public static File createVideoFile(Context context) {
        return createMediaFile(context,VIDEO_PATH,POSTFIX_VIDEO);
    }

    public static File createCropFile(Context context) {
        return createMediaFile(context,CROP_PATH,POSTFIX_IMAGE);
    }

    private static File createMediaFile(Context context, String parentPath,String postfix){
        File folderDir=new File(parentPath);
        if(!folderDir.exists()){
            folderDir.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String fileName = APP_NAME + "_" + timeStamp + "";
        File tmpFile = new File(folderDir, fileName + postfix);
        return tmpFile;
    }
}
