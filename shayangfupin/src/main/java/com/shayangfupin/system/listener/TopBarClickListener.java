package com.shayangfupin.system.listener;

/**
 * 顶部导航按钮监听器
 */


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.shayangfupin.system.R;
import com.shayangfupin.system.ui.activity.SearchHomeActivity;
import com.shayangfupin.system.ui.activity.SetActivity;
import com.shayangfupin.system.utlis.TopClickUtil;


public class TopBarClickListener implements OnClickListener{

	private Context ctx;
	public TopBarClickListener(Context ctx){
		this.ctx = ctx;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.btn_top_goback:
				((Activity) ctx).finish();
				break;
			case R.id.img_top_right_one:
				int rightOneTag = Integer.parseInt(v.getTag().toString());
				Intent searchIntent = new Intent(ctx, SearchHomeActivity.class);
				switch (rightOneTag){
					case TopClickUtil.TOP_NOTC_SEA:
						searchIntent.putExtra("type","notice");
						ctx.startActivity(searchIntent);
						break;
					case TopClickUtil.TOP_SET:
						Intent intent = new Intent(ctx, SetActivity.class);
						ctx.startActivity(intent);
						break;
				}
				break;
		}
	}

}
