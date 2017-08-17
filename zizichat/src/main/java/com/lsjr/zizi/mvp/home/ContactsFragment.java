package com.lsjr.zizi.mvp.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.adapter.ABaseRefreshAdapter;
import com.andview.adapter.BaseRecyclerHolder;
import com.lsjr.zizi.AppConfig;
import com.lsjr.zizi.R;
import com.lsjr.zizi.base.MvpFragment;
import com.lsjr.zizi.mvp.chat.ConfigApplication;
import com.lsjr.zizi.mvp.chat.bean.BaseSortModel;
import com.lsjr.zizi.mvp.chat.dao.FriendDao;
import com.lsjr.zizi.mvp.chat.db.Friend;
import com.lsjr.zizi.mvp.chat.helper.AvatarHelper;
import com.lsjr.zizi.mvp.chat.utils.BaseComparator;
import com.lsjr.zizi.mvp.chat.utils.PingYinUtil;
import com.lsjr.zizi.mvp.chat.utils.Pinyin4jUtil;
import com.lsjr.zizi.mvp.home.session.BasicInfoActivity;
import com.lsjr.zizi.mvp.home.session.GroupActivity;
import com.lsjr.zizi.mvp.home.session.NearbyActivity;
import com.ymz.baselibrary.mvp.BasePresenter;
import com.ymz.baselibrary.utils.UIUtils;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;


/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/8/7 13:50
 */

public class ContactsFragment extends MvpFragment {

