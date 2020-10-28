package com.handmark.pulltorefresh.library.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 自定义图片控件
 * @author KevinLi
 *
 */
public class CustomImageView extends ImageView{

    private int measuredWidth;
    private int measuredHeight;
    private Bitmap bitmap;
    private float mCurrentProgress;

    public CustomImageView(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomImageView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
    	//bitmap = get
    }

    /**
     * 重写onMeasure方法主要是设置wrap_content时 View的大小
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 在onLayout里面获得测量后View的宽高
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        measuredWidth = getMeasuredWidth();
        measuredHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //这个方法是对画布进行缩放，从而达到图片的缩放，第一个参数为宽度缩放比例，第二个参数为高度缩放比例，
        canvas.scale(mCurrentProgress, mCurrentProgress, measuredWidth / 2, measuredHeight / 2);
        //将等比例缩放后的图片画在画布上面
        canvas.drawBitmap(bitmap, 0, measuredHeight / 4, null);
    }

    /**
     * 设置缩放比例，从0到1  0为最小 1为最大
     * @param currentProgress
     */
    public void setCurrentProgress(float currentProgress){
        mCurrentProgress = currentProgress;
    }
}
