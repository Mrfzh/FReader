package com.feng.freader.httpUrlUtil;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Feng Zhaohao
 * Created on 2019/10/24
 */
public class HttpUrlRequestBuilder {
    // 默认的线程池为 FixedThreadPool，在运行过程中有 n 个常驻线程，这些线程可重复使用，避免反复地创建线程
    private static final Executor DEFAULT_EXECUTOR = Executors.newFixedThreadPool(3);

    private Request mRequest;        // 用户的请求
    private Response mResponse;      // 返回用户的响应
    private Executor mThreadPool = DEFAULT_EXECUTOR;    // 用户指定的线程池

    private HttpUrlRequestBuilder() {}

    public static HttpUrlRequestBuilder getInstance() {
        return new HttpUrlRequestBuilder();
    }

    public HttpUrlRequestBuilder setRequest(Request request) {
        mRequest = request;
        return this;
    }

    public HttpUrlRequestBuilder setResponse(Response response) {
        mResponse = response;
        return this;
    }

    public HttpUrlRequestBuilder setThreadPool(Executor threadPool) {
        mThreadPool = threadPool;
        return this;
    }

    public HttpUrlRequest build() {
        return new HttpUrlRequest(mRequest, mResponse, mThreadPool);
    }
}
