package com.shishoureport.system.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.shishoureport.system.ui.activity.LoginActivity;
import com.shishoureport.system.ui.activity.MainActivity;
import com.shishoureport.system.utils.MySharepreference;
import com.shishoureport.system.utils.StringUtil;
import com.umeng.message.UTrack;
import com.umeng.message.common.UmLog;
import com.umeng.message.entity.UMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mitic_xue on 16/10/26.
 */
public class NotificationBroadcast extends BroadcastReceiver {
    public static final String EXTRA_KEY_ACTION = "ACTION";
    public static final String EXTRA_KEY_MSG = "MSG";
    public static final int ACTION_CLICK = 10;
    public static final int ACTION_DISMISS = 11;
    public static final int EXTRA_ACTION_NOT_EXIST = -1;
    private static final String TAG = NotificationBroadcast.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra(EXTRA_KEY_MSG);
        int action = intent.getIntExtra(EXTRA_KEY_ACTION,
                EXTRA_ACTION_NOT_EXIST);
        Log.e("NotificationBroadcast", "message=" + message);
        try {
            UMessage msg = null;
            if (!StringUtil.isEmpty(message)) {
                msg = new UMessage(new JSONObject(message));
            }
            switch (action) {
                case ACTION_DISMISS:
                    UmLog.d(TAG, "dismiss notification");
                    UTrack.getInstance(context).setClearPrevMessage(true);
                    if (msg!=null){
                        UTrack.getInstance(context).trackMsgDismissed(msg);
                    }
                    break;
                case ACTION_CLICK:
                    UmLog.d(TAG, "click notification");
                    if (MySharepreference.getInstance(context.getApplicationContext()).isLogin()) {
                        Intent pustIntent = new Intent(context.getApplicationContext(), MainActivity.class);
                        pustIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(pustIntent);
                    } else {
                        Intent pustIntent = new Intent(context.getApplicationContext(), LoginActivity.class);
                        pustIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(pustIntent);
                    }
                    UTrack.getInstance(context).setClearPrevMessage(true);
                    MyNotificationService.oldMessage = null;
                    if (msg!=null){
                        UTrack.getInstance(context).trackMsgClick(msg);
                    }
                    break;
            }
            //
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
