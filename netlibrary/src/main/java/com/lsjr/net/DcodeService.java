package com.lsjr.net;

import android.annotation.SuppressLint;
import android.content.Context;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.lsjr.param.RequestBodyUtils;
import com.lsjr.param.RxHttpParams;
import com.lsjr.param.UploadProgressRequestBody;
import com.lsjr.utils.DateUtils;
import com.lsjr.utils.UrlUtils;
import com.shove.Convert;
import com.shove.security.Encrypt;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * @version 需要加密目前这种方法可行
 * @author: gyymz1993
 * 创建时间：2017/5/3 22:46
 **/
public class DcodeService {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private static String mCurrentTime;


    public static void initialize(Context context) {
        mContext = context;
    }

    private static ApiService getApiService() {
        if (mContext == null) {
            throw new NullPointerException("请先初始化 initialize");
        }
        return AppClient.getApiService(mContext);
    }

    /* get网络请求入口*/
    public static Observable<String> getServiceData(Map map) {
        if (TextUtils.isEmpty(BaseUrl.bastUrl)) {
            throw new NullPointerException("请设置BaseUrl");
        }
        UrlUtils.spliceGetUrl(BaseUrl.bastUrl, map);
        String baseUrl = encryptUrl(false, BaseUrl.bastUrl, BaseUrl.URLKEY, map);
        return getApiService().getData(baseUrl);
    }


    /* get网络请求入口*/
    public static Observable<String> getCacheServiceData(Map map) {
        if (TextUtils.isEmpty(BaseUrl.bastUrl)) {
            throw new NullPointerException("请设置BaseUrl");
        }
        //UrlUtils.spliceGetUrl(BaseUrl.bastUrl, map);
        String baseUrl = encryptUrl(true, BaseUrl.bastUrl, BaseUrl.URLKEY, map);
        return getApiService().getData(baseUrl);
    }

    /* get网络请求入口*/
    public static Observable<String> getServiceTimeOut(Map map) {
        if (TextUtils.isEmpty(BaseUrl.bastUrl)) {
            throw new NullPointerException("请设置BaseUrl");
        }
        saveString(mContext, new Date().toString());
        UrlUtils.spliceGetUrl(BaseUrl.bastUrl, map);
        String baseUrl = encryptUrl(true, BaseUrl.bastUrl, BaseUrl.URLKEY, map);
        return getApiService().getData(baseUrl);
    }


    /* post网络请求入口*/
    public static Observable<String> postServiceData(Map map) {
        if (TextUtils.isEmpty(BaseUrl.bastUrl)) {
            throw new NullPointerException("请设置BaseUrl");
        }
        //UrlUtils.encodesParameters(BaseUrl.bastUrl, map);
        String baseUrl = encryptUrl(false, BaseUrl.bastUrl, BaseUrl.URLKEY, map);
        return getApiService().postData(baseUrl);
    }

    /* post网络请求入口*/
    public static Observable<String> postServiceData(String baseUrl,Map map) {
        if (TextUtils.isEmpty(baseUrl)) {
            throw new NullPointerException("请设置BaseUrl");
        }
        String url=UrlUtils.encodesParameters(baseUrl, map);
        //Log.e("UrlUtils----post>","没有加密的URL  post:"+baseUrl+url);
        return getApiService().postData(url);
    }


