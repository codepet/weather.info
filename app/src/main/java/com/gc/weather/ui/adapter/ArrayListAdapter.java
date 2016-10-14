package com.gc.weather.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gc.weather.entity.City;

import java.util.List;

public class ArrayListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<City> list;

    public ArrayListAdapter(Context context, List<City> list) {
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 因存在同名城市，所以结果以省-市-地方式展示
        String name = list.get(position).getProvince_cn() + " - " + list.get(position).getDistrict_cn() + " - " + list.get(position).getName_cn();
        holder.text.setText(name);
        return convertView;
    }

    private class ViewHolder {
        TextView text;
    }
}
