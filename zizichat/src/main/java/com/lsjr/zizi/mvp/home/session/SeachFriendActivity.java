package com.lsjr.zizi.mvp.home.session;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.adapter.ABaseRefreshAdapter;
import com.andview.adapter.BaseRecyclerHolder;
import com.andview.listener.OnItemClickListener;
import com.lsjr.bean.ArrayResult;
import com.lsjr.callback.ChatArrayCallBack;
import com.lsjr.utils.HttpUtils;
import com.lsjr.zizi.AppConfig;
import com.lsjr.zizi.R;
import com.lsjr.zizi.base.MvpActivity;
import com.lsjr.zizi.mvp.home.ConfigApplication;
import com.lsjr.zizi.chat.bean.ResultCode;
import com.lsjr.zizi.chat.db.User;
import com.lsjr.zizi.loader.AvatarHelper;
import com.ymz.baselibrary.mvp.BasePresenter;
import com.ymz.baselibrary.utils.L_;
import com.ymz.baselibrary.utils.T_;
import com.ymz.baselibrary.utils.UIUtils;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/9/1 16:13
 */

public class SeachFriendActivity extends MvpActivity implements View.OnClickListener {
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.ll_none)
    LinearLayout llNone;

    protected InputMethodManager imm;

    @BindView(R.id.id_fd_list)
    RecyclerView idFdList;

    private String mKeyWord;// 关键字(keyword)
    private int mSex;// 城市Id(cityId)
    private int mMinAge;// 行业Id(industryId)
    private int mMaxAge;// 职能Id(fnId)
    private int mShowTime;// 日期(days)

    private List<User> datas;
    FriendAdapter friendAdapter;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_friend_;
    }


    @Override
    protected void initTitle() {
        super.initTitle();
        setTitleText("搜索好友");
        //setTopLeftButton(R.drawable.ic_back);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent() != null) {
            mKeyWord = getIntent().getStringExtra("key_word");
            mSex = getIntent().getIntExtra("sex", 0);
            mMinAge = getIntent().getIntExtra("min_age", 0);
            mMaxAge = getIntent().getIntExtra("max_age", 0);
            mShowTime = getIntent().getIntExtra("show_time", 0);
        }
        datas=new ArrayList<>();
        super.onCreate(savedInstanceState);


        friendAdapter=new FriendAdapter(this,datas,R.layout.item_friend);
        friendAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseRecyclerHolder baseRecyclerHolder, int position, Object item) {
                Intent intent = new Intent(SeachFriendActivity.this, BasicInfoActivity.class);
                intent.putExtra(AppConfig.EXTRA_USER_ID, datas.get(position).getUserId());
                startActivity(intent);
            }
        });

        idFdList.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });
        idFdList.setAdapter(friendAdapter);
        etSearch.setOnClickListener(this);
        ivClear.setOnClickListener(this);
        llSearch.setOnClickListener(this);
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    llSearch.setVisibility(View.VISIBLE);
                    ivClear.setVisibility(View.VISIBLE);
                } else {
                    ivClear.setVisibility(View.GONE);
                    llSearch.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_search:
                //T_.showToastReal("点击搜索");
                imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
                mKeyWord = etSearch.getText().toString();
                if (TextUtils.isEmpty(mKeyWord)){
                    T_.showToastReal("请输入搜索条件");
                    return;
                }
                Pattern pattern = Pattern.compile("[0-9a-zA-Z\u4E00-\u9FA5]+");
                Matcher matcher = pattern.matcher(mKeyWord);
                if (!matcher.matches() ) {
                    T_.showToastReal("有特殊字符");
                    return;
                }

                if (null != mKeyWord && mKeyWord.length() > 0) {
                    //getData();
                    seachFriend();
                } else {
                    T_.showToastReal("搜索条件不能为空");
                    //  Toast.makeText(SearchAddFriendActivity.this, "搜索内容不能为空...", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.iv_clear:
                etSearch.setText("");
                break;
        }
    }



    public void seachFriend() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("pageIndex", String.valueOf(0));
        params.put("pageSize", String.valueOf(AppConfig.PAGE_SIZE));
        params.put("access_token", ConfigApplication.instance().mAccessToken);
        if (!TextUtils.isEmpty(mKeyWord)) {
            params.put("nickname", mKeyWord);
        }
        if (mSex != 0) {
            params.put("sex", String.valueOf(mSex));
        }
        if (mMinAge != 0) {
            params.put("minAge", String.valueOf(mMinAge));
        }
        if (mMaxAge != 0) {
            params.put("maxAge", String.valueOf(mMaxAge));
        }
        params.put("active", String.valueOf(mShowTime));

        HttpUtils.getInstance().postServiceData(AppConfig.USER_NEAR, params, new ChatArrayCallBack<User>(User.class) {

            @Override
            protected void onXError(String exception) {
                L_.e("搜索失败" + exception);
                idFdList.setVisibility(View.GONE);
                llNone.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onSuccess(ArrayResult<User> result) {
                L_.e("搜索成功");
                boolean success = ResultCode.defaultParser(result, true);
                if (success) {
                    datas = result.getData();
                    if (datas.size() != 0 && datas != null) {
                        L_.e("----------"+datas.size());
                        idFdList.setVisibility(View.VISIBLE);
                        llNone.setVisibility(View.GONE);
                        friendAdapter.notifyDataSetChanged(datas);
                    } else {
                        idFdList.setVisibility(View.GONE);
                        llNone.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }



    public class FriendAdapter extends ABaseRefreshAdapter<User> {
        public FriendAdapter(Context context, List<User> datas, int itemLayoutId) {
            super(context, datas, itemLayoutId);
        }

        @Override
        protected void convert(com.andview.adapter.BaseRecyclerHolder var1, User user, int var3) {
            AutoLinearLayout llNewFriend;
            ImageView  ivNewFriend =  var1.getView(R.id.ivNewFriend);
            TextView tvNewFriendUnread =  var1.getView(R.id.tvNewFriendUnread);
            AvatarHelper.getInstance().displayAvatar(user, ivNewFriend, true);
            tvNewFriendUnread.setText(user.getNickName());
        }
    }

}
