package com.gc.weather.callback;

public interface OnMovedAndSwipedListener {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);

}
