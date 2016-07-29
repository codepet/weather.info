package com.gc.weather.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView(savedInstanceState);
        fetchData();
    }

    /**
     * 初始化控件
     * @param savedInstanceState onCreate(savedInstanceState)
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 获取数据
     */
    protected abstract void fetchData();
}
