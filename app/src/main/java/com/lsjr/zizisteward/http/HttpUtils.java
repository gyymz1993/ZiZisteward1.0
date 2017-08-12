package com.lsjr.zizisteward.http;

import com.lsjr.callback.HttpSubscriber;
import com.lsjr.callback.OnTimeOutListener;
import com.lsjr.net.DcodeService;
import com.lsjr.zizisteward.http.callback.SubscriberCallBack;
import com.ymz.baselibrary.utils.L_;

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

    public void loadDataForNet(final Map map, final SubscriberCallBack subscriberCallBack) {
        loadDataForNet(DcodeService.getServiceData(map), subscriberCallBack);
    }

    public void loadDataForNet(final Map map, final HttpSubscriber httpSubscriber) {
        loadDataForNet(DcodeService.getServiceData(map), httpSubscriber);
    }


    public void postFile(String baseUrl, final Map map, List<File> files, final HttpSubscriber httpSubscriber) {
        loadDataForNet(DcodeService.uploadFilesWithParts(baseUrl,map,files), httpSubscriber);
    }

    public void loadDataForCache(final Map map, final HttpSubscriber httpSubscriber) {
        loadDataForNet(DcodeService.getCacheServiceData(map), httpSubscriber);
        httpSubscriber.setOnTimeOutListener(new OnTimeOutListener() {
            @Override
            public void onTimeOut(boolean isTimeOut) {
                if (isTimeOut) {
                    L_.e("失效   重新请求");
                    loadDataForNet(DcodeService.getServiceTimeOut(map), httpSubscriber);
                }
            }
        });
    }
}
