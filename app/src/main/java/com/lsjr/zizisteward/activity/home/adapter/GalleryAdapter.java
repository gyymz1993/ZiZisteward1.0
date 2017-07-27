package com.lsjr.zizisteward.activity.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.coustom.AutoAdjustRecylerView;
import com.ymz.baselibrary.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/5/11.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private String[] mTitles;
    private Context mContext;
    private List<Boolean> isSelected;

    public interface OnItmeOnclickListener {
        void onItemClick(int position);
    }

    private OnItmeOnclickListener onItmeOnclickListener;
    private AutoAdjustRecylerView mAutoAdjustRecylerView = null;
    private Integer[] mImageNormal;
    private Integer[] mImageSelect;

    public void setOnItmeOnclickListener(OnItmeOnclickListener onItmeOnclickListener) {
        this.onItmeOnclickListener = onItmeOnclickListener;
    }

    public GalleryAdapter(Context context, AutoAdjustRecylerView autoAdjustRecylerView, String[] titles, Integer[] imageNormal, Integer[] imageSelect) {
        this.mAutoAdjustRecylerView = autoAdjustRecylerView;
        this.mTitles = titles;
        this.mContext = context;
        this.mImageNormal = imageNormal;
        this.mImageSelect = imageSelect;
        mInflater = LayoutInflater.from(context);
        isSelected = new ArrayList<>();
        int count=titles.length;
        for (int i = 0; count > i; i++) {
            isSelected.add(false);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_gallery, parent, false);
        ViewHolder viewHolder=new ViewHolder(view);
        viewHolder.imageView = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);
        viewHolder.rlContent = (RelativeLayout) view.findViewById(R.id.id_rl_content);
        viewHolder.textView = (TextView) view.findViewById(R.id.id_index_gallery_item_text);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.itemView.setTag(Integer.valueOf(position));
        holder.textView.setText(mTitles[position]);
        if (isSelected.get(position)) {
            holder.imageView.setImageResource(mImageSelect[position]);
            setBigImageLayouParams(holder);
        } else {
            holder.imageView.setImageResource(mImageNormal[position]);
            setSmaillImageLayouParams(holder);
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentItme(position);
            }
        });
    }

    public void setCurrentItme(int position){
        mAutoAdjustRecylerView.checkAutoAdjust(position);
        int center= (int) Math.ceil((getItemCount()+1)/2);
        for (int i=0;i<isSelected.size();i++){
            isSelected.set(i,false);
        }
        isSelected.set(position,true);
        notifyDataSetChanged();
        if (onItmeOnclickListener!=null){
            onItmeOnclickListener.onItemClick(position);
        }
    }

    private  void setBigImageLayouParams(ViewHolder holder) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(UIUtils.dp2px(42), UIUtils.dip2px(42));
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        holder.imageView.setLayoutParams(layoutParams);
        holder.textView.setLayoutParams(setBelowfoImageView(holder));
    }

    private void setSmaillImageLayouParams(ViewHolder holder) {
        //RelativeLayout.CENTER_IN_PARENT  相对于父控件完全居中
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(UIUtils.dp2px(35), UIUtils.dip2px(35));
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        holder.imageView.setLayoutParams(layoutParams);
        holder.textView.setLayoutParams(setBelowfoImageView(holder));
    }


    private RelativeLayout.LayoutParams setBelowfoImageView(ViewHolder holder) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                UIUtils.dp2px(25));
        //layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        layoutParams.addRule(RelativeLayout.BELOW, holder.imageView.getId());  //子控件相对于控件：imageViewId在其的下面
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        return layoutParams;
    }

    @Override
    public int getItemCount() {
        return mTitles.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlContent;
        ImageView imageView;
        TextView textView;
        private ViewHolder(View itemView) {
            super(itemView);
        }

    }

}
