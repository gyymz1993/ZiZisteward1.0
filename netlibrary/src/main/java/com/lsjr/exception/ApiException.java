package com.lsjr.exception;

public class ApiException extends RuntimeException {
    public static final String CONNECT_EXCEPTION = "网络连接异常，请检查您的网络状态";
    public static final String SOCKET_TIMEOUT_EXCEPTION = "网络连接超时，请检查您的网络状态，稍后重试";
    public static final String MALFORMED_JSON_EXCEPTION = "数据解析错误";
    public static final String ONTIMEOUT = "网络请求成功访问超时";
    public ApiException(int resultCode, String msg) {
        this(getApiExceptionMessage(resultCode, msg));
    }

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * @param code
     * @return
     */
    private static String getApiExceptionMessage(int code, String msg) {
        String message;
        switch (code) {
            default:
                message = code + "#" + msg;
        }
        return message;
    }
}

