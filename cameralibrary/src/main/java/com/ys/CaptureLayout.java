//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ys;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

import com.cjt2325.cameralibrary.ReturnButton;
import com.cjt2325.cameralibrary.TypeButton;
import com.cjt2325.cameralibrary.lisenter.CaptureLisenter;
import com.cjt2325.cameralibrary.lisenter.ReturnLisenter;
import com.cjt2325.cameralibrary.lisenter.TypeLisenter;

public class CaptureLayout extends FrameLayout {
    private CaptureLisenter captureLisenter;
    private TypeLisenter typeLisenter;
    private ReturnLisenter returnLisenter;
    private CaptureButton btn_capture;
    private TypeButton btn_confirm;
    private TypeButton btn_cancel;
    private ReturnButton btn_return;
    private TextView txt_tip;
    private int layout_width;
    private int layout_height;
    private int button_size;
    private boolean isFirst;

    public void setTypeLisenter(TypeLisenter typeLisenter) {
        this.typeLisenter = typeLisenter;
    }

    public void setCaptureLisenter(CaptureLisenter captureLisenter) {
        this.captureLisenter = captureLisenter;
    }

    public void setReturnLisenter(ReturnLisenter returnLisenter) {
        this.returnLisenter = returnLisenter;
    }

    public CaptureLayout(Context context) {
        this(context, (AttributeSet)null);
    }

    public CaptureLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CaptureLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.isFirst = true;
        WindowManager manager = (WindowManager)context.getSystemService("window");
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        if(this.getResources().getConfiguration().orientation == 1) {
            this.layout_width = outMetrics.widthPixels;
        } else {
            this.layout_width = outMetrics.widthPixels / 2;
        }

        this.button_size = (int)((float)this.layout_width / 4.5F);
        this.layout_height = this.button_size + this.button_size / 5 * 2 + 100;
        this.initView();
        this.initEvent();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.setMeasuredDimension(this.layout_width, this.layout_height);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void initEvent() {
        this.btn_cancel.setVisibility(4);
        this.btn_confirm.setVisibility(4);
    }

