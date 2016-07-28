package com.gc.weather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.gc.weather.R;
import com.gc.weather.adapter.SwipeRecyclerAdapter;
import com.gc.weather.callback.SimpleItemTouchHelperCallback;
import com.gc.weather.entity.City;
import com.gc.weather.util.LogUtil;
import com.gc.weather.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class CityListActivity extends BaseActivity {

    private FloatingActionButton addButton;
    private RecyclerView cityView;
    private SwipeRecyclerAdapter adapter;
    private ArrayList<City> cities;
    private SimpleItemTouchHelperCallback callback;
    private ItemTouchHelper helper;
    private final static int REQUESTCODE = 100;
    private boolean isChange = false;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_city_list);
        cityView = (RecyclerView) findViewById(R.id.id_city_list);
        cityView.setLayoutManager(new LinearLayoutManager(this));
        addButton = (FloatingActionButton) findViewById(R.id.id_add_city);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CityListActivity.this, SearchActivity.class);
                startActivityForResult(intent, REQUESTCODE);
            }
        });
    }

    @Override
    protected void fetchData() {
        cities = (ArrayList<City>) getIntent().getExtras().getSerializable("cities");
        adapter = new SwipeRecyclerAdapter(this, cities);
        cityView.setAdapter(adapter);
        cityView.setHasFixedSize(true);
        cityView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        callback = new SimpleItemTouchHelperCallback(adapter);
        helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(cityView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE && resultCode == RESULT_OK) {
            City city = (City) data.getExtras().getSerializable("city");
            cities.add(city);
            adapter.notifyDataSetChanged();
            isChange = true;
        }
    }

    @Override
    public void onBackPressed() {
        if (callback.isChange() || isChange) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("cities", cities);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
        }
        super.onBackPressed();
    }
}
