package com.feng.freader.util;

import android.app.Activity;
import android.os.Build;
import android.view.View;

/**
 * @author Feng Zhaohao
 * Created on 2019/10/28
 */
public class StatusBarUtil {

    /**
     * 设置浅色状态栏（黑色字体和图标）
     *
     * @param activity
     */
    public static void setLightColorStatusBar(Activity activity) {
        // 6.0 以上版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = activity.getWindow().getDecorView();
            decor.setSystemUiVisibility(decor.getSystemUiVisibility()
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * 设置深色状态栏（白色字体和图标）
     *
     * @param activity
     */
    public static void setDarkColorStatusBar(Activity activity) {
        // 6.0 以上版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = activity.getWindow().getDecorView();
            decor.setSystemUiVisibility(decor.getSystemUiVisibility()
                    & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

}
