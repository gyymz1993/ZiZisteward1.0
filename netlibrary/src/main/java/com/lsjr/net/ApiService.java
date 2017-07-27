package com.lsjr.net;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @version 需要加密目前这种方法可行
 * @author: gyymz1993
 * 创建时间：2017/5/3 22:46
 **/
public interface ApiService {
    @GET
    Observable<String> getData(@Url String url);

    @POST
    Observable<String> postData(@Url String url);

    @GET("{url}")
    Observable<String> getBody(@Path("url") String url, @PartMap Map<String, String> map);

    @POST("{url}")
    Observable<ResponseBody> postBody(@Path("url") String url, @Body Object object);

    /**
     * 多文件多参数上传
     **/
    @Multipart
    @POST("uploadSight")
    Observable<String> uploadFiles(@Part List<MultipartBody.Part> partList);

}
