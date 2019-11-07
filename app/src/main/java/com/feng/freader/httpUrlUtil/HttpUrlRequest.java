package com.feng.freader.httpUrlUtil;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;

/**
 * @author Feng Zhaohao
 * Created on 2019/10/18
 */
public class HttpUrlRequest {
    private static final String TAG = "fzh";

    private Request mRequest;        // 用户的请求
    private Response mResponse;      // 返回用户的响应
    private Executor mThreadPool;    // 用户指定的线程池

    // 请求类型
    public enum METHOD_TYPE {
        GET, POST
    }
    
    private HttpUrlRequest() {}

    HttpUrlRequest(Request mRequest, Response mResponse, Executor mThreadPool) {
        this.mRequest = mRequest;
        this.mResponse = mResponse;
        this.mThreadPool = mThreadPool;
    }

    /**
     * 执行请求
     */
    public void doRequest() {
        if (!checkBeforeRequest()) {
            return;
        }

        // 在线程池中开启一个子线程进行网络请求
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
//                Log.d(TAG, "run: start");
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(mRequest.url);
                    connection = (HttpURLConnection) url.openConnection();
                    // 设置请求方式
                    switch (mRequest.methodType) {
                        case GET:
                            connection.setRequestMethod("GET");
                            break;
                        default:
                            break;
                    }
                    // 设置连接超时时间
                    connection.setConnectTimeout(mRequest.connectTimeout);
                    // 设置读取超时时间
                    connection.setReadTimeout(mRequest.readTimeout);
                    // 得到输入流
                    InputStream is = connection.getInputStream();
                    // 在主线程回调响应信息
                    showResponse(mResponse, Utils.readInputStream(is));
//                    Log.d(TAG, "run: end");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.d(TAG, "run 1, message = " + e.getMessage());
                    showError(mResponse, e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "run 2, message = " + e.getMessage());
                    showError(mResponse, e.getMessage());
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        });
    }
    
    private boolean checkBeforeRequest() {
        if (mRequest == null) {
            throw new RuntimeException("Request has not init!");
        }
        if (mResponse == null) {
            throw new RuntimeException("Response has not init!");
        }
        if (mRequest.url.equals("")) {
            showError(mResponse, "url 为空");
            return false;
        }

        return true;
    }

    /**
     * 在主线程显示错误信息
     */
    private static void showError(final Response mResponse, final String error) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mResponse.error(error);
            }
        });
    }

    /**
     * 在主线程显示响应信息
     */
    private static void showResponse(final Response mResponse, final String res) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mResponse.success(res);
            }
        });
    }
}
