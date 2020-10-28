package com.zycreview.system.ui.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zycreview.system.R;
import com.zycreview.system.adapter.address.ArrayWheelAdapter;
import com.zycreview.system.api.RegisterApi;
import com.zycreview.system.entity.ComTypeData;
import com.zycreview.system.entity.ComTypeEntity;
import com.zycreview.system.entity.EntpTypeEntity;
import com.zycreview.system.entity.FieldErrors;
import com.zycreview.system.utils.AsynImageUtil;
import com.zycreview.system.utils.BitmapHelper;
import com.zycreview.system.utils.CacheImgUtil;
import com.zycreview.system.utils.ConfigUtil;
import com.zycreview.system.utils.ListUtils;
import com.zycreview.system.utils.StringUtil;
import com.zycreview.system.utils.ToastUtil;
import com.zycreview.system.wibget.address.OnWheelChangedListener;
import com.zycreview.system.wibget.address.WheelView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 注册界面
 **/
public class RegisterUserActivity extends BaseActivity implements View.OnClickListener, OnWheelChangedListener {

    private View et_firm_name, et_firm_introduce, et_firm_charter,
            upload_firm_charter, et_organization_code, upload_organization_code,
            et_legal_person,
            et_contact_person, et_mobile_number, et_cap_number,
            et_fax, et_mail_box, et_manage_address, et_mail_code, ll_parent;
    private EditText et_firm_name_text, et_firm_charter_text,
            et_organization_code_text, et_legal_person_text, et_legal_person_password_text,
            et_contact_person_text, et_mobile_number_text, et_cap_number_text, et_legal_person_password_ag_text,
            et_fax_text, et_mail_box_text, et_manage_address_text, et_mail_code_text;
    private ImageView upload_firm_charter_iv, upload_organization_code_iv;
    private TextView upload_firm_charter_text, upload_organization_code_text,
            tv_province, tv_city, tv_county, tv_firm_type, com_charge_et, com_addr_et, email_et,
            fax_et, gap_et, gmp_et, gsp_et, com_type_tv, work_range_et;
    private Button bt_commit;
    private String[] baseName;//类型名字
    private String[] baseId;//类型id
    private String[] comTypes;//企业主体性质
    private int selComIndex = -1;
    private List<ComTypeEntity> dataList;
    /**
     * 当前省的名称 注册的当前省
     */
    private String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    private String mCurrentCityName;
    /**
     * 当前区的名称
     */
    private String mCurrentDistrictName = "";

    /**
     * 当前区的邮政编码
     */
    private String mCurrentZipCode = "";

    // 选择地区相关
    private PopupWindow pop = null;
    private WheelView mViewProvince, mViewCity, mViewDistrict, mViewType;
    private TextView tv_confirm, tv_close;
    private String entp_type;//选择的企业类型

    //上传图片
    private static final int TAKE_PICTURE_CA = 1;//相机获取
    private static final int LOCATION_PHOTO = 2;//相册获取
    private Uri picUri = null;
    private PopupWindow popupWindow;
    private Button btnPhotograph, btnLocation, btnCancel;
    private Animation mRotateAnimation;
    private boolean isLoading;
    private Callback.Cancelable cancelable;
    private int typeImg = 0;
    private String url1;//营业执照url
    private String url2;//组织机构url
    private String imgName;//图片名称

