package com.ymz.baselibrary.mvp;

import android.os.Bundle;

import com.ymz.baselibrary.ABaseActivity;


/**
  * @author: gyymz1993
  * 创建时间：2017/5/3 21:53
  * @version
  *
 **/
public abstract class BaseMvpActivity<P extends BasePresenter> extends ABaseActivity {
    protected P mvpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (mvpPresenter == null) mvpPresenter = createPresenter();
        super.onCreate(savedInstanceState);

    }
    protected abstract P createPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mvpPresenter != null) {
            mvpPresenter.detachView();
        }
        mvpPresenter=null;
    }

}
