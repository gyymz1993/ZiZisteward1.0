package com.lsjr.callback;

public interface OnResultCallBack<T> {

    void onSuccess(T t);

    void onError(String errorMsg);

    void onFailure(T t);
}
