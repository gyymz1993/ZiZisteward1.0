package com.lsjr.zizisteward.http;

import com.lsjr.base.SubscriberCallBack;
import com.lsjr.net.DcodeService;
import com.ymz.baselibrary.utils.L_;

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


    public static HttpUtils httpUtils;

    private HttpUtils() {

    }

    public static HttpUtils getInstance() {
        if (httpUtils == null) {
            synchronized (HttpUtils.class) {
                httpUtils = new HttpUtils();
            }
        }
        return httpUtils;
    }

    private CompositeSubscription mCompositeSubscription;

    public void loadDataForNet(Observable observable, Subscriber subscriber) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber));
    }


    public void loadDataForNet(final Map map, final SubscriberCallBack subscriberCallBack) {
        loadDataForNet(DcodeService.getServiceData(map), subscriberCallBack);
    }


    public void loadDataForCache(final Map map, final SubscriberCallBack subscriberCallBack) {
        loadDataForNet(DcodeService.getCacheServiceData(map), subscriberCallBack);
    }
}
