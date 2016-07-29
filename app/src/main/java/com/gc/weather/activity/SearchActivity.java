package com.gc.weather.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.gc.weather.R;
import com.gc.weather.adapter.ArrayListAdapter;
import com.gc.weather.app.BaseApplication;
import com.gc.weather.entity.City;
import com.gc.weather.entity.Result;
import com.gc.weather.util.ConnUtil;
import com.gc.weather.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class SearchActivity extends BaseActivity {

    private EditText mSearchText;
    private Button mSearchButton;
    private ListView mResultList;
    private List<City> mCityList;
    private ArrayListAdapter mAdapter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search);
        initListView();
        initEditView();
        initProgressBar();
    }

    private void initListView() {
        mResultList = (ListView) findViewById(R.id.id_search_result_list);
        TextView emptyView = (TextView) findViewById(R.id.id_empty_view);
        mResultList.setEmptyView(emptyView);
        mCityList = new ArrayList<>();
        mAdapter = new ArrayListAdapter(this, mCityList);
        mResultList.setAdapter(mAdapter);
        mResultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("city", mCityList.get(position));
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    /**
     * 初始化输入框
     */
    private void initEditView() {
        mSearchButton = (Button) findViewById(R.id.id_search_button);
        mSearchText = (EditText) findViewById(R.id.id_search_text);
        // 监听输入，当输入为空时显示取消，不为空显示为搜索
        mSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 输入空格会改变状态
                if (mSearchText.getText().length() > 0) {
                    mSearchButton.setText(getString(R.string.search));
                } else {
                    mSearchButton.setText(getString(R.string.search_cancel));
                }
            }
        });
        // 监听键盘输入，使键盘确定键显示为搜索样式
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 这个判断是防止用户输入空格后点击键盘上的搜索按钮出现关闭的问题
                    if (mSearchText.getText().toString().trim().isEmpty()) {
                        ToastUtil.show(getString(R.string.search_tips));
                        return true;
                    }
                    getData();
                    return true;
                }
                return false;
            }
        });
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    /**
     * 初始化进度对话框
     */
    private void initProgressBar() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("搜索中...");
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void fetchData() { }

    /**
     * 获取数据，不把该方法写进fetchData的原因是想点击搜索按钮时再触发该事件
     */
    private void getData() {
        if (mSearchText.getText().toString().trim().length() > 0) {
            mProgressDialog.show();
            mCityList.clear();  // 搜索前清空上一次的搜索结果
            // 隐藏键盘
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mSearchText.getWindowToken(), 0);
            if (!ConnUtil.isNetConnected(SearchActivity.this)) {  // 判断网络状况
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mProgressDialog.dismiss();
                        Snackbar.make(mResultList, getString(R.string.net_error), Snackbar.LENGTH_LONG)
                                .setAction("设置", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                                    }
                                }).show();
                    }
                }, 1000);
                return;
            }
            BaseApplication.getService()
                    .getCity(mSearchText.getText().toString().trim())
                    .subscribeOn(Schedulers.io())  // 请求于io线程
                    .observeOn(AndroidSchedulers.mainThread())
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
                            mProgressDialog.dismiss();
                            //  此处需要判空，否则会有空指针异常
                            if (cities == null || cities.isEmpty()) {
                                Snackbar.make(mResultList, getString(R.string.empty_tips), Snackbar.LENGTH_SHORT).show();
                                return;
                            }
                            mCityList.addAll(cities);
                            mAdapter.notifyDataSetChanged();
                        }
                    });
        } else {
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 在Activity finish()前需取消Dialog的绑定，否则会抛出异常
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
