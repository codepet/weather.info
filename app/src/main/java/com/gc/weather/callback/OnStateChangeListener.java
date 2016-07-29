package com.gc.weather.callback;

public interface OnStateChangeListener {

    /**
     * Item被选中时回调
     */
    void onItemSelected();

    /**
     * 失去焦点时回调
     */
    void onItemClear();
}
