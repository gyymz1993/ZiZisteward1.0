package com.ymz.baselibrary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ymz.baselibrary.R;
import com.ymz.baselibrary.utils.UIUtils;


/**
 * @author: gyymz1993
 * 创建时间：2017/4/26 16:54
 **/
public class NavigationBarView extends RelativeLayout {

    private String letfText;
    private String rightText;
    private int letfTextColor;
    private int rightTextColor;
    private Drawable leftIcon;
    private Drawable rightIcon;
    private int bgColor;
    public String titleText;
    public View mView;
    private int height;
    private int titleColor;

    public NavigationBarView(Context context) {
        this(context, null);
    }

    public NavigationBarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public NavigationBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {// 得到自定义属性
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TopBarItem);
            leftIcon = ta.getDrawable(R.styleable.TopBarItem_leftIcon);
            rightIcon = ta.getDrawable(R.styleable.TopBarItem_rgihtIcon);
            titleText = ta.getString(R.styleable.TopBarItem_titles);
            letfText = ta.getString(R.styleable.TopBarItem_leftText);
            rightText = ta.getString(R.styleable.TopBarItem_rightText);
            height = ta.getInteger(R.styleable.TopBarItem_topBarheight,UIUtils.dip2px(50));
            bgColor = ta.getColor(R.styleable.TopBarItem_backgrounds, Color.parseColor("#FFFFFFFF"));
            letfTextColor = ta.getColor(R.styleable.TopBarItem_leftColor, Color.parseColor("#FF212121"));
            rightTextColor = ta.getColor(R.styleable.TopBarItem_rightColor, Color.parseColor("#FF212121"));
            ta.recycle();
        }
        mView = View.inflate(context, R.layout.navigation_default, this);// 将view添加。
        if (letfText != null) {
            setLeftText(letfText);
        }
        if (rightText != null) {
            setRightText(rightText);
        }
        if (leftIcon != null) {
            setleftImageResource(leftIcon);
        }else {
            setleftImageResource(UIUtils.getDrawable(R.drawable.icon_back));
        }
        if (rightIcon != null) {
            setRightImageResource(rightIcon);
        }else {
            setRightImageResource(UIUtils.getDrawable(R.drawable.icon_share));
        }
        if (titleText != null) {
            setTitleText(titleText);
        }
        if (height!=0){
            setTorBarTHeight(height);
        }
        if (titleColor!=0){
            setTitleTextColor(titleColor);
        }else {
            setTitleTextColor(Color.parseColor("#FF212121"));
        }
        setBackgroundColor(bgColor);
        setLeftTextColor(letfTextColor);
        setRightTextColor(rightTextColor);
        getRightImageView().setVisibility(GONE);
        getRightTextView().setVisibility(GONE);
    }

    public ImageView getLeftimageView() {
        return viewFindById(R.id.iv_left);
    }

    public <T extends View> T viewFindById(int id) {
        return (T) mView.findViewById(id);
    }

    public NavigationBarView setTitleText(String text) {
        TextView textView = viewFindById(R.id.title_tv);
        if (textView != null && text != null) {
            textView.setText(text);
        }
        return this;
    }

    public NavigationBarView setTitleTextColor(int color) {
        TextView textView = viewFindById(R.id.title_tv);
        if (textView != null && color != 0) {
            textView.setTextColor(color);
        }
        return this;
    }


    public NavigationBarView setTorBarTHeight(int height) {
        RelativeLayout relativeLayout = viewFindById(R.id.title_bar);
        ViewGroup.LayoutParams layoutParams = relativeLayout.getLayoutParams();
        layoutParams.width= UIUtils.WHD()[0];
        layoutParams.height=height;
        return this;
    }

    public NavigationBarView setLeftText(CharSequence text) {
        TextView textView = viewFindById(R.id.tv_left);
        if (textView != null && text != null) {
            textView.setText(text);
        }
        return this;
    }

    public NavigationBarView setRightText(CharSequence text) {
        TextView textView = viewFindById(R.id.tv_right);
        if (textView != null && text != null) {
            textView.setText(text);
        }
        return this;
    }

    public void setBackgroundColor(int color) {
        View view = viewFindById(R.id.title_bar);
        if (view != null && color != 0) {
            view.setBackgroundColor(color);
        }
    }

    public NavigationBarView setTopBarBackgroundColor(int color) {
        View view = viewFindById(R.id.title_bar);
        if (view != null && color != 0) {
            view.setBackgroundColor(color);
        }
        return this;
    }

    public NavigationBarView setLeftTextColor(int color) {
        TextView view = viewFindById(R.id.tv_left);
        if (view != null && color != 0) {
            view.setTextColor(color);
        }
        return this;
    }

    public NavigationBarView setRightTextColor(int color) {
        TextView view = viewFindById(R.id.tv_right);
        if (view != null && color != 0) {
            view.setTextColor(color);
        }
        return this;
    }



    public NavigationBarView setLetfIocnOnClickListener(View.OnClickListener listener) {
        View view = viewFindById(R.id.iv_left);
        if (view != null) {
            view.setOnClickListener(listener);
        }
        return this;
    }


    public NavigationBarView setRightIocnOnClickListener(View.OnClickListener listener) {
        View view = viewFindById(R.id.iv_right);
        if (view != null) {
            view.setOnClickListener(listener);
        }
        return this;
    }

    public NavigationBarView setRightTextOnClickListener(View.OnClickListener listener) {
        View view = viewFindById(R.id.tv_right);
        if (view != null) {
            view.setOnClickListener(listener);
        }
        return this;
    }

    /**
     * 设置背景资源
     *
     * @param resourceId
     */
    public NavigationBarView setleftImageResource(Drawable resourceId) {
        ImageView imageView = viewFindById(R.id.iv_left);
        if (imageView != null) {
            imageView.setImageDrawable(resourceId);
        }
        return this;
    }

    /**
     * 设置背景资源
     *
     * @param resourceId
     */
    public NavigationBarView setRightImageResource(Drawable resourceId) {
        ImageView imageView = viewFindById(R.id.iv_right);
        if (imageView != null) {
            imageView.setImageDrawable(resourceId);
        }
        return this;
    }

    public ImageView getRightImageView() {
        return viewFindById(R.id.iv_right);
    }

    public TextView getRightTextView() {
        return viewFindById(R.id.tv_right);
    }

    public TextView getLeftTextView() {
        return viewFindById(R.id.tv_left);
    }


}
