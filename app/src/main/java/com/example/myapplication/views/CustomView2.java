package com.example.myapplication.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.example.myapplication.SelectParametersActivity;

public class CustomView2 extends FrameLayout {
    private Paint radPaint;
    private static float mCx,mCy,radius;
    //private int mColor = Color.parseColor("#D20E0F02");
    public CustomView2(Context context){
        super(context);
        init(null);
    }

    public CustomView2(Context context, AttributeSet attrs){
        super(context,attrs);
        init(attrs);
    }

    public CustomView2(Context context, AttributeSet attrs,int defStyleAttr){
        super(context,attrs,defStyleAttr);
        init(attrs);
    }

    public CustomView2(Context context, AttributeSet attrs,int defStyleAttr,int defStyleRes){
        super(context,attrs,defStyleAttr,defStyleRes);
        init(attrs);
    }
    private void init(@Nullable AttributeSet set){
        if(set==null) return;
        radPaint = new Paint();
        radPaint.setAntiAlias(true);
        radPaint.setStyle(Paint.Style.STROKE);
        radPaint.setColor(Color.WHITE);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        //canvas.drawColor(mColor);
        mCx = getWidth()/2.0f;
        mCy = getHeight()/2.0f;
        radius = SelectParametersActivity.getImgRad();
        canvas.drawCircle(mCx,mCy,radius,radPaint);
    }

    public static float getmCx() {
        return mCx;
    }

    public static float getmCy() {
        return mCy;
    }

    public static float getRadius() {
        return radius;
    }
}
