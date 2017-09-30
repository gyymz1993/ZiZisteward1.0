//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ys;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Camera.Area;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
import android.os.Environment;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.widget.ImageView;
import com.cjt2325.cameralibrary.lisenter.ErrorLisenter;
import com.cjt2325.cameralibrary.util.AngleUtil;
import com.cjt2325.cameralibrary.util.CameraParamUtil;
import com.cjt2325.cameralibrary.util.CheckPermission;
import com.cjt2325.cameralibrary.util.DeviceUtil;
import com.cjt2325.cameralibrary.util.ScreenUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CameraInterface {
    private static final String TAG = "CJT";
    private Camera mCamera;
    private Parameters mParams;
    private boolean isPreviewing = false;
    private static CameraInterface mCameraInterface;
    private int SELECTED_CAMERA = -1;
    private int CAMERA_POST_POSITION = -1;
    private int CAMERA_FRONT_POSITION = -1;
    private SurfaceHolder mHolder = null;
    private float screenProp = -1.0F;
    private boolean isRecorder = false;
    private MediaRecorder mediaRecorder;
    private String videoFileName;
    private String saveVideoPath;
    private String videoFileAbsPath;
    private ErrorLisenter errorLisenter;
    private ImageView mSwitchView;
    private int preview_width;
    private int preview_height;
    private int angle = 0;
    private int rotation = 0;
    private boolean error = false;
    private int mediaQuality = 1600000;
    private SensorManager sm = null;
    private SensorEventListener sensorEventListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent event) {
            if(1 == event.sensor.getType()) {
                float[] values = event.values;
                CameraInterface.this.angle = AngleUtil.getSensorAngle(values[0], values[1]);
                CameraInterface.this.rotationAnimation();
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    public static final int TYPE_RECORDER = 144;
    public static final int TYPE_CAPTURE = 145;
    private int nowScaleRate = 0;
    private int recordScleRate = 0;

    boolean isPreviewing() {
        return this.isPreviewing;
    }

    public static void destroyCameraInterface() {
        if(mCameraInterface != null) {
            mCameraInterface = null;
        }

    }

    public void setSwitchView(ImageView mSwitchView) {
        this.mSwitchView = mSwitchView;
    }

    private void rotationAnimation() {
        if(this.mSwitchView != null) {
            if(this.rotation != this.angle) {
                short start_rotaion;
                short end_rotation;
                start_rotaion = 0;
                end_rotation = 0;
                label37:
                switch(this.rotation) {
                case 0:
                    start_rotaion = 0;
                    switch(this.angle) {
                    case 90:
                        end_rotation = -90;
                        break label37;
                    case 270:
                        end_rotation = 90;
                    default:
                        break label37;
                    }
                case 90:
                    start_rotaion = -90;
                    switch(this.angle) {
                    case 0:
                        end_rotation = 0;
                        break label37;
                    case 180:
                        end_rotation = -180;
                    default:
                        break label37;
                    }
                case 180:
                    start_rotaion = 180;
                    switch(this.angle) {
                    case 90:
                        end_rotation = 270;
                        break label37;
                    case 270:
                        end_rotation = 90;
                    default:
                        break label37;
                    }
                case 270:
                    start_rotaion = 90;
                    switch(this.angle) {
                    case 0:
                        end_rotation = 0;
                        break;
                    case 180:
                        end_rotation = 180;
                    }
                }

                ObjectAnimator anim = ObjectAnimator.ofFloat(this.mSwitchView, "rotation", new float[]{(float)start_rotaion, (float)end_rotation});
                anim.setDuration(500L);
                anim.start();
                this.rotation = this.angle;
            }

        }
    }

    public void setSaveVideoPath(String saveVideoPath) {
        this.saveVideoPath = saveVideoPath;
        File file = new File(saveVideoPath);
        if(!file.exists()) {
            file.mkdirs();
        }

    }

    public void setZoom(float zoom, int type) {
        if(this.mCamera != null) {
            if(this.mParams == null) {
                this.mParams = this.mCamera.getParameters();
            }

            if(this.mParams.isZoomSupported() && this.mParams.isSmoothZoomSupported()) {
                int scaleRate;
                switch(type) {
                case 144:
                    if(!this.isRecorder) {
                        return;
                    }

                    if(zoom >= 0.0F) {
                        scaleRate = (int)(zoom / 50.0F);
                        if(scaleRate <= this.mParams.getMaxZoom() && scaleRate >= this.nowScaleRate && this.recordScleRate != scaleRate) {
                            this.mParams.setZoom(scaleRate);
                            this.mCamera.setParameters(this.mParams);
                            this.recordScleRate = scaleRate;
                        }
                    }
                    break;
                case 145:
                    if(this.isRecorder) {
                        return;
                    }

                    scaleRate = (int)(zoom / 50.0F);
                    if(scaleRate < this.mParams.getMaxZoom()) {
                        this.nowScaleRate += scaleRate;
                        if(this.nowScaleRate < 0) {
                            this.nowScaleRate = 0;
                        } else if(this.nowScaleRate > this.mParams.getMaxZoom()) {
                            this.nowScaleRate = this.mParams.getMaxZoom();
                        }

                        this.mParams.setZoom(this.nowScaleRate);
                        this.mCamera.setParameters(this.mParams);
                    }

                    Log.i("CJT", "nowScaleRate = " + this.nowScaleRate);
                }

            }
        }
    }

    public void setMediaQuality(int quality) {
        this.mediaQuality = quality;
    }

    private CameraInterface() {
        this.findAvailableCameras();
        this.SELECTED_CAMERA = this.CAMERA_POST_POSITION;
        this.saveVideoPath = "";
    }

    public static synchronized CameraInterface getInstance() {
        if(mCameraInterface == null) {
            mCameraInterface = new CameraInterface();
        }

        return mCameraInterface;
    }

    void doOpenCamera(CameraInterface.CamOpenOverCallback callback) {
        if(!CheckPermission.isCameraUseable(this.SELECTED_CAMERA) && this.errorLisenter != null) {
            this.errorLisenter.onError();
        } else {
            if(this.mCamera == null) {
                this.openCamera(this.SELECTED_CAMERA);
            }

            callback.cameraHasOpened();
        }
    }

    private void openCamera(int id) {
        try {
            this.mCamera = Camera.open(id);
        } catch (Exception var4) {
            if(this.errorLisenter != null) {
                this.errorLisenter.onError();
            }
        }

        if(VERSION.SDK_INT > 17 && this.mCamera != null) {
            try {
                this.mCamera.enableShutterSound(false);
            } catch (Exception var3) {
                var3.printStackTrace();
                Log.e("CJT", "enable shutter sound faild");
            }
        }

    }

    public synchronized void switchCamera(CameraInterface.CamOpenOverCallback callback) {
        if(this.SELECTED_CAMERA == this.CAMERA_POST_POSITION) {
            this.SELECTED_CAMERA = this.CAMERA_FRONT_POSITION;
        } else {
            this.SELECTED_CAMERA = this.CAMERA_POST_POSITION;
        }

        this.doStopCamera();
        this.mCamera = Camera.open(this.SELECTED_CAMERA);
        this.doStartPreview(this.mHolder, this.screenProp);
        callback.cameraSwitchSuccess();
    }

    void doStartPreview(SurfaceHolder holder, float screenProp) {
        if(this.screenProp < 0.0F) {
            this.screenProp = screenProp;
        }

        if(holder != null) {
            this.mHolder = holder;
            if(this.mCamera != null) {
                try {
                    this.mParams = this.mCamera.getParameters();
                    Size previewSize = CameraParamUtil.getInstance().getPreviewSize(this.mParams.getSupportedPreviewSizes(), 1000, screenProp);
                    Size pictureSize = CameraParamUtil.getInstance().getPictureSize(this.mParams.getSupportedPictureSizes(), 1200, screenProp);
                    this.mParams.setPreviewSize(previewSize.width, previewSize.height);
                    this.preview_width = previewSize.width;
                    this.preview_height = previewSize.height;
                    this.mParams.setPictureSize(pictureSize.width, pictureSize.height);
                    if(CameraParamUtil.getInstance().isSupportedFocusMode(this.mParams.getSupportedFocusModes(), "auto")) {
                        this.mParams.setFocusMode("auto");
                    }

                    if(CameraParamUtil.getInstance().isSupportedPictureFormats(this.mParams.getSupportedPictureFormats(), 256)) {
                        this.mParams.setPictureFormat(256);
                        this.mParams.setJpegQuality(100);
                    }

                    this.mCamera.setParameters(this.mParams);
                    this.mParams = this.mCamera.getParameters();
                    this.mCamera.setPreviewDisplay(holder);
                    this.mCamera.setDisplayOrientation(90);
                    this.mCamera.startPreview();
                    this.isPreviewing = true;
                } catch (IOException var5) {
                    var5.printStackTrace();
                } catch (Exception var6) {
                    var6.printStackTrace();
                }
            }

            Log.i("CJT", "=== Start Preview ===");
        }
    }

    public void doStopCamera() {
        if(null != this.mCamera) {
            try {
                this.mCamera.stopPreview();
                this.mCamera.setPreviewDisplay((SurfaceHolder)null);
                this.isPreviewing = false;
                this.mCamera.release();
                this.mCamera = null;
                Log.i("CJT", "=== Stop Camera ===");
            } catch (IOException var2) {
                var2.printStackTrace();
            }
        }

    }

    public void doDestroyCamera() {
        if(null != this.mCamera) {
            try {
                this.mSwitchView = null;
                this.mCamera.stopPreview();
                this.mCamera.setPreviewDisplay((SurfaceHolder)null);
                this.mHolder = null;
                this.isPreviewing = false;
                this.mCamera.release();
                this.mCamera = null;
                destroyCameraInterface();
                Log.i("CJT", "=== Destroy Camera ===");
            } catch (IOException var2) {
                var2.printStackTrace();
            }
        }

    }

    void takePicture(final CameraInterface.TakePictureCallback callback) {
        if(this.mCamera != null) {
            final int nowAngle = (this.angle + 90) % 360;
            this.mCamera.takePicture((ShutterCallback)null, (PictureCallback)null, new PictureCallback() {
                public void onPictureTaken(byte[] data, Camera camera) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    Matrix matrix = new Matrix();
                    if(CameraInterface.this.SELECTED_CAMERA == CameraInterface.this.CAMERA_POST_POSITION) {
                        matrix.setRotate((float)nowAngle);
                    } else if(CameraInterface.this.SELECTED_CAMERA == CameraInterface.this.CAMERA_FRONT_POSITION) {
                        matrix.setRotate(270.0F);
                        matrix.postScale(-1.0F, 1.0F);
                    }

                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    if(callback != null) {
                        if(nowAngle != 90 && nowAngle != 270) {
                            callback.captureResult(bitmap, false);
                        } else {
                            callback.captureResult(bitmap, true);
                        }
                    }

                }
            });
        }
    }

    void startRecord(Surface surface, CameraInterface.ErrorCallback callback) {
        if(!this.isRecorder) {
            int nowAngle = (this.angle + 90) % 360;
            if(this.mCamera == null) {
                this.openCamera(this.SELECTED_CAMERA);
            }

            if(this.mediaRecorder == null) {
                this.mediaRecorder = new MediaRecorder();
            }

            if(this.mParams == null) {
                this.mParams = this.mCamera.getParameters();
            }

            List<String> focusModes = this.mParams.getSupportedFocusModes();
            if(focusModes.contains("continuous-video")) {
                this.mParams.setFocusMode("continuous-video");
            }

            this.mCamera.setParameters(this.mParams);
            this.mCamera.unlock();
            this.mediaRecorder.reset();
            this.mediaRecorder.setCamera(this.mCamera);
            this.mediaRecorder.setVideoSource(1);
            this.mediaRecorder.setAudioSource(1);
            this.mediaRecorder.setOutputFormat(2);
            this.mediaRecorder.setVideoEncoder(2);
            this.mediaRecorder.setAudioEncoder(3);
            Size videoSize;
            if(this.mParams.getSupportedVideoSizes() == null) {
                videoSize = CameraParamUtil.getInstance().getPictureSize(this.mParams.getSupportedPreviewSizes(), 1000, this.screenProp);
            } else {
                videoSize = CameraParamUtil.getInstance().getPictureSize(this.mParams.getSupportedVideoSizes(), 1000, this.screenProp);
            }

            if(videoSize.width == videoSize.height) {
                this.mediaRecorder.setVideoSize(this.preview_width, this.preview_height);
            } else {
                this.mediaRecorder.setVideoSize(videoSize.width, videoSize.height);
            }

            if(this.SELECTED_CAMERA == this.CAMERA_FRONT_POSITION) {
                this.mediaRecorder.setOrientationHint(270);
            } else {
                this.mediaRecorder.setOrientationHint(nowAngle);
            }

            if(DeviceUtil.isHuaWeiRongyao()) {
                this.mediaRecorder.setVideoEncodingBitRate(400000);
            } else {
                this.mediaRecorder.setVideoEncodingBitRate(this.mediaQuality);
            }

            this.mediaRecorder.setPreviewDisplay(surface);
            this.videoFileName = "video_" + System.currentTimeMillis() + ".mp4";
            if(this.saveVideoPath.equals("")) {
                this.saveVideoPath = Environment.getExternalStorageDirectory().getPath();
            }

            this.videoFileAbsPath = this.saveVideoPath + File.separator + this.videoFileName;
            this.mediaRecorder.setOutputFile(this.videoFileAbsPath);

            try {
                this.mediaRecorder.prepare();
                this.mediaRecorder.start();
                this.isRecorder = true;
            } catch (IllegalStateException var7) {
                var7.printStackTrace();
                Log.i("CJT", "startRecord IllegalStateException");
                if(this.errorLisenter != null) {
                    this.errorLisenter.onError();
                }
            } catch (IOException var8) {
                var8.printStackTrace();
                Log.i("CJT", "startRecord IOException");
                if(this.errorLisenter != null) {
                    this.errorLisenter.onError();
                }
            } catch (RuntimeException var9) {
                Log.i("CJT", "startRecord RuntimeException");
            }

        }
    }

    void stopRecord(boolean isShort, CameraInterface.StopRecordCallback callback) {
        if(this.isRecorder) {
            if(this.mediaRecorder != null) {
                this.mediaRecorder.setOnErrorListener((OnErrorListener)null);
                this.mediaRecorder.setOnInfoListener((OnInfoListener)null);
                this.mediaRecorder.setPreviewDisplay((Surface)null);

                try {
                    this.mediaRecorder.stop();
                } catch (RuntimeException var8) {
                    var8.printStackTrace();
                    this.mediaRecorder = null;
                    this.mediaRecorder = new MediaRecorder();
                    Log.i("CJT", "stop RuntimeException");
                } catch (Exception var9) {
                    var9.printStackTrace();
                    this.mediaRecorder = null;
                    this.mediaRecorder = new MediaRecorder();
                    Log.i("CJT", "stop Exception");
                } finally {
                    if(this.mediaRecorder != null) {
                        this.mediaRecorder.release();
                    }

                    this.mediaRecorder = null;
                    this.isRecorder = false;
                }

                if(isShort) {
                    boolean result = true;
                    File file = new File(this.videoFileAbsPath);
                    if(file.exists()) {
                        result = file.delete();
                    }

                    if(result) {
                        callback.recordResult((String)null);
                    }

                    return;
                }

                this.doStopCamera();
                String fileName = this.saveVideoPath + File.separator + this.videoFileName;
                callback.recordResult(fileName);
            }

        }
    }

    private void findAvailableCameras() {
        CameraInfo info = new CameraInfo();
        int cameraNum = Camera.getNumberOfCameras();

        for(int i = 0; i < cameraNum; ++i) {
            Camera.getCameraInfo(i, info);
            switch(info.facing) {
            case 0:
                this.CAMERA_POST_POSITION = info.facing;
                break;
            case 1:
                this.CAMERA_FRONT_POSITION = info.facing;
            }
        }

    }

    public void handleFocus(final Context context, final float x, final float y, final CameraInterface.FocusCallback callback) {
        if(this.mCamera != null) {
            Parameters params = this.mCamera.getParameters();
            Rect focusRect = calculateTapArea(x, y, 1.0F, context);
            this.mCamera.cancelAutoFocus();
            if(params.getMaxNumFocusAreas() > 0) {
                List<Area> focusAreas = new ArrayList();
                focusAreas.add(new Area(focusRect, 800));
                params.setFocusAreas(focusAreas);
                final String currentFocusMode = params.getFocusMode();

                try {
                    params.setFocusMode("auto");
                    this.mCamera.setParameters(params);
                    this.mCamera.autoFocus(new AutoFocusCallback() {
                        public void onAutoFocus(boolean success, Camera camera) {
                            if(success) {
                                Parameters params = camera.getParameters();
                                params.setFocusMode(currentFocusMode);
                                camera.setParameters(params);
                                callback.focusSuccess();
                            } else {
                                CameraInterface.this.handleFocus(context, x, y, callback);
                            }

                        }
                    });
                } catch (Exception var9) {
                    Log.e("CJT", "autoFocus failer");
                }

            } else {
                Log.i("CJT", "focus areas not supported");
                callback.focusSuccess();
            }
        }
    }

    private static Rect calculateTapArea(float x, float y, float coefficient, Context context) {
        float focusAreaSize = 300.0F;
        int areaSize = Float.valueOf(focusAreaSize * coefficient).intValue();
        int centerX = (int)(x / (float)ScreenUtils.getScreenHeight(context) * 2000.0F - 1000.0F);
        int centerY = (int)(y / (float)ScreenUtils.getScreenWidth(context) * 2000.0F - 1000.0F);
        int left = clamp(centerX - areaSize / 2, -1000, 1000);
        int top = clamp(centerY - areaSize / 2, -1000, 1000);
        RectF rectF = new RectF((float)left, (float)top, (float)(left + areaSize), (float)(top + areaSize));
        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }

    private static int clamp(int x, int min, int max) {
        return x > max?max:(x < min?min:x);
    }

    public void setErrorLinsenter(ErrorLisenter errorLisenter) {
        this.errorLisenter = errorLisenter;
    }

    public void registerSensorManager(Context context) {
        if(this.sm == null) {
            this.sm = (SensorManager)context.getSystemService("sensor");
        }

        this.sm.registerListener(this.sensorEventListener, this.sm.getDefaultSensor(1), 3);
    }

    public void unregisterSensorManager(Context context) {
        if(this.sm == null) {
            this.sm = (SensorManager)context.getSystemService("sensor");
        }

        this.sm.unregisterListener(this.sensorEventListener);
    }

    interface FocusCallback {
        void focusSuccess();
    }

    interface TakePictureCallback {
        void captureResult(Bitmap var1, boolean var2);
    }

    interface ErrorCallback {
        void onError();
    }

    interface StopRecordCallback {
        void recordResult(String var1);
    }

    interface CamOpenOverCallback {
        void cameraHasOpened();

        void cameraSwitchSuccess();
    }
}
