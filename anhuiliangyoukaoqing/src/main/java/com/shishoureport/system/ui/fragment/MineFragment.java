package com.shishoureport.system.ui.fragment;

import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.shishoureport.system.R;
import com.shishoureport.system.entity.User;
import com.shishoureport.system.ui.activity.LoginActivity;
import com.shishoureport.system.utils.FrescoHelper;
import com.shishoureport.system.utils.MySharepreference;
import com.shishoureport.system.utils.StringUtil;

/**
 * 我的
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {
    private TextView name_tv, work_palce_tv, work_status_tv;
    private SimpleDraweeView photo_sd;
    private View login_out_rl;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View v) {
        setTopTitle("我的");
        setLeftBackButton(false);
        name_tv = (TextView) v.findViewById(R.id.name_tv);
        work_palce_tv = (TextView) v.findViewById(R.id.work_palce_tv);
        work_status_tv = (TextView) v.findViewById(R.id.work_status_tv);
        login_out_rl = v.findViewById(R.id.login_out_rl);
        login_out_rl.setOnClickListener(this);
        photo_sd = (SimpleDraweeView) v.findViewById(R.id.photo_sd);
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_out_rl://注销登陆
                MySharepreference.getInstance(getActivity()).clearUser();
                LoginActivity.startActivity(getActivity());
                getActivity().finish();
                break;
        }
    }

    private void setData() {
        User user = MySharepreference.getInstance(getActivity()).getUser();
        if (user == null || StringUtil.isEmpty(user.id)) {
            name_tv.setText("");
            work_palce_tv.setText("");
            work_status_tv.setText("");
            photo_sd.setImageResource(R.mipmap.ic_launcher);
        } else {
            name_tv.setText(user.user_name);
            work_palce_tv.setText(user.dept_name);
            work_status_tv.setText("");//工作状态未知
            FrescoHelper.loadImage(user.positions, photo_sd);
        }
    }
}
