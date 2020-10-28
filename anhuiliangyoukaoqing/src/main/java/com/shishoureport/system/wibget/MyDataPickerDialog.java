package com.shishoureport.system.wibget;

import android.app.Dialog;
import android.content.Context;
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

public class MyDataPickerDialog extends Dialog implements View.OnClickListener {
    public interface OnButtonClick {
        void onOkClick(String date, int type, int pos);

        void onCancelClick(int type, int pos);
    }

    public static final int TYPE_START_TIME = 0;
    public static final int TYPE_END_TIME = 1;
    public static final int TYPE_GO_TIME = 2;
    public static final int YEAR_MONTH = 101;
    public static final int YEAR_MONTH_HOUR_MINITE = 102;
    private Context mContext;
    private Calendar c;
    private WheelView wv_year, wv_month, wv_day, wv_hour, wv_min;
    private Button ok, cancel;
    private TextView popTip;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int startYear;
    private int startMonth;
    private int startDay;
    private int startHour;
    private int startMin;
    // 添加大小月月份并将其转换为list,方便之后的判断
    String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
    String[] months_little = {"4", "6", "9", "11"};
    List<String> list_big;
    List<String> list_little;
    private static int START_YEAR = 1895, END_YEAR = 2100;
    private String title;
    private OnButtonClick mClickListener;
    private int mType, showType;
    private int mPos;

