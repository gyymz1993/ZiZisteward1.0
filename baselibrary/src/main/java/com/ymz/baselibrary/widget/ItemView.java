package com.ymz.baselibrary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ymz.baselibrary.R;


public class ItemView extends RelativeLayout {

    /**
     * 标题
     */
    private String title = "";
    /**
     * 标题字体大小
     */
    private float titleTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
            16, getResources().getDisplayMetrics());
    /**
     * 标题颜色
     */
    private int titleTextColor = Color.BLACK;
    /**
     * 左边文字
     */
    private String leftText = "";


    /**
     * 左边  左图又文
     */
    private String lefttoRightText = "";

    /**
     * 左边文字大小
     */
    private float leftTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
            16, getResources().getDisplayMetrics());
    /**
     * 左字左边距
     */
    private int leftTextMarginLeft = -1;
    /**
     * 左图左边距
     */
    private int leftImageMarginLeft = -1;
    /**
     * 左图右边距
     */
    private int leftImageMarginRight = -1;
    /**
     * 左边文字颜色
     */
    private int leftTextColor = Color.BLACK;
    /**
     * 右边文字
     */
    private String rightText = "";
    /**
     * 右边文字大小
     */
    private float rightTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
            16, getResources().getDisplayMetrics());
    /**
     * 右边文字颜色
     */
    private int rightTextColor = Color.BLACK;
    /**
     * 右字右边距
     */
    private int rightTextMarginRight = -1;
    /**
     * 右图左边距
     */
    private int rightImageMarginLeft = -1;
    /**
     * 右图右边距
     */
    private int rightImageMarginRight = -1;


    private int iconLeft;
    private int  iconRight;
    private ImageView switchView;

    public void setSwitchViewListenern(OnClickListener listener) {
        switchView.setOnClickListener(listener);
    }

    public ItemView(Context context) {
        this(context,null);
    }
    public ItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemView);
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.ItemView_left_src) {
                iconLeft = typedArray.getResourceId(attr, 0);
            } else if (attr == R.styleable.ItemView_right_src) {
                iconRight = typedArray.getResourceId(attr, 0);
            } else if (attr == R.styleable.ItemView_title_size) {
                titleTextSize = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                        16, getResources().getDisplayMetrics()));

            } else if (attr == R.styleable.ItemView_title_color) {
                titleTextColor = typedArray.getColor(attr, Color.BLACK);

            } else if (attr == R.styleable.ItemView_title) {
                title = typedArray.getString(attr);

            } else if (attr == R.styleable.ItemView_left_text) {
                leftText = typedArray.getString(attr);

            }   else if (attr == R.styleable.ItemView_to_left_img_text) {
                lefttoRightText = typedArray.getString(attr);
            }else if (attr == R.styleable.ItemView_left_text_size) {
                leftTextSize = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                        16, getResources().getDisplayMetrics()));

            } else if (attr == R.styleable.ItemView_left_text_margin_left) {
                leftTextMarginLeft = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        -1, getResources().getDisplayMetrics()));
            } else if (attr == R.styleable.ItemView_left_image_margin_left) {
                leftImageMarginLeft = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        -1, getResources().getDisplayMetrics()));
            } else if (attr == R.styleable.ItemView_left_image_margin_right) {
                leftImageMarginRight = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        -1, getResources().getDisplayMetrics()));
            } else if (attr == R.styleable.ItemView_left_text_color) {
                leftTextColor = typedArray.getColor(attr, Color.BLACK);

            } else if (attr == R.styleable.ItemView_right_text) {
                rightText = typedArray.getString(attr);
            } else if (attr == R.styleable.ItemView_right_text_size) {
                rightTextSize = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                        16, getResources().getDisplayMetrics()));
            } else if (attr == R.styleable.ItemView_right_text_margin_right) {
                rightTextMarginRight = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        -1, getResources().getDisplayMetrics()));
            } else if (attr == R.styleable.ItemView_right_image_margin_left) {
                rightImageMarginLeft = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        -1, getResources().getDisplayMetrics()));
            } else if (attr == R.styleable.ItemView_right_image_margin_right) {
                rightImageMarginRight = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        -1, getResources().getDisplayMetrics()));
            } else if (attr == R.styleable.ItemView_right_text_color) {
                rightTextColor = typedArray.getColor(attr, Color.BLACK);
            }
        }
        typedArray.recycle();    //回收typeArray
        init();

    }

    TextView tv_right_text;
    View view;
    RelativeLayout id_item_rl;
    private void init() {
        view = LayoutInflater.from(getContext()).inflate(R.layout.view_item, this);
        id_item_rl = (RelativeLayout) view.findViewById(R.id.id_item_rl);
        TextView tv_left = (TextView) view.findViewById(R.id.tv_left);
        ImageView left_image = (ImageView) view.findViewById(R.id.left_image);
        TextView to_right_leftig_text = (TextView) view.findViewById(R.id.to_right_leftig_text);
        TextView valueTv = (TextView) view.findViewById(R.id.valueTv);
        tv_right_text = (TextView) view.findViewById(R.id.right_text);
        ImageView right_icon = (ImageView) view.findViewById(R.id.right_icon);
        if (!TextUtils.isEmpty(leftText)){
            tv_left.setText(leftText);
        }
        if (iconLeft!=0){
            left_image.setImageResource(iconLeft);
        }
        if (iconRight!=0){
            right_icon.setImageResource(iconRight);
        }
        if (!TextUtils.isEmpty(rightText)){
            tv_right_text.setText(rightText);
        }
        if (!TextUtils.isEmpty(lefttoRightText)){
            to_right_leftig_text.setText(lefttoRightText);
        }
    }

    public void setItemOnclickListener(OnClickListener onclickListener){
        id_item_rl.setOnClickListener(onclickListener);
    }
    public void setRightText(String rightText){
        if (!TextUtils.isEmpty(rightText)){
            tv_right_text.setText(rightText);
        }
    }

}
