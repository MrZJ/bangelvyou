package com.shishoureport.system.wibget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jianzhang.
 * on 2017/6/2.
 * copyright easybiz.
 */

public class TimeTextView extends TextView {
    public static final int TIME_START = 1000;
    SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");

    public TimeTextView(Context context) {
        super(context);
    }

    public TimeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 在控件被销毁时移除消息
        handler.removeMessages(0);
    }

    long Time;
    private boolean run = true; // 是否启动了
    @SuppressLint("NewApi")
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (run) {
                        long mTime = Time;
                        if (mTime > 0) {
                            String day = "";
                            TimeTextView.this.setText("倒计时    还有" + Time + "s");
                            Time = Time - 1000;
                            handler.sendEmptyMessageDelayed(TIME_START, 1000);
                        } else {
                            handler.removeMessages(TIME_START);
                            TimeTextView.this.setVisibility(View.GONE);
                        }
                    } else {
                        TimeTextView.this.setVisibility(View.GONE);
                        handler.removeMessages(TIME_START);
                    }
            }
        }
    };


    @SuppressLint("NewApi")
    public void setTimes(long mT) {
        // 标示已经启动
        Date date = new Date();
        long t2 = date.getTime();
        Time = mT - t2;
        date = null;

        if (Time > 0) {
            handler.removeMessages(TIME_START);
            handler.sendEmptyMessage(TIME_START);
        } else {
            TimeTextView.this.setVisibility(View.GONE);
        }
    }

    public void stop() {
        run = false;
    }
}