    public MyDataPickerDialog(Context context, String title, OnButtonClick click, int type) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = context;
        this.title = title;
        this.mClickListener = click;
        mType = type;
        showType = YEAR_MONTH_HOUR_MINITE;
        initView();
    }

    public MyDataPickerDialog(int showType, Context context, String title, OnButtonClick click, int type) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = context;
        this.title = title;
        this.mClickListener = click;
        mType = type;
        this.showType = showType;
        initView();
    }

    public MyDataPickerDialog(Context context, String title, OnButtonClick click, int type, int pos) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = context;
        this.title = title;
        this.mClickListener = click;
        mType = type;
        mPos = pos;
        showType = YEAR_MONTH_HOUR_MINITE;
        initView();
    }


    private void initView() {
        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        View popView = LayoutInflater.from(mContext).inflate(R.layout.pop_datepicker, null);
        setCanceledOnTouchOutside(false);
        popTip = (TextView) popView.findViewById(R.id.date_tip);
        wv_year = (WheelView) popView.findViewById(R.id.year);
        wv_month = (WheelView) popView.findViewById(R.id.month);
        wv_day = (WheelView) popView.findViewById(R.id.day);
        wv_hour = (WheelView) popView.findViewById(R.id.hour);
        wv_min = (WheelView) popView.findViewById(R.id.min);
        ok = (Button) popView.findViewById(R.id.date_sure);
        cancel = (Button) popView.findViewById(R.id.date_cancel);
        if (YEAR_MONTH_HOUR_MINITE == showType) {
            wv_year.setVisibility(View.VISIBLE);
            wv_month.setVisibility(View.VISIBLE);
            wv_day.setVisibility(View.VISIBLE);
            wv_hour.setVisibility(View.VISIBLE);
            wv_min.setVisibility(View.VISIBLE);
        } else {
            wv_year.setVisibility(View.VISIBLE);
            wv_month.setVisibility(View.VISIBLE);
            wv_day.setVisibility(View.VISIBLE);
            wv_hour.setVisibility(View.GONE);
            wv_min.setVisibility(View.GONE);
        }
        popTip.setText(title);
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
        // 日
        wv_day.setCyclic(true);
        // 判断大小月及是否闰年,用来确定"日"的数据
        if (list_big.contains(String.valueOf(mMonth + 1))) {
            wv_day.setAdapter(new NumericWheelAdapter(1, 31));
        } else if (list_little.contains(String.valueOf(mMonth + 1))) {
            wv_day.setAdapter(new NumericWheelAdapter(1, 30));
        } else {
            // 闰年
            if ((mYear % 4 == 0 && mYear % 100 != 0) || mYear % 400 == 0)
                wv_day.setAdapter(new NumericWheelAdapter(1, 29));
            else
                wv_day.setAdapter(new NumericWheelAdapter(1, 28));
        }
        wv_day.setLabel("日");
        wv_day.setCurrentItem(mDay - 1);
        //时
        wv_hour.setAdapter(new NumericWheelAdapter(0, 23));
        wv_hour.setLabel("时");
        wv_hour.setCyclic(true);
        wv_hour.setCurrentItem(c.get(Calendar.HOUR_OF_DAY));
        //分
        wv_min.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
        wv_min.setLabel("分");
        wv_min.setCyclic(true);
        wv_min.setCurrentItem(c.get(Calendar.MINUTE));
        wv_year.addChangingListener(wheelListener_year);
        wv_month.addChangingListener(wheelListener_month);
        wv_day.TEXT_SIZE = textSize;
        wv_month.TEXT_SIZE = textSize;
        wv_year.TEXT_SIZE = textSize;
        wv_hour.TEXT_SIZE = textSize;
        wv_min.TEXT_SIZE = textSize;
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
        setContentView(popView);
    }

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
            return 18;
        } else if (screenWidth <= 320) { // 320X480 屏幕
            return 18;
        } else if (screenWidth <= 480) { // 480X800 或 480X854 屏幕
            return 24;
        } else if (screenWidth <= 540) { // 540X960 屏幕
            return 28;
        } else if (screenWidth <= 800) { // 800X1280 屏幕
            return 28;
        } else { // 大于 800X1280
            return 32;
        }
    }

    /**
     * 日期选择添加年监听
     */
    OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            int year_num = newValue + START_YEAR;
            // 判断大小月及是否闰年,用来确定"日"的数据
            if (list_big.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                wv_day.setAdapter(new NumericWheelAdapter(1, 31));
            } else if (list_little.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                wv_day.setAdapter(new NumericWheelAdapter(1, 30));
            } else {
                if ((year_num % 4 == 0 && year_num % 100 != 0) || year_num % 400 == 0)
                    wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                else
                    wv_day.setAdapter(new NumericWheelAdapter(1, 28));
            }
        }
    };

    /**
     * 日期选择监听月大小
     */
    OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            int month_num = newValue + 1;
            // 判断大小月及是否闰年,用来确定"日"的数据
            if (list_big.contains(String.valueOf(month_num))) {
                wv_day.setAdapter(new NumericWheelAdapter(1, 31));
            } else if (list_little.contains(String.valueOf(month_num))) {
                wv_day.setAdapter(new NumericWheelAdapter(1, 30));
            } else {
                if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
                        .getCurrentItem() + START_YEAR) % 100 != 0)
                        || (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
                    wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                else
                    wv_day.setAdapter(new NumericWheelAdapter(1, 28));
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date_cancel:
                dismiss();
                if (mClickListener != null) {
                    mClickListener.onCancelClick(mType, mPos);
                }
                break;
            case R.id.date_sure:
                dismiss();
                if (mClickListener != null) {
                    String date = buffStartDateString();
                    mClickListener.onOkClick(date, mType, mPos);
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
        startDay = wv_day.getCurrentItem() + 1;
        startHour = wv_hour.getCurrentItem();
        startMin = wv_min.getCurrentItem();
        startBuff.append(startYear + "-");
        if (wv_month.getCurrentItem() + 1 < 10) {
            startBuff.append("0" + startMonth + "-");
        } else {
            startBuff.append(startMonth + "-");
        }
        if (wv_day.getCurrentItem() + 1 < 10) {
            startBuff.append("0" + startDay + " ");
        } else {
            startBuff.append(startDay + " ");
        }
        if (YEAR_MONTH_HOUR_MINITE == showType) {
            if (wv_hour.getCurrentItem() < 10) {
                startBuff.append("0" + startHour + ":");
            } else {
                startBuff.append(startHour + ":");
            }
            if (wv_min.getCurrentItem() < 10) {
                startBuff.append("0" + startMin);
            } else {
                startBuff.append(startMin);
            }
        }
        return startBuff.toString();
    }
}
