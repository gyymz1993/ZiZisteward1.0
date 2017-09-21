package com.lsjr.callback;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lsjr.bean.ObjectResult;
import com.lsjr.bean.Result;
import com.lsjr.exception.ApiException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import rx.exceptions.CompositeException;

public abstract class ChatObjectCallBack<T> extends BaseCallBack<String> {
    private Class<T> mClazz;
    public ChatObjectCallBack(Class<T> clazz) {
        this.mClazz=clazz;
    }
    @Override
    public void onNext(String response) {
        //Log.e("ChatObjectCallBack", "网络请求成功" + response);
        ObjectResult<T> result = new ObjectResult<>();
        try {
            response = response.replace("null", "\"\"");
            JSONObject jsonObject = JSON.parseObject(response);
            result.setResultCode(jsonObject.getIntValue(Result.RESULT_CODE));
            result.setResultMsg(jsonObject.getString(Result.RESULT_MSG));

           if (result.getResultCode() == Result.CODE_SUCCESS){
               if (!mClazz.equals(Void.class)) {
                   String data = jsonObject.getString(Result.DATA);
                   Log.e("ChatObjectCallBack", "成功返回data---->:" + data );
                   if (!TextUtils.isEmpty(data)) {
                       if (mClazz.equals(String.class) || mClazz.getSuperclass().equals(Number.class)) {// String
                           // 类型或者基本数据类型（Integer）
                           result.setData(castValue(mClazz, data));
                       } else {
                           result.setData(JSON.parseObject(data, mClazz));
                       }
                   }
               }
               onSuccess(result);
           } else {
             //  result.setResultMsg(jsonObject.getString("resultMsg"));
               //String resultMsg=result.setResultMsg(jsonObject.getString("resultMsg"));
               Log.e("HSubscriberCallBack", "网络请求失败" + result.getResultMsg());
               onXError(result.getResultMsg());
           }

        } catch (Exception e) {
            e.printStackTrace();
            onXError(result.getResultMsg());
        }
    }


    private T castValue(Class<T> clazz, String data) {
        try {
            Constructor<T> constructor = clazz.getConstructor(String.class);
            return constructor.newInstance(data);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
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

    protected abstract void onSuccess(ObjectResult<T> result);


}