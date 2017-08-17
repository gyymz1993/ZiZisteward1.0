package com.lsjr.zizi.mvp.home.session;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.autofresh.BGANormalRefreshViewHolder;
import com.andview.autofresh.BGARefreshLayout;
import com.bumptech.glide.Glide;
import com.lqr.audio.AudioRecordManager;
import com.lqr.audio.IAudioRecordListener;
import com.lqr.emoji.EmotionKeyboard;
import com.lqr.emoji.EmotionLayout;
import com.lqr.emoji.IEmotionExtClickListener;
import com.lqr.emoji.IEmotionSelectedListener;
import com.lqr.imagepicker.ImagePicker;
import com.lqr.imagepicker.bean.ImageItem;
import com.lqr.imagepicker.ui.ImagePreviewActivity;
import com.lqr.recyclerview.LQRRecyclerView;
import com.lsjr.bean.ArrayResult;
import com.lsjr.callback.ChatArrayCallBack;
import com.lsjr.zizi.AppConfig;
import com.lsjr.zizi.AppConst;
import com.lsjr.zizi.R;
import com.lsjr.zizi.base.MvpActivity;
import com.lsjr.zizi.bean.LocationData;
import com.lsjr.zizi.http.HttpUtils;
import com.lsjr.zizi.chat.ConfigApplication;
import com.lsjr.zizi.chat.Constants;
import com.lsjr.zizi.chat.bean.PublicMessage;
import com.lsjr.zizi.chat.bean.ResultCode;
import com.lsjr.zizi.chat.broad.MsgBroadcast;
import com.lsjr.zizi.chat.dao.FriendDao;
import com.lsjr.zizi.chat.db.ChatMessage;
import com.lsjr.zizi.chat.db.Friend;
import com.lsjr.zizi.chat.utils.FileUtils;
import com.lsjr.zizi.chat.xmpp.CoreService;
import com.lsjr.zizi.chat.xmpp.ListenerManager;
import com.lsjr.zizi.chat.xmpp.listener.ChatMessageListener;
import com.lsjr.zizi.mvp.home.photo.ImageGridActivity;
import com.lsjr.zizi.mvp.home.photo.MyLocationActivity;
import com.lsjr.zizi.mvp.home.photo.TakePhotoActivity;
import com.lsjr.zizi.mvp.home.photo.view.ISessionAtView;
import com.lsjr.zizi.util.ImageUtils;
import com.nostra13.universalimageloader.utils.L;
import com.ymz.baselibrary.AppCache;
import com.ymz.baselibrary.utils.L_;
import com.ymz.baselibrary.utils.T_;
import com.ymz.baselibrary.utils.UIUtils;
import com.ymz.baselibrary.view.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;
import static com.lsjr.zizi.mvp.home.photo.ImageGridActivity.REQUEST_PERMISSION_STORAGE;

/**
 * 会话界面（单聊、群聊）
 */
