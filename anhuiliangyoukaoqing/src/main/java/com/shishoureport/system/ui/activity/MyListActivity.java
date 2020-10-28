package com.shishoureport.system.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import com.shishoureport.system.R;
import com.shishoureport.system.ui.fragment.BaseFragment;
import com.shishoureport.system.ui.fragment.BusTraListFragment;
import com.shishoureport.system.ui.fragment.CarmanageListFragment;
import com.shishoureport.system.ui.fragment.ContactsFragment;
import com.shishoureport.system.ui.fragment.CountFragment;
import com.shishoureport.system.ui.fragment.FileListFragment;
import com.shishoureport.system.ui.fragment.LeaveListFragment;
import com.shishoureport.system.ui.fragment.OverTimeListFragment;

import static com.shishoureport.system.ui.fragment.BusTraListFragment.TYPE_ALL_BUS_TRA_LIST;

/**
 * Created by jianzhang.
 * on 2017/9/5.
 * copyright easybiz.
 */

public class MyListActivity extends BaseActivity implements View.OnClickListener {
    public static final int TYPE_CONTACTS_LIST = 100;
    public static final int TYPE_ATTANDENCE_COUNT = 120;
    public static final int TYPE_OVER_TIME_LIST = 121;
    public static final int TYPE_FILE_SOLVED_LIST = 122;
    public static final int TYPE_PERSONAL_OVER_TIME_LIST = 123;
    public static final int TYPE_CAR_MANAGE_LIST = 124;
    private BaseFragment mFragment;
    private View back;
    private TextView title_tv;
    public static final String KEY_LIST_TYPE = "type";
    private int mType = -1;

    public static void startActivity(Context context, int type) {
        Intent i = new Intent(context, MyListActivity.class);
        i.putExtra(KEY_LIST_TYPE, type);
        context.startActivity(i);
    }

    public static void startActivityForResult(Activity context, int type, int requestcode) {
        Intent i = new Intent(context, MyListActivity.class);
        i.putExtra(KEY_LIST_TYPE, type);
        context.startActivityForResult(i, requestcode);
    }

    public static void startActivityForResult(Activity context, int type, String ids, int requestcode) {
        Intent i = new Intent(context, MyListActivity.class);
        i.putExtra(KEY_LIST_TYPE, type);
        i.putExtra("ids", ids);
        context.startActivityForResult(i, requestcode);
    }

    @Override
    public int getLayoutId() {
        mType = getIntent().getIntExtra(KEY_LIST_TYPE, 0);
        return R.layout.activity_my_list;
    }

    public void initView() {
        title_tv = (TextView) findViewById(R.id.title_tv);
        back = findViewById(R.id.back_tv);
        back.setOnClickListener(this);
        if (mType == LeaveListFragment.TYPE_I_SENDED) {
            title_tv.setText("我发起的");
        } else if (mType == LeaveListFragment.TYPE_I_COPYED) {
            title_tv.setText("抄送我的");
        } else if (mType == TYPE_ALL_BUS_TRA_LIST) {
            title_tv.setText("出差报告单");
        } else if (mType == LeaveListFragment.TYPE_LEAVE_APP_LIST) {
            title_tv.setText("请假报告单");
        } else if (mType == TYPE_OVER_TIME_LIST) {
            title_tv.setText("加班报告单");
        } else if (mType == TYPE_PERSONAL_OVER_TIME_LIST) {
            title_tv.setText("个人加班报告单");
        } else if (mType == TYPE_CAR_MANAGE_LIST) {
            title_tv.setText("派车管理");
        }
        FragmentManager manager = getSupportFragmentManager();
        if (mType == TYPE_OVER_TIME_LIST) {
            mFragment = OverTimeListFragment.getInstance(OverTimeListFragment.TYPE_ALL);
        } else if (mType == TYPE_CONTACTS_LIST) {
            String ids = getIntent().getStringExtra("ids");
            mFragment = ContactsFragment.getInstance(ids);
        } else if (mType == TYPE_ALL_BUS_TRA_LIST) {
            mFragment = BusTraListFragment.getInstance(TYPE_ALL_BUS_TRA_LIST);
        } else if (mType == TYPE_ATTANDENCE_COUNT) {
            mFragment = new CountFragment();
        } else if (mType == TYPE_FILE_SOLVED_LIST) {
            mFragment = FileListFragment.getInstance(FileListFragment.TYPE_WAITE_SOLVED);
        } else if (mType == TYPE_PERSONAL_OVER_TIME_LIST) {
            mFragment = OverTimeListFragment.getInstance(OverTimeListFragment.TYPE_PERSON);
        } else if (mType == TYPE_CAR_MANAGE_LIST) {
            mFragment = CarmanageListFragment.getInstance();
        } else {
            mFragment = LeaveListFragment.getInstance(mType);
        }
        manager.beginTransaction().add(R.id.container, mFragment).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_tv:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            mFragment.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {

        }
    }
}
