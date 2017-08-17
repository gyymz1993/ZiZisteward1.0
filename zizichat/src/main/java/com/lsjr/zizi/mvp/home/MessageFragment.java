package com.lsjr.zizi.mvp.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lsjr.zizi.R;
import com.lsjr.zizi.base.MvpFragment;
import com.lsjr.zizi.chat.helper.AvatarHelper;
import com.lsjr.zizi.mvp.home.session.ChatActivity;
import com.lsjr.zizi.chat.ConfigApplication;
import com.lsjr.zizi.chat.bean.BaseSortModel;
import com.lsjr.zizi.chat.broad.MsgBroadcast;
import com.lsjr.zizi.chat.broad.MucgroupUpdateUtil;
import com.lsjr.zizi.chat.dao.FriendDao;
import com.lsjr.zizi.chat.db.Friend;
import com.lsjr.zizi.chat.utils.HtmlUtils;
import com.lsjr.zizi.chat.utils.StringUtils;
import com.lsjr.zizi.chat.utils.TimeUtils;
import com.lsjr.zizi.chat.xmpp.XmppMessage;
import com.lsjr.zizi.util.PinyinUtils;
import com.lsjr.zizi.view.ClearEditText;
import com.ymz.baselibrary.mvp.BasePresenter;
import com.ymz.baselibrary.utils.L_;
import com.ymz.baselibrary.utils.T_;
import com.ys.uilibrary.base.BaseRecyclerAdapter;
import com.ys.uilibrary.base.BaseRecyclerHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/8/5 16:14
 */

public class MessageFragment extends MvpFragment {

  //  @BindView(R.id.search_edit)
    ClearEditText mClearEditText;
    @BindView(R.id.id_ms_rv)
    RecyclerView idMsRv;
    private boolean mNeedUpdate = true;
    private List<BaseSortModel<Friend>> mFriendList;// 筛选后的朋友数据
    private List<BaseSortModel<Friend>> mOriginalFriendList;// 原始的朋友数据，也就是从数据库查询出来，没有筛选的
    private Handler mHandler = new Handler();
    private NearlyMessageAdapter mAdapter;

    public MessageFragment() {
        mOriginalFriendList = new ArrayList<>();
        mFriendList = new ArrayList<>();
    }


    @Override
    protected void lazyLoad() {
        super.lazyLoad();
        loadData();
    }



