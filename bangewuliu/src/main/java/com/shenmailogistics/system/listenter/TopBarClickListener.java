package com.shenmailogistics.system.listenter;

/**
 * 顶部导航按钮监听器
 */


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.shenmailogistics.system.R;


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
		}
	}

}
