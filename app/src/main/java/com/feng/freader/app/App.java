package com.feng.freader.app;

import android.app.Application;

import com.feng.freader.util.CrashHandler;

/**
 * @author Feng Zhaohao
 * Created on 2019/10/28
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 为应用设置异常处理
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init();
    }
}
