package com.ajou.capstone_design_freitag.Work;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CustomView extends View{

    private float mStartX;
    private float mStartY;
    private float mEndX;
    private float mEndY;
    private boolean drawing = false;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint text = new Paint(Paint.ANTI_ALIAS_FLAG);

    Bitmap myBitmap;
    String viewLabel;
    Boolean labelSet = false;

    public void setLabel(String label) {
        viewLabel = null;
        this.viewLabel = label;
        System.out.println(viewLabel);
    }

    public void setBitmap(Bitmap bitmap) {
        this.myBitmap = bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas){
        if(myBitmap!=null){
            canvas.drawColor(Color.TRANSPARENT);
            Bitmap resizeImgBitmap = Bitmap.createScaledBitmap(myBitmap, 800, 600, true);
            canvas.drawBitmap(resizeImgBitmap,0,0,null);
            if(drawing){
                if(viewLabel != null) {
                    canvas.drawRect(mStartX, mStartY, mEndX, mEndY,paint);
                    canvas.drawText(viewLabel, mStartX, mStartY, text);
                }
            }
        }
    }

    public CustomView(Context context) {
        super(context);
        init();
    }

    public CustomView(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        init();
    }

    public CustomView(Context context,AttributeSet attributeSet,int defStyle){
        super(context,attributeSet,defStyle);
        init();
    }

    private void init() {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        paint.setColor(Color.GREEN);
        text.setColor(Color.GREEN);
        text.setTextSize(50);
        setFocusable(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        setMeasuredDimension(800, 600);//캔버스 사이즈 지정해줘
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int action = event.getAction();
        if(action == MotionEvent.ACTION_MOVE){//누르고 움직였을 때
            mEndX = event.getX();
            mEndY = event.getY();
            invalidate();
        }
        else if(action == MotionEvent.ACTION_DOWN){//처음 눌렸을 때
            mStartX = (int) event.getX();
            mStartY = (int) event.getY();
            drawing = true;
            invalidate();
        }
        else if(action == MotionEvent.ACTION_UP){//눌렀다가 뗐을 때
            drawing = false;
            mEndX = (int) event.getX();
            mEndY = (int) event.getY();
        }
        return true;
    }

    @Override
    public boolean performClick(){
        return  super.performClick();
    }

}