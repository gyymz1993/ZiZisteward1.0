package com.lsjr.zizi.mvp.chat;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.lsjr.bean.ObjectResult;
import com.lsjr.bean.Result;
import com.lsjr.callback.ChatObjectCallBack;
import com.lsjr.zizi.AppConfig;
import com.lsjr.zizi.R;
import com.lsjr.zizi.base.MvpActivity;
import com.lsjr.zizi.http.HttpUtils;
import com.lsjr.zizi.mvp.chat.bean.LoginRegisterResult;
import com.lsjr.zizi.mvp.chat.helper.LoginHelper;
import com.lsjr.zizi.util.DeviceInfoUtil;
import com.lsjr.zizi.util.Md5Util;
import com.ymz.baselibrary.BaseApplication;
import com.ymz.baselibrary.mvp.BasePresenter;
import com.ymz.baselibrary.utils.L_;

import java.util.HashMap;


/**
 * 登陆界面
 * 
 * @author Dean Tao
 * @version 1.0
 */
public class LoginActivity extends MvpActivity implements View.OnClickListener {
	private EditText mPhoneNumberEdit;
	private EditText mPasswordEdit;

	@Override
	protected BasePresenter createPresenter() {
		return null;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_login;
	}

	@Override
	protected void initTitle() {
		setTitleText("用户登录");
	}

	@Override
	protected void afterCreate(Bundle savedInstanceState) {
		mPhoneNumberEdit = (EditText) findViewById(R.id.phone_numer_edit);
		mPasswordEdit = (EditText) findViewById(R.id.password_edit);
		//忘记密码按钮点击事件
		findViewById(R.id.forget_password_btn).setOnClickListener(this);
		//注册账号
		findViewById(R.id.register_account_btn).setOnClickListener(this);
		//登陆账号
		findViewById(R.id.login_btn).setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_account_btn:// 注册
			//startActivity(new Intent(mContext, RegisterActivity.class));
			break;
		case R.id.forget_password_btn:// 忘记密码
			// Intent intent = new Intent(mContext, FindPwdActivity.class);
			// intent.putExtra(FindPwdActivity.EXTRA_FROM_LOGIN, this.getClass().getName());
			// startActivity(intent);
			break;
		case R.id.login_btn:// 登陆
			login();
			break;
		}
	}

	private void login() {
		final String phoneNumber = mPhoneNumberEdit.getText().toString().trim();
		String password = mPasswordEdit.getText().toString().trim();
		if (TextUtils.isEmpty(phoneNumber)) {
			return;
		}
		if (TextUtils.isEmpty(password)) {
			return;
		}
		// 加密之后的密码
		final String digestPwd = Md5Util.toMD5(password);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("telephone", Md5Util.toMD5(phoneNumber));// 账号登陆的时候需要MD5加密，服务器需求
		params.put("password", digestPwd);
		// 附加信息
		params.put("model", DeviceInfoUtil.getModel());
		params.put("osVersion", DeviceInfoUtil.getOsVersion());
		params.put("serial", DeviceInfoUtil.getDeviceId(BaseApplication.getApplication()));
		// 地址信息
		//double latitude = MyApplication.getInstance().getBdLocationHelper().getLatitude();
		//double longitude = MyApplication.getInstance().getBdLocationHelper().getLongitude();
		params.put("latitude", String.valueOf(100));
		params.put("longitude", String.valueOf(100));
//		if (latitude != 0)
//			params.put("latitude", String.valueOf(latitude));
//		if (longitude != 0)
//			params.put("longitude", String.valueOf(longitude));
		//mConfig.USER_LOGIN
		L_.e(AppConfig.USER_LOGIN);
		HttpUtils.getInstance().postServiceData(AppConfig.USER_LOGIN, params, new ChatObjectCallBack<LoginRegisterResult>(LoginRegisterResult.class) {

			@Override
			protected void onXError(String exception) {

			}

			@Override
			protected void onSuccess(ObjectResult<LoginRegisterResult> result) {
				L_.e(result.getData().toString());
				boolean success = false;
				if (result.getResultCode() == Result.CODE_SUCCESS) {
					success = LoginHelper.setLoginUser(BaseApplication.getApplication(), phoneNumber, digestPwd, result);// 设置登陆用户信息
				}
				if (success) {// 登陆成功
					ConfigApplication.instance().setmAccessToken(result.getData().getAccess_token());
					openActivity(DataDownloadActivity.class);
				} else {// 登录失败
					//String message = TextUtils.isEmpty(result.getResultMsg()) ? getString(R.string.login_failed) : result.getResultMsg();
					//ToastUtil.showToast(mContext, message);
				}
			}
		});
	}

}
