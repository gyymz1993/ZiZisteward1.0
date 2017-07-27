package com.lsjr.base;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.coustom.CustomGifHeader;
import com.ymz.baselibrary.mvp.BasePresenter;

import butterknife.BindView;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/7/21 15:14
 */

public abstract class BaseRefreshFragment<P extends BasePresenter> extends MvpFragment<P> {

    @BindView(R.id.recyclerview)
    public RecyclerView recyclerView;
    @BindView(R.id.xrefreshview)
    public XRefreshView xRefreshView;
    public int pager = 1;
    public static final int ON_REFRESH = 1;
    public static final int ON_LOAD = 2;
    public int pullStatus;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_zishang;
    }

    @Override
    protected void initView() {
        xRefreshView.setAutoRefresh(false);
        xRefreshView.setAutoLoadMore(true);
        xRefreshView.setPinnedTime(1000);
        xRefreshView.stopLoadMore(false);
        xRefreshView.setPullLoadEnable(true);
        xRefreshView.setMoveForHorizontal(true);
        xRefreshView.setMoveForHorizontal(true);
        CustomGifHeader header = new CustomGifHeader(getContext());
        xRefreshView.setCustomHeaderView(header);
        initRecycleView();

    }

    public void initRecycleView() {
        BaseRefreshAdapter baseRefreshAdapter = getBaseRefreshAdapter();
        baseRefreshAdapter.setCustomLoadMoreView(new XRefreshViewFooter(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });
        baseRefreshAdapter.setHeaderView(getHeadView(), recyclerView);
        recyclerView.setAdapter(baseRefreshAdapter);
    }


    public abstract BaseRefreshAdapter getBaseRefreshAdapter();

    public abstract View getHeadView();

}
