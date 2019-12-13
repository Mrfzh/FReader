package com.feng.freader.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.feng.freader.app.App;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/10
 */
public class ScreenUtil {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth() {
        DisplayMetrics dm = App.getContext().getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight() {
        DisplayMetrics dm = App.getContext().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 获取系统屏幕亮度
     */
    public static int getSystemBrightness() {
        int systemBrightness = 0;
        try {
            systemBrightness = Settings.System.getInt(App.getContext().getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return systemBrightness;
    }

    /**
     * 获取屏幕最大亮度
     */
    public static int getBrightnessMax() {
        Resources system = Resources.getSystem();
        int resId = system.getIdentifier("config_screenBrightnessSettingMaximum",
                "integer", "android");
        if (resId != 0) {
            return system.getInteger(resId);
        }
        return 255;
    }

    /**
     * 设置窗口亮度
     */
    public static void setWindowBrightness(Activity activity, float percent) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = percent;
        window.setAttributes(lp);
    }

}
