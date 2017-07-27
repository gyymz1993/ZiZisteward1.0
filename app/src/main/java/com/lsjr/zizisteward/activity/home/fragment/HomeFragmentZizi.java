package com.lsjr.zizisteward.activity.home.fragment;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lsjr.base.MvpFragment;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.group.ui.GroupActivity;
import com.lsjr.zizisteward.coustom.NoScrollViewPager;
import com.ymz.baselibrary.ABaseFragment;
import com.ymz.baselibrary.mvp.BasePresenter;
import com.ymz.baselibrary.utils.L_;
import com.ymz.baselibrary.utils.UIUtils;
import com.ys.uilibrary.rv.RecyclerTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class HomeFragmentZizi extends MvpFragment implements GroupActivity.GetDataOnclickListener, View.OnClickListener {

    private String[] titles = new String[]{"匠品", "奢品", "出行", "首页", "美食", "健康", "家族"};
    private Integer[] imageNormal = new Integer[]{R.drawable.jiangpin_normal, R.drawable.shepin_normal,
            R.drawable.travel_normal, R.drawable.home_normal, R.drawable.food_normal, R.drawable.health_normal,
            R.drawable.family_nromal};
    private Integer[] imageSelect = new Integer[]{R.drawable.jiangpin_select, R.drawable.shepin_select,
            R.drawable.travel_select, R.drawable.home_select, R.drawable.food_select, R.drawable.health_select,
            R.drawable.family_select};

    @BindView(R.id.id_vp_home)
    NoScrollViewPager mVpHome;

    @BindView(R.id.id_tv_city)
    TextView tvCity;

    @BindView(R.id.id_recyview)
    RecyclerTabLayout idRecyview;


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


    @Override
    protected void initData() {
        GroupActivity.setGetDataOnclickListener(this);
    }


    @Override
    public void initView() {
        //SystemBarHelper.tintStatusBar(getActivity(), UIUtils.getColor(R.color.white));
        initFragments();
        initViewPager();
        initViewOnClick();
        initGallery();
    }

    private void initViewOnClick() {
        tvCity.setOnClickListener(this);
    }

    private List<ABaseFragment> footFragments = new ArrayList<>();

    private void initFragments() {
        for (int i = 0; i < titles.length; i++) {
            // ShiJieFragment shiJieFragment=new ShiJieFragment();
            //footFragments.add(shiJieFragment);
            ShepinFragment ziShangFragment = new ShepinFragment();
            footFragments.add(ziShangFragment);
            //FootFragmentZizi fotFragment = new FootFragmentZizi();
            //footFragments.add(fotFragment);
        }
    }

    private void initViewPager() {
        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager());
        mVpHome.setNoScroll(false);
        mVpHome.setAdapter(pagerAdapter);
        mVpHome.setCurrentItem(3);
       // mVpHome.setOffscreenPageLimit(titles.length);
        mVpHome.setOffscreenPageLimit(2);
    }

    private void initGallery() {
        idRecyview.setUpWithAdapter(new IndicatorAdapter(mVpHome));
        idRecyview.setPositionThreshold(0.5f);
        idRecyview.setIndicatorColor(UIUtils.getColor(R.color.idselcte));
        idRecyview.setIndicatorHeight(8);
        //idRecyview.setAutoSelectionMode(true);
        idRecyview.setRecycleViewScollto(true);
    }


    public class IndicatorAdapter extends RecyclerTabLayout.Adapter<IndicatorAdapter.ViewHolder> {
        private PagerAdapter mAdapater;

        public IndicatorAdapter(ViewPager viewPager) {
            super(viewPager);
            mAdapater = (PagerAdapter) mViewPager.getAdapter();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_custom_view02_tab, parent, false);
            return new ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (position == getCurrentIndicatorPosition()) {
                holder.imageView.setImageResource(mAdapater.getSelectImageResourceId(position));
            } else {
                holder.imageView.setImageResource(mAdapater.getNormalImageResourceId(position));
            }
            //holder.indactor.setVisibility(position == getCurrentIndicatorPosition() ? View.VISIBLE : View.GONE);
            holder.indactor.setVisibility(View.GONE);
        }

        private Drawable loadIconWithTint(Context context, @DrawableRes int resourceId) {
            Drawable icon = ContextCompat.getDrawable(context, resourceId);
            ColorStateList colorStateList = ContextCompat
                    .getColorStateList(context, R.color.custom_view02_tint);
            icon = DrawableCompat.wrap(icon);
            DrawableCompat.setTintList(icon, colorStateList);
            return icon;
        }

        @Override
        public int getItemCount() {
            return mAdapater.getCount();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView imageView;
            ImageView indactor;

            ViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.image);
                indactor = (ImageView) itemView.findViewById(R.id.id_indactor);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getViewPager().setCurrentItem(getAdapterPosition());
                    }
                });
            }
        }
    }


    public class PagerAdapter extends FragmentPagerAdapter {

        PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return footFragments.get(position);
        }

        @Override
        public int getCount() {
            return footFragments.size();
        }

        @Override
        public String getPageTitle(int position) {
            return titles[position];
        }

        int getNormalImageResourceId(int position) {
            return imageNormal[position];
        }

        int getSelectImageResourceId(int position) {
            return imageSelect[position];
        }

    }


    @Override
    protected void initTitle() {
        //SystemBarHelper.tintStatusBar(getActivity(), UIUtils.getColor(R.color.colorBlack));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void afterCreate(Bundle bundle) {
        //UIUtils.setStatusBarColor(getActivity(), UIUtils.getColor(R.color.white));
    }

    @Override
    public void setData(String data) {
        String name = HomeFragmentZizi.class.getName();
        L_.e(name + data);
    }


    @Override
    public void onClick(View v) {

    }


}
