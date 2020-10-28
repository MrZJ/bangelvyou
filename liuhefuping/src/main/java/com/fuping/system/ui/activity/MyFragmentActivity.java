package com.fuping.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.fuping.system.R;
import com.fuping.system.ui.fragment.BaseFragment;
import com.fuping.system.ui.fragment.CountryListFragment;
import com.fuping.system.ui.fragment.PeopleCountryListFragment;
import com.fuping.system.ui.fragment.PeopleListFragment;

/**
 * Created by jianzhang.
 * on 2017/10/20.
 * copyright easybiz.
 */

public class MyFragmentActivity extends BaseActivity {
    private BaseFragment mFragment;
    public static final int TYPE_TONGJI_COUNTRY_LIST = 0;
    public static final int TYPE_HOME_COUNTRY_LIST = 1;
    public static final int TYPE_TONGJI_PEOPLE_COUNTRY_LIST = 2;
    public static final int TYPE_HOME_PEOPLE_LIST = 3;
    public static final int TYPE_TONGJI_PEOPLE_PEOPLE_LIST = 4;
    private int mType = -1;

    public static void startActivity(Context context, int type, String inspection_state, String town_p_index, String name_like) {
        Intent intent = new Intent(context, MyFragmentActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("name_like", name_like);
        intent.putExtra("inspection_state", inspection_state);
        intent.putExtra("town_p_index", town_p_index);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fragment);
        FragmentManager manager = getSupportFragmentManager();
        Intent intent = getIntent();
        mType = intent.getIntExtra("type", -1);
        if (TYPE_TONGJI_COUNTRY_LIST == mType) {
            String name_like = intent.getStringExtra("name_like");
            String town_p_index = intent.getStringExtra("town_p_index");
            String inspection_state = intent.getStringExtra("inspection_state");
            mFragment = CountryListFragment.getInstance(CountryListFragment.TYPE_DETAIL_LIST, name_like, town_p_index, inspection_state);
        } else if (TYPE_TONGJI_PEOPLE_COUNTRY_LIST == mType) {
            String name_like = intent.getStringExtra("name_like");
            String town_p_index = intent.getStringExtra("town_p_index");
            String inspection_state = intent.getStringExtra("inspection_state");
            mFragment = PeopleCountryListFragment.getInstance(PeopleCountryListFragment.TYPE_DETAIL_LIST, name_like, town_p_index, inspection_state);
        } else if (TYPE_TONGJI_PEOPLE_PEOPLE_LIST == mType) {
            String name_like = intent.getStringExtra("name_like");
            String town_p_index = intent.getStringExtra("town_p_index");
            String inspection_state = intent.getStringExtra("inspection_state");
            mFragment = PeopleListFragment.getInstance(TYPE_TONGJI_PEOPLE_PEOPLE_LIST, name_like, town_p_index, inspection_state);
        }
        manager.beginTransaction().add(R.id.fragment_container, mFragment).commit();
    }
}
