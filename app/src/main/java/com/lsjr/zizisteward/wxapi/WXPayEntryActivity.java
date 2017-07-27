package com.lsjr.zizisteward.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.ymz.baselibrary.utils.L_;
import com.ymz.baselibrary.utils.T_;

import static com.lsjr.zizisteward.MyApplication.wxApi;

/**
 * Created by Administrator on 2017/5/22.
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        wxApi.handleIntent(getIntent(), this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        wxApi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        int code = baseResp.errCode;
            String result = null;
            switch (code) {
                case BaseResp.ErrCode.ERR_OK:
                    result = "支付成功";
                    break;
                case BaseResp.ErrCode.ERR_COMM:
                    result = "支付失败";
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    result = "取消支付";
                    break;
                default:
                    result = "未知原因支付失败";
                    break;
            }
            T_.showToastReal(result);
            L_.e(result);
            finish();
    }
}
