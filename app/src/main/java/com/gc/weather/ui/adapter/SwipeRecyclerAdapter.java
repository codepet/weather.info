package com.gc.weather.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gc.weather.R;
import com.gc.weather.ui.callback.OnMovedAndSwipedListener;
import com.gc.weather.ui.callback.OnStateChangeListener;
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
        // 上下平移，交换数据，并通知更新
        Collections.swap(list, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        // 左右滑动删除数据，并通知更新
        list.remove(position);
        notifyItemRemoved(position);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements OnStateChangeListener {

        TextView cityName;

        public ItemViewHolder(View itemView) {
            super(itemView);
            cityName = (TextView) itemView.findViewById(R.id.id_city_name);
        }

        /**
         * Item被选中时回调，改变背景颜色
         */
        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.parseColor("#B0CECECE"));
        }

        /**
         * Item无状态时回调，设置清除背景颜色
         */
        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
