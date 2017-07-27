package com.ymz.baselibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.ymz.baselibrary.BaseApplication;
import com.ymz.baselibrary.R;


public class UIUtils {
	
	public static Context getContext() {
		return BaseApplication.getApplication();
	}

	public static Thread getMainThread() {
		return BaseApplication.getMainThread();
	}

	public static long getMainThreadId() {
		return BaseApplication.getMainThreadId();
	}



	/**
	 * 设置视图宽高(含weight属性时无效)
	 *
	 * @param view
	 * @param W
	 * @param H
	 */
	public static void setViewWH(View view, float W, float H) {
		if (view == null)
			return;
		ViewGroup.LayoutParams params = view.getLayoutParams();
		if (params == null)
			return;
		if (W > 0)
			params.width = (int) W;
		if (H > 0)
			params.height = (int) H;
		view.setLayoutParams(params);
	}

	/**
	 * 获取宽高密度信息0
	 *
	 * @return [0]宽 [1]高 [2]密度
	 */
	public static int[] WHD() {
		DisplayMetrics outMetrics = new DisplayMetrics();
		WindowManager mm = (WindowManager) getContext()
			.getSystemService(Context.WINDOW_SERVICE);
		mm.getDefaultDisplay().getMetrics(outMetrics);
		return new int[]
			{ outMetrics.widthPixels, outMetrics.heightPixels,
				(int) outMetrics.density };
	}


	/**
	 * 	在不加载图片情况下获取图片大小 适合网络图片
	 */

	public static int[] getBitmapWH(String path)
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
		/**
		 * 最关键在此，把options.inJustDecodeBounds = true;
		 * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
		 */
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
		/**
		 *options.outHeight为原始图片的高
		 */
		//L_.e("options:"+bitmap.getWidth()+"options.outHeight :"+bitmap.getHeight());
		L_.e("options:"+options.outWidth+"options.outHeight :"+options.outHeight);
		return new int[]{options.outWidth,options.outHeight};
	}

	public void satusBarColor(Activity activity,int color){
		SystemBarHelper.tintStatusBar(activity, color);
	}

	/** dip转换px */
	public static int dip2px(int dip) {
		final float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}

	/** pxz转换dip */
	public static int px2dip(int px) {
		final float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	/**将sp值转换为px值，保证文字大小不变**/
	public static int sp2px(float spValue) {
		final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	public static int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
			getContext().getResources().getDisplayMetrics());
	}


	/** 获取主线程的handler */
	public static Handler getHandler() {
		Log.e("BaseApplication",BaseApplication.class.getName());
		return BaseApplication.getMainThreadHandler();
	}


	/** 延时在主线程执行runnable */
	public static boolean postDelayed(Runnable runnable, long delayMillis) {
		return getHandler().postDelayed(runnable, delayMillis);
	}

	/** 在主线程执行runnable */
	public static boolean post(Runnable runnable) {
		return getHandler().post(runnable);
	}

	/** 从主线程looper里面移除runnable */
	public static void removeCallbacks(Runnable runnable) {
		getHandler().removeCallbacks(runnable);
	}

	public static View inflate(int resId){
		return LayoutInflater.from(getContext()).inflate(resId,null);
	}

	/** 获取资源 */
	public static Resources getResources() {
		return getContext().getResources();
	}

	/** 获取文字 */
	public static String getString(int resId) {
		return getResources().getString(resId);
	}

	/** 获取文字数组 */
	public static String[] getStringArray(int resId) {
		return getResources().getStringArray(resId);
	}

	/** 获取dimen */
	public static int getDimens(int resId) {
		return getResources().getDimensionPixelSize(resId);
	}

	/** 获取drawable */
	public static Drawable getDrawble(@DrawableRes int id){
		return ContextCompat.getDrawable(getContext(),id);
	}

	/** 获取颜色 */
	public static int getColor(@ColorRes int id){
		return  ContextCompat.getColor(getContext(),id);
	}

	public static int getDimen(int dimen) {
		return (int) getResources().getDimension(dimen);
	}
	public static <T extends View> T findViewById(View v, int id) {
		return (T) v.findViewById(id);
	}


	/** 获取颜色选择 */
	public static ColorStateList getColorStateList(int resId) {
		return getResources().getColorStateList(resId);
	}
	//判断当前的线程是不是在主线程 
	public static boolean isRunInMainThread() {
		return android.os.Process.myTid() == getMainThreadId();
	}

	public static View runInWindows(Window window){
		View decorView = window.getDecorView();
		return decorView;
	}
    
	public static void runInMainThread(Runnable runnable) {
		if (isRunInMainThread()) {
			runnable.run();
		} else {
			post(runnable);
		}
	}

	public static void setNavigationBarColor(Activity activity, int colorId) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = activity.getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setNavigationBarColor(colorId);
		}

		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
			activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			SystemBarTintManager manager = new SystemBarTintManager(activity);
			manager.setNavigationBarTintEnabled(true);
			manager.setNavigationBarTintColor(colorId);
		}
	}


	public static void setStatusBarColor(Activity activity, int colorId) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			SystemBarTintManager manager = new SystemBarTintManager(activity);
			manager.setStatusBarTintEnabled(true);
			manager.setStatusBarTintColor(colorId);
		}
	}



	@RequiresApi(api = Build.VERSION_CODES.M)
	public static int getColor(int resId, Resources.Theme theme) {
		return getResources().getColor(resId, theme);
	}

	public static int getColor(String color) {
		return Color.parseColor(color);
	}

	/**
	 * 获取Drawable
	 * @param resTd Drawable资源id
	 * @return Drawable
	 */
	public static Drawable getDrawable(int resTd) {
		return getResources().getDrawable(resTd);
	}

}


