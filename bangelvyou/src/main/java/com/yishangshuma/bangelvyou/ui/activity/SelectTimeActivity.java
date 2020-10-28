package com.yishangshuma.bangelvyou.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.wibget.HotelDataSelectView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 酒店选择时间界面
 * Created by KevinLi on 2016/2/25.
 */
public class SelectTimeActivity extends BaseActivity implements HotelDataSelectView.OnDaySelectListener {
    private LinearLayout ll_select_time;
    private HotelDataSelectView dataSelectView;
    private String nowday;
    private long nd = 1000*24L*60L*60L;//一天的毫秒数
    private SimpleDateFormat simpleDateFormat, sd1, sd2;
    private String inday, outday;
    private View inView;//已经选择的日期view
    private int inPosition = 1;//已经选择的日期位置
    private String selectType;//选择类型1（只选择一个时间）2（选择2个时间）

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);
        selectType = getIntent().getExtras().getString("type");
        initView();
        initListener();
        setData();
    }

    private void initView() {
        initTopView();
        setLeftBackButton();
        setTitle("选择时间");
        ll_select_time = (LinearLayout) findViewById(R.id.ll_select_time);
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
    }

    private void setData(){
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        nowday = simpleDateFormat.format(new Date());
        sd1 = new SimpleDateFormat("yyyy");
        sd2 = new SimpleDateFormat("dd");
        List<String> listDate = getDateList();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        for(int i = 0; i < listDate.size(); i++){
            dataSelectView = new HotelDataSelectView(this);
            dataSelectView.setLayoutParams(params);
            Date date = null;
            try {
                date = simpleDateFormat.parse(listDate.get(i));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dataSelectView.setTheDay(date);
            dataSelectView.setOnDaySelectListener(this);
            ll_select_time.addView(dataSelectView);
        }
    }

    @Override
    public void onDaySelectListener(View view, String date, int position) {
        //若日历日期小于当前日期，或日历日期-当前日期超过三个月，则不能点击
        try {
            if(simpleDateFormat.parse(date).getTime() < simpleDateFormat.parse(nowday).getTime()){
                return;
            }
            long dayxc = (simpleDateFormat.parse(date).getTime() - simpleDateFormat.parse(nowday).getTime()) / nd;
            if(dayxc > 90){
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String dateDay = date.split("-")[2];
        if(Integer.parseInt(dateDay) < 10){
            dateDay = date.split("-")[2].replace("0", "");
        }
        TextView textDayView = (TextView) view.findViewById(R.id.tv_calendar_day);
        TextView textView = (TextView) view.findViewById(R.id.tv_calendar);
        view.setBackgroundResource(R.drawable.bg_select_time);
        textDayView.setTextColor(Color.WHITE);
        if(null == inday || "".equals(inday)){
            inday = date;
            inView = view;
            inPosition = position;
            if("1".equals(selectType)){
                textView.setText("进店");
                Intent intent = new Intent();
                intent.putExtra("inDate", inday);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }else{
                textView.setText("入住");
            }
        } else {
            if(inday.equals(date)){
                if((position + 1) % 7 == 0 || (position) % 7 == 0){
                    textDayView.setTextColor(getResources().getColor(R.color.date_select));
                } else {
                    textDayView.setTextColor(Color.BLACK);
                }
                view.setBackgroundColor(Color.WHITE);
                textView.setText("");
                inday = "";
                inView = null;
            }else{
                try {
                    if(simpleDateFormat.parse(date).getTime() < simpleDateFormat.parse(inday).getTime()){
                        textView.setText("入住");
                        inday = date;
                        if(inView != null){
                            inView.setBackgroundColor(Color.WHITE);
                            TextView tv = (TextView) inView.findViewById(R.id.tv_calendar_day);
                            if((inPosition + 1) % 7 == 0 || (inPosition) % 7 == 0){
                                tv.setTextColor(getResources().getColor(R.color.date_select));
                            } else {
                                tv.setTextColor(Color.BLACK);
                            }
                            ((TextView) inView.findViewById(R.id.tv_calendar)).setText("");
                        }
                        inView = view;
                        inPosition = position;
                        return;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                textView.setText("离开");
                outday = date;
                Intent intent = new Intent();
                intent.putExtra("inDate", inday);
                intent.putExtra("outDate", outday);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    }

    //根据当前日期，向后数三个月（若当前day不是1号，为满足至少90天，则需要向后数4个月）
    public List<String> getDateList(){
        List<String> list = new ArrayList<String>();
        Date date = new Date();
        int nowMon = date.getMonth() + 1;
        String yyyy = sd1.format(date);
        String dd = sd2.format(date);
        if(nowMon == 9){
            list.add(simpleDateFormat.format(date));
            list.add(yyyy + "-10-" + dd);
            list.add(yyyy + "-11-" + dd);
            if(!dd.equals("01")){
                list.add(yyyy + "-12-" + dd);
            }
        }else if(nowMon == 10){
            list.add(yyyy + "-10-" + dd);
            list.add(yyyy + "-11-" + dd);
            list.add(yyyy + "-12-" + dd);
            if(!dd.equals("01")){
                list.add((Integer.parseInt(yyyy) + 1) + "-01-" + dd);
            }
        }else if(nowMon == 11){
            list.add(yyyy + "-11-" + dd);
            list.add(yyyy + "-12-" + dd);
            list.add((Integer.parseInt(yyyy) + 1) + "-01-" + dd);
            if(!dd.equals("01")){
                list.add((Integer.parseInt(yyyy) + 1) + "-02-" + dd);
            }
        }else if(nowMon == 12){
            list.add(yyyy + "-12-" + dd);
            list.add((Integer.parseInt(yyyy) + 1) + "-01-" + dd);
            list.add((Integer.parseInt(yyyy) + 1) + "-02-" + dd);
            if(!dd.equals("01")){
                list.add((Integer.parseInt(yyyy) + 1) + "-03-" + dd);
            }
        }else{
            list.add(yyyy + "-" + getMon(nowMon) + "-" + dd);
            list.add(yyyy + "-" + getMon((nowMon + 1)) + "-" + dd);
            list.add(yyyy + "-" + getMon((nowMon + 2)) + "-" + dd);
            if(!dd.equals("01")){
                list.add(yyyy + "-" + getMon((nowMon + 3)) + "-" + dd);
            }
        }
        return list;
    }

    public String getMon(int mon){
        String month = "";
        if(mon < 10){
            month = "0" + mon;
        }else{
            month = "" + mon;
        }
        return month;
    }
}
