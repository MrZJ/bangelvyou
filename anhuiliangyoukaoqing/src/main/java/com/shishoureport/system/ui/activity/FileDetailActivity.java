package com.shishoureport.system.ui.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.shishoureport.system.R;
import com.shishoureport.system.entity.FieldErrors;
import com.shishoureport.system.entity.TaskEntity;
import com.shishoureport.system.entity.TaskFileEntity;
import com.shishoureport.system.entity.TaskPersonList;
import com.shishoureport.system.entity.User;
import com.shishoureport.system.request.ApproveTaskRequest;
import com.shishoureport.system.request.CommitTaskRequest;
import com.shishoureport.system.request.TaskDetailRequest;
import com.shishoureport.system.ui.adapter.AddPeopleAdapter;
import com.shishoureport.system.ui.adapter.DownloadFileAdapter;
import com.shishoureport.system.ui.adapter.FileStatusAdapter;
import com.shishoureport.system.ui.fragment.ContactsFragment;
import com.shishoureport.system.ui.fragment.FileListFragment;
import com.shishoureport.system.ui.fragment.PreViewActivity;
import com.shishoureport.system.utils.CacheImgUtil;
import com.shishoureport.system.utils.MySharepreference;
import com.shishoureport.system.utils.StringUtil;
import com.shishoureport.system.utils.TimeDateUtil;
import com.shishoureport.system.wibget.HorizontalListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.shishoureport.system.ui.activity.LeaveActivity.COPY_CONTACTS_REQUEST;
import static com.shishoureport.system.ui.fragment.FileListFragment.TYPE_FILE_REC;
import static com.shishoureport.system.ui.fragment.FileListFragment.TYPE_FILE_SEND;
import static com.shishoureport.system.ui.fragment.FileListFragment.TYPE_WAITE_SOLVED;
import static com.shishoureport.system.ui.fragment.FileListFragment.TYPE_WTAITE_AUDIT;

/**
 * Created by jianzhang.
 * on 2017/9/5.
 * copyright easybiz.
 */

public class FileDetailActivity extends BaseActivity implements View.OnClickListener, DownloadFileAdapter.ListClick {
    public static final String KEY_EXTRA = "key_extra";
    public static final String KEY_TYPE = "key_type";
    private ListView mylistview, file_listview;
    private TextView receive_des_et, dowload_file_tv, file_name_tv, release_time_tv, received_people_tv, audit_suggestion_et, top_title,
            receive_time_et, jiguan_file_et, text_num_et, num_et, xingzhi_et, remark_et, reason_et_et, task_detail_tv, send_people_tv;
    private View describ_layout, commit_layout;
    private View ok_btn, pass_btn, not_pass_btn, public_file_layout;
    private HorizontalListView copy_listview;
    private TaskEntity mEntity;
    private String fileNameTip, releaseTimeTip, receivedPersonTip;
    private int mType;
    private AddPeopleAdapter copyPeopleAdapter;

    public static void startActivity(Context context, TaskEntity entity, int type) {
        Intent intent = new Intent(context, FileDetailActivity.class);
        intent.putExtra(KEY_EXTRA, entity);
        intent.putExtra(KEY_TYPE, type);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_file_detail;
    }