    private boolean[] checked;//默认企业类型为未选中
    private List<String> checkedType;//记录的企业类型
    private List<String> checkedTypeId;//记录企业类型id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        initView();
        initListener();
    }

    private void initView() {
        initTopView();
        setLeftBackButton();
        setTitle("注册");
        initLoadingView();
        ll_parent = findViewById(R.id.ll_parent);
        et_firm_name = findViewById(R.id.et_firm_name);
        et_firm_name_text = (EditText) et_firm_name.findViewById(R.id.et_register);
        tv_firm_type = (TextView) findViewById(R.id.tv_firm_type);
        et_firm_charter = findViewById(R.id.et_firm_charter);
        et_firm_charter_text = (EditText) et_firm_charter.findViewById(R.id.et_register);
        upload_firm_charter = findViewById(R.id.upload_firm_charter);
        upload_firm_charter_iv = (ImageView) findViewById(R.id.upload_firm_charter).findViewById(R.id.iv_firm_charter);
        upload_firm_charter_text = (TextView) findViewById(R.id.upload_firm_charter).findViewById(R.id.tv_firm_charter);
        et_organization_code = findViewById(R.id.et_organization_code);
        et_organization_code_text = (EditText) et_organization_code.findViewById(R.id.et_register);
        upload_organization_code = findViewById(R.id.upload_organization_code);
        upload_organization_code_iv = (ImageView) upload_organization_code.findViewById(R.id.iv_firm_charter);
        upload_organization_code_text = (TextView) upload_organization_code.findViewById(R.id.tv_firm_charter);
        /*upload_other_certificates = findViewById(R.id.upload_other_certificates);
        upload_other_certificates_iv = (ImageView) upload_other_certificates.findViewById(R.id.iv_firm_charter);
        upload_other_certificates_text = (TextView) upload_other_certificates.findViewById(R.id.tv_firm_charter);*/
        et_contact_person = findViewById(R.id.et_contact_person);
        et_contact_person_text = (EditText) et_contact_person.findViewById(R.id.et_register);
        et_mobile_number = findViewById(R.id.et_mobile_number);
        et_mobile_number_text = (EditText) et_mobile_number.findViewById(R.id.et_register);
        et_cap_number = findViewById(R.id.et_cap_number);
        et_cap_number_text = (EditText) et_cap_number.findViewById(R.id.et_register);
        //用户注册
        et_legal_person = findViewById(R.id.et_legal_person);
        et_legal_person_text = (EditText) et_legal_person.findViewById(R.id.et_register);
        et_legal_person_password_text = (EditText) findViewById(R.id.et_legal_person_password_text);
        et_legal_person_password_ag_text = (EditText) findViewById(R.id.et_legal_person_password_ag_text);
        /*et_fax = findViewById(R.id.et_fax);
        et_fax_text = (EditText) et_fax.findViewById(R.id.et_register);
        et_mail_box = findViewById(R.id.et_mail_box);
        et_mail_box_text = (EditText) et_mail_box.findViewById(R.id.et_register_gray);
        et_manage_address = findViewById(R.id.et_manage_address);
        et_manage_address_text = (EditText) et_manage_address.findViewById(R.id.et_register);
        et_mail_code = findViewById(R.id.et_mail_code);
        et_mail_code_text = (EditText) et_mail_code.findViewById(R.id.et_register_gray);*/
        tv_province = (TextView) findViewById(R.id.tv_province);
        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_county = (TextView) findViewById(R.id.tv_county);
//        mTv_jingdu_weidu = (TextView) findViewById(R.id.tv_jingdu_weidu);
        bt_commit = (Button) findViewById(R.id.bt_commit);

        //选择地区相关
        View addressView = getLayoutInflater().inflate(R.layout.pop_address_item, null);
        pop = new PopupWindow(this);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(addressView);
        pop.setAnimationStyle(R.style.popwin_anim_style);
        mViewProvince = (WheelView) addressView.findViewById(R.id.id_province);
        mViewCity = (WheelView) addressView.findViewById(R.id.id_city);
        mViewDistrict = (WheelView) addressView.findViewById(R.id.id_district);
        tv_confirm = (TextView) addressView.findViewById(R.id.tv_confirm);
        tv_close = (TextView) addressView.findViewById(R.id.tv_close);

        //选择图片相关
        View popupView = View.inflate(this, R.layout.photo_more, null);
        popupWindow = new PopupWindow(this);
        popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(popupView);
        btnPhotograph = (Button) popupView.findViewById(R.id.btn_photo_more_photograph);
        btnLocation = (Button) popupView.findViewById(R.id.btn_photo_more_location);
        btnCancel = (Button) popupView.findViewById(R.id.btn_photo_more_cancel);

        et_firm_name_text.setHint("企业名称");
        et_firm_charter_text.setHint("营业执照号");
        et_organization_code_text.setHint("统一社会信用码");
        et_contact_person_text.setHint("联系人");
        et_mobile_number_text.setHint("联系电话");
//        et_mobile_number_text.setInputType(InputType.TYPE_CLASS_NUMBER);
        et_cap_number_text.setHint("CAP编号");
        et_legal_person_text.setHint("用户名");
        et_legal_person_password_text.setHint("请输入密码");
        et_legal_person_password_ag_text.setHint("请再次输入密码");
        /*et_fax_text.setHint("传真");
        et_mail_box_text.setHint("电子邮箱");
        et_manage_address_text.setHint("经营地址");
        et_mail_code_text.setHint("邮编");*/
        upload_firm_charter_iv.setImageResource(R.mipmap.org_code);
        upload_firm_charter_text.setText("上传营业执照");
        upload_organization_code_iv.setImageResource(R.mipmap.org_code);
        upload_organization_code_text.setText("上传组织机构代码证");

        //新增条目
        com_charge_et = (TextView) findViewById(R.id.com_charge_et);
        com_addr_et = (TextView) findViewById(R.id.com_addr_et);
        email_et = (TextView) findViewById(R.id.email_et);
        fax_et = (TextView) findViewById(R.id.fax_et);
        gap_et = (TextView) findViewById(R.id.gap_et);
        gmp_et = (TextView) findViewById(R.id.gmp_et);
        gsp_et = (TextView) findViewById(R.id.gsp_et);
        com_type_tv = (TextView) findViewById(R.id.com_type_tv);
        work_range_et = (TextView) findViewById(R.id.work_range_et);
        com_type_tv.setOnClickListener(this);

        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(this, ConfigUtil.shanxiProvince));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
    }

    /**
     * 初始化数据加载布局
     */
    private void initLoadingView() {
        /**数据加载布局*/
        emptyView = findViewById(R.id.empty_view_mini);
        tipTextView = (TextView) findViewById(R.id.tip_text_mini);
        loadView = findViewById(R.id.loading_img_mini);

        if (null != emptyView) {
            View view = findViewById(R.id.loading_layout_mini);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();

            if (null != params) {
                DisplayMetrics displayMetrics = getResources()
                        .getDisplayMetrics();

                int height = displayMetrics.heightPixels;
                params.topMargin = (int) (height * 0.25);
                view.setLayoutParams(params);
            }
            mRotateAnimation = AsynImageUtil.mRotateAnimation;

            mRotateAnimation
                    .setInterpolator(AsynImageUtil.ANIMATION_INTERPOLATOR);
            mRotateAnimation
                    .setDuration(AsynImageUtil.ROTATION_ANIMATION_DURATION_SHORT);
            mRotateAnimation.setRepeatCount(Animation.INFINITE);
            mRotateAnimation.setRepeatMode(Animation.RESTART);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
        setBtnClickable();
    }

    private void setData() {
        updateCities();
        updateAreas();
        getTypeData();
        getComTypeData();
    }

    @Override
    public void initListener() {
        super.initListener();
        initSideBarListener();
        upload_firm_charter.setOnClickListener(this);
        upload_organization_code.setOnClickListener(this);
//        upload_other_certificates.setOnClickListener(this);
        tv_city.setOnClickListener(this);
        tv_county.setOnClickListener(this);
        tv_province.setOnClickListener(this);
        bt_commit.setOnClickListener(this);

        //选择地区相关
        // 添加change事件
//        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
        // 添加onclick事件
        tv_confirm.setOnClickListener(this);
        tv_close.setOnClickListener(this);
        tv_firm_type.setOnClickListener(this);

        btnCancel.setOnClickListener(this);
        btnLocation.setOnClickListener(this);
        btnPhotograph.setOnClickListener(this);
    }

    private void getTypeData() {
        httpGetRequest(RegisterApi.getEntpTypeUrl(), RegisterApi.API_GET_ENTPTYPE);
    }

    private void getComTypeData() {
        httpGetRequest(RegisterApi.getComTypeUrl(), RegisterApi.API_GET_COM_TYPE);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_firm_type:
                if (baseName != null) {
                    showCheckType();
                } else {
                    Toast.makeText(this, "暂无企业类型", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.upload_firm_charter://上传营业执照
                popupWindow.showAtLocation(ll_parent,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                typeImg = 1;
                imgName = "";
                softKeyHide();
                break;
            case R.id.upload_organization_code://上传组织机构代码证
                popupWindow.showAtLocation(ll_parent,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                typeImg = 2;
                imgName = "";
                softKeyHide();
                break;
            case R.id.tv_province://选择省份
            case R.id.tv_city://选择市区
            case R.id.tv_county://选择县城
                pop.showAtLocation(ll_parent, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.bt_commit://提交
                commitData();
                break;
            case R.id.tv_confirm://地区选择确定
                pop.dismiss();
                tv_province.setText(mCurrentProviceName);
                tv_city.setText(mCurrentCityName);
                tv_county.setText(mCurrentDistrictName);
                break;
            case R.id.tv_close:
                pop.dismiss();
                break;
            case R.id.btn_photo_more_photograph:
                photograph();
                popupWindowHide();
                break;
            case R.id.btn_photo_more_location:
                locationPhoto();
                popupWindowHide();
                break;
            case R.id.btn_photo_more_cancel:
                popupWindowHide();
                break;
            case R.id.com_type_tv:
                showComType();
                break;
        }
    }

    /**
     * 选择企业类型
     */
    private void showCheckType() {
        checked = new boolean[baseName.length];
        checkedType = new ArrayList<>();
        checkedTypeId = new ArrayList<>();
        tv_firm_type.setText("企业类型");
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("选择企业类型");
        //  设置多选项
        builder.setMultiChoiceItems(baseName, checked, new DialogInterface.OnMultiChoiceClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1, boolean arg2) {
                if (arg2) {
                    checkedType.add(baseName[arg1]);
                    checkedTypeId.add(baseId[arg1]);
                } else {
                    checkedType.remove(baseName[arg1]);
                    checkedTypeId.remove(baseId[arg1]);
                }
            }
        });
        //  设置确定按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if (!ListUtils.isEmpty(checkedType)) {
                    StringBuffer result = new StringBuffer();
                    StringBuffer resultId = new StringBuffer();
                    for (int i = 0; i < checkedType.size(); i++) {
                        if (checkedType.size() == 1 || i == (checkedType.size() - 1)) {
                            result.append(checkedType.get(i));
                            resultId.append(checkedTypeId.get(i));
                        } else {
                            result.append(checkedType.get(i) + ",");
                            resultId.append(checkedTypeId.get(i) + ",");
                        }
                    }
                    entp_type = resultId.toString();
                    tv_firm_type.setText(result.toString());
                } else {
                    tv_firm_type.setText("企业类型");
                }
            }
        });
        //  设置取消按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        builder.create().show();
    }

    /**
     * 选择企业类型
     */
    private void showComType() {
        if (comTypes == null) {
            Toast.makeText(this, "无企业主体性质", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("企业主体性质");
        //  设置多选项
        builder.setSingleChoiceItems(comTypes, selComIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selComIndex = which;
                dialog.dismiss();
                com_type_tv.setText(comTypes[which]);
            }
        });
//        //  设置确定按钮
//        builder.setPositiveButton("确定", );
//        //  设置取消按钮
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface arg0, int arg1) {
//
//            }
//        });
        builder.create().show();
    }

    /**
     * 隐藏键盘
     */
    private void softKeyHide() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * 隐藏弹出窗口
     */
    private void popupWindowHide() {
        popupWindow.dismiss();
    }

    /**
     * 走拍照的流程
     */
    private void photograph() {
        String imgPath;
        if (typeImg == 1) {
            imgPath = CacheImgUtil.PATH_CACHE + "/营业执照.jpg";
            imgName = "营业执照.jpg";
        } else {
            imgPath = CacheImgUtil.PATH_CACHE + "/组织机构代码证.jpg";
            imgName = "组织机构代码证.jpg";
        }
        File file = new File(imgPath);
        picUri = Uri.fromFile(file);
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE_CA);
    }

    /**
     * 走相册流程
     */
    private void locationPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, LOCATION_PHOTO);
    }

    /**
     * 返回方法获取到头像
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE_CA://相机获取图片
                if (resultCode == RESULT_OK) {
                    updataImg(picUri);
                }
                break;
            case LOCATION_PHOTO://相册获取图片
                if (resultCode == RESULT_OK) {
                    try {
                        String imgpath1 = CacheImgUtil.getImageAbsolutePath(this, data.getData());
                        imgName = imgpath1.substring(imgpath1.lastIndexOf("/") + 1);
                        String imgpath = CacheImgUtil.PATH_CACHE + "/" + System.currentTimeMillis() + ".jpg";
                        if (!copyFile(imgpath1, imgpath)) {
                            ToastUtil.showToast("返回图片出错", this, ToastUtil.DELAY_SHORT);
                            return;
                        }
                        picUri = Uri.fromFile(new File(imgpath));
                        updataImg(picUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    /**
     * 处理图片并上传
     *
     * @param uri
     */
    private void updataImg(Uri uri) {
        showUpLoading("正在上传中...", true);
        final String imgpath = CacheImgUtil.getImageAbsolutePath(this, uri);
        File tempFile = new File(imgpath);
        if (!tempFile.exists()) {
            ToastUtil.showToast("图片不存在", this, ToastUtil.DELAY_SHORT);
            hiddenUpLoading();
            return;
        }
        final Bitmap bps = BitmapHelper.compressBitmap(imgpath, BitmapHelper.width, BitmapHelper.height);
        if (null == bps) {
            ToastUtil.showToast("图片压缩失败", this, ToastUtil.DELAY_SHORT);
            hiddenUpLoading();
            return;
        }
        String tmppath = BitmapHelper.compressBitmap(this, imgpath, bps, true);
        final File tempFilemin = new File(tmppath);
        if (!tempFilemin.exists()) {
            ToastUtil.showToast("图片不存在", this, ToastUtil.DELAY_SHORT);
            hiddenUpLoading();
            return;
        }
        RequestParams params = new RequestParams(RegisterApi.getActionUpdatePhotoUrl());
        params.setMultipart(true);
//        params.addQueryStringParameter("key", configEntity.key);
        params.addQueryStringParameter("type", "image");
        params.addBodyParameter(tempFilemin.getPath().replace("/", ""), tempFilemin);
//        params.addBodyParameter("fileidname", tempFilemin);
        cancelable = x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String s) {
                String text = s;
                if (StringUtil.isEmpty(text)) {
                    ToastUtil.showToast("上传失败,没有返回数据", RegisterUserActivity.this, ToastUtil.DELAY_SHORT);
                    hiddenUpLoading();
                    return;
                }
                JSONObject json = JSON.parseObject(text);
                String ret = json.getString("code");
                String msg = json.getString("msg");
                if (typeImg == 1) {
                    url1 = json.getString("url");
                    if (!StringUtil.isEmpty(imgName)) {
                        upload_firm_charter_text.setText(imgName);
                    }
                } else {
                    url2 = json.getString("url");
                    if (!StringUtil.isEmpty(imgName)) {
                        upload_organization_code_text.setText(imgName);
                    }
                }
                tempFilemin.delete();
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                if (!StringUtil.isEmpty(throwable.getMessage())) {
                    Toast.makeText(RegisterUserActivity.this, "上传图片失败", Toast.LENGTH_SHORT).show();
                    tempFilemin.delete();
                }
                hiddenUpLoading();
            }

            @Override
            public void onCancelled(CancelledException e) {
                tempFilemin.delete();
            }

            @Override
            public void onFinished() {
                hiddenUpLoading();
            }
        });
    }

    /**
     * 显示加载布局
     */
    public void showUpLoading(final String msg, final boolean load) {
        isLoading = true;
        mHandler.post(new Runnable() {

            public void run() {
                if (null != emptyView) {
                    tipTextView.setText(msg);
                    emptyView.setVisibility(View.VISIBLE);
                    loadView.setVisibility(View.VISIBLE);
                    loadView.startAnimation(mRotateAnimation);
                }
            }
        });

    }

    /**
     * dimiss加载布局
     */
    public void hiddenUpLoading() {
        mHandler.post(new Runnable() {
            public void run() {
                if (null != emptyView) {
                    emptyView.setVisibility(View.GONE);
                    loadView.clearAnimation();
                }

                isLoading = false;
            }
        });
    }


    /**
     * 复制文件
     *
     * @param oldPath 原路径
     * @param newPath 新路径
     * @return
     */
    public boolean copyFile(String oldPath, String newPath) {
        InputStream inStream = null;
        FileOutputStream fs = null;
        try {
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (!oldfile.exists() || !oldfile.isFile() || !oldfile.canRead()) {
                return false;
            }
            inStream = new FileInputStream(oldPath); //读入原文件
            fs = new FileOutputStream(newPath);
            byte[] buffer = new byte[1444];
            while ((byteread = inStream.read(buffer)) != -1) {
                fs.write(buffer, 0, byteread);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                    inStream = null;
                } catch (Exception e) {

                }
            }
            if (fs != null) {
                try {
                    fs.close();
                    fs = null;
                } catch (Exception e) {

                }
            }
        }
        return false;
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = ConfigUtil.mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = ConfigUtil.mZipcodeDatasMap.get(mCurrentDistrictName);
        }
    }

    /**
     * 根据当前的市,更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = ConfigUtil.mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = ConfigUtil.mDistrictDatasMap.get(mCurrentCityName);
        if (areas == null) {
            areas = new String[]{""};
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
        mViewDistrict.setCurrentItem(0);
        mCurrentDistrictName = ConfigUtil.mDistrictDatasMap.get(mCurrentCityName)[0];
        mCurrentZipCode = ConfigUtil.mZipcodeDatasMap.get(mCurrentDistrictName);
    }

    /**
     * 根据当前的省,更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = ConfigUtil.shanxiProvince[pCurrent];
        String[] cities = ConfigUtil.mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }

    /**
     * 提交按钮触发事件
     */
    private void commitData() {
        if (checkData()) {
            httpPostRequest(RegisterApi.getRegisterUrl(),
                    getRequestParams(), RegisterApi.API_REGISTER);
        }
    }

    /**
     * 获取登录参数
     *
     * @return
     */
    private HashMap<String, String> getRequestParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_name", et_legal_person_text.getText().toString());
        params.put("password", et_legal_person_password_text.getText().toString());
        params.put("entp_name", et_firm_name_text.getText().toString());
        params.put("entp_type", entp_type);
        params.put("entp_license", et_firm_charter_text.getText().toString());
        if (!StringUtil.isEmpty(url1)) {
            params.put("entp_license_path", url1);
        }
        params.put("entp_code", et_organization_code_text.getText().toString());
        if (!StringUtil.isEmpty(url2)) {
            params.put("entp_code_path", url2);
        }
        if (!StringUtil.isEmpty(et_contact_person_text.getText().toString())) {
            params.put("link_man", et_contact_person_text.getText().toString());
        }
        params.put("link_mobile", et_mobile_number_text.getText().toString());
        params.put("cap_code", et_cap_number_text.getText().toString());
        if (!StringUtil.isEmpty(mCurrentZipCode)) {
            params.put("p_index", mCurrentZipCode);
        }
        params.put("entp_legal", com_charge_et.getText().toString());
        if (!StringUtil.isEmpty(com_addr_et.getText().toString())) {
            params.put("entp_addr", com_addr_et.getText().toString());
        }
        if (!StringUtil.isEmpty(email_et.getText().toString())) {
            params.put("e_mail", email_et.getText().toString());
        }
        if (!StringUtil.isEmpty(email_et.getText().toString())) {
            params.put("e_mail", email_et.getText().toString());
        }
        if (!StringUtil.isEmpty(fax_et.getText().toString())) {
            params.put("fax", fax_et.getText().toString());
        }
        if (!StringUtil.isEmpty(gap_et.getText().toString())) {
            params.put("gap_code", gap_et.getText().toString());
        }
        if (!StringUtil.isEmpty(gmp_et.getText().toString())) {
            params.put("gmp_code", gmp_et.getText().toString());
        }
        if (!StringUtil.isEmpty(gsp_et.getText().toString())) {
            params.put("gsp_code", gsp_et.getText().toString());
        }
        if (!StringUtil.isEmpty(work_range_et.getText().toString())) {
            params.put("manager_scope", work_range_et.getText().toString());
        }
        try {
            if (dataList != null && selComIndex != -1) {
                params.put("nature_type", dataList.get(selComIndex).nature_id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }

    /**
     * 必填项是否有空
     *
     * @return
     */
    private boolean checkData() {
        if (!checkDataIsFalse(et_firm_name_text, "企业名称不能为空!")) {
            return false;
        }
        if (tv_firm_type.getText().toString().equals("企业类型")) {
            Toast.makeText(this, "企业类型不能为空!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!checkDataIsFalse(et_firm_charter_text, "营业执照号不能为空!")) {
            return false;
        }
//        if (StringUtil.isEmpty(url1)) {
//            Toast.makeText(this, "请上传营业执照图片", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        if (!checkDataIsFalse(et_organization_code_text, "统一社会信用码不能为空!")) {
            return false;
        }
//        if (StringUtil.isEmpty(url2)) {
//            Toast.makeText(this, "请上传组织机构代码证图片", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        if (!checkDataIsFalse(et_contact_person_text, "联系人不能为空!")) {
            return false;
        }
        if (!checkDataIsFalse(et_mobile_number_text, "联系电话不能为空!")) {
            return false;
        }
        if (!checkDataIsFalse(et_cap_number_text, "CAP编码不能为空!")) {
            return false;
        }
        if (!checkDataIsFalse(et_legal_person_text, "用户名不能为空!")) {
            return false;
        }
        if (!checkDataIsFalse(et_legal_person_password_text, "密码不能为空!")) {
            return false;
        }
        if (!checkDataIsFalse(et_legal_person_password_ag_text, "确认密码不能为空!")) {
            return false;
        }
        if (!et_legal_person_password_text.getText().toString()
                .equals(et_legal_person_password_ag_text.getText().toString())) {
            Toast.makeText(this, "两次密码不一致!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (tv_county.getText().equals("区")) {
            Toast.makeText(this, "区域不能为空!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!checkDataIsFalse(com_charge_et, "企业法人不能为空!")) {
            return false;
        }
        /*if (!checkDataIsFalse(et_fax_text, "传真不能为空!")) {
            return false;
        }
        if (!checkDataIsFalse(tv_province, "省份不能为空!")) {
            return false;
        }
        if (!checkDataIsFalse(tv_city, "市区不能为空!")) {
            return false;
        }
        if (!checkDataIsFalse(tv_county, "县城不能为空!")) {
            return false;
        }
        if (!checkDataIsFalse(et_manage_address_text, "经营地址不能为空!")) {
            return false;
        }*/
        return true;
    }

    @Override
    public void httpResponse(String json, int action) {
        super.httpResponse(json, action);
        switch (action) {
            case RegisterApi.API_REGISTER:
                Toast.makeText(this, "企业注册成功,请等待管理审核！", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case RegisterApi.API_GET_ENTPTYPE:
                entpHander(json);
                break;
            case RegisterApi.API_GET_COM_TYPE:
                handComType(json);
                break;
        }
    }

    private void handComType(String json) {
        try {
            ComTypeData comTypeData = JSONObject.parseObject(json, ComTypeData.class);
            if (comTypeData != null && !ListUtils.isEmpty(comTypeData.dataList)) {
                dataList = comTypeData.dataList;
                comTypes = new String[comTypeData.dataList.size()];
                for (int i = 0; i < comTypeData.dataList.size(); i++) {
                    comTypes[i] = comTypeData.dataList.get(i).nature_name;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理企业类型
     *
     * @param str
     */
    private void entpHander(String str) {
        if (!StringUtil.isEmpty(str)) {
            EntpTypeEntity entpTypeEntity = JSON.parseObject(str, EntpTypeEntity.class);
            if (entpTypeEntity != null) {
                List<EntpTypeEntity.EntpType> type = entpTypeEntity.dataList;
                if (!ListUtils.isEmpty(type)) {
                    baseName = new String[type.size()];
                    baseId = new String[type.size()];
                    for (int i = 0; i < type.size(); i++) {
                        baseName[i] = type.get(i).base_name;
                        baseId[i] = type.get(i).base_id;
                    }
                }
            }
        }
    }

    @Override
    protected void httpError(FieldErrors error, int action) {
        super.httpError(error, action);
        setBtnClickable();
    }

    /**
     * 设置按钮可以点击
     */
    private void setBtnClickable() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindowHide();
                return true;
            }
            if (cancelable != null && !cancelable.isCancelled()) {
                cancelable.cancel();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
