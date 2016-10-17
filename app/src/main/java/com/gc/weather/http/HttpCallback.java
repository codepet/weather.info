package com.gc.weather.http;

public interface HttpCallback<T> {

    void onSuccess(T t);

    void onFailure(String message);

}
