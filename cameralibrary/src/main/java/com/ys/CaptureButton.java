//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ys;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import com.cjt2325.cameralibrary.lisenter.CaptureLisenter;
import com.cjt2325.cameralibrary.util.CheckPermission;

public class CaptureButton extends View {
    private int button_state;
    public static final int STATE_NULL = 0;
    public static final int STATE_UNPRESS_CLICK = 2;
    public static final int STATE_PRESS_CLICK = 1;
    public static final int STATE_PRESS_LONG_CLICK = 3;
    public static final int STATE_UNPRESS_LONG_CLICK = 4;
    private CaptureButton.LongPressRunnable longPressRunnable;
    private CaptureButton.RecordRunnable recordRunnable;
    @SuppressLint("NewApi")
    private ValueAnimator record_anim = ValueAnimator.ofFloat(new float[]{0.0F, 362.0F});
    private int state;
    private Paint mPaint;
    private float strokeWidth;
    private int outside_add_size;
    private int inside_reduce_size;
    private float center_X;
    private float center_Y;
    private float button_radius;
    private float button_outside_radius;
    private float button_inside_radius;
    private int button_size;
    private float progress;
    private RectF rectF;
    private int duration;
    private CaptureLisenter captureLisenter;
    float event_Y;
    private boolean isRecorder = false;

    public CaptureButton(Context context) {
        super(context);
    }

