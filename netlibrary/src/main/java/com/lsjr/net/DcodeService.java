package com.lsjr.net;

import android.annotation.SuppressLint;
import android.content.Context;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.shove.Convert;
import com.shove.security.Encrypt;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * @version 需要加密目前这种方法可行
 * @author: gyymz1993
 * 创建时间：2017/5/3 22:46
 **/
public class DcodeService {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    public static void  initialize(Context context){
        mContext=context;
    }

    private static ApiService getApiService() {
        if (mContext==null){
            throw new NullPointerException("请先初始化 initialize");
        }
        return AppClient.getApiService(mContext);
    }

    /* get网络请求入口*/
    public static Observable<String> getServiceData(Map map) {
        if (TextUtils.isEmpty(BaseUrl.bastUrl)){
            throw new NullPointerException("请设置BaseUrl");
        }
        //UrlUtils.spliceGetUrl(BaseUrl.bastUrl, map);
        String baseUrl = encryptUrl(false, BaseUrl.bastUrl, BaseUrl.URLKEY, map);
        return getApiService().getData(baseUrl);
    }


    /* get网络请求入口*/
    public static Observable<String> getCacheServiceData(Map map) {
        if (TextUtils.isEmpty(BaseUrl.bastUrl)){
            throw new NullPointerException("请设置BaseUrl");
        }
        //UrlUtils.spliceGetUrl(BaseUrl.bastUrl, map);
        String baseUrl = encryptUrl(true, BaseUrl.bastUrl, BaseUrl.URLKEY, map);
        return getApiService().getData(baseUrl);
    }


    /* post网络请求入口*/
    public static Observable<String> postServiceData(Map map) {
        if (TextUtils.isEmpty(BaseUrl.bastUrl)){
            throw new NullPointerException("请设置BaseUrl");
        }
        //UrlUtils.encodesParameters(BaseUrl.bastUrl, map);
        String baseUrl = encryptUrl(false, BaseUrl.bastUrl, BaseUrl.URLKEY, map);
        return getApiService().postData(baseUrl);
    }


    /*没有加密*/
    public static Observable<String> getBody(String url, Map map) {
        return getApiService().getBody(url, map);
    }


    /**
     * Url加密处理
     *
     * @param urlBase
     * @param key
     * @param parameters
     * @return
     */
    private static String encryptUrl(boolean isCache, String urlBase, String key, Map<String, String> parameters) {
        if ((parameters.containsKey("_s")) || (parameters.containsKey("_t"))) {
            // throw new RuntimeException("在使用 buildUrl 方法构建通用 REST 接口 Url 时，不能使用 _s, _t 此保留字作为参数名");
            //L_.e("在使用 buildUrl 方法构建通用 REST 接口 Url 时，不能使用 _s, _t 此保留字作为参数名");
            parameters.remove("_t");
            parameters.remove("_s");
        }
        if (TextUtils.isEmpty(key)) {
            throw new RuntimeException("在使用 buildUrl 方法构建通用 REST 接口 Url 时，必须提供一个用于摘要签名用的 key (俗称 MD5 加盐)");
        }

        if (mContext==null){
            throw new NullPointerException("请先初始化 initialize");
        }

        if (isCache) {
            //当前时间
            //L_.e("data 当前时间 :" + new Date().toString());
            if (TextUtils.isEmpty(getString(mContext,"data"))) {
                saveString(mContext,"data", new Date().toString());
            }
            //L_.e("data 缓存时间:" + new Date(SpUtils.getInstance().getString("data")).toString());
            if (DateUtils.betweenTime(new Date(), new Date(getString(mContext,"data")))) {
                saveString(mContext,"data", new Date().toString());
                //L_.e("SpUtils  过期后重设置:" +new Date().toString());
                parameters.put("_t", Convert.dateToStr(new Date(getString(mContext,"data")), "yyyy-MM-dd HH:mm:ss", "1970-01-01 00:00:00"));
            } else {
                //L_.e("SpUtils  缓存请求:" +new Date(SpUtils.getInstance().getString("data")).toString());
                parameters.put("_t", Convert.dateToStr(new Date(getString(mContext,"data")), "yyyy-MM-dd HH:mm:ss", "1970-01-01 00:00:00"));
            }

        } else {
            parameters.put("_t", Convert.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss", "1970-01-01 00:00:00"));
        }

        //new Date("Wed Jul 26 12:10:26 GMT+08:00 2017")
        List parameterNames = new ArrayList(parameters.keySet());
        Collections.sort(parameterNames);

        if ((!urlBase.endsWith("?")) && (!urlBase.endsWith("&"))) {
            urlBase = urlBase + (!urlBase.contains("?") ? "?" : "&");
        }
        String signData = "";
        String urlParam = "";
        for (int i = 0; i < parameters.size(); i++) {
            String _key = (String) parameterNames.get(i);
            String _value = parameters.get(_key);

            signData = signData + _key + "=" + _value;
            try {
                urlParam = urlParam + _key + "=" + URLEncoder.encode(_value, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (i < parameters.size() - 1) {
                signData = signData + "&";
                urlParam = urlParam + "&";
            }
        }
        try {
            urlBase = urlBase + "_t=" + URLEncoder.encode(parameters.get("_t"), "utf-8") + "&_p="
                    + Encrypt.encrypt3DES(urlParam, key) + "&_s="
                    + Encrypt.MD5(String.valueOf(signData) + key, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return urlBase;
    }


    public static void saveString(Context context, String key, String value){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, null);
    }
}