    /*多图片上传*/
    public static Observable<String> uploadFilesWithParts(String baseUrl,Map<String, String> parameters, List<File> fileList) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);//表单类型;
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String _key = entry.getKey();
            String _value = entry.getValue();
            builder.addFormDataPart(_key, _value);
            Log.e("postFile_key---------->",_key+"-----------:L"+_value);
        }
        String url=UrlUtils.encodesParameters(baseUrl, parameters);
        for (int i = 0; i < fileList.size(); i++) {
            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), fileList.get(i));
            builder.addFormDataPart("file1", fileList.get(i).getName(), imageBody);//"shareImg"+i 后台接收图片流的参数名
        }
        List<MultipartBody.Part> parts = builder.build().parts();
        return getApiService().uploadFiles(baseUrl,parts);
    }

    /*多图片上传*/
    public static Observable<ResponseBody> uploadFilesWithBodys(String url,RxHttpParams params){
        if (TextUtils.isEmpty(BaseUrl.bastUrl)) {
            throw new NullPointerException("请设置BaseUrl");
        }
        Map<String, RequestBody> mBodyMap = new HashMap<>();
        //拼接参数键值对
        for (Map.Entry<String, String> mapEntry : params.urlParamsMap.entrySet()) {
            RequestBody body = RequestBody.create(MediaType.parse("text/plain"), mapEntry.getValue());
            mBodyMap.put(mapEntry.getKey(), body);
        }
        //拼接文件
        for (Map.Entry<String, List<RxHttpParams.FileWrapper>> entry : params.fileParamsMap.entrySet()) {
            List<RxHttpParams.FileWrapper> fileValues = entry.getValue();
            for (RxHttpParams.FileWrapper fileWrapper : fileValues) {
                RequestBody requestBody = getRequestBody(fileWrapper);
                UploadProgressRequestBody uploadProgressRequestBody = new UploadProgressRequestBody(requestBody, fileWrapper.responseCallBack);
                mBodyMap.put(entry.getKey(), uploadProgressRequestBody);
            }
        }
        return getApiService().uploadFiles(url, mBodyMap);
    }


    /*
    *  带上传进度
    *
    * */
    public static Observable<String> uploadFilesWithParts(String url,RxHttpParams params) {
        List<MultipartBody.Part> parts = new ArrayList<>();
        //拼接参数键值对
        for (Map.Entry<String, String> mapEntry : params.urlParamsMap.entrySet()) {
            parts.add(MultipartBody.Part.createFormData(mapEntry.getKey(), mapEntry.getValue()));
        }
        //拼接文件
        for (Map.Entry<String, List<RxHttpParams.FileWrapper>> entry : params.fileParamsMap.entrySet()) {
            List<RxHttpParams.FileWrapper> fileValues = entry.getValue();
            for (RxHttpParams.FileWrapper fileWrapper : fileValues) {
                MultipartBody.Part part = addFile(entry.getKey(), fileWrapper);
                parts.add(part);
            }
        }
        return getApiService().uploadFiles(url,parts);
    }


    /*
  *  带上传进度
  *
  * */
    public static Observable<ResponseBody> downloadFile(String url) {
        return getApiService().downloadFile(url);
    }


    //文件方式
    private static MultipartBody.Part addFile(String key, RxHttpParams.FileWrapper fileWrapper) {
        //MediaType.parse("application/octet-stream", file)
        RequestBody requestBody = getRequestBody(fileWrapper);
      //  Utils.checkNotNull(requestBody, "requestBody==null fileWrapper.file must is File/InputStream/byte[]");
        //包装RequestBody，在其内部实现上传进度监听
        if (fileWrapper.responseCallBack != null) {
            UploadProgressRequestBody uploadProgressRequestBody = new UploadProgressRequestBody(requestBody, fileWrapper.responseCallBack);
            MultipartBody.Part part = MultipartBody.Part.createFormData(key, fileWrapper.fileName, uploadProgressRequestBody);
            return part;
        } else {
            MultipartBody.Part part = MultipartBody.Part.createFormData(key, fileWrapper.fileName, requestBody);
            return part;
        }
    }


    private static RequestBody getRequestBody(RxHttpParams.FileWrapper fileWrapper) {
        RequestBody requestBody = null;
        if (fileWrapper.file instanceof File) {
            requestBody = RequestBody.create(fileWrapper.contentType, (File) fileWrapper.file);
        } else if (fileWrapper.file instanceof InputStream) {
            //requestBody = RequestBodyUtils.create(RequestBodyUtils.MEDIA_TYPE_MARKDOWN, (InputStream) fileWrapper.file);
            requestBody = RequestBodyUtils.create(fileWrapper.contentType, (InputStream) fileWrapper.file);
        } else if (fileWrapper.file instanceof byte[]) {
            requestBody = RequestBody.create(fileWrapper.contentType, (byte[]) fileWrapper.file);
        }
        return requestBody;
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

        if (mContext == null) {
            throw new NullPointerException("请先初始化 initialize");
        }

        if (isCache) {
            //当前时间
            //Log.e("data 当前时间 :" , new Date().toString());
            if (TextUtils.isEmpty(getString(mContext))) {
                saveString(mContext, new Date().toString());
            }
            //Log.e("data 缓存时间:" ,new Date(getString(mContext)).toString());
            if (DateUtils.twoDateDistance(new Date(), new Date(getString(mContext)))) {
                mCurrentTime = new Date().toString();
                saveString(mContext, mCurrentTime);
                //Log.e("SpUtils  过期后重设置:" ,mCurrentTime);
            }
           // Log.e("SpUtils  缓存请求:" ,new Date(getString(mContext)).toString());
            parameters.put("_t", Convert.dateToStr(new Date(getString(mContext)), "yyyy-MM-dd HH:mm:ss", "1970-01-01 00:00:00"));
        } else {
            parameters.put("_t", Convert.dateToStr(new Date(getString(mContext)), "yyyy-MM-dd HH:mm:ss", "1970-01-01 00:00:00"));
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



    private static void saveString(Context context, String value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("data", value).commit();
    }

    private static String getString(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("data", null);
    }
}
