package com.feng.freader.util;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/11
 */
public class FileUtil {

    /**
     * 将 Uri 转换为 file path
     */
    public static String uri2FilePath(Activity activity, Uri uri) {
        String filePath;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualimagecursor = activity.managedQuery(uri, proj, null, null, null);
        if (actualimagecursor == null) {
            filePath = uri.getPath();
        } else {
            int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            filePath = actualimagecursor.getString(actual_image_column_index);
        }
        
        return filePath;
    }

    /**
     * 获取文件长度，以 M 为单位
     */
    public static double getFileSize(File file){
        long len = file.length();
        return (double) len / Math.pow(2, 20);
    }

    /**
     * 通过图片的 filePath 加载本地图片
     */
    public static Bitmap loadLocalPicture(String filePath) {
        FileInputStream fis = null;
        try {
            File file = new File(filePath);
            fis = new FileInputStream(file);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
