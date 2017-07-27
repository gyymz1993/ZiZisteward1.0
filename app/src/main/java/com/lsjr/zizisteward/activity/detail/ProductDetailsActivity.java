package com.lsjr.zizisteward.activity.detail;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lsjr.base.SwipeBackActivity;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.contrl.MenuController;
import com.ymz.baselibrary.mvp.BasePresenter;
import com.ymz.baselibrary.utils.T_;
import com.ymz.baselibrary.utils.UIUtils;

import java.util.ArrayList;

import butterknife.BindView;

import static com.lsjr.zizisteward.R.id.circle_menu_button_4;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/7/14 15:37
 */

public class ProductDetailsActivity extends SwipeBackActivity implements View.OnClickListener {

    @BindView(R.id.id_ig_dt_icon)
    ImageView idIgDtIcon;
    //扇形菜单按钮
    private int res[] = {R.id.circle_menu_button_1, R.id.circle_menu_button_2, R.id.circle_menu_button_3, circle_menu_button_4};
    private ArrayList<ImageView> imageViews = new ArrayList<>();
    //菜单是否展开的flag,false表示没展开
    private boolean mFlag = false;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_product_detials;
    }


    @Override
    protected void afterCreate(Bundle bundle) {
        setTitleText("商品详情");
        //getToolBarView().setTitleTextColor(UIUtils.getColor("#21212"));
        idIgDtIcon.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
        idIgDtIcon.getLayoutParams().height = UIUtils.WHD()[0] * 2 / 3;
        setTopRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T_.showToastReal("点击分享");
            }
        });
        for (int i = 0; i < res.length; i++) {
            ImageView imageView = (ImageView) findViewById(res[i]);
            imageView.setOnClickListener(this);
            imageViews.add(imageView);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.circle_menu_button_4) {
            if (!mFlag) {
                showEnterAnim(100); //100为扇形半径dp值
            }
        }
    }

    //显示扇形菜单的属性动画
    private void showEnterAnim(int dp) {
        //for循环来开始小图标的出现动画
        for (int i = 1; i < res.length; i++) {
            AnimatorSet set = new AnimatorSet();
            double x = -Math.cos(0.5 / (res.length - 2) * (i - 1) * Math.PI) * UIUtils.dip2px(dp);
            double y = -Math.sin(0.5 / (res.length - 2) * (i - 1) * Math.PI) * UIUtils.dip2px(dp);
            if (i == 2) {
                x = -300.0;
                y = -300.0;
            }
            set.playTogether(
                    ObjectAnimator.ofFloat(imageViews.get(i), "translationX", (float) (x * 0.25), (float) x),
                    ObjectAnimator.ofFloat(imageViews.get(i), "translationY", (float) (y * 0.25), (float) y)
                    , ObjectAnimator.ofFloat(imageViews.get(i), "alpha", 0, 1).setDuration(2000)
            );
            set.setInterpolator(new BounceInterpolator());
            set.setDuration(500).setStartDelay(100 * i);
            set.start();
        }
        //转动加号大图标本身45°
        ObjectAnimator rotate = ObjectAnimator.ofFloat(imageViews.get(0), "rotation", 0, 45).setDuration(300);
        rotate.setInterpolator(new BounceInterpolator());
        rotate.start();

        //菜单状态置打开
        mFlag = true;
    }


}
