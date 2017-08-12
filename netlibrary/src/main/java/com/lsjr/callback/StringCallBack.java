package com.lsjr.callback;

import android.util.Log;

import com.lsjr.exception.ApiException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.exceptions.CompositeException;

public abstract class StringCallBack extends BaseCallBack<String> {

    @Override
    public void onNext(String response) {
        Log.e("StringCallBack", "网络请求成功" + response);
        response = response.replace("null", "\"\"");
        onSuccess(response);
    }


    @Override
    public void onError(final Throwable e) {
        if (e instanceof CompositeException) {
            CompositeException compositeE = (CompositeException) e;
            for (Throwable throwable : compositeE.getExceptions()) {
                if (throwable instanceof SocketTimeoutException) {
                    onXError(ApiException.SOCKET_TIMEOUT_EXCEPTION);
                } else if (throwable instanceof ConnectException) {
                    onXError(ApiException.CONNECT_EXCEPTION);
                } else if (throwable instanceof UnknownHostException) {
                    onXError(ApiException.CONNECT_EXCEPTION);
                } else {
                    onXError(ApiException.CONNECT_EXCEPTION);
                }
            }
        } else {
            onXError(ApiException.CONNECT_EXCEPTION);
        }
        e.printStackTrace();

    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCompleted() {
    }

    protected abstract void onXError(String exception);


    protected abstract void onSuccess(String response);


}