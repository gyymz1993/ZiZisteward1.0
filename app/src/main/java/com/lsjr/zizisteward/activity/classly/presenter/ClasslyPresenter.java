package com.lsjr.zizisteward.activity.classly.presenter;

import com.lsjr.zizisteward.http.callback.SubscriberCallBack;
import com.lsjr.net.DcodeService;
import com.lsjr.zizisteward.activity.classly.view.IClasslyView;
import com.ymz.baselibrary.mvp.BasePresenter;
import com.ymz.baselibrary.utils.T_;


import java.util.Map;

/**
 * Created by admin on 2017/5/12.
 */

public class ClasslyPresenter extends BasePresenter<IClasslyView> {
    public ClasslyPresenter(IClasslyView mvpView) {
        super(mvpView);
    }

    public void getClasslyData(Map map){
        addSubscription(DcodeService.getServiceData(map), new SubscriberCallBack() {
            @Override
            protected void onSuccess(String response) {
                T_.showToastReal("请求成功");
                mvpView.onLoadDataSucceed(response);

            }
            @Override
            protected void onError(Exception e) {
                T_.showToastReal("请求错误");
            }

            @Override
            protected void onFailure(String response) {
                T_.showToastReal("请求成功");
                mvpView.onLoadDataSucceed(response);
            }

        });
    }

}
