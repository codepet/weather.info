package com.gc.weather.ui.callback;

public interface OnMovedAndSwipedListener {

    /**
     * Item上下移动回调接口
     * @param fromPosition 开始的位置
     * @param toPosition 结束的位置
     * @return
     */
    boolean onItemMove(int fromPosition, int toPosition);

    /**
     * 左右滑动删除接口
     * @param position 位置
     */
    void onItemDismiss(int position);

}
