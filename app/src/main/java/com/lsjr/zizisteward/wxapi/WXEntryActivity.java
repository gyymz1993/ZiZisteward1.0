package com.lsjr.zizisteward.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.lsjr.zizisteward.Config;
import com.lsjr.zizisteward.http.AppUrl;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.ymz.baselibrary.utils.L_;
import com.ymz.baselibrary.utils.T_;


import static com.lsjr.zizisteward.MyApplication.wxApi;
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wxApi.handleIntent(getIntent(), this);
    }

    public void onReq(BaseReq req) {
        finish();
    }

    public void onResp(BaseResp resp) {
        L_.e("resp.errCode  :"+resp.errCode);
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                SendAuth.Resp sendResp = (SendAuth.Resp) resp;
                //AppsActivity.WX_CODE = sendResp.code;
                T_.showToastReal(sendResp.code);
                Toast.makeText(this, "成功!", Toast.LENGTH_LONG).show();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Toast.makeText(this, "取消!", Toast.LENGTH_LONG).show();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                Toast.makeText(this, "被拒绝", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(this, "失败!", Toast.LENGTH_LONG).show();
                break;
        }
    }

    public void getToken(){

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        wxApi.handleIntent(intent, this);
        finish();
    }


    private static String getTokenRequest(String code) {
        String tokenRequest = AppUrl.GET_REQUEST_ACCESS_TOKEN.replace("APPID", Config.WX_APP_ID).
                replace("SECRET", Config.WX_SECRET).
                replace("CODE", code);
        return tokenRequest;
    }

    public static String getUserInfoUrl(String access_token, String openid) {
        String userInfo = AppUrl.GET_REQUEST_USER_INFO.replace("ACCESS_TOKEN", access_token).
                replace("OPENID", openid);
        return userInfo;
    }
}
