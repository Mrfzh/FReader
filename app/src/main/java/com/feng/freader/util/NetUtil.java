package com.feng.freader.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author Feng Zhaohao
 * Created on 2020/1/2
 */
public class NetUtil {

    /**
     * 判断是否有网络
     *
     * @return
     */
    public static boolean hasInternet(Context context) {
        //获取一个用于管理网络连接的系统服务类
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取NetworkInfo实例
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //判断网络连接状态，有网则返回真
        if (networkInfo != null && networkInfo.isAvailable()) {
            return true;
        } else {
            return false;
        }
    }


}
