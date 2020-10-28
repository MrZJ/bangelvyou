package com.shishoureport.system.request;

import com.shishoureport.system.ui.fragment.FileListFragment;

import java.util.Map;

/**
 * Created by jianzhang.
 * on 2017/9/13.
 * copyright easybiz.
 */

public class TaskDetailRequest extends BaseRequest {
    private String id;
    private static final String METHOD = "/mobile/taskinfo/view";
    private static final String METHOD1 = "/mobile/taskinfo/audit";
    public static final int TASK_DETAIL_REQUEST = 100080;
    private int type;

    public TaskDetailRequest(String id, int type) {
        super();
        this.id = id;
        this.type = type;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }

    @Override
    public String getRequestUrl() {
        StringBuilder builder = new StringBuilder(baseUrl);
        if (type == FileListFragment.TYPE_WAITE_SOLVED) {
            builder.append(METHOD1);
        } else {
            builder.append(METHOD);
        }
        builder.append("?id=").append(id);
        return builder.toString();
    }
}
