package com.github.tifezh.kchartlib.chart;

/**
 * 工具类
 * Created by tifezh on 2018/3/31.
 */
class Utils {

    /**
     * 检查实体是否为null
     * @param object 实体
     * @param msg 异常信息
     * @throws NullPointerException 实体为空时抛出异常
     */
     static void checkNull(Object object, String msg){
         if(object==null){
             throw new NullPointerException(msg);
         }
     }
}
