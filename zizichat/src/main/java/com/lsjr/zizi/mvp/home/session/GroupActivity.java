package com.lsjr.zizi.mvp.home.session;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.adapter.ABaseRefreshAdapter;
import com.andview.adapter.BaseRecyclerHolder;
import com.lsjr.bean.ArrayResult;
import com.lsjr.callback.ChatArrayCallBack;
import com.lsjr.zizi.AppConfig;
import com.lsjr.zizi.R;
import com.lsjr.zizi.base.MvpActivity;
import com.lsjr.zizi.http.HttpUtils;
import com.lsjr.zizi.chat.ConfigApplication;
import com.lsjr.zizi.chat.bean.MucRoom;
import com.lsjr.zizi.chat.bean.ResultCode;
import com.lsjr.zizi.chat.broad.MucgroupUpdateUtil;
import com.lsjr.zizi.util.TimeUtils;
import com.ymz.baselibrary.mvp.BasePresenter;
import com.ymz.baselibrary.utils.L_;
import com.ymz.baselibrary.utils.T_;
import com.ymz.baselibrary.utils.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/8/14 17:04
 */

@SuppressLint("Registered")
public class GroupActivity extends MvpActivity {
    /* 分页的Size */
    public static final int PAGE_SIZE = 50;
    private List<MucRoom> mMucRooms;
    private MucRoomAdapter mAdapter;
    private int mPageIndex = 0;
    private boolean mNeedUpdate = true;
    @BindView(R.id.id_contacts)
    RecyclerView idRvGroup;
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
        showProgressDialogWithText("加载中");
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
       registerReceiver(mUpdateReceiver,
                MucgroupUpdateUtil.getUpdateActionFilter());
       initRvView();
       requestData();
    }

    private void initRvView() {
        setTitleText("群聊");
        setTopLeftButton(R.drawable.ic_back).
                setTitleTextColor(UIUtils.getColor(R.color.white)).
                setBackgroundColor(UIUtils.getColor(R.color.colorPrimary));
        setTopRightButton(R.drawable.actionbar_add_icon, v ->
                T_.showToastReal("新建群聊"));
        mMucRooms = new ArrayList<>();
        mAdapter = new MucRoomAdapter(this,mMucRooms,R.layout.item_group);
        idRvGroup.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });
        idRvGroup.setAdapter(mAdapter);
    }

    private void requestData() {
        mPageIndex = 0;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("pageIndex", String.valueOf(mPageIndex));
        params.put("pageSize", String.valueOf(PAGE_SIZE));
        params.put("access_token", ConfigApplication.instance().mAccessToken);
        HttpUtils.getInstance().postServiceData(AppConfig.ROOM_LIST, params, new ChatArrayCallBack<MucRoom>(MucRoom.class) {
            @Override
            protected void onXError(String exception) {
                dismissProgressDialog();
            }

            @Override
            protected void onSuccess(ArrayResult result) {
                dismissProgressDialog();
                boolean success = ResultCode.defaultParser(result, true);
                if (success) {
                    L_.e(result.getData().toString());
                    mMucRooms=result.getData();
                    mPageIndex++;
                    mAdapter.notifyDataSetChanged(mMucRooms);
                }
            }

        });
    }


    private BroadcastReceiver mUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MucgroupUpdateUtil.ACTION_UPDATE)) {
//                if (isResumed()) {
//                    requestData();
//                } else {
//                    mNeedUpdate = true;
//                }
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mUpdateReceiver);
    }

    public class MucRoomAdapter extends ABaseRefreshAdapter<MucRoom> {

         MucRoomAdapter(Context context, List<MucRoom> datas, int itemLayoutId) {
            super(context, datas, itemLayoutId);
        }

        @Override
        protected void convert(BaseRecyclerHolder holder, MucRoom room, int position) {
            ImageView avatar_img = holder.getView(R.id.avatar_img);
            TextView nick_name_tv = holder.getView(R.id.nick_name_tv);
            TextView content_tv = holder.getView(R.id.content_tv);
            TextView time_tv = holder.getView(R.id.time_tv);
            L_.e(room.getName());
            nick_name_tv.setText(room.getName());
            time_tv.setText(TimeUtils.getFriendlyTimeDesc(UIUtils.getContext(), (int) room.getCreateTime()));
            content_tv.setText(room.getDesc());
        }


    }

}
