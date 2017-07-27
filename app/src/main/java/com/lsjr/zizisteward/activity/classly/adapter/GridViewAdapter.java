package com.lsjr.zizisteward.activity.classly.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.bean.Commodity;
import com.lsjr.zizisteward.http.AppUrl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/5/12.
 */

public class GridViewAdapter  extends RecyclerView.Adapter<GridViewAdapter.ViewHolder> {


    private View headView;
    private View foodView;
    private LayoutInflater inflater;
    private Context mContent;
    private List<Commodity.FamousShopBean> mFamousShop=new ArrayList<>();

    public GridViewAdapter(Context content) {
        this.mContent = content;
        inflater=LayoutInflater.from(content);
    }

    public void setmFamousShop(List<Commodity.FamousShopBean> famousShop) {
        this.mFamousShop = famousShop;
        notifyDataSetChanged();
    }

    public void addmFamousShop(List<Commodity.FamousShopBean> famousShop) {
        this.mFamousShop.addAll(famousShop);
        notifyDataSetChanged();
    }

    public View getHeadView() {
        return headView;
    }

    public void setHeadView(View headView) {
        this.headView = headView;
    }

    public View getFoodView() {
        return foodView;
    }

    public void setFoodView(View foodView) {
        this.foodView = foodView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_gridview_classify,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        viewHolder.imageView= (ImageView) view.findViewById(R.id.iv_classic);
        viewHolder.textName= (TextView) view.findViewById(R.id.tv_brand);
        viewHolder.textPrice= (TextView) view.findViewById(R.id.tv_price);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textName.setText(mFamousShop.get(position).getSname());
        holder.textPrice.setText(mFamousShop.get(position).getSprice()+"");
        Glide.with(mContent).load(AppUrl.Http + mFamousShop.get(position).getSpic())
                .into(holder.imageView);

       // LoaderFactory.getInstance().displayImage(holder.imageView,BaseUrl.IMAGEHOST + mBanners.get(0).getImage_filename());
    }

    @Override
    public int getItemCount() {
        return mFamousShop.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }
        ImageView imageView;
        TextView  textName;
        TextView  textPrice;
    }
}
