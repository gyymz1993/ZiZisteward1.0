package com.lsjr.zizi.mvp;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;

import com.lsjr.zizi.R;
import com.lsjr.zizi.base.MvpActivity;
import com.lsjr.zizi.mvp.adapter.HomeAadapter;
import com.lsjr.zizi.mvp.chat.ConfigApplication;
import com.lsjr.zizi.mvp.chat.Constants;
import com.lsjr.zizi.mvp.chat.broad.CardcastUiUpdateUtil;
import com.lsjr.zizi.mvp.chat.broad.MsgBroadcast;
import com.lsjr.zizi.mvp.chat.dao.FriendDao;
import com.lsjr.zizi.mvp.chat.dao.UserDao;
import com.lsjr.zizi.mvp.chat.db.NewFriendMessage;
import com.lsjr.zizi.mvp.chat.db.User;
import com.lsjr.zizi.mvp.chat.helper.LoginHelper;
import com.lsjr.zizi.mvp.chat.xmpp.CoreService;
import com.lsjr.zizi.mvp.chat.xmpp.ListenerManager;
import com.lsjr.zizi.mvp.chat.xmpp.listener.AuthStateListener;
import com.ymz.baselibrary.mvp.BasePresenter;
import com.ymz.baselibrary.utils.L_;
import com.ymz.baselibrary.utils.SpUtils;
import com.ymz.baselibrary.utils.T_;
import com.ymz.baselibrary.utils.UIUtils;

import april.yun.ISlidingTabStrip;
import april.yun.JPagerSlidingTabStrip2;
import april.yun.other.JTabStyleDelegate;
import butterknife.BindView;

import static april.yun.other.JTabStyleBuilder.STYLE_DEFAULT;

/**
 * 创建人：gyymz1993
 * 创建时间：2017/5/29/3:12
 **/
public class HomeActivity extends MvpActivity implements AuthStateListener {

    @BindView(R.id.tab_buttom)
    JPagerSlidingTabStrip2 tabBtn;

    @BindView(R.id.pager)
    ViewPager mViewPager;
    private boolean mBind;
    private CoreService mXmppService;
    private ActivityManager mActivityManager;


    @Override
    protected void initView() {
        super.initView();
        setTitleText("消息列表");
        setTitleText("消息列表").setTitleTextColor(UIUtils.getColor(R.color.white));
        getToolBarView().setBackgroundColor(UIUtils.getColor(R.color.colorPrimary));
        setupTabStrips();
        setupViewpager();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.chat_activity_home;
    }

    private void setupTabStrips() {
        setupStrip(tabBtn.getTabStyleDelegate(), STYLE_DEFAULT);
        tabBtn.getTabStyleDelegate()
                .setIndicatorHeight(0)
                .setDividerColor(Color.TRANSPARENT);
        tabBtn.getTabStyleDelegate()
                .setFrameColor(Color.TRANSPARENT)
                .setIndicatorColor(Color.TRANSPARENT)
                .setTabIconGravity(Gravity.TOP)//图标显示在top
                .setIndicatorHeight(-8)//设置的高小于0 会显示在tab顶部 否则底部
                .setDividerColor(Color.TRANSPARENT);
    }

    private void setupStrip(JTabStyleDelegate tabStyleDelegate, int type) {
        tabStyleDelegate.setJTabStyle(type)
                .setShouldExpand(true)
                .setFrameColor(Color.parseColor("#45C01A"))
                .setTabTextSize(/*UIUtils.getDimen(13)*/30)
                .setTextColor(Color.parseColor("#45C01A"), Color.GRAY)
                //.setTextColor(R.drawable.tabstripbg)
                .setDividerColor(Color.parseColor("#45C01A"))
                .setDividerPadding(0)
                .setUnderlineColor(Color.parseColor("#3045C01A"))
                .setUnderlineHeight(0)
                .setIndicatorColor(Color.parseColor("#7045C01A"))
                .setIndicatorHeight(/*UIUtils.getDimen(28)*/50);


    }

