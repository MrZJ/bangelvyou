package com.zycreview.system.listener;

/**
 * 顶部导航按钮监听器
 */


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.zycreview.system.R;
import com.zycreview.system.ui.activity.CheckManageActivity;
import com.zycreview.system.ui.activity.FertilizerOrPesticideActivity;
import com.zycreview.system.ui.activity.InstockDetailActivity;
import com.zycreview.system.ui.activity.PlantManageActivity;
import com.zycreview.system.ui.activity.PlantTaskAddActivity;
import com.zycreview.system.ui.activity.SearchNoticeAndPolicyActivity;
import com.zycreview.system.ui.activity.TradedDetailOrChangeStateActivity;
import com.zycreview.system.ui.activity.TradingAddActivity;
import com.zycreview.system.ui.activity.TradingManageActivity;
import com.zycreview.system.utils.TopClickUtil;


public class TopBarClickListener implements OnClickListener{

	private Context ctx;
	public TopBarClickListener(Context ctx){
		this.ctx = ctx;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.btn_top_goback:
				if (v.getTag() != null) {
					int backTag = Integer.parseInt(v.getTag().toString());
					switch (backTag) {
						case TopClickUtil.TOP_PLANT_BACK:
							((PlantManageActivity) ctx).finish();
							break;
						case TopClickUtil.TOP_CHECK_BACK:
							((CheckManageActivity) ctx).finish();
							break;
					}
				} else {
					((Activity) ctx).finish();
				}
				break;
			case R.id.img_top_right_one:
				int rightOneTag = Integer.parseInt(v.getTag().toString());
				Intent searchIntent = new Intent(ctx, SearchNoticeAndPolicyActivity.class);
				switch (rightOneTag){
					case TopClickUtil.TOP_NOTC_SEA:
						searchIntent.putExtra("type","notice");
						ctx.startActivity(searchIntent);
						break;
					case TopClickUtil.TOP_POLI_SEA:
						searchIntent.putExtra("type","policy");
						ctx.startActivity(searchIntent);
						break;
				}
		        break;
			case R.id.tv_top_right_text:
				switch ((Integer) v.getTag()){
					case TopClickUtil.TOP_TRADING:
						ctx.startActivity(new Intent(ctx, TradingManageActivity.class));
						break;
					case TopClickUtil.TOP_ADD_FERTI:
						((FertilizerOrPesticideActivity)ctx).fertilizerIntent();
						break;
					case TopClickUtil.TOP_ADD_PESTI:
						((FertilizerOrPesticideActivity)ctx).pesticideIntent();
						break;
					case TopClickUtil.TOP_ADD_PLANT:
						ctx.startActivity(new Intent(ctx, PlantTaskAddActivity.class));
						break;
					case TopClickUtil.TOP_INSTOCK:
						((InstockDetailActivity)ctx).updateInstock();
						break;
					case TopClickUtil.TOP_ADD_TRADING:
						((TradingManageActivity)ctx).addTrading();
						break;
					case TopClickUtil.TOP_UPDATE_STATE:
						((TradedDetailOrChangeStateActivity)ctx).updateTraded();
						break;
					case TopClickUtil.TOP_UPDATE_TRADING:
						((TradingAddActivity)ctx).upTradingInfo();
						break;
				}
				break;
		}
	}

}
