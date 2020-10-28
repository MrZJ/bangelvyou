package com.basulvyou.system.wibget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

import com.basulvyou.system.R;
import com.basulvyou.system.util.DensityUtil;

/**
 * Created by Administrator on 2016/2/3.
 */
public class DashedLine extends View {
    private Paint paint = null;
    private Path path = null;
    private PathEffect pe = null;
    private String lineOrientation;

    public DashedLine(Context paramContext) {
        this(paramContext, null);
    }

    public DashedLine(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        //通过R.styleable.dashedline获得我们在attrs.xml中定义的
        //<declare-styleable name="dashedline"> TypedArray
        TypedArray a = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable. dashedline);
        //我们在attrs.xml中<declare-styleable name="dashedline">节点下
        //添加了<attr name="lineColor" format="color" />
        //表示这个属性名为lineColor类型为color。当用户在布局文件中对它有设定值时
        //可通过TypedArray获得它的值当用户无设置值是采用默认值0XFF00000
        int lineColor = a.getColor(R.styleable. dashedline_lineColor, 0XFF000000);
        lineOrientation = a.getString(R.styleable.dashedline_lineOrientation);
        a.recycle();
        this. paint = new Paint();
        this. path = new Path();
        this. paint.setStyle(Paint.Style.STROKE);
        this. paint.setColor(lineColor);
        this. paint.setAntiAlias(true);
        this. paint.setStrokeWidth(DensityUtil.dip2px(getContext(), 4.0f));
        float[] arrayOfFloat = new float[4];
        arrayOfFloat[0] = DensityUtil.dip2px(getContext(),3.0f);
        arrayOfFloat[1] = DensityUtil.dip2px(getContext(),3.0f);
        arrayOfFloat[2] = DensityUtil.dip2px(getContext(),3.0f);
        arrayOfFloat[3] = DensityUtil.dip2px(getContext(),3.0f);
        this. pe = new DashPathEffect(arrayOfFloat, DensityUtil.dip2px(getContext(),2.0f));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this. path.moveTo(0.0F, 0.0F);
        if(null == lineOrientation){
            this. path.lineTo(getMeasuredWidth(), 0.0F);
        }else{
            this. path.lineTo(0.0F, 500F);
        }
        this. paint.setPathEffect(this.pe);
        canvas.drawPath( this. path, this. paint);
    }

}
