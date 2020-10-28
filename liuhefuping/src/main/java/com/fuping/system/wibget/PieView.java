package com.fuping.system.wibget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.fuping.system.R;
import com.fuping.system.entity.Pie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianzhang.
 * on 2017/10/18.
 * copyright easybiz.
 */

public class PieView extends View {

    private Paint mPaint;
    //饼图和矩形的距离
    private final int PIE_RECT_PADDING = getResources().getDimensionPixelSize(R.dimen.pie_rect_padding);
    //矩形的宽度
    private final int RECT_WIDTH = getResources().getDimensionPixelSize(R.dimen.rect_width);
    //矩形和文字的距离
    private final int RECT_TEXT_PADDING = getResources().getDimensionPixelSize(R.dimen.rect_text_padding);
    //文字的大小
    private final int TEXT_SIZE = getResources().getDimensionPixelSize(R.dimen.text_size);
    //文字的垂直距离
    private final int TEXT_VERTICAL_PADDING = getResources().getDimensionPixelSize(R.dimen.text_vertical_padding);
    //得到文字颜色
    private final int TEXT_COLOR = getResources().getColor(R.color.gray_dark);
    //文字和控件顶部的距离
    private float mPadding;
    //饼图的半径
    private int mPieRadios;
    //所有数值的总和
    private float mMaxValue;
    //饼图开始的角度
    private float mStartAngle;
    //文字的宽度
    private int mTextWidth;
    //控件半高
    private int mControlHalfHeight;
    //当前索引
    private int mCurrentIndex;
    //左边距
    private int mRectMarginLeft;
    private int mTextMarginLeft;
    //当前颜色
    private int mCurrentColor;
    //圆的范围
    private RectF oval;
    //最长的字符串
    private String mMaxString;

    private List<Integer> mPieColorList;
    private List<Float> mPieValue;
    private List<String> mStringList;
    private ArrayList<Pie> mPieArrayList;

    private RectF rect;

    public PieView(Context context) {
        this(context, null);
    }

    public PieView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPieColorList = new ArrayList<>();
        mPieValue = new ArrayList<>();
        mStringList = new ArrayList<>();
        mPaint = new Paint();
        mMaxString = "";
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(10);//画笔宽度
        mPaint.setAntiAlias(true);//抗锯齿
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        mTextWidth = (int) mPaint.measureText(mMaxString);
        mControlHalfHeight = height / 2;
        //饼图半径
        mPieRadios = mControlHalfHeight - 5;
        //控件内容宽度
        int contentWidth = mPieRadios * 2 + PIE_RECT_PADDING + RECT_WIDTH + RECT_TEXT_PADDING + mTextWidth;
        //内容的左边距
        int contentMarginLeft = (width - contentWidth) / 2;
        //矩形的左边距
        mRectMarginLeft = contentMarginLeft + mPieRadios * 2 + PIE_RECT_PADDING;
        //文字的左边距
        mTextMarginLeft = mRectMarginLeft + RECT_WIDTH + RECT_TEXT_PADDING;
        //文字和控件顶部的距离
        mPadding = height / 3 * 0.8f;
        oval = new RectF(contentMarginLeft, mControlHalfHeight - mPieRadios,
                contentMarginLeft + mPieRadios * 2, mControlHalfHeight + mPieRadios);
    }

    /**
     * @param pieArrayList
     */
    public void SetPie(ArrayList<Pie> pieArrayList) {
        mPieArrayList = pieArrayList;
        for (Pie mPie : mPieArrayList) {
            mPieColorList.add(mPie.PieColor);
            mPieValue.add(mPie.PieValue);
            mStringList.add(mPie.PieString);
            if (mMaxString.length() > mPie.PieString.length())
                mMaxString = mPie.PieString;
        }

        //使用postInvalidate可以直接在主线程中更新界面
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mStartAngle = -90;
        mCurrentIndex = 0;
        mMaxValue = 100;
        mAngle = 0;
        mPaint.setStyle(Paint.Style.FILL);

        for (int i = 0; i < mPieValue.size(); i++) {
            mCurrentColor = mPieColorList.get(mCurrentIndex);
            Log.i("mCurrentColor", "onDraw: " + mCurrentColor);
            drawPie(canvas, mPieValue.get(mCurrentIndex));
            drawRect(canvas);
            drawText(canvas, mStringList.get(mCurrentIndex));
            mCurrentIndex++;
        }
    }

    /**
     * 绘制饼图
     *
     * @param canvas
     * @param amount
     */
    private float mAngle = 0;

    private void drawPie(Canvas canvas, double amount) {
        mPaint.setColor(mCurrentColor);
        mPaint.setStyle(Paint.Style.FILL);
        float angle = (float) (360f * amount / mMaxValue);
        if (mPieArrayList.size() - 1 == mCurrentIndex) {
            angle = 360 - mAngle;
        }
        mAngle += angle;
        Log.d("angle", "drawPie: " + angle + ",mAngle =" + mAngle);
        canvas.drawArc(oval, mStartAngle, angle, true, mPaint);
        mStartAngle += angle;
    }

    /**
     * 绘制矩形
     *
     * @param canvas
     */
    private void drawRect(Canvas canvas) {
        if (mCurrentIndex == 0) {
            rect = new RectF(mRectMarginLeft, mPadding, mRectMarginLeft + RECT_WIDTH, mPadding + RECT_WIDTH);
            canvas.drawRect(rect, mPaint);
        } else {
            rect = new RectF(mRectMarginLeft, (mCurrentIndex) * TEXT_VERTICAL_PADDING + mPadding, mRectMarginLeft + RECT_WIDTH, (mCurrentIndex) * TEXT_VERTICAL_PADDING + mPadding + RECT_WIDTH);
            canvas.drawRect(rect, mPaint);
        }
    }

    /**
     * 绘画文字
     *
     * @param canvas
     * @param text
     */
    private void drawText(Canvas canvas, String text) {
        mPaint.setTextSize(18);
        mPaint.setColor(TEXT_COLOR);
        if (mCurrentIndex == 0) {
            canvas.drawText(text, mTextMarginLeft, mPadding + TEXT_SIZE * 0.8f - 5, mPaint);
        } else {
            canvas.drawText(text, mTextMarginLeft, (mCurrentIndex) * TEXT_VERTICAL_PADDING + mPadding + TEXT_SIZE * 0.8f - 5, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            Log.e("", x + "-----------");
        }
        return true;
    }
}