package com.gc.weather.view;

import android.widget.EditText;
import android.widget.ListView;

import com.gc.weather.entity.City;
import com.gc.weather.ui.adapter.ArrayListAdapter;

import java.util.List;

public interface ISearchView {

    String getSearchString();

    EditText getSearchView();

    List<City> getResultList();

    ListView getListView();

    ArrayListAdapter getAdapter();

    void showLoadingDialog();

    void hideLoadingDialog();

    void showMessage(String message);

}
