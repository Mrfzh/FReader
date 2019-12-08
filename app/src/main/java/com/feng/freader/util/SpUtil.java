package com.feng.freader.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.feng.freader.app.App;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/8
 */
public class SpUtil {
    private static final String NAME = "freader_data";
    private static final String KEY_TEXT_SIZE = "key_text_size";
    private static final String KEY_ROW_SPACE = "key_row_space";
    private static final float DEFAULT_TEXT_SIZE = 56f;
    private static final float DEFAULT_ROW_SPACE = 24f;

    public static void saveTextSize(float textSize) {
        SharedPreferences.Editor editor = App.getContext()
                .getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
        editor.putFloat(KEY_TEXT_SIZE, textSize);
        editor.apply();
    }

    public static void saveRowSpace(float rowSpace) {
        SharedPreferences.Editor editor = App.getContext()
                .getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
        editor.putFloat(KEY_ROW_SPACE, rowSpace);
        editor.apply();
    }

    public static float getTextSize() {
        SharedPreferences sp = App.getContext()
                .getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getFloat(KEY_TEXT_SIZE, DEFAULT_TEXT_SIZE);
    }

    public static float getRowSpace() {
        SharedPreferences sp = App.getContext()
                .getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getFloat(KEY_ROW_SPACE, DEFAULT_ROW_SPACE);
    }
}
