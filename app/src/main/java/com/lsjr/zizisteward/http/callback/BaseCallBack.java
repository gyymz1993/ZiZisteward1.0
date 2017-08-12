package com.lsjr.zizisteward.http.callback;

import android.os.Handler;
import android.os.Looper;

import rx.Subscriber;

/**
  * @author: gyymz1993
  * 创建时间：2017/5/3 22:27
  * @version
  *
 **/
 abstract class BaseCallBack<T> extends Subscriber<T> {
    public Handler mDelivery;
    @Override
    public void onCompleted() {
    }

    public BaseCallBack() {
        mDelivery = new Handler(Looper.getMainLooper());
    }


}
