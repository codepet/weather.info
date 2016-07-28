package com.gc.weather.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gc.weather.R;
import com.gc.weather.activity.CityListActivity;
import com.gc.weather.activity.MainActivity;
import com.gc.weather.app.BaseApplication;
import com.gc.weather.entity.Data;
import com.gc.weather.entity.Index;
import com.gc.weather.entity.Result;
import com.gc.weather.entity.Weather;
import com.gc.weather.util.ConnUtil;
import com.gc.weather.util.ResourceUtil;
import com.gc.weather.util.SerializeUtil;
import com.gc.weather.util.SnackbarUtil;
import com.squareup.picasso.Picasso;
import com.yalantis.phoenix.PullToRefreshView;

import java.io.IOException;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class WeatherFragment extends Fragment {

    private TextView currentTempText;
    private TextView cityNameText;
    private TextView aqiText;
    private TextView windDirectionText;
    private TextView windPowerText;
    private TextView weekText;
    private TextView weatherTypeText;
    private TextView dateText;
    private TextView highTempText;
    private TextView lowTempText;
    private ImageView weatherImage;
    private LinearLayout forecastLayot;
    private LinearLayout historyLayout;
    private LinearLayout indexLayout;
    private PullToRefreshView refreshView;
    private ImageButton menuButton;

    private String cityName;
    private String cityId;

    public static WeatherFragment newInstance(String cityName, String cityId) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString("city_name", cityName);
        args.putString("city_id", cityId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cityName = getArguments().getString("city_name");
        cityId = getArguments().getString("city_id");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.framgent_weather, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        fetchData();
    }

    private void initView(View view) {
        currentTempText = (TextView) view.findViewById(R.id.id_current_temp);
        cityNameText = (TextView) view.findViewById(R.id.id_city_name);
        aqiText = (TextView) view.findViewById(R.id.id_weather_aqi);
        windDirectionText = (TextView) view.findViewById(R.id.id_wind_direction);
        windPowerText = (TextView) view.findViewById(R.id.id_wind_power);
        weekText = (TextView) view.findViewById(R.id.id_week);
        weatherTypeText = (TextView) view.findViewById(R.id.id_weather_type);
        dateText = (TextView) view.findViewById(R.id.id_date);
        highTempText = (TextView) view.findViewById(R.id.id_high_temp);
        lowTempText = (TextView) view.findViewById(R.id.id_low_temp);
        weatherImage = (ImageView) view.findViewById(R.id.id_weather_image);
        forecastLayot = (LinearLayout) view.findViewById(R.id.id_forecast_layout);
        historyLayout = (LinearLayout) view.findViewById(R.id.id_history_layout);
        indexLayout = (LinearLayout) view.findViewById(R.id.id_index_layout);
        menuButton = (ImageButton) view.findViewById(R.id.id_menu);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPupopWindow();
            }
        });
        refreshView = (PullToRefreshView) view.findViewById(R.id.id_pull_to_refresh);
        refreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fetchData();
                    }
                }, 1000);
            }
        });
    }

    private void showPupopWindow() {
        View view = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.layout_popup_view, new LinearLayout(getActivity()), false);
        final PopupWindow window = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        Button managerButton = (Button) view.findViewById(R.id.id_manager_city);
        managerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CityListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("cities", MainActivity.instance.getCityList());
                intent.putExtras(bundle);
                getActivity().startActivityForResult(intent, 300);
                window.dismiss();
            }
        });
        Button shareButton = (Button) view.findViewById(R.id.id_share_weather);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ColorDrawable drawable = new ColorDrawable(0xB0000000);
        window.setBackgroundDrawable(drawable);
        window.setWidth(200);
        window.setOutsideTouchable(true);
        window.setFocusable(true);
        window.showAsDropDown(menuButton, -150, 0);
    }

    public void fetchData() {
        try {
            Data data = (Data) SerializeUtil.getObject(getActivity(), cityId);
            if (data != null) {
                setData(data);
            }
        } catch (ClassNotFoundException e) {
        } catch (IOException e) {
        }
        if (!ConnUtil.isNetConnected(getActivity())) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshView.setRefreshing(false);
                    Snackbar.make(indexLayout, getString(R.string.net_error), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.setting), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                                }
                            }).show();
                }
            }, 1000);
            return;
        }
        BaseApplication.getService().getWeather(cityName, cityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Result<Data>, Data>() {
                    @Override
                    public Data call(Result<Data> dataResult) {
                        if (dataResult == null || dataResult.getErrNum() != 0) {
                            return null;
                        }
                        return dataResult.getRetData();
                    }
                })
                .subscribe(new Action1<Data>() {
                    @Override
                    public void call(Data data) {
                        if (data == null) {
                            refreshView.setRefreshing(false);
                            SnackbarUtil.show(indexLayout, getString(R.string.data_error));
                            return;
                        }
                        setData(data);
                        refreshView.setRefreshing(false);
                        SnackbarUtil.show(indexLayout, getString(R.string.data_success));
                        try {
                            SerializeUtil.saveObject(getActivity(), data, cityId);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void setData(Data data) {
        cityNameText.setText(data.getCity());
        currentTempText.setText(data.getToday().getCurTemp());
        dateText.setText(data.getToday().getDate());
        weekText.setText(data.getToday().getWeek());
        if (data.getToday().getAqi() == null || data.getToday().getAqi().isEmpty()) {
            aqiText.setText(getString(R.string.data_unknow));
        } else {
            aqiText.setText(data.getToday().getAqi());
        }
        weatherTypeText.setText(data.getToday().getType());
        windDirectionText.setText(data.getToday().getFengxiang());
        windPowerText.setText(data.getToday().getFengli());
        highTempText.setText(data.getToday().getHightemp());
        lowTempText.setText(data.getToday().getLowtemp());
        Picasso.with(getActivity())
                .load(ResourceUtil.getImageResource(data.getToday().getType()))
                .placeholder(R.mipmap.undefined)
                .into(weatherImage);
        // 获取未来天气
        forecastLayot.removeAllViews();
        for (Weather weather : data.getForecast()) {
            View view = LayoutInflater.from(getActivity())
                    .inflate(R.layout.layout_weather_item, new LinearLayout(getActivity()), false);
            TextView week = (TextView) view.findViewById(R.id.id_item_week);
            TextView type = (TextView) view.findViewById(R.id.id_item_weather_type);
            TextView date = (TextView) view.findViewById(R.id.id_item_date);
            TextView hightemp = (TextView) view.findViewById(R.id.id_item_high_temp);
            TextView lowtemp = (TextView) view.findViewById(R.id.id_item_low_temp);
            ImageView image = (ImageView) view.findViewById(R.id.id_item_weather_image);
            week.setText(weather.getWeek());
            type.setText(weather.getType());
            date.setText(weather.getDate());
            hightemp.setText(weather.getHightemp());
            lowtemp.setText(weather.getLowtemp());
            Picasso.with(getActivity())
                    .load(ResourceUtil.getImageResource(weather.getType()))
                    .placeholder(R.mipmap.undefined)
                    .into(image);
            forecastLayot.addView(view);
        }
        // 获取历史天气
        historyLayout.removeAllViews();
        for (Weather weather : data.getHistory()) {
            View view = LayoutInflater.from(getActivity())
                    .inflate(R.layout.layout_weather_item, new LinearLayout(getActivity()), false);
            TextView week = (TextView) view.findViewById(R.id.id_item_week);
            TextView type = (TextView) view.findViewById(R.id.id_item_weather_type);
            TextView date = (TextView) view.findViewById(R.id.id_item_date);
            TextView hightemp = (TextView) view.findViewById(R.id.id_item_high_temp);
            TextView lowtemp = (TextView) view.findViewById(R.id.id_item_low_temp);
            ImageView image = (ImageView) view.findViewById(R.id.id_item_weather_image);
            week.setText(weather.getWeek());
            type.setText(weather.getType());
            date.setText(weather.getDate());
            hightemp.setText(weather.getHightemp());
            lowtemp.setText(weather.getLowtemp());
            Picasso.with(getActivity())
                    .load(ResourceUtil.getImageResource(weather.getType()))
                    .placeholder(R.mipmap.undefined)
                    .into(image);
            historyLayout.addView(view);
        }
        // 获取建议指数
        indexLayout.removeAllViews();
        for (Index index : data.getToday().getIndex()) {
            View view = LayoutInflater.from(getActivity())
                    .inflate(R.layout.layout_index_item, new LinearLayout(getActivity()), false);
            TextView name = (TextView) view.findViewById(R.id.id_index_name);
            TextView level = (TextView) view.findViewById(R.id.id_index_level);
            TextView detail = (TextView) view.findViewById(R.id.id_index_detail);
            name.setText(index.getName());
            level.setText(index.getIndex());
            detail.setText(index.getDetails());
            indexLayout.addView(view);
        }
    }
}
