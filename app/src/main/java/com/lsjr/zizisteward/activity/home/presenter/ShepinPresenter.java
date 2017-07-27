package com.lsjr.zizisteward.activity.home.presenter;

import com.lsjr.base.SubscriberCallBack;
import com.lsjr.net.DcodeService;
import com.lsjr.zizisteward.activity.home.view.ShepinView;
import com.lsjr.zizisteward.http.HttpUtils;
import com.ymz.baselibrary.mvp.BasePresenter;
import com.ymz.baselibrary.utils.L_;

import java.util.Map;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/7/12 15:31
 */

public class ShepinPresenter extends BasePresenter<ShepinView> {
    public ShepinPresenter(ShepinView mvpView) {
        super(mvpView);
    }

    public void loadDataforNet1(Map map){
        addSubscription(DcodeService.getServiceData(map), new SubscriberCallBack() {

            @Override
            protected void onSuccess(String response) {
                L_.e("getHomePager"+response);
                mvpView.onLoadDataSucceed(response);
            }


            @Override
            protected void onError(Exception e) {
                L_.e("getHomePager Exception  无网络页面");
                mvpView.noData();
            }

            @Override
            protected void onFailure(String response) {
                L_.e("getHomePager   无网络页面"+response);
                mvpView.noData();
            }

        });
    }


    public void loadDataforNet(Map map){
        HttpUtils.getInstance().loadDataForCache(map, new SubscriberCallBack() {

            @Override
            protected void onSuccess(String response) {
                //L_.e("getHomePager"+response);
                mvpView.onLoadDataSucceed(response);
            }


            @Override
            protected void onError(Exception e) {
               // L_.e("getHomePager Exception  无网络页面");
                mvpView.noData();
            }

            @Override
            protected void onFailure(String response) {
               // L_.e("getHomePager   无网络页面"+response);
                mvpView.noData();
            }

        });
    }
}
