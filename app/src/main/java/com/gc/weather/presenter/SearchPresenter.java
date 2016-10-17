package com.gc.weather.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.inputmethod.InputMethodManager;

import com.gc.weather.R;
import com.gc.weather.common.ConnUtil;
import com.gc.weather.entity.City;
import com.gc.weather.http.HttpCallback;
import com.gc.weather.model.SearchModel;
import com.gc.weather.view.ISearchView;

import java.util.List;

public class SearchPresenter {

    private ISearchView searchView;
    private SearchModel searchModel;

    public SearchPresenter(ISearchView searchView) {
        this.searchView = searchView;
        searchModel = new SearchModel();
    }

    public void search(final Activity act) {
        searchView.getResultList().clear();  // 搜索前清空上一次的搜索结果
        if (searchView.getSearchString().length() > 0) {
            if (searchView.getSearchString().trim().isEmpty()) {
                searchView.showMessage(act.getString(R.string.search_tips));
                return;
            }
            searchView.showLoadingDialog();
            // 隐藏键盘
            InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchView.getSearchView().getWindowToken(), 0);
            if (!ConnUtil.isNetConnected(act)) {  // 判断网络状况
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        searchView.hideLoadingDialog();
                        searchView.showMessage(act.getString(R.string.net_error));
                    }
                }, 1000);
                return;
            }
            searchModel.search(searchView.getSearchString(), new HttpCallback<List<City>>() {
                @Override
                public void onSuccess(List<City> cities) {
                    searchView.hideLoadingDialog();
                    searchView.getResultList().addAll(cities);
                    searchView.getAdapter().notifyDataSetChanged();
                }

                @Override
                public void onFailure(String message) {
                    searchView.hideLoadingDialog();
                    searchView.showMessage(message);
                }
            });
        } else {
            act.finish();
        }
    }

}
