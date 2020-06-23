package com.ajou.capstone_design_freitag.Work;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

public class CustomView extends View{


    private List<String> rect;
    private List<String> label;
    Bitmap myBitmap;

    public void setLabel(List<String> label) {
        this.label = label;
    }
    public void setBitmap(Bitmap bitmap) {
        this.myBitmap = bitmap;
    }
    public void setRect(List<String> rect) {
        this.rect = rect;
    }


    @Override
    protected void onDraw(Canvas canvas){
        if(myBitmap!=null){
            canvas.drawColor(Color.TRANSPARENT);
//            Bitmap resizeImgBitmap = Bitmap.createScaledBitmap(myBitmap, x, y, true);
            canvas.drawBitmap(myBitmap, 0, 0, null);
            for(int i=0;i<rect.size()/4;i++) {
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                Paint text = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(3);
                paint.setColor(Color.GREEN);
                text.setColor(Color.GREEN);
                text.setTextSize(50);
                setFocusable(true);

                canvas.drawRect(Float.parseFloat(rect.get(i*4)),Float.parseFloat(rect.get(i*4+1)),Float.parseFloat(rect.get(i*4+2)),Float.parseFloat(rect.get(i*4+3)), paint);
                canvas.drawText(label.get(i),Float.parseFloat(rect.get(i*4)),Float.parseFloat(rect.get(i*4+1)), text);
            }
        }
    }

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
    }

    public CustomView(Context context,AttributeSet attributeSet,int defStyle){
        super(context,attributeSet,defStyle);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),MeasureSpec.getSize(heightMeasureSpec));//캔버스 사이즈 지정해줘
        //setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    public boolean performClick(){
        return  super.performClick();
    }

}