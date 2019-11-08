package com.feng.freader.util;

import android.app.Activity;
import android.view.WindowManager;
import android.widget.EditText;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/8
 */
public class EditTextUtil {

    /**
     * EditText 获取焦点并显示软键盘
     */
    public static void focusAndShowSoftKeyboard(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        //显示软键盘
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

}
