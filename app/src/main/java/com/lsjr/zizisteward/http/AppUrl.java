package com.lsjr.zizisteward.http;

/**
 * Created by admin on 2017/5/4.
 */

public interface AppUrl {

    String URLKEY="TNNYF17DbevNyxVv";
    /* 正式服务器 */
    String Http = "https://app.zizi.com.cn";

    /*开发服务器*/
   // String Http = "https://dev.zizi.com.cn";

    String baseUrl = Http + "/app/";

    String HOST = Http + "/app/services?";

    //  微信授权
    String GET_REQUEST_ACCESS_TOKEN =
            "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    String GET_REQUEST_USER_INFO =
            "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
}
