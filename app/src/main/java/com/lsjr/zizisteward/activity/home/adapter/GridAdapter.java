package com.lsjr.zizisteward.activity.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.bean.HomeBean;
import com.lsjr.zizisteward.coustom.RoundImageView;
import com.lsjr.zizisteward.http.AppUrl;
import com.ymz.baselibrary.utils.L_;

import java.util.List;

/**
 * Created by admin on 2017/5/17.
 */

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    private static final int ITEM_VIEW_TYPE_HEADER=0;
    private static final int ITEM_VIEW_TYPE_ITME=1;

    private List<HomeBean.DiligentRecommendBean> recommends;
    private Context context;
    private LayoutInflater inflater;

    public GridAdapter(Context context, List<HomeBean.DiligentRecommendBean> recommends) {
        this.recommends = recommends;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void notifyDataChange(List<HomeBean.DiligentRecommendBean> commends){
        this.recommends.clear();
        this.recommends.addAll(commends);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = inflater.inflate(R.layout.item_home_commend, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(context).load(AppUrl.Http + recommends.get(position).getSpicfirst())
                .into(holder.ivPhoto);
        L_.e(recommends.get(position).getSpicfirst());
        holder.tvName.setText(recommends.get(position).getSkeyword());
        holder.tvContent.setText(recommends.get(position).getSname());
    }

    @Override
    public int getItemCount() {
        return recommends.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        RoundImageView ivPhoto;
        TextView tvName;
        TextView tvContent;
         ViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (RoundImageView) itemView.findViewById(R.id.iv_photo);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }
}
