package com.gc.weather.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gc.weather.R;
import com.gc.weather.callback.OnMovedAndSwipedListener;
import com.gc.weather.callback.OnStateChangeListener;
import com.gc.weather.entity.City;

import java.util.Collections;
import java.util.List;

public class SwipeRecyclerAdapter extends RecyclerView.Adapter<SwipeRecyclerAdapter.ItemViewHolder> implements OnMovedAndSwipedListener {

    private List<City> list;
    private LayoutInflater inflater;

    public SwipeRecyclerAdapter(Context context, List<City> list) {
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_city_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.cityName.setText(list.get(position).getName_cn());
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(list, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements OnStateChangeListener {

        TextView cityName;

        public ItemViewHolder(View itemView) {
            super(itemView);
            cityName = (TextView) itemView.findViewById(R.id.id_city_name);
        }


        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.parseColor("#B0CECECE"));
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
