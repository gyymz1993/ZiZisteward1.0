package com.lsjr.zizi.mvp.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lsjr.zizi.AppConfig;
import com.lsjr.zizi.R;
import com.lsjr.zizi.base.MvpFragment;
import com.lsjr.zizi.mvp.chat.ConfigApplication;
import com.lsjr.zizi.mvp.chat.helper.AvatarHelper;
import com.lsjr.zizi.mvp.home.photo.SingleImagePreviewActivity;
import com.ymz.baselibrary.mvp.BasePresenter;

import butterknife.BindView;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/8/15 16:11
 */

public class MeFragment extends MvpFragment {

    @BindView(R.id.ivHeader)
    ImageView mAvatarImg;
    @BindView(R.id.tvName)
    TextView mTvName;
    @BindView(R.id.tvAccount)
    TextView mTvAccount;
    @BindView(R.id.ivQRCordCard)
    ImageView mIvQRCordCard;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initView() {
        mIvQRCordCard.setVisibility(View.GONE);
        String loginUserId = ConfigApplication.instance().mLoginUser.getUserId();
        AvatarHelper.getInstance().displayAvatar(loginUserId, mAvatarImg, true);
        mTvName.setText(ConfigApplication.instance().mLoginUser.getNickName());
        mTvAccount.setText(ConfigApplication.instance().mLoginUser.getTelephone());
        mAvatarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginUserId = ConfigApplication.instance().mLoginUser.getUserId();
                Intent intent = new Intent(getActivity(), SingleImagePreviewActivity.class);
                intent.putExtra(AppConfig.EXTRA_IMAGE_URI, AvatarHelper.getAvatarUrl(loginUserId, false));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {

    }
}
