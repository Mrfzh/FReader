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
    private static final String KEY_TEXT_SIZE = "key_text_size";    // 文字大小
    private static final String KEY_ROW_SPACE = "key_row_space";    // 行距
    private static final String KEY_THEME = "key_theme";            // 阅读主题
    private static final String KEY_BRIGHTNESS = "key_brightness";  // 亮度
    private static final String KEY_IS_NIGHT_MODE= "key_is_night_mode";  // 是否为夜间模式
    private static final String KEY_TURN_TYPE = "key_turn_type";  // 翻页模式
    private static final float DEFAULT_TEXT_SIZE = 56f;
    private static final float DEFAULT_ROW_SPACE = 24f;
    private static final int DEFAULT_THEME = 0;
    private static final float DEFAULT_BRIGHTNESS = -1f;
    private static final boolean DEFAULT_IS_NIGHT_MODE = false;
    private static final int DEFAULT_TURN_TYPE = 0;

    public static void saveTextSize(float textSize) {
        SharedPreferences.Editor editor = App.getContext()
                .getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
        editor.putFloat(KEY_TEXT_SIZE, textSize);
        editor.apply();
    }

    public static float getTextSize() {
        SharedPreferences sp = App.getContext()
                .getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getFloat(KEY_TEXT_SIZE, DEFAULT_TEXT_SIZE);
    }

    public static void saveRowSpace(float rowSpace) {
        SharedPreferences.Editor editor = App.getContext()
                .getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
        editor.putFloat(KEY_ROW_SPACE, rowSpace);
        editor.apply();
    }

    public static float getRowSpace() {
        SharedPreferences sp = App.getContext()
                .getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getFloat(KEY_ROW_SPACE, DEFAULT_ROW_SPACE);
    }

    public static void saveTheme(int theme) {
        SharedPreferences.Editor editor = App.getContext()
                .getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(KEY_THEME, theme);
        editor.apply();
    }

    public static int getTheme() {
        SharedPreferences sp = App.getContext()
                .getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getInt(KEY_THEME, DEFAULT_THEME);
    }

    public static void saveBrightness(float brightness) {
        SharedPreferences.Editor editor = App.getContext()
                .getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
        editor.putFloat(KEY_BRIGHTNESS, brightness);
        editor.apply();
    }

    public static float getBrightness() {
        SharedPreferences sp = App.getContext()
                .getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getFloat(KEY_BRIGHTNESS, DEFAULT_BRIGHTNESS);
    }

    public static void saveIsNightMode(boolean isNightMode) {
        SharedPreferences.Editor editor = App.getContext()
                .getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(KEY_IS_NIGHT_MODE, isNightMode);
        editor.apply();
    }

    public static boolean getIsNightMode() {
        SharedPreferences sp = App.getContext()
                .getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_IS_NIGHT_MODE, DEFAULT_IS_NIGHT_MODE);
    }

    public static void saveTurnType(int turnType) {
        SharedPreferences.Editor editor = App.getContext()
                .getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(KEY_TURN_TYPE, turnType);
        editor.apply();
    }

    public static int getTurnType() {
        SharedPreferences sp = App.getContext()
                .getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getInt(KEY_TURN_TYPE, DEFAULT_TURN_TYPE);
    }
}
