package com.shishoureport.system.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.shishoureport.system.R;
import com.shishoureport.system.entity.SelFileBean;
import com.shishoureport.system.entity.TaskTypeEntity;
import com.shishoureport.system.entity.User;
import com.shishoureport.system.ui.adapter.AddFileAdapter;
import com.shishoureport.system.ui.adapter.AddPeopleAdapter;
import com.shishoureport.system.ui.fragment.ContactsFragment;
import com.shishoureport.system.ui.fragment.FileListFragment;
import com.shishoureport.system.utils.ConfigUtil;
import com.shishoureport.system.utils.FileUtils;
import com.shishoureport.system.utils.ListUtils;
import com.shishoureport.system.utils.MySharepreference;
import com.shishoureport.system.utils.StringUtil;
import com.shishoureport.system.wibget.HorizontalListView;
import com.shishoureport.system.wibget.MyDataPickerDialog;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.shishoureport.system.ui.activity.LeaveActivity.CONTACTS_REQUEST;

/**
 * Created by jianzhang.
 * on 2017/9/1.
 * copyright easybiz.
 */

public class ReleaseFileActivity extends BaseActivity implements AdapterView.OnItemClickListener,
        View.OnClickListener, AdapterView.OnItemSelectedListener, MyDataPickerDialog.OnButtonClick {
    public static final String PUBLIC_TASK_FILE = "965";
    private HorizontalListView mHorizonListView;
    private ListView file_listview;
    private AddPeopleAdapter addPeopleAdapter;
    //    private RadioButton jd_rb, gh_rb, yw_rb, gw_rb;
    private TextView file_name_et, file_explain_et, file_upload_tv, top_title, tv_top_right_text,
            receive_time_et, jiguan_file_et, text_num_et, num_et, xingzhi_et, remark_et, reason_et_et;
    private Spinner task_type_sp;
    private View sel_file_layout, public_file_layout, task_detail_layout;
    private AddFileAdapter fileAdapter;
    private List<SelFileBean> fileBeans = new ArrayList<>();
    private List<TaskTypeEntity> mTaskTypes;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ReleaseFileActivity.class);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_release_file;
    }

    public void initView() {
        mHorizonListView = (HorizontalListView) findViewById(R.id.horizontal_lsitview);
        file_name_et = (TextView) findViewById(R.id.file_name_et);
        file_explain_et = (TextView) findViewById(R.id.file_explain_et);
        file_upload_tv = (TextView) findViewById(R.id.file_upload_tv);
        sel_file_layout = findViewById(R.id.sel_file_layout);
        sel_file_layout.setOnClickListener(this);
        task_type_sp = (Spinner) findViewById(R.id.task_type_sp);
        task_type_sp.setOnItemSelectedListener(this);
        findViewById(R.id.selet_time_layout).setOnClickListener(this);
        public_file_layout = findViewById(R.id.public_file_layout);
        task_detail_layout = findViewById(R.id.task_detail_layout);
        receive_time_et = (TextView) findViewById(R.id.receive_time_et);
        jiguan_file_et = (TextView) findViewById(R.id.jiguan_file_et);
        text_num_et = (TextView) findViewById(R.id.text_num_et);
        num_et = (TextView) findViewById(R.id.num_et);
        xingzhi_et = (TextView) findViewById(R.id.xingzhi_et);
        remark_et = (TextView) findViewById(R.id.remark_et);
        reason_et_et = (TextView) findViewById(R.id.reason_et_et);
        findViewById(R.id.btn_top_sidebar).setVisibility(View.VISIBLE);
        tv_top_right_text = (TextView) findViewById(R.id.tv_top_right_text);
        tv_top_right_text.setOnClickListener(this);
        tv_top_right_text.setVisibility(View.VISIBLE);
        tv_top_right_text.setText("提交");
        addPeopleAdapter = new AddPeopleAdapter(this, getData());
        mHorizonListView.setAdapter(addPeopleAdapter);
        mHorizonListView.setOnItemClickListener(this);
        top_title = (TextView) findViewById(R.id.top_title);
        file_listview = (ListView) findViewById(R.id.file_listview);
        fileAdapter = new AddFileAdapter(this, fileBeans);
        file_listview.setAdapter(fileAdapter);
        top_title.setText("发布任务");
        String sp = MySharepreference.getInstance(this).getString(MySharepreference.ENTITY100LIST);
        if (!StringUtil.isEmpty(sp)) {
            mTaskTypes = JSONArray.parseArray(sp, TaskTypeEntity.class);
            List<String> list = new ArrayList<>();
            for (TaskTypeEntity entity : mTaskTypes) {
                list.add(entity.type_name);
            }
            task_type_sp.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == (mData.size() - 1)) {
            MyListActivity.startActivityForResult(this, MyListActivity.TYPE_CONTACTS_LIST, addPeopleAdapter.getIds(), CONTACTS_REQUEST);
        } else {
            mData.remove(position);
            addPeopleAdapter.notifyDataSetChanged();
        }
    }

    List<User> mData = new ArrayList<>();

    private List<User> getData() {
        mData = new ArrayList<>();
        User p = new User();
        mData.add(p);
        return mData;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CONTACTS_REQUEST) {
                try {
                    User user = (User) data.getSerializableExtra(ContactsFragment.KEY_CONTACTS);
                    if (mData.size() >= 1) {
                        mData.add(mData.size() - 1, user);
                    } else {
                        mData.add(user);
                    }
                    addPeopleAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == FILE_SELECT_CODE) {
                Uri uri = data.getData();
                fileAdapter.addData(FileUtils.getPath(this, uri));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sel_file_layout:
                chooseFile();
                break;
            case R.id.tv_top_right_text:
                upload();
                break;
            case R.id.selet_time_layout:
                showDataDialog("开始时间", MyDataPickerDialog.TYPE_START_TIME);
                break;
        }
    }

    private void showDataDialog(String title, int type) {
        MyDataPickerDialog dataPickerDialog = new MyDataPickerDialog(this, title, this, type);
        dataPickerDialog.show();
    }

    private static final int FILE_SELECT_CODE = 0;

    private void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "选择文件"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "亲，木有文件管理器啊-_-!!", Toast.LENGTH_SHORT).show();
        }
    }

    public void upload() {
        String file_name = file_name_et.getText().toString();
        if (StringUtil.isEmpty(file_name)) {
            showToast("请输入任务名称");
            return;
        }
        if (mData.size() == 1) {
            showToast("请选择接收人");
            return;
        }
        RequestParams params = new RequestParams(ConfigUtil.HTTP_URL + "/mobile/taskinfo/save");
        if (!ListUtils.isEmpty(fileBeans)) {
            for (int i = 0; i < fileBeans.size(); i++) {
                params.addBodyParameter("upload" + i, new File(fileBeans.get(i).file_path));
            }
        }
        StringBuilder names = new StringBuilder();
        StringBuilder ids = new StringBuilder();
        for (int i = 0; i < mData.size() - 1; i++) {
            User user = mData.get(i);
            if (mData.size() - 2 == i) {
                names.append(user.user_name);
                ids.append(user.id);
            } else {
                names.append(user.user_name + ",");
                ids.append(user.id + ",");
            }
        }
        params.addBodyParameter("task_name", file_name);
        try {
            String task_type_name = mTaskTypes.get(task_type_sp.getSelectedItemPosition()).type_name;
            String task_type = mTaskTypes.get(task_type_sp.getSelectedItemPosition()).id;
            params.addBodyParameter("task_type_name", task_type_name);
            params.addBodyParameter("task_type", task_type);
            if (PUBLIC_TASK_FILE.equals(task_type)) {
//                receive_time_et, jiguan_file_et, text_num_et, num_et, xingzhi_et, remark_et, reason_et_et
                if (!StringUtil.isEmpty(receive_time_et.getText().toString())) {
                    params.addBodyParameter("get_date", receive_time_et.getText().toString());
                }
                if (!StringUtil.isEmpty(jiguan_file_et.getText().toString())) {
                    params.addBodyParameter("civil_service", jiguan_file_et.getText().toString());
                }
                if (!StringUtil.isEmpty(text_num_et.getText().toString())) {
                    params.addBodyParameter("word_size", text_num_et.getText().toString());
                }
                if (!StringUtil.isEmpty(num_et.getText().toString())) {
                    params.addBodyParameter("copies", num_et.getText().toString());
                }
                if (!StringUtil.isEmpty(xingzhi_et.getText().toString())) {
                    params.addBodyParameter("nature", xingzhi_et.getText().toString());
                }
                if (!StringUtil.isEmpty(remark_et.getText().toString())) {
                    params.addBodyParameter("remark", remark_et.getText().toString());
                }
                if (!StringUtil.isEmpty(reason_et_et.getText().toString())) {
                    params.addBodyParameter("reason", reason_et_et.getText().toString());
                }
            } else {
                String file_explain = file_explain_et.getText().toString();
                if (StringUtil.isEmpty(file_explain)) {
                    showToast("请输入任务明细");
                    return;
                }
                params.addBodyParameter("task_desc", file_explain);
            }
        } catch (Exception e) {
        }
        params.addBodyParameter("user_names", names.toString());
        User user = MySharepreference.getInstance(this).getUser();
        params.addBodyParameter("user_id", user.id);
        params.addBodyParameter("user_ids", ids.toString());
        params.addBodyParameter("is_submit", "1");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String responseEntity) {
                closeDialog();
                showToast("提交完成");
                Intent intent = new Intent(FileListFragment.ACTIPN_REFRESH_LIST);
                sendBroadcast(intent);
                finish();
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                if (throwable != null && throwable.getMessage() != null) {
                    Log.e("jianzhang", throwable.getMessage().toLowerCase());
                }
                showToast("上传失败");
                closeDialog();
            }

            @Override
            public void onCancelled(CancelledException e) {
                closeDialog();
            }

            @Override
            public void onFinished() {
                closeDialog();
            }
        });
        showDialog();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (PUBLIC_TASK_FILE.equals(mTaskTypes.get(position).id)) {
            public_file_layout.setVisibility(View.VISIBLE);
            task_detail_layout.setVisibility(View.GONE);
        } else {
            public_file_layout.setVisibility(View.GONE);
            task_detail_layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onOkClick(String date, int type, int pos) {
        receive_time_et.setText(date);
    }

    @Override
    public void onCancelClick(int type, int pos) {

    }
}
