package com.lsjr.zizi.mvp.home.session;

import android.net.Uri;

import com.lsjr.zizi.bean.LocationData;
import com.lsjr.zizi.mvp.chat.dao.ChatMessageDao;
import com.lsjr.zizi.mvp.chat.db.ChatMessage;
import com.lsjr.zizi.mvp.chat.xmpp.ListenerManager;
import com.lsjr.zizi.mvp.chat.xmpp.ReceiptManager;
import com.lsjr.zizi.mvp.chat.xmpp.listener.ChatMessageListener;
import com.lsjr.zizi.mvp.home.photo.view.ISessionAtView;
import com.ymz.baselibrary.mvp.BasePresenter;
import com.ymz.baselibrary.utils.L_;
import com.ymz.baselibrary.utils.UIUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.FileMessage;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;


public class ChatAtPresenter extends BasePresenter<ISessionAtView> implements ChatMessageListener {
    private String mSessionId;
    private Conversation.ConversationType mConversationType = Conversation.ConversationType.PRIVATE;
    private ChatContentAdapter chatContentAdapter;
    private String mUserId;
    private String mLoginUserId;
    private MessageUtils messageUtils;

    ChatAtPresenter(ISessionAtView mvpView, MessageUtils msgtils) {
        super(mvpView);
        ListenerManager.getInstance().addChatMessageListener(this);
        messageUtils = msgtils;
        mUserId = messageUtils.getmFriend().getUserId();
        mLoginUserId = messageUtils.mLoginUserId;
    }

    void loadDatas(final boolean scrollToBottom) {
        int mMinId;
        if (messageUtils.getmChatMessages().size() > 0) {
            mMinId = messageUtils.getmChatMessages().get(0).get_id();
        } else {
            mMinId = 0;
        }
        int mPageSize = 10;
        List<ChatMessage> chatLists = ChatMessageDao.getInstance().getSingleChatMessages(mLoginUserId,
                mUserId, mMinId, mPageSize);
        if (chatLists == null || chatLists.size() <= 0) {
            getView().getRefreshLayout().endRefreshing();
        } else {
            List<ChatMessage> chatMessages = new ArrayList<>();
            long currentTime = System.currentTimeMillis() / 1000;
            for (int i = 0; i < chatLists.size(); i++) {
                ChatMessage message = chatLists.get(i);
                if (message.isMySend() && message.getMessageState() == ChatMessageListener.MESSAGE_SEND_ING) {
                    // 如果是我发的消息，有时候在消息发送中，直接退出了程序，此时消息发送状态可能使用是发送中，
                    if (currentTime - message.getTimeSend() > ReceiptManager.MESSAGE_DELAY / 1000) {
                        ChatMessageDao.getInstance().updateMessageSendState(mLoginUserId, mUserId,
                                message.get_id(), ChatMessageListener.MESSAGE_SEND_FAILED);
                        message.setMessageState(ChatMessageListener.MESSAGE_SEND_FAILED);
                    }
                }
                chatMessages.add(0, message);
                // messageUtils.getmChatMessages().add(0,message);

            }
            getView().getRefreshLayout().endRefreshing();
            if (chatMessages.size() == 0) return;
            if (scrollToBottom) {
                // ChatMsg.getInStance().setmChatMessages(chatMessages);
                messageUtils.getmChatMessages().clear();
                messageUtils.getmChatMessages().addAll(chatMessages);
               // chatContentAdapter.setData(chatMessages);
                setAdapter();
            } else {
                chatContentAdapter.addNewData(chatMessages);
//                if (getView() != null && getView().getRvMsg() != null)
//                    getView().getRvMsg().smoothMoveToPosition(messageUtils.getmChatMessages().size()-chatLists.size());
                // chatContentAdapter.notifyDataSetChangedWrapper();
            }
        }
    }


    public void loadMore() {
        loadDatas(false);
    }

