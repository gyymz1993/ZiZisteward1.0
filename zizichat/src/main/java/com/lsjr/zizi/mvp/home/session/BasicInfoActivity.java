package com.lsjr.zizi.mvp.home.session;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lsjr.bean.ObjectResult;
import com.lsjr.callback.ChatObjectCallBack;
import com.lsjr.zizi.AppConfig;
import com.lsjr.zizi.R;
import com.lsjr.zizi.base.MvpActivity;
import com.lsjr.zizi.http.HttpUtils;
import com.lsjr.zizi.mvp.chat.ConfigApplication;
import com.lsjr.zizi.mvp.chat.bean.AddAttentionResult;
import com.lsjr.zizi.mvp.chat.bean.AttentionUser;
import com.lsjr.zizi.mvp.chat.bean.ResultCode;
import com.lsjr.zizi.mvp.chat.broad.CardcastUiUpdateUtil;
import com.lsjr.zizi.mvp.chat.broad.MsgBroadcast;
import com.lsjr.zizi.mvp.chat.dao.FriendDao;
import com.lsjr.zizi.mvp.chat.dao.NewFriendDao;
import com.lsjr.zizi.mvp.chat.db.Friend;
import com.lsjr.zizi.mvp.chat.db.NewFriendMessage;
import com.lsjr.zizi.mvp.chat.db.User;
import com.lsjr.zizi.mvp.chat.helper.AvatarHelper;
import com.lsjr.zizi.mvp.chat.helper.FriendHelper;
import com.lsjr.zizi.mvp.chat.xmpp.CoreService;
import com.lsjr.zizi.mvp.chat.xmpp.ListenerManager;
import com.lsjr.zizi.mvp.chat.xmpp.XmppMessage;
import com.lsjr.zizi.mvp.chat.xmpp.listener.NewFriendListener;
import com.lsjr.zizi.util.TimeUtils;
import com.ymz.baselibrary.mvp.BasePresenter;
import com.ymz.baselibrary.utils.L_;
import com.ymz.baselibrary.utils.T_;
import com.ymz.baselibrary.utils.UIUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/8/14 19:21
 */

public class BasicInfoActivity extends MvpActivity implements NewFriendListener {

    @BindView(R.id.ivHeader)
    ImageView mIvHeader;
    @BindView(R.id.tvName)
    TextView mTvName;
    @BindView(R.id.ivGender)
    ImageView mIvGender;
    @BindView(R.id.tvAccount)
    TextView mTvAccount;
    @BindView(R.id.tvNickName)
    TextView mTvNickName;


    /*发送  消息     TODO */
    @BindView(R.id.btnCheat)
    Button mNextStepBtn;
    @BindView(R.id.btnAddToContact)
    Button mBtnAddToContact;


