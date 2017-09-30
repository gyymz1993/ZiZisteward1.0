//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ys;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.View;

public class FoucsView extends View {
    private int size;
    private int center_x;
    private int center_y;
    private int length;
    private Paint mPaint;

    public FoucsView(Context context, int size) {
        this(context);
        this.size = size;
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setDither(true);
        this.mPaint.setColor(-16724992);
        this.mPaint.setStrokeWidth(3.0F);
        this.mPaint.setStyle(Style.STROKE);
    }

    private FoucsView(Context context) {
        super(context);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.center_x = (int)((double)this.size / 2.0D);
        this.center_y = (int)((double)this.size / 2.0D);
        this.length = (int)((double)this.size / 2.0D) - 2;
        this.setMeasuredDimension(this.size, this.size);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect((float)(this.center_x - this.length), (float)(this.center_y - this.length), (float)(this.center_x + this.length), (float)(this.center_y + this.length), this.mPaint);
        canvas.drawLine(2.0F, (float)(this.getHeight() / 2), (float)(this.size / 10), (float)(this.getHeight() / 2), this.mPaint);
        canvas.drawLine((float)(this.getWidth() - 2), (float)(this.getHeight() / 2), (float)(this.getWidth() - this.size / 10), (float)(this.getHeight() / 2), this.mPaint);
        canvas.drawLine((float)(this.getWidth() / 2), 2.0F, (float)(this.getWidth() / 2), (float)(this.size / 10), this.mPaint);
        canvas.drawLine((float)(this.getWidth() / 2), (float)(this.getHeight() - 2), (float)(this.getWidth() / 2), (float)(this.getHeight() - this.size / 10), this.mPaint);
    }
}
