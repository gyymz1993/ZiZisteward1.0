package com.lsjr.net;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

class AppClient {

    private static Retrofit mRetrofit;
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    private static Retrofit retrofit() {
        if (mRetrofit == null) {
            Gson gson = new GsonBuilder().setDateFormat(DateUtils.DB_DATA_FORMAT).create();
            //使用 gson coverter，统一日期请求格式
            String mBaseUrl = "https://app.zizi.com.cn/app/";
          //  mBaseUrl="http://192.168.100.10:8081/";
            if (BaseUrl.getBastUrl() == null) {
                throw new NullPointerException("请设置BaseUrl");
            }
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(mBaseUrl)
                    .client(getOkHttpClient())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    //自定义解析增加加密方式`
                    //一定要在gsonConverter前面,否则gson会拦截所有的解析方式
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return mRetrofit;
        }
        return mRetrofit;
    }


    /*
    *请求路径打印
    */
     private static final int DEFAULT_MILLISECONDS = 60000;             //默认的超时时间
    private static OkHttpClient getOkHttpClient() {
        //定制OkHttp
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        //httpClientBuilder.connectTimeout(10, TimeUnit.SECONDS)
        httpClientBuilder.connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS);
        httpClientBuilder.addInterceptor(new LoggingInterceptor());
        httpClientBuilder.addNetworkInterceptor(new NetworkInterceptor());
        /*设置缓存*/
        File cacheFile = new File(mContext.getCacheDir(), "[缓存目录]");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb
        httpClientBuilder.cache(cache);
        return httpClientBuilder.build();
    }


    private static class NetworkInterceptor implements Interceptor {
        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @Override
        public Response intercept(Chain chain) throws IOException {
            //通过 CacheControl 控制缓存数据
            CacheControl.Builder cacheBuilder = new CacheControl.Builder();
            cacheBuilder.maxAge(0, TimeUnit.MINUTES);//这个是控制缓存的最大生命时间
            cacheBuilder.maxStale(30, TimeUnit.MINUTES);//这个是控制缓存的过时时间
            CacheControl cacheControl = cacheBuilder.build();
            //设置拦截器
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            //header配置
            //builder.addHeader("Content-Type", "application/json");
            Response originalResponse = chain.proceed(request);
            if (!NetUtils.isConnected(mContext)) {
                builder.cacheControl(cacheControl);
            }
            Response.Builder responsibility = originalResponse.newBuilder();
            responsibility.removeHeader("Pragma");
            if (NetUtils.isConnected(mContext)) {
                int maxAge = 0;//read from cache
                responsibility.header("Cache-Control", "public ,max-age=" + maxAge);
            } else {
                int maxStale = 60 * 30;
                responsibility.header("Cache-Control", "poublic, only-if-cached, max-stale=" + maxStale);
            }
            return responsibility.build();
        }
    }


    private static class LoggingInterceptor implements Interceptor {
        @SuppressLint("DefaultLocale")
        @Override
        public Response intercept(Chain chain) throws IOException {
            //这个chain里面包含了request和response，所以你要什么都可以从这里拿
            Request request = chain.request();
            long t1 = System.nanoTime();//请求发起的时间
//            L_.i(String.format("发送请求 %s on %s%n%s",
//                    request.url(), chain.connection(), request.headers()));
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();//收到响应的时间
            ResponseBody responseBody = response.peekBody(1024 * 1024);
            Log.e("AppClient", String.format("接收响应: [%s] %n返回json:【%s】 %.1fms%n%s",
                    response.request().url(),
                    responseBody.string(),
                    (t2 - t1) / 1e6d,
                    response.headers()));
            return response;
        }

    }

    static ApiService getApiService(Context context) {
        mContext = context;
        return retrofit().create(ApiService.class);
    }

}
