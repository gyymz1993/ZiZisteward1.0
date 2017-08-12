package com.lsjr.zizi.base;

import com.ymz.baselibrary.mvp.BaseMvpActivity;
import com.ymz.baselibrary.mvp.BasePresenter;

/**
  * @author: gyymz1993
  * 创建时间：2017/5/3 21:53
  * @version  最基本的MVP 模式基类
  *
 **/
public abstract class MvpActivity<P extends BasePresenter> extends BaseMvpActivity<P> {

    @Override
    protected void initTitle() {
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public int getFragmentContentId() {
        return 0;
    }

}
