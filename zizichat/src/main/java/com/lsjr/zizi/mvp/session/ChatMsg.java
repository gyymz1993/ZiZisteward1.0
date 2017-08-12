package com.lsjr.zizi.mvp.session;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.lsjr.bean.Result;
import com.lsjr.callback.StringCallBack;
import com.lsjr.zizi.AppConfig;
import com.lsjr.zizi.http.HttpUtils;
import com.lsjr.zizi.mvp.chat.ConfigApplication;
import com.lsjr.zizi.mvp.chat.bean.UploadFileResult;
import com.lsjr.zizi.mvp.chat.dao.ChatMessageDao;
import com.lsjr.zizi.mvp.chat.dao.FriendDao;
import com.lsjr.zizi.mvp.chat.db.ChatMessage;
import com.lsjr.zizi.mvp.chat.db.Friend;
import com.lsjr.zizi.mvp.chat.utils.TimeUtils;
import com.lsjr.zizi.mvp.chat.xmpp.CoreService;
import com.lsjr.zizi.mvp.chat.xmpp.ListenerManager;
import com.lsjr.zizi.mvp.chat.xmpp.XmppMessage;
import com.lsjr.zizi.mvp.chat.xmpp.listener.ChatMessageListener;
import com.nostra13.universalimageloader.utils.L;
import com.ymz.baselibrary.utils.L_;
import com.ymz.baselibrary.utils.T_;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.rong.message.FileMessage;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import io.rong.message.VoiceMessage;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/7/31 9:47
 */

public class ChatMsg  {

    private static ChatMsg chatMsg;
    private List<ChatMessage> mChatMessages;// 存储聊天消息
    private CoreService mService;
    public String mLoginUserId;
    private String mLoginNickName;
    private Friend mFriend;
    private boolean mHasSend = false;// 有没有发送过消息，发送过需要更新界面

    private ChatMsg() {
        mLoginUserId = ConfigApplication.instance().mLoginUser.getUserId();
        mLoginNickName = ConfigApplication.instance().mLoginUser.getNickName();
        getmBlackList();
    }

    public void init(){
        mChatMessages=new ArrayList<>();
    }
    public static ChatMsg getInStance() {
        if (chatMsg==null){
            synchronized (ChatMsg.class) {
                if (chatMsg == null) {
                    chatMsg = new ChatMsg();
                }
            }
        }
        return chatMsg;
    }

    public void setmFriend(Friend mFriend) {
        this.mFriend = mFriend;
    }

    public Friend getmFriend() {
        return mFriend;
    }

    public List<ChatMessage> getmChatMessages() {
        return mChatMessages;
    }

    public CoreService getmService() {
        return mService;
    }

    public void setmService(CoreService mService) {
        this.mService = mService;
    }

    public interface OnRefreshChatViewListener {
        void onRefresh();
    }
    public interface OnChatMessageListener{
        void onChatMessage(ChatMessage message);
    }

    private OnChatMessageListener onChatMessageListener;

    public void setOnChatMessageListener(OnChatMessageListener onChatMessageListener) {
        this.onChatMessageListener = onChatMessageListener;
    }

    private OnRefreshChatViewListener onRefreshChatViewListener;

    public void setOnRefreshChatViewListener(OnRefreshChatViewListener onRefreshChatViewListener) {
        this.onRefreshChatViewListener = onRefreshChatViewListener;
    }


    private void onRefreshAdatpter(ChatMessage message){
        if (onChatMessageListener!=null){
            onChatMessageListener.onChatMessage(message);
        }
    }
    private void setOnRefresh() {
        if (onRefreshChatViewListener != null) {
            onRefreshChatViewListener.onRefresh();
        }
    }

    public void sendText(String text) {
        Log.d("wang", "sendText");
        if (TextUtils.isEmpty(text)) {
            return;
        }
        ChatMessage message = new ChatMessage();
        message.setType(XmppMessage.TYPE_TEXT);
        message.setContent(text);
        message.setFromUserName(mLoginNickName);
        message.setFromUserId(mLoginUserId);
        message.setTimeSend(TimeUtils.sk_time_current_time());
        mChatMessages.add(message);
        sendMessage(message);
    }

    public void sendGif(String text) {
        Log.d("wang", "sendgif");
        if (TextUtils.isEmpty(text)) {
            return;
        }
        ChatMessage message = new ChatMessage();
        message.setType(XmppMessage.TYPE_GIF);
        message.setContent(text);
        message.setFromUserName(mLoginNickName);
        message.setFromUserId(mLoginUserId);
        message.setTimeSend(TimeUtils.sk_time_current_time());
        mChatMessages.add(message);
        sendMessage(message);
        //TODO 刷新界面
        //TODO 刷新界面
        setOnRefresh();

    }

