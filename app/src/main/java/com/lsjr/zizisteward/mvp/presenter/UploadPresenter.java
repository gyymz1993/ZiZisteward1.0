package com.lsjr.zizisteward.mvp.presenter;

import com.lsjr.net.DcodeService;
import com.lsjr.zizisteward.http.callback.SubscriberCallBack;
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

    public void onUploadImage(String url,Map<String,String> map,List <File> fileList){
        addSubscription(DcodeService.uploadFilesWithParts(url,map,fileList), new SubscriberCallBack() {
            @Override
            protected void onError(Exception e) {
                L_.e("onError");
                mvpView.onUploadSucceed(false);
            }

            @Override
            protected void onFailure(String response) {
                L_.e("onFailure"+response);
                mvpView.onUploadSucceed(false);
            }

            @Override
            protected void onSuccess(String response) {
                L_.e("onSuccess"+response);
                mvpView.onUploadSucceed(true);
            }

        });
    }

}
