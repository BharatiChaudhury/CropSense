package com.example.myapplication.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myapplication.CircleActivity;


public class CustomView extends View {
    private Paint centerPaint;
    private Paint radPaint;
    private Paint radPaint2;
    private static float centerX;
    private static float centerY;
    private static float radRadius;
    public CustomView(Context context){
        super(context);
        init(null);
    }

    public CustomView(Context context, AttributeSet attrs){
        super(context,attrs);
        init(attrs);
    }

    public CustomView(Context context, AttributeSet attrs,int defStyleAttr){
        super(context,attrs,defStyleAttr);
        init(attrs);
    }

    public CustomView(Context context, AttributeSet attrs,int defStyleAttr,int defStyleRes){
        super(context,attrs,defStyleAttr,defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet set){
        if(set==null) return;

        centerPaint = new Paint();
        centerPaint.setAntiAlias(true);
        centerPaint.setColor(Color.WHITE);

        radPaint = new Paint();
        radPaint.setAntiAlias(true);
        radPaint.setStyle(Paint.Style.STROKE);
        radPaint.setColor(Color.WHITE);

        radPaint2 = new Paint();
        radPaint2.setAntiAlias(true);
        radPaint2.setStyle(Paint.Style.STROKE);
        radPaint2.setColor(Color.WHITE);
        radPaint2.setPathEffect(new DashPathEffect(new float[]{10,20},0));
    }
    @Override
    protected void onDraw(Canvas canvas){
        if(centerX==0f || centerY==0f) {
            centerX = getWidth() / 2;
            centerY = getHeight() / 2;
        }
        if(radRadius==0f) radRadius=100f;
        canvas.drawCircle(centerX,centerY,10f,centerPaint);
        canvas.drawCircle(centerX,centerY,radRadius,radPaint);
        canvas.drawCircle(centerX,centerY,radRadius*0.25f,radPaint2);
        canvas.drawCircle(centerX,centerY,radRadius*0.5f,radPaint2);
        canvas.drawCircle(centerX,centerY,radRadius*0.75f,radPaint2);

    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        boolean value = super.onTouchEvent(event);
        int clickOpt= CircleActivity.getClickOpt();
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                return true;
            }
            case MotionEvent.ACTION_MOVE:{
                if(clickOpt==0){
                    centerX=event.getX();
                    centerY=event.getY();}
                else{
                    float x = event.getX();
                    float y = event.getY();
                    float rad=(float)Math.sqrt(Math.pow(x-centerX,2)+Math.pow(y-centerY,2));
                    if(rad>20f) radRadius=rad;
                }
                postInvalidate();
                return true;
            }
        }
        return value;
    }
    public static float getRadRadius(){
        return radRadius;
    }
}