    public void startTypeBtnAnimator() {
        this.btn_capture.setVisibility(4);
        this.btn_return.setVisibility(4);
        this.btn_cancel.setVisibility(0);
        this.btn_confirm.setVisibility(0);
        this.btn_cancel.setClickable(false);
        this.btn_confirm.setClickable(false);
        ObjectAnimator animator_cancel = ObjectAnimator.ofFloat(this.btn_cancel, "translationX", new float[]{(float)(this.layout_width / 4), 0.0F});
        animator_cancel.setDuration(200L);
        animator_cancel.start();
        ObjectAnimator animator_confirm = ObjectAnimator.ofFloat(this.btn_confirm, "translationX", new float[]{(float)(-this.layout_width / 4), 0.0F});
        animator_confirm.setDuration(200L);
        animator_confirm.start();
        animator_confirm.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                CaptureLayout.this.btn_cancel.setClickable(true);
                CaptureLayout.this.btn_confirm.setClickable(true);
            }
        });
    }

    public void startAlphaAnimation() {
        if(this.isFirst) {
            ObjectAnimator animator_txt_tip = ObjectAnimator.ofFloat(this.txt_tip, "alpha", new float[]{1.0F, 0.0F});
            animator_txt_tip.setDuration(500L);
            animator_txt_tip.start();
            this.isFirst = false;
        }

    }

    private void initView() {
        this.setWillNotDraw(false);
        this.btn_capture = new CaptureButton(this.getContext(), this.button_size);
        LayoutParams btn_capture_param = new LayoutParams(-1, -1);
        btn_capture_param.gravity = 17;
        this.btn_capture.setLayoutParams(btn_capture_param);
        this.btn_capture.setDuration(10000);
        this.btn_capture.setCaptureLisenter(new CaptureLisenter() {
            public void takePictures() {
                if(CaptureLayout.this.captureLisenter != null) {
                    CaptureLayout.this.captureLisenter.takePictures();
                }

            }

            public void recordShort(long time) {
                if(CaptureLayout.this.captureLisenter != null) {
                    CaptureLayout.this.captureLisenter.recordShort(time);
                }

                CaptureLayout.this.startAlphaAnimation();
            }

            public void recordStart() {
                if(CaptureLayout.this.captureLisenter != null) {
                    CaptureLayout.this.captureLisenter.recordStart();
                }

                CaptureLayout.this.startAlphaAnimation();
            }

            public void recordEnd(long time) {
                if(CaptureLayout.this.captureLisenter != null) {
                    CaptureLayout.this.captureLisenter.recordEnd(time);
                }

                CaptureLayout.this.startAlphaAnimation();
                CaptureLayout.this.startTypeBtnAnimator();
            }

            public void recordZoom(float zoom) {
                if(CaptureLayout.this.captureLisenter != null) {
                    CaptureLayout.this.captureLisenter.recordZoom(zoom);
                }

            }

            public void recordError() {
                if(CaptureLayout.this.captureLisenter != null) {
                    CaptureLayout.this.captureLisenter.recordError();
                }

            }
        });
        this.btn_cancel = new TypeButton(this.getContext(), 1, this.button_size);
        LayoutParams btn_cancel_param = new LayoutParams(-1, -1);
        btn_cancel_param.gravity = 16;
        btn_cancel_param.setMargins(this.layout_width / 4 - this.button_size / 2, 0, 0, 0);
        this.btn_cancel.setLayoutParams(btn_cancel_param);
        this.btn_cancel.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if(CaptureLayout.this.typeLisenter != null) {
                    CaptureLayout.this.typeLisenter.cancel();
                }

                CaptureLayout.this.startAlphaAnimation();
                CaptureLayout.this.btn_cancel.setVisibility(4);
                CaptureLayout.this.btn_confirm.setVisibility(4);
                CaptureLayout.this.btn_capture.setVisibility(0);
                CaptureLayout.this.btn_return.setVisibility(0);
            }
        });
        this.btn_confirm = new TypeButton(this.getContext(), 2, this.button_size);
        LayoutParams btn_confirm_param = new LayoutParams(-1, -1);
        btn_confirm_param.gravity = 21;
        btn_confirm_param.setMargins(0, 0, this.layout_width / 4 - this.button_size / 2, 0);
        this.btn_confirm.setLayoutParams(btn_confirm_param);
        this.btn_confirm.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if(CaptureLayout.this.typeLisenter != null) {
                    CaptureLayout.this.typeLisenter.confirm();
                }

                CaptureLayout.this.startAlphaAnimation();
                CaptureLayout.this.btn_cancel.setVisibility(4);
                CaptureLayout.this.btn_confirm.setVisibility(4);
                CaptureLayout.this.btn_capture.setVisibility(0);
                CaptureLayout.this.btn_return.setVisibility(0);
            }
        });
        this.btn_return = new ReturnButton(this.getContext(), this.button_size / 2);
        LayoutParams btn_return_param = new LayoutParams(-2, -2);
        btn_return_param.gravity = 16;
        btn_return_param.setMargins(this.layout_width / 6, 0, 0, 0);
        this.btn_return.setLayoutParams(btn_return_param);
        this.btn_return.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if(CaptureLayout.this.captureLisenter != null && CaptureLayout.this.returnLisenter != null) {
                    CaptureLayout.this.returnLisenter.onReturn();
                }

            }
        });
        this.txt_tip = new TextView(this.getContext());
        LayoutParams txt_param = new LayoutParams(-1, -2);
        txt_param.gravity = 1;
        txt_param.setMargins(0, 0, 0, 0);
        this.txt_tip.setText("轻触拍照，长按摄像");
        this.txt_tip.setTextColor(-1);
        this.txt_tip.setGravity(17);
        this.txt_tip.setLayoutParams(txt_param);
        this.addView(this.btn_capture);
        this.addView(this.btn_cancel);
        this.addView(this.btn_confirm);
        this.addView(this.btn_return);
        this.addView(this.txt_tip);
    }

    public void setTextWithAnimation(String tip) {
        this.txt_tip.setText(tip);
        ObjectAnimator animator_txt_tip = ObjectAnimator.ofFloat(this.txt_tip, "alpha", new float[]{0.0F, 1.0F, 1.0F, 0.0F});
        animator_txt_tip.setDuration(2500L);
        animator_txt_tip.start();
    }

    public void setDuration(int duration) {
        this.btn_capture.setDuration(duration);
    }

    public void isRecord(boolean record) {
        this.btn_capture.isRecord(record);
    }

    public void setButtonFeatures(int state) {
        this.btn_capture.setButtonFeatures(state);
    }
}
