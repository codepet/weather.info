package com.gc.weather.model;

import com.gc.weather.entity.City;
import com.gc.weather.http.Http;
import com.gc.weather.http.HttpCallback;

import java.util.List;

public class SearchModel {

    public void search(String name, HttpCallback<List<City>> callback) {
        Http.getCity(name, callback);
    }

}
