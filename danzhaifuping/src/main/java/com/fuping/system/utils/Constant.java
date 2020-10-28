package com.fuping.system.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.fuping.system.R;
import com.fuping.system.entity.MyIntentEntity;
import com.fuping.system.ui.activity.DuChaActivity;
import com.fuping.system.ui.activity.HelpActivity;
import com.fuping.system.ui.activity.InspectionSelectActivity;
import com.fuping.system.ui.activity.LoginActivity;
import com.fuping.system.ui.activity.ModuleDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/10/11.
 * copyright easybiz.
 */

public class Constant {
    public static final String USER_TYPE_DUCHA_GUANLIYUAN = "10";//督察管理员
    public static final String USER_TYPE_DUCHA_LINGDAO = "9";//督察领导
    public static final String USER_TYPE_DUCHA_RENYUAN = "8";//督察人员
    public static final String USER_TYPE_XIAN_ADMIN = "7";//县管理员
    public static final String USER_TYPE_ZHEN_ADMIN = "6";//镇管理员
    public static final String USER_TYPE_CUN_ADMIN = "5";//村管理员
    public static final String USER_TYPE_BANGFU_DANWEI = "4";//帮扶单位
    public static final String USER_TYPE_BANGFU_REN = "3";//帮扶人
    public static final String USER_TYPE_PINGKUN_REN = "2";//贫困户
    public static final String USER_TYPE_XITONG_ADMIN = "1";//系统管理员