public class ChatActivity extends MvpActivity<ChatAtPresenter> implements ISessionAtView, IEmotionSelectedListener, BGARefreshLayout.BGARefreshLayoutDelegate
        , ChatMessageListener {
    private static final int REQUEST_CODE_SELECT_FILE = 4;
    public static final int REQUEST_IMAGE_PICKER = 1000;
    public final static int REQUEST_TAKE_PHOTO = 1001;
    public final static int REQUEST_MY_LOCATION = 1002;
    public final static int SESSION_TYPE_PRIVATE = 1;
    public final static int SESSION_TYPE_GROUP = 2;
    private boolean mIsFirst = false;
    @BindView(R.id.llRoot)
    LinearLayout mLlRoot;
    @BindView(R.id.llContent)
    LinearLayout mLlContent;
    @BindView(R.id.refreshLayout)
    BGARefreshLayout mRefreshLayout;
    @BindView(R.id.rvMsg)
    LQRRecyclerView mRvMsg;
    @BindView(R.id.ivAudio)
    ImageView mIvAudio;
    @BindView(R.id.btnAudio)
    Button mBtnAudio;
    @BindView(R.id.etContent)
    EditText mEtContent;
    @BindView(R.id.ivEmo)
    ImageView mIvEmo;
    @BindView(R.id.ivMore)
    ImageView mIvMore;
    @BindView(R.id.btnSend)
    Button mBtnSend;
    @BindView(R.id.flEmotionView)
    FrameLayout mFlEmotionView;
    @BindView(R.id.elEmotion)
    EmotionLayout mElEmotion;
    @BindView(R.id.llMore)
    LinearLayout mLlMore;
    @BindView(R.id.rlAlbum)
    RelativeLayout mRlAlbum;
    @BindView(R.id.rlTakePhoto)
    RelativeLayout mRlTakePhoto;
    @BindView(R.id.rlLocation)
    RelativeLayout mRlLocation;
    @BindView(R.id.rlRedPacket)
    RelativeLayout mRlRedPacket;
    @BindView(R.id.im_card_tvrlRly)
    RelativeLayout mRlCard;
    @BindView(R.id.im_file_tvrlRly)
    RelativeLayout mRlFile;
    private String mLoginUserId;
    private EmotionKeyboard mEmotionKeyboard;
    private Friend mFriend;// 存储所有的当前聊天对象
    public static final String FRIEND = "friend";
    private List<ChatMessage> chatMessageList=new ArrayList<>();
    private MessageUtils messageUtils;

    private PopupWindow mRecordWindow;
    private View windowView;
    public void initAudioRecord() {
        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO
                ,  Manifest.permission.MODIFY_AUDIO_SETTINGS
        }, new PermissionListener() {
            @Override
            public void onGranted() {
                initAudioRecordManager();
            }

            @Override
            public void onDenied(List<String> deniedPermissions) {
            }
        });
        initAudioRecordManager();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_session;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {

        initAudioRecord();
        initEmotionKeyboard();
        initRefreshLayout();
        initListener();
        initDataChat(savedInstanceState);
        downOnlineMsg();

    }


    /**
     * 离线消息
     */
    private void downOnlineMsg() {
        L_.e(mFriend.toString());
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("access_token", ConfigApplication.instance().mAccessToken);
        params.put("receiver", mFriend.getOwnerId());
        params.put("sender", mLoginUserId);
        //direction=1（receiver=发送方，sender=接收方）
        params.put("startTime",(System.currentTimeMillis()-60 * 60 * 24 )+"");
        params.put("endTime", System.currentTimeMillis()+"");
        params.put("pageIndex", "1");
        params.put("pageSize", "50");
        HttpUtils.getInstance().postServiceData(AppConfig.ONLINE_MSG, params, new ChatArrayCallBack<PublicMessage>(PublicMessage.class) {

            @Override
            protected void onXError(String exception) {
                T_.showToastReal(exception);
            }

            @Override
            protected void onSuccess(ArrayResult<PublicMessage> result) {
                L_.e(result.toString());
                boolean success = ResultCode.defaultParser( result, true);
                //mPullToRefreshListView.onRefreshComplete();
            }
        });

    }

    protected void initDataChat(Bundle savedInstanceState) {
        mLoginUserId = ConfigApplication.instance().mLoginUser.getUserId();
        if (savedInstanceState != null) {
            mFriend = (Friend) savedInstanceState.getSerializable(Constants.EXTRA_FRIEND);
        } else if (getIntent() != null) {
            mFriend = (Friend) getIntent().getSerializableExtra(Constants.EXTRA_FRIEND);
        }
        assert mFriend != null;
        setTitleText(mFriend.getNickName());
        setTopLeftButton(R.drawable.ic_back).
                setTitleText(mFriend.getNickName()).
                setTitleTextColor(UIUtils.getColor(R.color.white)).
                setBackgroundColor(UIUtils.getColor(R.color.colorPrimary));

        mvpPresenter.setAdapter();
        mvpPresenter.loadDatas(true);
        ListenerManager.getInstance().addChatMessageListener(this);
        bindService(CoreService.getIntent(), mConnection, BIND_AUTO_CREATE);
        IntentFilter filter = new IntentFilter(Constants.CHAT_MESSAGE_DELETE_ACTION);
        registerReceiver(broadcastReceiver, filter);
    }


    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            messageUtils.setmService(((CoreService.CoreServiceBinder) service).getService());
        }
    };


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("wang", "接收到广播");
//            if (mChatContentView != null) {
//                int position = intent.getIntExtra(Constants.CHAT_REMOVE_MESSAGE_POSITION, 10000);
//                if (position == 10000) {
//                    return;
//                }
//                mChatMessages.remove(position);
//                mChatContentView.notifyDataSetInvalidated(true);
//            }
        }
    };



    @SuppressLint("ClickableViewAccessibility")
    public void initListener() {
        /*发送表情事件*/
        mElEmotion.setEmotionSelectedListener(this);
        mElEmotion.setEmotionAddVisiable(true);
        mElEmotion.setEmotionSettingVisiable(true);
        mElEmotion.setEmotionExtClickListener(new IEmotionExtClickListener() {
            @Override
            public void onEmotionAddClick(View view) {
                UIUtils.showToast("add");
            }

            @Override
            public void onEmotionSettingClick(View view) {
                UIUtils.showToast("setting");
            }
        });
        mLlContent.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case ACTION_DOWN:
                    closeBottomAndKeyboard();
                    break;
            }
            return false;
        });

        mRvMsg.setOnTouchListener((v, event) -> {
            closeBottomAndKeyboard();
            return false;
        });

        mRvMsg.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    Glide.with(UIUtils.getContext()).resumeRequests();
                }else{
                    Glide.with(UIUtils.getContext()).pauseRequests();
                }

            }
        });

        mIvAudio.setOnClickListener(v -> {
            if (mBtnAudio.isShown()) {
                hideAudioButton();
                mEtContent.requestFocus();
                if (mEmotionKeyboard != null) {
                    mEmotionKeyboard.showSoftInput();
                }
            } else {
                mEtContent.clearFocus();
                showAudioButton();
                hideEmotionLayout();
                hideMoreLayout();
            }
            UIUtils.postTaskDelay(() -> mRvMsg.smoothMoveToPosition(mRvMsg.getAdapter().getItemCount() - 1), 50);
        });
        mEtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mEtContent.getText().toString().trim().length() > 0) {
                    mBtnSend.setVisibility(View.VISIBLE);
                    mIvMore.setVisibility(View.GONE);
                    //RongIMClient.getInstance().sendTypingStatus(mConversationType, mSessionId, TextMessage.class.getAnnotation(MessageTag.class).value());
                } else {
                    mBtnSend.setVisibility(View.GONE);
                    mIvMore.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mEtContent.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                UIUtils.postTaskDelay(() ->
                        mRvMsg.smoothMoveToPosition(mRvMsg.getAdapter().getItemCount() - 1), 50);
            }


        });
        mBtnSend.setOnClickListener(v -> mvpPresenter.sendTextMsg());
        mBtnAudio.setOnTouchListener((v, event) -> {
            L_.e(""+event.getAction());
            switch (event.getAction()) {
                case ACTION_DOWN:
                    AudioRecordManager.getInstance(ChatActivity.this).startRecord();
                    break;
                case ACTION_MOVE:
                    if (isCancelled(v, event)) {
                        AudioRecordManager.getInstance(ChatActivity.this).willCancelRecord();
                    } else {
                        AudioRecordManager.getInstance(ChatActivity.this).continueRecord();
                    }
                    break;
                case ACTION_UP:
                case ACTION_CANCEL:
                    AudioRecordManager.getInstance(ChatActivity.this).stopRecord();
                    AudioRecordManager.getInstance(ChatActivity.this).destroyRecord();
                    break;
            }

            return false;
        });

        mRlAlbum.setOnClickListener(v -> {
            Intent intent = new Intent(this, ImageGridActivity.class);
            startActivityForResult(intent, REQUEST_IMAGE_PICKER);
        });
        mRlTakePhoto.setOnClickListener(v -> {
            Intent intent = new Intent(ChatActivity.this, TakePhotoActivity.class);
            startActivityForResult(intent, REQUEST_TAKE_PHOTO);
        });
        mRlLocation.setOnClickListener(v -> {
            Intent intent = new Intent(ChatActivity.this, MyLocationActivity.class);
            startActivityForResult(intent, REQUEST_MY_LOCATION);
        });
        mRlCard.setOnClickListener(v -> {
            mvpPresenter.sendCard(mLoginUserId);
        });
        mRlFile.setOnClickListener(v -> {
            // Intent intent = new Intent(mContext, MemoryFileManagement.class);
            // startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
            Intent target = FileUtils.createGetContentIntent();
            // Create the chooser Intent
            Intent intent = Intent.createChooser(target, "选择发送文件");
            try {
                startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
            } catch (ActivityNotFoundException e) {
                // The reason for the existence of aFileChooser
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_STORAGE) {
            initAudioRecordManager();
        } else {
            UIUtils.showToast("权限被禁止");
        }

    }

    private void initAudioRecordManager() {
        AudioRecordManager.getInstance(this).setMaxVoiceDuration(AppConst.DEFAULT_MAX_AUDIO_RECORD_TIME_SECOND);
        if (AppCache.getInstance().getmVoicesDir() == null) {
            UIUtils.showToast("创建文件夹失败");
            return;
        }
        File audioDir = AppCache.getInstance().getmVoicesDir();
        AudioRecordManager.getInstance(this).setAudioSavePath(audioDir.getAbsolutePath());
        AudioRecordManager.getInstance(this).setAudioRecordListener(new IAudioRecordListener() {

            private TextView mTimerTV;
            private TextView mStateTV;
            private ImageView mStateIV;

            @Override
            public void initTipView() {
                windowView = View.inflate(ChatActivity.this, R.layout.popup_audio_wi_vo, null);
                mStateIV = (ImageView) windowView.findViewById(R.id.rc_audio_state_image);
                mStateTV = (TextView) windowView.findViewById(R.id.rc_audio_state_text);
                mTimerTV = (TextView) windowView.findViewById(R.id.rc_audio_timer);
                mRecordWindow = new PopupWindow(windowView, -1, -1);
                mRecordWindow.showAtLocation(mLlRoot, 17, 0, 0);
                mRecordWindow.setFocusable(true);
                mRecordWindow.setOutsideTouchable(false);
                mRecordWindow.setTouchable(false);
            }

            @Override
            public void setTimeoutTipView(int counter) {
                if (mRecordWindow != null) {
                    this.mStateIV.setVisibility(View.GONE);
                    this.mStateTV.setVisibility(View.VISIBLE);
                    this.mStateTV.setText(R.string.voice_rec);
                    this.mStateTV.setBackgroundResource(R.drawable.bg_voice_popup);
                    this.mTimerTV.setText(String.format("%s", new Object[]{Integer.valueOf(counter)}));
                    this.mTimerTV.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void setRecordingTipView() {
                if (mRecordWindow != null) {
                    this.mStateIV.setVisibility(View.VISIBLE);
                    this.mStateIV.setImageResource(R.mipmap.ic_volume_1);
                    this.mStateTV.setVisibility(View.VISIBLE);
                    this.mStateTV.setText(R.string.voice_rec);
                    this.mStateTV.setBackgroundResource(R.drawable.bg_voice_popup);
                    this.mTimerTV.setVisibility(View.GONE);
                }
            }

            @Override
            public void setAudioShortTipView() {
                if (mRecordWindow != null) {
                    mStateIV.setImageResource(R.mipmap.ic_volume_wraning);
                    mStateTV.setText(R.string.voice_short);
                }
            }

            @Override
            public void setCancelTipView() {
                if (mRecordWindow != null) {
                    this.mTimerTV.setVisibility(View.GONE);
                    this.mStateIV.setVisibility(View.VISIBLE);
                    this.mStateIV.setImageResource(R.mipmap.ic_volume_cancel);
                    this.mStateTV.setVisibility(View.VISIBLE);
                    this.mStateTV.setText(R.string.voice_cancel);
                    this.mStateTV.setBackgroundResource(R.drawable.corner_voice_style);
                }
            }

            @Override
            public void destroyTipView() {
                if (mRecordWindow != null) {
                    mRecordWindow.dismiss();
                    mRecordWindow = null;
                    this.mStateIV = null;
                    this.mStateTV = null;
                    this.mTimerTV = null;
                }
            }

            @Override
            public void onStartRecord() {
                // RongIMClient.getInstance().sendTypingStatus(mConversationType, mSessionId, VoiceMessage.class.getAnnotation(MessageTag.class).value());
            }

            @Override
            public void onFinish(Uri audioPath, int duration) {
                if(mvpPresenter==null)return;
                mvpPresenter.sendVoice(audioPath, duration);
            }

            @Override
            public void onAudioDBChanged(int db) {
                if (mStateIV==null)return;
                switch (db / 5) {
                    case 0:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_1);
                        break;
                    case 1:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_2);
                        break;
                    case 2:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_3);
                        break;
                    case 3:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_4);
                        break;
                    case 4:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_5);
                        break;
                    case 5:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_6);
                        break;
                    case 6:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_7);
                        break;
                    default:
                        this.mStateIV.setImageResource(R.mipmap.ic_volume_8);
                }
            }
        });
    }


    private boolean isCancelled(View view, MotionEvent event) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return event.getRawX() < location[0] || event.getRawX() > location[0] + view.getWidth()
                || event.getRawY() < location[1] - 40;
    }


    @Override
    public void finish() {
        super.finish();
        MsgBroadcast.broadcastMsgUiUpdate(this);
    }

