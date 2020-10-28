package com.basulvyou.system.listener;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.basulvyou.system.util.AsyncExecuter;
import com.basulvyou.system.util.CacheImgUtil;
import com.basulvyou.system.util.ConfigUtil;
import com.basulvyou.system.util.StringUtil;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

/**
 * 分享点击监听事件
 */
public class ShareClickListener implements OnClickListener {

    private Context ctx;
    private ImageView img_share;
    private Animation mRotateAnimation;
    private View view;
    private String customText = "#荥经旅游#", imageUrl, downLoadUrl;
    private String goodsID = "";
    private String cultureUrl = "";
    private boolean silent;
    private URL urlStr;
    private HttpURLConnection connection;
    private int state = -1;
    private boolean succ;

    public ShareClickListener(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void onClick(View view) {
        this.view = view;
        showShare(true);
    }

    public void setShareImg(ImageView img_share) {
        this.img_share = img_share;
    }

    /**
     * 设置分享图片
     *
     * @param imageUrl
     */
    public void setShareImgUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * 设置分享正文
     *
     * @param content
     */
    public void setContent(String content) {
        if (!StringUtil.isEmpty(content)) {
            customText = content + "#荥经旅游#";
        }
    }

    /**
     * 设置分享正文
     */
    public void setGoodsID(String goodsId) {
        if (!StringUtil.isEmpty(goodsId)) {
            goodsID = goodsId;
        }
    }

    /**
     * 设置分享正文
     */
    public void setcultureUrl(String url) {
        if (!StringUtil.isEmpty(url)) {
            cultureUrl = url;
        }
    }

    /**
     * 设置分享链接下载地址
     */
    public void setDownloadUrl(String loadUrl) {
        if (!StringUtil.isEmpty(loadUrl)) {
            downLoadUrl = loadUrl;
        }
    }

    // 使用快捷分享完成分享（请务必仔细阅读位于SDK解压目录下Docs文件夹中OnekeyShare类的JavaDoc）

    /**
     * ShareSDK集成方法有两种</br>
     * 1、第一种是引用方式，例如引用onekeyshare项目，onekeyshare项目再引用mainlibs库</br>
     * 2、第二种是把onekeyshare和mainlibs集成到项目中，本例子就是用第二种方式</br>
     * 请看“ShareSDK 使用说明文档”，SDK下载目录中 </br>
     * 或者看网络集成文档 http://wiki.mob.com/Android_%E5%BF%AB%E9%80%9F%E9%9B%86%E6%88%90%E6%8C%87%E5%8D%97
     * 3、混淆时，把sample或者本例子的混淆代码copy过去，在proguard-project.txt文件中
     * <p>
     * <p>
     * 平台配置信息有三种方式：
     * 1、在我们后台配置各个微博平台的key
     * 2、在代码中配置各个微博平台的key，http://mob.com/androidDoc/cn/sharesdk/framework/ShareSDK.html
     * 3、在配置文件中配置，本例子里面的assets/ShareSDK.conf,
     * <p>
     * silent:表示直接分享,没有界面.
     */
    private void showShare(boolean silent) {
        this.silent = silent;
        checkImgUrl();
    }

    private void shareByCheckImgUrl(boolean silent) {
        final OnekeyShare oks = new OnekeyShare();
        oks.setTitle(customText);
        oks.setTitleUrl("http://60.174.234.249:8103");
        oks.setUrl("http://60.174.234.249:8103");
        oks.setSiteUrl("http://60.174.234.249:8103");
        if (!StringUtil.isEmpty(downLoadUrl)) {
            oks.setTitleUrl(downLoadUrl);
            oks.setUrl(downLoadUrl);
            oks.setSiteUrl(downLoadUrl);
        } else {
            if (!StringUtil.isEmpty(goodsID)) {
                String goodsUrl = ConfigUtil.HTTP + "/service/AppEntpInfo.do?id=" + goodsID;
                oks.setTitleUrl(goodsUrl);
                oks.setUrl(goodsUrl);
                oks.setSiteUrl(goodsUrl);
            }
            if (!StringUtil.isEmpty(cultureUrl)) {
                oks.setTitleUrl(cultureUrl);
                oks.setUrl(cultureUrl);
                oks.setSiteUrl(cultureUrl);
            }
        }
        oks.setText("荥经旅游");
        if (StringUtil.isEmpty(imageUrl) || !succ) {//商品图片为空时 用应用的logo
            File imgFile = new File(CacheImgUtil.img_logo);
            if (imgFile.exists()) {
                oks.setImagePath(imgFile.getAbsolutePath());
            }
        } else {//商品图片不为空时用商品的图片
            oks.setImageUrl(imageUrl);
        }
        oks.setComment("comment");
        oks.setSite("荥经");
        oks.setSilent(silent);
        oks.setTheme(OnekeyShareTheme.CLASSIC);
        oks.setDialogMode();
        oks.disableSSOWhenAuthorize();
        oks.show(ctx);
    }

    /**
     * 问吧分享
     *
     * @param title
     * @param url
     */
    public void showWenBaShare(String title, String url) {
        OnekeyShare oks = new OnekeyShare();
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(url);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        File imgFile = new File(CacheImgUtil.img_logo);
        if (imgFile.exists()) {
            oks.setImagePath(imgFile.getAbsolutePath());
        }
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
         /*oks.setComment("我是测试评论文本");*/
        // site是分享此内容的网站名称，仅在QQ空间使用
		/* oks.setSite(getString(R.string.app_name));*/
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
		/*oks.setImagePath(ConfigUtil.IMAGE_ICON);*/
		/*oks.setImagePath(imagePath);*/
        // 启动分享GUI
        //关闭sso授权
        oks.setSilent(true);
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
    private void checkImgUrl() {
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
                        if (connection == null) {
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