    public void setAdapter() {
        if (chatContentAdapter == null) {
            chatContentAdapter = new ChatContentAdapter(UIUtils.getContext(), messageUtils.getmChatMessages(), this, mUserId, messageUtils);
            getView().getRvMsg().setAdapter(chatContentAdapter);
            UIUtils.postTaskDelay(() -> getView().getRvMsg().smoothMoveToPosition(messageUtils.getmChatMessages().size() - 1), 0);
        } else {
            chatContentAdapter.notifyDataSetChangedWrapper();
            if (getView() != null && getView().getRvMsg() != null)
                rvMoveToBottom();
        }

    }


    private void rvMoveToBottom() {
        if (getView() != null && getView().getRvMsg() != null)
            getView().getRvMsg().smoothMoveToPosition(messageUtils.getmChatMessages().size() - 1);
    }

    public void sendTextMsg() {
        sendTextMsg(getView().getEtContent().getText().toString());
        getView().getEtContent().setText("");
    }

    public void sendTextMsg(String content) {
        messageUtils.sendText(content);
    }

    public void sendImgMsg(File imageFileThumb, File imageFileSource) {
        Uri imageFileThumbUri = Uri.fromFile(imageFileThumb);
        Uri imageFileSourceUri = Uri.fromFile(imageFileSource);
        // sendImgMsg(imageFileThumbUri, imageFileSourceUri);
        ImageMessage imgMsg = ImageMessage.obtain(imageFileThumbUri, imageFileSourceUri);
        messageUtils.sendImage(imageFileSource, imgMsg);
    }

    public void sendImgMsg(File imageFileThumb) {
        ImageMessage imgMsg = ImageMessage.obtain();
        Uri imageFileThumbUri = Uri.fromFile(imageFileThumb);
        imgMsg.setRemoteUri(imageFileThumbUri);
        messageUtils.sendImage(imageFileThumb, imgMsg);
    }


    public void sendVoice(Uri audioPath, int timeLen) {
        //发送录音
        messageUtils.sendVoice(audioPath, timeLen);
    }

    public void sendCard(String ObjectId) {
        //发送录音
        messageUtils.sendCard(ObjectId);
    }


    public void sendFileMsg(File file) {

        messageUtils.sendFile(file);
        rvMoveToBottom();
        //发送成功
    }

    public void sendLocationMessage(LocationData locationData) {
        LocationMessage locationMessage = LocationMessage.obtain(locationData.getLat(), locationData.getLng(), locationData.getPoi(), Uri.parse(locationData.getImgUrl()));
        //保存数据库成功
        messageUtils.sendLocate(locationMessage, locationData.getLat(), locationData.getLng(), locationData.getPoi());
        rvMoveToBottom();

    }

    public void sendAudioFile(File file) {
        messageUtils.sendVideo(file);
        rvMoveToBottom();
        //发送成功
    }


    public void sendGif(String file) {
        messageUtils.sendGif(file);
        rvMoveToBottom();
        //发送成功
    }

    public void sendGifFile(File file){
        Message fileMessage = Message.obtain(mSessionId, mConversationType, FileMessage.obtain(Uri.fromFile(file)));
        L_.e("mSessionId  --->" + mSessionId + "----->" + FileMessage.obtain(Uri.fromFile(file)));
        mSessionId = "1";
        FileMessage obtain = FileMessage.obtain(Uri.fromFile(file));
        sendTextMsg(obtain.getName());
    }


    /**
     * 新消息到来
     */
    @Override
    public boolean onNewMessage(String fromUserId, ChatMessage message, boolean isGroupMsg) {
        L_.e("收到消息" + message.getContent());
        if (isGroupMsg) {
            return false;
        }
        if (messageUtils.getmFriend().getUserId().compareToIgnoreCase(fromUserId) == 0) {// 是该人的聊天消息
            chatContentAdapter.addLastItem(message);
            rvMoveToBottom();
            return true;
        }
        return false;
    }

    @Override
    public void onMessageSendStateChange(int messageState, int msg_id) {
        L_.e("收到消息");
        for (int i = 0; i < messageUtils.getmChatMessages().size(); i++) {
            ChatMessage msg = messageUtils.getmChatMessages().get(i);
            if (msg_id == msg.get_id()) {
                L_.e("收到消息后刷新");
                msg.setMessageState(ChatMessageListener.MESSAGE_SEND_SUCCESS);
                setAdapter();
                break;
            }
        }
    }

}
