package com.andview.listener;

import android.view.MotionEvent;
import android.view.View;

import com.andview.myrvview.LQRViewHolder;

/**
 * CSDN_LQR
 * item的触摸回调
 */
public interface OnItemTouchListener {
    boolean onItemTouch(LQRViewHolder helper, View childView, MotionEvent event, int position);
}
