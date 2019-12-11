package com.feng.freader.util;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

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
}
