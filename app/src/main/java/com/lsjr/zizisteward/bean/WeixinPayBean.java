package com.lsjr.zizisteward.bean;

import java.util.List;

/**
 * Created by admin on 2017/5/22.
 */

public class WeixinPayBean {


    /**
     * alipayValue : [{"appid":"wx388b90f68846eb73","mch_id":"1470162702","nonce_str":"JwlEeT7UNZlbfAxN","prepay_id":"wx201705221818023317ef812c0226017013","result_code":"SUCCESS","return_code":"SUCCESS","return_msg":"OK","sign":"BE2774AFD573959DEC02DA3F5B6870AA","timestamp":"1495448262","trade_type":"APP"}]
     * error : 1
     * msg : 订单信息反馈成功！
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
         * appid : wx388b90f68846eb73
         * mch_id : 1470162702
         * nonce_str : JwlEeT7UNZlbfAxN
         * prepay_id : wx201705221818023317ef812c0226017013
         * result_code : SUCCESS
         * return_code : SUCCESS
         * return_msg : OK
         * sign : BE2774AFD573959DEC02DA3F5B6870AA
         * timestamp : 1495448262
         * trade_type : APP
         */

        private String appid;
        private String mch_id;
        private String nonce_str;
        private String prepay_id;
        private String result_code;
        private String return_code;
        private String return_msg;
        private String sign;
        private String timestamp;
        private String trade_type;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getMch_id() {
            return mch_id;
        }

        public void setMch_id(String mch_id) {
            this.mch_id = mch_id;
        }

        public String getNonce_str() {
            return nonce_str;
        }

        public void setNonce_str(String nonce_str) {
            this.nonce_str = nonce_str;
        }

        public String getPrepay_id() {
            return prepay_id;
        }

        public void setPrepay_id(String prepay_id) {
            this.prepay_id = prepay_id;
        }

        public String getResult_code() {
            return result_code;
        }

        public void setResult_code(String result_code) {
            this.result_code = result_code;
        }

        public String getReturn_code() {
            return return_code;
        }

        public void setReturn_code(String return_code) {
            this.return_code = return_code;
        }

        public String getReturn_msg() {
            return return_msg;
        }

        public void setReturn_msg(String return_msg) {
            this.return_msg = return_msg;
        }

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

        public String getTrade_type() {
            return trade_type;
        }

        public void setTrade_type(String trade_type) {
            this.trade_type = trade_type;
        }
    }
}
