package com.github.tifezh.kchartlib.chart.base;

import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collection;
import java.util.Date;
import java.util.List;


/**
 * 数据适配器
 * @param <T> 实体类泛型
 * Created by tifezh on 2016/6/14.
 */

public interface IAdapter<T> {
    /**
     * 获取点的数目
     *
     */
    int getCount();

    /**
     * 通过序号获取item
     *
     * @param position 对应的序号
     * @return 数据实体
     */
    T getItem(int position);

    /**
     * 通过序号获取时间
     *t
     * @param position 索引
     */
    Date getDate(int position);

    /**
     * 注册一个数据观察者
     *
     * @param observer 数据观察者
     */
    void registerDataSetObserver(DataSetObserver observer);

    /**
     * 移除一个数据观察者
     *
     * @param observer 数据观察者
     */
    void unregisterDataSetObserver(DataSetObserver observer);

    /**
     * 当数据发生变化时调用
     */
    void notifyDataSetChanged();

    /**
     * 获取数据集合的实体
     */
    List<? extends T> getData();


    /**
     * 清空历史数据并添加新的数据集合
     * @param newData 新数据集合
     */
    void setNewData(@Nullable Collection<? extends T> newData);

    /**
     * 在尾部追加数据
     * @param item 实体类
     */
    void addData(@NonNull T item);

    /**
     * 在尾部追加数据
     */
    void addData(Collection<? extends T> newData);

    /**
     * 根据索引改变实体值
     * @param index 索引
     * @param item 实体
     */
    void changeItem(int index,@NonNull T item);
}
