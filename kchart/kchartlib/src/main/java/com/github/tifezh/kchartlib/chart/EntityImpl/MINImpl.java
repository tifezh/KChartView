package com.github.tifezh.kchartlib.chart.EntityImpl;

/**
 * 分钟线实体
 * Created by silladus on 2017/3/7.
 */

public interface MINImpl {

    /**
     * 上次收盘价
     *
     * @return
     */
    public float getLastPrice();

    /**
     * 昨日收盘价
     *
     * @return
     */
    public float getLastClosePrice();

    /**
     * 开盘价
     *
     * @return
     */
    public float getOpenPrice();

    /**
     * 最高价
     *
     * @return
     */
    public float getHighPrice();

    /**
     * 最低价
     *
     * @return
     */
    public float getLowPrice();

    /**
     * 收盘价
     *
     * @return
     */
    public float getClosePrice();

    /**
     * 成交量
     *
     * @return
     */
    public float getVolume();

    /**
     * 成交均价
     *
     * @return
     */
    public float getAVPrice();

    /**
     * 成交时间
     *
     * @return
     */
    public String getTime();
}
