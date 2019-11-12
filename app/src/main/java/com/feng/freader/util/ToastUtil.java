package com.feng.freader.util;

import android.content.Context;
import android.widget.Toast;

/**
 * @author Feng Zhaohao
 * Created on 2019/10/19
 */
public class ToastUtil {

    private static final String TAG = "fzh";

    private static Toast toast = null;


    public static void showToast(Context context, String text) {
//        // 多次点击只会显示最新一次的Toast
//        if (toast == null) {
//            toast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
//            toast.setText(text);    // 这样写是为了解决小米手机显示app名的问题
//            toast.show();
//        } else {
//            // 问题：连续多次点击时，后面的 Toast 就显示不了（小米 9.0）
//            toast.setText(text);
//            toast.setDuration(Toast.LENGTH_SHORT);
//            toast.show();
//        }
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

}
