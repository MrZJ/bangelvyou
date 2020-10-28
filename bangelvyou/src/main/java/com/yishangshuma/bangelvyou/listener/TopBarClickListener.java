package com.yishangshuma.bangelvyou.listener;

/**
 * 顶部导航按钮监听器
 */


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.yishangshuma.bangelvyou.R;
import com.yishangshuma.bangelvyou.entity.ConfigEntity;
import com.yishangshuma.bangelvyou.ui.activity.AttractionReserveActivity;
import com.yishangshuma.bangelvyou.ui.activity.EditCommentActivity;
import com.yishangshuma.bangelvyou.ui.activity.LocationDetailActivity;
import com.yishangshuma.bangelvyou.ui.activity.LoginActivity;
import com.yishangshuma.bangelvyou.ui.activity.NoticeActivity;
import com.yishangshuma.bangelvyou.ui.activity.RegisterActivity;
import com.yishangshuma.bangelvyou.ui.activity.SearchHistoryActivity;
import com.yishangshuma.bangelvyou.ui.activity.SearchResultActivity;
import com.yishangshuma.bangelvyou.ui.activity.SetActivity;
import com.yishangshuma.bangelvyou.util.ConfigUtil;
import com.yishangshuma.bangelvyou.util.StringUtil;
import com.yishangshuma.bangelvyou.util.TopClickUtil;

public class TopBarClickListener implements OnClickListener , View.OnTouchListener{

	private Context ctx;
	private float x = 0;
	private float y = 0;
	public TopBarClickListener(Context ctx){
		this.ctx = ctx;
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_top_goback:
			int tag = Integer.parseInt(v.getTag().toString());
			switch(tag){
				case TopClickUtil.TOP_BACK:
					((Activity) ctx).finish();
					break;
				case TopClickUtil.TOP_SET:
					Intent intent = new Intent(ctx, SetActivity.class);
					ctx.startActivity(intent);
					break;
			}
			break;
		case R.id.top_title_search://goto搜索界面
			String keyWord = ((EditText) v).getText().toString().trim();
			if(!StringUtil.isEmpty(keyWord)){
				Intent searchResultIntent = new Intent(ctx, SearchResultActivity.class);
				searchResultIntent.putExtra("keyWord", keyWord);
				ConfigEntity configEntity = ConfigUtil.loadConfig(ctx);
				String hisStr = configEntity.searchHistory;
				if("".equals(hisStr)){
					configEntity.searchHistory = keyWord;
				}else{
					configEntity.searchHistory = hisStr+","+keyWord;
				}
				ConfigUtil.saveConfig(ctx,configEntity);
				((EditText) v).setText("");
				ctx.startActivity(searchResultIntent);
			}
			break;
		case R.id.home_top_title_search:
			Intent intent = new Intent(ctx, SearchHistoryActivity.class);
			ctx.startActivity(intent);
			break;
		case R.id.tv_top_right_text:
			int rightTag = Integer.parseInt(v.getTag().toString());
			switch(rightTag){
				case TopClickUtil.TOP_REG:
					Intent regIntent = new Intent(ctx, RegisterActivity.class);
					ctx.startActivity(regIntent);
					((Activity) ctx).finish();
					break;
				case TopClickUtil.TOP_LOG:
					Intent logIntent = new Intent(ctx, LoginActivity.class);
					ctx.startActivity(logIntent);
					((Activity) ctx).finish();
					break;
				case TopClickUtil.TOP_COM:
					((EditCommentActivity) ctx).commitComment();
					break;
			}
		   break;
		case R.id.img_top_right_one:
			int rightOneTag = Integer.parseInt(v.getTag().toString());
			switch(rightOneTag){
				case TopClickUtil.TOP_COL://虚拟商品详情页面收藏
					if((Activity) ctx instanceof LocationDetailActivity){
						((LocationDetailActivity) ctx).collect();
					} else if((Activity) ctx instanceof AttractionReserveActivity){
						((AttractionReserveActivity) ctx).collect();
					}
					break;
				case TopClickUtil.TOP_MES://个人中心消息
					Intent noticeIntent = new Intent(ctx, NoticeActivity.class);
					ctx.startActivity(noticeIntent);
					break;
			}
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent m) {
		final int action = MotionEventCompat.getActionMasked(m);
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				x=m.getX();
				y=m.getY();
				break;
			case MotionEvent.ACTION_UP:
				if ((Math.abs(m.getX()-x) < 10.0)&&Math.abs(m.getY()-y) < 10.0) {
					Intent intent = new Intent(ctx, SearchHistoryActivity.class);
					ctx.startActivity(intent);
				}
				break;
			default:
				break;
		}
		return true;
	}
}
