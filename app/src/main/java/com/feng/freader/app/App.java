package com.feng.freader.app;

import android.app.Application;
import android.content.Context;

import com.feng.freader.util.CrashHandler;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.update.BmobUpdateAgent;

/**
 * @author Feng Zhaohao
 * Created on 2019/10/28
 */
public class App extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        // 为应用设置异常处理
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init();

        context = getApplicationContext();

        Bmob.initialize(this, "ce63bdbbd4197409b82920b0835a42eb");
        BmobUpdateAgent.setUpdateCheckConfig(false);
    }

    public static Context getContext() {
        return context;
    }

}
