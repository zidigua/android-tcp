package com.threepapa.vmtcp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * 类描述：方便操作的工具类用于存储相应的字段
 *
 * @author zanyang
 * @version 2.0
 */
public class PreferenceUtils {

    private static SharedPreferences pref;


    /**
     * 该方法在Application 里初始化
     *
     * @param context   上下文
     */
    public static void initialize(Context context) {
        pref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public static boolean contains(String key) {
        return pref.contains(key);
    }

    public static boolean get(String key, boolean defValue) {
        return pref.getBoolean(key, defValue);
    }

    public static float get(String key, float defValue) {
        return pref.getFloat(key, defValue);
    }

    public static int get(String key, int defValue) {
        return pref.getInt(key, defValue);
    }

    public static long get(String key, long defValue) {
        return pref.getLong(key, defValue);
    }

    public static String get(String key, String defValue) {
        return pref.getString(key, defValue);
    }

    public static String get(String key) {
        return pref.getString(key, "");
    }

    public static void put(String key, boolean value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void put(String key, float value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public static void put(String key, int value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void put(String key, long value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static void put(String key, String value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void clear() {
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }

    public static void remove(String key) {
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.apply();
    }

    public static Map<String, ?> getAll() {
        return pref.getAll();
    }

}
