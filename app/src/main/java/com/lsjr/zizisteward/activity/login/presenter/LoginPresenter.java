package com.lsjr.zizisteward.activity.login.presenter;

import com.lsjr.zizisteward.http.callback.SubscriberCallBack;
import com.lsjr.net.DcodeService;
import com.lsjr.zizisteward.activity.login.view.ILoginView;
import com.ymz.baselibrary.mvp.BasePresenter;


import java.util.Map;


public class LoginPresenter extends BasePresenter<ILoginView> {
    public LoginPresenter(ILoginView mvpView) {
        super(mvpView);
    }

      public void getLogin( Map<String,String>  map) {

        addSubscription(DcodeService.getServiceData(map), new SubscriberCallBack() {
            @Override
            protected void onError(Exception e) {
                mvpView.onloginError();
            }

            @Override
            protected void onFailure(String response) {
                mvpView.onloginSuccess(response);
            }

            @Override
            protected void onSuccess(String response) {
                mvpView.onloginSuccess(response);
            }

        });
    }



}
