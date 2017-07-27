package com.ymz.baselibrary.mvp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
  * @author: gyymz1993
  * 创建时间：2017/5/3 21:53
  * @version
  *
 **/
public abstract class BaseMvpFragmentActivity<P extends BasePresenter> extends FragmentActivity {
    protected P mvpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mvpPresenter = createPresenter();
        super.onCreate(savedInstanceState);
    }

    protected abstract P createPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mvpPresenter != null) {
            mvpPresenter.detachView();
        }
    }

}
