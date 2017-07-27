package com.lsjr.zizisteward.bean;

/**
 * Created by admin on 2017/5/24.
 */

public class ProductDetail {


    /**
     * error : 1
     * productUrl : /productdetails?sid=566&userid=77
     * msg : 查询产品详情成功!
     */

    private String error;
    private String productUrl;
    private String msg;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
