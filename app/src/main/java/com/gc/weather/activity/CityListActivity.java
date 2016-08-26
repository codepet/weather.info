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
import com.gc.weather.widget.DividerItemDecoration;

import java.util.ArrayList;

public class CityListActivity extends BaseActivity {

    private FloatingActionButton addButton;
    private RecyclerView cityView;
    private SwipeRecyclerAdapter adapter;
    private ArrayList<City> cities;
    private SimpleItemTouchHelperCallback callback;  // RecyclerView触碰回调
    private final static int REQUESTCODE = 100;
    private boolean isChange = false;  // 监控数据是否变化

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

    @SuppressWarnings("unchecked")
    @Override
    protected void fetchData() {
        // 获取从MainActivity传来的数据
        cities = (ArrayList<City>) getIntent().getExtras().getSerializable("cities");
        adapter = new SwipeRecyclerAdapter(this, cities);
        cityView.setAdapter(adapter);
        cityView.setHasFixedSize(true);
        cityView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        // 以下三行代码完成RecyclerView侧滑删除和上下移动的功能
        callback = new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(cityView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE && resultCode == RESULT_OK) {
            // 获得得搜索得到的城市数据并添加到列表中
            City city = (City) data.getExtras().getSerializable("city");
            cities.add(city);
            adapter.notifyDataSetChanged();
            isChange = true;
        }
    }

    @Override
    public void onBackPressed() {
        if (callback.isChange() || isChange) {  // 如果数据有变化则通知上一个activity更新数据
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("cities", cities);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
        }
        super.onBackPressed();
    }
}
