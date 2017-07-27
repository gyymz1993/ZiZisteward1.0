package com.lsjr.zizisteward.contrl.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.contrl.PathMenu;
import com.ymz.baselibrary.utils.L_;

import java.util.List;

/**
 * 子菜单项布
 */
public class ArcLayout extends ViewGroup {
    private int mChildSize; // 子菜单项大小相同
    private int mChildPadding = 5;
    public static final float DEFAULT_FROM_DEGREES = 270.0f;
    public static final float DEFAULT_TO_DEGREES = 480.0f;
    private float mFromDegrees = DEFAULT_FROM_DEGREES;
    private float mToDegrees = DEFAULT_TO_DEGREES;
    private static final int DEFAULT_MIN_RADIUS = ViewUtil.dp2px(50);
    private static int MIN_RADIUS = DEFAULT_MIN_RADIUS;
    private int mRadius;// 中心菜单圆点到子菜单中心的距离
    private boolean mExpanded = false;

    private int position = PathMenu.LEFT_TOP;
    private int centerX = 0;
    private int centerY = 0;

    public void computeCenterXY(int position) {
        switch (position) {
            case PathMenu.LEFT_TOP://左上
                centerX = getWidth() / 2 - getRadiusAndPadding();
                centerY = getHeight() / 2 - getRadiusAndPadding();
                break;
            case PathMenu.LEFT_CENTER://左中
                centerX = getWidth() / 2 - getRadiusAndPadding();
                centerY = getHeight() / 2;
                break;
            case PathMenu.LEFT_BOTTOM://左下
                centerX = getWidth() / 2 - getRadiusAndPadding();
                centerY = getHeight() / 2 + getRadiusAndPadding();
                break;
            case PathMenu.CENTER_TOP://上中
                centerX = getWidth() / 2;
                centerY = getHeight() / 2 - getRadiusAndPadding();
                break;
            case PathMenu.CENTER_BOTTOM://下中
                centerX = getWidth() / 2;
                centerY = getHeight() / 2 + getRadiusAndPadding();
                break;
            case PathMenu.RIGHT_TOP://右上
                centerX = getWidth() / 2 + getRadiusAndPadding();
                centerY = getHeight() / 2 - getRadiusAndPadding();
                break;
            case PathMenu.RIGHT_CENTER://右中
                centerX = getWidth() / 2 + getRadiusAndPadding();
                centerY = getHeight() / 2;
                break;
            case PathMenu.RIGHT_BOTTOM://右下
                centerX = getWidth() / 2 + getRadiusAndPadding();
                centerY = getHeight() / 2 + getRadiusAndPadding();
                break;

            case PathMenu.CENTER:
                centerX = getWidth() / 2;
                centerY = getHeight() / 2;
                break;

        }

        L_.e("测量位置  centerX："+centerX+" centerY:  "+centerY);
    }

    private int getRadiusAndPadding() {
        return mRadius + (mChildPadding * 2);
    }

