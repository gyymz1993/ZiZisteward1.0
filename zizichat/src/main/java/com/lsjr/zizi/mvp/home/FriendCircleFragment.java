package com.lsjr.zizi.mvp.home;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.lsjr.bean.ArrayResult;
import com.lsjr.callback.ChatArrayCallBack;
import com.lsjr.zizi.AppConfig;
import com.lsjr.zizi.R;
import com.lsjr.zizi.base.MvpFragment;
import com.lsjr.zizi.http.HttpUtils;
import com.lsjr.zizi.chat.ConfigApplication;
import com.lsjr.zizi.chat.bean.PublicMessage;
import com.lsjr.zizi.chat.bean.ResultCode;
import com.lsjr.zizi.chat.dao.CircleMessageDao;
import com.lsjr.zizi.chat.helper.FileDataHelper;
import com.lsjr.zizi.mvp.circledemo.adapter.CircleAdapter;
import com.lsjr.zizi.mvp.circledemo.mvp.presenter.CirclePresenter;
import com.lsjr.zizi.mvp.circledemo.utils.CommonUtils;
import com.lsjr.zizi.mvp.circledemo.widgets.DivItemDecoration;
import com.lsjr.zizi.mvp.home.cicle.ACicleAdapter;
import com.lsjr.zizi.mvp.home.cicle.HeadCellAdapter;
import com.lsjr.zizi.mvp.home.cicle.ImageCellAdapter;
import com.lsjr.zizi.mvp.home.cicle.URLCellAdapter;
import com.lsjr.zizi.mvp.home.cicle.VideoCellAdapter;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.ymz.baselibrary.mvp.BasePresenter;
import com.ymz.baselibrary.utils.L_;
import com.ymz.baselibrary.utils.T_;
import com.ymz.baselibrary.utils.UIUtils;
import com.ys.uilibrary.base.RVBaseAdapter;
import com.ys.uilibrary.base.RVBaseCell;
import com.ys.uilibrary.base.RVBaseViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/8/15 16:40
 */

public class FriendCircleFragment extends MvpFragment {
    @BindView(R.id.recyclerView)
    SuperRecyclerView recyclerView;
    @BindView(R.id.circleEt)
    EditText editText;
    @BindView(R.id.sendIv)
    ImageView sendIv;
    @BindView(R.id.editTextBodyLl)
    LinearLayout edittextbody;
    @BindView(R.id.bodyLayout)
    RelativeLayout bodyLayout;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;

    private String mLoginUserId;// 当前登陆用户的UserId
    private String mLoginNickName;// 当前登陆用户的昵称
    private List<PublicMessage> mMessages = new ArrayList<>();

