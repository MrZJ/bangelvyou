package com.shishoureport.system.request;

import android.content.Context;
import android.text.TextUtils;

import com.shishoureport.system.entity.ApplyPurchaseEntity;
import com.shishoureport.system.entity.ApplyUseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/7.
 * copyright easybiz.
 */

public class SaveApplyUseRequest extends BaseRequest {
    public static final int SAVE_APPLY_USE_REQUEST = 10424;
    private static final String METHOD = "/mobile/applyOrder/save";
    private ApplyUseEntity entity;

    public SaveApplyUseRequest(ApplyUseEntity entity, Context context) {
        super(context);
        this.entity = entity;
    }

    @Override
    public Map<String, String> getRequestParams() {
        HashMap<String, String> map = new HashMap<>();
        if (entity != null) {
            map.put("batch", entity.batch);
            map.put("reagentIds", entity.reagentIds/*"34"*/);
            map.put("quantitys", entity.quantitys/*"10"*/);
            map.put("remarks", entity.remarks/*"remarks"*/);
            if (!TextUtils.isEmpty(entity.remark)) {
                map.put("remark", entity.remark);
            }
            map.put("user_names", entity.audit_name);
            map.put("user_ids", entity.audit_uid);
            map.put("is_submit", "1");
            map.put("user_id", user.id);
        }
        return map;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        builder.append(METHOD);
        return builder.toString();
    }


}
