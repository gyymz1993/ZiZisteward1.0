//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ys;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.view.View;

public class TypeButton extends View {
    public static final int TYPE_CANCEL = 1;
    public static final int TYPE_CONFIRM = 2;
    private int button_type;
    private int button_size;
    private float center_X;
    private float center_Y;
    private float button_radius;
    private Paint mPaint;
    private Path path;
    private float strokeWidth;
    private float index;
    private RectF rectF;

    public TypeButton(Context context) {
        super(context);
    }

    public TypeButton(Context context, int type, int size) {
        super(context);
        this.button_type = type;
        this.button_size = size;
        this.button_radius = (float)size / 2.0F;
        this.center_X = (float)size / 2.0F;
        this.center_Y = (float)size / 2.0F;
        this.mPaint = new Paint();
        this.path = new Path();
        this.strokeWidth = (float)size / 50.0F;
        this.index = (float)this.button_size / 12.0F;
        this.rectF = new RectF(this.center_X, this.center_Y - this.index, this.center_X + this.index * 2.0F, this.center_Y + this.index);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.setMeasuredDimension(this.button_size, this.button_size);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(this.button_type == 1) {
            this.mPaint.setAntiAlias(true);
            this.mPaint.setColor(-288568116);
            this.mPaint.setStyle(Style.FILL);
            canvas.drawCircle(this.center_X, this.center_Y, this.button_radius, this.mPaint);
            this.mPaint.setColor(-16777216);
            this.mPaint.setStyle(Style.STROKE);
            this.mPaint.setStrokeWidth(this.strokeWidth);
            this.path.moveTo(this.center_X - this.index / 7.0F, this.center_Y + this.index);
            this.path.lineTo(this.center_X + this.index, this.center_Y + this.index);
            this.path.arcTo(this.rectF, 90.0F, -180.0F);
            this.path.lineTo(this.center_X - this.index, this.center_Y - this.index);
            canvas.drawPath(this.path, this.mPaint);
            this.mPaint.setStyle(Style.FILL);
            this.path.reset();
            this.path.moveTo(this.center_X - this.index, (float)((double)this.center_Y - (double)this.index * 1.5D));
            this.path.lineTo(this.center_X - this.index, (float)((double)this.center_Y - (double)this.index / 2.3D));
            this.path.lineTo((float)((double)this.center_X - (double)this.index * 1.6D), this.center_Y - this.index);
            this.path.close();
            canvas.drawPath(this.path, this.mPaint);
        }

        if(this.button_type == 2) {
            this.mPaint.setAntiAlias(true);
            this.mPaint.setColor(-1);
            this.mPaint.setStyle(Style.FILL);
            canvas.drawCircle(this.center_X, this.center_Y, this.button_radius, this.mPaint);
            this.mPaint.setAntiAlias(true);
            this.mPaint.setStyle(Style.STROKE);
            this.mPaint.setColor(-16724992);
            this.mPaint.setStrokeWidth(this.strokeWidth);
            this.path.moveTo(this.center_X - (float)this.button_size / 6.0F, this.center_Y);
            this.path.lineTo(this.center_X - (float)this.button_size / 21.2F, this.center_Y + (float)this.button_size / 7.7F);
            this.path.lineTo(this.center_X + (float)this.button_size / 4.0F, this.center_Y - (float)this.button_size / 8.5F);
            this.path.lineTo(this.center_X - (float)this.button_size / 21.2F, this.center_Y + (float)this.button_size / 9.4F);
            this.path.close();
            canvas.drawPath(this.path, this.mPaint);
        }

    }
}
