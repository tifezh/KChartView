package com.github.tifezh.kchart.chart;

import com.github.tifezh.kchartlib.chart.EntityImpl.KLineImpl;


/**
 * K线实体
 * Created by tifezh on 2016/5/16.
 */

public class KLineEntity implements KLineImpl {

    public String getDatetime() {
        return Date;
    }

    public float getOpenPrice() {
        return Open;
    }

    public float getHighPrice() {
        return High;
    }

    public float getLowPrice() {
        return Low;
    }

    public float getClosePrice() {
        return Close;
    }

    public float getMA5Price() {
        return MA5Price;
    }

    public float getMA10Price() {
        return MA10Price;
    }

    public float getMA20Price() {
        return MA20Price;
    }

    public float getDea() {
        return dea;
    }

    public float getDif() {
        return dif;
    }

    public float getMacd() {
        return macd;
    }

    public float getK() {
        return k;
    }

    public float getD() {
        return d;
    }

    public float getJ() {
        return j;
    }

    public float getRsi1() {
        return rsi1;
    }

    public float getRsi2() {
        return rsi2;
    }

    public float getRsi3() {
        return rsi3;
    }

    public float getUp() {
        return up;
    }

    public float getMb() {
        return mb;
    }

    public float getDn() {
        return dn;
    }

    public String Date;
    public float Open;
    public float High;
    public float Low;
    public float Close;
    public float Volume;

    public float MA5Price;

    public float MA10Price;

    public float MA20Price;

    public float dea;

    public float dif;

    public float macd;

    public float k;

    public float d;

    public float j;

    public float rsi1;

    public float rsi2;

    public float rsi3;

    public float up;

    public float mb;

    public float dn;

}
