package com.lsjr.zizisteward.contrl;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lsjr.zizisteward.R;
import com.lsjr.zizisteward.contrl.view.ArcMenu;
import com.lsjr.zizisteward.contrl.view.ViewUtil;
import com.ymz.baselibrary.BaseApplication;
import com.ymz.baselibrary.utils.L_;
import com.ymz.baselibrary.utils.T_;

import java.util.ArrayList;
import java.util.List;


public class MenuController implements View.OnTouchListener {
    private static final int MOVETOEDGE = 10010;
    private static final float DEFAULT_MAX_LENGTH = 146;
    private static final float DEFAULT_MIN_LENGTH = 50;
    public static final int DEFAULT_MIN_WIDTH_HIDE = 20;
    private static float MAX_LENGTH = 150;
    private static float MIN_LENGTH = 150;
    private ArcMenu archMenu;
    private boolean isShowIcon;
    private ImageView floatImageView;
    private float mCurrentIconAlpha = 1f;
    int padding = ViewUtil.dp2px(3);
    int arcMenupadding = ViewUtil.dp2px(8);
    private Drawable floatViewLastCache;
    private boolean isStick;
    private int mScreenWidth, mScreenHeight;
    private WindowManager mWindowManager;
    private Context mContext;
    private ViewGroup acrFloatView;
    private LinearLayout iconFloatView;
    public Handler mainHandler;
    private WindowManager.LayoutParams layoutParams;
    private float mTouchStartX, mTouchStartY;
    private int rotation;
    private boolean isMovingToEdge = false;
    private float density = 0;
    private boolean showBigBang = true;
    private boolean isMoving = false;
    private boolean isLongPressed = false;
    private int mScaledTouchSlop;
    private boolean isRemoved = false;
    private boolean isTempAdd = false;
    private List<ActionListener> mActionListener;
    private int mStatusBarHeight = 0;
    int[] icons;
    String[] contentDiscription;

    private static class InnerClass {
        private static MenuController instance = new MenuController(BaseApplication.getApplication());
    }

    public static MenuController getInstance() {
        return InnerClass.instance;
    }


