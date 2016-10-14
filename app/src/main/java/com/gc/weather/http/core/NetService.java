package com.gc.weather.http.core;

import com.gc.weather.entity.City;
import com.gc.weather.entity.Data;
import com.gc.weather.entity.Result;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

public interface NetService {

    /**
     * 获取天气信息
     *
     * @param cityName 城市名
     * @param cityId   城市id
     * @return 天气信息结果的Observable对象
     */
    @Headers("apikey:a77bae703557e5884b6873af93b603d1")
    @GET("/apistore/weatherservice/recentweathers")
    Observable<Result<Data>> getWeather(@Query("cityname") String cityName, @Query("cityid") String cityId);

    /**
     * 查询城市信息
     *
     * @param cityName 城市名
     * @return 城市信息列表的Observable对象
     */
    @Headers("apikey:a77bae703557e5884b6873af93b603d1")
    @GET("/apistore/weatherservice/citylist")
    Observable<Result<List<City>>> getCity(@Query("cityname") String cityName);

}
