//package com.lsjr.zizi.mvp.chat.helper;
//
//import android.os.Handler;
//import android.os.Looper;
//import android.text.TextUtils;
//import android.widget.ImageView;
//
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.imageaware.ImageAware;
//
//import java.io.File;
//import java.io.IOException;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * 用户头像的上传和获取
// *
// *
// */
//public class AvatarHelper {
//
//	private Map<String, Long> mCheckTimeMaps;
//	private Handler mHandler;
//
//	private AvatarHelper() {
//		mCheckTimeMaps = new HashMap<String, Long>();
//		mHandler = new Handler(Looper.getMainLooper());
//	}
//
//	public static AvatarHelper INSTANCE;
//
//	public static AvatarHelper getInstance() {
//		if (INSTANCE == null) {
//			synchronized (AvatarHelper.class) {
//				if (INSTANCE == null) {
//					INSTANCE = new AvatarHelper();
//				}
//			}
//		}
//		return INSTANCE;
//	}
//
//	/**
//	 * 当自己上传了新的头像了，立即删除缓存
//	 *
//	 * @param userId
//	 */
//	public void deleteAvatar(String userId) {
//		final String url1 = getAvatarUrl(userId, true);
//		final String url2 = getAvatarUrl(userId, false);
//		if (!TextUtils.isEmpty(url1)) {
//			deleteCache(url1);
//		}
//		if (!TextUtils.isEmpty(url2)) {
//			deleteCache(url2);
//		}
//	}
//
//	/**
//	 * 删除缓存在本地和内存中的图片
//	 *
//	 * @param url
//	 */
//	private void deleteCache(String url) {
//		final File localFile = ImageLoader.getInstance().getDiscCache().get(url);
//		if (localFile != null && localFile.exists()) {
//			localFile.delete();
//		}
//		List<String> keys = MemoryCacheUtil.findCacheKeysForImageUri(url, ImageLoader.getInstance().getMemoryCache());
//		if (keys != null && keys.size() > 0) {
//			for (String key : keys) {
//				ImageLoader.getInstance().getMemoryCache().remove(key);
//			}
//		}
//	}
//
//	public void displayAvatar(String userId, final ImageView imageView, final boolean isThumb) {
//		final String url = getAvatarUrl(userId, isThumb);
//		if (TextUtils.isEmpty(url)) {
//			return;
//		}
//
//		Long lastCheckTime = mCheckTimeMaps.get(url);
//		if (lastCheckTime == null || System.currentTimeMillis() - lastCheckTime > 5 * 60 * 1000) {// 至少间隔5分钟检测一下
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					long lastModifyTime = getLastModify(url);
//					long localLastModified = 0;
//					final File localFile = ImageLoader.getInstance().getDiscCache().get(url);
//					if (localFile != null && localFile.exists()) {
//						localLastModified = localFile.lastModified();
//					}
//					final boolean delete = localLastModified < lastModifyTime;
//					mHandler.post(new Runnable() {
//						@Override
//						public void run() {
//							mCheckTimeMaps.put(url, System.currentTimeMillis());
//							if (delete) {
//								if (localFile != null) {
//									localFile.delete();
//								}
//								List<String> keys = MemoryCacheUtil.findCacheKeysForImageUri(url, ImageLoader.getInstance().getMemoryCache());
//								if (keys != null && keys.size() > 0) {
//									for (String key : keys) {
//										ImageLoader.getInstance().getMemoryCache().remove(key);
//									}
//								}
//							}
//							display(url, imageView, isThumb);
//						}
//					});
//
//				}
//			}).start();
//		} else {
//			display(url, imageView, isThumb);
//		}
//	}
//
//	private long getLastModify(String url) {
//		HttpURLConnection connection = null;
//		try {
//			connection = (HttpURLConnection) new URL(url).openConnection();
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		if (connection == null) {
//			return 0;
//		} else {
//			connection.setDoOutput(false);
//			connection.setDoInput(true);
//			try {
//				connection.connect();
//				return connection.getLastModified();
//			} catch (IOException e) {
//				e.printStackTrace();
//			} finally {
//				connection.disconnect();
//			}
//		}
//		return 0;
//	}
//
//	private void display(String url, ImageView imageView, boolean isThumb) {
//		if (isThumb) {
//
//            ImageLoader.getInstance().displayImage(url, imageView, MyApplication.mAvatarRoundImageOptions);
//		} else {
//			ImageLoader.getInstance().displayImage(url, imageView, MyApplication.mAvatarNormalImageOptions);
//		}
//	}
//
//	private void display(String url, ImageAware imageAware, boolean isThumb) {
//		if (isThumb) {
//			ImageLoader.getInstance().displayImage(url, imageAware, MyApplication.mAvatarRoundImageOptions);
//		} else {
//			ImageLoader.getInstance().displayImage(url, imageAware, MyApplication.mAvatarNormalImageOptions);
//		}
//	}
//
//	public void displayAvatar(String userId, final ImageAware imageAware, final boolean isThumb) {
//		final String url = getAvatarUrl(userId, isThumb);
//		if (TextUtils.isEmpty(url)) {
//			return;
//		}
//		Long lastCheckTime = mCheckTimeMaps.get(url);
//		if (lastCheckTime == null || System.currentTimeMillis() - lastCheckTime > 1 * 60 * 1000) {
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					long lastModifyTime = getLastModify(url);
//					long localLastModified = 0;
//					final File localFile = ImageLoader.getInstance().getDiscCache().get(url);
//					if (localFile != null && localFile.exists()) {
//						localLastModified = localFile.lastModified();
//					}
//					final boolean delete = localLastModified < lastModifyTime;
//					mHandler.post(new Runnable() {
//						@Override
//						public void run() {
//							mCheckTimeMaps.put(url, System.currentTimeMillis());
//							if (delete) {
//								if (localFile != null) {
//									localFile.delete();
//								}
//								List<String> keys = MemoryCacheUtil.findCacheKeysForImageUri(url, ImageLoader.getInstance().getMemoryCache());
//								if (keys != null && keys.size() > 0) {
//									for (String key : keys) {
//										ImageLoader.getInstance().getMemoryCache().remove(key);
//									}
//								}
//							}
//							display(url, imageAware, isThumb);
//						}
//					});
//				}
//			}).start();
//		} else {
//			display(url, imageAware, isThumb);
//		}
//	}
//
//}
