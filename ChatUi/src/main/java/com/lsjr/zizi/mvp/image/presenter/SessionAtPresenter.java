package com.lsjr.zizi.mvp.home.photo.presenter;

import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lqr.audio.AudioPlayManager;
import com.lqr.audio.IAudioPlayListener;
import com.lsjr.zizi.R;
import com.lsjr.zizi.UserCache;
import com.lsjr.zizi.base.BaseFragmentActivity;
import com.lsjr.zizi.base.BaseFragmentPresenter;
import com.lsjr.zizi.bean.LocationData;
import com.lsjr.zizi.bean.RPConstant;
import com.lsjr.zizi.bean.RedPacketMessage;
import com.lsjr.zizi.mvp.home.photo.view.ISessionAtView;
import com.lsjr.zizi.mvp.SessionAdapter;
import com.lsjr.zizi.util.FileOpenUtils;
import com.lsjr.zizi.util.LogUtils;
import com.lsjr.zizi.util.MediaFileUtils;
import com.lsjr.zizi.util.UIUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.FileMessage;
import io.rong.message.GroupNotificationMessage;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import io.rong.message.RecallNotificationMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;



public class SessionAtPresenter extends BaseFragmentPresenter<ISessionAtView> {

    public Conversation.ConversationType mConversationType;
    private String mSessionId;

    private List<Message> mData=new ArrayList<>();
    private SessionAdapter mAdapter;

    public SessionAtPresenter(BaseFragmentActivity context, String sessionId, Conversation.ConversationType conversationType) {
        super(context);
        for (int i = 0; i < 2; i++) {
            Message message = new Message();
            Message.SentStatus sentStatus;
            if (i % 2 == 0) {
                sentStatus = Message.SentStatus.setValue(30);
                message.setMessageDirection(Message.MessageDirection.SEND);
            } else {
                sentStatus = Message.SentStatus.setValue(30);
                message.setMessageDirection(Message.MessageDirection.RECEIVE);
            }
            // SEND  1
            message.setSentStatus(sentStatus);
            message.setContent(new TextMessage("测试"));
            mData.add(message);
        }
        mSessionId = sessionId;
        mConversationType = conversationType;
    }

    public Message getNewSendMessage(){
        Message message=new Message();
        Message.SentStatus sentStatus = Message.SentStatus.setValue(30);
        message.setMessageDirection(Message.MessageDirection.SEND);
        message.setSentStatus(sentStatus);
        return message;
    }

    public Message getNewReceiveMessage(){
        Message message=new Message();
        Message.SentStatus sentStatus = Message.SentStatus.setValue(30);
        message.setMessageDirection(Message.MessageDirection.RECEIVE);
        message.setSentStatus(sentStatus);
        return message;

    }

    public void loadMessage() {
        loadData();
        setAdapter();
    }

    private void loadData() {
        setAdapter();
    }

    public void loadMore() {
        mAdapter.notifyDataSetChangedWrapper();
    }




