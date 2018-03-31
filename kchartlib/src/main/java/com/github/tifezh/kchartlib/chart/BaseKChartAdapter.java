package com.github.tifezh.kchartlib.chart;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.github.tifezh.kchartlib.chart.base.IAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * k线图的数据适配器
 * Created by tifezh on 2016/6/9.
 */

public abstract class BaseKChartAdapter<T> implements IAdapter<T> {

    private final List<T> data=new ArrayList<>();

    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    @Override
    public List<T> getData() {
        return data;
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public void changeItem(int index, @NonNull T item) {
        Utils.checkNull(item,"item == null");
        data.set(index,item);
        notifyDataSetChanged();
    }

    @Override
    public void setNewData(@Nullable Collection<? extends T> newData) {
        data.clear();
        if(newData!=null){
            data.addAll(newData);
        }
        notifyDataSetChanged();
    }

    @Override
    public void addData(@NonNull T item) {
        Utils.checkNull(item,"item == null");
        data.add(0,item);
        notifyDataSetChanged();
    }

    @Override
    public void addData(Collection<? extends T> newData) {
        if(newData!=null){
            data.addAll(0,newData);
            notifyDataSetChanged();
        }
    }

    public void notifyDataSetChanged() {
        if (getCount() > 0) {
            mDataSetObservable.notifyChanged();
        } else {
            mDataSetObservable.notifyInvalidated();
        }
    }


    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }
}
