package com.shishoureport.system.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.shishoureport.system.R;
import com.shishoureport.system.entity.AttendanceAllEntity;
import com.shishoureport.system.entity.AttendanceEntity;
import com.shishoureport.system.entity.FieldErrors;
import com.shishoureport.system.entity.User;
import com.shishoureport.system.request.AttandenceRequest;
import com.shishoureport.system.request.AttendanceDetailRequest;
import com.shishoureport.system.ui.activity.LocationActivity;
import com.shishoureport.system.utils.MySharepreference;
import com.shishoureport.system.utils.StringUtil;
import com.shishoureport.system.wibget.CustomDialog;

/**
 * Created by jianzhang.
 * on 2017/8/29.
 * copyright easybiz.
 */

public class AttandanceFragment extends BaseFragment implements View.OnClickListener, CustomDialog.CustomInterface {
    public static final int LOCATION_REQUESTCODE = 250;
    private View retry_location_tv, attendance_layout, failed_tv;
    private LocationClient mLocationClient;
    private TextView loc_place_tv, start_work_status_tv, start_work_attandence_place,
            end_work_attandence_place, end_work_status_tv, attendance_tv, end_work_time_tv, start_work_time_tv,
            pouch_in_time, pouch_out_time;
    private BDLocation mLocation;
    private AttendanceEntity mEntity;
    private boolean isFirstLocation = true;
    private boolean isCommitData = false;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_attandence;
    }

    @Override
    protected void initView(View v) {
        retry_location_tv = v.findViewById(R.id.retry_location_tv);
        retry_location_tv.setOnClickListener(this);
        attendance_layout = v.findViewById(R.id.attendance_layout);
        attendance_layout.setOnClickListener(this);
        loc_place_tv = (TextView) v.findViewById(R.id.loc_place_tv);
        start_work_status_tv = (TextView) v.findViewById(R.id.start_work_status_tv);
        start_work_attandence_place = (TextView) v.findViewById(R.id.start_work_attandence_place);
        end_work_attandence_place = (TextView) v.findViewById(R.id.end_work_attandence_place);
        end_work_status_tv = (TextView) v.findViewById(R.id.end_work_status_tv);
        attendance_tv = (TextView) v.findViewById(R.id.attendance_tv);
        end_work_time_tv = (TextView) v.findViewById(R.id.end_work_time_tv);
        start_work_time_tv = (TextView) v.findViewById(R.id.start_work_time_tv);
        failed_tv = v.findViewById(R.id.failed_tv);
        pouch_in_time = (TextView) v.findViewById(R.id.pouch_in_time);
        pouch_out_time = (TextView) v.findViewById(R.id.pouch_out_time);
        initLocation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.retry_location_tv:
                LocationActivity.startActivityForResult(getActivity(), LOCATION_REQUESTCODE);
                break;
            case R.id.attendance_layout:
                if (mLocation != null) {
                    isCommitData = true;
                    getAttendanceDetail(mLocation.getLatitude(), mLocation.getLongitude());
                } else {
                    showToast("获取定位信息失败，请重试");
                }
                break;
        }
    }

    /**
     * 获取当前城市地理位置
     */
    private void initLocation() {
        showDialog();
        mLocationClient = new LocationClient(getActivity());
        MyLocationListener listener = new MyLocationListener();
        mLocationClient.registerLocationListener(listener);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setScanSpan(10000);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    /**
     * 百度地图监听
     */
    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation arg0) {
            mLocation = arg0;
            Log.e("jianzhang", "arg0" + arg0.getLatitude() + ",long" + arg0.getLongitude());
            if (isFirstLocation) {
                isFirstLocation = false;
                getAttendanceDetail(arg0.getLatitude(), arg0.getLongitude());
            }
        }
    }

    public void getAttendanceDetail(double lat, double lng) {
        showDialog();
        AttendanceDetailRequest request = new AttendanceDetailRequest(MySharepreference.getInstance(getActivity()).getUser().id, lat, lng);
        httpGetRequest(request.getRequestUrl(), AttendanceDetailRequest.ATTENDANCE_DETAIL_REQUEST);
    }

    @Override
    protected void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        closeDialog();
        switch (action) {
            case AttendanceDetailRequest.ATTENDANCE_DETAIL_REQUEST:
                AttendanceAllEntity entity = JSONObject.parseObject(json, AttendanceAllEntity.class);
                if (entity == null || entity.map == null) {
                    showToast("获取打卡信息失败，请重试");
                    getActivity().finish();
                }
                mEntity = entity.map;
                setData();
                if (isCommitData) {
                    isCommitData = false;
                    if (mEntity.in_or_out == AttendanceEntity.UNNORMAL) {
                        showComfirmDialog();
                    } else {
                        commitData();
                    }
                }
                break;
            case AttandenceRequest.ATTANDENCE_REQUEST:
                showToast("打卡成功");
                getActivity().finish();
                break;
        }
    }

    private void setData() {
        if (1 == mEntity.is_no) {
            attendance_tv.setText("上班打卡");
        } else if (2 == mEntity.is_no) {
            attendance_tv.setText("下班打卡");
        }
//        if ("1".equals(mEntity.is_can_punch)) {
//            attendance_layout.setVisibility(View.VISIBLE);
//        } else {
//            attendance_layout.setVisibility(View.GONE);
//        }
        if (mEntity.is_complete == AttendanceEntity.UNNORMAL) {
            end_work_status_tv.setText(mEntity.sb_out);
            end_work_status_tv.setVisibility(View.VISIBLE);
            end_work_attandence_place.setVisibility(View.VISIBLE);
            end_work_attandence_place.setText(mEntity.punch_out_place);
//            attendance_layout.setVisibility(View.GONE);
        } else {
            end_work_status_tv.setVisibility(View.GONE);
            end_work_attandence_place.setVisibility(View.GONE);
        }
        if (mEntity.is_first == AttendanceEntity.UNNORMAL) {
            start_work_status_tv.setVisibility(View.VISIBLE);
            start_work_attandence_place.setVisibility(View.VISIBLE);
        } else {
            start_work_status_tv.setVisibility(View.GONE);
            start_work_attandence_place.setVisibility(View.GONE);
        }
        start_work_status_tv.setText(mEntity.sb_in);
        if (AttendanceEntity.UNNORMAL == mEntity.in_or_out) {
            loc_place_tv.setText("非考勤范围：" + mLocation.getAddrStr());
        } else {
            loc_place_tv.setText("已进入考勤范围：" + mLocation.getAddrStr());
        }
        start_work_time_tv.setText(mEntity.start_time);
        end_work_time_tv.setText(mEntity.end_time);
        start_work_attandence_place.setText(mEntity.punch_point);
        if (!StringUtil.isEmpty(mEntity.punch_hour_time)) {
            pouch_in_time.setText("打卡时间：" + mEntity.punch_hour_time);
        } else {
            pouch_in_time.setVisibility(View.GONE);
        }
        if (!StringUtil.isEmpty(mEntity.punch_out_time)) {
            pouch_out_time.setText("打卡时间：" + mEntity.punch_out_time);
        } else {
            pouch_out_time.setVisibility(View.GONE);
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        closeDialog();
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    private void commitData() {
        User user = MySharepreference.getInstance(getActivity()).getUser();
        AttandenceRequest request = new AttandenceRequest(mEntity.id, user.id, mLocation.getLatitude(), mLocation.getLongitude(),
                mEntity.is_no, mEntity.is_cd, mEntity.is_zt, mEntity.in_or_out, mLocation.getAddrStr(), "");
        httpPostRequest(request.getRequestUrl(), request.getRequestParams(), AttandenceRequest.ATTANDENCE_REQUEST);
    }

    private void showComfirmDialog() {
        CustomDialog dialog = new CustomDialog(getActivity(), "确认打卡", "取消", "确认", "当前为外勤打卡，是否打卡？", this);
        dialog.show();
    }

    @Override
    public void okClick() {
        commitData();
    }

    @Override
    public void cancelClick() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == LOCATION_REQUESTCODE) {
                getActivity().finish();
            }
        }
    }
}