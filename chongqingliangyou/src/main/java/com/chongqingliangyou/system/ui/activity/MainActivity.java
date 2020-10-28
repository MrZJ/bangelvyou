package com.chongqingliangyou.system.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chongqingliangyou.system.R;
import com.chongqingliangyou.system.util.ConfigUtil;
import com.slidingmenu.lib.SlidingMenu;

/**
 * 主页面
 */
public class MainActivity extends BaseActivity implements View.OnClickListener{

    private View stress, daily, finished, set , about;
    private static boolean isExit = false;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };
    private TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        initSlidMenu();
        initView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        configEntity = ConfigUtil.loadConfig(this);
    }

    private void initSlidMenu(){
        SlidingMenu menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        // 设置滑动菜单视图的宽度
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //为侧滑菜单设置布局
        menu.setMenu(R.layout.left_menu);
    }

    private void  initView(){
        initTopView();
        setTitle("重庆粮油检测");
        setLeftBackShow(false);
        stress = findViewById(R.id.rel_main_stress);
        daily = findViewById(R.id.rel_main_daily);
        finished = findViewById(R.id.rel_main_finished);
        set = findViewById(R.id.rel_main_set);
        about = findViewById(R.id.rel_main_about);
        userName = (TextView) findViewById(R.id.tv_main_username);
        userName.setText(configEntity.username);
    }

    @Override
    public void initListener() {
        super.initListener();
        stress.setOnClickListener(this);
        daily.setOnClickListener(this);
        finished.setOnClickListener(this);
        set.setOnClickListener(this);
        about.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent taskListIntent = new Intent(this,TaskListActivity.class);
        switch (v.getId()){
            case R.id.rel_main_stress:
                taskListIntent.putExtra("type","stress");
                startActivity(taskListIntent);
                break;
            case R.id.rel_main_daily:
                taskListIntent.putExtra("type","daily");
                startActivity(taskListIntent);
                break;
            case R.id.rel_main_finished:
                taskListIntent.putExtra("type","finished");
                startActivity(taskListIntent);
                break;
            case R.id.rel_main_set:
                IntentOtherActivity(SetActivity.class);
                break;
            case R.id.rel_main_about:
                IntentOtherActivity(ABoutActivity.class);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            handler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }
}
