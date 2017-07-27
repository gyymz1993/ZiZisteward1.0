package com.lsjr.zizisteward.activity.product.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.bumptech.glide.Glide;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.bean.ProductListBean;
import com.lsjr.zizisteward.http.AppUrl;

import java.util.List;

public class ProductListAdapter extends BaseRecyclerAdapter<ProductListAdapter.ViewHolder> {
    private Context context;
    private List<ProductListBean.PierreDetail> list;



    public ProductListAdapter(Context context, List<ProductListBean.PierreDetail> list) {
        this.context = context;
        this.list = list;
    }

    public void notifyDataSetChanged(List<ProductListBean.PierreDetail> pierreDetails){
        list.clear();
        list.addAll(pierreDetails);
        notifyDataSetChanged();
    }


    public void LoadMoreChanged(List<ProductListBean.PierreDetail> pierreDetails){
        list.addAll(pierreDetails);
        notifyDataSetChanged();
    }



    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view,false);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.item_product_list, null);
        ViewHolder vh = new ViewHolder(convertView,true);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position, boolean isItem) {
        Glide.with(context).load(AppUrl.Http + list.get(position).getSpicfirst()).centerCrop()
                .animate(android.R.anim.slide_in_left).into(holder.mIv_recommend);
        holder.mTv_one.setText(list.get(position).getSname());
        holder.mTv_two.setText(list.get(position).getSkeyword());

        if (onItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.OnItemClick(position);
                }
            });
        }
    }

    @Override
    public int getAdapterItemCount() {
        return list == null ? 0 : list.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView mIv_recommend;
        private TextView mTv_one;
        private TextView mTv_two;

        public ViewHolder(View v,boolean isItem) {
            super(v);
            if (isItem){
                mIv_recommend = (ImageView) v.findViewById(R.id.iv_recommend);
                mTv_one = (TextView) v.findViewById(R.id.tv_one);
                mTv_two = (TextView) v.findViewById(R.id.tv_two);
            }

        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
      void   OnItemClick(int position);
    }
}
