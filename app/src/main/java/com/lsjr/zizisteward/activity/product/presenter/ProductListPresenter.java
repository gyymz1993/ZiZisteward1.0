package com.lsjr.zizisteward.activity.product.presenter;

import com.lsjr.base.SubscriberCallBack;
import com.lsjr.net.DcodeService;
import com.lsjr.zizisteward.activity.product.view.IProductListView;
import com.ymz.baselibrary.mvp.BasePresenter;


import java.util.Map;

/**
 * Created by admin on 2017/5/24.
 */

public class ProductListPresenter extends BasePresenter<IProductListView> {
    public ProductListPresenter(IProductListView mvpView) {
        super(mvpView);
    }

    public void getProductList(Map  map){
        addSubscription(DcodeService.getServiceData(map), new SubscriberCallBack() {
            @Override
            protected void onError(Exception e) {
            }

            @Override
            protected void onFailure(String response) {

            }

            @Override
            protected void onSuccess(String response) {
                mvpView.onLoadNetDataResult(response);
            }
        });
    }
}
