package com.lsjr.zizisteward.activity.home.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lsjr.base.BaseRefreshAdapter;
import com.lsjr.base.BaseRefreshFragment;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.detail.ProductDetailsActivity;
import com.lsjr.zizisteward.activity.home.presenter.ShepinPresenter;
import com.lsjr.zizisteward.activity.home.view.ShepinView;
import com.lsjr.zizisteward.http.AppUrl;
import com.lsjr.zizisteward.mvp.ui.UploadActivity;
import com.lsjr.zizisteward.utils.RoundImageView;
import com.ymz.baselibrary.utils.L_;
import com.ymz.baselibrary.utils.UIUtils;
import com.youth.banner.Banner;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.ys.uilibrary.base.BaseRecyclerHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/7/12 15:31
 */

public class ShepinFragment extends BaseRefreshFragment implements ShepinView {


    int pager = 1;
    ShepinAdapter shepinAdapter;
    RecyclerView rvNewPro;
    List<TravelHost> hottest_list;
    List<BannerBean> bannerData = new ArrayList<>();
    List<Recommend> recommends;
    Banner banner;
    RecyclerView rvPorduces;

    private String titleClass[] = new String[]{"更多分类", "会员专享"};

    @Override
    protected ShepinPresenter createPresenter() {
        return new ShepinPresenter(this);
    }

    @Override
    protected void afterCreate(Bundle bundle) {
        showLoadingView();
        loadData();
    }