    private void setupViewpager() {
        int[] mSelectors = new int[]{R.drawable.hy_tab1,
                R.drawable.hy_tab2,
                R.drawable.hy_tab3, R.drawable.hy_tab4};
        HomeAadapter adapter = new HomeAadapter(getSupportFragmentManager(), mSelectors);

        adapter.setSelectFragPosition(position -> {
            if (position==0){
                setTitleText("消息列表");
            }else {
               // getToolBarView().setVisibility(View.GONE);
            }
        });
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(mSelectors.length);
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
                getResources().getDisplayMetrics());
        mViewPager.setPageMargin(pageMargin);
        tabBtn.bindViewPager(mViewPager);
        showPromptMsg(tabBtn);

    }

    public void clearPromptMsg(ISlidingTabStrip slidingTabStrip) {
        for (int i = 0; i < mViewPager.getAdapter().getCount(); i++) {
            slidingTabStrip.setPromptNum(i, 0);
        }
    }


    public void showPromptMsg(ISlidingTabStrip slidingTabStrip) {
//        slidingTabStrip.setPromptNum(1, 9).
//                setPromptNum(0, 10).
//                setPromptNum(2, -9).
//                setPromptNum(3, 100);
    }



    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


    @Override
    public void onAuthStateChange(int authState) {

    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        //百度推送
       // PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,"8h0GOjOlgP8dXRzp9nG1dGBT");
        mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        // 注册网络改变回调
        // ConfigApplication.instance().registerNetWorkObserver(this);
        // 绑定监听
        ListenerManager.getInstance().addAuthStateChangeListener(this);
        // 注册消息更新广播
        IntentFilter msgIntentFilter = new IntentFilter();
        msgIntentFilter.addAction(MsgBroadcast.ACTION_MSG_NUM_UPDATE);
        msgIntentFilter.addAction(MsgBroadcast.ACTION_MSG_NUM_RESET);
        registerReceiver(mUpdateUnReadReceiver, msgIntentFilter);
        // 注册用户登录状态广播
        registerReceiver(mUserLogInOutReceiver, LoginHelper.getLogInOutActionFilter());
        // 绑定服务
        mXmppBind = bindService(CoreService.getIntent(), mXmppServiceConnection, BIND_AUTO_CREATE);

        // 检查用户的状态，做不同的初始化工作
        User loginUser = ConfigApplication.instance().mLoginUser;
        if (!LoginHelper.isUserValidation(loginUser)) {
            LoginHelper.prepareUser(this);
        }

        if (!ConfigApplication.instance().mUserStatusChecked) {// 用户状态没有检测，那么开始检测
            mUserCheckHander.sendEmptyMessageDelayed(MSG_USER_CHECK, mRetryCheckDelay);
        } else {
            if (ConfigApplication.instance().mUserStatus == LoginHelper.STATUS_USER_VALIDATION) {
                LoginHelper.broadcastLogin(this);
            } else {// 重新检测
                ConfigApplication.instance().mUserStatusChecked = false;
                mUserCheckHander.sendEmptyMessageDelayed(MSG_USER_CHECK, mRetryCheckDelay);
            }
        }
         //registerReceiver(mUpdateReceiver, CardcastUiUpdateUtil.getUpdateActionFilter());
        mBind = bindService(CoreService.getIntent(), mServiceConnection, BIND_AUTO_CREATE);
    }


    /* UserCheck */
    private static final int MSG_USER_CHECK = 0x1;
    private static final int RETRY_CHECK_DELAY_MAX = 30000;// 为成功的情况下，最长30s检测一次
    private int mRetryCheckDelay = 0;
    @SuppressLint("HandlerLeak")
    private Handler mUserCheckHander = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == MSG_USER_CHECK) {
                if (mRetryCheckDelay < RETRY_CHECK_DELAY_MAX) {
                    mRetryCheckDelay += 5000;
                }
                mUserCheckHander.removeMessages(RETRY_CHECK_DELAY_MAX);
                doUserCheck();
            }
        }
    };

    private void doUserCheck() {
        if (!com.lsjr.net.NetUtils.isConnected(this)) {
            return;
        }
        if (ConfigApplication.instance().mUserStatusChecked) {
            return;
        }
        LoginHelper.checkStatusForUpdate(this, () ->
                mUserCheckHander.sendEmptyMessageDelayed(MSG_USER_CHECK, mRetryCheckDelay));
    }

    private void cancelUserCheckIfExist() {
        mUserCheckHander.removeMessages(RETRY_CHECK_DELAY_MAX);
    }

    private void checkUserDb(final String userId) {
        // 检测用户基本数据库信息的完整性
        new Thread(() -> {
            FriendDao.getInstance().checkSystemFriend(userId);
            initMsgUnReadTips(userId);
        }).start();
    }

    private BroadcastReceiver mUserLogInOutReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(LoginHelper.ACTION_LOGIN)) {
                User user = ConfigApplication.instance().mLoginUser;
                Intent startIntent = CoreService.getIntent(HomeActivity.this, user.getUserId(), user.getPassword(), user.getNickName());
                startService(startIntent);
                // ToastUtil.showNormalToast(MainActivity.this, "开始Xmpp登陆");
                checkUserDb(user.getUserId());

            } else if (action.equals(LoginHelper.ACTION_LOGOUT)) {
                ConfigApplication.instance().mUserStatus = LoginHelper.STATUS_USER_SIMPLE_TELPHONE;
                mCoreService.logout();

                cancelUserCheckIfExist();
                //startActivity(new Intent(HomeActivity.this, LoginHistoryActivity.class));
                // mFindRb.setChecked(true);
               // removeNeedUserFragment(false);

            } else if (action.equals(LoginHelper.ACTION_CONFLICT)) {
                // 改变用户状态
                ConfigApplication.instance().mUserStatus = LoginHelper.STATUS_USER_TOKEN_CHANGE;
                mCoreService.logout();
                // mFindRb.setChecked(true);
                //removeNeedUserFragment(true);
                cancelUserCheckIfExist();
                T_.showToastReal("异地登录");
                // 弹出对话框
                //startActivity(new Intent(HomeActivity.this, UserCheckedActivity.class));
//
//                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.HONEYCOMB) {
//                    mActivityManager.moveTaskToFront(getTaskId(), ActivityManager.MOVE_TASK_WITH_HOME);
//                } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
//                    mActivityManager.moveTaskToFront(getTaskId(), ActivityManager.MOVE_TASK_NO_USER_ACTION);
//                }
            } else if (action.equals(LoginHelper.ACTION_NEED_UPDATE)) {
                // mFindRb.setChecked(true);
                //removeNeedUserFragment(true);
                cancelUserCheckIfExist();
                // 弹出对话框
              //  startActivity(new Intent(MainActivity.this, UserCheckedActivity.class));
            } else if (action.equals(LoginHelper.ACTION_LOGIN_GIVE_UP)) {
                cancelUserCheckIfExist();
                ConfigApplication.instance().mUserStatus = LoginHelper.STATUS_USER_NO_UPDATE;
                mCoreService.logout();
            }

        }

    };



    private int mImStatus = AuthStateListener.AUTH_STATE_NOT;
    private ServiceConnection mXmppServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mCoreService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mCoreService = ((CoreService.CoreServiceBinder) service).getService();
            mImStatus = mCoreService.isAuthenticated() ? AuthStateListener.AUTH_STATE_SUCCESS : AuthStateListener.AUTH_STATE_NOT;
        }
    };


    /*********************** 未读数量的更新功能 *****************/
    private Handler mUnReadHandler = new Handler();
    //private TextView mMsgUnReadTv;
    private int mMsgUnReadNum = 0;
    private boolean mMsgNumNeedUpdate = false;
    //
    private boolean mXmppBind;
    private CoreService mCoreService;
    private boolean isPause = true;// 界面是否暂停

    private void initMsgUnReadTips(String userId) {// 初始化未读条数
        // 消息未读条数累加
        mMsgUnReadNum = FriendDao.getInstance().getMsgUnReadNumTotal(userId);
        mUnReadHandler.post(() -> updateMsgUnReadTv());
    }

    private BroadcastReceiver mUpdateUnReadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MsgBroadcast.ACTION_MSG_NUM_UPDATE)) {
                int operation = intent.getIntExtra(MsgBroadcast.EXTRA_NUM_OPERATION, MsgBroadcast.NUM_ADD);
                int count = intent.getIntExtra(MsgBroadcast.EXTRA_NUM_COUNT, 0);
                mMsgUnReadNum = (operation == MsgBroadcast.NUM_ADD) ? mMsgUnReadNum + count : mMsgUnReadNum - count;
                updateMsgUnReadTv();
            } else if (action.equals(MsgBroadcast.ACTION_MSG_NUM_RESET)) {
                if (isPause) {// 等待恢复的时候更新
                    mMsgNumNeedUpdate = true;
                } else {// 立即更新
                    initMsgUnReadTips(ConfigApplication.instance().mLoginUser.getUserId());
                }
            }
        }
    };

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mXmppService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mXmppService = ((CoreService.CoreServiceBinder) service).getService();
        }
    };

    private void updateMsgUnReadTv() {
        L_.e("消息列表"+mMsgUnReadNum);
        if (mMsgUnReadNum > 0) {
           // mMsgUnReadTv.setVisibility(View.VISIBLE);
            String numStr = mMsgUnReadNum >= 99 ? "99" : mMsgUnReadNum + "";
            tabBtn.setPromptNum(0,Integer.valueOf(numStr));
        }
    }

    public void exitMucChat(String toUserId) {
        if (mCoreService != null) {
            mCoreService.exitMucChat(toUserId);
        }
    }

    public void sendNewFriendMessage(String toUserId, NewFriendMessage message) {
        if (mBind && mXmppService != null) {
            mXmppService.sendNewFriendMessage(toUserId, message);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        saveOfflineTime();

    }

    private void saveOfflineTime() {
        //将现在的时间存起来,
        long time=System.currentTimeMillis()/1000;
        Log.d("wang", "time_destory::" + time + "");
        SpUtils.putLong(this, Constants.OFFLINE_TIME, time);
        ConfigApplication.instance().mLoginUser.setOfflineTime(time);
        UserDao.getInstance().updateUnLineTime(ConfigApplication.instance().mLoginUser.getUserId(), time);
    }



    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        saveOfflineTime();
    }

    @Override
    protected void onDestroy() {
        saveOfflineTime();
        ListenerManager.getInstance().removeAuthStateChangeListener(this);
        if (mXmppBind) {
            unbindService(mXmppServiceConnection);
        }
        unregisterReceiver(mUpdateUnReadReceiver);
        unregisterReceiver(mUserLogInOutReceiver);
        super.onDestroy();
    }



}
