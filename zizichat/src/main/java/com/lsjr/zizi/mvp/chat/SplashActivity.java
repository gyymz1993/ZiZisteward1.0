package com.lsjr.zizi.mvp.chat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;


/**
 * 启动页
 * 
 * @author Dean Tao
 * @version 1.0
 */
public class SplashActivity extends Activity {

	private long mStartTimeMs;// 记录进入该界面时间，保证至少在该界面停留3秒
	private boolean mConfigReady = false;// 配置获取成功

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}


	private void ready() {
		if (!mConfigReady) {
			return;
		}
		long currentTimeMs = System.currentTimeMillis();
		int useTime = (int) (currentTimeMs - mStartTimeMs);
		int delayTime = useTime > 2000 ? 0 : 2000 - useTime;
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				jump();
			}
		}, delayTime);
	}

	@SuppressLint("NewApi")
	private void jump() {
		if (isDestroyed()) {
			return;
		}

	}

}
