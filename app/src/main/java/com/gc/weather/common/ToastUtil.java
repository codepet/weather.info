package com.gc.weather.common;

import android.widget.Toast;

import com.gc.weather.app.BaseApplication;

public class ToastUtil {

    private static Toast toast;

    public static void show(String message) {
        if (toast == null) {
            toast = Toast.makeText(BaseApplication.getContext(), message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

}