    @Override
    public void initView() {
        fileNameTip = getResources().getString(R.string.file_name_tip);
        releaseTimeTip = getResources().getString(R.string.release_time_tip);
        receivedPersonTip = getResources().getString(R.string.received_person_tip);
        mEntity = (TaskEntity) getIntent().getSerializableExtra(KEY_EXTRA);
        mType = getIntent().getIntExtra(KEY_TYPE, 0);
        if (mEntity == null || mEntity.id == null) {
            finish();
            return;
        }
        top_title = (TextView) findViewById(R.id.top_title);
        mylistview = (ListView) findViewById(R.id.mylistview);
        audit_suggestion_et = (TextView) findViewById(R.id.audit_suggestion_et);
        receive_des_et = (TextView) findViewById(R.id.receive_des_et);
        dowload_file_tv = (TextView) findViewById(R.id.dowload_file_tv);
        send_people_tv = (TextView) findViewById(R.id.send_people_tv);
        task_detail_tv = (TextView) findViewById(R.id.task_detail_tv);
        dowload_file_tv.setOnClickListener(this);
        describ_layout = findViewById(R.id.describ_layout);
        file_name_tv = (TextView) findViewById(R.id.file_name_tv);
        release_time_tv = (TextView) findViewById(R.id.release_time_tv);
        file_listview = (ListView) findViewById(R.id.file_listview);
        copy_listview = (HorizontalListView) findViewById(R.id.copy_listview);
        received_people_tv = (TextView) findViewById(R.id.received_people_tv);
        commit_layout = findViewById(R.id.commit_layout);
        pass_btn = findViewById(R.id.pass_btn);
        not_pass_btn = findViewById(R.id.not_pass_btn);
        ok_btn = findViewById(R.id.ok_btn);
        public_file_layout = findViewById(R.id.public_file_layout);
        receive_time_et = (TextView) findViewById(R.id.receive_time_et);
        jiguan_file_et = (TextView) findViewById(R.id.jiguan_file_et);
        text_num_et = (TextView) findViewById(R.id.text_num_et);
        num_et = (TextView) findViewById(R.id.num_et);
        xingzhi_et = (TextView) findViewById(R.id.xingzhi_et);
        remark_et = (TextView) findViewById(R.id.remark_et);
        reason_et_et = (TextView) findViewById(R.id.reason_et_et);
        ok_btn.setOnClickListener(this);
        pass_btn.setOnClickListener(this);
        not_pass_btn.setOnClickListener(this);
        if (TYPE_FILE_SEND == mType || TYPE_FILE_REC == mType) {
            findViewById(R.id.btn_layout).setVisibility(View.GONE);
            describ_layout.setVisibility(View.GONE);
            commit_layout.setVisibility(View.GONE);
            findViewById(R.id.copy_people_tv).setVisibility(View.GONE);
            copy_listview.setVisibility(View.GONE);
            audit_suggestion_et.setVisibility(View.GONE);
            findViewById(R.id.add_people_layout).setVisibility(View.GONE);
            top_title.setText("任务详情");
        } else if (TYPE_WAITE_SOLVED == mType) {
            describ_layout.setVisibility(View.VISIBLE);
            commit_layout.setVisibility(View.VISIBLE);
            pass_btn.setVisibility(View.GONE);
            not_pass_btn.setVisibility(View.GONE);
            audit_suggestion_et.setVisibility(View.GONE);
            top_title.setText("任务详情");
        } else if (TYPE_WTAITE_AUDIT == mType) {
            describ_layout.setVisibility(View.GONE);
            commit_layout.setVisibility(View.VISIBLE);
            ok_btn.setVisibility(View.GONE);
            pass_btn.setVisibility(View.VISIBLE);
            not_pass_btn.setVisibility(View.VISIBLE);
            findViewById(R.id.add_people_layout).setVisibility(View.GONE);
            audit_suggestion_et.setVisibility(View.VISIBLE);
            top_title.setText("任务审核");
        }
        copyPeopleAdapter = new AddPeopleAdapter(this, getmCopyUsers());
        copy_listview.setAdapter(copyPeopleAdapter);
        copy_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (position == (mCopyUsers.size() - 1)) {
                        MyListActivity.startActivityForResult(FileDetailActivity.this, MyListActivity.TYPE_CONTACTS_LIST, copyPeopleAdapter.getIds(), COPY_CONTACTS_REQUEST);
                    } else {
                        mCopyUsers.remove(position);
                        copyPeopleAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                }
            }
        });
        loadData();
    }

    List<User> mCopyUsers = new ArrayList<>();

    private List<User> getmCopyUsers() {
        mCopyUsers = new ArrayList<>();
        User p = new User();
        mCopyUsers.add(p);
        return mCopyUsers;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dowload_file_tv:
                if (mEntity != null && !StringUtil.isEmpty(mEntity.file_path)) {
//                    download(mEntity.file_path, CacheImgUtil.PATH_DATA_CACHE + "/");
                    download(mEntity.file_path, CacheImgUtil.PATH_DATA_CACHE + "/", mEntity.file_name);
                }
                break;
           /* case R.id.cancel_btn:
                if (mEntity.map.is_first) {
                    showToast("你是审批该任务的第一个人，无法打回到上一个人");
                    return;
                }
                commitData("0");
                break;*/
            case R.id.pass_btn:
                commitData1("1");
                break;
            case R.id.not_pass_btn:
                commitData1("-1");
                break;
            case R.id.ok_btn:
                commitData("1");
                break;
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        closeDialog();
        showToast("获取数据失败，请重试");
        finish();
    }

    private void loadData() {
        showDialog();
        String id;
        if (TYPE_FILE_REC == mType) {
            id = mEntity.link_id;
        } else {
            id = mEntity.id;
        }
        TaskDetailRequest request = new TaskDetailRequest(id, mType);
        httpGetRequest(request.getRequestUrl(), TaskDetailRequest.TASK_DETAIL_REQUEST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        closeDialog();
        switch (action) {
            case TaskDetailRequest.TASK_DETAIL_REQUEST:
                handleRequest(json);
                break;
            case CommitTaskRequest.COMMIT_TASK_REQUEST:
                showToast("提交完成");
                Intent intent = new Intent(FileListFragment.ACTIPN_REFRESH_LIST);
                sendBroadcast(intent);
                finish();
                break;
        }
    }

    private void handleRequest(String json) {
        mEntity = JSONObject.parseObject(json, TaskEntity.class);
        if (mEntity == null) {
            finish();
            return;
        }
        file_name_tv.setText(String.format(fileNameTip, mEntity.task_name));
        release_time_tv.setText(String.format(releaseTimeTip, TimeDateUtil.date(mEntity.add_date)));
        if (!StringUtil.isEmpty(mEntity.task_desc) && !"null".equals(mEntity.task_desc)) {
            task_detail_tv.setText("任务明细：" + mEntity.task_desc);
        }
        if (mEntity.map != null && !StringUtil.isEmpty(mEntity.map.real_name) && !"null".equals(mEntity.map.real_name)) {
            send_people_tv.setText("发起人：" + mEntity.map.real_name);
        }
        if (mEntity.map != null && mEntity.map.bfList != null) {
            file_listview.setAdapter(new DownloadFileAdapter(this, mEntity.map.bfList, this));
        }
        if (mEntity.map != null && mEntity.map.taskLinkInfoList != null) {
            StringBuilder persons = new StringBuilder("");
            for (int i = 0; i < mEntity.map.taskLinkInfoList.size(); i++) {
                TaskPersonList personList = mEntity.map.taskLinkInfoList.get(i);
                if (i == 0) {
                    persons.append(personList.accpect_person_name);
                } else {
                    persons.append(" " + personList.accpect_person_name);
                }
            }
            received_people_tv.setText(String.format(receivedPersonTip, persons.toString()));
            mylistview.setAdapter(new FileStatusAdapter(mEntity.map.taskLinkInfoList, this));
        }
        if (ReleaseFileActivity.PUBLIC_TASK_FILE.equals(mEntity.task_type)) {
            public_file_layout.setVisibility(View.VISIBLE);
            receive_time_et.setText(TimeDateUtil.date(mEntity.get_date));
            jiguan_file_et.setText(mEntity.civil_service);
            text_num_et.setText(mEntity.word_size);
            num_et.setText(mEntity.copies);
            xingzhi_et.setText(mEntity.nature);
            remark_et.setText(mEntity.remark);
            reason_et_et.setText(mEntity.reason);
        } else {
            public_file_layout.setVisibility(View.GONE);
        }
    }

    private void commitData(String is_pass) {
        if (StringUtil.isEmpty(receive_des_et.getText().toString())) {
            showToast("请填写处理说明");
            return;
        }
        StringBuilder cc_user_names = new StringBuilder();
        StringBuilder cc_user_ids = new StringBuilder();
        if (mCopyUsers.size() > 1) {
            for (int i = 0; i < mCopyUsers.size() - 1; i++) {
                User user1 = mCopyUsers.get(i);
                if (mCopyUsers.size() - 2 == i) {
                    cc_user_names.append(user1.user_name);
                    cc_user_ids.append(user1.id);
                } else {
                    cc_user_names.append(user1.user_name + ",");
                    cc_user_ids.append(user1.id + ",");
                }
            }
        }
        CommitTaskRequest request = new CommitTaskRequest(MySharepreference.getInstance(this).getUser().id, mEntity.id, is_pass, receive_des_et.getText().toString(),
                cc_user_ids.toString(), cc_user_names.toString());
        httpPostRequest(request.getRequestUrl(), request.getRequestParams(), CommitTaskRequest.COMMIT_TASK_REQUEST);
    }

    private void commitData1(String is_pass) {
        ApproveTaskRequest request = new ApproveTaskRequest(MySharepreference.getInstance(this).getUser().id, is_pass, audit_suggestion_et.getText().toString(), mEntity.id);
        httpPostRequest(request.getRequestUrl(), request.getRequestParams(), CommitTaskRequest.COMMIT_TASK_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == COPY_CONTACTS_REQUEST) {
                try {
                    User user = (User) data.getSerializableExtra(ContactsFragment.KEY_CONTACTS);
                    if (mCopyUsers.size() >= 1) {
                        mCopyUsers.add(mCopyUsers.size() - 1, user);
                    } else {
                        mCopyUsers.add(user);
                    }
                    copyPeopleAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDownload(TaskFileEntity entity) {
        String filePath = CacheImgUtil.PATH_DATA_CACHE + "/" + entity.file_name;
        File file = new File(filePath);
        if (!isForeground(this)) return;
        if (file.exists()) {
            PreViewActivity.startActivity(this, file.getPath());
        } else {
            download(entity.file_path, CacheImgUtil.PATH_DATA_CACHE + "/", entity.file_name);
        }
    }

    @Override
    public void complateDownload(File file) {
        if (file != null) {
            String filePath = file.getPath();
            PreViewActivity.startActivity(this, filePath);
        }
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param activity 要判断的Activity
     * @return 是否在前台显示
     */
    public static boolean isForeground(Activity activity) {
        return isForeground(activity, activity.getClass().getName());
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context   Context
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className))
            return false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName()))
                return true;
        }
        return false;
    }

}
