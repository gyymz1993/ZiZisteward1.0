package com.andview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class ABaseRefreshAdapter<T> extends BaseRecyclerAdapter<BaseRecyclerHolder> {
    private Context context;
    private int layouId;
    private List<T> mDatas;

    public ABaseRefreshAdapter(Context context, List<T> datas, int itemLayoutId) {
        this.context = context;
        this.mDatas = datas;
        this.layouId = itemLayoutId;
        this.setListData(this.mDatas);
    }

    protected abstract void convert(BaseRecyclerHolder var1, T var2, int var3);


    @Override
    public BaseRecyclerHolder getViewHolder(View view) {
        return new BaseRecyclerHolder(view);
    }

    @Override
    public BaseRecyclerHolder onCreateViewHolder(ViewGroup viewGroup, int i, boolean b) {
        View view = LayoutInflater.from(this.context).inflate(this.layouId, viewGroup, false);
        return new BaseRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerHolder baseRecyclerHolder, int position, boolean b) {
        this.convert(baseRecyclerHolder, this.mDatas.get(position), position);
    }

    @Override
    public int getAdapterItemCount() {
        return mDatas.size();
    }

    public void notifyDataSetChanged(List<T> list) {
        this.mDatas = list;
        notifyDataSetChanged();
    }

    public void setListData(List<T> list) {
        if (list == null) {
            list = new ArrayList(0);
        }

        this.mDatas = (List) list;
        this.notifyDataSetChanged();
    }
}