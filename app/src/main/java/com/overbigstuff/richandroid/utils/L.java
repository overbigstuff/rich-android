package com.overbigstuff.richandroid.utils;

import android.util.Log;

public class L {

    public static boolean LOG_ENABLED = true;

    public static void e(String message) {
        if (LOG_ENABLED) {
            Log.e("Rich Android", "" + message);
        }
    }

    public static void i(String message) {
        if (LOG_ENABLED) {
            Log.i("Rich Android", "" + message);
        }
    }

    public static void d(String message) {
        if (LOG_ENABLED) {
            Log.d("Rich Android", "" + message);
        }
    }
}
