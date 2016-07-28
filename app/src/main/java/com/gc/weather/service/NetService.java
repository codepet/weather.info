package com.gc.weather.service;

import com.gc.weather.entity.City;
import com.gc.weather.entity.Data;
import com.gc.weather.entity.Result;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

public interface NetService {

    @Headers("apikey:a77bae703557e5884b6873af93b603d1")
    @GET("/apistore/weatherservice/recentweathers")
    Observable<Result<Data>> getWeather(@Query("cityname") String cityName, @Query("cityid") String cityId);

    @Headers("apikey:a77bae703557e5884b6873af93b603d1")
    @GET("/apistore/weatherservice/citylist")
    Observable<Result<List<City>>> getCity(@Query("cityname") String cityName);

}
