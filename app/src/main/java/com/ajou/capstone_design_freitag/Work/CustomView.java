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
    boolean isVertical;
    int width;
    int height;

    public void setLabel(List<String> label) {
        this.label = label;
    }
    public void setBitmap(Bitmap bitmap) {
        this.myBitmap = bitmap;
        if((float)myBitmap.getWidth() / myBitmap.getHeight() < (float)4 / 3) {
            isVertical = true;
            width = myBitmap.getWidth() * 600 / myBitmap.getHeight();
            height = 600;
        } else {
            isVertical = false;
            width = 800;
            height = myBitmap.getHeight() * 800 / myBitmap.getWidth();
        }
    }
    public void setRect(List<String> rect) {
        this.rect = rect;
    }


    @Override
    protected void onDraw(Canvas canvas){
        if(myBitmap!=null){
            canvas.drawColor(Color.TRANSPARENT);

            Bitmap resizeImgBitmap = Bitmap.createScaledBitmap(myBitmap, width, height, true);
            if(isVertical){
                canvas.drawBitmap(resizeImgBitmap, 400 - width / 2, 0, null);
            } else {
                canvas.drawBitmap(resizeImgBitmap, 0, 300 - height / 2, null);
            }
            System.out.println(width);
            System.out.println(height);
            for(int i=0;i<rect.size()/4;i++) {
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                Paint text = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(3);
                paint.setColor(Color.GREEN);
                text.setColor(Color.GREEN);
                text.setTextSize(50);
                setFocusable(true);

                float left = Float.parseFloat(rect.get(i * 4)) * 800;
                float top = Float.parseFloat(rect.get(i * 4 + 1)) * 600;
                float right = Float.parseFloat(rect.get(i * 4 + 2)) * 800;
                float bottom = Float.parseFloat(rect.get(i * 4 + 3)) * 600;
                canvas.drawRect(left, top, right, bottom, paint);
                canvas.drawText(label.get(i), left, top, text);
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
        setMeasuredDimension(800, 600);//캔버스 사이즈 지정해줘
        //setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    public boolean performClick(){
        return  super.performClick();
    }

}