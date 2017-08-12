package com.lsjr.zizisteward.activity.lcq;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lsjr.zizisteward.MyApplication;
import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.bean.BasicParameterBean;
import com.lsjr.zizisteward.bean.NoteLoginBean;
import com.lsjr.zizisteward.http.HttpClientGet;
import com.lsjr.zizisteward.http.MyError;
import com.lsjr.zizisteward.utils.CustomDialogUtils;
import com.lsjr.zizisteward.utils.GsonUtil;
import com.lsjr.zizisteward.utils.PreferencesUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by Ly on 2017/6/1.
 */

public class NoteLoginActivity extends Activity implements View.OnClickListener {
    /*取消按钮*/
    private TextView ly_cancel;
    /**
     * 手机号
     */
    private EditText et_phone;
    /**
     * 短信验证码
     */
    private EditText et_code;
    private TextView tv_code;
    /**
     * 图形验证码
     */
    private EditText et_iv_code;
    /**
     * CheckBox
     */
    private CheckBox cb;
    /**
     * 协议说明
     */
    private TextView tv_explain;
    /**
     * 提交
     */
    private TextView tv_submit;

    private LinearLayout ll_code;

    private LinearLayout ll_iv_code;

    private ImageView iv;

    private View v_phone;

    private View v_code;

    private View v_iv_code;

    private ImageCode code;

    private int _error = 0;

    private String bask;

    private boolean space = false;

    private boolean _old = false;

    private BasicParameterBean bean;

    private TimeCount time;

    private Pattern IS_PHONE = Pattern.compile("^(1(3|4|5|7|8))\\d{9}$");

