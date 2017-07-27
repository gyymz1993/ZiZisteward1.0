package com.lsjr.zizisteward.activity.home.presenter;

import com.lsjr.base.SubscriberCallBack;
import com.lsjr.net.DcodeService;
import com.lsjr.zizisteward.activity.home.view.IHomeView;
import com.ymz.baselibrary.mvp.BasePresenter;
import com.ymz.baselibrary.utils.L_;
import com.ymz.baselibrary.utils.T_;

import java.util.Map;

/**
 * Created by admin on 2017/5/16.
 */

public class HomePresenter  extends BasePresenter<IHomeView> {
    public HomePresenter(IHomeView mvpView) {
        super(mvpView);
    }


    /*环信登陆*/
    public void easeLogin(Map<String,String> map){
        addSubscription(DcodeService.getServiceData(map), new SubscriberCallBack() {
            @Override
            protected void onError(Exception e) {
                T_.showToastReal("请求失败");
            }

            @Override
            protected void onFailure(String response) {

            }

            @Override
            protected void onSuccess(String response) {
                mvpView.easeLoignSucceed(response);
            }


        });
    }

    public void getHomePager(Map map){
        addSubscription(DcodeService.getServiceData(map), new SubscriberCallBack() {

            @Override
            protected void onSuccess(String response) {
                L_.e("getHomePager"+response);
                mvpView.getPageDataSucceed(response);
            }


            @Override
            protected void onError(Exception e) {
                L_.e("getHomePager Exception");
            }

            @Override
            protected void onFailure(String response) {
                L_.e("getHomePager"+response);
                mvpView.getPageDataSucceed(response);
            }

        });
    }
}
