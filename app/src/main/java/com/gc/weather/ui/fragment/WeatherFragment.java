package com.gc.weather.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import com.gc.weather.ui.activity.CityListActivity;
import com.gc.weather.ui.activity.MainActivity;
import com.gc.weather.app.BaseApplication;
import com.gc.weather.entity.Data;
import com.gc.weather.entity.Index;
import com.gc.weather.entity.Result;
import com.gc.weather.entity.Weather;
import com.gc.weather.common.ConnUtil;
import com.gc.weather.common.LogUtil;
import com.gc.weather.common.ResourceUtil;
import com.gc.weather.common.SerializeUtil;
import com.gc.weather.common.SnackbarUtil;
import com.squareup.picasso.Picasso;
import com.yalantis.phoenix.PullToRefreshView;

import java.io.IOException;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class WeatherFragment extends BaseFragment {

    private final static String TAG = WeatherFragment.class.getSimpleName();
    private TextView currentTempText;  // 当前温度
    private TextView cityNameText;  // 城市名
    private TextView aqiText;  // PM2.5
    private TextView windDirectionText; // 风向
    private TextView windPowerText; //风力
    private TextView weekText; // 周日期
    private TextView weatherTypeText; // 天气类型
    private TextView dateText;  // 日期
    private TextView highTempText;  // 最高温度
    private TextView lowTempText;  // 最低温度
    private ImageView weatherImage;  // 天气类型图片
    private LinearLayout forecastLayot;  // 未来天气的布局
    private LinearLayout historyLayout;  //历史天气的布局
    private LinearLayout indexLayout;  // 天气指数建议布局
    private PullToRefreshView refreshView;  // 下拉刷新布局
    private ImageButton menuButton;  // 菜单按钮

    private String cityName;  // 城市名
    private String cityId;  // 城市id

    public static WeatherFragment newInstance(String cityName, String cityId) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString("city_name", cityName);  // 设置城市名
        args.putString("city_id", cityId);  // 设置城市id
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cityName = getArguments().getString("city_name");  // 获取城市名
        cityId = getArguments().getString("city_id");  // 获取城市id
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
        initData();
    }

    /**
     * 初始化所有控件
     *
     * @param view fragment展示的view
     */
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

    /**
     * 从本地文件获取缓存数据
     */
    private void initData() {
        try {
            Data data = (Data) SerializeUtil.getObject(getActivity(), cityId);
            if (data != null) {
                setData(data);
            }
        } catch (ClassNotFoundException e) {
            LogUtil.e(TAG, "get object throws class not found exception: " + e.getMessage());
        } catch (IOException e) {
            LogUtil.e(TAG, "get object throws io exception: " + e.getMessage());
        }
    }

    /**
     * 界面可见时调用
     */
    @Override
    public void fetchData() {
        if (!connect()) return;
        fetch();
    }

    /**
     * 判断网络连接
     *
     * @return 网络是否连接
     */
    private boolean connect() {
        if (!ConnUtil.isNetConnected(getActivity())) {  // 判断网络状态
            new Handler().postDelayed(new Runnable() {  // 无网络延时1000ms提示
                @Override
                public void run() {
                    refreshView.setRefreshing(false);
                    Snackbar.make(indexLayout, getString(R.string.net_error), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.setting), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // 点击可打开设置界面
                                    startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                                }
                            }).show();
                }
            }, 1000);
            return false;
        }
        return true;
    }

    /**
     * 获取数据
     */
    private void fetch() {
        BaseApplication.getService().getWeather(cityName, cityId)
                .subscribeOn(Schedulers.io())  // 请求数据于IO线程
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
                        refreshView.setRefreshing(false);
                        if (data == null) {  // 数据为空，提示获取失败
                            SnackbarUtil.show(indexLayout, getString(R.string.data_error));
                            return;
                        }
                        setData(data);
                        SnackbarUtil.show(indexLayout, getString(R.string.data_success));
                        try {
                            // 获取数据成功后保存至本地
                            SerializeUtil.saveObject(getActivity(), data, cityId);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 数据与界面绑定
     *
     * @param data 获取的数据
     */
    private void setData(Data data) {
        setToday(data);
        setFuture(data);
        setHistory(data);
        setIndex(data);
    }

    /**
     * 设置今天的数据
     *
     * @param data 数据
     */
    private void setToday(Data data) {
        cityNameText.setText(data.getCity());
        currentTempText.setText(data.getToday().getCurTemp());
        dateText.setText(data.getToday().getDate());
        weekText.setText(data.getToday().getWeek());
        // aqi需判空，不是每一个城市都含有此详细信息
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
                .placeholder(R.mipmap.undefined)  // 占位图
                .into(weatherImage);
    }

    /**
     * 设置未来天气数据
     *
     * @param data 数据
     */
    private void setFuture(Data data) {
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
            Picasso.with(getActivity())  // 利用Picasso加载本地图片
                    .load(ResourceUtil.getImageResource(weather.getType()))
                    .placeholder(R.mipmap.undefined)  // 占位图
                    .into(image);
            forecastLayot.addView(view);
        }
    }

    /**
     * 设置历史天气数据
     *
     * @param data 数据
     */
    private void setHistory(Data data) {
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
    }

    /**
     * 设置建议指数
     *
     * @param data 数据
     */
    private void setIndex(Data data) {
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


    /**
     * 二级菜单使用PopupWindow
     */
    private void showPupopWindow() {
        // Fragment Inflater 使用((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
        // Activity Inflater 使用LayoutInflater.from(getActivity())
        View view = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.layout_popup_view, new LinearLayout(getActivity()), false);
        final PopupWindow window = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        Button managerButton = (Button) view.findViewById(R.id.id_manager_city);
        managerButton.setOnClickListener(new View.OnClickListener() {  // 城市管理按钮点击事件
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CityListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("cities", ((MainActivity) getActivity()).getCityList());
                intent.putExtras(bundle);
                getActivity().startActivityForResult(intent, 300);
                window.dismiss();
            }
        });
        Button shareButton = (Button) view.findViewById(R.id.id_share_weather);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 添加分享功能
            }
        });
        ColorDrawable drawable = new ColorDrawable(0xB0000000);  // 半透明背景
        window.setBackgroundDrawable(drawable);  // 设置一个背景
        window.setWidth(200);  // 设置宽度
        window.setOutsideTouchable(true);  // 点击外部取消
        window.setFocusable(true);  // 必须设置此属性，否则点击返回键会直接退出程序
        window.showAsDropDown(menuButton, -150, 0);  // 显示在菜单按钮下
    }
}
