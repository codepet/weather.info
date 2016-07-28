package com.gc.weather.app;

import android.app.Application;
import android.content.Context;

import com.gc.weather.service.NetService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseApplication extends Application {

    private static Context context;
    private static OkHttpClient client;
    private static Retrofit retrofit;
    private static NetService service;
    private final static long READ_TIME_OUT = 8000;
    private final static String BASEURL = "http://apis.baidu.com";

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        initHttpClient();
        initRetrofit();
        initNetService();
    }

    private void initHttpClient() {
        client = new OkHttpClient.Builder()
                .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .build();
    }

    private void initRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASEURL)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
    }

    private void initNetService() {
        service = retrofit.create(NetService.class);
    }

    public static Context getContext() {
        return context;
    }

    public static OkHttpClient getHttpClient() {
        return client;
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    public static NetService getService() {
        return service;
    }

}