    public static boolean getInstractionPermission(String usertype) {
        if (Constant.USER_TYPE_XIAN_ADMIN.equals(usertype)
                || Constant.USER_TYPE_DUCHA_RENYUAN.equals(usertype)
                || Constant.USER_TYPE_DUCHA_LINGDAO.equals(usertype)
                || Constant.USER_TYPE_BANGFU_DANWEI.endsWith(usertype)
                || Constant.USER_TYPE_DUCHA_GUANLIYUAN.equals(usertype)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean getAddInstractionPermission(String usertype) {
        if (Constant.USER_TYPE_XIAN_ADMIN.equals(usertype)
                || Constant.USER_TYPE_ZHEN_ADMIN.equals(usertype)
                || Constant.USER_TYPE_CUN_ADMIN.equals(usertype)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean getReplyInstruction(String usertype) {
        if (Constant.USER_TYPE_BANGFU_DANWEI.equals(usertype)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean getReplyTaskProcess(String usertype) {
        if (Constant.USER_TYPE_BANGFU_DANWEI.equals(usertype)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean getPlanManagePermission(String usertype) {
        if (Constant.USER_TYPE_BANGFU_REN.equals(usertype)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean getBangfuPermission(String usertype) {
        if (Constant.USER_TYPE_XIAN_ADMIN.equals(usertype) || Constant.USER_TYPE_BANGFU_DANWEI.equals(usertype)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean getDuChaPermission(String usertype) {
        if (Constant.USER_TYPE_XIAN_ADMIN.equals(usertype)) {
            return true;
        } else {
            return false;
        }
    }

    public static int getProgressColor(Context context, String percent) {
        try {
            double per = Double.valueOf(percent);
            if (per >= 0 && per <= 25) {
                return context.getResources().getColor(R.color.red);
            } else if (per >= 26 && per <= 50) {
                return context.getResources().getColor(R.color.yellow);
            } else if (per >= 51 && per <= 75) {
                return context.getResources().getColor(R.color.light_blue);
            } else if (per >= 76 && per <= 99) {
                return context.getResources().getColor(R.color.light_green);
            } else if (per >= 100) {
                return context.getResources().getColor(R.color.deep_blue);
            }
        } catch (Exception e) {

        }
        return context.getResources().getColor(R.color.black);
    }

    public static Drawable getProgressDrawble(Context context, int per) {
        try {
            if (per >= 0 && per <= 25) {
                return context.getResources().getDrawable(R.drawable.progressbar_25);
            } else if (per >= 26 && per <= 50) {
                return context.getResources().getDrawable(R.drawable.progressbar_50);
            } else if (per >= 51 && per <= 75) {
                return context.getResources().getDrawable(R.drawable.progressbar_75);
            } else if (per >= 76 && per <= 99) {
                return context.getResources().getDrawable(R.drawable.progressbar_99);
            } else if (per >= 100) {
                return context.getResources().getDrawable(R.drawable.progressbar_100);
            }
        } catch (Exception e) {

        }
        return context.getResources().getDrawable(R.drawable.progressbar_25);
    }

    private static int[] homeBtnImageViewId = {R.mipmap.helping_object, R.mipmap.planning_mag, R.mipmap.log_mag, R.mipmap.mail, R.mipmap.help, R.mipmap.ducha, R.mipmap.shaixuan};
    private static String homeButtonText[] = {"贫困户信息", "计划管理", "日志管理", "通讯录", "督查任务", "脱贫督查", "督查筛选统计"};

    public static List<MyIntentEntity> getPermissionIntetnt(Context context, boolean isLogin, String usertype) {
        List<MyIntentEntity> intentList = new ArrayList<>();
        if (!isLogin) {//未登录
            for (int i = 0; i < 7; i++) {
                MyIntentEntity entity = new MyIntentEntity();
                Intent intent = new Intent(context, LoginActivity.class);
                entity.mIntent = intent;
                entity.title = homeButtonText[i];
                entity.img = homeBtnImageViewId[i];
                intentList.add(entity);
            }
            return intentList;
        }
        if (Constant.USER_TYPE_PINGKUN_REN.equals(usertype)) {//贫困户
            return intentList;
        }
        if (USER_TYPE_BANGFU_REN.equals(usertype)) {
            MyIntentEntity entity = new MyIntentEntity();
            Intent intent = new Intent(context, ModuleDetailActivity.class);
            intent.putExtra("moduleType", "helpObj");
            entity.mIntent = intent;
            entity.title = homeButtonText[0];
            entity.img = homeBtnImageViewId[0];
            intentList.add(entity);

            entity = new MyIntentEntity();
            intent = new Intent(context, ModuleDetailActivity.class);
            intent.putExtra("moduleType", "planMag");
            entity.mIntent = intent;
            entity.title = homeButtonText[1];
            entity.img = homeBtnImageViewId[1];
            intentList.add(entity);

            entity = new MyIntentEntity();
            intent = new Intent(context, ModuleDetailActivity.class);
            intent.putExtra("moduleType", "logMag");
            entity.mIntent = intent;
            entity.title = homeButtonText[2];
            entity.img = homeBtnImageViewId[2];
            intentList.add(entity);

            entity = new MyIntentEntity();
            intent = new Intent(context, ModuleDetailActivity.class);
            intent.putExtra("moduleType", "mail");
            entity.mIntent = intent;
            entity.title = homeButtonText[3];
            entity.img = homeBtnImageViewId[3];
            intentList.add(entity);
            return intentList;
        }
        if (USER_TYPE_BANGFU_DANWEI.equals(usertype)) {
            MyIntentEntity entity = new MyIntentEntity();
            Intent intent = new Intent(context, ModuleDetailActivity.class);
            intent.putExtra("moduleType", "helpObj");
            entity.mIntent = intent;
            entity.title = homeButtonText[0];
            entity.img = homeBtnImageViewId[0];
            intentList.add(entity);

            entity = new MyIntentEntity();
            intent = new Intent(context, ModuleDetailActivity.class);
            intent.putExtra("moduleType", "logMag");
            entity.mIntent = intent;
            entity.title = homeButtonText[2];
            entity.img = homeBtnImageViewId[2];
            intentList.add(entity);

            entity = new MyIntentEntity();
            intent = new Intent(context, ModuleDetailActivity.class);
            intent.putExtra("moduleType", "mail");
            entity.mIntent = intent;
            entity.title = homeButtonText[3];
            entity.img = homeBtnImageViewId[3];
            intentList.add(entity);

            entity = new MyIntentEntity();
            intent = new Intent(context, HelpActivity.class);
            entity.mIntent = intent;
            entity.title = homeButtonText[4];
            entity.img = homeBtnImageViewId[4];
            intentList.add(entity);
        }
        if (USER_TYPE_CUN_ADMIN.equals(usertype) || USER_TYPE_ZHEN_ADMIN.equals(usertype)) {
            MyIntentEntity entity = new MyIntentEntity();
            Intent intent = new Intent(context, ModuleDetailActivity.class);
            intent.putExtra("moduleType", "helpObj");
            entity.mIntent = intent;
            entity.title = homeButtonText[0];
            entity.img = homeBtnImageViewId[0];
            intentList.add(entity);

            entity = new MyIntentEntity();
            intent = new Intent(context, ModuleDetailActivity.class);
            intent.putExtra("moduleType", "logMag");
            entity.mIntent = intent;
            entity.title = homeButtonText[2];
            entity.img = homeBtnImageViewId[2];
            intentList.add(entity);

            entity = new MyIntentEntity();
            intent = new Intent(context, ModuleDetailActivity.class);
            intent.putExtra("moduleType", "mail");
            entity.mIntent = intent;
            entity.title = homeButtonText[3];
            entity.img = homeBtnImageViewId[3];
            intentList.add(entity);
        }
        if (USER_TYPE_XIAN_ADMIN.equals(usertype)) {
            MyIntentEntity entity = new MyIntentEntity();
            Intent intent = new Intent(context, ModuleDetailActivity.class);
            intent.putExtra("moduleType", "helpObj");
            entity.mIntent = intent;
            entity.title = homeButtonText[0];
            entity.img = homeBtnImageViewId[0];
            intentList.add(entity);

//            entity = new MyIntentEntity();
//            intent = new Intent(context, ModuleDetailActivity.class);
//            intent.putExtra("moduleType", "logMag");
//            entity.mIntent = intent;
//            entity.title = homeButtonText[2];
//            entity.img = homeBtnImageViewId[2];
//            intentList.add(entity);

            entity = new MyIntentEntity();
            intent = new Intent(context, ModuleDetailActivity.class);
            intent.putExtra("moduleType", "mail");
            entity.mIntent = intent;
            entity.title = homeButtonText[3];
            entity.img = homeBtnImageViewId[3];
            intentList.add(entity);

            entity = new MyIntentEntity();
            intent = new Intent(context, HelpActivity.class);
            entity.mIntent = intent;
            entity.title = homeButtonText[4];
            entity.img = homeBtnImageViewId[4];
            intentList.add(entity);

//            entity = new MyIntentEntity();
//            intent = new Intent(context, DuChaActivity.class);
//            entity.mIntent = intent;
//            entity.title = homeButtonText[5];
//            entity.img = homeBtnImageViewId[5];
//            intentList.add(entity);
//
//
//            entity = new MyIntentEntity();
//            intent = new Intent(context, InspectionSelectActivity.class);
//            entity.mIntent = intent;
//            entity.title = homeButtonText[6];
//            entity.img = homeBtnImageViewId[6];
//            intentList.add(entity);
        }
        if (USER_TYPE_DUCHA_RENYUAN.equals(usertype) ||
                USER_TYPE_DUCHA_LINGDAO.equals(usertype) ||
                USER_TYPE_DUCHA_GUANLIYUAN.equals(usertype)) {
            MyIntentEntity entity = new MyIntentEntity();
            Intent intent = new Intent(context, ModuleDetailActivity.class);
            intent.putExtra("moduleType", "helpObj");
            entity.mIntent = intent;
            entity.title = homeButtonText[0];
            entity.img = homeBtnImageViewId[0];
            intentList.add(entity);

//            entity = new MyIntentEntity();
//            intent = new Intent(context, ModuleDetailActivity.class);
//            intent.putExtra("moduleType", "logMag");
//            entity.mIntent = intent;
//            entity.title = homeButtonText[2];
//            entity.img = homeBtnImageViewId[2];
//            intentList.add(entity);

            entity = new MyIntentEntity();
            intent = new Intent(context, ModuleDetailActivity.class);
            intent.putExtra("moduleType", "mail");
            entity.mIntent = intent;
            entity.title = homeButtonText[3];
            entity.img = homeBtnImageViewId[3];
            intentList.add(entity);

            entity = new MyIntentEntity();
            intent = new Intent(context, HelpActivity.class);
            entity.mIntent = intent;
            entity.title = homeButtonText[4];
            entity.img = homeBtnImageViewId[4];
            intentList.add(entity);

            entity = new MyIntentEntity();
            intent = new Intent(context, DuChaActivity.class);
            entity.mIntent = intent;
            entity.title = homeButtonText[5];
            entity.img = homeBtnImageViewId[5];
            intentList.add(entity);


            entity = new MyIntentEntity();
            intent = new Intent(context, InspectionSelectActivity.class);
            entity.mIntent = intent;
            entity.title = homeButtonText[6];
            entity.img = homeBtnImageViewId[6];
            intentList.add(entity);
        }

        if (USER_TYPE_XITONG_ADMIN.equals(usertype)) {
            MyIntentEntity entity = new MyIntentEntity();
            Intent intent = new Intent(context, ModuleDetailActivity.class);
            intent.putExtra("moduleType", "helpObj");
            entity.mIntent = intent;
            entity.title = homeButtonText[0];
            entity.img = homeBtnImageViewId[0];
            intentList.add(entity);

            entity = new MyIntentEntity();
            intent = new Intent(context, ModuleDetailActivity.class);
            intent.putExtra("moduleType", "planMag");
            entity.mIntent = intent;
            entity.title = homeButtonText[1];
            entity.img = homeBtnImageViewId[1];
            intentList.add(entity);

            entity = new MyIntentEntity();
            intent = new Intent(context, ModuleDetailActivity.class);
            intent.putExtra("moduleType", "logMag");
            entity.mIntent = intent;
            entity.title = homeButtonText[2];
            entity.img = homeBtnImageViewId[2];
            intentList.add(entity);

            entity = new MyIntentEntity();
            intent = new Intent(context, ModuleDetailActivity.class);
            intent.putExtra("moduleType", "mail");
            entity.mIntent = intent;
            entity.title = homeButtonText[3];
            entity.img = homeBtnImageViewId[3];
            intentList.add(entity);

            entity = new MyIntentEntity();
            intent = new Intent(context, HelpActivity.class);
            entity.mIntent = intent;
            entity.title = homeButtonText[4];
            entity.img = homeBtnImageViewId[4];
            intentList.add(entity);

            entity = new MyIntentEntity();
            intent = new Intent(context, DuChaActivity.class);
            entity.mIntent = intent;
            entity.title = homeButtonText[5];
            entity.img = homeBtnImageViewId[5];
            intentList.add(entity);


            entity = new MyIntentEntity();
            intent = new Intent(context, InspectionSelectActivity.class);
            entity.mIntent = intent;
            entity.title = homeButtonText[6];
            entity.img = homeBtnImageViewId[6];
            intentList.add(entity);
        }
        return intentList;
    }
}
