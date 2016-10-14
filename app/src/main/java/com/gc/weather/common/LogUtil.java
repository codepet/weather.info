package com.gc.weather.common;

import android.util.Log;

public class LogUtil {

    private static boolean ENABLE = true;

    public static void v(String tag, String msg) {
        if (ENABLE) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (ENABLE) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (ENABLE) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (ENABLE) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (ENABLE) {
            Log.e(tag, msg);
        }
    }

}
