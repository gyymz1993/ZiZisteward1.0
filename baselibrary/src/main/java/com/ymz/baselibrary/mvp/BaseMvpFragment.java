package com.ymz.baselibrary.mvp;

import android.os.Bundle;
import android.view.View;

import com.ymz.baselibrary.ABaseFragment;

/**
  * @author: gyymz1993
  * 创建时间：2017/5/3 21:56
  * @version
  *
 **/
public abstract class BaseMvpFragment<P extends BasePresenter> extends ABaseFragment {
    protected P mvpPresenter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (mvpPresenter == null) mvpPresenter = createPresenter();
        super.onViewCreated(view,savedInstanceState);
    }


    protected abstract P createPresenter();


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mvpPresenter != null) {
            mvpPresenter.detachView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mvpPresenter = null;
    }



}
