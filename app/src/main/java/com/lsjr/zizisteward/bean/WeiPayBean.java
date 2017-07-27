package com.lsjr.zizisteward.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by admin on 2017/5/24.
 */

public class WeiPayBean {

    /**
     * error : 1
     * msg : 订单信息反馈成功！
     * alipayValue : [{"sign":"82F74906B91F948B22895331F1814903","timestamp":"1495618445","partnerid":"1470162702","noncestr":"9db70d5ff8b144049020b9cf097a6995","prepayid":"wx20170524173435500d1c74aa0239051690","package":"Sign=WXPay","appid":"wx388b90f68846eb73"}]
     */

    private String error;
    private String msg;
    private List<AlipayValueBean> alipayValue;

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

    public List<AlipayValueBean> getAlipayValue() {
        return alipayValue;
    }

    public void setAlipayValue(List<AlipayValueBean> alipayValue) {
        this.alipayValue = alipayValue;
    }

    public static class AlipayValueBean {
        /**
         * sign : 82F74906B91F948B22895331F1814903
         * timestamp : 1495618445
         * partnerid : 1470162702
         * noncestr : 9db70d5ff8b144049020b9cf097a6995
         * prepayid : wx20170524173435500d1c74aa0239051690
         * package : Sign=WXPay
         * appid : wx388b90f68846eb73
         */

        private String sign;
        private String timestamp;
        private String partnerid;
        private String noncestr;
        private String prepayid;
        @SerializedName("package")
        private String packageX;
        private String appid;

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }
    }
}