//    private void doBack() {
//        if (mHasSend) {
//
//        }
//        finish();
//    }



    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mIsFirst) {
            mEtContent.clearFocus();
        } else {
            mIsFirst = false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE_PICKER:
                if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {//返回多张照片
                    if (data != null) {
                        //是否发送原图
                        boolean isOrig = data.getBooleanExtra(ImagePreviewActivity.ISORIGIN, false);
                        ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                        Log.e("CSDN_LQR", isOrig ? "发原图" : "不发原图");//若不发原图的话，需要在自己在项目中做好压缩图片算法
                        for (ImageItem imageItem : images) {
                            File imageFileThumb;
                            File imageFileSource;
                            if (isOrig) {
                                imageFileSource = new File(imageItem.path);
                                imageFileThumb = ImageUtils.genThumbImgFile(imageItem.path);
                            } else {
                                //压缩图片
                                imageFileSource = ImageUtils.genThumbImgFile(imageItem.path);
                                imageFileThumb = ImageUtils.genThumbImgFile(imageFileSource.getAbsolutePath());
                            }
                            mvpPresenter.sendImgMsg(imageFileThumb, imageFileSource);
                        }
                    }
                }
            case REQUEST_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    String path = data.getStringExtra("path");
                    if (data.getBooleanExtra("take_photo", true)) {
                        //照片
                        //mvpPresenter.sendImgMsg(new File(path), new File(path));
                        mvpPresenter.sendImgMsg(ImageUtils.genThumbImgFile(path), new File(path));
                    } else {
                        //小视频
                        // mvpPresenter.sendFileMsg(new File(path));
                        mvpPresenter.sendAudioFile(new File(path));
                    }
                }
                break;
            case REQUEST_MY_LOCATION:
                if (resultCode == RESULT_OK) {
                    LocationData locationData = (LocationData) data.getSerializableExtra("location");
                    mvpPresenter.sendLocationMessage(locationData);
                }
                break;
            case REQUEST_CODE_SELECT_FILE:
                if (resultCode == RESULT_OK) {
                    String filePath = null;
                    if (data != null) {
                        // Get the URI of the selected file
                        final Uri uri = data.getData();
                        try {
                            // Get the file path from the URI
                            filePath = FileUtils.getPath(this, uri);
                        } catch (Exception e) {
                            Log.e("roamer", "File select error", e);
                        }
                    }
                    // String filePath = data.getStringExtra(AppConstant.FILE_PAT_NAME);
                    if (TextUtils.isEmpty(filePath)) {
                        return;
                    }
                    File file = new File(filePath);
                    L_.e("roamer" + file.getAbsolutePath());
                    if (!file.exists()) {
                        return;
                    }
                    mvpPresenter.sendFileMsg(file);
                }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRecordWindow=null;
        windowView=null;
        ListenerManager.getInstance().removeChatMessageListener(this);
        AudioRecordManager.getInstance(this).stopRecord();
        AudioRecordManager.getInstance(this).destroyRecord();
        unbindService(mConnection);
        unregisterReceiver(broadcastReceiver);

    }


    private void initRefreshLayout() {
        mRefreshLayout.setDelegate(this);
        BGANormalRefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(this, false);
        refreshViewHolder.setRefreshingText("");
        refreshViewHolder.setPullDownRefreshText("");
        refreshViewHolder.setReleaseRefreshText("");
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
    }

    private void initEmotionKeyboard() {
        mElEmotion.attachEditText(mEtContent);
        mEmotionKeyboard = EmotionKeyboard.with(this);
        mEmotionKeyboard.bindToEditText(mEtContent);
        mEmotionKeyboard.bindToContent(mLlContent);
        mEmotionKeyboard.setEmotionLayout(mFlEmotionView);
        mEmotionKeyboard.bindToEmotionButton(mIvEmo, mIvMore);
        mEmotionKeyboard.setOnEmotionButtonOnClickListener(view -> {
            switch (view.getId()) {
                case R.id.ivEmo:
                    UIUtils.postTaskDelay(() -> mRvMsg.smoothMoveToPosition(mRvMsg.getAdapter().getItemCount() - 1), 50);
                    mEtContent.clearFocus();
                    if (!mElEmotion.isShown()) {
                        if (mLlMore.isShown()) {
                            showEmotionLayout();
                            hideMoreLayout();
                            hideAudioButton();
                            return true;
                        }
                    } else if (mElEmotion.isShown() && !mLlMore.isShown()) {
                        mIvEmo.setImageResource(R.mipmap.ic_cheat_emo);
                        return false;
                    }
                    showEmotionLayout();
                    hideMoreLayout();
                    hideAudioButton();
                    break;
                case R.id.ivMore:
                    UIUtils.postTaskDelay(() -> mRvMsg.smoothMoveToPosition(mRvMsg.getAdapter().getItemCount() - 1), 50);
                    mEtContent.clearFocus();
                    if (!mLlMore.isShown()) {
                        if (mElEmotion.isShown()) {
                            showMoreLayout();
                            hideEmotionLayout();
                            hideAudioButton();
                            return true;
                        }
                    }
                    showMoreLayout();
                    hideEmotionLayout();
                    hideAudioButton();
                    break;
            }
            return false;
        });
    }

    private void showAudioButton() {
        mBtnAudio.setVisibility(View.VISIBLE);
        mEtContent.setVisibility(View.GONE);
        mIvAudio.setImageResource(R.mipmap.ic_cheat_keyboard);

        if (mFlEmotionView.isShown()) {
            if (mEmotionKeyboard != null) {
                mEmotionKeyboard.interceptBackPress();
            }
        } else {
            if (mEmotionKeyboard != null) {
                mEmotionKeyboard.hideSoftInput();
            }
        }
    }

    private void hideAudioButton() {
        mBtnAudio.setVisibility(View.GONE);
        mEtContent.setVisibility(View.VISIBLE);
        mIvAudio.setImageResource(R.mipmap.ic_cheat_voice);
    }

    private void showEmotionLayout() {
        mElEmotion.setVisibility(View.VISIBLE);
        mIvEmo.setImageResource(R.mipmap.ic_cheat_keyboard);
    }

    private void hideEmotionLayout() {
        mElEmotion.setVisibility(View.GONE);
        mIvEmo.setImageResource(R.mipmap.ic_cheat_emo);
    }

    private void showMoreLayout() {
        mLlMore.setVisibility(View.VISIBLE);
    }

    private void hideMoreLayout() {
        mLlMore.setVisibility(View.GONE);
    }

    private void closeBottomAndKeyboard() {
        mElEmotion.setVisibility(View.GONE);
        mLlMore.setVisibility(View.GONE);
        if (mEmotionKeyboard != null) {
            mEmotionKeyboard.interceptBackPress();
            mIvEmo.setImageResource(R.mipmap.ic_cheat_emo);
        }
    }

    @Override
    public void onBackPressed() {
        if (mElEmotion.isShown() || mLlMore.isShown()) {
            mEmotionKeyboard.interceptBackPress();
            mIvEmo.setImageResource(R.mipmap.ic_cheat_emo);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected ChatAtPresenter createPresenter() {
        mLoginUserId = ConfigApplication.instance().mLoginUser.getUserId();
        if (getIntent() != null) {
            mFriend = (Friend) getIntent().getSerializableExtra(Constants.EXTRA_FRIEND);
        }
        assert mFriend != null;
        messageUtils=new MessageUtils(chatMessageList);
        messageUtils.setmFriend(mFriend);
        // 表示已读
        FriendDao.getInstance().markUserMessageRead(mLoginUserId, mFriend.getUserId());
        return new ChatAtPresenter(this,messageUtils);
    }

    @Override
    public void onEmojiSelected(String key) {
//        LogUtils.e("onEmojiSelected : " + key);
        L_.e("选择头像    ------>  " + key);
    }

    @Override
    public void onStickerSelected(String categoryName, String stickerName, String stickerBitmapPath) {
        L_.e("选择头像    ------>" + categoryName + stickerName + stickerBitmapPath);
        //mvpPresenter.sendFileMsg(stickerBitmapPath);
        //mvpPresenter.sendFileMsg(new File(stickerBitmapPath));
        //mvpPresenter.sendGifFile(new File(stickerBitmapPath));
        T_.showToastReal("暂不支持");
    }

    @Override
    public BGARefreshLayout getRefreshLayout() {
        return mRefreshLayout;
    }

    @Override
    public LQRRecyclerView getRvMsg() {
        return mRvMsg;
    }

    @Override
    public EditText getEtContent() {
        return mEtContent;
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mvpPresenter.loadMore();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    @Override
    public void onMessageSendStateChange(int messageState, int msg_id) {

    }

    @Override
    public boolean onNewMessage(String fromUserId, ChatMessage message, boolean isGroupMsg) {
        return false;
    }
}
