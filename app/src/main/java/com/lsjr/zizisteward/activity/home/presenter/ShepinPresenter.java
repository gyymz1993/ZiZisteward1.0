package com.lsjr.zizisteward.activity.home.presenter;

import com.lsjr.callback.HttpSubscriber;
import com.lsjr.zizisteward.activity.home.view.ShepinView;
import com.lsjr.zizisteward.http.HttpUtils;
import com.ymz.baselibrary.mvp.BasePresenter;
import com.ymz.baselibrary.utils.L_;
import com.ymz.baselibrary.utils.T_;

import java.util.Map;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/7/12 15:31
 */

public class ShepinPresenter extends BasePresenter<ShepinView> {
    public ShepinPresenter(ShepinView mvpView) {
        super(mvpView);
    }


    public void loadDataforNet(Map map) {
        HttpUtils.getInstance().loadDataForCache(map, new HttpSubscriber() {
            @Override
            protected void onXError(String exception) {
                L_.e("getHomePager   无网络页面" + exception);
                T_.showToastReal(exception);
                mvpView.noData();
            }

            @Override
            protected void onFailure(String response) {
                L_.e("getHomePager   无网络页面" + response);
                T_.showToastReal(response);
                mvpView.noData();
            }

            @Override
            protected void onSuccess(String response) {
                mvpView.onLoadDataSucceed(response);
            }
        });
    }
}
