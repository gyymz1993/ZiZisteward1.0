package com.lsjr.zizisteward.activity.login.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lsjr.base.MvpActivity;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.activity.login.presenter.ThirdPartPresenter;
import com.lsjr.zizisteward.activity.login.view.IThirdPartView;
import com.lsjr.zizisteward.coustom.CodeButton;
import com.lsjr.zizisteward.coustom.adapter.MyEditTextChangeListener;
import com.ymz.baselibrary.utils.L_;
import com.ymz.baselibrary.widget.NavigationBarView;

import butterknife.BindView;

/**
 * Created by admin on 2017/5/20.
 */

public class ThirdPartActivity extends MvpActivity implements IThirdPartView {

    @BindView(R.id.tv_lg_number)
    EditText tvLgNumber;
    @BindView(R.id.btn_lg_code)
    Button btnLgCode;
    CodeButton codeButton;
    private final int M = 1000;
    @BindView(R.id.id_nativgation_view)
    NavigationBarView nativgationView;
    @BindView(R.id.lay_design_bottom_sheet)
    LinearLayout bottomSheet;
    @BindView(R.id.id_lay_login)
    LinearLayout idLayLogin;
    @BindView(R.id.id_btn_confir)
    Button idBtnConfir;
    @BindView(R.id.id_tv_status_expanded)
    TextView idTvStatusExpanded;
    @BindView(R.id.id_tv_status_collapsed)
    TextView idTvStatusCollapsed;
    @BindView(R.id.id_tv_weichatlg)
    TextView idTvWeichatlg;
    @BindView(R.id.id_tv_qqlg)
    TextView idTvQQlg;
    @BindView(R.id.id_tv_weibolg)
    TextView idTvWeibolg;

    @BindView(R.id.id_ed_code)
    EditText idEdCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handlePermission();
    }

    @Override
    protected ThirdPartPresenter createPresenter() {
        return new ThirdPartPresenter(this);
    }


    @Override
    protected void initView() {
        codeButton = new CodeButton(btnLgCode, 60 * M, M);
        btnLgCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeButton.start();
               // CodeConfig config = new CodeConfig.Builder()
//                        .codeLength(4) // 设置验证码长度
//                        .smsFromStart(133) // 设置验证码发送号码前几位数字
//                        //.smsFrom(1690123456789) // 如果验证码发送号码固定，则可以设置验证码发送完整号码
//                        .smsBodyStartWith("孜孜管家") // 设置验证码短信开头文字
//                        .smsBodyContains("验证码") // 设置验证码短信内容包含文字
//                        .build();
               // AuthCode.getInstance().with(ThirdPartActivity.this).config(config).into(idEdCode);
            }
        });
        tvLgNumber.addTextChangedListener(new MyEditTextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0 && s.toString() != null) {
                    btnLgCode.setEnabled(true);
                    idBtnConfir.setEnabled(true);
                } else {
                    btnLgCode.setEnabled(false);
                    idBtnConfir.setEnabled(false);
                }
            }
        });


        //获取到Bottom Sheet对象
        final BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        // behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                showBottomSheet(behavior);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                showBottomSheet(behavior);
            }
        });

        idTvWeichatlg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPresenter().weiChatLogin();
            }
        });

        idTvQQlg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPresenter().qqlogin(ThirdPartActivity.this);
            }
        });

        idTvWeibolg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPresenter().sinaLogin(ThirdPartActivity.this);
            }
        });

    }


    private void showBottomSheet(BottomSheetBehavior behavior) {
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            L_.e("展开状态");
            idTvStatusCollapsed.setVisibility(View.VISIBLE);
            idTvStatusExpanded.setVisibility(View.GONE);
        }
        if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            L_.e("收缩状态");
            idTvStatusCollapsed.setVisibility(View.GONE);
            idTvStatusExpanded.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void initTitle() {
       // SystemBarHelper.tintStatusBar(this, UIUtils.getColor(R.color.colorBlack));
        nativgationView.setTitleText("注册/登录");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_thirdparty;
    }

    @Override
    protected void afterCreate(Bundle bundle) {

    }

    @Override
    public int getFragmentContentId() {
        return 0;
    }


    private static final int REQUEST_PERMISSION_CODE = 0;
    /**
     * 简单处理了短信权限
     */
    private void handlePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_SMS}, REQUEST_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length != 0) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "您阻止了app读取您的短信，你可以自己手动输入验证码", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