    public void sendVoice(Uri audioPath, int timeLen) {

        File file = new File(audioPath.getPath());
        L.e("file------------------>","1111111111"+file.getAbsolutePath());
        VoiceMessage voiceMessage = VoiceMessage.obtain(audioPath, timeLen);
        if (!file.exists()) {
            L.e("file------------------------>","没有此路径");
            return;
        }
        ChatMessage message = new ChatMessage();
        long fileSize = file.length();
        message.setMessageContent(voiceMessage);
        message.setType(XmppMessage.TYPE_VOICE);
        message.setContent("");
        message.setFromUserName(mLoginNickName);
        message.setFromUserId(mLoginUserId);
        message.setTimeSend(TimeUtils.sk_time_current_time());
        message.setFilePath(file.getAbsolutePath());
        message.setFileSize((int) fileSize);
        message.setTimeLen(timeLen);
        sendMessage(message);
        mChatMessages.add(message);
        //TODO 刷新界面
        setOnRefresh();

    }


    public void sendImage(File file, ImageMessage imageMessage) {
        if (!file.exists()) {
            L_.e("文件不存在");
            return;
        }
        long fileSize = file.length();
        ChatMessage message = new ChatMessage();
        message.setMessageContent(imageMessage);
        message.setType(XmppMessage.TYPE_IMAGE);
        message.setContent("");
        message.setFromUserName(mLoginNickName);
        message.setFromUserId(mLoginUserId);
        message.setTimeSend(TimeUtils.sk_time_current_time());
        String filePath = file.getAbsolutePath();
        message.setFilePath(filePath);
        message.setFileSize((int) fileSize);
        mChatMessages.add(message);
        sendMessage(message);
        //TODO 刷新界面
        setOnRefresh();

    }

    public void sendVideo(File file) {
        if (!file.exists()) {
            return;
        }
        long fileSize = file.length();
        FileMessage fileMessage=FileMessage.obtain(Uri.fromFile(file));
        ChatMessage message = new ChatMessage();
        message.setType(XmppMessage.TYPE_VIDEO);
        message.setMessageContent(fileMessage);
        message.setContent("");
        message.setFromUserName(mLoginNickName);
        message.setFromUserId(mLoginUserId);
        message.setTimeSend(TimeUtils.sk_time_current_time());
        String filePath = file.getAbsolutePath();
        L_.e("视频绝对路径:" + filePath);
        message.setFilePath(filePath);
        message.setFileSize((int) fileSize);
        sendMessage(message);
        mChatMessages.add(message);
        //TODO 刷新界面
        setOnRefresh();

    }


    public void sendFile(File file) {
        if (!file.exists()) {
            return;
        }
        long fileSize = file.length();
        ChatMessage message = new ChatMessage();
        message.setType(XmppMessage.TYPE_FILE);
        message.setContent("");
        message.setFromUserName(mLoginNickName);
        message.setFromUserId(mLoginUserId);
        message.setTimeSend(TimeUtils.sk_time_current_time());
        String filePath = file.getAbsolutePath();
        message.setFilePath(filePath);
        message.setFileSize((int) fileSize);
        mChatMessages.add(message);
        sendMessage(message);
        Log.d("roamer", "开始发送文件");
        //TODO 刷新界面
        setOnRefresh();

    }


    public void sendLocate(LocationMessage locationMessage,double latitude, double longitude, String address) {
        ChatMessage message = new ChatMessage();
        message.setType(XmppMessage.TYPE_LOCATION);
        message.setFromUserName(mLoginNickName);
        message.setFromUserId(mLoginUserId);
        message.setTimeSend(TimeUtils.sk_time_current_time());
        message.setLocation_x(latitude + "");
        message.setLocation_y(longitude + "");
        message.setContent(address);
        message.setMessageContent(locationMessage);
        mChatMessages.add(message);
        sendMessage(message);
        //TODO 刷新界面
        setOnRefresh();

    }

    public void sendCard(String ObjectId) {
        ChatMessage message = new ChatMessage();
        message.setType(XmppMessage.TYPE_CARD);
        message.setFromUserName(mLoginNickName);
        message.setFromUserId(mLoginUserId);
        message.setTimeSend(TimeUtils.sk_time_current_time());
        message.setObjectId(ObjectId);
        message.setContent(ConfigApplication.instance().mLoginUser.getSex() + "");// 性别
        // 0表示女，1表示男
        mChatMessages.add(message);
        sendMessage(message);
        //TODO 刷新界面
        setOnRefresh();
        // mChatContentView.notifyDataSetInvalidated(true);
    }



    /**
     所有发送消息的口子
     **/
    public void sendMessage(final ChatMessage message) {
        if (interprect(message)) {
            return;
        }
        mHasSend = true;
        Log.d("roamer", "开始发送消息,ChatBottomView的回调 sendmessage");
        message.setPacketId(UUID.randomUUID().toString().replaceAll("-", ""));
        L_.e(mLoginUserId);
        ChatMessageDao.getInstance().saveNewSingleChatMessage(mLoginUserId, mFriend.getUserId(), message);
        if (message.getType() == XmppMessage.TYPE_VOICE || message.getType() == XmppMessage.TYPE_IMAGE
                || message.getType() == XmppMessage.TYPE_VIDEO || message.getType() == XmppMessage.TYPE_FILE) {
            if (!message.isUpload()) {
                L_.e("roamer", "去更新服务器的数据"+message.getType());
                //TODO  上传文件
                postFiles(mFriend.getUserId(),message);

            } else {
                Log.d("roamer", "sendChatMessage....");
                mService.sendChatMessage(mFriend.getUserId(), message);
            }
        } else {
            Log.d("roamer", "sendChatMessage");
            mService.sendChatMessage(mFriend.getUserId(), message);
        }
    }


