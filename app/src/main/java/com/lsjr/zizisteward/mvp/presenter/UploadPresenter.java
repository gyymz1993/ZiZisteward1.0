package com.lsjr.zizisteward.mvp.presenter;

import com.lsjr.base.SubscriberCallBack;
import com.lsjr.net.UploadService;
import com.lsjr.zizisteward.mvp.view.IUploadView;
import com.ymz.baselibrary.mvp.BasePresenter;
import com.ymz.baselibrary.utils.L_;

import java.io.File;
import java.util.List;
import java.util.Map;


public class UploadPresenter extends BasePresenter<IUploadView> {
    public UploadPresenter(IUploadView mvpView) {
        super(mvpView);
    }

    public void onUploadImage(Map<String,String> map,List <File> fileList){
        addSubscription(UploadService.uploadImage(map,fileList), new SubscriberCallBack() {
            @Override
            protected void onError(Exception e) {
                L_.e("uploadFiles onError");
                mvpView.onUploadSucceed(false);
            }

            @Override
            protected void onFailure(String response) {
                L_.e("uploadFiles");
                mvpView.onUploadSucceed(false);
            }

            @Override
            protected void onSuccess(String response) {
                L_.e("uploadFiles"+response);
                mvpView.onUploadSucceed(true);
            }

        });
    }

}