    @Override
    protected void initView() {
        super.initView();
        xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                pullStatus = ON_REFRESH;
                loadData();
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                pullStatus = ON_LOAD;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadData();
                    }
                }, 2000);

            }
        });
        recyclerView.setAdapter(shepinAdapter);
    }

    @Override
    public BaseRefreshAdapter getBaseRefreshAdapter() {
        return shepinAdapter=new ShepinAdapter(getActivity(),hottest_list,R.layout.item_shepin_frag);
    }


    public View getHeadView() {
        View headView = UIUtils.inflate(R.layout.shepin_rv_head);
        banner = (Banner) headView.findViewById(R.id.viewpager);
        banner.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.WHD()[0] * 2 / 5));
        rvNewPro = (RecyclerView) headView.findViewById(R.id.rv_new_pro);
        rvPorduces = (RecyclerView) headView.findViewById(R.id.id_rv_pro);
        TextView rvMore = (TextView) headView.findViewById(R.id.id_tv_more);

        initNewPro();

        /*更多商品*/
        rvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(UploadActivity.class);
                //Intent intent = new Intent(getActivity(), MorePorductActivity.class);
                //startActivity(intent);
            }
        });
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int i) {
                openActivity(ProductDetailsActivity.class);
            }
        });

        return headView;
    }


    public class PorductAdapter extends com.ys.uilibrary.base.BaseRecyclerAdapter<PorducteBean> {
        public PorductAdapter(Context context, List<PorducteBean> datas, int itemLayoutId) {
            super(context, datas, itemLayoutId);
        }

        @Override
        protected void convert(BaseRecyclerHolder holder, PorducteBean item, int position) {
               /*居家好物*/
            LinearLayout layoutContainer = holder.getView(R.id.id_hs_container);
            ImageView igMore = holder.getView(R.id.id_ig_more);
            igMore.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, UIUtils.WHD()[0] * 2 / 5));
            Glide.with(getContext()).load(AppUrl.Http + bannerData.get(position).getImageFileName()).into(igMore);
            TextView textViewClass = holder.getView(R.id.id_tv_more_pro);
            textViewClass.setText(titleClass[position]);
            layoutContainer.removeAllViews();
            for (int i = 0; i < recommends.size(); i++) {
                View view = UIUtils.inflate(R.layout.luxury_classic);
                ImageView imageView = (ImageView) view.findViewById(R.id.iv_classic_licqi);
                TextView textView = (TextView) view.findViewById(R.id.tv_classic_licqi);
                imageView.setImageResource(recommends.get(i).getResId());
                textView.setText(recommends.get(i).getImageFileName());
                view.setTag(i);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 10, 0, 10);
                layoutContainer.addView(view, layoutParams);
            }
        }
    }


    public void initBanner() {
         /*Banner图片*/
        if (bannerData == null || bannerData.size() == 0) return;
        List<String> bannerImag = new ArrayList<>();
        for (int position = 0; position < bannerData.size(); position++) {
            bannerImag.add(AppUrl.Http + bannerData.get(position).getImageFileName());
        }
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //开始轮播
        banner.setDelayTime(10000);
        banner.setImages(bannerImag)
                .setImageLoader(new GlideImageLoader());
        banner.start();
        // .start();//.startAutoPlay();

    }

    public void initNewPro() {
        recommends = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            Recommend recommend = new Recommend();
            recommend.setImageFileName(titles[i]);
            recommend.setResId(images[i]);
            recommends.add(recommend);
        }
        ProNewAdapter proNewAdapter = new ProNewAdapter(getContext(), recommends, R.layout.item_shepin_new_pro);
        SpaceItemDecoration spacesItemDecoration = new SpaceItemDecoration(140);
        rvNewPro.addItemDecoration(spacesItemDecoration);
        rvNewPro.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvNewPro.setAdapter(proNewAdapter);
    }


    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = 10;
            outRect.right = 10;
            if (parent.getChildAdapterPosition(view) == 3 || parent.getChildAdapterPosition(view)
                    == 4 || parent.getChildAdapterPosition(view) == 5) {
                outRect.bottom = 0;
            } else {
                outRect.bottom = space;
            }
        }

    }


    String[] titles = {"针剂面膜", "CUCCI包包", "LV经典黑裙", "IDO钻戒", "CUCCI 女鞋", "兰蔻气垫BB"};
    int[] images = {R.drawable.test_pic, R.drawable.test_pic, R.drawable.test_pic,
            R.drawable.test_pic, R.drawable.test_pic, R.drawable.test_pic};

    private class ProNewAdapter extends com.ys.uilibrary.base.BaseRecyclerAdapter<Recommend> {

        public ProNewAdapter(Context context, List<Recommend> datas, int itemLayoutId) {
            super(context, datas, itemLayoutId);
        }

        @Override
        protected void convert(BaseRecyclerHolder holder, Recommend item, int position) {
            ImageView iv_service = holder.getView(R.id.iv_service);/*出行服务图片*/
            TextView tv_service = holder.getView(R.id.tv_service);/*出行服务文字*/
            RoundImageView iv_photo = holder.getView(R.id.iv_photo);/*目的地图片*/
            iv_service.setImageResource(item.getResId());
            tv_service.setText(item.getImageFileName());
            //Glide.with(getActivity()).load(BaseUrl.Http + item.getThumbImage()).into(holder.iv_service);
        }
    }


    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(context.getApplicationContext())
                    .load(path)
                    .crossFade()
                    .into(imageView);
        }

    }


    public void loadData() {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("OPT", "522");
        map.put("currPage", String.valueOf(pager));
        createPresenter().loadDataforNet(map);
    }

    @Override
    public void onLoadDataSucceed(String result) {
        showContentView();
        //*刷新*//
        xRefreshView.stopRefresh();
        xRefreshView.setLoadComplete(false);
        TravelBean travelBean = new Gson().fromJson(result, TravelBean.class);
        hottest_list = travelBean.getHottest();
        bannerData = travelBean.getBanner();
        moreList.addAll(hottest_list);
        //recommends = travelBean.getRecommend();
        initBanner();
        setPorductList();
        endNetRequse(hottest_list);

    }


    @Override
    public void onNetworkViewRefresh() {
        loadData();
    }

    @Override
    public void noData() {
        /**/
        L_.e("noData noData 无网络页面");
        showErrorView();
    }

    private static final int ON_REFRESH = 1;
    private static final int ON_LOAD = 2;
    private int pullStatus;
    List<TravelHost> moreList = new ArrayList<>();

    public void endNetRequse(List<TravelHost> hottest_list) {
        //*加载更多*//*
        if (pullStatus == ON_LOAD) {
            if (moreList.size() >= 12) {
                /*再无数据*/
                xRefreshView.setLoadComplete(true);
            } else {
                xRefreshView.stopLoadMore(true);
            }
            moreList.addAll(hottest_list);
            shepinAdapter.notifyDataSetChanged(moreList);
        } else if (pullStatus == ON_REFRESH) {
            moreList.clear();
            xRefreshView.stopRefresh();
            xRefreshView.setLoadComplete(false);
            shepinAdapter.notifyDataSetChanged(hottest_list);
        } else {
            shepinAdapter.notifyDataSetChanged(hottest_list);
        }
        pullStatus = 0;
    }


    private void setPorductList() {
        rvPorduces.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });
        List<PorducteBean> porducteBeans = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            porducteBeans.add(new PorducteBean());
        }
        PorductAdapter porductAdapter = new PorductAdapter(getContext(), porducteBeans, R.layout.shepin_rv_head_type);
        rvPorduces.setAdapter(porductAdapter);
    }


    private class TravelBean {
        public List<BannerBean> getBanner() {
            return banner;
        }

        public List<TravelHost> getHottest() {
            return hottest;
        }

        public List<Recommend> getRecommend() {
            return recommend;
        }

        private List<BannerBean> banner;
        private List<TravelHost> hottest;
        private List<Recommend> recommend;
    }

    private class Recommend {
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImageFileName() {
            return imageFileName;
        }

        public String getLocation() {
            return location;
        }

        public String getThumbImage() {
            return thumbImage;
        }


        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setImageFileName(String imageFileName) {
            this.imageFileName = imageFileName;
        }

        public void setResId(int resId) {
            this.resId = resId;
        }

        public int getResId() {
            return resId;
        }

        private String id;
        private String imageFileName;
        private String location;
        private String thumbImage;
        private String url;
        private int resId;
    }


    private class BannerBean {
        public String getId() {
            return id;
        }


        public String getImageFileName() {
            return imageFileName;
        }

        public String getThumbImage() {
            return thumbImage;
        }

        public String getUrl() {
            return url;
        }

        private String id;
        private String imageFileName;
        private String thumbImage;
        private String url;

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        private String no;

    }

    public class PorducteBean {

        private String imagUrl;
        List<Recommend> recommends;

        public String getImagUrl() {
            return imagUrl;
        }

        public void setImagUrl(String imagUrl) {
            this.imagUrl = imagUrl;
        }

        public List<Recommend> getRecommends() {
            return recommends;
        }

        public void setRecommends(List<Recommend> recommends) {
            this.recommends = recommends;
        }
    }

    private class ShepinAdapter extends BaseRefreshAdapter {
        private Context context;
        private List<TravelHost> hottest_list;
        private AdapterView.OnItemClickListener clickListener;

        public ShepinAdapter(Context context, List datas, int itemLayoutId) {
            super(context, datas, itemLayoutId);
        }

        private void setOnClickListener(AdapterView.OnItemClickListener clickListener) {
            this.clickListener = clickListener;
        }


        @Override
        protected void convert(BaseRecyclerHolder holder, Object var2, int position) {

            ImageView iv_photo_main = holder.getView(R.id.iv_photo_main);
              /*设置图片的宽高比*/
            RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) iv_photo_main.getLayoutParams();
            linearParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            linearParams.height = UIUtils.WHD()[0] * 2 / 5;
            iv_photo_main.setLayoutParams(linearParams);
            int count = position > 1 ? 0 : 1;
            Glide.with(getContext()).load(AppUrl.Http + bannerData.get(count).getImageFileName()).into(iv_photo_main);
            // Glide.with(context).load(BaseUrl.Http + hottest_list.get(position).getImageFileName()).into(holder.iv_photo_main);
        }

    }

    private class TravelHost {
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImageFileName() {
            return imageFileName;
        }

        public String getThumbImage() {
            return thumbImage;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        private String id;
        private String imageFileName;
        private String thumbImage;
        private String url;

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        private String no;

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        private String location;
    }


    class TravelHolder extends RecyclerView.ViewHolder {
        ImageView iv_photo_main;

        public TravelHolder(View view) {
            super(view);
            iv_photo_main = (ImageView) view.findViewById(R.id.iv_photo_main);
        }
    }

}