    public void postFiles(final String toUserId ,final ChatMessage message){
        Log.d("roamer", "开始上传...");
        Map<String,String> params=new HashMap<>();
        final String loginUserId = ConfigApplication.instance().mLoginUser.getUserId();
        params.put("userId", loginUserId);
        params.put("access_token", ConfigApplication.instance().mAccessToken);
        File file=new File(message.getFilePath());
        List<File> files=new ArrayList<>();
        files.add(file);
        L_.e("roamer", "开始上传...类型"+message.getType()+"...."+message.getFilePath());
        HttpUtils.getInstance().uploadFileWithParts(AppConfig.UPLOAD_URL, params, files, new StringCallBack() {
            @Override
            protected void onXError(String exception) {
               L.e("发送失败---------exception");
            }

            @Override
            protected void onSuccess(String response) {
                L.e("发送成功--------->");
                String url = null;
                UploadFileResult result = null;
                L_.e(response);
                try {
                    result = JSON.parseObject(response, UploadFileResult.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (result == null || result.getResultCode() != Result.CODE_SUCCESS || result.getData() == null
                        || result.getSuccess() != result.getTotal()) {
                } else {
                    UploadFileResult.Data data = result.getData();
                    if (message.getType() == XmppMessage.TYPE_IMAGE) {
                        if (data.getImages() != null && data.getImages().size() > 0) {
                            url = data.getImages().get(0).getOriginalUrl();
                        }
                    } else if (message.getType() == XmppMessage.TYPE_VOICE) {
                        if (data.getAudios() != null && data.getAudios().size() > 0) {
                            url = data.getAudios().get(0).getOriginalUrl();
                        }
                        if (TextUtils.isEmpty(url)){
                            url=data.getOthers().get(0).getOriginalUrl();
                        }
                    } else if (message.getType() == XmppMessage.TYPE_VIDEO) {
                        if (data.getVideos() != null && data.getVideos().size() > 0) {
                            url = data.getVideos().get(0).getOriginalUrl();
                        }
                    } else if (message.getType() == XmppMessage.TYPE_FILE){
                        if (data.getFiles() != null && data.getFiles().size() > 0) {
                            url = data.getFiles().get(0).getOriginalUrl();
                        }else if (data.getVideos() != null && data.getVideos().size() > 0) {
                            url = data.getVideos().get(0).getOriginalUrl();
                        }else if (data.getAudios() != null && data.getAudios().size() > 0) {
                            url = data.getAudios().get(0).getOriginalUrl();
                        }else if (data.getImages() != null && data.getImages().size() > 0) {
                            url = data.getImages().get(0).getOriginalUrl();
                        }else if(data.getOthers()!=null&&data.getOthers().size()>0){
                            url=data.getOthers().get(0).getOriginalUrl();
                        }
                    }
                }
                L_.e("url:"+url);
                if (TextUtils.isEmpty(url)) {
                    ChatMessageDao.getInstance().updateMessageUploadState(loginUserId, toUserId, message.get_id(), false, url);
                    for (int i = 0; i < mChatMessages.size(); i++) {
                        ChatMessage msg = mChatMessages.get(i);
                        if (message.get_id() == msg.get_id()) {
                            msg.setMessageState(ChatMessageListener.MESSAGE_SEND_FAILED);
                            ChatMessageDao.getInstance().updateMessageSendState(mLoginUserId, mFriend.getUserId(),
                                    message.get_id(), ChatMessageListener.MESSAGE_SEND_FAILED);
                            break;
                        }
                    }
                } else {
                    ChatMessageDao.getInstance().updateMessageUploadState(loginUserId, toUserId, message.get_id(), true, url);
                    Log.d("roamer", "上传文件成功了");
                    message.setContent(url);
                    message.setUpload(true);
                    message.setMessageState(ChatMessageListener.MESSAGE_SEND_SUCCESS);
                    mService.sendChatMessage(mFriend.getUserId(), message);
                }
            }
        });
    }


    /**
     * 拦截发送的消息
     *
     * @param message
     */
    private List<Friend> mBlackList;//TODO 黑名单列表
    //得到当前用户的黑名单列表
    public List<Friend> getmBlackList() {
        return mBlackList = FriendDao.getInstance().getAllBlacklists(mLoginUserId);
    }

    public boolean interprect(ChatMessage message) {
        int len = 0;
        for (Friend friend : mBlackList) {
            if (friend.getUserId().equals(mFriend.getUserId())) {
                T_.showToastReal("已经加入黑名单,无法发送消息");
                len++;
            }
        }
        if (len != 0) {
            // finish();
            ListenerManager.getInstance().notifyMessageSendStateChange(mLoginUserId, mFriend.getUserId(),
                    message.get_id(), ChatMessageListener.MESSAGE_SEND_FAILED);
            return true;
        }
        return false;
    }


}