    private MenuController(Context application) {
        mContext = application;
        mWindowManager = (WindowManager) application.getSystemService(Context.WINDOW_SERVICE);
        MAX_LENGTH = DEFAULT_MAX_LENGTH * SPHelper.getFloat(ConstantUtil.FLOATVIEW_SIZE, 100) / 100f;
        MIN_LENGTH = DEFAULT_MIN_LENGTH * SPHelper.getFloat(ConstantUtil.FLOATVIEW_SIZE, 100) / 100f;
        int resourceId = application.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            mStatusBarHeight = application.getResources().getDimensionPixelSize(resourceId);
        }
        mainHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                synchronized (MenuController.this) {
                    switch (msg.what) {
                        case MOVETOEDGE:
                            int desX = (int) msg.obj;
                            if (desX == 0) {
                                layoutParams.x = (int) (layoutParams.x - density * 10);
                                if (layoutParams.x < 0) {
                                    layoutParams.x = 0;
                                }
                            } else {
                                layoutParams.x = (int) (layoutParams.x + density * 10);
                                if (layoutParams.x > desX) {
                                    layoutParams.x = desX;
                                }
                            }
                            updateViewPosition(layoutParams.x, layoutParams.y);
                            if (layoutParams.x != desX) {
                                mainHandler.sendMessageDelayed(mainHandler.obtainMessage(MOVETOEDGE, desX), 10);
                            } else {
                                isMovingToEdge = false;
                                if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) {
                                    SPHelper.save(ConstantUtil.FLOAT_VIEW_PORT_X, layoutParams.x);
                                    SPHelper.save(ConstantUtil.FLOAT_VIEW_PORT_Y, layoutParams.y);
                                } else {
                                    SPHelper.save(ConstantUtil.FLOAT_VIEW_LAND_X, layoutParams.x);
                                    SPHelper.save(ConstantUtil.FLOAT_VIEW_LAND_Y, layoutParams.y);
                                }
                            }
                            break;
                    }
                }
            }
        };
        mActionListener = new ArrayList<>();
        mScaledTouchSlop = (int) (ViewUtil.dp2px(DEFAULT_MIN_WIDTH_HIDE) * SPHelper.getFloat(ConstantUtil.FLOATVIEW_SIZE, 100) / 100f);
        initView();
        applySizeChange();
        isRemoved = true;
    }


    private void initView() {
        icons = new int[]{R.drawable.email_icon, R.drawable.phone_icon, R.drawable.speech_icon};
        contentDiscription = new String[]{"服务", "服务", "服务"};
        showBigBang = SPHelper.getBoolean(ConstantUtil.TOTAL_SWITCH, true);
        isStick = SPHelper.getBoolean(ConstantUtil.FLOATVIEW_IS_STICK, false);
        Point point = new Point();
        mWindowManager.getDefaultDisplay().getSize(point);
        mScreenWidth = point.x;
        mScreenHeight = point.y;
        if (showBigBang) {
            mCurrentIconAlpha = SPHelper.getInt(ConstantUtil.FLOATVIEW_ALPHA, 70) / 100f;
        } else {
            mCurrentIconAlpha = 0.6f * SPHelper.getInt(ConstantUtil.FLOATVIEW_ALPHA, 70) / 100f;
        }
        iconFloatView = (LinearLayout) View.inflate(mContext, R.layout.arc_float_icon, null);
        floatImageView = ((ImageView) iconFloatView.findViewById(R.id.float_image));
        acrFloatView = (RelativeLayout) View.inflate(mContext, R.layout.arc_view_float, null);
        archMenu = (ArcMenu) acrFloatView.findViewById(R.id.arc_menu);
        archMenu.setOnModeSeletedListener(new ArcMenu.OnModeSeletedListener() {
            @Override
            public void onModeSelected() {
                showFloatImageView();
            }

            @Override
            public void onNothing() {

            }
        });
        // event listeners
        acrFloatView.setOnTouchListener(this);
        iconFloatView.setOnTouchListener(this);
    }

    private void initArcMenu(ArcMenu menu, int[] itemDrawables) {
        menu.removeAllItemViews();
        final int itemCount = itemDrawables.length;
        applySizeChange();
        if (archMenu != null) {
            /*收起  悬浮*/
            floatViewLastCache = mContext.getResources().getDrawable(R.drawable.float_view_bg);
            archMenu.getHintView().setBackgroundDrawable(floatViewLastCache);
            archMenu.getHintView().setAlpha(SPHelper.getInt(ConstantUtil.FLOATVIEW_ALPHA, 70) / 100f);
        }
        if (showBigBang) {
            mCurrentIconAlpha = SPHelper.getInt(ConstantUtil.FLOATVIEW_ALPHA, 70) / 100f;
        } else {
            mCurrentIconAlpha = 0.9f * SPHelper.getInt(ConstantUtil.FLOATVIEW_ALPHA, 70) / 100f;
        }
        for (int i = 0; i < itemCount; i++) {
            ImageView item = new ImageView(acrFloatView.getContext());
            item.setImageResource(itemDrawables[i]);
            item.setContentDescription(contentDiscription[i]);
            item.setPadding(arcMenupadding, arcMenupadding, arcMenupadding, arcMenupadding);
            item.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            if (i == 0) {
                item.setAlpha(mCurrentIconAlpha);
                if (showBigBang) {
                    item.setContentDescription(contentDiscription[i]);
                }
            } else {
                item.setAlpha(SPHelper.getInt(ConstantUtil.FLOATVIEW_ALPHA, 70) / 100f);
            }

            final int position = i;
            menu.addItem(item, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    T_.showToastReal("点击");
                    //showFuncation(position);
                    showFloatImageView();
                }
            });
        }
    }

    private void applySizeChange() {
        float persent = SPHelper.getFloat(ConstantUtil.FLOATVIEW_SIZE, 100.0f) / 100;
        padding = (int) (ViewUtil.dp2px(3) * persent);
        arcMenupadding = (int) (ViewUtil.dp2px(8) * persent);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) archMenu.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = ViewUtil.dp2px(MAX_LENGTH);
            layoutParams.height = ViewUtil.dp2px(MAX_LENGTH);
            archMenu.setLayoutParams(layoutParams);
        }
        archMenu.applySizeChange(persent);
    }

    private void showFuncation(int position) {
        switch (position) {
            case 0:
                showBigBang = !showBigBang;
                SPHelper.save(ConstantUtil.TOTAL_SWITCH, showBigBang);
                if (mActionListener != null) {
                    for (ActionListener listener : mActionListener) {
                        listener.isShow(showBigBang);
                    }
                }
                initArcMenu(archMenu, icons);
                break;
        }
    }

    private void showArcMenuView() {
        reuseSavedWindowMangerPosition(0, 0);
        removeAllView();
        mainHandler.post(new Runnable() {
            @SuppressLint("NewApi")
            @Override
            public void run() {
                synchronized (MenuController.this) {
                    try {
                        acrFloatView.setVisibility(View.VISIBLE);
                        acrFloatView.setOnTouchListener(MenuController.this);
                        int position = getArcPostion(layoutParams);
                        mWindowManager.addView(acrFloatView, layoutParams);
                        reMeasureHeight(position, layoutParams);
                        initArcMenu(archMenu, icons);
                        archMenu.refreshPathMenu(position);
                        mWindowManager.updateViewLayout(acrFloatView, layoutParams);
                        archMenu.performClickShowMenu(position);
                        isShowIcon = false;
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void reMeasureHeight(int position, WindowManager.LayoutParams layoutParams) {
        if (position == PathMenu.LEFT_CENTER || position == PathMenu.RIGHT_CENTER) {
            layoutParams.y = layoutParams.y - (ViewUtil.dp2px((MAX_LENGTH - MIN_LENGTH) / 2));
            Point point = new Point();
            mWindowManager.getDefaultDisplay().getSize(point);
            mScreenWidth = point.x;
            mScreenHeight = point.y;
            if (layoutParams.x > mScreenWidth / 2) {
                layoutParams.x = mScreenWidth;
            } else {
                layoutParams.x = 0;
            }

        }
    }

    @SuppressLint("NewApi")
    private void removeAllView() {
        if (acrFloatView == null)
            initView();
        if (mWindowManager == null)
            mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        try {
            mWindowManager.removeView(acrFloatView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mWindowManager.removeView(iconFloatView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (archMenu != null)
            archMenu.reset();
        if (acrFloatView != null) {
            acrFloatView.setVisibility(View.GONE);
            acrFloatView.setOnTouchListener(null);
        }
        if (iconFloatView != null) {
            iconFloatView.setVisibility(View.INVISIBLE);
            iconFloatView.setOnTouchListener(null);
        }


    }

    private int getArcPostion(WindowManager.LayoutParams layoutParams) {
        int wmX = layoutParams.x;
        int wmY = layoutParams.y;
        int position = PathMenu.RIGHT_CENTER;
        Point point = new Point();
        mWindowManager.getDefaultDisplay().getSize(point);
        mScreenWidth = point.x;
        mScreenHeight = point.y;
        mScreenHeight = mScreenHeight - mStatusBarHeight;
        if (wmX <= mScreenWidth / 3) //左边  竖区域
        {
            if (wmY <= ViewUtil.dp2px((MAX_LENGTH - MIN_LENGTH) / 2)) {
                position = PathMenu.LEFT_TOP;//左上
            } else if (wmY > mScreenHeight - ViewUtil.dp2px((MAX_LENGTH + MIN_LENGTH) / 2)) {
                position = PathMenu.LEFT_BOTTOM;//左下
            } else {
                position = PathMenu.LEFT_CENTER;//左中
            }
        } else if (wmX >= mScreenWidth * 2 / 3)//右边竖区域
        {
            if (wmY <= ViewUtil.dp2px((MAX_LENGTH - MIN_LENGTH) / 2)) {
                position = PathMenu.RIGHT_TOP;//左上
            } else if (wmY > mScreenHeight - ViewUtil.dp2px((MAX_LENGTH + MIN_LENGTH) / 2)) {
                position = PathMenu.RIGHT_BOTTOM;//左下
            } else {
                position = PathMenu.RIGHT_CENTER;//左中
            }
        }
        return position;
    }

    private void showFloatImageView() {
        if (layoutParams == null){
            reuseSavedWindowMangerPosition(0, 0);
        }
        showFloatIcon();
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (MenuController.this) {
                    reuseSavedWindowMangerPosition(ViewUtil.dp2px(MIN_LENGTH), ViewUtil.dp2px(MIN_LENGTH));
                    removeAllView();
                    try {
                        iconFloatView.setAlpha(mCurrentIconAlpha);
                        iconFloatView.setScaleX(1);
                        iconFloatView.setScaleY(1);
                        iconFloatView.setOnTouchListener(MenuController.this);
                        iconFloatView.setVisibility(View.VISIBLE);
                        mWindowManager.addView(iconFloatView, layoutParams);
                        isShowIcon = true;
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private void showFloatIcon() {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                floatViewLastCache = mContext.getResources().getDrawable(R.drawable.float_view_bg);
                floatImageView.setImageDrawable(floatViewLastCache);
                LinearLayout.LayoutParams floatImageViewlayoutParams = (LinearLayout.LayoutParams) floatImageView.getLayoutParams();
                floatImageViewlayoutParams.width = ViewUtil.dp2px(MIN_LENGTH);
                floatImageViewlayoutParams.height = ViewUtil.dp2px(MIN_LENGTH);
                floatImageView.setLayoutParams(floatImageViewlayoutParams);
                floatImageView.setPadding(padding, padding, padding, padding);
                reuseSavedWindowMangerPosition(ViewUtil.dp2px(MIN_LENGTH), ViewUtil.dp2px(MIN_LENGTH));
                try {
                    mWindowManager.updateViewLayout(iconFloatView, layoutParams);
                } catch (Throwable e) {
                    L_.d("shang", "showFloatIcon e=" + e);
                }
            }
        });

    }


    private void reuseSavedWindowMangerPosition(int width_vale, int height_value) {
        int w;
        int h;
        if (width_vale == 0 && height_value == 0) {
            //获取windowManager
            w = ViewGroup.LayoutParams.WRAP_CONTENT;
            h = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            //获取windowManager
            w = width_vale;
            h = height_value;
        }
        if (layoutParams == null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            mWindowManager.getDefaultDisplay().getMetrics(displayMetrics);
            density = displayMetrics.density;

            int flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
            int type = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(mContext)) {
                type = WindowManager.LayoutParams.TYPE_PHONE;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                type = WindowManager.LayoutParams.TYPE_TOAST;
            } else {
                type = WindowManager.LayoutParams.TYPE_PHONE;
            }
            Point point = new Point();
            mWindowManager.getDefaultDisplay().getSize(point);
            mScreenWidth = point.x;
            mScreenHeight = point.y;
            rotation = mWindowManager.getDefaultDisplay().getRotation();
            int x, y;
            if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) {
                x = SPHelper.getInt(ConstantUtil.FLOAT_VIEW_PORT_X, mScreenWidth);
                y = SPHelper.getInt(ConstantUtil.FLOAT_VIEW_PORT_Y, mScreenHeight / 2);
            } else {
                x = SPHelper.getInt(ConstantUtil.FLOAT_VIEW_LAND_X, mScreenWidth);
                y = SPHelper.getInt(ConstantUtil.FLOAT_VIEW_LAND_Y, mScreenHeight / 2);
            }
            layoutParams = new WindowManager.LayoutParams(w, h, type, flags, PixelFormat.TRANSLUCENT);
            layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
            layoutParams.x = x;
            layoutParams.y = y;
        } else {
            layoutParams.width = w;
            layoutParams.height = h;
        }

    }

    public void romove() {
        mainHandler.post(removeViewRunnanble);
    }

    public synchronized void show() {
        if (isRemoved) {
            showFloatImageView();
            isRemoved = false;
        }
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (MenuController.this) {
                    if (iconFloatView != null) {
                        iconFloatView.setVisibility(View.VISIBLE);
                        if (rotation != mWindowManager.getDefaultDisplay().getRotation()) {
                            moveToEdge();
                        }
                    }
                }
            }
        });
    }

    private synchronized void remove() {
        mainHandler.removeCallbacks(showViewRunnable);
        removeAllView();
        isRemoved = true;
    }


    private Runnable removeViewRunnanble = new Runnable() {
        @Override
        public void run() {
            synchronized (MenuController.this) {
                if (iconFloatView == null) {
                    return;
                }
                iconFloatView.setOnClickListener(null);
                iconFloatView.animate().alpha(0).scaleX(0).scaleY(0).setDuration(0).setInterpolator(new AnticipateOvershootInterpolator()).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (!isTempAdd) {
                            remove();
                            isTempAdd = false;
                            mainHandler.removeCallbacks(showViewRunnable);
                            mainHandler.removeCallbacks(removeViewRunnanble);
                            mainHandler.post(showViewRunnable);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();
            }
        }
    };
    private Runnable showViewRunnable = new Runnable() {
        @Override
        public void run() {
            if (!SPHelper.getBoolean(ConstantUtil.SHOW_FLOAT_VIEW, false))
                return;
            if (layoutParams == null)
                reuseSavedWindowMangerPosition(0, 0);
            if (isRemoved) {
                isRemoved = false;
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (MenuController.this) {
                            reuseSavedWindowMangerPosition(ViewUtil.dp2px(MIN_LENGTH), ViewUtil.dp2px(MIN_LENGTH));
                            removeAllView();
                            try {
                                iconFloatView.setAlpha(mCurrentIconAlpha);
                                iconFloatView.setScaleX(1);
                                iconFloatView.setScaleY(1);
                                iconFloatView.setOnTouchListener(MenuController.this);
                                iconFloatView.setVisibility(View.VISIBLE);
                                mWindowManager.addView(iconFloatView, layoutParams);
                                isShowIcon = true;
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }
    };

    /**
     * touch the outside of the content view, remove the popped view
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!isShowIcon) {
            showFloatImageView();
            return false;
        }
        if (isMovingToEdge) {
            return true;
        }
        float x = event.getRawX();
        float y = event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchStartX = x;
                mTouchStartY = y;
                isMoving = false;
                isLongPressed = false;
                if (isShowIcon)
                    mainHandler.postDelayed(longPressRunnable, 500);
                break;
            case MotionEvent.ACTION_MOVE:
                if (isMoving || Math.abs(x - mTouchStartX) > mScaledTouchSlop || Math.abs(y - mTouchStartY) > mScaledTouchSlop) {
                    isMoving = true;
                    if (layoutParams.x <= mScaledTouchSlop && x <= mTouchStartX) {
                    } else if (mScreenWidth - layoutParams.x - ViewUtil.dp2px(MIN_LENGTH) <= mScaledTouchSlop && x >= mTouchStartX) {
                    } else {
                        showFloatIcon();
                    }
                    mainHandler.removeCallbacks(longPressRunnable);
                }
                if (!isStick) {
                    updateViewPosition(x - iconFloatView.getWidth() / 2, y - iconFloatView.getHeight());
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isMoving || Math.abs(x - mTouchStartX) > mScaledTouchSlop || Math.abs(y - mTouchStartY) > mScaledTouchSlop) {
                    mainHandler.removeCallbacks(longPressRunnable);
                } else {
                    if (!isLongPressed) {
                        mainHandler.removeCallbacks(longPressRunnable);
                        if (!isMoving) {
                            showArcMenuView();
                        }
                    }
                }
                if (!isStick) {
                    updateViewPosition(x - iconFloatView.getWidth() / 2, y - iconFloatView.getHeight());
                }
                mTouchStartX = mTouchStartY = 0;
                if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) {
                    SPHelper.save(ConstantUtil.FLOAT_VIEW_PORT_X, layoutParams.x);
                    SPHelper.save(ConstantUtil.FLOAT_VIEW_PORT_Y, layoutParams.y);
                } else {
                    SPHelper.save(ConstantUtil.FLOAT_VIEW_LAND_X, layoutParams.x);
                    SPHelper.save(ConstantUtil.FLOAT_VIEW_LAND_Y, layoutParams.y);
                }
                moveToEdge();

            case MotionEvent.ACTION_OUTSIDE:
                if (!isShowIcon) {
                    showFloatImageView();
                }
                break;
        }
        return true;
    }


    private synchronized void updateViewPosition(float x, float y) {
        layoutParams.x = (int) (x);
        layoutParams.y = (int) (y);
        if (layoutParams.x < 0) {
            layoutParams.x = 0;
        }
        if (layoutParams.y < 0) {
            layoutParams.y = 0;
        }
        layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        try {
            mWindowManager.updateViewLayout(iconFloatView, layoutParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void moveToEdge() {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                isMovingToEdge = true;
                rotation = mWindowManager.getDefaultDisplay().getRotation();
                int x, y;
                Point point = new Point();
                mWindowManager.getDefaultDisplay().getSize(point);
                mScreenWidth = point.x;
                mScreenHeight = point.y;
                if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) {
                    x = SPHelper.getInt(ConstantUtil.FLOAT_VIEW_PORT_X, layoutParams.x);
                    y = SPHelper.getInt(ConstantUtil.FLOAT_VIEW_PORT_Y, layoutParams.y);
                } else {
                    x = SPHelper.getInt(ConstantUtil.FLOAT_VIEW_LAND_X, layoutParams.x);
                    y = SPHelper.getInt(ConstantUtil.FLOAT_VIEW_LAND_Y, layoutParams.y);
                }
                layoutParams.x = x;
                layoutParams.y = y;
                int desX = 0;
                if (layoutParams.x > mScreenWidth / 2) {
                    desX = mScreenWidth;
                } else {
                    desX = 0;
                }
                mainHandler.sendMessage(mainHandler.obtainMessage(MOVETOEDGE, desX));
            }
        });
    }


    public interface ActionListener {
        void isShow(boolean isShow);

        boolean longPressed();
    }

    private Runnable longPressRunnable = new Runnable() {
        @Override
        public void run() {
            isLongPressed = true;
            if (mActionListener != null) {
                Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(10);
                for (ActionListener listener : mActionListener) {
                    if (listener.longPressed()) {
                        break;
                    }
                }
            }
        }
    };

}
