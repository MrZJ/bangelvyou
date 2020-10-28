package com.shenmaireview.system.ui.fragment;


import android.support.v4.app.Fragment;

import com.shenmaireview.system.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends BaseFragment {

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_about;
    }

    @Override
    protected void initView() {
        setTopTitle("关于我们");
        setLeftBackButton(false);
    }

}
