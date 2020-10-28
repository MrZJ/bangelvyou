package com.shenmai.system.listener;

/**
 * 顶部导航按钮监听器
 */


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.shenmai.system.R;
import com.shenmai.system.ui.activity.BankSetActivity;
import com.shenmai.system.ui.activity.EditAddressActivity;
import com.shenmai.system.ui.activity.LoginActivity;
import com.shenmai.system.ui.activity.RegisterActivity;
import com.shenmai.system.ui.activity.SearchActivity;
import com.shenmai.system.ui.activity.TouxiangActivity;
import com.shenmai.system.ui.activity.UpdatePasswordActivity;
import com.shenmai.system.utlis.AppManager;
import com.shenmai.system.utlis.ConfigUtil;
import com.shenmai.system.utlis.TopClickUtil;


public class TopBarClickListener implements OnClickListener {

    private Context ctx;
    private float x = 0;
    private float y = 0;

    public TopBarClickListener(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_top_goback:
                ((Activity) ctx).finish();
                break;
            case R.id.tv_top_right_text:
                int rightTag = Integer.parseInt(v.getTag().toString());
                switch (rightTag) {
                    case TopClickUtil.TOP_REG:
                        if (AppManager.getInstance().getPreviousActivity() instanceof RegisterActivity) {
                            ((Activity) ctx).finish();
                        } else {
                            Intent regIntent = new Intent(ctx, RegisterActivity.class);
                            ctx.startActivity(regIntent);
                        }
                        break;
                    case TopClickUtil.TOP_LOG:
                        if (AppManager.getInstance().getPreviousActivity() instanceof LoginActivity) {
                            ((Activity) ctx).finish();
                        } else {
                            Intent logIntent = new Intent(ctx, LoginActivity.class);
                            ctx.startActivity(logIntent);
                        }
                        break;
                    case TopClickUtil.TOP_SAVE_TOUXIANGANDNAME:
                        ((TouxiangActivity) ctx).saveAndUploading();
                        break;
                    case TopClickUtil.TOP_SAVE_ADDRESS:
                        ((EditAddressActivity) ctx).saveAddress();
                        break;
                    case TopClickUtil.TOP_RESET:
                        ConfigUtil.clearConfig(ctx);
                        Intent logIntent = new Intent(ctx, LoginActivity.class);
                        ctx.startActivity(logIntent);
                        ((Activity) ctx).finish();
                        break;
                    case TopClickUtil.TOP_UPDATE_PASSWORD:
                        ((UpdatePasswordActivity) ctx).updatePassword();
                        break;
                    case TopClickUtil.TOP_SET_BANK:
                        ((BankSetActivity) ctx).setBankInfo();
                        break;
                }
            case R.id.img_top_right_one:
                int rightImgTag = Integer.parseInt(v.getTag().toString());
                switch (rightImgTag) {
                    case TopClickUtil.TOP_SEARCH:
                        Intent logIntent = new Intent(ctx, SearchActivity.class);
                        ctx.startActivity(logIntent);
                        break;
                    case TopClickUtil.TOP_WEB_CLOSE:
                        ((Activity) ctx).finish();
                        break;
                }
                break;
        }
    }

}