    public ArcLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 获取自定义属性，设定默认值
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs,
                    R.styleable.ArcLayout, 0, 0);
            mFromDegrees = a.getFloat(R.styleable.ArcLayout_fromDegrees,
                    DEFAULT_FROM_DEGREES);
            mToDegrees = a.getFloat(R.styleable.ArcLayout_toDegrees,
                    DEFAULT_TO_DEGREES);
            mChildSize = Math.max(a.getDimensionPixelSize(R.styleable.ArcLayout_childSize, 0), 0);
            a.recycle();
        }
    }

    /**
     * 计算半径
     */
    private static int computeRadius(final float arcDegrees,
                                     final int childCount, final int childSize, final int childPadding,
                                     final int minRadius) {
        if (childCount < 2) {
            return minRadius;
        }
        final float perDegrees = arcDegrees == 360 ? (arcDegrees) / (childCount) : (arcDegrees) / (childCount - 1);
        final float perHalfDegrees = perDegrees / 2;
        final int perSize = childSize + childPadding;
        final int radius = (int) ((perSize / 2) / Math.sin(Math.toRadians(perHalfDegrees)));
        return Math.max(radius, minRadius);
    }

    /**
     * 计算子菜单项的范围
     */
    private static Rect computeChildFrame(int position, final int centerX, final int centerY,
                                          final int radius, final float degrees, final int size) {
        //子菜单项中心点
        final double childCenterX = centerX + radius * Math.cos(Math.toRadians(degrees));
        final double childCenterY = centerY + radius * Math.sin(Math.toRadians(degrees));
        L_.e("子菜单中心点位置centerX  :"+centerX+"centerY :"+centerY);
        int left = 0,top = 0,right = 0,bottom = 0;
//        //子菜单项的左上角，右上角，左下角，右下角
         left = (int) (childCenterX - size / 2);
         top = (int) (childCenterY - size / 2);
         right = (int) (childCenterX + size / 2);
         bottom = (int) (childCenterY + size / 2);
         L_.e("childCenterX:"+childCenterX+"childCenterY:"+childCenterY);
        L_.e("position  :"+position+"left:"+left+"top:"+top+"right:"+right+"bottom :"+bottom);
        return new Rect(left, top, right, bottom);
    }



    /**
     * 计算子菜单项的范围
     */
    private static Rect computeChildFrame1(int position, final int centerX, final int centerY,
                                          final int radius, final float degrees, final int size) {
        double distace=150;//换成千米
        double y = centerY;
        double dx = 2 * Math.asin(Math.sin(distace / (2 * radius)) / Math.cos(deg2rad(y)));
        dx = rad2deg(dx);
        double dy = distace / radius;
        dy= rad2deg(dy);
        int left = 0,top = 0,right = 0,bottom = 0;
        L_.e("position  :"+position+"    dx:   --->"+dx+"  dy:   --->"+dy);

        //子菜单项中心点
        final double childCenterX = centerX + radius * Math.cos(Math.toRadians(degrees));
        final double childCenterY = centerY + radius * Math.sin(Math.toRadians(degrees));
        L_.e("子菜单中心点位置centerX  :"+childCenterX+"centerY :"+childCenterY);
        if (position==1){
            left=171;
            top = 332;
            bottom=512;
            right= 351;
        }
        if (position==2){

            // 子菜单中心点位置centerX  :332.0 centerY :591.9999999999999
            //--->-59.91572068280288  dy:   --->35.80986219567645
//            left= 332;
//            top= 501;
//            bottom=681;
//            right = 422;
           // 2            left:92  top:532  right:151  bottom :627
           //position  :2  left:332 top:532 right:391   bottom :627
            left= (int) 322;
            top= (int) (childCenterY+dx);
            bottom= (int) (childCenterY+dy);
            right = (int) (childCenterX-dx-100);
        }
        if (position==0){
            left= 1;
            top= 262;
            bottom= 442;
            right = 181;
        }
        L_.e("position  :"+position+"left:"+left+"top:"+top+"right:"+right+"bottom :"+bottom);
        return new Rect(left, top, right, bottom);
    }


    /**
     *
     * @param center 中心坐标
     * @param distace 到中心的距离(正方形内切圆的半径）单位*米
     * @return 左上 右上 左下 右下 4个GPS坐标点
     */
    public  static void get4limit(int positon ,final int centerX, final int centerY, double distace_m) {


    }

    // 将角度转换为弧度
    private static double deg2rad(double degree) {
        return degree * Math.PI / 180.0;
    }

    // 将弧度转换为角度
    static double rad2deg(double radian) {
        return radian * 180 / Math.PI;
    }

    public int getRadius() {
        return mRadius;
    }

    public void setMinRadius(float persent) {
        MIN_RADIUS = (int) (DEFAULT_MIN_RADIUS * persent);
    }

    /**
     * 子菜单项大小
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int radius;
        if (getChildCount() == 1) {
            radius = mRadius = 2 * computeRadius(Math.abs(mToDegrees - mFromDegrees), getChildCount(),
                    mChildSize, mChildPadding, MIN_RADIUS);
        } else {
            radius = mRadius = computeRadius(Math.abs(mToDegrees - mFromDegrees), getChildCount(),
                    mChildSize, mChildPadding, MIN_RADIUS);
        }
        int layoutPadding = 10;
        int size = radius * 2 + mChildSize + mChildPadding + layoutPadding * 2;
        setMeasuredDimension(size, size);
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).measure(MeasureSpec.makeMeasureSpec(mChildSize, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(mChildSize, MeasureSpec.EXACTLY));
        }
    }

    /**
     * 子菜单项位置
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        computeCenterXY(position);
        //当子菜单要收缩时radius=0，在ViewGroup坐标中心
        final int radius = mExpanded ? mRadius : 0;
        final int childCount = getChildCount();
        final float perDegrees = Math.abs(mToDegrees - mFromDegrees) == 360 ? (Math.abs(mToDegrees - mFromDegrees)) / (childCount) : (Math.abs(mToDegrees - mFromDegrees)) / (childCount - 1);
        float degrees = mFromDegrees;
        for (int i = 0; i < childCount; i++) {
            Rect frame = computeChildFrame(i, centerX, centerY, radius, degrees, mChildSize);
            degrees += perDegrees;
            getChildAt(i).layout(frame.left, frame.top, frame.right, frame.bottom);
        }
    }

    /**
     * 计算动画开始时的偏移量
     */
    private static long computeStartOffset(final int childCount,
                                           final boolean expanded, final int index, final float delayPercent,
                                           final long duration, Interpolator interpolator) {
        final float delay = delayPercent * duration;
        final long viewDelay = (long) (getTransformedIndex(expanded,
                childCount, index) * delay);
        final float totalDelay = delay * childCount;
        float normalizedDelay = viewDelay / totalDelay;
        normalizedDelay = interpolator.getInterpolation(normalizedDelay);
        return (long) (normalizedDelay * totalDelay);
    }

    /**
     * 变换时的子菜单项索引
     */
    private static int getTransformedIndex(final boolean expanded,
                                           final int count, final int index) {
        if (expanded) {
            return count - 1 - index;
        }
        return index;
    }

    /**
     * 展开动画
     */
    private static Animation createExpandAnimation(float fromXDelta,
                                                   float toXDelta, float fromYDelta, float toYDelta, long startOffset,
                                                   long duration, Interpolator interpolator) {
        Animation animation = new RotateAndTranslateAnimation(0, toXDelta, 0,
                toYDelta, 0, 720);
        animation.setStartOffset(startOffset);
        animation.setDuration(duration);
        animation.setInterpolator(interpolator);
        animation.setFillAfter(true);
        return animation;
    }

    /**
     * 收缩动画
     */
    private static Animation createShrinkAnimation(float fromXDelta,
                                                   float toXDelta, float fromYDelta, float toYDelta, long startOffset,
                                                   long duration, Interpolator interpolator) {
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.setFillAfter(true);
        //收缩过程中，child 逆时针自旋转360度
        final long preDuration = duration / 2;
        Animation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateAnimation.setStartOffset(startOffset);
        rotateAnimation.setDuration(preDuration);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setFillAfter(true);

        animationSet.addAnimation(rotateAnimation);
        //收缩过程中位移，并逆时针旋转360度
        Animation translateAnimation = new RotateAndTranslateAnimation(0,
                toXDelta, 0, toYDelta, 360, 720);
        translateAnimation.setStartOffset(startOffset + preDuration);
        translateAnimation.setDuration(duration - preDuration);
        translateAnimation.setInterpolator(interpolator);
        translateAnimation.setFillAfter(true);

        animationSet.addAnimation(translateAnimation);

        return animationSet;
    }

    /**
     * 绑定子菜单项动画
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void bindChildAnimation(int mPosition, final View child, final int index,
                                    final long duration) {
        final boolean expanded = mExpanded;
//        final int centerX = getWidth() / 2 - mRadius;  //ViewGroup的中心X坐标
//        final int centerY = getHeight() / 2;
        computeCenterXY(position);
        final int radius = expanded ? 0 : mRadius;

        final int childCount = getChildCount();
        final float perDegrees = Math.abs(mToDegrees - mFromDegrees) == 360 ? (mToDegrees - mFromDegrees) / (childCount) : (mToDegrees - mFromDegrees) / (childCount - 1);
        Rect frame = computeChildFrame(0, centerX, centerY, radius, mFromDegrees
                + index * perDegrees, mChildSize);
        int toXDelta = frame.left - child.getLeft();//展开或收缩动画,child沿X轴位移距离
        int toYDelta = frame.top - child.getTop();//展开或收缩动画,child沿Y轴位移距离
        Interpolator interpolator = mExpanded ? new AccelerateInterpolator()
                : new OvershootInterpolator(1.5f);
        final long startOffset = computeStartOffset(childCount, mExpanded, index, 0.1f, duration, interpolator);
        //mExpanded为true，已经展开，收缩动画；为false,展开动画
        Animation animation = mExpanded ? createShrinkAnimation(0, toXDelta, 0, toYDelta, startOffset, duration, interpolator)
                : createExpandAnimation(0, 0, 0, 0, startOffset,
                duration, interpolator);

        final boolean isLast = getTransformedIndex(expanded, childCount, index) == childCount - 1;
        animation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isLast) {
                    post(new Runnable() {

                        @Override
                        public void run() {
                            onAllAnimationsEnd();
                        }
                    });
                }
            }
        });
        child.setAnimation(animation);
    }

    public boolean isExpanded() {
        return true;
    }


    /**
     * 设定弧度
     */
    public void setArc(float fromDegrees, float toDegrees, int position) {
        this.position = position;
        if (mFromDegrees == fromDegrees && mToDegrees == toDegrees) {
            return;
        }
        mFromDegrees = fromDegrees;
        mToDegrees = toDegrees;
        computeCenterXY(position);
        requestLayout();
    }

    /**
     * 设定弧度
     */
    public void setArc(float fromDegrees, float toDegrees) {
        if (mFromDegrees == fromDegrees && mToDegrees == toDegrees) {
            return;
        }

        mFromDegrees = fromDegrees;
        mToDegrees = toDegrees;
        computeCenterXY(position);
        requestLayout();
    }

    /**
     * 设定子菜单项大小
     */
    public void setChildSize(int size) {
        if (mChildSize == size || size < 0) {
            return;
        }
        mChildSize = size;
        requestLayout();
    }

    public int getChildSize() {
        return mChildSize;
    }

    /**
     * 切换中心按钮的展开缩小
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public void switchState(final boolean showAnimation, int position) {
        this.position = position;
        if (showAnimation) {
            final int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                bindChildAnimation(i, getChildAt(i), i, 300);
            }
        }
        mExpanded = !mExpanded;
        if (!showAnimation) {
            requestLayout();
        }
        invalidate();
    }

    public void setExpand(boolean expand) {
        mExpanded = expand;
    }

    /**
     * 切换中心按钮的展开缩小
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public void switchState(final boolean showAnimation) {
        if (showAnimation) {
            final int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                bindChildAnimation(i, getChildAt(i), i, 300);
            }
        }
        mExpanded = !mExpanded;
        if (!showAnimation) {
            requestLayout();
        }
        invalidate();
    }


    /**
     * 结束所有动画
     */
    private void onAllAnimationsEnd() {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).clearAnimation();
        }
        requestLayout();
    }
}