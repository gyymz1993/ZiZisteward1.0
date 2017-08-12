package com.lsjr.callback;

import android.util.Log;

import com.google.gson.stream.MalformedJsonException;
import com.lsjr.exception.ApiException;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.exceptions.CompositeException;

public abstract class HttpSubscriber extends BaseCallBack<String> {

    private OnTimeOutListener onTimeOutListener;

    public void setOnTimeOutListener(OnTimeOutListener onTimeOutListener) {
        this.onTimeOutListener = onTimeOutListener;
    }

    @Override
    public void onNext(String response) {
        Log.e("HSubscriberCallBack", "网络请求成功" + response);
        response = response.replace("null", "\"\"");
        try {
            JSONObject jsonObject = new JSONObject(response);
            String ERROR = "error";
            if (!jsonObject.isNull(ERROR)) {
                String error = jsonObject.getString(ERROR);
                String MSG = "msg";
                String msg = jsonObject.getString(MSG);
                if (error.equals("1")) {
                    if (response != null) {
                        onSuccess(response);
                    }
                } else {
                    onFailure(msg);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            if (e.getMessage().contains("Value 访问超时")) {
                if (onTimeOutListener != null) {
                    onTimeOutListener.onTimeOut(true);
                }
            }else {
                onError(e);
            }

        }
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
                } else if (throwable instanceof MalformedJsonException) {
                    onXError(ApiException.MALFORMED_JSON_EXCEPTION);
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

    protected abstract void onFailure(String msg);

    protected abstract void onSuccess(String response);


}