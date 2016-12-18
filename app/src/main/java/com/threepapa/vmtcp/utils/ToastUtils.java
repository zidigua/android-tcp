package com.threepapa.vmtcp.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 类描述：ToastUtils的管理类
 *
 * @author zanyang
 * @version 2.0
 */
public class ToastUtils {
    private static Toast mToast;

    private ToastUtils() {
        /** cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static boolean isShow = true;

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        if (mToast == null) {
            mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        }
        if (isShow) {
            mToast.setText(message);
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.show();
        }

    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, int message) {
        if (mToast == null) {
            mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        }
        if (isShow) {
            mToast.setText(message);
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.show();
        }

    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        if (mToast == null) {
            mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        }
        if (isShow) {
            mToast.setText(message);
            mToast.setDuration(Toast.LENGTH_LONG);
            mToast.show();
        }

    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, int message) {
        if (mToast == null) {
            mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        }
        if (isShow) {
            mToast.setText(message);
            mToast.setDuration(Toast.LENGTH_LONG);
            mToast.show();

        }
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, CharSequence message, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        }
        if (isShow) {
            mToast.setText(message);
            mToast.setDuration(duration);
            mToast.show();

        }
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, int message, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        }
        if (isShow) {
            mToast.setText(message);
            mToast.setDuration(duration);
            mToast.show();

        }
    }

}
