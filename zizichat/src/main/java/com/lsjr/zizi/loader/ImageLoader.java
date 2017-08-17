package com.lsjr.zizi.loader;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.lsjr.zizi.R;
import com.ymz.baselibrary.utils.UIUtils;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/8/17 10:27
 */

public class ImageLoader {
    private static final ImageLoader ourInstance = new ImageLoader();

    public static ImageLoader getInstance() {
        return ourInstance;
    }

    private ImageLoader() {
    }


    public void showRealImage(String url,OnShowBitmapListener onShowBitmapListener){
        final int MAX_W_H_RATIO = 3;
        Glide.with(UIUtils.getContext()).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
            @SuppressLint("NewApi")
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                int parentWidth=UIUtils.WHD()[0];
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();
                int newW;
                int newH;
                if (h > w * MAX_W_H_RATIO) {//h:w = 5:3
                    newW = parentWidth / 2;
                    newH = newW * 5 / 3;
                } else if (h < w) {//h:w = 2:3
                    newW = parentWidth * 2 / 3;
                    newH = newW * 2 / 3;
                } else {//newH:h = newW :w
                    newW = parentWidth / 2;
                    newH = h * newW / w;
                }
                bitmap.setWidth(newW);
                bitmap.setHeight(newH);
                BitmapDrawable drawable = new BitmapDrawable(UIUtils.getResources(),bitmap);
                if (onShowBitmapListener!=null){
                    onShowBitmapListener.onShow(bitmap);
                }
            }
        }); //方法中设置asBitmap可以设置回调类型
    }

    public void showRealWHImage(String url,OnShowBitmapListener onShowBitmapListener){
        Glide.with(UIUtils.getContext()).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
            @SuppressLint("NewApi")
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                BitmapDrawable drawable = new BitmapDrawable(UIUtils.getResources(),bitmap);
                if (onShowBitmapListener!=null){
                    onShowBitmapListener.onShow(bitmap);
                }
            }
        }); //方法中设置asBitmap可以设置回调类型
    }

    public void showRealWHImage(Uri url, OnShowBitmapListener onShowBitmapListener){
        Glide.with(UIUtils.getContext()).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
            @SuppressLint("NewApi")
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                BitmapDrawable drawable = new BitmapDrawable(UIUtils.getResources(),bitmap);
                if (onShowBitmapListener!=null){
                    onShowBitmapListener.onShow(bitmap);
                }
            }
        }); //方法中设置asBitmap可以设置回调类型
    }



    OnShowBitmapListener onShowBitmapListener;

    public interface OnShowBitmapListener{
        void onShow(Bitmap bitmap);
    }

    public void display(String url, ImageView imageView, boolean isThumb) {
        Glide.with(UIUtils.getContext()).load(url).error(R.drawable.icon_head).into(imageView);
    }

//    .placeholder(R.mipmap.default_img)
    public void showImage(String url, ImageView imageView,int errorResId) {
        Glide.with(UIUtils.getContext()).load(url).skipMemoryCache(false).
                diskCacheStrategy(DiskCacheStrategy.SOURCE).
                error(errorResId).centerCrop().into(imageView);
    }

    public void showImage(String url, ImageView imageView,int errorResId,int width,int height) {
        Glide.with(UIUtils.getContext()).load(url).skipMemoryCache(false).
                diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .override(width,height).
                error(errorResId).centerCrop().into(imageView);
    }

    public void showImage(Uri url, ImageView imageView,int errorResId,int placeResId) {
        Glide.with(UIUtils.getContext()).load(url).skipMemoryCache(false).
                diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(placeResId)
                .error(errorResId).centerCrop().into(imageView);
    }

//    .placeholder(R.mipmap.default_img)
    public void showImage(Uri url, ImageView imageView, int errorResId, int width, int height) {
        Glide.with(UIUtils.getContext()).load(url).skipMemoryCache(false).
                diskCacheStrategy(DiskCacheStrategy.SOURCE).skipMemoryCache(true)
                .override(width,height).
                error(errorResId).centerCrop().into(imageView);
    }

    public void showVideo(String url, ImageView imageView) {
        Glide.with(UIUtils.getContext()).load(url).skipMemoryCache(false).
                diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(imageView);
    }

    //    .placeholder(R.mipmap.default_img)
    public void showfriendImage(String url, ImageView imageView) {
        Glide.with(UIUtils.getContext()).load(url).skipMemoryCache(true).
                diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(imageView);
    }

}
