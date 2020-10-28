package com.shishoureport.system.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ExpandableListView;

import com.alibaba.fastjson.JSONArray;
import com.shishoureport.system.R;
import com.shishoureport.system.entity.ContactsGroup;
import com.shishoureport.system.entity.User;
import com.shishoureport.system.request.GetUserListRequest;
import com.shishoureport.system.ui.adapter.ContactsExpandAdapter;
import com.shishoureport.system.utils.ListUtils;

import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/9/7.
 * copyright easybiz.
 */

public class ContactsFragment extends BaseFragment {
    public static final String KEY_CONTACTS = "key";
    private ExpandableListView mListView;
    private List<ContactsGroup> mData;

    private String ids;

    public static ContactsFragment getInstance(String ids) {
        ContactsFragment fragment = new ContactsFragment();
        fragment.ids = ids;
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_contact;
    }

    @Override
    protected void initView(View v) {
        mListView = (ExpandableListView) v.findViewById(R.id.listview);
        loadData();
        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = new Intent();
                User user = mData.get(groupPosition).userInfoList.get(childPosition);
                intent.putExtra(KEY_CONTACTS, user);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
                return true;
            }
        });
    }


    @Override
    public void loadData() {
        GetUserListRequest request = new GetUserListRequest(ids);
        httpGetRequest(request.getRequestUrl(), GetUserListRequest.GET_USER_LIST_REQUEST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case GetUserListRequest.GET_USER_LIST_REQUEST:
                datahandle(json);
                break;
        }
    }

    private void datahandle(String json) {
        mData = JSONArray.parseArray(json, ContactsGroup.class);
        if (ListUtils.isEmpty(mData) && isVisible()) {
            showToast(R.string.no_data_tip);
        }
        mListView.setAdapter(new ContactsExpandAdapter(mData, getActivity()));
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//    }
}
