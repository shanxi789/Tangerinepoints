package com.mzth.tangerinepoints.util;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.mzth.tangerinepoints.R;


/**
 * Created by guoenbo on 17/1/6.
 */

public class CountDownTimerUtils extends CountDownTimer {
    private TextView mTextView;
    private Context context;
    private String str;
    private String content;
    private boolean isSend;

    /**
     * @param textView          The TextView
     *
     *
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receiver
     *                          {@link #onTick(long)} callbacks.
     */
    public CountDownTimerUtils(Context context, TextView textView, long millisInFuture, long countDownInterval, String content, String viewContent, boolean isSend) {
        super(millisInFuture, countDownInterval);
        this.mTextView = textView;
        this.context = context;
        this.str = content;
        this.content = viewContent;
        this.isSend = isSend;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mTextView.setClickable(false); //设置不可点击
        mTextView.setText(millisUntilFinished / 1000 + str);  //设置倒计时时间
        if(isSend){
            mTextView.setTextColor(Color.WHITE); //设置按钮为白色，这时是不能点击的
        }else{
            mTextView.setTextColor(Color.WHITE); //设置按钮为灰色，这时是不能点击的
        }


        /**
         * 超链接 URLSpan
         * 文字背景颜色 BackgroundColorSpan
         * 文字颜色 ForegroundColorSpan
         * 字体大小 AbsoluteSizeSpan
         * 粗体、斜体 StyleSpan
         * 删除线 StrikethroughSpan
         * 下划线 UnderlineSpan
         * 图片 ImageSpan
         * http://blog.csdn.net/ah200614435/article/details/7914459
         */
        SpannableString spannableString = new SpannableString(mTextView.getText().toString());  //获取按钮上的文字
        ForegroundColorSpan span;
        if(isSend){
            span = new ForegroundColorSpan(Color.WHITE);
        }else{
            span = new ForegroundColorSpan(Color.WHITE);
        }

        /**
         * public void setSpan(Object what, int start, int end, int flags) {
         * 主要是start跟end，start是起始位置,无论中英文，都算一个。
         * 从0开始计算起。end是结束位置，所以处理的文字，包含开始位置，但不包含结束位置。
         */
        spannableString.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//将倒计时的时间设置为红色
        mTextView.setText(spannableString);
    }

    @Override
    public void onFinish() {
        mTextView.setText(content);
        mTextView.setClickable(true);//重新获得点击
        if (isSend){
            mTextView.setTextColor(context.getResources().getColor(R.color.white));//还原背景色
        }else{
            mTextView.setTextColor(context.getResources().getColor(R.color.white));//还原背景色
        }

    }
}
