package com.yishangshuma.bangelvyou.listener;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.yishangshuma.bangelvyou.util.AsyncExecuter;
import com.yishangshuma.bangelvyou.util.CacheImgUtil;
import com.yishangshuma.bangelvyou.util.ConfigUtil;
import com.yishangshuma.bangelvyou.util.StringUtil;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

/**
 * 分享点击监听事件
 *
 */
public class ShareClickListener implements OnClickListener{

	private Context ctx;
	private ImageView img_share;
	private Animation mRotateAnimation;
	private View view;
	private String customText = "#藏北梦旅#", imageUrl, downLoadUrl;
	private String goodsID = "";
	private String newsUrl = "";//新闻地址
	private boolean silent;
	private URL urlStr;  
	private HttpURLConnection connection;  
	private int state = -1;  
	private boolean succ;
	public ShareClickListener(Context ctx){
		this.ctx = ctx;
	}
	
	@Override
	public void onClick(View view) {
		this.view = view;
		showShare(true);
	}
	
	public void setShareImg(ImageView img_share){
		this.img_share = img_share;
	}
	
	/**
	 * 设置分享图片
	 * @param imageUrl
	 */
	public void setShareImgUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}
	
	/**
	 * 设置分享正文
	 * @param content
	 */
	public void setContent(String content){
		if(!StringUtil.isEmpty(content)){
			customText = content + "#班戈#";
		}
	}
	/**
	 * 设置分享正文
	 */
	public void setGoodsID(String goodsId){
		if(!StringUtil.isEmpty(goodsId)){
			goodsID = goodsId;
		}
	}

	/**
	 * 设置分享正文
	 */
	public void setNewsUrl(String id){
		if(!StringUtil.isEmpty(id)){
			newsUrl = ConfigUtil.HTTP_URL_NEWS_DETAIL + id;
		}
	}

	/**
	 * 设置分享链接下载地址
	 */
	public void setDownloadUrl(String loadUrl){
		if(!StringUtil.isEmpty(loadUrl)){
			downLoadUrl = loadUrl;
		}
	}
	
	// 使用快捷分享完成分享（请务必仔细阅读位于SDK解压目录下Docs文件夹中OnekeyShare类的JavaDoc）
	/**ShareSDK集成方法有两种</br>
	 * 1、第一种是引用方式，例如引用onekeyshare项目，onekeyshare项目再引用mainlibs库</br>
	 * 2、第二种是把onekeyshare和mainlibs集成到项目中，本例子就是用第二种方式</br>
	 * 请看“ShareSDK 使用说明文档”，SDK下载目录中 </br>
	 * 或者看网络集成文档 http://wiki.mob.com/Android_%E5%BF%AB%E9%80%9F%E9%9B%86%E6%88%90%E6%8C%87%E5%8D%97
	 * 3、混淆时，把sample或者本例子的混淆代码copy过去，在proguard-project.txt文件中
	 *
	 *
	 * 平台配置信息有三种方式：
	 * 1、在我们后台配置各个微博平台的key
	 * 2、在代码中配置各个微博平台的key，http://mob.com/androidDoc/cn/sharesdk/framework/ShareSDK.html
	 * 3、在配置文件中配置，本例子里面的assets/ShareSDK.conf,
	 * 
	 * silent:表示直接分享,没有界面.
	 */
	private void showShare(boolean silent) {
		this.silent = silent;
		checkImgUrl();
	}

	private void shareByCheckImgUrl(boolean silent) {
		final OnekeyShare oks = new OnekeyShare();
		oks.setTitle(customText);
		oks.setTitleUrl("http://www.baidu.com");
		oks.setUrl("http://www.baidu.com");
		oks.setSiteUrl("http://www.baidu.com");
		if (null != downLoadUrl && !"".equals(downLoadUrl)) {
			oks.setTitleUrl(downLoadUrl);
			oks.setUrl(downLoadUrl);
			oks.setSiteUrl(downLoadUrl);
		}else{
			if(null != goodsID && !"".equals(goodsID)){
				String goodsUrl = "http://www.ckshangcheng.cn/service/AppEntpInfo.do?id="+goodsID;
				oks.setTitleUrl(goodsUrl);
				oks.setUrl(goodsUrl);
				oks.setSiteUrl(goodsUrl);
			}
		}
		if(!StringUtil.isEmpty(newsUrl)){
			oks.setTitleUrl(newsUrl);
			oks.setUrl(newsUrl);
			oks.setSiteUrl(newsUrl);
		}
		oks.setText("藏北梦旅");
		if(StringUtil.isEmpty(imageUrl) || !succ){//商品图片为空时 用应用的logo
			File imgFile = new File(CacheImgUtil.img_logo);
			if (imgFile.exists()){
				oks.setImagePath(imgFile.getAbsolutePath());
			}
		} else {//商品图片不为空时用商品的图片
			oks.setImageUrl(imageUrl);
		}
		oks.setComment("comment");
		oks.setSite("bange");
		oks.setSilent(silent);
		oks.setTheme(OnekeyShareTheme.CLASSIC);
		oks.setDialogMode();
		oks.disableSSOWhenAuthorize();
		oks.show(ctx);
	}
		
	private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            shareByCheckImgUrl(silent);
        }
    };
    
	/**
	 * 检查图片地址是否有效
	 */
	private void checkImgUrl(){
		AsyncExecuter.getInstance().execute(new Runnable() {
			public void run() {
				int counts = 0;  
				succ = false; 
				if (imageUrl == null || imageUrl.length() <= 0) { 
					// 利用handler更改状态信息
		            handler.sendEmptyMessage(0);
		            return;
				}  
				while (counts < 3) {  
					try {  
						urlStr = new URL(imageUrl);  
						connection = (HttpURLConnection) urlStr.openConnection();
						if(connection == null){
							counts++;
							continue;
						}
						state = connection.getResponseCode();  
						if (state == 200) {  
							succ = true;  
						}  
						break;  
					} catch (Exception ex) {  
						counts++;  
						continue;  
					}
				} 
				// 利用handler更改状态信息
	            handler.sendEmptyMessage(0);
			}
    	});
	}
}
