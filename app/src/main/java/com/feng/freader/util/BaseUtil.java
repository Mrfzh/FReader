package com.feng.freader.util;

import android.content.Context;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/10
 */
public class BaseUtil {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}
