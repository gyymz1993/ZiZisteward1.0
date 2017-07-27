package com.lsjr.zizisteward.activity.product.presenter;

import android.webkit.JavascriptInterface;

import com.lsjr.base.SubscriberCallBack;
import com.lsjr.net.DcodeService;
import com.lsjr.zizisteward.activity.product.view.IProductDetailView;
import com.ymz.baselibrary.mvp.BasePresenter;


import java.util.Map;

/**
 * Created by admin on 2017/5/24.
 */

public class ProductDetailPresenter extends BasePresenter<IProductDetailView> {





    public ProductDetailPresenter(IProductDetailView mvpView) {
        super(mvpView);
    }

    public void getProductDetail(Map  map){
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

    public void senOrder(Map  map){
        addSubscription(DcodeService.getServiceData(map), new SubscriberCallBack() {
            @Override
            protected void onError(Exception e) {
            }

            @Override
            protected void onFailure(String response) {

            }

            @Override
            protected void onSuccess(String response) {
                mvpView.onSendOrderSucceed(response);
            }
        });
    }



    public interface  ProductDetailH5Contrl {

        /**
         * 和H5方法名 参数类型必须一致
         *
         * @param urls
         */
        @JavascriptInterface
        void sendUrls(String urls);

    }
}
