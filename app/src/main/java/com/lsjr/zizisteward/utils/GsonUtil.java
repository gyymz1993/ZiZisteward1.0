package com.lsjr.zizisteward.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class GsonUtil {
    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    private GsonUtil() {
    }

    public static Gson getInstance() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    /**
     * @param object
     * @return
     */
    public static String GsonString(Object object) {
        String gsonString = null;
        if (gson != null) {
            gsonString = gson.toJson(object);
        }
        return gsonString;
    }

    public static Object JsontoString(String result, String key) {
        if (result == null || key == null)
            throw new NullPointerException("json or key is null");
        try {
            if (new JSONObject(result).opt(key) == null) {
                return null;
            }
            return new JSONObject(result).get(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getJson(String gsonString, String key, String flag) {
        if (gsonString == null || key == null)
            throw new NullPointerException("json or key is null");
        try {
            JSONObject jsonObject = new JSONObject(gsonString);
            if (flag.equals("JSONObject")) {
                if (jsonObject.optJSONObject(key) == null) {
                    return "";
                }
                return jsonObject.optJSONObject(key).toString();
            } else if (flag.equals("JSONArray")) {
                if (jsonObject.optJSONArray(key) == null) {
                    return "";
                }
                return jsonObject.optJSONArray(key).toString();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> T GsonToBean(String gsonString, Class<T> cls) {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(gsonString, cls);
        }
        return t;
    }

    /**
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> List<T> GsonToList(String gsonString, Class<T> cls) {
        List<T> list = new ArrayList<T>();
        if (gson != null) {
            list = gson.fromJson(gsonString, new TypeToken<List<T>>() {
            }.getType());
        }
        return list;
    }

    /**
     * @param gsonString
     * @return
     */
    public static <T> List<Map<String, T>> GsonToListMaps(String gsonString) {
        List<Map<String, T>> list = new ArrayList<Map<String, T>>();
        if (gson != null) {
            list = gson.fromJson(gsonString,
                    new TypeToken<List<Map<String, T>>>() {
                    }.getType());
        }
        return list;
    }

    /**
     * @param gsonString
     * @return
     */
    public static <T> Map<String, T> GsonToMaps(String gsonString) {
        Map<String, T> map = null;
        if (gson != null) {
            map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
            }.getType());
        }
        return map;
    }

}