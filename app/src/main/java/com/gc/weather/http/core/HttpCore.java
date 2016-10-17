package com.gc.weather.http.core;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class HttpCore {
    private OkHttpClient mHttpClient;

    private volatile static HttpCore mInstance;

    public static HttpCore getInstance() {
        if (mInstance == null) {
            synchronized (HttpCore.class) {
                if (mInstance == null) {
                    mInstance = new HttpCore();
                }
            }
        }
        return mInstance;
    }

    private HttpCore() {
        HttpConfig httpConfig = new HttpConfig();
        mHttpClient = new OkHttpClient.Builder()
                .connectTimeout(httpConfig.getConnectedTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(httpConfig.getReadTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(httpConfig.getWriteTimeout(), TimeUnit.MILLISECONDS)
                .build();
    }

    public OkHttpClient getHttpClient() {
        return mHttpClient;
    }
}
