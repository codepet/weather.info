package com.gc.weather.http.core;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private NetService mNetService;

    private volatile static RetrofitManager mInstance;

    private RetrofitManager() {
        Retrofit mRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(HttpCore.getInstance().getHttpClient())
                .baseUrl(HttpConfig.getBaseUrl())
                .build();
        mNetService = mRetrofit.create(NetService.class);
    }

    public static RetrofitManager get() {
        if (mInstance == null) {
            synchronized (RetrofitManager.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitManager();
                }
            }
        }
        return mInstance;
    }

    public NetService getNetService() {
        return mNetService;
    }
}
