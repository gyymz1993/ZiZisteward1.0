package com.lsjr.callback;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lsjr.bean.ArrayResult;
import com.lsjr.bean.Result;
import com.lsjr.exception.ApiException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.exceptions.CompositeException;

public abstract class ChatDownCallBack<T> extends BaseCallBack<String> {
    private Class<T> mClazz;
    public ChatDownCallBack(Class<T> clazz) {
        this.mClazz=clazz;
    }
    @Override
    public void onNext(String response) {
        Log.e("ChatObjectCallBack", "网络请求成功" + response);
        ArrayResult<T> result = new ArrayResult<>();
        try {
            response = response.replace("null", "\"\"");
            JSONObject jsonObject = JSON.parseObject(response);
            result.setResultCode(jsonObject.getIntValue(Result.RESULT_CODE));
           // result.setResultMsg(jsonObject.getString(Result.RESULT_MSG));
           if (result.getResultCode() == Result.CODE_SUCCESS){
               String data = jsonObject.getString(Result.DATA);
               Log.e("ChatArrayCallBack", "成功返回data---->:" + data );
               if (!TextUtils.isEmpty(data)) {
                   result.setData(JSON.parseArray(data, mClazz));
               }
           }
            onSuccess(result);
        } catch (Exception e) {
            e.printStackTrace();
            onXError(e.getMessage());
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
                }else {
                    onXError(ApiException.CONNECT_EXCEPTION);
                }
            }
        } else {
            onXError(ApiException.CONNECT_EXCEPTION);
        }

    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCompleted() {
    }

    protected abstract void onXError(String exception);

    protected abstract void onSuccess(ArrayResult<T> result);


}