    public void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new SessionAdapter(mContext, mData, this);
            mAdapter.setOnItemClickListener((helper, parent, itemView, position) -> {
                Message message = mData.get(position);
                MessageContent content = message.getContent();
                if (content instanceof ImageMessage) {
//                    ImageMessage imageMessage = (ImageMessage) content;
//                    Intent intent = new Intent(mContext, ShowBigImageActivity.class);
//                    intent.putExtra("url", imageMessage.getLocalUri() == null ? imageMessage.getRemoteUri().toString() : imageMessage.getLocalUri().toString());
//                    mContext.jumpToActivity(intent);
                } else if (content instanceof FileMessage) {
                    FileMessage fileMessage = (FileMessage) content;
                    if (MediaFileUtils.isVideoFileType(fileMessage.getName())) {
                        helper.getView(R.id.bivPic).setOnClickListener(v -> {
                            boolean isSend = message.getMessageDirection() == Message.MessageDirection.SEND ? true : false;
                            if (isSend) {
                                if (fileMessage.getLocalPath() != null && new File(fileMessage.getLocalPath().getPath()).exists()) {
                                    FileOpenUtils.openFile(mContext, fileMessage.getLocalPath().getPath());
                                } else {
                                    downloadMediaMessage(message);
                                }
                            } else {
                                Message.ReceivedStatus receivedStatus = message.getReceivedStatus();
                                if (/*receivedStatus.isDownload() || */receivedStatus.isRetrieved()) {
                                    if (fileMessage.getLocalPath() != null) {
                                        FileOpenUtils.openFile(mContext, fileMessage.getLocalPath().getPath());
                                    } else {
                                        UIUtils.showToast(UIUtils.getString(R.string.file_out_of_date));
                                    }
                                } else {
                                    downloadMediaMessage(message);
                                }
                            }
                        });
                    }
                } else if (content instanceof VoiceMessage) {
                    VoiceMessage voiceMessage = (VoiceMessage) content;
                    final ImageView ivAudio = helper.getView(R.id.ivAudio);
                    AudioPlayManager.getInstance().startPlay(mContext, voiceMessage.getUri(), new IAudioPlayListener() {
                        @Override
                        public void onStart(Uri var1) {
                            if (ivAudio != null && ivAudio.getBackground() instanceof AnimationDrawable) {
                                AnimationDrawable animation = (AnimationDrawable) ivAudio.getBackground();
                                animation.start();
                            }
                        }

                        @Override
                        public void onStop(Uri var1) {
                            if (ivAudio != null && ivAudio.getBackground() instanceof AnimationDrawable) {
                                AnimationDrawable animation = (AnimationDrawable) ivAudio.getBackground();
                                animation.stop();
                                animation.selectDrawable(0);
                            }

                        }

                        @Override
                        public void onComplete(Uri var1) {
                            if (ivAudio != null && ivAudio.getBackground() instanceof AnimationDrawable) {
                                AnimationDrawable animation = (AnimationDrawable) ivAudio.getBackground();
                                animation.stop();
                                animation.selectDrawable(0);
                            }
                        }
                    });
                } else if (content instanceof RedPacketMessage) {
                    RedPacketMessage redPacketMessage = (RedPacketMessage) content;
                    int chatType = mConversationType == Conversation.ConversationType.PRIVATE ? RPConstant.RP_ITEM_TYPE_SINGLE : RPConstant.RP_ITEM_TYPE_GROUP;
                    String redPacketId = redPacketMessage.getBribery_ID();
                    String redPacketType = redPacketMessage.getBribery_Message();
                   // String receiverId = UserCache.getId();
                    String direct = RPConstant.MESSAGE_DIRECT_RECEIVE;
                   //RedPacketUtil.openRedPacket(((SessionActivity) mContext), chatType, redPacketId, redPacketType, receiverId, direct);
                }
            });
            getView().getRvMsg().setAdapter(mAdapter);
            mAdapter.setOnItemLongClickListener((helper, viewGroup, view, position) -> {
                View sessionMenuView = View.inflate(mContext, R.layout.dialog_session_menu, null);
              //  mSessionMenuDialog = new CustomDialog(mContext, sessionMenuView, R.style.MyDialog);
                TextView tvReCall = (TextView) sessionMenuView.findViewById(R.id.tvReCall);
                TextView tvDelete = (TextView) sessionMenuView.findViewById(R.id.tvDelete);

                //根据消息类型控制显隐
                Message message = mData.get(position);
                MessageContent content = message.getContent();
                if (content instanceof GroupNotificationMessage || content instanceof RecallNotificationMessage) {
                    return false;
                }
                if (content instanceof RedPacketMessage || !message.getSenderUserId().equalsIgnoreCase(UserCache.getId())) {
                    tvReCall.setVisibility(View.GONE);
                }

                tvReCall.setOnClickListener(v -> RongIMClient.getInstance().recallMessage(message, "", new RongIMClient.ResultCallback<RecallNotificationMessage>() {
                    @Override
                    public void onSuccess(RecallNotificationMessage recallNotificationMessage) {
                        UIUtils.postTaskSafely(() -> {
                            recallMessageAndInsertMessage(recallNotificationMessage, position);
                            UIUtils.showToast(UIUtils.getString(R.string.recall_success));
                        });
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        UIUtils.postTaskSafely(() -> {
                            UIUtils.showToast(UIUtils.getString(R.string.recall_fail) + ":" + errorCode.getValue());
                        });
                    }
                }));
                tvDelete.setOnClickListener(v -> RongIMClient.getInstance().deleteMessages(new int[]{message.getMessageId()}, new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        UIUtils.postTaskSafely(() -> {
                            mData.remove(position);
                            mAdapter.notifyDataSetChangedWrapper();
                            UIUtils.showToast(UIUtils.getString(R.string.delete_success));
                        });
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        UIUtils.postTaskSafely(() -> {
                            UIUtils.showToast(UIUtils.getString(R.string.delete_fail) + ":" + errorCode.getValue());
                        });
                    }
                }));
                return false;
            });
            UIUtils.postTaskDelay(() -> getView().getRvMsg().smoothMoveToPosition(mData.size() - 1), 200);
        } else {
            mAdapter.notifyDataSetChangedWrapper();
            if (getView() != null && getView().getRvMsg() != null)
                rvMoveToBottom();
        }
    }

    private void rvMoveToBottom() {
        getView().getRvMsg().smoothMoveToPosition(mData.size() - 1);
    }

    private void updateMessageStatus(Message message) {
//        for (int i = 0; i < mData.size(); i++) {
//            Message msg = mData.get(i);
//            if (msg.getMessageId() == message.getMessageId()) {
//                mData.remove(i);
//                mData.add(i, message);
//                mAdapter.notifyDataSetChangedWrapper();
//                break;
//            }
//        }
    }


    public void sendTextMsg() {
        sendTextMsg(getView().getEtContent().getText().toString());
        getView().getEtContent().setText("");
    }

    public void sendTextMsg(String content) {
        Message message = getNewSendMessage();
        message.setContent(new TextMessage(content));
        mAdapter.addLastItem(message);
        rvMoveToBottom();
    }


    public void sendImgMsg(File imageFileThumb, File imageFileSource) {
        Uri imageFileThumbUri = Uri.fromFile(imageFileThumb);
        Uri imageFileSourceUri = Uri.fromFile(imageFileSource);
        sendImgMsg(imageFileThumbUri, imageFileSourceUri);
    }


    public void sendImgMsg(Uri imageFileThumbUri, Uri imageFileSourceUri) {
        ImageMessage imgMsg = ImageMessage.obtain(imageFileThumbUri, imageFileSourceUri);
        //如果发送成功？
        Message message=getNewSendMessage();
        message.setContent(imgMsg);
        //保存数据库成功
        mAdapter.addLastItem(message);
        rvMoveToBottom();
        // //发送失败
        updateMessageStatus(message); //失败成功状态
    }


    public void sendFileMsg(File file) {
        Message fileMessage = Message.obtain(mSessionId, mConversationType, FileMessage.obtain(Uri.fromFile(file)));
       // Message message = getNewReceiveMessage();
        //Message message=new Message();
        Message.SentStatus sentStatus = Message.SentStatus.setValue(30);
        fileMessage.setMessageDirection(Message.MessageDirection.RECEIVE);
        fileMessage.setSentStatus(sentStatus);
        //message.setContent(fileMessage);
        //保存数据库成功
        mAdapter.addLastItem(fileMessage);
        rvMoveToBottom();
        //发送成功
        updateMessageStatus(fileMessage);

    }

    public void sendLocationMessage(LocationData locationData) {
        LocationMessage locationMessage = LocationMessage.obtain(locationData.getLat(), locationData.getLng(), locationData.getPoi(), Uri.parse(locationData.getImgUrl()));
        //保存数据库成功
        Message newReceiveMessage = getNewReceiveMessage();
        newReceiveMessage.setContent(locationMessage);
        mAdapter.addLastItem(newReceiveMessage);
        rvMoveToBottom();

    }

    public void sendAudioFile(Uri audioPath, int duration) {
        if (audioPath != null) {
            File file = new File(audioPath.getPath());
            if (!file.exists() || file.length() == 0L) {
                LogUtils.sf(UIUtils.getString(R.string.send_audio_fail));
                return;
            }
            VoiceMessage voiceMessage = VoiceMessage.obtain(audioPath, duration);
            Message message = getNewSendMessage();
            message.setContent(voiceMessage);
            //保存数据库成功
            mAdapter.addLastItem(message);
            rvMoveToBottom();
            //发送成功
            updateMessageStatus(message);

        }
    }

    public void sendRedPacketMsg() {
//        if (mConversationType == Conversation.ConversationType.PRIVATE) {
//            UserInfo userInfo = DBManager.getInstance().getUserInfo(mSessionId);
//            if (userInfo != null)
//                RedPacketUtil.startRedPacket(mContext, userInfo, RPSendPacketCallback);
//        } else {
//            List<GroupMember> groupMembers = DBManager.getInstance().getGroupMembers(mSessionId);
//            if (groupMembers != null)
//                RedPacketUtil.startRedPacket(mContext, mSessionId, groupMembers.size(), RPSendPacketCallback);
//        }
    }

