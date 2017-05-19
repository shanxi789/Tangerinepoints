package com.mzth.tangerinepoints.util;

import android.content.Context;
import android.widget.Toast;

/**
 * @Desc Toast工具类
 */
public class ToastUtil {
	
    private static Toast toast;

    public static void showShort(Context context, CharSequence message) {
        if (null == toast) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    public static void showLong(Context context, CharSequence message) {
        if (null == toast) {
            toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    public static void hideToast() {
        if (null != toast) {
            toast.cancel();
        }
    }
}
