package com.feng.freader.http;

/**
 * @author Feng Zhaohao
 * Created on 2020/3/4
 */
public class OkhttpBuilder {
    String url;
    OkhttpCall okhttpCall;
    int connectTimeout;
    int readTimeout;

    private OkhttpBuilder() {}

    public static class Builder {
        private String url;
        private int connectTimeout = 10;
        private int readTimeout = 20;
        private OkhttpCall okhttpCall;

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setConnectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder setReadTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public Builder setOkhttpCall(OkhttpCall okhttpCall) {
            this.okhttpCall = okhttpCall;
            return this;
        }

        public OkhttpBuilder build() {
            OkhttpBuilder okhttpBuilder = new OkhttpBuilder();
            okhttpBuilder.url = this.url;
            okhttpBuilder.connectTimeout = this.connectTimeout;
            okhttpBuilder.readTimeout = this.readTimeout;
            okhttpBuilder.okhttpCall = this.okhttpCall;

            return okhttpBuilder;
        }
    }
}
