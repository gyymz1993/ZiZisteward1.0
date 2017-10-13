//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ys.utils;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.AudioRecord;
import android.util.Log;

public class CheckPermission {
    public static final int STATE_RECORDING = -1;
    public static final int STATE_NO_PERMISSION = -2;
    public static final int STATE_SUCCESS = 1;

    public CheckPermission() {
    }

    public static int getRecordState() {
        int minBuffer = AudioRecord.getMinBufferSize('걄', 16, 2);
        AudioRecord audioRecord = new AudioRecord(0, '걄', 16, 2, minBuffer * 100);
        short[] point = new short[minBuffer];
        boolean var3 = false;

        try {
            audioRecord.startRecording();
        } catch (Exception var5) {
            if(audioRecord != null) {
                audioRecord.release();
                audioRecord = null;
            }

            return -2;
        }

        if(audioRecord.getRecordingState() != 3) {
            if(audioRecord != null) {
                audioRecord.stop();
                audioRecord.release();
                audioRecord = null;
                Log.d("CheckAudioPermission", "录音机被占用");
            }

            return -1;
        } else {
            int readSize = audioRecord.read(point, 0, point.length);
            if(readSize <= 0) {
                if(audioRecord != null) {
                    audioRecord.stop();
                    audioRecord.release();
                    audioRecord = null;
                }

                Log.d("CheckAudioPermission", "录音的结果为空");
                return -2;
            } else {
                if(audioRecord != null) {
                    audioRecord.stop();
                    audioRecord.release();
                    audioRecord = null;
                }

                return 1;
            }
        }
    }

    public static synchronized boolean isCameraUseable(int cameraID) {
        boolean canUse = true;
        Camera mCamera = null;

        try {
            mCamera = Camera.open(cameraID);
            Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
        } catch (Exception var7) {
            var7.printStackTrace();
            canUse = false;
        } finally {
            if(mCamera != null) {
                mCamera.release();
            } else {
                canUse = false;
            }

        }

        return canUse;
    }
}