//    RPSendPacketCallback RPSendPacketCallback = new RPSendPacketCallback() {
//        @Override
//        public void onGenerateRedPacketId(String redPacketId) {
//
//        }
//
//        @Override
//        public void onSendPacketSuccess(RedPacketInfo redPacketInfo) {
//            RedPacketMessage rpMsg = RedPacketMessage.obtain(redPacketInfo.redPacketId, redPacketInfo.fromNickName, redPacketInfo.redPacketType, redPacketInfo.redPacketGreeting);
//            RongIMClient.getInstance().sendMessage(Message.obtain(mSessionId, mConversationType, rpMsg), mPushCotent, mPushData, new IRongCallback.ISendMessageCallback() {
//                @Override
//                public void onAttached(Message message) {
//                    //保存数据库成功
//                    mAdapter.addLastItem(message);
//                    rvMoveToBottom();
//                }
//
//                @Override
//                public void onSuccess(Message message) {
//                    //发送成功
//                    updateMessageStatus(message);
//                }
//
//                @Override
//                public void onError(Message message, RongIMClient.ErrorCode errorCode) {
//                    //发送失败
//                    updateMessageStatus(message);
//                }
//            });
//        }
//    };

    public void downloadMediaMessage(Message message) {
        RongIMClient.getInstance().downloadMediaMessage(message, new IRongCallback.IDownloadMediaMessageCallback() {
            @Override
            public void onSuccess(Message message) {
                message.getReceivedStatus().setDownload();
                updateMessageStatus(message);
            }

            @Override
            public void onProgress(Message message, int progress) {
                //发送进度
                message.setExtra(progress + "");
                updateMessageStatus(message);
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                updateMessageStatus(message);
            }

            @Override
            public void onCanceled(Message message) {
                updateMessageStatus(message);
            }
        });
    }



    private void saveHistoryMsg(List<Message> messages) {
        //messages的时间顺序从新到旧排列，所以必须反过来加入到mData中
        if (messages != null && messages.size() > 0) {
            for (Message msg : messages) {
                mData.add(0, msg);
            }
            getView().getRvMsg().moveToPosition(messages.size() - 1);
        }
    }

    private void loadMessageError(RongIMClient.ErrorCode errorCode) {
        LogUtils.sf("拉取历史消息失败，errorCode = " + errorCode);
    }

    private void loadError(Throwable throwable) {
        LogUtils.sf(throwable.getLocalizedMessage());
    }

    public void recallMessageFromListener(int messageId, RecallNotificationMessage recallNotificationMessage) {
        for (int i = 0; i < mData.size(); i++) {
            Message message = mData.get(i);
            if (message.getMessageId() == messageId) {
                recallMessageAndInsertMessage(recallNotificationMessage, i);
                break;
            }
        }
    }

    private void recallMessageAndInsertMessage(RecallNotificationMessage recallNotificationMessage, int position) {
       // RongIMClient.getInstance().insertMessage(mConversationType, mSessionId, UserCache.getId(), recallNotificationMessage);
        mData.remove(position);
        mData.add(Message.obtain(mSessionId, mConversationType, recallNotificationMessage));
        mAdapter.notifyDataSetChangedWrapper();
    }
}
