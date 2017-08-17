package com.lsjr.zizi.mvp.chat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;

import com.lsjr.bean.ArrayResult;
import com.lsjr.bean.ObjectResult;
import com.lsjr.callback.ChatArrayCallBack;
import com.lsjr.callback.ChatObjectCallBack;
import com.lsjr.zizi.AppConfig;
import com.lsjr.zizi.R;
import com.lsjr.zizi.base.MvpActivity;
import com.lsjr.zizi.http.HttpUtils;
import com.lsjr.zizi.mvp.home.HomeActivity;
import com.lsjr.zizi.mvp.chat.bean.AttentionUser;
import com.lsjr.zizi.mvp.chat.bean.MucRoom;
import com.lsjr.zizi.mvp.chat.bean.ResultCode;
import com.lsjr.zizi.mvp.chat.dao.CircleMessageDao;
import com.lsjr.zizi.mvp.chat.dao.FriendDao;
import com.lsjr.zizi.mvp.chat.dao.UserDao;
import com.lsjr.zizi.mvp.chat.db.CircleMessage;
import com.lsjr.zizi.mvp.chat.db.MyPhoto;
import com.lsjr.zizi.mvp.chat.db.User;
import com.lsjr.zizi.mvp.chat.helper.UserSp;
import com.ymz.baselibrary.mvp.BasePresenter;
import com.ymz.baselibrary.utils.L_;
import com.ymz.baselibrary.view.PermissionListener;

import java.util.HashMap;
import java.util.List;

/**
 *  数据更新界面 ,下载的数据： 1、我的商务圈最新数据 2、我的通讯录 3、更新用户基本资料 4、我的相册下载
 */
@SuppressLint("Registered")
public class DataDownloadActivity extends MvpActivity {
	private final int STATUS_NO_RESULT = 0;// 请求中，尚未返回
	private final int STATUS_FAILED = 1;// 已经返回，失败了
	private final int STATUS_SUCCESS = 2;// 已经返回，成功了
	private int circle_msg_download_status = STATUS_NO_RESULT;// 商务圈ids下载
	private int address_user_download_status = STATUS_NO_RESULT;// 通讯录下载
	private int user_info_download_status = STATUS_NO_RESULT;// 个人基本资料下载
	private int user_photo_download_status = STATUS_SUCCESS;// 我的相册下载
	private int room_download_status = STATUS_NO_RESULT;// 我的房间下载
	private String mLoginUserId;
	private Handler mHandler;

	@Override
	protected BasePresenter createPresenter() {
		return null;
	}


	@Override
	protected int getLayoutId() {
		return R.layout.activity_data_download;
	}

	@Override
	protected void afterCreate(Bundle savedInstanceState) {
		UserSp.getInstance().setUpdate(false);// 进入下载资料界面，就将该值赋值false
		mLoginUserId = ConfigApplication.instance().mLoginUser.getUserId();
		mHandler = new Handler();

		requestPermissions(new String[]{ Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,
				Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO }, new PermissionListener() {
			@Override
			public void onGranted() {

			}

			@Override
			public void onDenied(List<String> deniedPermissions) {
			}
		});
		startDownload();
	}


	private void startDownload() {
	   //  showProgressDialogWithText("数据下载中...");

		if (circle_msg_download_status != STATUS_SUCCESS) {// 没有成功，就下载
			circle_msg_download_status = STATUS_NO_RESULT;// 初始化下载状态
			downloadCircleMessage();
		}

		if (address_user_download_status != STATUS_SUCCESS) {// 没有成功，就下载
			address_user_download_status = STATUS_NO_RESULT;// 初始化下载状态
			downloadAddressBook();
		}

		if (user_info_download_status != STATUS_SUCCESS) {// 没有成功，就下载
			user_info_download_status = STATUS_NO_RESULT;// 初始化下载状态
			downloadUserInfo();
		}

		if (user_photo_download_status != STATUS_SUCCESS) {// 没有成功，就下载
			user_photo_download_status = STATUS_NO_RESULT;// 初始化下载状态
			downloadUserPhoto();
		}
		if (room_download_status != STATUS_SUCCESS) {// 没有成功，就下载
			room_download_status = STATUS_NO_RESULT;// 初始化下载状态
			downloadRoom();
		}

	}



	private void endDownload() {
		// 只有有一个下载没返回，那么就继续等待
		if (circle_msg_download_status == STATUS_NO_RESULT || address_user_download_status == STATUS_NO_RESULT
				|| user_info_download_status == STATUS_NO_RESULT || user_photo_download_status == STATUS_NO_RESULT
				|| room_download_status == STATUS_NO_RESULT) {
			return;
		}
		// 只要有一个下载失败，那么显示更新失败。就继续下载
		if (circle_msg_download_status == STATUS_FAILED || address_user_download_status == STATUS_FAILED
				|| user_info_download_status == STATUS_FAILED || user_photo_download_status == STATUS_FAILED
				|| room_download_status == STATUS_FAILED) {
			dismissProgressDialog();
			showErrorView();
		} else {// 所有数据加载完毕,跳转回用户操作界面
			dismissProgressDialog();
			showContentView();
			//openActivity(HomeActivity.class);
			jumpToActivityAndClearTask(HomeActivity.class);
		}
	}

