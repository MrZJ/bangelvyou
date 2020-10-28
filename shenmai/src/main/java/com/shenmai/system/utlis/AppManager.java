package com.shenmai.system.utlis;

import android.app.Activity;

import com.shenmai.system.ui.activity.LoginActivity;
import com.shenmai.system.ui.activity.MainActivity;
import com.shenmai.system.ui.activity.NoticeActivity;
import com.shenmai.system.ui.activity.OrderWebActivity;

import java.util.ArrayList;

/**
 * Created by KevinLi on 2016/1/25.
 */
public class AppManager {
    private static AppManager instance = null;
    private ArrayList<Activity> activities;
    private Activity mainUIActivity;
    private AppManager() {
        activities = new ArrayList<Activity>();
    }

    public static synchronized AppManager getInstance() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    public Activity getCurrActivity() {
        if (activities.size() > 0) {
            return activities.get(0);
        } else {
            return mainUIActivity;
        }
    }

    public Activity getLastActivity() {
        if (activities.size() > 0) {
            return activities.get(activities.size() - 1);
        } else {
            return mainUIActivity;
        }
    }

    /**
     * 获取上一个activity
     *
     * @return
     */
    public Activity getPreviousActivity() {
        if (activities.size() > 1) {
            return activities.get(activities.size() - 2);
        } else {
            return mainUIActivity;
        }
    }

    public void PushActivity(Activity activity) {
        if (activity instanceof MainActivity) {
            this.mainUIActivity = activity;
        }
        if(!activities.contains(activity)){
            activities.add(activity);
        }
    }

    public void PopActivity() {
        if (activities.size() > 0) {
            activities.remove(activities.size() - 1);
            activities.add(mainUIActivity);
        }
    }

    /**跳转关闭除首页外的其他程序*/
    public void finishOtherActivity(){
        ArrayList<Activity> activitiesTemp = new ArrayList<Activity>(activities);
        for(Activity activity : activitiesTemp){
            if(activity instanceof MainActivity){
                continue;
            }
            if(null != activity){
                activity.finish();
            }
        }
    }

    /**跳转关闭除首页和提示用户界面外的其他程序*/
    public void finishMNOtherActivity(){
        ArrayList<Activity> activitiesTemp = new ArrayList<Activity>(activities);
        for(Activity activity : activitiesTemp){
            if(activity instanceof MainActivity){
                continue;
            }
            if(activity instanceof NoticeActivity){
                continue;
            }
            if(null != activity){
                activity.finish();
            }
        }
    }

    /**跳转关闭除登录界面其他程序*/
    public void finishLoginOtherActivity(){
        ArrayList<Activity> activitiesTemp = new ArrayList<Activity>(activities);
        for(Activity activity : activitiesTemp){
            if(activity instanceof LoginActivity){
                continue;
            }
            if(null != activity){
                activity.finish();
            }
        }
    }

    /**跳转关闭除主界面详情和我的订单界面 */
    public void finishOtherByMyOrderActivity(){
        ArrayList<Activity> activitiesTemp = new ArrayList<Activity>(activities);
        for(Activity activity : activitiesTemp){
            if(activity instanceof OrderWebActivity ||activity instanceof MainActivity){
                continue;
            }
            if(null != activity){
                activity.finish();
            }
        }
    }

    /**退出程序*/
    public void finishActivity(){
        ArrayList<Activity> activitiesTemp = new ArrayList<Activity>(activities);
        for(Activity activity : activitiesTemp){
            activity.finish();
        }
    }

}
