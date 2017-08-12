package com.lsjr.zizi.http;

import com.lsjr.callback.ChatArrayCallBack;
import com.lsjr.callback.ChatObjectCallBack;
import com.lsjr.callback.HttpSubscriber;
import com.lsjr.callback.StringCallBack;
import com.lsjr.net.DcodeService;
import com.lsjr.param.RxHttpParams;

import java.io.File;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/7/21 10:42
 */

public class HttpUtils {
    private static HttpUtils httpUtils;
    private CompositeSubscription mCompositeSubscription;
    private HttpUtils() {}
    public static HttpUtils getInstance() {
        if (httpUtils == null) {
            synchronized (HttpUtils.class) {
                httpUtils = new HttpUtils();
            }
        }
        return httpUtils;
    }

    private void loadDataForNet(Observable observable, Subscriber subscriber) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber));
    }

    public void postServiceData(String baseUrl,final Map map, final ChatObjectCallBack httpSubscriber) {
        loadDataForNet(DcodeService.postServiceData(baseUrl,map), httpSubscriber);
    }


    public void postServiceData(String baseUrl,final Map map, final HttpSubscriber httpSubscriber) {
        loadDataForNet(DcodeService.postServiceData(baseUrl,map), httpSubscriber);
    }



    public void postServiceData(String baseUrl,final Map map, final ChatArrayCallBack httpSubscriber) {
        loadDataForNet(DcodeService.postServiceData(baseUrl,map), httpSubscriber);
    }


    public void uploadFileWithParts(String baseUrl,final Map map, List<File> files,final HttpSubscriber httpSubscriber) {
        loadDataForNet(DcodeService.uploadFilesWithParts(baseUrl,map,files), httpSubscriber);
    }


    public void uploadFileWithParts(String baseUrl,final Map map, List<File> files,final StringCallBack httpSubscriber) {
        loadDataForNet(DcodeService.uploadFilesWithParts(baseUrl,map,files), httpSubscriber);
    }



    public void uploadFileWithBodys(String baseUrl, RxHttpParams params, final HttpSubscriber httpSubscriber) {
        loadDataForNet(DcodeService.uploadFilesWithBodys(baseUrl,params), httpSubscriber);
    }

    public void uploadFileWithParts(String baseUrl, RxHttpParams params, final HttpSubscriber httpSubscriber) {
        loadDataForNet(DcodeService.uploadFilesWithParts(baseUrl,params), httpSubscriber);
    }

}