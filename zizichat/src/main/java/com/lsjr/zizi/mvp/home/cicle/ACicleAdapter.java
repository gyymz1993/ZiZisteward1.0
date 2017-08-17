package com.lsjr.zizi.mvp.home.cicle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lsjr.zizi.R;
import com.lsjr.zizi.chat.ConfigApplication;
import com.lsjr.zizi.chat.bean.PublicMessage;
import com.lsjr.zizi.chat.helper.AvatarHelper;
import com.lsjr.zizi.mvp.circledemo.bean.ActionItem;
import com.lsjr.zizi.mvp.circledemo.widgets.CommentListView;
import com.lsjr.zizi.mvp.circledemo.widgets.ExpandTextView;
import com.lsjr.zizi.mvp.circledemo.widgets.PraiseListView;
import com.lsjr.zizi.mvp.circledemo.widgets.SnsPopupWindow;
import com.lsjr.zizi.util.TimeUtils;
import com.ymz.baselibrary.utils.UIUtils;
import com.ys.uilibrary.base.RVBaseCell;
import com.ys.uilibrary.base.RVBaseViewHolder;


/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/8/16 11:55
 */

public abstract class ACicleAdapter extends RVBaseCell<PublicMessage> {
    /** 消息类型 */
    static final int TYPE_URL = 1;// 文字类型
    static final int TYPE_IMG = 2;// 图片类型
    static final int TYPE_VOICE = 3;// 语音
    public static final int TYPE_VIDEO = 4;// 视频
    //public final static int TYPE_URL = 5;

    public int viewType;

    ImageView headIv;
    TextView nameTv;
    TextView urlTipTv;
    /** 动态的内容 */
     ExpandTextView contentTv;
     TextView timeTv;
     TextView deleteBtn;
     ImageView snsBtn;
    /** 点赞列表*/
     PraiseListView praiseListView;

     LinearLayout digCommentBody;
     View digLine;

    /** 评论列表 */
     CommentListView commentList;
    // ===========================
    public SnsPopupWindow snsPopupWindow;

    private PublicMessage publicMessage;

    public ACicleAdapter(PublicMessage publicMessage) {
        super(publicMessage);
        this.publicMessage=publicMessage;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_circle_item, null);
        return new RVBaseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {
        ViewStub viewStub = (ViewStub) holder.getView(R.id.viewStub);
        initSubView(viewType, viewStub);
        headIv = (ImageView)  holder.getView(R.id.headIv);
        nameTv = (TextView)  holder.getView(R.id.nameTv);
        digLine =  holder.getView(R.id.lin_dig);
        contentTv = (ExpandTextView)  holder.getView(R.id.contentTv);
        urlTipTv = (TextView)  holder.getView(R.id.urlTipTv);
        timeTv = (TextView)  holder.getView(R.id.timeTv);
        deleteBtn = (TextView)  holder.getView(R.id.deleteBtn);
        snsBtn = (ImageView)  holder.getView(R.id.snsBtn);
        praiseListView = (PraiseListView)  holder.getView(R.id.praiseListView);
        digCommentBody = (LinearLayout)  holder.getView(R.id.digCommentBody);
        commentList = (CommentListView) holder.getView(R.id.commentList);


        //分享萧条
        urlTipTv.setVisibility(View.GONE);
        //评论区
        digCommentBody.setVisibility(View.GONE);

        final PublicMessage message = publicMessage;
        PublicMessage.Body body = message.getBody();
        // 设备头像
        AvatarHelper.getInstance().displayAvatar(message.getUserId(), headIv, true);
		/* 设置昵称 */
        nameTv.setText(message.getNickName());
        // 设置body_tv
        if (TextUtils.isEmpty(body.getText())) {
            contentTv.setVisibility(View.GONE);
        } else {
            contentTv.setVisibility(View.VISIBLE);
            contentTv.setText(body.getText());
        }
        // 设置发布时间
        timeTv.setText(TimeUtils.getFriendlyTimeDesc(UIUtils.getContext(), (int) message.getTime()));
        if(ConfigApplication.instance().mLoginUser.getUserId().equals(message.getUserId())){
            deleteBtn.setVisibility(View.VISIBLE);
            nameTv.setText("自己");
        }else{
            deleteBtn.setVisibility(View.GONE);
        }

        /*点击点赞或者评论*/
         snsPopupWindow = new SnsPopupWindow(UIUtils.getContext());
        snsPopupWindow.update();
        snsPopupWindow.setmItemClickListener(new PopupItemClickListener());
        snsBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //弹出popupwindow
                snsPopupWindow.showPopupWindow(view);
            }
        });

    }


    private class PopupItemClickListener implements SnsPopupWindow.OnItemClickListener{

        private long mLasttime = 0;

        public PopupItemClickListener(){
        }
        @Override
        public void onItemClick(ActionItem actionitem, int position) {
            switch (position) {
                case 0://点赞、取消点赞
                    if(System.currentTimeMillis()-mLasttime<700)//防止快速点击操作
                        return;
                    mLasttime = System.currentTimeMillis();
                    break;
                case 1://发布评论
                    if (showEditTextListener!=null){
                        showEditTextListener.updateEditTextBodyVisible(View.VISIBLE);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void setShowEditTextListener(ShowEditTextListener showEditTextListener) {
        this.showEditTextListener = showEditTextListener;
    }

    ShowEditTextListener showEditTextListener;
    public interface ShowEditTextListener{
        void updateEditTextBodyVisible(int visible);
    }

    @Override
    public int getItemType() {
        return publicMessage.getBody().getType();
    }

    public abstract void initSubView(int viewType, ViewStub viewStub);

}
