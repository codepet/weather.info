package com.gc.weather.common;

import android.support.design.widget.Snackbar;
import android.view.View;

public class SnackbarUtil {

    private static Snackbar snackbar;

    public static void show(View view, String message) {
        if (snackbar == null) {
            snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        } else {
            snackbar.setText(message);
        }
        snackbar.show();
    }
}
