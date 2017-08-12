package com.lsjr.zizisteward.activity.more;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.andview.refreshview.XRefreshView;
import com.lsjr.base.BaseRefreshActivity;
import com.lsjr.base.BaseRefreshAdapter;
import com.lsjr.zizisteward.R;
import com.ymz.baselibrary.mvp.BasePresenter;
import com.ymz.baselibrary.utils.UIUtils;
import com.ys.uilibrary.CouponLinearLayout;
import com.ys.uilibrary.base.BaseRecyclerHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/7/21 14:46
 */

public class MorePorductActivity extends BaseRefreshActivity {

    List<String> mdata = new ArrayList();

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public BaseRefreshAdapter getBaseRefreshAdapter() {
        for (int i = 0; i < 15; i++) {
            mdata.add("iiii");
        }
        return new PorductRvAdapter(getApplicationContext(), mdata,0);
    }

    @Override
    public View getHeadView() {
        View headView = UIUtils.inflate(R.layout.activity_more_header);
        //getToolBarView().setTitleTextColor(UIUtils.getColor("#21212"));
        ImageView idIgDtIcon = (ImageView) headView.findViewById(R.id.id_ig_dt_icon);
        idIgDtIcon.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
        idIgDtIcon.getLayoutParams().height = UIUtils.WHD()[0] * 2 / 3;
        return headView;
    }

    @Override
    protected void afterCreate(Bundle bundle) {
        setTitleText("商品详细");
        recyclerView.setAdapter(getBaseRefreshAdapter());
        xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                pullStatus = ON_REFRESH;
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                pullStatus = ON_LOAD;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    }
                }, 2000);

            }
        });
    }

    public class PorductRvAdapter extends BaseRefreshAdapter {
        private static final int TYPE_1 = 0X11;
        private static final int TYPE_2 = 0X22;
        public PorductRvAdapter(Context context, List datas, int itemLayoutId) {
            super(context, datas, itemLayoutId);
        }

        @Override
        protected void convert(BaseRecyclerHolder var1, Object var2, int var3) {

        }
        @Override
        public void onBindViewHolder(BaseRecyclerHolder viewHolder, int position, boolean b) {
//            CouponLinearLayout linearLayout =  viewHolder.getView(R.id.id_coup_layout);
//            linearLayout.setSemicircleTop(true);
//            linearLayout.setSemicircleBottom(true);
        }


        @Override
        public BaseRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean b) {
            View viewTypeInfalter;
            switch (viewType) {
                case TYPE_1:
                    viewTypeInfalter = UIUtils.inflate(R.layout.item_more_pd_left);
                    return new BaseRecyclerHolder(viewTypeInfalter);
                case TYPE_2:
                    viewTypeInfalter = UIUtils.inflate(R.layout.item_more_pd_right);
                    return new BaseRecyclerHolder(viewTypeInfalter);
                default:

            }
            return null;
        }


        /**
         * 实现此方法来设置viewType
         *
         * @param position
         * @return viewType
         */
        public int getAdapterItemViewType(int position) {
            if (position % 2 == 0) {
                return TYPE_1;
            } else {
                return TYPE_2;
            }
        }
    }

}
