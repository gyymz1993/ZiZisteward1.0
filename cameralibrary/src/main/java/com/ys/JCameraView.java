////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by Fernflower decompiler)
////
//
//package com.ys;
//
//import android.animation.AnimatorSet;
//import android.animation.ObjectAnimator;
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.graphics.Bitmap;
//import android.media.MediaPlayer;
//import android.media.MediaPlayer.OnPreparedListener;
//import android.media.MediaPlayer.OnVideoSizeChangedListener;
//import android.support.annotation.RequiresApi;
//import android.util.AttributeSet;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.util.TypedValue;
//import android.view.MotionEvent;
//import android.view.SurfaceHolder;
//import android.view.SurfaceHolder.Callback;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.ImageView.ScaleType;
//import android.widget.VideoView;
//
//import com.cjt2325.cameralibrary.R.drawable;
//import com.cjt2325.cameralibrary.R.styleable;
//import com.cjt2325.cameralibrary.lisenter.CaptureLisenter;
//import com.cjt2325.cameralibrary.lisenter.ErrorLisenter;
//import com.cjt2325.cameralibrary.lisenter.JCameraLisenter;
//import com.cjt2325.cameralibrary.lisenter.ReturnLisenter;
//import com.cjt2325.cameralibrary.lisenter.TypeLisenter;
//
//import java.io.File;
//import java.io.IOException;
//
//public class JCameraView extends FrameLayout implements CameraInterface.CamOpenOverCallback, Callback {
//    private static final String TAG = "CJT";
//    private static final int TYPE_PICTURE = 1;
//    private static final int TYPE_VIDEO = 2;
//    public static final int MEDIA_QUALITY_HIGH = 2000000;
//    public static final int MEDIA_QUALITY_MIDDLE = 1600000;
//    public static final int MEDIA_QUALITY_LOW = 1200000;
//    public static final int MEDIA_QUALITY_POOR = 800000;
//    public static final int MEDIA_QUALITY_FUNNY = 400000;
//    public static final int MEDIA_QUALITY_DESPAIR = 200000;
//    public static final int MEDIA_QUALITY_SORRY = 80000;
//    public static final int MEDIA_QUALITY_SORRY_YOU_ARE_GOOD_MAN = 10000;
//    public static final int BUTTON_STATE_ONLY_CAPTURE = 257;
//    public static final int BUTTON_STATE_ONLY_RECORDER = 258;
//    public static final int BUTTON_STATE_BOTH = 259;
//    private JCameraLisenter jCameraLisenter;
//    private Context mContext;
//    private VideoView mVideoView;
//    private ImageView mPhoto;
//    private ImageView mSwitchCamera;
//    private CaptureLayout mCaptureLayout;
//    private FoucsView mFoucsView;
//    private MediaPlayer mMediaPlayer;
//    private int layout_width;
//    private int fouce_size;
//    private float screenProp;
//    private Bitmap captureBitmap;
//    private String videoUrl;
//    private int type;
//    private boolean onlyPause;
//    private int CAMERA_STATE;
//    private static final int STATE_IDLE = 16;
//    private static final int STATE_RUNNING = 32;
//    private static final int STATE_WAIT = 48;
//    private boolean stopping;
//    private boolean isBorrow;
//    private boolean takePictureing;
//    private boolean forbiddenSwitch;
//    private int iconSize;
//    private int iconMargin;
//    private int iconSrc;
//    private int duration;
//    private boolean switching;
//    private boolean firstTouch;
//    private float firstTouchLength;
//    private int zoomScale;
//    private ErrorLisenter errorLisenter;
//
//    public JCameraView(Context context) {
//        this(context, (AttributeSet)null);
//    }
//
//    public JCameraView(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public JCameraView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        this.type = -1;
//        this.onlyPause = false;
//        this.CAMERA_STATE = -1;
//        this.stopping = false;
//        this.isBorrow = false;
//        this.takePictureing = false;
//        this.forbiddenSwitch = false;
//        this.iconSize = 0;
//        this.iconMargin = 0;
//        this.iconSrc = 0;
//        this.duration = 0;
//        this.switching = false;
//        this.firstTouch = true;
//        this.firstTouchLength = 0.0F;
//        this.zoomScale = 0;
//        this.mContext = context;
//        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, styleable.JCameraView, defStyleAttr, 0);
//        this.iconSize = a.getDimensionPixelSize(styleable.JCameraView_iconSize, (int)TypedValue.applyDimension(2, 35.0F, this.getResources().getDisplayMetrics()));
//        this.iconMargin = a.getDimensionPixelSize(styleable.JCameraView_iconMargin, (int)TypedValue.applyDimension(2, 15.0F, this.getResources().getDisplayMetrics()));
//        this.iconSrc = a.getResourceId(styleable.JCameraView_iconSrc, drawable.ic_sync_black_24dp);
//        this.duration = a.getInteger(styleable.JCameraView_duration_max, 10000);
//        a.recycle();
//        this.initData();
//        this.initView();
//    }
//
//    private void initData() {
//        @SuppressLint("WrongConstant") WindowManager manager = (WindowManager)this.mContext.getSystemService("window");
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        manager.getDefaultDisplay().getMetrics(outMetrics);
//        this.layout_width = outMetrics.widthPixels;
//        this.fouce_size = this.layout_width / 4;
//        this.CAMERA_STATE = 16;
//    }
//
//    private void initView() {
//        this.setWillNotDraw(false);
//        this.setBackgroundColor(-16777216);
//        this.mVideoView = new VideoView(this.mContext);
//        LayoutParams videoViewParam = new LayoutParams(-1, -1);
//        this.mVideoView.setLayoutParams(videoViewParam);
//        this.mPhoto = new ImageView(this.mContext);
//        LayoutParams photoParam = new LayoutParams(-1, -1);
//        this.mPhoto.setLayoutParams(photoParam);
//        this.mPhoto.setBackgroundColor(-16777216);
//        this.mPhoto.setVisibility(VISIBLE);
//        this.mSwitchCamera = new ImageView(this.mContext);
//        LayoutParams imageViewParam = new LayoutParams(this.iconSize + 2 * this.iconMargin, this.iconSize + 2 * this.iconMargin);
//        imageViewParam.gravity = 5;
//        this.mSwitchCamera.setPadding(this.iconMargin, this.iconMargin, this.iconMargin, this.iconMargin);
//        this.mSwitchCamera.setLayoutParams(imageViewParam);
//        this.mSwitchCamera.setImageResource(this.iconSrc);
//        this.mSwitchCamera.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                if(!JCameraView.this.isBorrow && !JCameraView.this.switching && !JCameraView.this.forbiddenSwitch) {
//                    JCameraView.this.switching = true;
//                    (new Thread() {
//                        public void run() {
//                            CameraInterface.getInstance().switchCamera(JCameraView.this);
//                        }
//                    }).start();
//                }
//            }
//        });
//        this.mCaptureLayout = new CaptureLayout(this.mContext);
//        LayoutParams layout_param = new LayoutParams(-2, -2);
//        layout_param.gravity = 81;
//        this.mCaptureLayout.setLayoutParams(layout_param);
//        this.mCaptureLayout.setDuration(this.duration);
//        this.mFoucsView = new FoucsView(this.mContext, this.fouce_size);
//        LayoutParams foucs_param = new LayoutParams(-2, -2);
//        foucs_param.gravity = 17;
//        this.mFoucsView.setLayoutParams(foucs_param);
//        this.mFoucsView.setVisibility(0);
//        this.addView(this.mVideoView);
//        this.addView(this.mPhoto);
//        this.addView(this.mSwitchCamera);
//        this.addView(this.mCaptureLayout);
//        this.addView(this.mFoucsView);
//        this.mCaptureLayout.setCaptureLisenter(new CaptureLisenter() {
//            public void takePictures() {
//                if(JCameraView.this.CAMERA_STATE == 16 && !JCameraView.this.takePictureing) {
//                    JCameraView.this.CAMERA_STATE = 32;
//                    JCameraView.this.takePictureing = true;
//                    JCameraView.this.mFoucsView.setVisibility(4);
//                    CameraInterface.getInstance().takePicture(new CameraInterface.TakePictureCallback() {
//                        public void captureResult(Bitmap bitmap, boolean isVertical) {
//                            JCameraView.this.captureBitmap = bitmap;
//                            CameraInterface.getInstance().doStopCamera();
//                            JCameraView.this.type = 1;
//                            JCameraView.this.isBorrow = true;
//                            JCameraView.this.CAMERA_STATE = 48;
//                            if(isVertical) {
//                                JCameraView.this.mPhoto.setScaleType(ScaleType.FIT_XY);
//                            } else {
//                                JCameraView.this.mPhoto.setScaleType(ScaleType.CENTER_INSIDE);
//                            }
//
//                            JCameraView.this.mPhoto.setImageBitmap(bitmap);
//                            JCameraView.this.mPhoto.setVisibility(GONE);
//                            JCameraView.this.mCaptureLayout.startAlphaAnimation();
//                            JCameraView.this.mCaptureLayout.startTypeBtnAnimator();
//                            JCameraView.this.takePictureing = false;
//                            JCameraView.this.mSwitchCamera.setVisibility(VISIBLE);
//                            CameraInterface.getInstance().doOpenCamera(JCameraView.this);
//                        }
//                    });
//                }
//            }
//
//            public void recordShort(long time) {
//                if(JCameraView.this.CAMERA_STATE == 32 || !JCameraView.this.stopping) {
//                    JCameraView.this.stopping = true;
//                    JCameraView.this.mCaptureLayout.setTextWithAnimation("录制时间过短");
//                    JCameraView.this.postDelayed(new Runnable() {
//                        public void run() {
//                            CameraInterface.getInstance().stopRecord(true, new CameraInterface.StopRecordCallback() {
//                                public void recordResult(String url) {
//                                    Log.i("CJT", "Record Stopping ...");
//                                    JCameraView.this.mCaptureLayout.isRecord(false);
//                                    JCameraView.this.CAMERA_STATE = 16;
//                                    JCameraView.this.stopping = false;
//                                    JCameraView.this.isBorrow = false;
//                                }
//                            });
//                        }
//                    }, 1500L - time);
//                }
//            }
//
//            @SuppressLint("WrongConstant")
//            public void recordStart() {
//                if(JCameraView.this.CAMERA_STATE == 16 || !JCameraView.this.stopping) {
//                    JCameraView.this.mCaptureLayout.isRecord(true);
//                    JCameraView.this.isBorrow = true;
//                    JCameraView.this.CAMERA_STATE = 32;
//                    JCameraView.this.mFoucsView.setVisibility(4);
//                    CameraInterface.getInstance().startRecord(JCameraView.this.mVideoView.getHolder().getSurface(), new CameraInterface.ErrorCallback() {
//                        public void onError() {
//                            Log.i("CJT", "startRecorder error");
//                            JCameraView.this.mCaptureLayout.isRecord(false);
//                            JCameraView.this.CAMERA_STATE = 48;
//                            JCameraView.this.stopping = false;
//                            JCameraView.this.isBorrow = false;
//                        }
//                    });
//                }
//            }
//
//            public void recordEnd(long time) {
//                CameraInterface.getInstance().stopRecord(false, new StopRecordCallback() {
//                    public void recordResult(final String url) {
//                        JCameraView.this.CAMERA_STATE = 48;
//                        JCameraView.this.videoUrl = url;
//                        JCameraView.this.type = 2;
//                        (new Thread(new Runnable() {
//                            @RequiresApi(
//                                api = 16
//                            )
//                            public void run() {
//                                try {
//                                    if(JCameraView.this.mMediaPlayer == null) {
//                                        JCameraView.this.mMediaPlayer = new MediaPlayer();
//                                    } else {
//                                        JCameraView.this.mMediaPlayer.reset();
//                                    }
//
//                                    Log.i("CJT", "URL = " + url);
//                                    JCameraView.this.mMediaPlayer.setDataSource(url);
//                                    JCameraView.this.mMediaPlayer.setSurface(JCameraView.this.mVideoView.getHolder().getSurface());
//                                    JCameraView.this.mMediaPlayer.setVideoScalingMode(1);
//                                    JCameraView.this.mMediaPlayer.setAudioStreamType(3);
//                                    JCameraView.this.mMediaPlayer.setOnVideoSizeChangedListener(new OnVideoSizeChangedListener() {
//                                        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
//                                            JCameraView.this.updateVideoViewSize((float)JCameraView.this.mMediaPlayer.getVideoWidth(), (float)JCameraView.this.mMediaPlayer.getVideoHeight());
//                                        }
//                                    });
//                                    JCameraView.this.mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
//                                        public void onPrepared(MediaPlayer mp) {
//                                            JCameraView.this.mMediaPlayer.start();
//                                        }
//                                    });
//                                    JCameraView.this.mMediaPlayer.setLooping(true);
//                                    JCameraView.this.mMediaPlayer.prepare();
//                                } catch (IOException var2) {
//                                    var2.printStackTrace();
//                                }
//
//                            }
//                        })).start();
//                    }
//                });
//            }
//
//            public void recordZoom(float zoom) {
//                CameraInterface.getInstance().setZoom(zoom, 144);
//            }
//
//            public void recordError() {
//                if(JCameraView.this.errorLisenter != null) {
//                    JCameraView.this.errorLisenter.AudioPermissionError();
//                }
//
//            }
//        });
//        this.mCaptureLayout.setTypeLisenter(new TypeLisenter() {
//            public void cancel() {
//                if(JCameraView.this.CAMERA_STATE == 48) {
//                    if(JCameraView.this.mMediaPlayer != null && JCameraView.this.mMediaPlayer.isPlaying()) {
//                        JCameraView.this.mMediaPlayer.stop();
//                        JCameraView.this.mMediaPlayer.release();
//                        JCameraView.this.mMediaPlayer = null;
//                    }
//
//                    JCameraView.this.handlerPictureOrVideo(JCameraView.this.type, false);
//                }
//
//            }
//
//            public void confirm() {
//                if(JCameraView.this.CAMERA_STATE == 48) {
//                    if(JCameraView.this.mMediaPlayer != null && JCameraView.this.mMediaPlayer.isPlaying()) {
//                        JCameraView.this.mMediaPlayer.stop();
//                        JCameraView.this.mMediaPlayer.release();
//                        JCameraView.this.mMediaPlayer = null;
//                    }
//
//                    JCameraView.this.handlerPictureOrVideo(JCameraView.this.type, true);
//                }
//
//            }
//        });
//        this.mCaptureLayout.setReturnLisenter(new ReturnLisenter() {
//            public void onReturn() {
//                if(JCameraView.this.jCameraLisenter != null && !JCameraView.this.takePictureing) {
//                    JCameraView.this.jCameraLisenter.quit();
//                }
//
//            }
//        });
//        this.mVideoView.getHolder().addCallback(this);
//    }
//
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        float widthSize = (float)MeasureSpec.getSize(widthMeasureSpec);
//        float heightSize = (float)MeasureSpec.getSize(heightMeasureSpec);
//        this.screenProp = heightSize / widthSize;
//    }
//
//    public void cameraHasOpened() {
//        CameraInterface.getInstance().doStartPreview(this.mVideoView.getHolder(), this.screenProp);
//    }
//
//    public void cameraSwitchSuccess() {
//        this.switching = false;
//    }
//
//    public void onResume() {
//        CameraInterface.getInstance().registerSensorManager(this.mContext);
//        CameraInterface.getInstance().setSwitchView(this.mSwitchCamera);
//        if(this.onlyPause) {
//            (new Thread() {
//                public void run() {
//                    CameraInterface.getInstance().doOpenCamera(JCameraView.this);
//                }
//            }).start();
//        }
//
//        this.mFoucsView.setVisibility(VISIBLE);
//    }
//
//    public void onPause() {
//        this.onlyPause = true;
//        CameraInterface.getInstance().unregisterSensorManager(this.mContext);
//        CameraInterface.getInstance().doStopCamera();
//    }
//
//    public boolean onTouchEvent(MotionEvent event) {
//        switch(event.getAction()) {
//        case 0:
//            if(event.getPointerCount() == 1) {
//                this.setFocusViewWidthAnimation(event.getX(), event.getY());
//            }
//
//            if(event.getPointerCount() == 2) {
//                Log.i("CJT", "ACTION_DOWN = 2");
//            }
//            break;
//        case 1:
//            this.firstTouch = true;
//            break;
//        case 2:
//            if(event.getPointerCount() == 1) {
//                this.firstTouch = true;
//            }
//
//            if(event.getPointerCount() == 2) {
//                float point_1_X = event.getX(0);
//                float point_1_Y = event.getY(0);
//                float point_2_X = event.getX(1);
//                float point_2_Y = event.getY(1);
//                float result = (float)Math.sqrt(Math.pow((double)(point_1_X - point_2_X), 2.0D) + Math.pow((double)(point_1_Y - point_2_Y), 2.0D));
//                if(this.firstTouch) {
//                    this.firstTouchLength = result;
//                    this.firstTouch = false;
//                }
//
//                if((int)(result - this.firstTouchLength) / 50 != 0) {
//                    this.firstTouch = true;
//                    CameraInterface.getInstance().setZoom(result - this.firstTouchLength, 145);
//                }
//
//                Log.i("CJT", "result = " + (result - this.firstTouchLength));
//            }
//        }
//
//        return true;
//    }
//
//    @SuppressLint("NewApi")
//    private void setFocusViewWidthAnimation(float x, float y) {
//        if(!this.isBorrow) {
//            if(y <= (float)this.mCaptureLayout.getTop()) {
//                this.mFoucsView.setVisibility(0);
//                if(x < (float)(this.mFoucsView.getWidth() / 2)) {
//                    x = (float)(this.mFoucsView.getWidth() / 2);
//                }
//
//                if(x > (float)(this.layout_width - this.mFoucsView.getWidth() / 2)) {
//                    x = (float)(this.layout_width - this.mFoucsView.getWidth() / 2);
//                }
//
//                if(y < (float)(this.mFoucsView.getWidth() / 2)) {
//                    y = (float)(this.mFoucsView.getWidth() / 2);
//                }
//
//                if(y > (float)(this.mCaptureLayout.getTop() - this.mFoucsView.getWidth() / 2)) {
//                    y = (float)(this.mCaptureLayout.getTop() - this.mFoucsView.getWidth() / 2);
//                }
//
//                CameraInterface.getInstance().handleFocus(this.mContext, x, y, new FocusCallback() {
//                    public void focusSuccess() {
//                        JCameraView.this.mFoucsView.setVisibility(VISIBLE);
//                    }
//                });
//                this.mFoucsView.setX(x - (float)(this.mFoucsView.getWidth() / 2));
//                this.mFoucsView.setY(y - (float)(this.mFoucsView.getHeight() / 2));
//                ObjectAnimator scaleX = ObjectAnimator.ofFloat(this.mFoucsView, "scaleX", new float[]{1.0F, 0.6F});
//                ObjectAnimator scaleY = ObjectAnimator.ofFloat(this.mFoucsView, "scaleY", new float[]{1.0F, 0.6F});
//                ObjectAnimator alpha = ObjectAnimator.ofFloat(this.mFoucsView, "alpha", new float[]{1.0F, 0.3F, 1.0F, 0.3F, 1.0F, 0.3F, 1.0F});
//                AnimatorSet animSet = new AnimatorSet();
//                animSet.play(scaleX).with(scaleY).before(alpha);
//                animSet.setDuration(400L);
//                animSet.start();
//            }
//        }
//    }
//
//    public void setJCameraLisenter(JCameraLisenter jCameraLisenter) {
//        this.jCameraLisenter = jCameraLisenter;
//    }
//
//    private void handlerPictureOrVideo(int type, boolean confirm) {
//        if(this.jCameraLisenter != null && type != -1) {
//            switch(type) {
//            case 1:
//                this.mPhoto.setVisibility(4);
//                if(confirm && this.captureBitmap != null) {
//                    this.jCameraLisenter.captureSuccess(this.captureBitmap);
//                } else {
//                    if(this.captureBitmap != null) {
//                        this.captureBitmap.recycle();
//                    }
//
//                    this.captureBitmap = null;
//                }
//                break;
//            case 2:
//                if(confirm) {
//                    this.jCameraLisenter.recordSuccess(videoUrl);
//                } else {
//                    File file = new File(this.videoUrl);
//                    if(file.exists()) {
//                        file.delete();
//                    }
//                }
//
//                this.mCaptureLayout.isRecord(false);
//                LayoutParams videoViewParam = new LayoutParams(-1, -1);
//                this.mVideoView.setLayoutParams(videoViewParam);
//                CameraInterface.getInstance().doOpenCamera(this);
//            }
//
//            this.isBorrow = false;
//            this.mSwitchCamera.setVisibility(GONE);
//            this.CAMERA_STATE = 16;
//        }
//    }
//
//    public void setSaveVideoPath(String path) {
//        CameraInterface.getInstance().setSaveVideoPath(path);
//    }
//
//    public void updateVideoViewSize(float videoWidth, float videoHeight) {
//        if(videoWidth > videoHeight) {
//            int height = (int)(videoHeight / videoWidth * (float)this.getWidth());
//            LayoutParams videoViewParam = new LayoutParams(-1, height);
//            videoViewParam.gravity = 17;
//            this.mVideoView.setLayoutParams(videoViewParam);
//        }
//
//    }
//
//    public void enableshutterSound(boolean enable) {
//    }
//
//    public void forbiddenSwitchCamera(boolean forbiddenSwitch) {
//        this.forbiddenSwitch = forbiddenSwitch;
//    }
//
//    public void setErrorLisenter(ErrorLisenter errorLisenter) {
//        this.errorLisenter = errorLisenter;
//        CameraInterface.getInstance().setErrorLinsenter(errorLisenter);
//    }
//
//    public void setFeatures(int state) {
//        this.mCaptureLayout.setButtonFeatures(state);
//    }
//
//    public void setMediaQuality(int quality) {
//        CameraInterface.getInstance().setMediaQuality(quality);
//    }
//
//    public void surfaceCreated(SurfaceHolder holder) {
//        Log.i("CJT", "surfaceCreated");
//        (new Thread() {
//            public void run() {
//                CameraInterface.getInstance().doOpenCamera(JCameraView.this);
//            }
//        }).start();
//    }
//
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//    }
//
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        this.onlyPause = false;
//        Log.i("CJT", "surfaceDestroyed");
//        CameraInterface.getInstance().doDestroyCamera();
//    }
//}
