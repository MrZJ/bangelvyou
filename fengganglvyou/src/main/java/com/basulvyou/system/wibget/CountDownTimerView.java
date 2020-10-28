package com.basulvyou.system.wibget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.basulvyou.system.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 自定义倒计时控件
 * @author Administrator
 *
 */
public class CountDownTimerView extends LinearLayout {

	private TextView tv_day_decade;	 // 天，十位
	private TextView tv_day_unit;	 // 天，个位
	private TextView tv_hour_decade; // 小时，十位
	private TextView tv_hour_unit;	// 小时，个位
	private TextView tv_min_decade;	// 分钟，十位
	private TextView tv_min_unit;	// 分钟，个位
	private TextView tv_sec_decade;	// 秒，十位
	private TextView tv_sec_unit;	// 秒，个位
	private TextView tv_tip_day;// 提示天
	private TextView tv_tip_hour;//提示时
	private TextView tv_tip_min;//提示分
	private TextView canbuy;//购买按钮
	private boolean cantB;//不能买标识
	
	private Context context;
	private int day_decade;
	private int day_unit;
	private int hour_decade;
	private int hour_unit;
	private int min_decade;
	private int min_unit;
	private int sec_decade;
	private int sec_unit;
	// 计时器
	private Timer timer;

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			countDown();
		};
	};

	public CountDownTimerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.view_countdowntimer, this);

		tv_day_decade = (TextView) view.findViewById(R.id.tv_day_decade);
		tv_day_unit = (TextView) view.findViewById(R.id.tv_day_unit);
		tv_tip_day = (TextView) view.findViewById(R.id.tv_tip_day);
		tv_hour_decade = (TextView) view.findViewById(R.id.tv_hour_decade);
		tv_hour_unit = (TextView) view.findViewById(R.id.tv_hour_unit);
		tv_tip_hour = (TextView) view.findViewById(R.id.tv_tip_hour);
		tv_min_decade = (TextView) view.findViewById(R.id.tv_min_decade);
		tv_min_unit = (TextView) view.findViewById(R.id.tv_min_unit);
		tv_tip_min = (TextView) view.findViewById(R.id.tv_tip_min);
		tv_sec_decade = (TextView) view.findViewById(R.id.tv_sec_decade);
		tv_sec_unit = (TextView) view.findViewById(R.id.tv_sec_unit);
		
	}

	/**
	 * 设置时间文字颜色
	 */
	public void setDateTextColor(int color){
		tv_day_decade.setTextColor(color);
		tv_day_unit.setTextColor(color);
		tv_hour_decade.setTextColor(color);
		tv_hour_unit.setTextColor(color);
		tv_min_decade.setTextColor(color);
		tv_min_unit.setTextColor(color);
		tv_sec_decade.setTextColor(color);
		tv_sec_unit.setTextColor(color);
		
	}
	
	/**
	 * 设置提示文字颜色
	 */
	public void setTipTextColor(int color){
		tv_tip_day.setTextColor(color);
		tv_tip_hour.setTextColor(color);
		tv_tip_min.setTextColor(color);
		
	}
	
	/**
	 * 
	 * @Description: 开始计时
	 * @param
	 * @return void
	 * @throws
	 */
	public void start() {

		if (timer == null) {
			timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					handler.sendEmptyMessage(0);
				}
			}, 0, 1000);
		}
	}

	/**
	 * 
	 * @Description: 停止计时
	 * @param
	 * @return void
	 * @throws
	 */
	public void stop() {
		if (cantB) {
			canbuy.setClickable(true);
			canbuy.setText("立即购买");
		} else { 
			canbuy.setClickable(false);
			canbuy.setText("团购已结束");
		}
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	/**
	 * @throws Exception
	 * 
	 * @Description: 设置倒计时的时长
	 * @param
	 * @return void
	 * @throws
	 */
	public void setTime(int day,int hour, int min, int sec,boolean cantBuy ,TextView canbtn) {

		canbuy = canbtn;
		cantB = cantBuy;
		if (min >= 60 || sec >= 60 || hour < 0 || min < 0
				|| sec < 0) {
			return;
		}
		
		day_decade = day / 10;
		day_unit = day - day_decade * 10;
		
		hour_decade = hour / 10;
		hour_unit = hour - hour_decade * 10;

		min_decade = min / 10;
		min_unit = min - min_decade * 10;

		sec_decade = sec / 10;
		sec_unit = sec - sec_decade * 10;

		tv_day_decade.setText(day_decade + "");
		tv_day_unit.setText(day_unit + "");
		tv_hour_decade.setText(hour_decade + "");
		tv_hour_unit.setText(hour_unit + "");
		tv_min_decade.setText(min_decade + "");
		tv_min_unit.setText(min_unit + "");
		tv_sec_decade.setText(sec_decade + "");
		tv_sec_unit.setText(sec_unit + "");

	}

	/**
	 * 
	 * @Description: 倒计时
	 * @param
	 * @return boolean
	 * @throws
	 */
	private void countDown() {

		if (isCarry4Unit(tv_sec_unit)) {
			if (isCarry4Decade(tv_sec_decade)) {

				if (isCarry4Unit(tv_min_unit)) {
					if (isCarry4Decade(tv_min_decade)) {

						if (isCarry4Unit(tv_hour_unit)) {
							if (isCarry4Decade(tv_hour_decade)) {
								
								if (isCarry4Unit(tv_day_unit)) {
									if (isCarry4Decade(tv_day_decade)) {
										tv_day_decade.setText("0");
										tv_day_unit.setText("0");
										tv_hour_decade.setText("0");
										tv_hour_unit.setText("0");
										tv_min_decade.setText("0");
										tv_min_unit.setText("0");
										tv_sec_decade.setText("0");
										tv_sec_unit.setText("0");
										stop();
									}
								}
								
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * @Description: 变化十位，并判断是否需要进位
	 * @param
	 * @return boolean
	 * @throws
	 */
	private boolean isCarry4Decade(TextView tv) {

		int time = Integer.valueOf(tv.getText().toString());
		time = time - 1;
		if (time < 0) {
			if (tv.getId() == R.id.tv_hour_decade) {
				time = 2;
			}else{
				time = 5;				
			}
			tv.setText(time + "");
			return true;
		} else {
			tv.setText(time + "");
			return false;
		}

	}

	/**
	 * 
	 * @Description: 变化个位，并判断是否需要进位
	 * @param
	 * @return boolean
	 * @throws
	 */
	private boolean isCarry4Unit(TextView tv) {

		int time = Integer.valueOf(tv.getText().toString());
		time = time - 1;
		if (time < 0) {
			if (tv.getId() == R.id.tv_hour_unit) {
				time = 3;
			}else{
				time = 9;				
			}
			tv.setText(time + "");
			return true;
		} else {
			tv.setText(time + "");
			return false;
		}

	}
	
	/**
	 * 清空倒计时器
	 * 
	 */
	public void clearTimer(){
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}
}
