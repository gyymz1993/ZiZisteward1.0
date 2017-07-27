package com.lsjr.zizisteward.coustom;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by admin on 2017/5/20.
 */

public class MySwipeRefreshLayout extends SwipeRefreshLayout {


    private float downX;
    private float downY;
    private int mTouchSlop;
    public MySwipeRefreshLayout(Context context) {
        this(context,null);
    }

    public MySwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action= MotionEventCompat.getActionMasked(ev);
        switch (action){
            case  MotionEvent.ACTION_DOWN:
                downY=ev.getY();
                downX=ev.getX();
                return super.onInterceptTouchEvent(ev);
            case MotionEvent.ACTION_MOVE:
                float lastX=ev.getRawX();
                float lastY=ev.getRawY();

                float distanceX=lastX-downX;
                float distanceY=lastY-downY;

                /*反向*/
                float distanceXX=downX-lastX;
               if (Math.abs(distanceX)>0){
                    //右滑动
                   if (Math.abs(distanceX)>Math.abs(distanceY)){
                       return false;
                   }else {
                       return super.onInterceptTouchEvent(ev);
                   }
                }else {
                   //左边滑动
                   if (Math.abs(distanceXX)>Math.abs(distanceY)) {
                       return false;
                   }else {
                       super.onInterceptTouchEvent(ev);
                   }
               }
            case MotionEvent.ACTION_UP:
                downY=0;
                downX=0;
                return super.onInterceptTouchEvent(ev);
        }
        return false;
    }
}
