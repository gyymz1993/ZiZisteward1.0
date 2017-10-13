//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ys;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.view.View;

public class ReturnButton extends View {
    private int size;
    private int center_X;
    private int center_Y;
    private float strokeWidth;
    private Paint paint;
    Path path;

    public ReturnButton(Context context, int size) {
        this(context);
        this.size = size;
        this.center_X = size / 2;
        this.center_Y = size / 2;
        this.strokeWidth = (float)size / 15.0F;
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setColor(-1);
        this.paint.setStyle(Style.STROKE);
        this.paint.setStrokeWidth(this.strokeWidth);
        this.path = new Path();
    }

    public ReturnButton(Context context) {
        super(context);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.setMeasuredDimension(this.size, this.size / 2);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.path.moveTo(this.strokeWidth, this.strokeWidth / 2.0F);
        this.path.lineTo((float)this.center_X, (float)this.center_Y - this.strokeWidth / 2.0F);
        this.path.lineTo((float)this.size - this.strokeWidth, this.strokeWidth / 2.0F);
        canvas.drawPath(this.path, this.paint);
    }
}
