package com.shenmaireview.system.listener;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shenmaireview.system.ui.fragment.BaseFragment;

import java.util.Map;


public class MyWebViewClient extends WebViewClient{

	private Map<String, Object> title_map;
	private String config_url;
	private BaseFragment mFragment;
    
	public MyWebViewClient(Map<String, Object> title_map,String config_url,BaseFragment fragment) {
		this.title_map=title_map;
		this.config_url=config_url;
		mFragment=fragment;
	}
	
	@Override
	public void onPageStarted(WebView view, final String url, Bitmap favicon) {
		/*if (null != title_map.get(url)) {
			mFragment.setTopTitle(title_map.get(url).toString());
		}*/
		//头条 搜索按钮是否显示
		if(!config_url.equals(url)){
			mFragment.setLeftBackButton(true);
		}else{
			mFragment.setLeftBackButton(false);
		}
		/*if( url != null && ((config_url).equals(url) ||
				(!url.contains(ConfigUtil.HTTP_ONE_SEARCH_TYPE) &&     //去除不匹配
						url.contains(ConfigUtil.HTTP_TWO_SUBSTRING)))){    //拿到不匹配筛选后的匹配的
			  mFragment.setLeftBackButton(false);
			if(ConfigUtil.HTTP_ONE.equals(url)){
			  mFragment.setRightSearchButton(true);
			}
			if(ConfigUtil.HTTP_ONE.equals(url) || url.contains(ConfigUtil.HTTP_TWO)){
				mFragment.setLeftLocationButton(true);
			}
    	} else {
    		mFragment.setLeftBackButton(true);
    	}*/
		super.onPageStarted(view, url, favicon);
	}
	
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
//		Log.e("ssLoading",view.getTitle());
		/*if(url != null && (url.startsWith("share:"))){
			String info[] = url.split(":");
			if(null != info[1]){
//				mFragment.httpGetRequest(ShareApi.getCheckTokenUrl(info[1],info[2]), ShareApi.API_SHARE_CONTENT);
			}
			return true;
		}*/
		/*//截取分享地址
		if(url != null && (url.contains("appshare:"))){
			String info = (String) url.substring(url.indexOf(":")+1);
			if(null != info){
//				mFragment.showShare(GetApplicationName.getApplicationName(), info);
			}
			return true;
		}*/
		if(url != null && (url.startsWith("imageclick:"))){
			/*String info[] = url.split(":");
			Intent galleyIntent = new Intent(mFragment.getActivity(), GalleyImageActivity.class);
    		if(null != info[2] && Integer.parseInt(info[2]) > 0){
    			String[] imgsUrl = new String[Integer.parseInt(info[2])];
    			if(null != ConfigUtil.detailImgs){
    				for (int i = 0; i < Integer.parseInt(info[2]); i++) {
        				imgsUrl[i] = ConfigUtil.detailImgs[i];
					}
        			galleyIntent.putExtra("url", imgsUrl);
            		galleyIntent.putExtra("index", Integer.parseInt(info[1]));
            		mFragment.startActivity(galleyIntent);
    			}
    		}*/
			return true;
		}
		/*if(url != null && (url.startsWith("objc:"))){
			String info[] = url.split(":");
			ConfigEntity configEntity = ConfigUtil.loadConfig(mFragment.getActivity());
			configEntity.key = info[1];
			ConfigUtil.saveConfig(mFragment.getActivity(), configEntity);
			if(!config_url.equals(ConfigUtil.HTTP_MINE_URL)){
    			MainActivity mainActivity = (MainActivity) mFragment.getActivity();
				mainActivity.reportFragment.onResume();
			}else{
				view.loadUrl(ConfigUtil.HTTP_MINE_URL);    //第五个Fragment不同之处
			}
			return true;
		}
		//第五个Fragment不同之处
		if(config_url.equals(ConfigUtil.HTTP_MINE_URL)){
			if(url != null && (url.startsWith("zhuxiao:"))){
				ConfigEntity configEntity = ConfigUtil.loadConfig(mFragment.getActivity());
				configEntity.key = "";
				ConfigUtil.saveConfig(mFragment.getActivity(), configEntity);
    			view.loadUrl(ConfigUtil.HTTP_MINE_URL);
				return true;
    		}
		}*/
		if (url != null && (url.startsWith("mailto:") || url.startsWith("geo:") || url.startsWith("tel:"))) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			mFragment.startActivity(intent);
			return true;
		}
		view.loadUrl(url);
		return true;
	}

	@Override  
	public void onPageFinished(WebView view, String url){  
		super.onPageFinished(view, url);
//		Log.e("ssEnd",view.getTitle());
//		title_map.put(url,view.getTitle());
		mFragment.setTopTitle(view.getTitle());
	}  
	
}
