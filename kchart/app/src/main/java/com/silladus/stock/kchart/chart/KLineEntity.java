package com.silladus.stock.kchart.chart;

import com.github.tifezh.kchartlib.chart.EntityImpl.KLineImpl;


/**
 * K线实体
 * Created by silladus on 2017/3/6.
 */

public class KLineEntity implements KLineImpl {
    public float lastPrice;
    public float lastClosePrice;
    public float avPrice;

    public String getDatetime() {
        return Date;
    }

    @Override
    public float getLastPrice() {
        return lastPrice;
    }

    @Override
    public float getLastClosePrice() {
        return lastClosePrice;
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

    @Override
    public float getVolume() {
        return Volume;
    }

    @Override
    public float getAVPrice() {
        return avPrice;
    }

    @Override
    public String getTime() {
        return Date;
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

    public float vMA5;

    public float vMA10;


    @Override
    public float getVOL() {
        return Volume;
    }

    @Override
    public float getVMA5() {
        return vMA5;
    }

    @Override
    public float getVMA10() {
        return vMA10;
    }

    public boolean isMinDraw = false;

    @Override
    public float getCloseDif() {
        if (isMinDraw) {
            return Close - lastPrice;
        } else {
            return Close - Open;
        }
    }

    @Override
    public float getRate() {
        return Close - lastClosePrice;
    }
}
