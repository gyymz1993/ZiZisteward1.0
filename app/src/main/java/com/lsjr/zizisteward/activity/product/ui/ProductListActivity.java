package com.lsjr.zizisteward.activity.product.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.google.gson.Gson;
import com.lsjr.base.MvpActivity;
import com.lsjr.zizisteward.Config;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.product.adapter.ProductListAdapter;
import com.lsjr.zizisteward.activity.product.presenter.ProductListPresenter;
import com.lsjr.zizisteward.activity.product.view.IProductListView;
import com.lsjr.zizisteward.bean.ProductListBean;
import com.ymz.baselibrary.utils.SystemBarHelper;
import com.ymz.baselibrary.utils.UIUtils;
import com.ymz.baselibrary.widget.NavigationBarView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

public class ProductListActivity extends MvpActivity<ProductListPresenter> implements IProductListView {


    List<ProductListBean.PierreDetail> mdata = new ArrayList<>();
    ProductListAdapter productListAdapter;
    @BindView(R.id.listview_recommend)
    RecyclerView recycleView;
    @BindView(R.id.xrefreshview)
    XRefreshView xRefreshview;
    //用来标记是否正在向最后一个滑动，既是否向下滑动
    int lastVisibleItem = 0;
    @BindView(R.id.id_nativgation_view)
    NavigationBarView idNativgationView;
    private boolean isBottom = false;

    private static final int ON_REFRESH = 1;
    private static final int ON_LOAD = 2;
    private int pullStatus;
    private  int pageNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected ProductListPresenter createPresenter() {
        return new ProductListPresenter(this);
    }


    LinearLayoutManager layoutManager;

    @Override
    protected void initView() {
        super.initView();


        productListAdapter = new ProductListAdapter(this, mdata);
        recycleView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(layoutManager);
        // 静默加载模式不能设置footerview
        recycleView.setAdapter(productListAdapter);
        //设置刷新完成以后，headerview固定的时间
        xRefreshview.setPinnedTime(500);
        xRefreshview.setMoveForHorizontal(true);
        xRefreshview.setPullLoadEnable(true);
        xRefreshview.setAutoLoadMore(true);
        productListAdapter.setCustomLoadMoreView(new XRefreshViewFooter(this));
        xRefreshview.enableReleaseToLoadMore(true);
        xRefreshview.enableRecyclerViewPullUp(true);
        xRefreshview.enablePullUpWhenLoadCompleted(true);
        xRefreshview.setSilenceLoadMore(true);

        //设置Recyclerview的滑动监听
        xRefreshview.setOnRecyclerViewScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        xRefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh(boolean isPullDown) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullStatus = ON_REFRESH;
                        pageNum = 1;
                        loadNetData();
                    }
                }, 500);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
//                new Handler().postDelayed(new Runnable() {
//                    public void run() {
//                        if (mdata.size()>50){
//                            xRefreshview.setLoadComplete(true);
//                            T_.showToastReal("没有更多数据");
//                        }
//                        else {
//                            xRefreshview.stopLoadMore();
//                        }
//
//                    }
//                }, 1000);
            }
        });


        // 实现Recyclerview的滚动监听，在这里可以自己处理到达底部加载更多的操作，可以不实现onLoadMore方法，更加自由
        xRefreshview.setOnRecyclerViewScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (isBottom = productListAdapter.getItemCount() - 2 == lastVisibleItem) {
                        pullStatus = ON_LOAD;
                        pageNum++;
                        xRefreshview.setLoadComplete(false);
                        loadNetData();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });

        productListAdapter.setOnItemClickListener(new ProductListAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                  /*商品详细*/
                Bundle bundle = new Bundle();
                bundle.putString(Config.PRODUCT_ID, mdata.get(position).getId());
                openActivity(ProductDetailActivity.class, bundle);
            }
        });


//        listviewRecommend.setAdapter(productListAdapter);
//        listviewRecommend.setOnRefreshListener(new SuperListView.OnRefreshListener() {
//
//            @Override
//            public void onRefresh() {
//            }
//        });
//        listviewRecommend.setOnLoadMoreListener(new SuperListView.OnLoadMoreListener() {
//
//            @Override
//            public void onLoadMore() {
//            }
//        });
//        listviewRecommend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                /*商品详细*/
//                Bundle bundle = new Bundle();
//                bundle.putString(Config.PRODUCT_ID, mdata.get(position - 1).getId());
//                openActivity(ProductDetailActivity.class, bundle);
//            }
//        });
//
//        listviewRecommend.refresh();
    }

    @Override
    protected void initData() {
        super.initData();
        loadNetData();
    }

    @Override
    protected void initTitle() {
        super.initTitle();
        SystemBarHelper.tintStatusBar(this, UIUtils.getColor(R.color.colorBlack));
        idNativgationView.setTitleText("商品列表");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_productlist;
    }

    @Override
    protected void afterCreate(Bundle bundle) {

    }

    @Override
    public int getFragmentContentId() {
        return 0;
    }


    private void loadNetData() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("OPT", "63");
        map.put("currPage", String.valueOf(pageNum++));
        createPresenter().getProductList(map);
    }

    @Override
    public void onLoadNetDataResult(String result) {
        ProductListBean productListBean = new Gson().fromJson(result, ProductListBean.class);
        List<ProductListBean.PierreDetail> page = productListBean.getPierre().getPage();
        endNetRequse(page);
    }

    public void endNetRequse(List<ProductListBean.PierreDetail> page) {
        if (pullStatus == ON_LOAD) {
            xRefreshview.stopLoadMore();
            mdata.addAll(page);
        }
        if (pullStatus == ON_REFRESH || pullStatus == 0) {
            xRefreshview.stopRefresh();
            mdata=page;
        }
        productListAdapter.notifyDataSetChanged(mdata);
        pullStatus = 0;
    }

}
