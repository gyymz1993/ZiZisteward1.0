package com.ymz.baselibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.ymz.baselibrary.utils.UIUtils;
import com.ymz.baselibrary.view.PermissionListener;
import com.ymz.baselibrary.view.ProgressDialogUtils;
import com.ymz.baselibrary.widget.NavigationBarView;
import com.ymz.baselibrary.widget.NetworkStateView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/6/28 14:21
 */

public abstract class ABaseActivity extends AppCompatActivity implements NetworkStateView.OnRefreshListener {

    private Unbinder unbinder;

    private ProgressDialogUtils progressDialog;

    private NetworkStateView networkStateView;

    private static PermissionListener mPermissionListener;
    private static final int CODE_REQUEST_PERMISSION = 1;

    private OnTopBarClickListener onTopBarLeftListener;
    private OnTopBarClickListener onTopBarRightListener;
    NavigationBarView toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        // ActivityUtils.addActivity(this);
        initDialog();
        afterCreate(savedInstanceState);
        // 初始化界面
        initView();

        // 初始化头部
        initTitle();

        // 初始化数据
        initData();

    }

    // 初始化数据
    protected abstract void initData();

    // 初始化界面
    protected abstract void initView();

    // 初始化头部
    protected abstract void initTitle();

    @SuppressLint("InflateParams")
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View view = getLayoutInflater().inflate(R.layout.activity_a_base, null);
        //设置填充activity_base布局
        super.setContentView(view);

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            view.setFitsSystemWindows(true);
        }
        //加载子类Activity的布局
        initDefaultView(layoutResID);
    }

    /**
     * 初始化默认布局的View
     *
     * @param layoutResId 子View的布局id
     */
    @SuppressLint("RestrictedApi")
    private void initDefaultView(int layoutResId) {
        networkStateView = (NetworkStateView) findViewById(R.id.nsv_state_view);
        toolbar = (NavigationBarView) findViewById(R.id.id_bar_view);
        toolbar.setVisibility(View.GONE);
        toolbar.setLetfIocnOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        FrameLayout container = (FrameLayout) findViewById(R.id.fl_activity_child_container);
        View childView = LayoutInflater.from(this).inflate(layoutResId, null);
        container.addView(childView, 0);
    }

    public void setToolbarHight(int height){
        toolbar.setTorBarTHeight(height);
    }

    public void setToolbarColor(int color){
        toolbar.setBackgroundColor(color);
    }

    public NavigationBarView getToolBarView(){
        return toolbar;
    }

    public void setHideTitleView(){
        toolbar.setVisibility(View.GONE);
    }

    public void setTitleText(String title) {
        toolbar.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(title)) {
            toolbar.setTitleText(title);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (onTopBarLeftListener != null) {
                onTopBarLeftListener.onClick();
            }
        }
        return true;
    }

    protected void setTopLeftButton(int iconResId, View.OnClickListener onTopBarLeftListener) {
        toolbar.setleftImageResource(UIUtils.getDrawable(iconResId));
        toolbar.setLetfIocnOnClickListener(onTopBarLeftListener);
    }

    /*默认 是返回键*/
    protected void setTopLeftButton(int iconResId) {
        toolbar.setleftImageResource(UIUtils.getDrawable(iconResId));
        toolbar.setLetfIocnOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    protected void setTopRightButton(int iconResId, View.OnClickListener onTopBarRightListener) {
        toolbar.setRightImageResource(UIUtils.getDrawable(iconResId));
        toolbar.setRightTextOnClickListener(onTopBarRightListener);
    }

    protected void setTopRightButton(View.OnClickListener onTopBarRightListener) {
        toolbar.setRightIocnOnClickListener(onTopBarRightListener);
    }


    public interface OnTopBarClickListener {
        void onClick();
    }

    protected abstract int getLayoutId();

    protected abstract void afterCreate(Bundle savedInstanceState);

    private void initDialog() {
        progressDialog = new ProgressDialogUtils(this, R.style.dialog_transparent_style);
    }

    /**
     * 显示加载中的布局
     */
    public void showLoadingView() {
        networkStateView.showLoading();
    }

    /**
     * 显示加载完成后的布局(即子类Activity的布局)
     */
    public void showContentView() {
        networkStateView.showSuccess();
    }

    /**
     * 显示没有网络的布局
     */
    public void showNoNetworkView() {
        networkStateView.showNoNetwork();
        networkStateView.setOnRefreshListener(this);
    }

    /**
     * 显示没有数据的布局
     */
    public void showEmptyView() {
        networkStateView.showEmpty();
        networkStateView.setOnRefreshListener(this);
    }

    /**
     * 显示数据错误，网络错误等布局
     */
    public void showErrorView() {
        networkStateView.showError();
        networkStateView.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        onNetworkViewRefresh();
    }

    /**
     * 重新请求网络
     */
    public void onNetworkViewRefresh() {
    }

    /**
     * 显示加载的ProgressDialog
     */
    public void showProgressDialog() {
        progressDialog.showProgressDialog();
    }

    /**
     * 显示有加载文字ProgressDialog，文字显示在ProgressDialog的下面
     *
     * @param text 需要显示的文字
     */
    public void showProgressDialogWithText(String text) {
        progressDialog.showProgressDialogWithText(text);
    }

    /**
     * 显示加载成功的ProgressDialog，文字显示在ProgressDialog的下面
     *
     * @param message 加载成功需要显示的文字
     * @param time    需要显示的时间长度(以毫秒为单位)
     */
    public void showProgressSuccess(String message, long time) {
        progressDialog.showProgressSuccess(message, time);
    }

    /**
     * 显示加载成功的ProgressDialog，文字显示在ProgressDialog的下面
     * ProgressDialog默认消失时间为1秒(1000毫秒)
     *
     * @param message 加载成功需要显示的文字
     */
    public void showProgressSuccess(String message) {
        progressDialog.showProgressSuccess(message);
    }

    /**
     * 显示加载失败的ProgressDialog，文字显示在ProgressDialog的下面
     *
     * @param message 加载失败需要显示的文字
     * @param time    需要显示的时间长度(以毫秒为单位)
     */
    public void showProgressFail(String message, long time) {
        progressDialog.showProgressFail(message, time);
    }

    /**
     * 显示加载失败的ProgressDialog，文字显示在ProgressDialog的下面
     * ProgressDialog默认消失时间为1秒(1000毫秒)
     *
     * @param message 加载成功需要显示的文字
     */
    public void showProgressFail(String message) {
        progressDialog.showProgressFail(message);
    }

    /**
     * 隐藏加载的ProgressDialog
     */
    public void dismissProgressDialog() {
        progressDialog.dismissProgressDialog();
    }

    /**
     * 申请权限
     *
     * @param permissions 需要申请的权限(数组)
     * @param listener    权限回调接口
     */
    public static void requestPermissions(String[] permissions, PermissionListener listener) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public void openActivity(Class<?> pClass, Bundle bundle) {
        Intent intent = new Intent(this, pClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void openActivity(Class<?> pClass) {
        Intent intent = new Intent(this, pClass);
        startActivity(intent);
    }

    /*跳转到登录页面  登录成功回调到刚刚页面*/
    public void loginToServer(Class<?> c, Activity resultActivcity) {
        Intent loginIntent = new Intent(this, resultActivcity.getClass());
        loginIntent.putExtra("", c);
        startActivity(loginIntent);
        finish();
    }


    //添加fragment
    protected void addFragment(ABaseFragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(getFragmentContentId(), fragment, fragment.getClass().getSimpleName())
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commitAllowingStateLoss();
        }
    }

    //移除fragment
    protected void removeFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    public abstract int getFragmentContentId();
}
