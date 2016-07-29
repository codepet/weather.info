package com.gc.weather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.gc.weather.R;
import com.gc.weather.adapter.SimplePagerAdapter;
import com.gc.weather.entity.City;
import com.gc.weather.fragment.WeatherFragment;
import com.gc.weather.util.SerializeUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private ViewPager mWeatherPager;
    private SimplePagerAdapter mAdapter;
    private List<Fragment> mFragments;
    private ArrayList<City> cityList;
    public static MainActivity instance;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        instance = this;
        mWeatherPager = (ViewPager) findViewById(R.id.id_weather_page);
    }

    @Override
    protected void fetchData() {
        try {
            cityList = (ArrayList<City>) SerializeUtil.getObject(this, "cities");
        } catch (IOException e) {
        } catch (ClassNotFoundException e) {
        } finally {
            if (cityList == null || cityList.size() <= 0) {
                cityList = new ArrayList<>();
                City city = new City();
                city.setProvince_cn(getString(R.string.default_province_cn));
                city.setDistrict_cn(getString(R.string.default_district_cn));
                city.setName_cn(getString(R.string.default_name_cn));
                city.setName_en(getString(R.string.default_name_en));
                city.setArea_id(getString(R.string.default_area_id));
                cityList.add(city);
            }

        }
        mFragments = new ArrayList<>();
        for (City city : cityList) {
            mFragments.add(WeatherFragment.newInstance(city.getName_cn(), city.getArea_id()));
        }
        mAdapter = new SimplePagerAdapter(getSupportFragmentManager(), mFragments);
        mWeatherPager.setAdapter(mAdapter);
        mWeatherPager.setOffscreenPageLimit(mFragments.size() - 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            ArrayList<City> cities = (ArrayList<City>) data.getExtras().getSerializable("cities");
            if (cities.size() == 0) {
                City city = new City();
                city.setProvince_cn(getString(R.string.default_province_cn));
                city.setDistrict_cn(getString(R.string.default_district_cn));
                city.setName_cn(getString(R.string.default_name_cn));
                city.setName_en(getString(R.string.default_name_en));
                city.setArea_id(getString(R.string.default_area_id));
                cities.add(city);
            }
            cityList.clear();
            cityList.addAll(cities);
            mFragments.clear();
            for (City city : cityList) {
                mFragments.add(WeatherFragment.newInstance(city.getName_cn(), city.getArea_id()));
            }
            mAdapter.notifyDataSetChanged();
            mWeatherPager.setOffscreenPageLimit(mFragments.size() - 1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            SerializeUtil.saveObject(this, cityList, "cities");
        } catch (IOException e) {
        }
    }

    public ArrayList<City> getCityList() {
        return cityList;
    }
}
