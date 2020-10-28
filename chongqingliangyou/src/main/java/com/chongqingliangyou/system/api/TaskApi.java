package com.chongqingliangyou.system.api;

import com.chongqingliangyou.system.util.ConfigUtil;
import com.chongqingliangyou.system.util.StringUtil;

/**
 * 任务接口
 */
public class TaskApi {
    public static String url;
    public static final String ACTION_TASK_LIST = "/Service.do?method=getWorkAssignedLinkList";
    public static final int API_TASK_LIST = 1;//获取任务列表数据
    public static final String ACTION_CHANGE_TASK = "/Service.do?method=updateWorkAssignedLink";
    public static final int API_CHANGE_TASK = 2;//修改任务状态
    public static final String ACTION_CHANGE_TASK_UPDATAIMG = "/UploaderServletForApp.do";
    public static final int API_CHANGE_TASK_UPDATAIMG = 3;//上传图片地址


    /**
     * 获取任务列表数据
     * 1、pager.requestPage请求页；
     * 2、pager.pageSize设置请求数量；
     * orderkey 排序的条件
     * @return
     */
    public static String getTaskListUrl(String type,String page, String curPage,String state, String keyword,String key){
        url = String.format(ACTION_TASK_LIST);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url).append("&key=")
                .append(key).append("&page=")
                .append(page).append("&curpage=").append(curPage);
        if(!StringUtil.isEmpty(keyword)){
            urlBuffer.append("&keyWord=").append(keyword);
        }
        if(!StringUtil.isEmpty(type)){
            urlBuffer.append("&type=").append(type);
        }
        if(!StringUtil.isEmpty(state)){
            urlBuffer.append("&state=").append(state);
        }
        return urlBuffer.toString();
    }

    /**
     * 获取修改任务状态接口
     * @return
     */
    public static String getTaskStateUrl(){
        url = String.format(ACTION_CHANGE_TASK);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP_URL);
        urlBuffer.append(url);
        return urlBuffer.toString();
    }

    /**
     * 获取修改任务状态接口
     * @return
     */
    public static String getUpdataTaskImgUrl(){
        url = String.format(ACTION_CHANGE_TASK_UPDATAIMG);
        StringBuffer urlBuffer = new StringBuffer(ConfigUtil.HTTP);
        urlBuffer.append(url);
        return urlBuffer.toString();
    }

}
