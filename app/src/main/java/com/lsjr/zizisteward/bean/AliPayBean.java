package com.lsjr.zizisteward.bean;

import android.text.TextUtils;

import java.util.Map;

/**
 * Created by admin on 2017/5/24.
 */

public class AliPayBean {


    /**
     * error : 1
     * msg : 订单信息反馈成功！
     * alipayValue : alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2017050207078320&biz_content=%7B%22body%22%3A%22GUCCI%E6%9C%AA%E6%9D%A5%E5%8D%B0%E8%8A%B1%E7%BA%AF%E6%A3%89T%E6%81%A4%22%2C%22out_trade_no%22%3A%2220170524175345282%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22GUCCI%E6%9C%AA%E6%9D%A5%E5%8D%B0%E8%8A%B1%E7%BA%AF%E6%A3%89T%E6%81%A4%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%222625.0%22%7D&charset=utf-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2F61.183.182.34%3A8888%2Ffront%2Fhome%2FBackAlipayResponse&sign=U85jr2GaroVmt7cY11y0X0VQhTPlWn2277BfsoUxw%2BryX6mz6I2Sr0NWPyQ35o6Y3EbTu56GY9%2B3IMp3BFxZ1LQ5vu5jOV%2BYSgz1UZQOokWSE5XU0qUxo8VTtDW8WajIcIdB1F%2BnIILjGM04y9GNg6IYgitxB2yIYIob6Fa7dgdK2gDJNorjWzXYMkvgbZCh5g1KnL5QxZJbL9BIbTmqbZwS%2B8EyFQXHXwST1cOr4GsKA%2FgRWgdWwEbCpw5KDEJi2Dn9DSuqMjmUSwDkh7fW26u3nqYrEaNNoiig6eMnp%2BeZ4sVrrmUYkuIgv4YiDu1EaXnUuqoNwZkrvnhG6xNr0Q%3D%3D&sign_type=RSA2&timestamp=2017-05-24+17%3A53%3A59&version=1.0&sign=U85jr2GaroVmt7cY11y0X0VQhTPlWn2277BfsoUxw%2BryX6mz6I2Sr0NWPyQ35o6Y3EbTu56GY9%2B3IMp3BFxZ1LQ5vu5jOV%2BYSgz1UZQOokWSE5XU0qUxo8VTtDW8WajIcIdB1F%2BnIILjGM04y9GNg6IYgitxB2yIYIob6Fa7dgdK2gDJNorjWzXYMkvgbZCh5g1KnL5QxZJbL9BIbTmqbZwS%2B8EyFQXHXwST1cOr4GsKA%2FgRWgdWwEbCpw5KDEJi2Dn9DSuqMjmUSwDkh7fW26u3nqYrEaNNoiig6eMnp%2BeZ4sVrrmUYkuIgv4YiDu1EaXnUuqoNwZkrvnhG6xNr0Q%3D%3D
     */

    private String error;
    private String msg;
    private String alipayValue;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAlipayValue() {
        return alipayValue;
    }

    public void setAlipayValue(String alipayValue) {
        this.alipayValue = alipayValue;
    }

    public static class PayResult {
        private String resultStatus;
        private String result;
        private String memo;

        public PayResult(Map<String, String> rawResult) {
            if (rawResult == null) {
                return;
            }

            for (String key : rawResult.keySet()) {
                if (TextUtils.equals(key, "resultStatus")) {
                    resultStatus = rawResult.get(key);
                } else if (TextUtils.equals(key, "result")) {
                    result = rawResult.get(key);
                } else if (TextUtils.equals(key, "memo")) {
                    memo = rawResult.get(key);
                }
            }
        }

        @Override
        public String toString() {
            return "resultStatus={" + resultStatus + "};memo={" + memo
                    + "};result={" + result + "}";
        }

        /**
         * @return the resultStatus
         */
        public String getResultStatus() {
            return resultStatus;
        }

        /**
         * @return the memo
         */
        public String getMemo() {
            return memo;
        }

        /**
         * @return the result
         */
        public String getResult() {
            return result;
        }
    }
}
