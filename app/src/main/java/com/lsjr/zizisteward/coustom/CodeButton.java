package com.lsjr.zizisteward.coustom;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;

/**
 * Created by admin on 2017/5/20.
 */

public class CodeButton  extends CountDownTimer {

    private Button mBtnCode;
    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public CodeButton(Button button,long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mBtnCode=button;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onTick(long millisUntilFinished) {
        mBtnCode.setClickable(false);
        mBtnCode.setText(millisUntilFinished/1000+"s");

        SpannableString spannableString=new SpannableString(mBtnCode.getText().toString());
        ForegroundColorSpan span=new ForegroundColorSpan(Color.WHITE);
        spannableString.setSpan(span,0,2, Spanned.SPAN_INCLUSIVE_INCLUSIVE); //时间设为红色
        mBtnCode.setText(spannableString);
    }

    @Override
    public void onFinish() {
        mBtnCode.setText("获取验证码");
        mBtnCode.setClickable(true);
    }
}
