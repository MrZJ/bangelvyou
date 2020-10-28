package com.shishoureport.system.wibget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.shishoureport.system.R;
import com.shishoureport.system.wibget.datepicker.NumericWheelAdapter;
import com.shishoureport.system.wibget.datepicker.OnWheelChangedListener;
import com.shishoureport.system.wibget.datepicker.WheelView;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/9/4.
 * copyright easybiz.
 */

public class YearMonthPickerDialog extends Dialog implements View.OnClickListener {
    public interface OnButtonClick {
        void onOkClick(String date, int year, int month);

        void onCancelClick();
    }

    private Context mContext;
    private Calendar c;
    private WheelView wv_year, wv_month;
    private Button ok, cancel;
    private TextView popTip;
    private int mYear;
    private int mMonth;
    private int startYear;
    private int startMonth;
    // 添加大小月月份并将其转换为list,方便之后的判断
    String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
    String[] months_little = {"4", "6", "9", "11"};
    List<String> list_big;
    List<String> list_little;
    private static int START_YEAR = 1895, END_YEAR = 2100;
    private String title;
    private OnButtonClick mClickListener;

    public YearMonthPickerDialog(@NonNull Context context, String title, OnButtonClick click) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = context;
        this.title = title;
        this.mClickListener = click;
        initView();
    }

    private void initView() {
        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        View popView = LayoutInflater.from(mContext).inflate(R.layout.pop_datepicker_year_month, null);
        setCanceledOnTouchOutside(false);
        popTip = (TextView) popView.findViewById(R.id.date_tip);
        wv_year = (WheelView) popView.findViewById(R.id.year);
        wv_month = (WheelView) popView.findViewById(R.id.month);
        ok = (Button) popView.findViewById(R.id.date_sure);
        cancel = (Button) popView.findViewById(R.id.date_cancel);
        list_big = Arrays.asList(months_big);
        list_little = Arrays.asList(months_little);
        int textSize = 0;
        textSize = adjustFontSize(getWindow().getWindowManager());
        // 年
        wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
        wv_year.setCyclic(false);// 可循环滚动
        wv_year.setLabel("年");// 添加文字
        wv_year.setCurrentItem(mYear - START_YEAR);// 初始化时显示的数据
        // 月
        wv_month.setAdapter(new NumericWheelAdapter(1, 12));
        wv_month.setCyclic(true);
        wv_month.setLabel("月");
        wv_month.setCurrentItem(mMonth);
        wv_year.addChangingListener(wheelListener_year);
        wv_month.addChangingListener(wheelListener_month);
        wv_month.TEXT_SIZE = textSize;
        wv_year.TEXT_SIZE = textSize;
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
        setContentView(popView);
    }

    /**
     * 日期选择添加年监听
     */
    OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            int year_num = newValue + START_YEAR;
            // 判断大小月及是否闰年,用来确定"日"的数据

        }
    };
    /**
     * 日期选择监听月大小
     */
    OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            int month_num = newValue + 1;
            // 判断大小月及是否闰年,用来确定"日"的数据

        }
    };

    /**
     * 日期选择框判断适配字体大小
     *
     * @param windowmanager
     * @return
     */
    public static int adjustFontSize(WindowManager windowmanager) {
        int screenWidth = windowmanager.getDefaultDisplay().getWidth();
        int screenHeight = windowmanager.getDefaultDisplay().getHeight();
        if (screenWidth <= 240) { // 240X320 屏幕
            return 10;
        } else if (screenWidth <= 320) { // 320X480 屏幕
            return 14;
        } else if (screenWidth <= 480) { // 480X800 或 480X854 屏幕
            return 18;
        } else if (screenWidth <= 540) { // 540X960 屏幕
            return 21;
        } else if (screenWidth <= 800) { // 800X1280 屏幕
            return 24;
        } else { // 大于 800X1280
            return 24;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date_cancel:
                dismiss();
                if (mClickListener != null) {
                    mClickListener.onCancelClick();
                }
                break;
            case R.id.date_sure:
                dismiss();
                if (mClickListener != null) {
                    String date = buffStartDateString();
                    mClickListener.onOkClick(date, startYear, startMonth);
                }
                break;
        }
    }

    /**
     * 整理拼接开始日期字符串
     */
    public String buffStartDateString() {
        StringBuffer startBuff = new StringBuffer();
        startYear = wv_year.getCurrentItem() + START_YEAR;
        startMonth = wv_month.getCurrentItem() + 1;
        startBuff.append(startYear + "年");
        startBuff.append(startMonth + "月");
        return startBuff.toString();
    }
}