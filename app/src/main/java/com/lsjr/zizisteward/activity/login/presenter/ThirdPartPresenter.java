package com.lsjr.zizisteward.activity.login.presenter;

import android.content.Context;

import com.lsjr.zizisteward.activity.login.view.IThirdPartView;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.ymz.baselibrary.mvp.BasePresenter;
import com.ymz.baselibrary.utils.T_;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;

import static com.lsjr.zizisteward.MyApplication.wxApi;

/**
 * Created by admin on 2017/5/22.
 */

public class ThirdPartPresenter extends BasePresenter<IThirdPartView> {

    public ThirdPartPresenter(IThirdPartView mvpView) {
        super(mvpView);
    }

    public void weiChatLogin() {
        if (wxApi != null && wxApi.isWXAppInstalled()) {
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_demo";
            wxApi.sendReq(req);
        } else {
            T_.showToastReal("用户未安装微信");
        }
    }


    /**
     * 新浪微博授权、获取用户信息页面
     */
    public void sinaLogin(Context context) {
        //初始化新浪平台
        Platform pf = ShareSDK.getPlatform(context, SinaWeibo.NAME);
        pf.SSOSetting(true);
        //设置监听
        pf.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                T_.showToastReal("授权成功 ");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                T_.showToastReal("授权失败 ");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                T_.showToastReal("授权取消 ");

                //退出登陆
                //Platform mPf = ShareSDK.getPlatform(MainActivity.this, SinaWeibo.NAME);
            }
        });
        //获取登陆用户的信息，如果没有授权，会先授权，然后获取用户信息
        pf.authorize();
    }

    /**
     * 实现QQ第三方登录
     */
//    public void qqlogin(Activity activity) {
//        mTencent.login(activity, "all", new IUiListener() {
//            @Override
//            public void onComplete(Object response) {
//                T_.showToastReal("授权成功");
//                JSONObject obj = (JSONObject) response;
//                try {
//                    String openID = obj.getString("openid");
//                    String accessToken = obj.getString("access_token");
//                    String expires = obj.getString("expires_in");
//                    mTencent.setOpenId(openID);
//                    mTencent.setAccessToken(accessToken, expires);
//                    QQToken qqToken = mTencent.getQQToken();
////                    mUserInfo = new UserInfo(getApplicationContext(),qqToken);
////                    mUserInfo.getUserInfo(new IUiListener() {
////                        @Override
////                        public void onComplete(Object response) {
////                            Log.e(TAG,"登录成功"+response.toString());
////                        }
////
////                        @Override
////                        public void onError(UiError uiError) {
////                            Log.e(TAG,"登录失败"+uiError.toString());
////                        }
////
////                        @Override
////                        public void onCancel() {
////                            Log.e(TAG,"登录取消");
////
////                        }
////                    });
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onError(UiError uiError) {
//                T_.showToastReal("授权失败 ");
//            }
//
//            @Override
//            public void onCancel() {
//                T_.showToastReal("授权取消 ");
//            }
//        });
//
//    }


}