    private List<BaseSortModel<Friend>> mSortFriends;
    FriendSortAdapter friendSortAdapter;
    private BaseComparator<Friend> mBaseComparator;
    private String mLoginUserId;
    @BindView(R.id.id_contacts)
    RecyclerView idContacts;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contacte;
    }


    @Override
    protected void initView() {
        mLoginUserId = ConfigApplication.instance().mLoginUser.getUserId();
        mSortFriends = new ArrayList<>();
        mBaseComparator = new BaseComparator<>();
        friendSortAdapter=new FriendSortAdapter(getActivity(),mSortFriends,R.layout.item_contacts);
        initRecycleView();
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        loadData();
    }

    public void initRecycleView() {
       //friendSortAdapter.setCustomLoadMoreView(new XRefreshViewFooter(getActivity()));
        //idContacts.setHasFixedSize(true);
        idContacts.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });
        friendSortAdapter.setHeaderView(getHeadView(), idContacts);
        idContacts.setAdapter(friendSortAdapter);

    }


    public void update() {
        loadData();
    }


    private void loadData() {
        new Thread(() -> {
            long startTime = System.currentTimeMillis();
            final List<Friend> friends = FriendDao.getInstance().getAllFriends(mLoginUserId);

            long delayTime = 200 - (startTime - System.currentTimeMillis());// 保证至少200ms的刷新过程
            if (delayTime < 0) {
                delayTime = 0;
            }

            UIUtils.getHandler().postDelayed(() -> {
                mSortFriends.clear();
                if (friends != null && friends.size() > 0) {
                    for (int i = 0; i < friends.size(); i++) {
                        BaseSortModel<Friend> mode = new BaseSortModel<>();
                        mode.setBean(friends.get(i));
                        setSortCondition(mode);
                        mSortFriends.add(mode);
                    }
                    Collections.sort(mSortFriends, mBaseComparator);
                }
                friendSortAdapter.notifyDataSetChanged();
               // friendSortAdapter.setListData(mSortFriends);
            }, delayTime);
        }).start();
    }


    private  void setSortCondition(BaseSortModel<Friend> mode) {
        Friend friend = mode.getBean();
        if (friend == null) {
            return;
        }
        String name = friend.getShowName();
        //String wholeSpell = PingYinUtil.getPingYin(name);
        String wholeSpell= Pinyin4jUtil.converterToSpell(name);
        if (!TextUtils.isEmpty(wholeSpell)) {
            String firstLetter = Character.toString(wholeSpell.charAt(0));
            mode.setWholeSpell(wholeSpell);
            mode.setFirstLetter(firstLetter);
            mode.setSimpleSpell(PingYinUtil.converterToFirstSpell(name));
        } else {// 如果全拼为空，理论上是一种错误情况，因为这代表着昵称为空
            mode.setWholeSpell("#");
            mode.setFirstLetter("#");
            mode.setSimpleSpell("#");
        }
    }



    public View getHeadView() {
       View headView = UIUtils.inflate(R.layout.header_rv_contacts) ;
        AutoLinearLayout llGroup= (AutoLinearLayout) headView.findViewById(R.id.llGroup);
        AutoLinearLayout llNearFriend= (AutoLinearLayout) headView.findViewById(R.id.llNearFriend);

        llGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(GroupActivity.class);
            }
        });
        llNearFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(NearbyActivity.class);
            }
        });
        return headView;
    }

    public class FriendSortAdapter extends ABaseRefreshAdapter<BaseSortModel<Friend>> {

        public FriendSortAdapter(Context context, List<BaseSortModel<Friend>> datas, int itemLayoutId) {
            super(context, datas, itemLayoutId);
        }


        @Override
        protected void convert(BaseRecyclerHolder holder, BaseSortModel<Friend> item, int position) {

            RelativeLayout id_root_ry = holder.getView(R.id.id_root_ry);
            TextView catagoryTitleTv = holder.getView(R.id.catagory_title);
            ImageView avatar_img = holder.getView(R.id.avatar_img);
            TextView nick_name_tv = holder.getView(R.id.nick_name_tv);
            TextView des_tv = holder.getView(R.id.des_tv);

            // 根据position获取分类的首字母的Char ascii值
            int section = getSectionForPosition(position);
            // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
//            if (position == getPositionForSection(section)) {
//                catagoryTitleTv.setVisibility(View.VISIBLE);
//                catagoryTitleTv.setText(mSortFriends.get(position).getFirstLetter());
//            } else {
//                catagoryTitleTv.setVisibility(View.GONE);
//            }

            // 设置头像
            final Friend friend = mSortFriends.get(position).getBean();
            if (friend.getRoomFlag() == 0) {// 这是单个人
                if (friend.getUserId().equals(Friend.ID_SYSTEM_MESSAGE)) {// 系统消息的头像
                    avatar_img.setImageResource(R.drawable.im_notice);
                } else if (friend.getUserId().equals(Friend.ID_NEW_FRIEND_MESSAGE)) {// 新朋友的头像
                    avatar_img.setImageResource(R.drawable.im_new_friends);
                } else {// 其他
                    AvatarHelper.getInstance().displayAvatar(friend.getUserId(), avatar_img, true);
                }
            } else {// 这是1个房间
                if (TextUtils.isEmpty(friend.getRoomCreateUserId())) {
                    avatar_img.setImageResource(R.drawable.avatar_normal);
                } else {
                    AvatarHelper.getInstance().displayAvatar(friend.getRoomCreateUserId(), avatar_img, true);// 目前在备注名放房间的创建者Id
                }
            }

            // 昵称
            String name = friend.getRemarkName();
            if (TextUtils.isEmpty(name)) {
                name = friend.getNickName();
            }
            nick_name_tv.setText(name);
            // 个性签名
            des_tv.setText(friend.getDescription());
            id_root_ry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), BasicInfoActivity.class);
                    intent.putExtra(AppConfig.EXTRA_USER_ID, friend.getUserId());
                    startActivity(intent);
                }
            });

        }

        /**
         * 根据ListView的当前位置获取分类的首字母的Char ascii值
         */
        public int getSectionForPosition(int position) {
            return mSortFriends.get(position).getFirstLetter().charAt(0);
        }

        /**
         * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
         */
        public int getPositionForSection(int section) {
            for (int i = 0; i < getItemCount(); i++) {
                String sortStr = mSortFriends.get(i).getFirstLetter();
                char firstChar = sortStr.toUpperCase().charAt(0);
                if (firstChar == section) {
                    return i;
                }
            }
            return -1;
        }
    }


}
