package com.feng.freader.http;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 对 okhttp3 的简单封装
 *
 * @author Feng Zhaohao
 * Created on 2019/11/17
 */
public class OkhttpUtil {

    //创建OkHttpClient
    private static OkHttpClient okHttpClient = new OkHttpClient();

    public static void getRequest(String url, final OkhttpCall okhttpCall) {
        //创建Request
        Request request = new Request.Builder()
                .url(url)
                .build();
        //创建Call
        Call call = okHttpClient.newCall(request);
        //调用Call的enqueue方法，该方法的回调是在子线程
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                doInUiThread(new Runnable() {
                    @Override
                    public void run() {
                        okhttpCall.onFailure(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    final String data = response.body().string();
                    doInUiThread(new Runnable() {
                        @Override
                        public void run() {
                            okhttpCall.onResponse(data);
                        }
                    });
                } catch (final IOException e) {
                    doInUiThread(new Runnable() {
                        @Override
                        public void run() {
                            okhttpCall.onFailure(e.getMessage());
                        }
                    });
                }
            }
        });
    }

    private static void doInUiThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }
}
