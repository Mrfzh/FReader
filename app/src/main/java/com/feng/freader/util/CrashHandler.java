package com.feng.freader.util;

import android.os.Process;
import android.util.Log;

/**
 * @author Feng Zhaohao
 * Created on 2019/10/28
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "fzh";

    private static CrashHandler sInstance = new CrashHandler();

    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;

    private CrashHandler() {

    }

    public static CrashHandler getInstance() {
        return sInstance;
    }

    public void init() {
        // 保存之前的默认异常处理器
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 将自己设置为新的默认异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        // 这里可以上传异常信息到服务器
        // 为了方便起见，这里只是简单地打印出来
        Log.d(TAG, "uncaughtException: " + e.getMessage());

        // 如果系统提供了默认的异常处理器，则交给系统结束程序，否则由自己来结束
        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(t, e);
        } else {
            Process.killProcess(Process.myPid());
        }
    }
}