	/**
	 * 下载商务圈消息
	 */
	private void downloadCircleMessage() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("access_token", ConfigApplication.instance().getmAccessToken());
		L_.e(ConfigApplication.instance().getmAccessToken());
		HttpUtils.getInstance().postServiceData(AppConfig.DOWNLOAD_CIRCLE_MESSAGE, params, new ChatArrayCallBack<CircleMessage>(CircleMessage.class) {
			@Override
			protected void onXError(String exception) {
				L_.e("商务圈下载失败" + exception);
				circle_msg_download_status = STATUS_FAILED;// 失败
				endDownload();
			}

			@Override
			protected void onSuccess(ArrayResult<CircleMessage> result) {
				L_.e("商务圈下载成功");
				boolean success = ResultCode.defaultParser(result, true);
				if (success) {
					CircleMessageDao.getInstance().addMessages(mHandler, mLoginUserId, result.getData(),
							() -> {
                                circle_msg_download_status = STATUS_SUCCESS;// 成功
                                endDownload();
                            });
				} else {
					circle_msg_download_status = STATUS_FAILED;// 失败
					endDownload();
				}
				//endDownload();
			}
		});
	}


	/**
	 * 下载我的关注，包括我的好友
	 */
	private void downloadAddressBook() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("access_token", ConfigApplication.instance().getmAccessToken());
		HttpUtils.getInstance().postServiceData(AppConfig.FRIENDS_ATTENTION_LIST, params, new ChatArrayCallBack<AttentionUser>(AttentionUser.class) {

			@Override
			protected void onXError(String exception) {
				L_.e("我的关注下载失败"+exception);
				address_user_download_status = STATUS_FAILED;// 失败
				endDownload();
			}

			@Override
			protected void onSuccess(ArrayResult<AttentionUser> result) {
				L_.e("我的关注下载成功"+result.getData().toString());
				boolean success = ResultCode.defaultParser(result, true);
				if (success) {

					FriendDao.getInstance().addAttentionUsers(mHandler, mLoginUserId, result.getData(),
							() -> {
                                address_user_download_status = STATUS_SUCCESS;// 成功
                                endDownload();
                            });
				} else {
					address_user_download_status = STATUS_FAILED;// 失败
					endDownload();
				}
			}
		});
	}

	/**
	 * 下载个人基本资料
	 */
	private void downloadUserInfo() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("access_token", ConfigApplication.instance().getmAccessToken());
		HttpUtils.getInstance().postServiceData(AppConfig.USER_GET_URL, params, new ChatObjectCallBack<User>(User.class) {
			@Override
			protected void onXError(String exception) {
				L_.e("个人基本资料下载失败"+exception);
				user_info_download_status = STATUS_FAILED;// 失败
				endDownload();
			}
			@Override
			protected void onSuccess(ObjectResult<User> result) {
				L_.e("个人基本资料下载成功");
				boolean updateSuccess;
				user_info_download_status = STATUS_SUCCESS;// 成功
				if (ResultCode.defaultParser(result, true)) {
					// 设置登陆用户信息

					User user = result.getData();
					updateSuccess = UserDao.getInstance().updateByUser(user);
					// 设置登陆用户信息
					if (updateSuccess) {// 如果成功，那么就将User的详情赋值给全局变量
						ConfigApplication.instance().mLoginUser =  result.getData();
					}
					if (updateSuccess) {
						user_info_download_status = STATUS_SUCCESS;// 成功
					} else {
						user_info_download_status = STATUS_FAILED;// 失败
					}
				}
				endDownload();
			}
		});

	}

	/**
	 * 下载我的相册
	 */
	private void downloadUserPhoto() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("access_token", ConfigApplication.instance().getmAccessToken());
		HttpUtils.getInstance().postServiceData(AppConfig.USER_PHOTO_LIST, params, new ChatArrayCallBack<MyPhoto>(MyPhoto.class) {
			@Override
			protected void onXError(String exception) {
				user_photo_download_status = STATUS_FAILED;// 失败
				endDownload();
			}

			@Override
			protected void onSuccess(ArrayResult<MyPhoto> result) {
				L_.e("我的相册下载成功");
				boolean success = ResultCode.defaultParser(result, true);
				if (success) {
					user_photo_download_status = STATUS_SUCCESS;// 成功
				} else {
					user_photo_download_status = STATUS_SUCCESS;// 失败
				}
				endDownload();
			}
		});
	}

	/**
	 * 下载我的房间
	 */
	private void downloadRoom() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("access_token", ConfigApplication.instance().getmAccessToken());
		params.put("type", "0");
		params.put("pageIndex", "0");
		params.put("pageSize", "200");// 给一个尽量大的值
		HttpUtils.getInstance().postServiceData(AppConfig.ROOM_LIST_HIS, params, new ChatArrayCallBack<MucRoom>(MucRoom.class) {

			@Override
			protected void onXError(String exception) {
				L_.e("我的房间下载失败"+exception);
				room_download_status = STATUS_FAILED;// 失败
				endDownload();
			}

			@Override
			protected void onSuccess(ArrayResult<MucRoom> result) {
				L_.e("我的房间下载成功");
				boolean success = ResultCode.defaultParser(result, true);
				if (success) {
					FriendDao.getInstance().addRooms(mHandler, mLoginUserId, result.getData(),
							() -> {
                                room_download_status = STATUS_SUCCESS;// 成功
                                endDownload();
                            });
				} else {
					room_download_status = STATUS_FAILED;// 失败
					endDownload();
				}
			}
		});

	}
}
