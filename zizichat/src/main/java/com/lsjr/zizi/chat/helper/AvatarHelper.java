package com.lsjr.zizi.chat.helper;

import android.text.TextUtils;
import android.widget.ImageView;

import com.lsjr.zizi.AppConfig;
import com.lsjr.zizi.loader.ImageLoader;
import com.ymz.baselibrary.utils.L_;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户头像的上传和获取
 *
 *
 */
public class AvatarHelper {

	private Map<String, Long> mCheckTimeMaps;
	private AvatarHelper() {
		mCheckTimeMaps = new HashMap<>();
	}
	public static AvatarHelper INSTANCE;
	public static AvatarHelper getInstance() {
		if (INSTANCE == null) {
			synchronized (AvatarHelper.class) {
				if (INSTANCE == null) {
					INSTANCE = new AvatarHelper();
				}
			}
		}
		return INSTANCE;
	}



	public void displayAvatar(String userId, final ImageView imageView, final boolean isThumb) {
		final String url = getAvatarUrl(userId, isThumb);
		//http://192.168.100.10:8092/avatar/t/11/11.jpg
		//http://192.168.100.10/avatar/t/11/11.jpg
		L_.e(url);
		if (TextUtils.isEmpty(url)) {
			return;
		}
		Long lastCheckTime = mCheckTimeMaps.get(url);
		if (lastCheckTime == null || System.currentTimeMillis() - lastCheckTime > 5 * 60 * 1000) {// 至少间隔5分钟检测一下
			ImageLoader.getInstance().display(url, imageView, isThumb);
		} else {
			ImageLoader.getInstance().display(url, imageView, isThumb);
		}
	}



	public static String getAvatarUrl(String userId, boolean isThumb) {
		if (TextUtils.isEmpty(userId)) {
			return null;
		}
		int userIdInt = -1;
		try {
			userIdInt = Integer.parseInt(userId);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if (userIdInt == -1 || userIdInt == 0) {
			return null;
		}

		int dirName = userIdInt % 10000;
		String url;
		if (isThumb) {
			url = AppConfig.AVATAR_THUMB_PREFIX + "/" + dirName + "/" + userId + ".jpg";
		} else {
			url = AppConfig.AVATAR_ORIGINAL_PREFIX + "/" + dirName + "/" + userId + ".jpg";
		}
		return url;
	}


}
