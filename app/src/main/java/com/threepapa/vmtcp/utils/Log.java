package com.threepapa.vmtcp.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

/**
 * <p>
 * Log类，对系统的Log类功能增强
 * </p>
 * <br/>
 * <p>
 * 通过改变类中的开关变量，可控制是否打印log，是否在SD上生成Log文件,在正式发布项目时只打印error级别以上日志
 * </p>
 *
 * @author huangchao
 * @since 1.0
 */
public final class Log {
    private static final String APP_TAG = "Sangebaba";
    /**
     * 是否在控制台输出Log
     */
    public static final boolean DEVELOPER_MODE = true;
    /**
     * 是否在SD卡生成Log文件
     */
    public static final boolean LogSD = false;

    /**
     * 在需要打印log的类中，调用这个方法来生成tag
     *
     * @param cls   需要打印log类的.class
     * @param debug 控制是否打印当前类里面的log
     * @return
     * @author huangchao
     * @since 1.0
     */
    @SuppressWarnings("rawtypes")
    public static String makeTag(Class cls, boolean debug) {
        if (!debug) {
            return "";
        } else {
            return cls.getSimpleName();
        }
    }

    private Log() {
    }

    private static String formatMsg(String tag, String msg) {
        return tag + " - " + msg;
    }

    public static void e(String tag, String msg) {
        android.util.Log.e(APP_TAG, formatMsg(tag, msg));
    }

    public static void e(String tag, String msg, Throwable tr) {
        android.util.Log.e(APP_TAG, formatMsg(tag, msg), tr);
    }

    public static void w(String tag, String msg) {
        if (!DEVELOPER_MODE)
            return;
        android.util.Log.w(APP_TAG, formatMsg(tag, msg));
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (!DEVELOPER_MODE)
            return;
        android.util.Log.w(APP_TAG, formatMsg(tag, msg), tr);
    }

    public static void i(String tag, String msg) {
        if (!DEVELOPER_MODE)
            return;
        android.util.Log.i(APP_TAG, formatMsg(tag, msg));
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (!DEVELOPER_MODE)
            return;
        android.util.Log.i(APP_TAG, formatMsg(tag, msg), tr);
    }

    public static void d(String tag, String msg) {
        if (!DEVELOPER_MODE)
            return;
        if (LogSD) {
            log(tag, msg);
        }
        android.util.Log.d(APP_TAG, formatMsg(tag, msg));
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (!DEVELOPER_MODE)
            return;
        android.util.Log.d(APP_TAG, formatMsg(tag, msg), tr);
    }

    public static void v(String tag, String msg) {
        android.util.Log.v(APP_TAG, formatMsg(tag, msg));
    }

    public static void v(String tag, String msg, Throwable tr) {
        android.util.Log.v(APP_TAG, formatMsg(tag, msg), tr);
    }

    // Log on sdcard,Just for debug use
    public static void log(String tag, String msg) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String file = Environment.getExternalStorageDirectory().getPath() + File.separator + APP_TAG + ".txt";
            File SDFile = new File(file);
            if (!SDFile.exists()) {
                try {
                    SDFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                FileOutputStream outputStream = new FileOutputStream(file, true);
                outputStream.write((DateFormat.getDateTimeInstance().format(new Date()) + "[" + tag + "]" + ": " + msg + "\r\n")
                        .getBytes());
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
