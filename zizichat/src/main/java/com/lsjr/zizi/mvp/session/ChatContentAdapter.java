package com.lsjr.zizi.mvp.session;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lqr.adapter.LQRAdapterForRecyclerView;
import com.lqr.adapter.LQRViewHolder;
import com.lqr.adapter.LQRViewHolderForRecyclerView;
import com.lqr.audio.AudioPlayManager;
import com.lqr.audio.IAudioPlayListener;
import com.lqr.emoji.MoonUtils;
import com.lsjr.callback.DownloadSubscriber;
import com.lsjr.utils.HttpUtils;
import com.lsjr.zizi.AppConst;
import com.lsjr.zizi.R;
import com.lsjr.zizi.mvp.chat.ConfigApplication;
import com.lsjr.zizi.mvp.chat.dao.ChatMessageDao;
import com.lsjr.zizi.mvp.chat.db.ChatMessage;
import com.lsjr.zizi.mvp.chat.utils.DisplayUtil;
import com.lsjr.zizi.mvp.chat.utils.HtmlUtils;
import com.lsjr.zizi.mvp.chat.utils.SmileyParser;
import com.lsjr.zizi.mvp.chat.utils.StringUtils;
import com.lsjr.zizi.mvp.chat.xmpp.XmppMessage;
import com.lsjr.zizi.mvp.chat.xmpp.listener.ChatMessageListener;
import com.lsjr.zizi.util.FileOpenUtils;
import com.lsjr.zizi.util.FileUtils;
import com.lsjr.zizi.util.MediaFileUtils;
import com.lsjr.zizi.util.TimeUtils;
import com.lsjr.zizi.util.VideoThumbLoader;
import com.lsjr.zizi.view.BubbleImageView;
import com.ymz.baselibrary.AppCache;
import com.ymz.baselibrary.utils.L_;
import com.ymz.baselibrary.utils.UIUtils;
import com.ymz.baselibrary.view.GifView;

import java.io.File;
import java.util.List;

