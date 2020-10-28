package com.shishoureport.system.listener;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;


public class MyWebChromeClient extends WebChromeClient{

	/*private final static int FILECHOOSER_RESULTCODE = 5;
	private ValueCallback<Uri> mUploadMessage;  
	private BaseFragment fragment;
	
	public MyWebChromeClient(BaseFragment fragment) {
		this.fragment=fragment;
	}

	public void openFileChooser(ValueCallback<Uri> uploadMsg,String acceptType, String capture) {
		 mUploadMessage = uploadMsg;
      Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
      intent.setType("image*//*");
      fragment.startActivityForResult(
      Intent.createChooser(intent, "选择图片"),
      MyWebChromeClient.FILECHOOSER_RESULTCODE);
  }

  // 3.0 + 调用这个方法
	public void openFileChooser(ValueCallback<Uri> uploadMsg,String acceptType) {
      mUploadMessage = uploadMsg;
      Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
      intent.setType("image*//*");
      fragment.startActivityForResult(
      Intent.createChooser(intent, "选择图片"),
      MyWebChromeClient.FILECHOOSER_RESULTCODE);
  }

  // Android < 3.0 调用这个方法
	public void openFileChooser(ValueCallback<Uri> uploadMsg) {
      mUploadMessage = uploadMsg;
      Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//	     intent.addCategory(Intent.CATEGORY_OPENABLE);
      intent.setType("image*//*");
      fragment.startActivityForResult(
      Intent.createChooser(intent, "选择图片"),
      MyWebChromeClient.FILECHOOSER_RESULTCODE);
  }
	
    @Override  
    public void onReceivedTitle(WebView view, String title) {  
        super.onReceivedTitle(view, title);
    }  */
    private ProgressBar webPro;

    public MyWebChromeClient(ProgressBar progressBar){
        this.webPro = progressBar;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (newProgress == 100) {
            webPro.setVisibility(View.GONE);
        } else {
            if (View.GONE == webPro.getVisibility()) {
                webPro.setVisibility(View.VISIBLE);
            }
            webPro.setProgress(newProgress);
        }
        super.onProgressChanged(view, newProgress);
    }
}