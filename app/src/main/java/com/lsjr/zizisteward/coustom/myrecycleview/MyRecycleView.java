package com.lsjr.zizisteward.coustom.myrecycleview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/5/23.
 */

public class MyRecycleView extends RecyclerView {


    private List<View> mHeaderView=new ArrayList<>();
    private List<View> mFooterView=new ArrayList<>();
    private Adapter mAdapter;

    public MyRecycleView(Context context) {
        this(context,null);
    }

    public MyRecycleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void addHeaderView(View view){
        mHeaderView.add(view);
        if (mAdapter!=null){
            if (!(mAdapter instanceof MyRecycleViewAdapter)){
                mAdapter=new MyRecycleViewAdapter(mAdapter);
            }
        }
    }


    public void addFooterView(View view){
        mFooterView.add(view);
        if (mAdapter!=null&&!(mAdapter  instanceof MyRecycleViewAdapter)){
            mAdapter=new MyRecycleViewAdapter(mAdapter);
        }
    }

    public void setmAdapter(Adapter mAdapter) {
        if (mHeaderView.size()>0||mFooterView.size()>0){
            this.mAdapter=new MyRecycleViewAdapter(mAdapter);
        }else {
            this.mAdapter = mAdapter;
        }
        super.setAdapter(mAdapter);
    }

    public class MyRecycleViewAdapter extends Adapter{

        private Adapter mAdapter;
        public MyRecycleViewAdapter(Adapter adapter) {
            this.mAdapter=adapter;
            if (mHeaderView==null){
                mHeaderView=new ArrayList<>();
            }
            if (mFooterView==null){
                mFooterView=new ArrayList<>();
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType==RecyclerView.INVALID_TYPE){
                return new HeaderViewHolder(mHeaderView.get(0));
            }else if (viewType==RecyclerView.INVALID_TYPE-1){
                return new HeaderViewHolder(mFooterView.get(0));
            }
            return mAdapter.createViewHolder(parent,viewType);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            int numHeader=getHeaderSize();
            if (position<numHeader){
                return;
            }
            final int  adjPosition=position-numHeader;
            int adapterCount=0;
            if (mAdapter!=null){
                adapterCount=mAdapter.getItemCount();
                if (adjPosition<adapterCount){
                    mAdapter.onBindViewHolder(holder,adjPosition);
                    return;
                }
            }
        }

        @Override
        public int getItemViewType(int position) {
            int numHeader=getHeaderSize();
            //判断当前条目是什么类型的---决定渲染什么视图给什么数据
            if (position<numHeader){
                return RecyclerView.INVALID_TYPE;
            }
            // 正常条目
            final int adjPosition=position-numHeader;
            int adapterCount;
            if (mAdapter!=null){
                adapterCount=mAdapter.getItemCount();
                if (adjPosition<adapterCount){
                    return mAdapter.getItemViewType(adjPosition);
                }
            }
            //foot类型
            return RecyclerView.INVALID_TYPE-1;

        }

        @Override
        public int getItemCount() {
            if (mAdapter!=null){
                return getHeaderSize()+getFooterSize()+mAdapter.getItemCount();
            }else {
                return getHeaderSize()+getFooterSize();
            }
        }

        public int getHeaderSize(){
            return mHeaderView.size();
        }
        public int getFooterSize(){
            return mFooterView.size();
        }

    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View view) {
            super(view);
        }
    }
}
