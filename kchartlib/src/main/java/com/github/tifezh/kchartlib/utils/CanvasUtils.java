package com.github.tifezh.kchartlib.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Pair;


/**
 * 画图工具类
 * Created by tifezh on 2018/4/1.
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public class CanvasUtils {

    public static float getTextX(float x,Paint paint,String text,XAlign align){
        switch (align){
            case CENTER:
                x-=paint.measureText(text)/2;
                break;
            case RIGHT:
                x-=paint.measureText(text);
                break;
        }
        return x;
    }

    public static float getTextY(float y,Paint paint,YAlign align){
        Paint.FontMetrics fm = paint.getFontMetrics();
        float textHeight = fm.descent - fm.ascent;
        float baseLine = (textHeight - fm.bottom - fm.top) / 2;
        switch (align){
            case TOP:
                y+=baseLine;
                break;
            case CENTER:
                y+=textHeight / 2 - fm.descent;
                break;
            case BOTTOM:
                y+=baseLine-textHeight;
                break;
        }
        return  y;
    }


    /**
     * 绘制文本工具
     * @param canvas {@link Canvas}
     * @param paint 画笔
     * @param x x坐标
     * @param y y坐标
     * @param xAlign x轴方向对齐方式
     * @param yAlign y轴方向对齐方式
     */
    public static void drawText(Canvas canvas, Paint paint, float x, float y, String text, XAlign xAlign,YAlign yAlign){
        y=getTextY(y,paint,yAlign);
        x=getTextX(x,paint,text,xAlign);
        canvas.drawText(text,x,y,paint);
    }


    /**
     * 绘制多个文本
     * @param canvas {@link Canvas}
     * @param x x坐标
     * @param y y坐标
     * @param xAlign x轴方向对齐方式
     * @param yAlign y轴方向对齐方式
     * @param strings 画笔和对应文本的集合
     */
    @SafeVarargs
    @SuppressWarnings("ConstantConditions")
    public static void drawTexts(Canvas canvas, float x, float y, XAlign xAlign, YAlign yAlign, Pair<Paint,String>... strings){

        if (strings == null || strings.length == 0) {
            return;
        }

        float textWidth=0;
        if(xAlign!=XAlign.LEFT){
            for (Pair<Paint,String> s:strings){
                textWidth+=s.first.measureText(s.second);
            }

            if(xAlign==XAlign.CENTER) x-=textWidth/2f;
            if(xAlign==XAlign.RIGHT) x-=textWidth;
        }

        for (Pair<Paint,String> s:strings){
            float newY=getTextY(y,s.first,yAlign);
            canvas.drawText(s.second,x,newY,s.first);
            x+=s.first.measureText(s.second);
        }
    }
}
