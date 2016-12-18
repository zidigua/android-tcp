package com.threepapa.vmtcp.utils;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * json解析工具类
 *
 * @author huangchao
 * @since 1.0
 */
public class JsonAPI {
    /**
     * JSON转换成对象
     *
     * @param target
     * @param jason
     * @return
     * @throws Exception
     * @author huangchao
     * @since 1.0
     */

    public static <T> T jsonToObject(Class<T> target, JSONObject json)
            throws Exception {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json.toString(), target);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * String转换成对象
     *
     * @param jsonStr
     * @param target
     * @throws Exception
     * @author huangchao
     * @since 1.0
     */

    public static <T> T parseJsonToObj(String jsonStr, Class<T> target) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(jsonStr, target);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 对象转化成JSON
     *
     * @param obj
     * @return
     * @author huangchao
     * @since 1.0
     */

    public static String objectToJson(Object obj) {
        try {
            Gson gson = new Gson();
            return gson.toJson(obj);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * JSON转换成对象数组
     *
     * @param target
     * @param array
     * @return
     * @author huangchao
     * @since 1.0
     */

    @SuppressWarnings("unchecked")
    public static <T> List<T> jsonToList(Class<T> target, JSONArray array)
            throws JSONException {
        List<T> list = new ArrayList<T>();

        for (int i = 0; i < array.length(); i++) {
            if (target == String.class) {
                String json = array.getString(i);
                list.add((T) json);
            } else {
                JSONObject json = array.getJSONObject(i);
                try {
                    list.add(jsonToObject(target, json));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /**
     * List转换成JSON字符串
     *
     * @param list
     * @return
     * @author huangchao
     * @since 1.0
     */

    public static <T> String listToJson(List<T> list) throws Exception {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

}
