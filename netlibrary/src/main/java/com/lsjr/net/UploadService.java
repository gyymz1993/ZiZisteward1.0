package com.lsjr.net;

import android.content.Context;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;


public class UploadService {

    private static Context mContext;
    public static void initialize(Context context) {
        mContext=context;
    }

    private static ApiService getApiService() {
        if (mContext==null){
            throw new NullPointerException("请先初始化 initialize");
        }
        return AppClient.getApiService(mContext);
    }

    /*多图片上传*/
    public static Observable<String> uploadImage(Map<String, String> parameters, List<File> fileList) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);//表单类型;
        for (int i = 0; i < parameters.size(); i++) {
            String _key = parameters.get(i);
            String _value = parameters.get(_key);
            builder.addFormDataPart(_key, _value);
        }
//                .addFormDataPart("user_id", userId)//定义参数key常量类，即参数名
//                .addFormDataPart("content", "content")
//                .addFormDataPart("customTag", "customTag")
//                .addFormDataPart("sightType", "豪车品鉴师")
//                .addFormDataPart("type", "1")
//                .addFormDataPart("imgNumber", "1");//ParamKey.TOKEN 自定义参数key常量类，即参数名
        //多张图片
        for (int i = 0; i < fileList.size(); i++) {
            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), fileList.get(i));
            builder.addFormDataPart("shareImg", fileList.get(i).getName(), imageBody);//"shareImg"+i 后台接收图片流的参数名
        }
        List<MultipartBody.Part> parts = builder.build().parts();
        return getApiService().uploadFiles(parts);
    }

}
