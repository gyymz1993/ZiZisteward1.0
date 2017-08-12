package com.lsjr.zizisteward.mvp.recycle;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.classly.adapter.SpacesItemDecoration;
import com.lsjr.zizisteward.coustom.myrecycleview.WrapRecyclerView;
import com.lsjr.zizisteward.http.AppUrl;
import com.lsjr.zizisteward.utils.RoundImageView;
import com.ymz.baselibrary.utils.UIUtils;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/5/22.
 */

public class RecycleViewTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private List<RecycleBean> mData;
    private LayoutInflater mInfalter;

    private static final int TYPE_1 = 0X11;
    private static final int TYPE_2 = 0X22;
    private static final int TYPE_3 = 0X33;
    private static final int TYPE_4 = 0X44;

    private List<RecycleBean.RecycleDeatil> recycleDeatils1=new ArrayList<>();
    private List<RecycleBean.RecycleDeatil> recycleDeatils2=new ArrayList<>();
    private List<RecycleBean.RecycleDeatil> recycleDeatils3=new ArrayList<>();

    public RecycleViewTypeAdapter(Context context, List<RecycleBean> mData) {
        this.context = context;
        this.mData = mData;
        mInfalter = LayoutInflater.from(context);
    }

    public void addData(List<RecycleBean> data) {
        this.mData.clear();
        this.mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewTypeInfalter;
        switch (viewType) {
            case TYPE_1:
                viewTypeInfalter = mInfalter.inflate(R.layout.test_vh_one, parent, false);
                return new ViewType1.OnCreateViewHolder1(viewTypeInfalter);
            case TYPE_2:
                viewTypeInfalter = mInfalter.inflate(R.layout.test_vh_two, parent, false);
                return new OnCreateViewHolder2(viewTypeInfalter);
            case TYPE_3:
                viewTypeInfalter = mInfalter.inflate(R.layout.test_vh_three, parent, false);
                return new ViewType3.OnCreateViewHolder3(viewTypeInfalter);
            case TYPE_4:
                break;
            default:

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewType1.OnCreateViewHolder1) {
            onBindViewHolder1((ViewType1.OnCreateViewHolder1) holder, position);
        } else if (holder instanceof OnCreateViewHolder2) {
            onBindViewHolder2((OnCreateViewHolder2) holder, position);
        } else if (holder instanceof ViewType3.OnCreateViewHolder3) {
            onBindViewHolder3((ViewType3.OnCreateViewHolder3) holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position).getType()==TYPE_1){
            return TYPE_1;
        }else if (mData.get(position).getType()==TYPE_2){
            return TYPE_2;
        }else if (mData.get(position).getType()==TYPE_3){
            return TYPE_3;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private void onBindViewHolder1(ViewType1.OnCreateViewHolder1 holder, int position) {
        for (int i = 0; i < 4; i++) {
            RecycleBean.RecycleDeatil recycleDeatil1 = new RecycleBean.RecycleDeatil();
            recycleDeatil1.setImgUrl("/images?uuid=9a27e0c7-86d6-4505-b35b-1715f4d204ea");
            recycleDeatils1.add(recycleDeatil1);
        }
        LinearLayoutManager llyanager = new LinearLayoutManager(context);
        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(10);
        llyanager.setOrientation(LinearLayoutManager.HORIZONTAL);

//        View headerView=mInfalter.inflate(R.layout.test_view_one_header,null);
//        holder.recyclerView1.addHeaderView(headerView);
        /*解决滑动缓慢 */
        holder.recyclerView1.addItemDecoration(spacesItemDecoration);
        holder.recyclerView1.setLayoutManager(llyanager);
        holder.recyclerView1.setAdapter(new RecyclerView.Adapter<ViewType1.ItemViewHolder1>() {

            @Override
            public ViewType1.ItemViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ViewType1.ItemViewHolder1(mInfalter.inflate(R.layout.test_item_type_one, parent, false));
            }

            @Override
            public void onBindViewHolder(ViewType1.ItemViewHolder1 holder, int position) {
                Glide.with(context).load(AppUrl.Http + recycleDeatils1.get(position).getImgUrl())
                        .into(holder.ivPhoto);
            }

            @Override
            public int getItemCount() {
                return recycleDeatils1.size();
            }

        });
    }


    static class ViewType1 {
        /*创建主页面的第一条Item*/
        static class OnCreateViewHolder1 extends RecyclerView.ViewHolder {
            WrapRecyclerView recyclerView1;

            public OnCreateViewHolder1(View itemView) {
                super(itemView);
                recyclerView1 = (WrapRecyclerView) itemView.findViewById(R.id.id_recyview1);
            }
        }

        /*第一条Itme里面的人子布局*/
        static class ItemViewHolder1 extends RecyclerView.ViewHolder {
            RoundImageView ivPhoto;
            TextView tvName;
            TextView tvContent;

            public ItemViewHolder1(View itemView) {
                super(itemView);
                ivPhoto = (RoundImageView) itemView.findViewById(R.id.iv_photo);
                tvName = (TextView) itemView.findViewById(R.id.tv_name);
                tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            }
        }

    }


    public class OnCreateViewHolder2 extends RecyclerView.ViewHolder {
        ImageView imageView;

        public OnCreateViewHolder2(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_ziwei);
        }
    }

    private void onBindViewHolder2(OnCreateViewHolder2 holder, int position) {

        RecycleBean.RecycleDeatil recycleDeati2 = new RecycleBean.RecycleDeatil();
        recycleDeati2.setImgUrl("/images?uuid=569d8899-e209-4849-b155-6507b58f3614");
        recycleDeatils2.add(recycleDeati2);

        RelativeLayout.LayoutParams linearParams2 = (RelativeLayout.LayoutParams) holder.imageView.getLayoutParams();
        linearParams2.width = UIUtils.WHD()[0];
        linearParams2.height = UIUtils.WHD()[0] / 2;
        holder.imageView.setLayoutParams(linearParams2);
        // 孜味天下图片
        Glide.with(context).load(AppUrl.Http + recycleDeatils2.get(0).getImgUrl())
                .into(holder.imageView);

    }


    private void onBindViewHolder3(ViewType3.OnCreateViewHolder3 holder, int position) {
        for (int i = 0; i < 4; i++) {
            RecycleBean.RecycleDeatil recycleDeati3 = new RecycleBean.RecycleDeatil();
            recycleDeati3.setImgUrl("/images?uuid=906537e4-5058-4ab0-9104-c71562f67a2d");
            recycleDeatils3.add(recycleDeati3);
        }
        LinearLayoutManager llyanager = new LinearLayoutManager(context);
        SpacesItemDecoration spacesItemDecoration = new SpacesItemDecoration(10);
        llyanager.setOrientation(LinearLayoutManager.HORIZONTAL);
        /*解决滑动缓慢 */
//        View headerView=mInfalter.inflate(R.layout.test_view_one_header,null);
//        holder.recyclerView3.addHeaderView(headerView);
        holder.recyclerView3.addItemDecoration(spacesItemDecoration);
        holder.recyclerView3.setLayoutManager(llyanager);
        holder.recyclerView3.setAdapter(new RecyclerView.Adapter<ViewType3.ItemViewHolder3>() {

            @Override
            public ViewType3.ItemViewHolder3 onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ViewType3.ItemViewHolder3(mInfalter.inflate(R.layout.test_vh_two, parent, false));
            }

            @Override
            public void onBindViewHolder(ViewType3.ItemViewHolder3 holder, int position) {
                Glide.with(context).load(AppUrl.Http + recycleDeatils3.get(position).getImgUrl())
                        .into(holder.ivPhoto);
            }

            @Override
            public int getItemCount() {
                return recycleDeatils3.size();
            }

        });
    }


    static class ViewType3 {
        /*Itme父控件*/
        public static class OnCreateViewHolder3 extends RecyclerView.ViewHolder {
            WrapRecyclerView recyclerView3;
            public OnCreateViewHolder3(View itemView) {
                super(itemView);
                recyclerView3 = (WrapRecyclerView) itemView.findViewById(R.id.id_recyview3);
            }
        }

        /*Itme子布局*/
        static class ItemViewHolder3 extends RecyclerView.ViewHolder {
            ImageView ivPhoto;
            TextView tvName;
            TextView tvContent;

            public ItemViewHolder3(View itemView) {
                super(itemView);
                ivPhoto = (ImageView) itemView.findViewById(R.id.iv_ziwei);
            }
        }
    }

}
