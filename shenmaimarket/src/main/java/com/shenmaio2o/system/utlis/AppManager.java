package com.shenmaio2o.system.utlis;

import android.app.Activity;

import com.shenmaio2o.system.ui.activity.MainActivity;

import java.util.ArrayList;

/**
 * 栈管理
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

    public void PushActivity(Activity activity) {
        if (activity instanceof MainActivity) {
            this.mainUIActivity = activity;
        }
        activities.add(activity);
    }

    public void PopActivity() {
        if (activities.size() > 0) {
            activities.remove(activities.size() - 1);
        }
    }

    /**跳转关闭除首页外的其他程序*/
    public void finishOtherActivity(){
        ArrayList<Activity> activitiesTemp = new ArrayList<Activity>(activities);
        for(Activity activity : activitiesTemp){
            if(activity instanceof MainActivity){
                continue;
            }
            activity.finish();
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
