package com.ymz.baselibrary.mvp;

import com.ymz.baselibrary.inter.Presenter;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
  * @author: gyymz1993
  * 创建时间：2017/5/3 21:53
  * @version
  *
 **/
public class BasePresenter<V> implements Presenter<V> {

    private CompositeSubscription mCompositeSubscription;
    public V mvpView;

    @Override
    public void attachView(V mvpView) {
        this.mvpView = mvpView;
    }

    public BasePresenter(V mvpView)
    {
        attachView(mvpView);
    }


    @Override
    public void detachView() {
        this.mvpView = null;
        onUnsubscribe();
    }

    public void addSubscription(Observable observable, Subscriber subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber));
    }

    //RXjava取消注册，以避免内存泄露
    public void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

}
