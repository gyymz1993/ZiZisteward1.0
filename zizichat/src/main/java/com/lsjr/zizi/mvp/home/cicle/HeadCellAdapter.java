package com.lsjr.zizi.mvp.home.cicle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lsjr.zizi.R;
import com.ys.uilibrary.base.RVBaseCell;
import com.ys.uilibrary.base.RVBaseViewHolder;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/8/16 14:40
 */

public class HeadCellAdapter extends RVBaseCell {

    public final static int TYPE_HEAD = 0;

    public HeadCellAdapter(Object o) {
        super(o);
    }


    @Override
    public int getItemType() {
        return TYPE_HEAD;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View headView = LayoutInflater.from(parent.getContext()).inflate(R.layout.head_circle, parent, false);
        return new RVBaseViewHolder(headView);
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {

    }
}