    public CaptureButton(Context context, int size) {
        super(context);
        this.button_size = size;
        this.button_radius = (float)size / 2.0F;
        this.button_outside_radius = this.button_radius;
        this.button_inside_radius = this.button_radius * 0.75F;
        this.strokeWidth = (float)(size / 15);
        this.outside_add_size = size / 5;
        this.inside_reduce_size = size / 8;
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.progress = 0.0F;
        this.longPressRunnable = new CaptureButton.LongPressRunnable(null);
        this.recordRunnable = new CaptureButton.RecordRunnable(null);
        this.state = 0;
        this.button_state = 259;
        this.duration = 10000;
        this.center_X = (float)((this.button_size + this.outside_add_size * 2) / 2);
        this.center_Y = (float)((this.button_size + this.outside_add_size * 2) / 2);
        this.rectF = new RectF(this.center_X - (this.button_radius + (float)this.outside_add_size - this.strokeWidth / 2.0F), this.center_Y - (this.button_radius + (float)this.outside_add_size - this.strokeWidth / 2.0F), this.center_X + (this.button_radius + (float)this.outside_add_size - this.strokeWidth / 2.0F), this.center_Y + (this.button_radius + (float)this.outside_add_size - this.strokeWidth / 2.0F));
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.setMeasuredDimension(this.button_size + this.outside_add_size * 2, this.button_size + this.outside_add_size * 2);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setColor(-288568116);
        canvas.drawCircle(this.center_X, this.center_Y, this.button_outside_radius, this.mPaint);
        this.mPaint.setColor(-1);
        canvas.drawCircle(this.center_X, this.center_Y, this.button_inside_radius, this.mPaint);
        if(this.state == 3) {
            this.mPaint.setAntiAlias(true);
            this.mPaint.setColor(-1728001024);
            this.mPaint.setStyle(Style.STROKE);
            this.mPaint.setStrokeWidth(this.strokeWidth);
            canvas.drawArc(this.rectF, -90.0F, this.progress, false, this.mPaint);
        }

    }

    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
        case 0:
            if(event.getPointerCount() <= 1) {
                this.event_Y = event.getY();
                this.state = 1;
                if(!this.isRecorder && (this.button_state == 258 || this.button_state == 259)) {
                    this.postDelayed(this.longPressRunnable, 500L);
                }
            }
            break;
        case 1:
            this.handlerUnpressByState();
            break;
        case 2:
            if(this.captureLisenter != null && this.state == 3 && (this.button_state == 258 || this.button_state == 259)) {
                this.captureLisenter.recordZoom(this.event_Y - event.getY());
            }
        }

        return true;
    }

    private void handlerUnpressByState() {
        this.removeCallbacks(this.longPressRunnable);
        switch(this.state) {
        case 1:
            if(this.captureLisenter != null && (this.button_state == 257 || this.button_state == 259)) {
                this.captureLisenter.takePictures();
            }
            break;
        case 3:
            this.state = 4;
            this.removeCallbacks(this.recordRunnable);
            this.recordEnd(false);
        }

        this.state = 0;
    }

    public void isRecord(boolean record) {
        this.isRecorder = record;
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if(hasWindowFocus) {
            this.handlerUnpressByState();
        }

    }

    @SuppressLint("NewApi")
    private void recordEnd(boolean finish) {
        this.state = 4;
        if(this.captureLisenter != null) {
            if(this.record_anim.getCurrentPlayTime() < 1500L && !finish) {
                this.captureLisenter.recordShort(this.record_anim.getCurrentPlayTime());
            } else if(finish) {
                this.captureLisenter.recordEnd((long)this.duration);
            } else {
                this.captureLisenter.recordEnd(this.record_anim.getCurrentPlayTime());
            }
        }

        this.resetRecordAnim();
    }

    @SuppressLint("NewApi")
    private void resetRecordAnim() {
        this.record_anim.cancel();
        this.progress = 0.0F;
        this.invalidate();
        this.startAnimation(this.button_outside_radius, this.button_radius, this.button_inside_radius, this.button_radius * 0.75F);
    }

    @SuppressLint("NewApi")
    private void startAnimation(float outside_start, float outside_end, float inside_start, float inside_end) {
        ValueAnimator outside_anim = ValueAnimator.ofFloat(new float[]{outside_start, outside_end});
        ValueAnimator inside_anim = ValueAnimator.ofFloat(new float[]{inside_start, inside_end});
        outside_anim.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                CaptureButton.this.button_outside_radius = ((Float)animation.getAnimatedValue()).floatValue();
                CaptureButton.this.invalidate();
            }
        });
        inside_anim.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                CaptureButton.this.button_inside_radius = ((Float)animation.getAnimatedValue()).floatValue();
                CaptureButton.this.invalidate();
            }
        });
        outside_anim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(CaptureButton.this.state == 3) {
                    if(CaptureButton.this.captureLisenter != null) {
                        CaptureButton.this.captureLisenter.recordStart();
                    }

                    CaptureButton.this.post(CaptureButton.this.recordRunnable);
                }

            }
        });
        outside_anim.setDuration(100L);
        inside_anim.setDuration(100L);
        outside_anim.start();
        inside_anim.start();
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setCaptureLisenter(CaptureLisenter captureLisenter) {
        this.captureLisenter = captureLisenter;
    }

    public void setButtonFeatures(int state) {
        this.button_state = state;
    }

    private class RecordRunnable implements Runnable {
        private RecordRunnable(Object o) {
        }

        @SuppressLint("NewApi")
        public void run() {
            CaptureButton.this.record_anim.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    if(CaptureButton.this.state == 3) {
                        CaptureButton.this.progress = ((Float)animation.getAnimatedValue()).floatValue();
                    }

                    CaptureButton.this.invalidate();
                }
            });
            CaptureButton.this.record_anim.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if(CaptureButton.this.state == 3) {
                        CaptureButton.this.recordEnd(true);
                    }

                }
            });
            CaptureButton.this.record_anim.setInterpolator(new LinearInterpolator());
            CaptureButton.this.record_anim.setDuration((long)CaptureButton.this.duration);
            CaptureButton.this.record_anim.start();
        }
    }

    private class LongPressRunnable implements Runnable {
        private LongPressRunnable(Object o) {
        }

        public void run() {
            CaptureButton.this.state = 3;
            if(CheckPermission.getRecordState() != 1 && CaptureButton.this.captureLisenter != null) {
                CaptureButton.this.captureLisenter.recordError();
                CaptureButton.this.state = 0;
            } else {
                CaptureButton.this.startAnimation(CaptureButton.this.button_outside_radius, CaptureButton.this.button_outside_radius + (float)CaptureButton.this.outside_add_size, CaptureButton.this.button_inside_radius, CaptureButton.this.button_inside_radius - (float)CaptureButton.this.inside_reduce_size);
            }
        }
    }
}