    @Override
    protected void initView() {

        mClearEditText = (ClearEditText) mView.findViewById(R.id.search_edit);
        mClearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String filter = mClearEditText.getText().toString().trim().toUpperCase();
                mFriendList.clear();
                if (mOriginalFriendList != null && mOriginalFriendList.size() > 0) {
                    for (int i = 0; i < mOriginalFriendList.size(); i++) {
                        BaseSortModel<Friend> mode = mOriginalFriendList.get(i);
                        // 获取筛选的数据
                        if (TextUtils.isEmpty(filter) || mode.getSimpleSpell().startsWith(filter) || mode.getWholeSpell().startsWith(filter)
                                || mode.getBean().getShowName().startsWith(filter)) {
                            mFriendList.add(mode);
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mAdapter = new NearlyMessageAdapter(getActivity(),mFriendList,R.layout.item_message);
        idMsRv.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });
        mAdapter.setOnItemSelectListener(new OnItemSelectListener<BaseSortModel< Friend >>() {
            @Override
            public void onItemSelect(BaseSortModel < Friend >  friendBaseSortModel) {
                Friend friend=friendBaseSortModel.getBean();
                /*将消息设置为0*/
                if (friend.getUnReadNum() > 0) {
                    MsgBroadcast.broadcastMsgNumUpdate(getActivity(), false, friend.getUnReadNum());
                    friend.setUnReadNum(0);
                    mAdapter.notifyDataSetChanged();
                }

                if (friend.getRoomFlag() == 0) {
                    if (friend.getUserId().equals(Friend.ID_NEW_FRIEND_MESSAGE)) {// 新朋友消息
                        T_.showToastReal("新朋友列表");
                        //startActivity(new Intent(getActivity(), NewFriendActivity.class));
                    } else {
                        Intent intent = new Intent(getActivity(), ChatActivity.class);
                        intent.putExtra(ChatActivity.FRIEND,friend);
                        startActivity(intent);
                    }
                } else {
                    T_.showToastReal("群聊");
//                    Intent intent = new Intent(getActivity(), MucChatActivity.class);
//                    intent.putExtra(AppConstant.EXTRA_USER_ID, friend.getUserId());
//                    intent.putExtra(AppConstant.EXTRA_NICK_NAME, friend.getNickName());
//                    intent.putExtra(AppConstant.EXTRA_IS_GROUP_CHAT, true);
//                    startActivity(intent);
                }




            }
        });

        idMsRv.setAdapter(mAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mNeedUpdate) {
            mNeedUpdate = false;
            loadData();
        }
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        getActivity().registerReceiver(mUpdateReceiver, new IntentFilter(MsgBroadcast.ACTION_MSG_UI_UPDATE));
    }

    private BroadcastReceiver mUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MsgBroadcast.ACTION_MSG_UI_UPDATE)) {
                if (isResumed()) {
                    loadData();
                } else {
                    mNeedUpdate = true;
                }
            }
        }
    };

    /**
     * 请求加载新的筛选条件的数据
     * <p>
     * 是下拉刷新，还是上拉加载
     */

    private void loadData() {
        new Thread(() -> {
            String mLoginUserId = ConfigApplication.instance().mLoginUser.getUserId();
            long startTime = System.currentTimeMillis();
            final List<Friend> friends = FriendDao.getInstance().getNearlyFriendMsg(mLoginUserId);
            long delayTime = 200 - (startTime - System.currentTimeMillis());// 保证至少200ms的刷新过程
            if (delayTime < 0) {
                delayTime = 0;
            }
            mHandler.postDelayed(() -> {
                mOriginalFriendList.clear();
                mFriendList.clear();
                String filter = mClearEditText.getText().toString().trim().toUpperCase();
                if (friends != null && friends.size() > 0) {
                    for (int i = 0; i < friends.size(); i++) {
                        BaseSortModel<Friend> mode = new BaseSortModel<>();
                        mode.setBean(friends.get(i));
                        setSortCondition(mode);
                        mOriginalFriendList.add(mode);
                        // 获取筛选的数据
                        if (TextUtils.isEmpty(filter) || mode.getSimpleSpell().startsWith(filter) || mode.getWholeSpell().startsWith(filter)
                                || mode.getBean().getShowName().startsWith(filter)) {
                            mFriendList.add(mode);
                        }
                    }
                }
                for (int i = 0; i < mFriendList.size(); i++) {
                    L_.e(mFriendList.get(i).getFirstLetter()+":"+mFriendList.get(i).getBean().toString());
                }
                mAdapter.notifyDataSetChanged();
            }, delayTime);
        }).start();

    }


    private void setSortCondition(BaseSortModel<Friend> mode) {
        Friend friend = mode.getBean();
        if (friend == null) {
            return;
        }
        String name = friend.getShowName();
        String wholeSpell = PinyinUtils.getPingYin(name);
        if (!TextUtils.isEmpty(wholeSpell)) {
            String firstLetter = Character.toString(wholeSpell.charAt(0));
            mode.setWholeSpell(wholeSpell);
            mode.setFirstLetter(firstLetter);
            mode.setSimpleSpell(PinyinUtils.converterToFirstSpell(name));
        } else {// 如果全拼为空，理论上是一种错误情况，因为这代表着昵称为空
            mode.setWholeSpell("#");
            mode.setFirstLetter("#");
            mode.setSimpleSpell("#");
        }
    }


    public void sendBroadcast() {
        Intent mIntent = new Intent(MucgroupUpdateUtil.ACTION_UPDATE);
        getActivity().sendBroadcast(mIntent);
    }

    public class NearlyMessageAdapter extends BaseRecyclerAdapter<BaseSortModel<Friend>> {
        public NearlyMessageAdapter(Context context, List<BaseSortModel<Friend>> datas, int itemLayoutId) {
            super(context, datas, itemLayoutId);
        }

        @Override
        protected void convert(BaseRecyclerHolder holder, BaseSortModel<Friend> item, int position) {

            holder.itemView.setOnClickListener(v -> {
                if (onItemSelectListener!=null){
                    onItemSelectListener.onItemSelect(item);
                }
            });
            ImageView avatar_img = holder.getView(R.id.avatar_img);
            TextView num_tv = holder.getView(R.id.num_tv);
            TextView nick_name_tv = holder.getView(R.id.nick_name_tv);
            TextView content_tv = holder.getView( R.id.content_tv);
            TextView time_tv = holder.getView(R.id.time_tv);

            final Friend friend = mFriendList.get(position).getBean();

            if (friend.getRoomFlag() == 0) {// 这是单个人
                if (friend.getUserId().equals(Friend.ID_SYSTEM_MESSAGE)) {// 系统消息的头像
                    avatar_img.setImageResource(R.drawable.im_notice);
                } else if (friend.getUserId().equals(Friend.ID_NEW_FRIEND_MESSAGE)) {// 新朋友的头像
                    avatar_img.setImageResource(R.drawable.im_new_friends);
                } else {// 其他
                    AvatarHelper.getInstance().displayAvatar(friend.getUserId(), avatar_img, true);
                }
            }

            nick_name_tv.setText(friend.getShowName());
            time_tv.setText(TimeUtils.getFriendlyTimeDesc(getActivity(), friend.getTimeSend()));
            CharSequence content ;
            if (friend.getType() == XmppMessage.TYPE_TEXT) {
                String s = StringUtils.replaceSpecialChar(friend.getContent());
                content = HtmlUtils.transform200SpanString(s.replaceAll("\n", "\r\n"), true);
            } else if (friend.getType()==XmppMessage.TYPE_CARD){
                content = "个人名片";
            }else {
                content = friend.getContent();
            }
            L_.e(content.toString());
            content_tv.setText(content);
            if (friend.getUnReadNum() > 0) {
                String numStr = friend.getUnReadNum() >= 99 ? "99+" : friend.getUnReadNum() + "";
                num_tv.setText(numStr);
                num_tv.setVisibility(View.VISIBLE);
            } else {
                num_tv.setVisibility(View.GONE);
            }

        }

        OnItemSelectListener onItemSelectListener;
        void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
            this.onItemSelectListener = onItemSelectListener;
        }
    }

    public  interface  OnItemSelectListener<T>{
        void onItemSelect(T t);
    }

}
