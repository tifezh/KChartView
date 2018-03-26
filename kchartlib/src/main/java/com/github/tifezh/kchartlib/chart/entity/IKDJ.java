package com.github.tifezh.kchartlib.chart.entity;

/**
 * KDJ指标(随机指标)接口
 * @see <a href="https://baike.baidu.com/item/KDJ%E6%8C%87%E6%A0%87/6328421?fr=aladdin&fromid=3423560&fromtitle=kdj"/>相关说明</a>
 * Created by tifezh on 2016/6/10.
 */
public interface IKDJ {

    /**
     * K值
     */
    float getK();

    /**
     * D值
     */
    float getD();

    /**
     * J值
     */
    float getJ();

}
