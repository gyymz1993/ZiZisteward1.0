//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ys.utils;

import android.hardware.Camera.Size;
import android.util.Log;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class CameraParamUtil {
    private static final String TAG = "JCameraView";
    private CameraParamUtil.CameraSizeComparator sizeComparator = new CameraParamUtil.CameraSizeComparator();
    private static CameraParamUtil cameraParamUtil = null;

    private CameraParamUtil() {
    }

    public static CameraParamUtil getInstance() {
        if(cameraParamUtil == null) {
            cameraParamUtil = new CameraParamUtil();
            return cameraParamUtil;
        } else {
            return cameraParamUtil;
        }
    }

    public Size getPreviewSize(List<Size> list, int th, float rate) {
        Collections.sort(list, this.sizeComparator);
        int i = 0;

        for(Iterator var5 = list.iterator(); var5.hasNext(); ++i) {
            Size s = (Size)var5.next();
            if(s.width > th && this.equalRate(s, rate)) {
                Log.i("JCameraView", "MakeSure Preview :w = " + s.width + " h = " + s.height);
                break;
            }
        }

        return i == list.size()?this.getBestSize(list, rate):(Size)list.get(i);
    }

    public Size getPictureSize(List<Size> list, int th, float rate) {
        Collections.sort(list, this.sizeComparator);
        int i = 0;

        for(Iterator var5 = list.iterator(); var5.hasNext(); ++i) {
            Size s = (Size)var5.next();
            if(s.width > th && this.equalRate(s, rate)) {
                Log.i("JCameraView", "MakeSure Picture :w = " + s.width + " h = " + s.height);
                break;
            }
        }

        return i == list.size()?this.getBestSize(list, rate):(Size)list.get(i);
    }

    private Size getBestSize(List<Size> list, float rate) {
        float previewDisparity = 100.0F;
        int index = 0;

        for(int i = 0; i < list.size(); ++i) {
            Size cur = (Size)list.get(i);
            float prop = (float)cur.width / (float)cur.height;
            if(Math.abs(rate - prop) < previewDisparity) {
                previewDisparity = Math.abs(rate - prop);
                index = i;
            }
        }

        return (Size)list.get(index);
    }

    private boolean equalRate(Size s, float rate) {
        float r = (float)s.width / (float)s.height;
        return (double)Math.abs(r - rate) <= 0.2D;
    }

    public boolean isSupportedFocusMode(List<String> focusList, String focusMode) {
        for(int i = 0; i < focusList.size(); ++i) {
            if(focusMode.equals(focusList.get(i))) {
                Log.i("JCameraView", "FocusMode supported " + focusMode);
                return true;
            }
        }

        Log.i("JCameraView", "FocusMode not supported " + focusMode);
        return false;
    }

    public boolean isSupportedPictureFormats(List<Integer> supportedPictureFormats, int jpeg) {
        for(int i = 0; i < supportedPictureFormats.size(); ++i) {
            if(jpeg == ((Integer)supportedPictureFormats.get(i)).intValue()) {
                Log.i("JCameraView", "Formats supported " + jpeg);
                return true;
            }
        }

        Log.i("JCameraView", "Formats not supported " + jpeg);
        return false;
    }

    private class CameraSizeComparator implements Comparator<Size> {
        private CameraSizeComparator() {
        }

        public int compare(Size lhs, Size rhs) {
            return lhs.width == rhs.width?0:(lhs.width > rhs.width?1:-1);
        }
    }
}