    /* 当前选择的是哪个用户的个人空间,仅用于查看个人空间的情况下 */
    private String mNickName;
    private int mType;
    /* mPageIndex仅用于商务圈情况下 */
    private int mPageIndex = 0;
    private CirclePresenter presenter;
    private CircleAdapter circleAdapter;
    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_circle;
    }


    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        L_.e("mLoginUserId afterCreate -->");
        mLoginUserId = ConfigApplication.instance().mLoginUser.getUserId();
        mLoginNickName = ConfigApplication.instance().mLoginUser.getNickName();
        L_.e("mLoginUserId  -->"+mLoginUserId+"    ---->"+mLoginNickName);
        if (TextUtils.isEmpty(mLoginUserId)) {// 容错
            return;
        }
        L_.e("mLoginUserId  -  isMyBusiness  ---->"+isMyBusiness());
        if (!isMyBusiness()) {// 如果查看的是个人空间的话，那么mUserId必须要有意义
            if (TextUtils.isEmpty(mLoginUserId)) {// 没有带userId参数，那么默认看的就是自己的空间
                mNickName = mLoginNickName;
            }
        }
        //实现自动下拉刷新功能
        recyclerView.getSwipeToRefresh().post(new Runnable(){
            @Override
            public void run() {
                recyclerView.setRefreshing(true);//执行下拉刷新的动画
                refreshListener.onRefresh();//执行数据加载操作
            }
        });

        requestMyBusiness();
    }

    public void editTextBodyVisible(int visibility) {
        edittextbody.setVisibility(visibility);
        if(View.VISIBLE==visibility){
            editText.requestFocus();
            //弹出键盘
            CommonUtils.showSoftInput(editText.getContext(),  editText);
        }else if(View.GONE==visibility){
            //隐藏键盘
            CommonUtils.hideSoftInput( editText.getContext(),  editText);
        }

    }


    @Override
    protected void initData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DivItemDecoration(2, true));
        recyclerView.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (edittextbody.getVisibility() == View.VISIBLE) {
                    editTextBodyVisible(View.GONE);
                    return true;
                }
                return false;
            }
        });

        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //presenter.loadData(TYPE_PULLREFRESH);
                        requestMyBusiness();
                    }
                }, 500);
            }
        };
        recyclerView.setRefreshListener(refreshListener);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    Glide.with(getActivity()).resumeRequests();
                }else{
                    Glide.with(getActivity()).pauseRequests();
                }

            }
        });

        sendIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


    RVBaseAdapter rvBaseAdapter = new RVBaseAdapter() {
        @Override
        protected void onViewHolderBound(RVBaseViewHolder holder, int position) {
        }
    };

    private void requestMyBusiness() {
        mPageIndex = 0;
        List<String> msgIds = CircleMessageDao.getInstance().getCircleMessageIds(mLoginUserId, mPageIndex, AppConfig.PAGE_SIZE);
        for (String msg:msgIds){
            L_.e(msg);
        }
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("access_token", ConfigApplication.instance().mAccessToken);
        params.put("ids", JSON.toJSONString(msgIds));
        HttpUtils.getInstance().postServiceData(AppConfig.MSG_GETS, params, new ChatArrayCallBack<PublicMessage>(PublicMessage.class) {

            @Override
            protected void onXError(String exception) {
                T_.showToastWhendebug(exception);
                recyclerView.setRefreshing(false);
            }

            @Override
            protected void onSuccess(ArrayResult<PublicMessage> result) {
                L_.e(result.getData().toString());
                mMessages = result.getData();
                firstNotifyAdapter();
            }
        });

    }


    List<PublicMessage.Body> bodyList=new ArrayList<>();
    public void firstNotifyAdapter(){
        /*自定义RecycleView中多类型数据*/
        List<RVBaseCell> cells = new ArrayList<>();
        final HeadCellAdapter headCellAdapter = new HeadCellAdapter(null);
        cells.add(headCellAdapter);

        for (int i=0;i<mMessages.size();i++){
            PublicMessage publicMessage = mMessages.get(i);
            PublicMessage.Body body = publicMessage.getBody();
            bodyList.add(body);
            ACicleAdapter aCicleAdapter=null;
            switch (body.getType()){
                case PublicMessage.TYPE_TEXT:
                    aCicleAdapter = new URLCellAdapter(publicMessage);
                    cells.add(aCicleAdapter);
                    break;
                case PublicMessage.TYPE_VIDEO:
                    aCicleAdapter = new VideoCellAdapter(publicMessage);

                    cells.add(aCicleAdapter);
                    break;
                case PublicMessage.TYPE_IMG:
                    aCicleAdapter = new ImageCellAdapter(publicMessage);
                    cells.add(aCicleAdapter);
                    break;
            }
            if (aCicleAdapter!=null)
            aCicleAdapter.setShowEditTextListener(new ACicleAdapter.ShowEditTextListener() {
                @Override
                public void updateEditTextBodyVisible(int visible) {
                    editTextBodyVisible(View.VISIBLE);
                }
            });
        }
        rvBaseAdapter.setData(cells);
        recyclerView.setAdapter(rvBaseAdapter);
    }


    private void readFromLocal() {
        ChatArrayCallBack chatArrayCallBack = new ChatArrayCallBack<PublicMessage>(PublicMessage.class) {
            @Override
            protected void onXError(String exception) {
            }

            @Override
            protected void onSuccess(ArrayResult<PublicMessage> result) {
                if (result != null && result.getData() != null) {
                    mMessages.clear();
                    mMessages.addAll(result.getData());
                   // mAdapter.notifyDataSetInvalidated();
                }
                requestData();
            }
        };
        FileDataHelper.readArrayData(UIUtils.getContext(), mLoginUserId, FileDataHelper.FILE_BUSINESS_CIRCLE,chatArrayCallBack,PublicMessage.class);

    }

    private void requestSpace() {
        String messageId = null;
        if (mMessages.size() > 0) {
            messageId = mMessages.get(mMessages.size() - 1).getMessageId();
        }
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("access_token", ConfigApplication.instance().mAccessToken);
        params.put("userId", mLoginUserId);
        params.put("flag", PublicMessage.FLAG_NORMAL + "");
        if (!TextUtils.isEmpty(messageId)) {
            params.put("messageId", messageId);
        }
        params.put("pageSize", String.valueOf(AppConfig.PAGE_SIZE));
        HttpUtils.getInstance().postServiceData(AppConfig.MSG_USER_LIST, params, new ChatArrayCallBack<PublicMessage>(PublicMessage.class) {

            @Override
            protected void onXError(String exception) {
                T_.showToastReal(exception);
                //mPullToRefreshListView.onRefreshComplete();
            }

            @Override
            protected void onSuccess(ArrayResult<PublicMessage> result) {
                L_.e(result.getData().toString());
                boolean success = ResultCode.defaultParser( result, true);
                if (success) {
                    List<PublicMessage> datas = result.getData();
                    if (datas != null && datas.size() > 0) {// 没有更多数据
                        mMessages.addAll(datas);
                    }
                }
            }
        });
    }

    /********** 公共消息的数据请求部分 *********/

    /**
     * 请求公共消息
     * 是下拉刷新，还是上拉加载
     */
    private void requestData() {
        if (isMyBusiness()) {
            requestMyBusiness();
        } else {
            requestSpace();
        }
    }

    /**
     * 是否是商务圈类型
     *
     * @return
     */
    private boolean isMyBusiness() {
       // return mType == AppConfig.CIRCLE_TYPE_MY_BUSINESS;
        return false;
    }

}