import io.rong.imlib.model.MessageContent;
import io.rong.message.FileMessage;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import io.rong.message.VoiceMessage;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ChatContentAdapter extends LQRAdapterForRecyclerView<ChatMessage> {

    /* 根据mLoginUserId和mToUserId 唯一确定一张表 */
    private String mLoginUserId;
    private String mToUserId;
    private boolean isSend;
    private Context mContext;
    private List<ChatMessage> mData;
    private ChatAtPresenter mPresenter;
    private static final int SEND_TEXT = R.layout.item_text_send;
    private static final int RECEIVE_TEXT = R.layout.item_text_receive;

    private static final int SEND_IMAGE = R.layout.item_image_send;
    private static final int RECEIVE_IMAGE = R.layout.item_image_receive;


    private static final int SEND_STICKER = R.layout.item_sticker_send;
    private static final int RECEIVE_STICKER = R.layout.item_sticker_receive;


    private static final int SEND_VIDEO = R.layout.item_video_send;
    private static final int RECEIVE_VIDEO = R.layout.item_video_receive;


    private static final int SEND_LOCATION = R.layout.item_location_send;
    private static final int RECEIVE_LOCATION = R.layout.item_location_receive;


    private static final int RECEIVE_NOTIFICATION = R.layout.item_notification;

    private static final int RECEIVE_VOICE = R.layout.item_audio_receive;
    private static final int SEND_VOICE = R.layout.item_audio_send;

    private static final int RECEIVE_RED_PACKET = R.layout.item_red_packet_receive;
    private static final int SEND_RED_PACKET = R.layout.item_red_packet_send;

    private static final int RECALL_NOTIFICATION = R.layout.item_notification;
    private static final int VIEW_SYSTEM = R.layout.item_notification;


    private static final int UNDEFINE_MSG = R.layout.item_no_support_msg_type;
    /*自定义类型*/
    private static final int SEND_GIF = R.layout.item_send_gif;
    private static final int RECEIVE_GIF = R.layout.item_receive_gif;

    private static final int SEND_CARD = R.layout.itme_send_card;
    private static final int RECEIVE_CARD = R.layout.itme_receive_card;


    private static final int SEND_FILE = R.layout.itme_send_file;
    private static final int RECEIVE_FILE = R.layout.itme_receive_file;

    // 匹配图标时用到的文件类型
    String[] fileTypes = new String[]{"apk", "avi", "bat", "bin", "bmp", "chm", "css", "dat", "dll", "doc", "docx",
            "dos", "dvd", "gif", "html", "ifo", "inf", "iso", "java", "jpeg", "jpg", "log", "m4a", "mid", "mov",
            "movie", "mp2", "mp2v", "mp3", "mp4", "mpe", "mpeg", "mpg", "pdf", "php", "png", "ppt", "pptx", "psd",
            "rar", "tif", "ttf", "txt", "wav", "wma", "wmv", "xls", "xlsx", "xml", "xsl", "zip"};
    String[] fileImage = new String[]{"jpg", "jpeg", "png"};
    String[] fileAudio = new String[]{"wam", "mp4"};

    public ChatContentAdapter(Context context, List<ChatMessage> data, ChatAtPresenter presenter, String toUserId) {
        super(context, data);
        if (data == null) {
            return;
        }
        mLoginUserId = ChatMsg.getInStance().mLoginUserId;
        mContext = context;
        mData = data;
        mPresenter = presenter;
        mToUserId = toUserId;

    }

    @Override
    public void convert(LQRViewHolderForRecyclerView helper, ChatMessage item, int position) {
        setTime(helper, item, position);
        setName(helper, item);
        setView(helper, item, position);
        setAdapterListener();
    }


    private void setTime(LQRViewHolderForRecyclerView helper, ChatMessage message, int position) {
          /* 设置时间 */
          /* 是否显示日期 */
        boolean showTime = true;
        if (position >= 1) {
            ChatMessage prevMessage = mData.get(position - 1);
            int prevTime = prevMessage.getTimeSend();
            int nowTime = message.getTimeSend();
            if (nowTime - prevTime < 15 * 60) {// 小于15分钟，不显示
                showTime = false;
            }
        }
        if (showTime) {
            helper.setViewVisibility(R.id.tvTime, View.VISIBLE).setText(R.id.tvTime, TimeUtils.sk_time_long_to_chat_time_str(message.getTimeSend()));
        } else {
            helper.setViewVisibility(R.id.tvTime, GONE);
        }

    }


    public int parserFileType(ChatMessage message) {
        String filePath = message.getFilePath();
        if (filePath != null) {
            int pointIndex = filePath.lastIndexOf(".");
            if (pointIndex != -1) {
                String type = filePath.substring(pointIndex + 1).toLowerCase();
                for (int i = 0; i < fileImage.length; i++) {
                    if (type.equals(fileImage[i])) {
                        return XmppMessage.TYPE_IMAGE;
                    }
                }
                for (int i = 0; i < fileAudio.length; i++) {
                    if (type.equals(fileAudio[i])) {
                        return XmppMessage.TYPE_VIDEO;
                    }
                }
            }
        }
        return 0;
    }

    private void setName(LQRViewHolderForRecyclerView helper, ChatMessage message) {
        int messageType = message.getType();
        if (messageType == XmppMessage.TYPE_TEXT || messageType == XmppMessage.TYPE_IMAGE || messageType == XmppMessage.TYPE_VIDEO
                || messageType == XmppMessage.TYPE_VOICE || messageType == XmppMessage.TYPE_LOCATION || messageType == XmppMessage.TYPE_GIF
                || messageType == XmppMessage.TYPE_CARD) {
            L_.e(messageType + "");
            helper.setViewVisibility(R.id.tvName, View.VISIBLE).setText(R.id.tvName, message.getFromUserName());
        }
    }

    private void setView(LQRViewHolderForRecyclerView helper, ChatMessage message, int position) {
        //根据消息类型设置消息显示内容
        int viewType = getItemViewType(position);
        int messageType = mData.get(position).getType();
        String content = message.getContent();
        MessageContent messageContent = message.getMessageContent();
        if (messageType == 0) return;
        switch (messageType) {
            case XmppMessage.TYPE_FILE:
                RelativeLayout relativeLayout = helper.getView(R.id.chat_file_view);
                BubbleImageView chat_file_pic = helper.getView(R.id.chat_file_pic);
                Log.d("roamer", "....TYPE_FILE要显示了...." + message.getContent());
                ImageView chat_warp_file = helper.getView(R.id.chat_to_file);
                TextView chat_file_name = helper.getView(R.id.file_name);
                ProgressBar progress = helper.getView(R.id.progress);
                ImageView unread_img_view = helper.getView(R.id.unread_img_view);
                if (!message.isMySend()) {
                    if (!message.isRead()) {
                        unread_img_view.setVisibility(View.VISIBLE);
                    } else {
                        unread_img_view.setVisibility(View.GONE);
                    }
                }
                progress.setVisibility(View.GONE);
                switch (parserFileType(message)) {
                    case XmppMessage.TYPE_IMAGE:
                        relativeLayout.setVisibility(GONE);
                        chat_file_pic.setVisibility(VISIBLE);
                        //chat_file_pic.setPercent(100);
                        //chat_file_pic.showShadow(false);
                        Glide.with(mContext).load(message.getContent()).
                                error(R.mipmap.default_img_failed).override(UIUtils.dip2px(80), UIUtils.dip2px(150)).centerCrop().into(chat_file_pic);
                        return;
                    case XmppMessage.TYPE_VIDEO:
                        return;
                    case 0:
                        String filePath = message.getFilePath();
                        String name = null;
                        if (filePath != null) {
                            int pointIndex = filePath.lastIndexOf(".");
                            if (pointIndex != -1) {
                                String type = filePath.substring(pointIndex + 1).toLowerCase();
                            }
                            int start = filePath.lastIndexOf("/");
                            name = filePath.substring(start + 1).toLowerCase();
                            Log.d("roamer", "filename::" + name);
                        }
                        chat_warp_file.setImageResource(R.mipmap.default_img_failed);
                        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/sk/" + name);
                        chat_file_name.setText(name);
                        return;
                }
                break;
            case XmppMessage.TYPE_TIP:
                /* 设置数据 */
                helper.setText(R.id.tvTime, TimeUtils.sk_time_s_long_2_str(message.getTimeSend())).setViewVisibility(R.id.tvNotification, VISIBLE);
                helper.setText(R.id.tvNotification, content).setViewVisibility(R.id.tvNotification, VISIBLE);
                break;
            case XmppMessage.TYPE_TEXT:
                String s = StringUtils.replaceSpecialChar(content);
                CharSequence charSequence = HtmlUtils.transform200SpanString(s.replaceAll("\n", "\r\n"), true);
                MoonUtils.identifyFaceExpression(mContext, helper.getView(R.id.tvText), charSequence.toString(), ImageSpan.ALIGN_BOTTOM);
                break;
            case XmppMessage.TYPE_IMAGE:
                BubbleImageView bivPic = helper.getView(R.id.bivPic);
                ImageMessage imageMessage = (ImageMessage) messageContent;
                if (messageContent != null) {
                    Glide.with(mContext).load(imageMessage.getLocalUri() == null ? imageMessage.getRemoteUri() : imageMessage.getLocalUri()).
                            error(R.mipmap.default_img_failed).override(UIUtils.dip2px(80), UIUtils.dip2px(150)).centerCrop().into(bivPic);
                } else {
                    Glide.with(mContext).load(message.getContent()).
                            error(R.mipmap.default_img_failed).override(UIUtils.dip2px(80), UIUtils.dip2px(150)).centerCrop().into(bivPic);
                }
                break;
            case XmppMessage.TYPE_VIDEO:
                FileMessage fileMessage = (FileMessage) messageContent;
                ImageView ivPic = helper.getView(R.id.ivSticker);
                bivPic = helper.getView(R.id.bivPic);
                if (!message.isMySend()) {
                    if (!message.isRead()) {
                        helper.setViewVisibility(R.id.unread_img_view, View.VISIBLE);
                    } else {
                        helper.setViewVisibility(R.id.unread_img_view, View.GONE);
                    }
                }

                if (fileMessage != null) {
                    if (MediaFileUtils.isImageFileType(fileMessage.getName())) {
                        Glide.with(mContext).load(fileMessage.getLocalPath() == null ? fileMessage.getMediaUrl() :
                                fileMessage.getLocalPath()).placeholder(R.mipmap.default_img).error(R.mipmap.default_img_failed).centerCrop().into(ivPic);
                    } else if (MediaFileUtils.isVideoFileType(fileMessage.getName())) {
                        if (fileMessage.getLocalPath() != null && new File(fileMessage.getLocalPath().getPath()).exists()) {
                            VideoThumbLoader.getInstance().showThumb(fileMessage.getLocalPath().getPath(), bivPic, 200, 200);
                        } else {
                            bivPic.setImageResource(R.mipmap.img_video_default);
                        }
                    }
                } else {
                    // 是否要去下载  /storage/emulated/0/Android/data/com.lsjr.zizi/files/Movies/44a6fc0e686d4140995863b1740b7d1e.mp4
                    boolean downLoad = true;
                    if (AppCache.getInstance().videoExists(AppCache.getInstance().mVoicesDir,FileUtils.getFileName(content))) {
                        downLoad = false;
                        L_.e("文件已存在");
                    }
                    if (downLoad) {
                        L_.e("下载视频" + content);
                        HttpUtils.getInstance().downloadFile(content, new DownloadSubscriber(UIUtils.getContext(),
                                AppCache.getInstance().mVideosDir , FileUtils.getFileName(content)) {
                            @Override
                            public void onComplete(String path) {
                                L_.e("文件保存路径：" + path);
                                Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MINI_KIND);
                                bivPic.setImageBitmap(bitmap);
                            }

                            @Override
                            public void update(long bytesRead, long contentLength, boolean done) {
                                int progress = (int) (bytesRead * 100 / contentLength);
                                L_.e(progress + "% ");
                            }

                            @Override
                            protected void onXError(String exception) {
                                L_.e(exception + "% ");
                            }
                        });
                    } else {
                        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(AppCache.getInstance().voicePath(FileUtils.getFileName(content)), MediaStore.Video.Thumbnails.MINI_KIND);
                        bivPic.setImageBitmap(bitmap);
                    }
                }
                break;
            case XmppMessage.TYPE_VOICE:
                VoiceMessage voiceMessage = (VoiceMessage) messageContent;
                if (!message.isMySend()) {
                    L_.e("当前的消息是否已经阅读：" + message.isRead());
                    if (!message.isRead()) {
                        helper.setViewVisibility(R.id.unread_img_view, View.VISIBLE);
                    } else {
                        helper.setViewVisibility(R.id.unread_img_view, View.GONE);
                    }
                }
                if (voiceMessage != null) {
                    int increment = (UIUtils.WHD()[0] / 2 / AppConst.DEFAULT_MAX_AUDIO_RECORD_TIME_SECOND * voiceMessage.getDuration());
                    RelativeLayout rlAudio = helper.setText(R.id.tvDuration, voiceMessage.getDuration() + "''").getView(R.id.rlAudio);
                    ViewGroup.LayoutParams params = rlAudio.getLayoutParams();
                    params.width = UIUtils.dip2px(65) + UIUtils.dip2px(increment);
                    rlAudio.setLayoutParams(params);
                } else {
                    //int increment = DisplayUtil.getVoiceViewWidth(mContext, message.getTimeLen());
                    int increment = (UIUtils.WHD()[0] / 2 / AppConst.DEFAULT_MAX_AUDIO_RECORD_TIME_SECOND * message.getTimeLen());
                    RelativeLayout rlAudio = helper.setText(R.id.tvDuration, message.getTimeLen() + "s" + "''").getView(R.id.rlAudio);
                    ViewGroup.LayoutParams params = rlAudio.getLayoutParams();
                    params.width = UIUtils.dip2px(65) + UIUtils.dip2px(increment);
                    rlAudio.setLayoutParams(params);
                }
                break;
            case XmppMessage.TYPE_LOCATION:
                LocationMessage locationMessage = (LocationMessage) messageContent;
                if (!message.isMySend()) {
                    if (!message.isRead()) {
                        helper.setViewVisibility(R.id.unread_img_view, View.VISIBLE);
                    } else {
                        helper.setViewVisibility(R.id.unread_img_view, View.GONE);
                    }
                }

                if (locationMessage != null) {
                    ImageView ivLocation = helper.getView(R.id.ivLocation);
                    Glide.with(mContext).load(locationMessage.getImgUri()).placeholder(R.mipmap.default_location).centerCrop().into(ivLocation);
                }
                helper.setText(R.id.tvTitle, content);
                break;
            case XmppMessage.TYPE_GIF:
                //holder.chat_gif_view = (GifImageView) convertView.findViewById(R.id.chat_to_gif_view);
                String gifName = message.getContent();
                int resId = SmileyParser.Gifs.textMapId(gifName);
                if (resId != -1) {
                    int margin = DisplayUtil.dip2px(mContext, 80);
                    GifView gifView = helper.getView(R.id.chat_to_gif_view);
                    ViewGroup.LayoutParams layoutParams = gifView.getLayoutParams();
                    layoutParams.width = margin;
                    layoutParams.height = margin;
                    gifView.setMovieResource(resId);
                }
                break;
            case XmppMessage.TYPE_CARD:
                if (!message.isMySend()) {
                    if (!message.isRead()) {
                        helper.setViewVisibility(R.id.unread_img_view, View.VISIBLE);
                    } else {
                        helper.setViewVisibility(R.id.unread_img_view, View.GONE);
                    }
                }

                if (!TextUtils.isEmpty(message.getContent())) {
                    //  helper.getView(R.id.chat_to_head).setVisibility(View.VISIBLE);
                    L_.e(content + "昵称:" + message.getFromUserName());
                    helper.setText(R.id.person_name, "昵称:" + message.getFromUserName());
                    L_.e(content + "性别");
                    if (content.equals("0")) {
                        helper.setText(R.id.person_sex, "性别:女");
                    } else {
                        helper.setText(R.id.person_sex, "性别:男");
                    }
                    if (!message.isMySend()) {
                        helper.getView(R.id.card_progress).setVisibility(GONE);
                        if (!message.isRead()) {
                            helper.getView(R.id.unread_img_view).setVisibility(View.VISIBLE);
                        } else {
                            helper.getView(R.id.unread_img_view).setVisibility(GONE);
                        }
                    }
                }
                break;
        }
        //根据发送还是接受状态改变显示
        setStatus(messageType, helper, message, position);
    }

    private void setStatus(int messageType, LQRViewHolderForRecyclerView helper, ChatMessage message, int position) {
        // 如果消息是从我发出的，就会有个发送的状态
        // 我的消息
        switch (messageType) {
            case XmppMessage.TYPE_TEXT:
            case XmppMessage.TYPE_VOICE:
            case XmppMessage.TYPE_LOCATION:
                //只需要设置自己发送的状态
                isSend = mData.get(position).getFromUserId().compareToIgnoreCase(mLoginUserId) == 0;
                if (isSend) {
                    switch (message.getMessageState()) {
                        case ChatMessageListener.MESSAGE_SEND_ING:
                            helper.setViewVisibility(R.id.pbSending, View.VISIBLE).setViewVisibility(R.id.llError, GONE);
                            break;
                        case ChatMessageListener.MESSAGE_SEND_SUCCESS:
                            helper.setViewVisibility(R.id.pbSending, GONE).setViewVisibility(R.id.llError, GONE);
                            break;
                        case ChatMessageListener.MESSAGE_SEND_FAILED:
                            helper.setViewVisibility(R.id.pbSending, GONE).setViewVisibility(R.id.llError, View.VISIBLE);
                            break;
                    }
                } else {
                    helper.setViewVisibility(R.id.pbSending, GONE).setViewVisibility(R.id.llError, GONE);
                }
                break;
            case XmppMessage.TYPE_IMAGE:
                BubbleImageView bivPic = helper.getView(R.id.bivPic);
                isSend = mData.get(position).getFromUserId().compareToIgnoreCase(mLoginUserId) == 0;
                if (isSend) {
                    switch (message.getMessageState()) {
                        case ChatMessageListener.MESSAGE_SEND_ING:
                            bivPic.setProgressVisible(true);
                            bivPic.showShadow(true);
                            helper.setViewVisibility(R.id.llError, GONE);
                            break;
                        case ChatMessageListener.MESSAGE_SEND_SUCCESS:
                            bivPic.setProgressVisible(false);
                            bivPic.showShadow(false);
                            helper.setViewVisibility(R.id.llError, GONE);
                            break;
                        case ChatMessageListener.MESSAGE_SEND_FAILED:
                            bivPic.setProgressVisible(false);
                            bivPic.showShadow(false);
                            helper.setViewVisibility(R.id.llError, View.VISIBLE);
                            break;
                    }
                } else {
                    message.setMessageState(ChatMessageListener.MESSAGE_SEND_SUCCESS);
                    bivPic.setProgressVisible(false);
                    bivPic.showShadow(false);
                    helper.setViewVisibility(R.id.llError, GONE);
                }
                break;
            case XmppMessage.TYPE_VIDEO:

                break;
        }

    }


    private void setAdapterListener() {
        setOnItemClickListener((helper, parent, itemView, position) -> {
            ChatMessage message = ChatMsg.getInStance().getmChatMessages().get(position);
            //根据消息类型设置消息显示内容
            int messageType = message.getType();
            String content = message.getContent();
            MessageContent messageContent = message.getMessageContent();
            L_.e("得到数据类型" + messageType + "--------->设置View:" + content);
            upMesageReadStaus(helper,message);
            switch (messageType) {
                case XmppMessage.TYPE_IMAGE:
                    ImageMessage imageMessage = (ImageMessage) messageContent;
                    if (messageContent != null) {
//                        Intent intent = new Intent(mContext, ShowBigImageActivity.class);
//                        intent.putExtra("url", imageMessage.getLocalUri() == null ? imageMessage.getRemoteUri().toString() : imageMessage.getLocalUri().toString());
//                        mContext.jumpToActivity(intent);
                    }
                    break;
                case XmppMessage.TYPE_VIDEO:
                    // 是否要去下载  /storage/emulated/0/Android/data/com.lsjr.zizi/files/Movies/44a6fc0e686d4140995863b1740b7d1e.mp4
                    FileMessage fileMessage = (FileMessage) messageContent;
                    helper.getView(R.id.bivPic).setOnClickListener(v -> {
                        // String filePath = message.getFilePath();
                        File filep = new File(AppCache.getInstance().mVideosDir + "/" + FileUtils.getFileName(content));
                        if (filep.exists()) {
                            L_.e("播放视频");
                            FileOpenUtils.openFile(UIUtils.getContext(), filep.getAbsolutePath());
                        }
                    });
                    if (fileMessage != null) {
//                        if (MediaFileUtils.isVideoFileType(fileMessage.getName())) {
//
//                        }

                    }
                    break;
                case XmppMessage.TYPE_VOICE:
                    final ImageView ivAudio = helper.getView(R.id.ivAudio);
                    if (messageContent != null) {
                        VoiceMessage voiceMessage = (VoiceMessage) messageContent;
                        playVoice(voiceMessage, ivAudio);
                    } else {
                        boolean downLoad = true;
                        if (AppCache.getInstance().voiceExists(AppCache.getInstance().mVoicesDir,FileUtils.getFileName(content))) {
                            downLoad = false;
                            L_.e("文件已存在");
                            VoiceMessage voiceMessage=VoiceMessage.obtain(Uri.parse(AppCache.getInstance().voicePath(FileUtils.getFileName(content))),message.getTimeLen());
                            playVoice(voiceMessage, ivAudio);
                        }
                        if (downLoad) {
                            L_.e("下载录音" + content);
                            HttpUtils.getInstance().downloadFile(content, new DownloadSubscriber(UIUtils.getContext(),
                                    AppCache.getInstance().mVoicesDir, FileUtils.getFileName(content)) {
                                @Override
                                public void onComplete(String path) {
                                    L_.e("文件保存路径：" + path);
                                    VoiceMessage voiceMessage = VoiceMessage.obtain(Uri.parse(path), message.getTimeLen());
                                    playVoice(voiceMessage, ivAudio);
                                }

                                @Override
                                public void update(long bytesRead, long contentLength, boolean done) {
                                    int progress = (int) (bytesRead * 100 / contentLength);
                                    L_.e(progress + "% ");
                                }

                                @Override
                                protected void onXError(String exception) {
                                    L_.e(exception + "% ");
                                }
                            });

                        }
                    }

                    break;
                case XmppMessage.TYPE_LOCATION:
                    LocationMessage locationMessage = (LocationMessage) messageContent;
                    if (locationMessage != null) {

                    }
                    break;
                case XmppMessage.TYPE_GIF:

                    break;
                case XmppMessage.TYPE_CARD:
                    helper.getView(R.id.chat_warp_view).setOnClickListener(v ->{
                        L_.e("点击名片");
                        upMesageReadStaus(helper,message);
                    });
                    break;
            }
        });
    }

    public void upMesageReadStaus(LQRViewHolder helper, ChatMessage message){
        //改变阅读状态
        ImageView readStausView = helper.getView(R.id.unread_img_view);
        L_.e("将消息标记为已读：" + message.isRead());
        if (!message.isMySend() && !message.isRead()) {
            message.setRead(true);
            L_.e("将消息标记为已读：" + message.isRead());
            ChatMessageDao.getInstance().updateMessageReadState(mLoginUserId, mToUserId,
                    message.get_id());
            if (readStausView != null) {
                readStausView.setVisibility(View.GONE);
            }
        }
    }


    private void playVoice(VoiceMessage voiceMessage, ImageView ivAudio) {
        AudioPlayManager.getInstance().startPlay(UIUtils.getContext(), voiceMessage.getUri(), new IAudioPlayListener() {
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
    }

    @Override
    public int getItemViewType(int position) {
        int messageType = mData.get(position).getType();
        L_.e("当前的数据类型L------" + messageType);
        if (messageType == XmppMessage.TYPE_TIP) {
            return VIEW_SYSTEM;// 消息提示
        }
        // 我的消息
        isSend = mData.get(position).getFromUserId().compareToIgnoreCase(mLoginUserId) == 0;
        switch (messageType) {
            case XmppMessage.TYPE_TEXT:
                return isSend ? SEND_TEXT : RECEIVE_TEXT;
            case XmppMessage.TYPE_IMAGE:
                return isSend ? SEND_IMAGE : RECEIVE_IMAGE;
            case XmppMessage.TYPE_VIDEO:
                return isSend ? SEND_VIDEO : RECEIVE_VIDEO;
            case XmppMessage.TYPE_VOICE:
                return isSend ? SEND_VOICE : RECEIVE_VOICE;
            case XmppMessage.TYPE_LOCATION:
                return isSend ? SEND_LOCATION : RECEIVE_LOCATION;
            case XmppMessage.TYPE_GIF:
                return isSend ? SEND_GIF : RECEIVE_GIF;
            case XmppMessage.TYPE_CARD:
                return isSend ? SEND_CARD : RECEIVE_CARD;
            case XmppMessage.TYPE_FILE:
                return isSend ? SEND_FILE : RECEIVE_FILE;
        }
        return UNDEFINE_MSG;
    }

}