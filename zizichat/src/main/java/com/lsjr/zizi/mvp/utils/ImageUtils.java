package com.lsjr.zizi.mvp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 创建人：$ gyymz1993
 * 创建时间：2017/8/31 14:19
 */

public class ImageUtils {
    public static ImageSize getImageSize(Bitmap bitmap) {
        ImageSize imageSize = new ImageSize();
        if (null == bitmap || bitmap.isRecycled()) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteTmp = baos.toByteArray();
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(byteTmp, 0, byteTmp.length, bitmapOptions);
        int outWidth = bitmapOptions.outWidth;
        int outHeight = bitmapOptions.outHeight;
        int maxWidth = 400;
        int maxHeight = 400;
        int minWidth = 150;
        int minHeight = 150;
        if (outWidth / maxWidth > outHeight / maxHeight) {//
            if (outWidth >= maxWidth) {//
                imageSize.setWidth(maxWidth);
                imageSize.setHeight(outHeight * maxWidth / outWidth);
            } else {
                imageSize.setWidth(outWidth);
                imageSize.setHeight(outHeight);
            }
            if (outHeight < minHeight) {
                imageSize.setHeight(minHeight);
                int width = outWidth * minHeight / outHeight;
                if (width > maxWidth) {
                    imageSize.setWidth(maxWidth);
                } else {
                    imageSize.setWidth(width);
                }
            }
        } else {
            if (outHeight >= maxHeight) {
                imageSize.setHeight(maxHeight);
                imageSize.setWidth(outWidth * maxHeight / outHeight);
            } else {
                imageSize.setHeight(outHeight);
                imageSize.setWidth(outWidth);
            }
            if (outWidth < minWidth) {
                imageSize.setWidth(minWidth);
                int height = outHeight * minWidth / outWidth;
                if (height > maxHeight) {
                    imageSize.setHeight(maxHeight);
                } else {
                    imageSize.setHeight(height);
                }
            }
        }

        return imageSize;
    }


    public static class ImageSize {

        private int width;
        private int height;
        public int getWidth() {
            return width;
        }
        public void setWidth(int width) {
            this.width = width;
        }
        public int getHeight() {
            return height;
        }
        public void setHeight(int height) {
            this.height = height;
        }
    }
}
