package com.shishoureport.system.request;

import android.content.Context;
import android.text.TextUtils;

import com.shishoureport.system.entity.ApplyPurchaseEntity;
import com.shishoureport.system.entity.ApplyWorkerEntity;
import com.shishoureport.system.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/7.
 * copyright easybiz.
 */

public class SaveApplyPurchaseRequest extends BaseRequest {
    public static final int SAVE_APPLY_PURCHASE_REQUEST = 1044;
    private static final String METHOD = "/mobile/purchaseOrder/save";
    private ApplyPurchaseEntity entity;

    public SaveApplyPurchaseRequest(ApplyPurchaseEntity entity, Context context) {
        super(context);
        this.entity = entity;
    }

    @Override
    public Map<String, String> getRequestParams() {
        HashMap<String, String> map = new HashMap<>();
        if (entity != null) {
            map.put("batch", entity.batch);
            map.put("place", entity.place);
            map.put("applyId", user.id);
            map.put("applyName", user.real_name);
            map.put("reciveId", entity.reciveId);
            map.put("reciveName", entity.reciveName);
            map.put("reciveDate", entity.reciveDate + " 00:00:00");
            map.put("reagentIds", entity.reagentIds);
            map.put("quantitys", entity.quantitys);
            map.put("remarks", entity.remarks);
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