    private String mLoginUserId;
    private boolean isMyInfo = false;// 快捷判断
    private String mUserId;
    private User mUser;
    private Friend mFriend;// 如果这个用户是当前登陆者的好友或者关注着，那么该值有意义
    private boolean mBind;
    private CoreService mXmppService;


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_userinfo;
    }

    @Override
    protected void initView() {
        if (getIntent() != null) {
            mUserId = getIntent().getStringExtra(AppConfig.EXTRA_USER_ID);
        }
        mLoginUserId = ConfigApplication.instance().mLoginUser.getUserId();
        if (TextUtils.isEmpty(mUserId)) {
            mUserId = mLoginUserId;
        }
        if (mLoginUserId.equals(mUserId) || TextUtils.isEmpty(mUserId)) {// 显示我的资料
            mUserId = mLoginUserId;// 让mUserId变为和登陆者一样，当做是查看登陆者自己的个人资料
            isMyInfo = true;
            loadMyInfoFromDb();
        } else {// 显示其他用户的资料
            isMyInfo = false;
            loadOthersInfoFromNet();
        }


        ListenerManager.getInstance().addNewFriendListener(this);
        mBind = bindService(CoreService.getIntent(), mServiceConnection, BIND_AUTO_CREATE);

    }

    private void loadOthersInfoFromNet() {
        showProgressDialogWithText("获取资料");
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", ConfigApplication.instance().mAccessToken);
        params.put("userId", mUserId);
        HttpUtils.getInstance().postServiceData(AppConfig.USER_GET_URL, params, new ChatObjectCallBack<User>(User.class) {

            @Override
            protected void onXError(String exception) {
                T_.showToastWhendebug(exception);
                dismissProgressDialog();
            }

            @Override
            protected void onSuccess(ObjectResult<User> result) {
                boolean success = ResultCode.defaultParser(result, true);
                if (success && result.getData() != null) {
                    mUser = result.getData();
                    // 如果本地的好友状态不正确，那么就更新本地好友状态
                    AttentionUser attentionUser = mUser.getFriends();// 服务器的状态
                    L_.e("onSuccess"+mUser.getFriends().getStatus());
                    boolean changed = FriendHelper.updateFriendRelationship(mLoginUserId, mUser.getUserId(),
                            attentionUser);
                    if (changed) {
                        updateAllCardcastUi();
                    }
                    dismissProgressDialog();
                    updateUI();
                } else {
                    dismissProgressDialog();
                }
            }
        });
    }


    private void updateUI() {
        if (mUser == null) {
            return;
        }
        // 设置头像
        AvatarHelper.getInstance().displayAvatar(mUser.getUserId(), mIvHeader, false);
        // 判断是否有备注名,有就显示
        if(mFriend!=null){
            if(mFriend.getRemarkName()!=null){
                mTvName.setText(mFriend.getRemarkName());
            }
        }else{
            mTvName.setText(mUser.getNickName());

        }
        mTvAccount.setText(mUser.getSex() == 0 ? "男" : "女");
        mTvNickName.setText(TimeUtils.sk_time_s_long_2_str(mUser.getBirthday()));
        //mCityTv.setText(Area.getProvinceCityString(mUser.getProvinceId(), mUser.getCityId()));

        // 设置头像
        //AvatarHelper.getInstance().displayAvatar(mUser.getUserId(), mAvatarImg, false);
        // 判断是否有备注名,有就显示
        // ActionBtn 的初始化
        if (isMyInfo) {// 如果是我自己，不显示ActionBtn
            mNextStepBtn.setVisibility(View.GONE);
           // mLookLocationBtn.setVisibility(View.GONE);
        } else {
            initFriendMoreAction();
            mNextStepBtn.setVisibility(View.VISIBLE);
            L_.e("onSuccess  updateUI"+mFriend.getStatus());
            if (mFriend == null) {
                mNextStepBtn.setText(R.string.add_attention);
                mNextStepBtn.setOnClickListener(new AddAttentionListener());
            } else {
                L_.e("mFriend.getStatus()"+mFriend.getStatus());
                switch (mFriend.getStatus()) {
                    case Friend.STATUS_BLACKLIST:// 在黑名单中，显示移除黑名单
                        mNextStepBtn.setText(R.string.remove_blacklist);
                        mNextStepBtn.setOnClickListener(new RemoveBlacklistListener());
                        break;
                    case Friend.STATUS_ATTENTION:// 已经是关注了，显示打招呼
                        mNextStepBtn.setText(R.string.say_hello);
                        mNextStepBtn.setOnClickListener(new SayHelloListener());
                        break;
                    case Friend.STATUS_FRIEND:// 已经是朋友了，显示发消息
                        mNextStepBtn.setText(R.string.send_msg);
                        mNextStepBtn.setOnClickListener(new SendMsgListener());
                        break;
                    default:// 其他（理论上不可能的哈，容错）
                        mNextStepBtn.setText(R.string.add_attention);
                        mNextStepBtn.setOnClickListener(new AddAttentionListener());
                        break;
                }
            }
        }
        //invalidateOptionsMenu();
    }

    /**
     * 懒得判断操作的用户到底属于好友、企业、还是公司，直接发广播，让所有的名片盒页面都更新
     */
    private void updateAllCardcastUi() {
        CardcastUiUpdateUtil.broadcastUpdateUi(this);
    }

    private void loadMyInfoFromDb() {
        mUser = ConfigApplication.instance().mLoginUser;
        updateUI();
    }


    private void initFriendMoreAction() {
        mFriend = FriendDao.getInstance().getFriend(mLoginUserId, mUser.getUserId());// 更新好友的状态
    }

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

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        setTitleText("基本资料");
        setTopLeftButton(R.drawable.ic_back).
                setTitleTextColor(UIUtils.getColor(R.color.white)).
                setBackgroundColor(UIUtils.getColor(R.color.colorPrimary));
    }

    @Override
    public void onNewFriendSendStateChange(String toUserId, NewFriendMessage message, int messageState) {

    }


    protected void onDestroy() {
        super.onDestroy();
        ListenerManager.getInstance().removeNewFriendListener(this);
        if (mBind) {
            unbindService(mServiceConnection);
        }
    }



    // 发消息
    private class SendMsgListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            MsgBroadcast.broadcastMsgUiUpdate(BasicInfoActivity.this);
            MsgBroadcast.broadcastMsgNumReset(BasicInfoActivity.this);
            Intent intent = new Intent(BasicInfoActivity.this, ChatActivity.class);
            intent.putExtra(ChatActivity.FRIEND, mFriend);
            startActivity(intent);
        }
    }

    // 移除黑名单
    private class RemoveBlacklistListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (mFriend == null || mFriend.getStatus() != Friend.STATUS_BLACKLIST) {
                return;
            }
            removeBlacklist(mFriend);
            mFriend = FriendDao.getInstance().getFriend(mLoginUserId, mUser.getUserId());// 更新好友的状态
        }
    }


    // 加关注
    private class AddAttentionListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            doAddAttention();
        }
    }

    // 打招呼
    private class SayHelloListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            doSayHello();
        }
    }

    private void doAddAttention() {
        if (mUser == null) {
            return;
        }
        showProgressDialogWithText("请稍等");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("access_token", ConfigApplication.instance().mAccessToken);
        params.put("toUserId", mUser.getUserId());
        HttpUtils.getInstance().postServiceData(AppConfig.FRIENDS_ATTENTION_ADD, params, new ChatObjectCallBack<AddAttentionResult>(AddAttentionResult.class) {
            @Override
            protected void onXError(String exception) {
                dismissProgressDialog();
                T_.showToastReal(exception);
            }

            @Override
            protected void onSuccess(ObjectResult<AddAttentionResult> result) {
                boolean success = ResultCode.defaultParser(result, true);
                if (success && result.getData() != null) {// 接口加关注成功
                    if (result.getData().getType() == 1 || result.getData().getType() == 3) {// 单方关注成功或已经是关注的
                        // 发送推送消息
                        NewFriendMessage message = NewFriendMessage.createWillSendMessage(
                                ConfigApplication.instance().mLoginUser, XmppMessage.TYPE_NEWSEE, null, mUser);
                        mXmppService.sendNewFriendMessage(mUser.getUserId(), message);
                        // 添加为关注
                        NewFriendDao.getInstance().ascensionNewFriend(message, Friend.STATUS_ATTENTION);
                        FriendHelper.addAttentionExtraOperation(mLoginUserId, mUser.getUserId());

                        // 提示加关注成功
                        T_.showToastReal("加关注成功");
                        // 更新界面
                        mNextStepBtn.setText(R.string.say_hello);
                        mNextStepBtn.setOnClickListener(new SayHelloListener());
                        // 由陌生关系变为关注了,那么右上角更多操作可以显示了
                        initFriendMoreAction();
                        // 更新名片盒
                        updateAllCardcastUi();
                        invalidateOptionsMenu();
                    } else if (result.getData().getType() == 2 || result.getData().getType() == 4) {// 已经是好友了
                        // 发送推送的消息
                        NewFriendMessage message = NewFriendMessage.createWillSendMessage(
                                ConfigApplication.instance().mLoginUser, XmppMessage.TYPE_FRIEND, null, mUser);
                        mXmppService.sendNewFriendMessage(mUser.getUserId(), message);

                        // 添加为好友
                        NewFriendDao.getInstance().ascensionNewFriend(message, Friend.STATUS_FRIEND);
                        FriendHelper.addFriendExtraOperation(mLoginUserId, mUser.getUserId());

                        // 提示加好友成功
                        T_.showToastReal("添加好友成功");
                        // 更新界面
                        mNextStepBtn.setText(R.string.send_msg);
                        mNextStepBtn.setOnClickListener(new SendMsgListener());
                        // 由陌生或者关注变为好友了,那么右上角更多操作可以显示了
                        initFriendMoreAction();
                        // 更新名片盒
                        updateAllCardcastUi();
                        invalidateOptionsMenu();
                    } else if (result.getData().getType() == 5) {
                        T_.showToastReal("加关注失败，你已被对方加入黑名单");
                    }
                }
                dismissProgressDialog();

            }
        });
    }


    public void doSayHello() {
        final EditText editText = new EditText(this);
        editText.setMaxLines(2);
        editText.setLines(2);
        editText.setHint("亲，给我个加你的理由吧！");
        editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
        editText.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("验证消息")
                .setView(editText).setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String text = editText.getText().toString().trim();
                        doSayHello(text);
                    }
                }).setNegativeButton(getString(R.string.cancel), null);
        builder.create().show();
    }

    private void doSayHello(String text) {
        if (TextUtils.isEmpty(text)) {
            text = "嗨，你好";
        }
        NewFriendMessage message = NewFriendMessage.createWillSendMessage(ConfigApplication.instance().mLoginUser,
                XmppMessage.TYPE_SAYHELLO, text, mUser);
        NewFriendDao.getInstance().createOrUpdateNewFriend(message);
        mXmppService.sendNewFriendMessage(mUser.getUserId(), message);
        // 提示打招呼成功
        T_.showToastReal("嗨，你好");
    }


    private void removeBlacklist(final Friend friend) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("access_token", ConfigApplication.instance().mAccessToken);
        params.put("toUserId", friend.getUserId());

        showProgressDialogWithText("请稍等");
        HttpUtils.getInstance().postServiceData(AppConfig.FRIENDS_BLACKLIST_DELETE, params, new ChatObjectCallBack<AttentionUser>(AttentionUser.class) {
            @Override
            protected void onXError(String exception) {
                dismissProgressDialog();
                T_.showToastReal(exception);
            }

            @Override
            protected void onSuccess(ObjectResult<AttentionUser> result) {
                boolean success = ResultCode.defaultParser(result, true);
                if (success) {
                    int currentStatus = Friend.STATUS_UNKNOW;
                    if (result.getData() != null) {
                        currentStatus = result.getData().getStatus();
                    }
                    FriendDao.getInstance().updateFriendStatus(friend.getOwnerId(), friend.getUserId(),
                            currentStatus);
                    friend.setStatus(currentStatus);
                    updateAllCardcastUi();

                    switch (currentStatus) {
                        case Friend.STATUS_ATTENTION:
                            mNextStepBtn.setText("打招呼");
                            mNextStepBtn.setOnClickListener(new SayHelloListener());
                            NewFriendMessage message1 = NewFriendMessage.createWillSendMessage(
                                    ConfigApplication.instance().mLoginUser, XmppMessage.TYPE_NEWSEE, null, friend);
                            mXmppService.sendNewFriendMessage(friend.getUserId(), message1);

                            FriendHelper.addAttentionExtraOperation(friend.getOwnerId(), friend.getUserId());
                            break;
                        case Friend.STATUS_FRIEND:
                            mNextStepBtn.setText("发消息");
                            mNextStepBtn.setOnClickListener(new SendMsgListener());

                            NewFriendMessage message2 = NewFriendMessage.createWillSendMessage(
                                    ConfigApplication.instance().mLoginUser, XmppMessage.TYPE_FRIEND, null, mUser);
                            mXmppService.sendNewFriendMessage(mUser.getUserId(), message2);
                            FriendHelper.addFriendExtraOperation(friend.getOwnerId(), friend.getUserId());
                            break;
                        default:// 其他，理论上不可能
                            mNextStepBtn.setText("加关注");
                            mNextStepBtn.setOnClickListener(new AddAttentionListener());
                            break;
                    }

                    T_.showToastReal("移出黑名单成功");
                }
                dismissProgressDialog();
            }
        });

    }

    @Override
    public boolean onNewFriend(NewFriendMessage message) {
        return false;
    }
}