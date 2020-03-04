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
    private static volatile OkHttpClient okHttpClient;

    private static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            synchronized (OkhttpUtil.class) {
                if (okHttpClient == null) {
                    okHttpClient = new OkHttpClient();
                }
            }
        }
        return okHttpClient;
    }

    public static void getRequest(final OkhttpBuilder okhttpBuilder) {
        //创建Request
        Request request = new Request.Builder()
                .url(okhttpBuilder.url)
                .build();
        //创建Call
        Call call = getOkHttpClient().newCall(request);
        //调用Call的enqueue方法，该方法的回调是在子线程
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                doInUiThread(new Runnable() {
                    @Override
                    public void run() {
                        okhttpBuilder.okhttpCall.onFailure(e.getMessage());
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
                            okhttpBuilder.okhttpCall.onResponse(data);
                        }
                    });
                } catch (final IOException e) {
                    doInUiThread(new Runnable() {
                        @Override
                        public void run() {
                            okhttpBuilder.okhttpCall.onFailure(e.getMessage());
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
