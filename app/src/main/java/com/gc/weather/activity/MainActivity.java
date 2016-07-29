package com.gc.weather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.gc.weather.R;
import com.gc.weather.adapter.SimplePagerAdapter;
import com.gc.weather.entity.City;
import com.gc.weather.fragment.WeatherFragment;
import com.gc.weather.util.LogUtil;
import com.gc.weather.util.SerializeUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private final static String TAG = MainActivity.class.getSimpleName();
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

    @SuppressWarnings("unchecked")
    @Override
    protected void fetchData() {
        try {
            cityList = (ArrayList<City>) SerializeUtil.getObject(this, "cities");
        } catch (IOException e) {
            LogUtil.e(TAG, "get object throws io exception: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            LogUtil.e(TAG, "get object throws class not found exception: " + e.getMessage());
        } finally {
            // 如果本地没有存储数据，则添加默认的城市
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

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            ArrayList<City> cities = (ArrayList<City>) data.getExtras().getSerializable("cities");
            if (cities != null && cities.size() == 0) {  // 如果返回城市数为0,则添加默认城市
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
            // 重新设置预加载数，否则新添加的城市不会自动更新天气信息
            mWeatherPager.setOffscreenPageLimit(mFragments.size() - 1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            SerializeUtil.saveObject(this, cityList, "cities");
        } catch (IOException e) {
            LogUtil.e(TAG, "save object throws io exception: " + e.getMessage());
        }
    }

    public ArrayList<City> getCityList() {
        return cityList;
    }
}
