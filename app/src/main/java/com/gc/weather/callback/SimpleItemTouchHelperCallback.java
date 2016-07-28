package com.gc.weather.callback;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private OnMovedAndSwipedListener listener;
    private boolean isChange = false;

    public SimpleItemTouchHelperCallback(OnMovedAndSwipedListener listener) {
        this.listener = listener;
    }

    /**
     * 这个方法是用来设置我们拖动的方向以及侧滑的方向
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            // 设置拖拽方向为上下左右
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            // 不支持侧滑
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else {
            // 拖拽方向为上下
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            // 滑动方向为左右
            final int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            return makeMovementFlags(dragFlags, swipeFlags);
        }
    }

    /**
     * 当我们拖动时回调此方法
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        listener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        isChange = true;
        return true;
    }

    /**
     * 当我们侧滑时回调此方法
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onItemDismiss(viewHolder.getAdapterPosition());
        isChange = true;
    }

    /**
     * 当状态改变时回调此方法
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        //当前状态不是idel（空闲）状态时，说明当前正在拖拽或者侧滑
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof OnStateChangeListener) {
                OnStateChangeListener listener = (OnStateChangeListener) viewHolder;
                listener.onItemSelected();
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    /**
     * 当用户拖拽完或者侧滑完一个item时回调此方法，用来清除施加在item上的一些状态
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (viewHolder instanceof OnStateChangeListener) {
            OnStateChangeListener listener = (OnStateChangeListener) viewHolder;
            listener.onItemClear();
        }
    }

    /**
     * 这个方法可以判断当前是拖拽还是侧滑
     */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            //根据侧滑的位移来修改item的透明度
            final float alpha = 1.0f - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
            viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    public boolean isChange() {
        return isChange;
    }
}