    /**
     * 判断密码是否含有特殊符号
     */
    public boolean isEmoji(String string) {
        Pattern p = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(string);
        return m.find();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.note_login_activity);
        this.findViewById();
    }

    private void findViewById() {
        this.ly_cancel = (TextView) super.findViewById(R.id.ly_cancel);
        this.v_code = super.findViewById(R.id.v_code);
        this.v_phone = super.findViewById(R.id.v_phone);
        this.cb = (CheckBox) super.findViewById(R.id.cb);
        this.iv = (ImageView) super.findViewById(R.id.iv);
        this.v_iv_code = super.findViewById(R.id.v_iv_code);
        this.et_code = (EditText) super.findViewById(R.id.et_code);
        this.tv_code = (TextView) super.findViewById(R.id.tv_code);
        this.et_phone = (EditText) super.findViewById(R.id.et_phone);
        this.ll_code = (LinearLayout) super.findViewById(R.id.ll_code);
        this.tv_submit = (TextView) super.findViewById(R.id.tv_submit);
        this.et_iv_code = (EditText) super.findViewById(R.id.et_iv_code);
        this.tv_explain = (TextView) super.findViewById(R.id.tv_explain);
        this.ll_iv_code = (LinearLayout) super.findViewById(R.id.ll_iv_code);

        this.tv_code.setOnClickListener(this);
        this.tv_submit.setOnClickListener(this);
        this.tv_explain.setOnClickListener(this);
        this.ly_cancel.setOnClickListener(this);

        this.tv_code.setClickable(false);
        this.tv_submit.setClickable(false);

        this.setIvCodeShow();

        this.setOldTime();

        this.bask = NoteLoginActivity.this.getIntent().getStringExtra("login");

        this.iv.setImageBitmap(code.getInstance().createBitmap());

        this.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv.setImageBitmap(code.getInstance().createBitmap());
            }
        });

        this.et_phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    v_phone.setBackgroundColor(Color.parseColor("#FFDE83"));
                } else {
                    v_phone.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }
        });

        this.et_code.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    v_code.setBackgroundColor(Color.parseColor("#FFDE83"));
                } else {
                    v_code.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }
        });

        this.et_iv_code.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    v_iv_code.setBackgroundColor(Color.parseColor("#FFDE83"));
                } else {
                    v_iv_code.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }
        });

        this.et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 11) {
                    if (!IS_PHONE.matcher(s).matches()) {
                        Toast.makeText(NoteLoginActivity.this, "请输入正确的手机号码", Toast.LENGTH_LONG).show();
                    } else {

                        if (ll_iv_code.getVisibility() == View.VISIBLE) {

                            if (et_iv_code.getText().length() == 4) {
                                Submit();
                            }

                        } else if (ll_code.getVisibility() == View.VISIBLE) {
                            if (!space) {
                                //如果还没有开始计时
                                setCodeAction(true);
                            }

                            if (et_code.getText().toString().length() == 4) {
                                setSubmitAction(true);
                            } else {
                                setSubmitAction(false);
                            }
                        }
                    }

                } else {
                    //如果手机号小于11位
                    if (!space) {
                        setCodeAction(false);
                    }

                    setSubmitAction(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        this.et_code.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 4) {
                    if (et_phone.getText().length() == 11) {
                        if (!IS_PHONE.matcher(et_phone.getText().toString()).matches()) {
                            Toast.makeText(NoteLoginActivity.this, "请输入正确的手机号码", Toast.LENGTH_LONG).show();
                            setSubmitAction(false);
                        } else {
                            setSubmitAction(true);
                        }
                    } else {
                        setSubmitAction(false);
                    }
                } else {
                    setSubmitAction(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        this.et_iv_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 4) {
                    if (et_phone.getText().length() == 11) {
                        if (!IS_PHONE.matcher(et_phone.getText().toString()).matches()) {
                            Toast.makeText(NoteLoginActivity.this, "请输入正确的手机号码", Toast.LENGTH_LONG).show();
                            setSubmitAction(false);
                        } else {
                            setSubmitAction(true);
                            Submit();
                            if (!space) {
                                //如果还没有开始计时
                                setCodeAction(true);
                            }
                        }
                    } else {
                        Toast.makeText(NoteLoginActivity.this, "请输入正确的手机号码", Toast.LENGTH_LONG).show();
                        setSubmitAction(false);
                    }
                } else {
                    setSubmitAction(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setOldTime() {
        //日期
        String old_date = PreferencesUtils.getString(NoteLoginActivity.this, "login_date");
        //秒数
        String old_time = PreferencesUtils.getString(NoteLoginActivity.this, "login_time");

        if (null != old_time && !old_time.equals("0")) {

            String[] o_array = old_date.split(",");

            String o_year = o_array[0];
            String o_month = o_array[1];
            String o_day = o_array[2];
            String o_hour = o_array[3];
            String o_minute = o_array[4];
            String o_second = o_array[5];

            String[] n_array = returnTime().split(",");

            String n_year = n_array[0];
            String n_month = n_array[1];
            String n_day = n_array[2];
            String n_hour = n_array[3];
            String n_minute = n_array[4];
            String n_second = n_array[5];

            int o_ymd = Integer.valueOf(o_year + o_month + o_day);
            int n_ymd = Integer.valueOf(n_year + n_month + n_day);

            if (o_ymd > n_ymd) {
                _old = false;
                time = new TimeCount(60000, 1000);
            } else if (o_ymd == n_ymd) {
                if (Integer.valueOf(n_hour) > Integer.valueOf(o_hour)) {
                    _old = false;
                    time = new TimeCount(60000, 1000);
                } else if (Integer.valueOf(n_hour) == Integer.valueOf(o_hour)) {

                    int n_s = Integer.valueOf(n_minute) * 60 + Integer.valueOf(n_second);
                    int o_s = Integer.valueOf(o_minute) * 60 + Integer.valueOf(o_second);

                    if (n_s > o_s) {

                        if (n_s - o_s >= 60) {
                            _old = false;
                            time = new TimeCount(60000, 1000);
                        } else {

                            if ((n_s - o_s) < Integer.valueOf(old_time)) {
                                _old = true;
                                time = new TimeCount((Integer.valueOf(old_time) - (n_s - o_s)) * 1000, 1000);
                                time.start();
                            } else if ((n_s - o_s) == Integer.valueOf(old_time)) {
                                _old = false;
                                time = new TimeCount(60000, 1000);
                            } else if ((n_s - o_s) > Integer.valueOf(old_time)) {
                                _old = false;
                                time = new TimeCount(60000, 1000);
                            }
                        }

                    } else if (n_s == o_s) {
                        _old = false;
                        time = new TimeCount(60000, 1000);
                    } else if (n_s < o_s) {
                        _old = false;
                        time = new TimeCount(60000, 1000);
                    }

                } else if (Integer.valueOf(n_hour) < Integer.valueOf(o_hour)) {
                    _old = false;
                    time = new TimeCount(60000, 1000);
                }
            } else if (o_ymd < n_ymd) {
                _old = false;
                time = new TimeCount(60000, 1000);
            }

        } else {
            _old = false;
            time = new TimeCount(60000, 1000);
        }
    }

    private void getForLink(final String phone, String noteCode) {
        CustomDialogUtils.startCustomProgressDialog(NoteLoginActivity.this, "请稍候");
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "102");
        map.put("mobile", phone);
        map.put("noteCode", noteCode);
        map.put("device", JPushInterface.getRegistrationID(NoteLoginActivity.this));
        new HttpClientGet(NoteLoginActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {
            @Override
            public void onSuccess(String result) {
                CustomDialogUtils.stopCustomProgressDialog(NoteLoginActivity.this);
                NoteLoginBean nlBean = new Gson().fromJson(result, NoteLoginBean.class);

                NoteLoginBean.RepMap rmBean = nlBean.getRepMap();
                //MyApplication.setUserInfo(rmBean);
                PreferencesUtils.putObject(NoteLoginActivity.this, "userinfo", rmBean);
                PreferencesUtils.putBoolean(NoteLoginActivity.this, "isLogin", true);
                PreferencesUtils.putString(NoteLoginActivity.this, "user_account", phone);
                PreferencesUtils.putString(NoteLoginActivity.this, "user_password", rmBean.getDefaultPwd());

//                LoginInstantMessage(NoteLoginActivity.this, MyApplication.getUserInfo().getName(), rmBean.getDefaultPwd());

                if (null == bask) {
                    finish();
                } else {
                    returnValue();
                }

            }

            @Override
            public void onFailure(MyError myError) {
                CustomDialogUtils.stopCustomProgressDialog(NoteLoginActivity.this);
                iv.setImageBitmap(code.getInstance().createBitmap());

                if (_error >= 2) {
                    ll_code.setVisibility(View.GONE);
                    ll_iv_code.setVisibility(View.VISIBLE);
                    et_iv_code.setFocusable(true);
                    et_iv_code.setFocusableInTouchMode(true);
                    et_iv_code.requestFocus();
                    setSubmitAction(false);
                    et_code.setText("");
                    _error++;
                } else {
                    et_code.setText("");
                    setSubmitAction(false);
                    _error++;
                }

                PreferencesUtils.putString(NoteLoginActivity.this, "login_error", returnDate() + "," + _error);

                super.onFailure(myError);
            }
        });
    }

//   private void LoginInstantMessage(final Context context, final String phone, final String passWord) {
//
//        DemoDBManager.getInstance().closeDB();
//        DemoHelper.getInstance().setCurrentUserName(phone);
//        EMClient.getInstance().login(phone, passWord, new EMCallBack() {
//            @SuppressWarnings({"unused", "static-access"})
//            @Override
//            public void onSuccess() {
//                CustomDialogUtils.stopCustomProgressDialog(NoteLoginActivity.this);
//                EMClient.getInstance().groupManager().loadAllGroups();
//                EMClient.getInstance().chatManager().loadAllConversations();
//                EMClient.getInstance().updateCurrentUserNick(App.currentUserNick.trim());
//                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
//
//                if (null == bask)
//                    finish();
//                else
//                    returnValue();
//            }
//
//            @Override
//            public void onError(int i, String s) {
//                CustomDialogUtils.stopCustomProgressDialog(NoteLoginActivity.this);
//            }
//
//            @Override
//            public void onProgress(int i, String s) {
//            }
//        });
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_submit:

                Submit();

                break;

            case R.id.tv_code:
                //获取验证码
                String phone = et_phone.getText().toString().trim();

                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(NoteLoginActivity.this, "请输入输手机号码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!IS_PHONE.matcher(phone).matches()) {
                    Toast.makeText(NoteLoginActivity.this, "请输入正确的手机号码", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    this.getCode(phone);
                }

                break;

            case R.id.tv_explain:
                //协议
                startActivity(new Intent(NoteLoginActivity.this, XieYiActivity.class));
                break;
            case R.id.ly_cancel:
                finish();
                break;
        }
    }

    private void Submit() {
        if (cb.isChecked()) {
            if (ll_iv_code.getVisibility() == View.VISIBLE) {

                if (TextUtils.isEmpty(et_iv_code.getText().toString())) {
                    Toast.makeText(NoteLoginActivity.this, "请输入图形验证码", Toast.LENGTH_SHORT).show();
                } else {

                    if (et_iv_code.getText().toString().toLowerCase().equals(code.getInstance().getCode())) {
                        ll_iv_code.setVisibility(View.GONE);
                        ll_code.setVisibility(View.VISIBLE);
                        et_code.setFocusable(true);
                        et_code.setFocusableInTouchMode(true);
                        et_code.requestFocus();
                        et_code.setText("");
                        et_iv_code.setText("");
                    } else {
                        et_code.setText("");
                        et_iv_code.setText("");
                        iv.setImageBitmap(code.getInstance().createBitmap());
                        _error++;
                        startActivityForResult(new Intent(NoteLoginActivity.this, NoteLoginPromptBox.class).putExtra("action", "iv_code"), 1);
                    }
                }

            } else {
                //提交
                this.getForLink(et_phone.getText().toString(), et_code.getText().toString());
            }

        } else {
            startActivityForResult(new Intent(NoteLoginActivity.this, NoteLoginPromptBox.class).putExtra("action", "deal"), 2);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 77:
                cb.setChecked(true);
                Submit();
                break;
        }
    }

    /**
     * 获取短信验证码
     */
    private void getCode(final String phone_number) {
        Map<String, String> map = new HashMap<>();
        map.put("OPT", "4");
        map.put("cellPhone", phone_number);
        map.put("state", "3");
        new HttpClientGet(NoteLoginActivity.this, null, map, false, new HttpClientGet.CallBacks<String>() {
            @Override
            public void onSuccess(String result) {
                time.start();
                bean = GsonUtil.getInstance().fromJson(result, BasicParameterBean.class);
                Toast.makeText(NoteLoginActivity.this, "短信验证码已放送至您的手机", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(MyError myError) {
                super.onFailure(myError);
            }
        });
    }

    @SuppressWarnings("unused")
    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            space = true;
            tv_code.setBackgroundColor(Color.parseColor("#DDDDDD"));
            tv_code.setTextColor(Color.parseColor("#FFFFFF"));
            tv_code.setClickable(false);
            tv_code.setText(millisUntilFinished / 1000 + "s");
        }

        @Override
        public void onFinish() {
            space = false;
            if (_old) {
                time = new TimeCount(60000, 1000);
            }
            tv_code.setBackgroundColor(Color.parseColor("#FFDE83"));
            tv_code.setTextColor(Color.parseColor("#212121"));
            tv_code.setText("获取验证码");
            tv_code.setClickable(true);
        }
    }

    @Override
    protected void onDestroy() {
        if (time != null) {
            PreferencesUtils.putString(NoteLoginActivity.this, "login_date", returnTime());

            if (space) {
                PreferencesUtils.putString(NoteLoginActivity.this, "login_time", tv_code.getText().toString().substring(0, tv_code.getText().toString().length() - 1));
            } else {
                PreferencesUtils.putString(NoteLoginActivity.this, "login_time", "0");
            }
            time.cancel();
        }
        super.onDestroy();
    }

    private void setCodeAction(boolean sham) {
        if (sham) {
            tv_code.setBackgroundColor(Color.parseColor("#FFDE83"));
            tv_code.setTextColor(Color.parseColor("#212121"));
            tv_code.setClickable(true);
        } else {
            tv_code.setBackgroundColor(Color.parseColor("#DDDDDD"));
            tv_code.setTextColor(Color.parseColor("#FFFFFF"));
            tv_code.setClickable(false);
        }
    }

    private void setSubmitAction(boolean sham) {
        if (sham) {
            tv_submit.setBackgroundColor(Color.parseColor("#ffde83"));
            tv_submit.setTextColor(Color.parseColor("#212121"));
            tv_submit.setClickable(true);
        } else {
            tv_submit.setBackgroundColor(Color.parseColor("#DDDDDD"));
            tv_submit.setTextColor(Color.parseColor("#FFFFFF"));
            tv_submit.setClickable(false);
        }
    }

    private void returnValue() {
        Intent intent = getIntent();
        if (bask.equals("setting")) {
            setResult(1);
        }
        finish();
    }

    private String returnTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy,MM,dd,HH,mm,ss");
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    private String returnDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    private void setIvCodeShow() {
        String login_error = PreferencesUtils.getString(NoteLoginActivity.this, "login_error");

        if (null != login_error && login_error.length() > 0) {
            String[] l_e = login_error.split(",");
            String l_e_n_time = returnDate();
            String l_e_o_time = l_e[0];
            String error_n = l_e[1];

            if (l_e_o_time.equals(l_e_n_time)) {
                _error = Integer.valueOf(error_n);

                if (_error >= 2) {

                    ll_code.setVisibility(View.GONE);
                    ll_iv_code.setVisibility(View.VISIBLE);
                } else {
                    ll_code.setVisibility(View.VISIBLE);
                    ll_iv_code.setVisibility(View.GONE);
                }
            } else {
                PreferencesUtils.putString(NoteLoginActivity.this, "login_error", returnDate() + "," + "0");
                PreferencesUtils.putString(NoteLoginActivity.this, "login_error", returnDate() + "," + "0");
                _error = 0;
                ll_code.setVisibility(View.VISIBLE);
                ll_iv_code.setVisibility(View.GONE);
            }
        } else {
            PreferencesUtils.putString(NoteLoginActivity.this, "login_error", returnDate() + "," + "0");
            PreferencesUtils.putString(NoteLoginActivity.this, "login_error", returnDate() + "," + "0");
            _error = 0;
            ll_code.setVisibility(View.VISIBLE);
            ll_iv_code.setVisibility(View.GONE);
        }
    }
}
