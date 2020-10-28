package com.handmark.pulltorefresh.library.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView.ScaleType;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Orientation;
import com.handmark.pulltorefresh.library.R;

/**
 * 自定义下拉刷新动画
 * @author KevinLi
 *
 */
@SuppressLint("ViewConstructor")
public class CustomAnimLoadingLayout extends LoadingLayout {  
  
    //private AnimationDrawable animationDrawable;  
    private final Matrix mHeaderImageMatrix;
    private float mPivotX, mPivotY;
  
    public CustomAnimLoadingLayout(Context context, Mode mode,  
            Orientation scrollDirection, TypedArray attrs) {  
        super(context, mode, scrollDirection, attrs);  
        // 初始化  
        //mHeaderImage.setImageResource(R.anim.custom_anim);  
        //animationDrawable = (AnimationDrawable) mHeaderImage.getDrawable(); 
        
        mHeaderImage.setScaleType(ScaleType.MATRIX);
		mHeaderImageMatrix = new Matrix();
		mHeaderImage.setImageMatrix(mHeaderImageMatrix);
    }  
    
    // 默认图片  
    @Override  
    protected int getDefaultDrawableResId() {  
        return R.drawable.app_loading0;  
    }  
      
    @Override  
    protected void onLoadingDrawableSet(Drawable imageDrawable) {  
        // NO-OP  
    	if (null != imageDrawable) {
			mPivotX = Math.round(imageDrawable.getIntrinsicWidth() / 2);
			mPivotY = Math.round(imageDrawable.getIntrinsicHeight() / 2);
		}
    }  
      
    @Override  
    protected void onPullImpl(float scaleOfLayout) {  
        // NO-OP
    	if(scaleOfLayout > 1){
    		scaleOfLayout = 1;
    	}
    	mHeaderImageMatrix.setScale(scaleOfLayout, scaleOfLayout, mPivotX, mPivotY);
    	mHeaderImage.setImageMatrix(mHeaderImageMatrix);
    } 
    
    // 下拉以刷新  
    @Override  
    protected void pullToRefreshImpl() {  
        // NO-OP  
    }  
    
    // 正在刷新时回调  
    @Override  
    protected void refreshingImpl() {  
        // 播放帧动画  
        //animationDrawable.start(); 
//    	mHeaderImage.setImageResource(R.anim.custom_anim);
    }  
    
    // 释放以刷新  
    @Override  
    protected void releaseToRefreshImpl() {
        // NO-OP  
    }  
    
    // 重新设置  
    @Override  
    protected void resetImpl() { 
    	mHeaderImage.setImageResource(R.drawable.app_loading0);
        mHeaderImage.setVisibility(View.VISIBLE); 
        if (null != mHeaderImageMatrix) {
			mHeaderImageMatrix.reset();
			mHeaderImage.setImageMatrix(mHeaderImageMatrix);
		}
    }  
  
} 