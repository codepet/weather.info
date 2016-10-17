package com.gc.weather.http;

import com.gc.weather.app.BaseApplication;
import com.gc.weather.entity.City;
import com.gc.weather.entity.Data;
import com.gc.weather.entity.Result;
import com.gc.weather.http.core.RetrofitManager;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class Http {

    public static void getWeather(String id, String name, final HttpCallback<Data> callback) {
        RetrofitManager.get().getNetService().getWeather(name, id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Func1<Throwable, Result<Data>>() {
                    @Override
                    public Result<Data> call(Throwable throwable) {
                        return null;
                    }
                })
                .map(new Func1<Result<Data>, Data>() {
                    @Override
                    public Data call(Result<Data> dataResult) {
                        // 需判空和判断返回码，返回码为0表示成功
                        if (dataResult == null || dataResult.getErrNum() != 0) {
                            return null;
                        }
                        return dataResult.getRetData();
                    }
                })
                .subscribe(new Action1<Data>() {
                    @Override
                    public void call(Data data) {
                        if (data == null) {  // 数据为空，提示获取失败
                            if (callback != null) {
                                callback.onFailure("");
                            }
                            return;
                        }
                        if (callback != null) {
                            callback.onSuccess(data);
                        }
                    }
                });
    }

    public static void getCity(String name, final HttpCallback<List<City>> callback) {
        BaseApplication.getService()
                .getCity(name)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Func1<Throwable, Result<List<City>>>() {
                    @Override
                    public Result<List<City>> call(Throwable throwable) {
                        return null;
                    }
                })
                .map(new Func1<Result<List<City>>, List<City>>() {
                    @Override
                    public List<City> call(Result<List<City>> listResult) {
                        if (listResult != null) {  // 系统异常时会出现空指针异常，所以这个判断必须做
                            return listResult.getRetData();
                        }
                        return null;
                    }
                })
                .subscribe(new Action1<List<City>>() {
                    @Override
                    public void call(List<City> cities) {
                        if (cities == null || cities.isEmpty()) {
                            if (callback != null) {
                                callback.onFailure("");
                            }
                            return;
                        }
                        if (callback != null) {
                            callback.onSuccess(cities);
                        }
                    }
                });
    }

}
