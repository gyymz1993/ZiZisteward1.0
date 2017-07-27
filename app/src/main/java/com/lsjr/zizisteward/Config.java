package com.lsjr.zizisteward;

import com.lsjr.zizisteward.utils.EncryptUtils;

/**
 * Created by admin on 2017/5/16.
 */

public interface Config {
    String USER_ID= EncryptUtils.addSign(Long.valueOf("77"), "u");
    String login_PhoneNumber="15071362669";
    String login_pwd="555555";

    // 自己微信应用的 appId
    String WX_APP_ID = "wx388b90f68846eb73";
    // 自己微信应用的 appSecret
    String WX_SECRET = "ab78f012fd5b2ce002cd1b3fe209265b";
    String WX_CODE = "";


    String PRODUCT_ID="PRODUCT_ID";

    String ORDERUTL="ORDERUTL";

    String H5CONTRL="control";

    /*朋友列表KEY*/
    String SP_ADRESS_BOOK_KEY="SP_ADRESS_BOOK_KEY";

}
