# KChartView
KChart for Android ；股票k线图

效果预览
-------  
* 支持macd,kdj,rsi,boll多种指标切换，支持横竖屏切换，支持长按，缩放，滑动，fling事件等。
<div class='row'>
        <img src='https://github.com/tifezh/KChartView/blob/master/img/demo.gif' width="300px"/>
</div>

* 支持自定义样式
<div class='row'>
        <img src='https://github.com/tifezh/KChartView/blob/master/img/style1.png' width="300px"/>
        <img src='https://github.com/tifezh/KChartView/blob/master/img/style2.png' width="300px"/>
</div>

#### 配置及使用

##### xml简单配置
```xml
<com.github.tifezh.kchartlib.chart.KChartView
        android:id="@+id/kchart_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
    </com.github.tifezh.kchartlib.chart.KChartView>
```


##### xml中配置示例

```xml
<com.github.tifezh.kchartlib.chart.KChartView
        android:id="@+id/kchart_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:kc_text_size="@dimen/chart_text_size"
        app:kc_text_color="#787878"
        app:kc_line_width="@dimen/chart_line_width"
        app:kc_background_color="#FFF"
        app:kc_selected_line_color="#B1B2B6"
        app:kc_selected_line_width="1dp"
        app:kc_grid_line_color="#d0d0d0"
        app:kc_grid_line_width="0.5dp"
        app:kc_point_width="6dp"
        app:kc_macd_width="4dp"
        app:kc_dif_color="@color/chart_ma5"
        app:kc_dea_color="@color/chart_ma10"
        app:kc_macd_color="@color/chart_ma20"
        app:kc_k_color="@color/chart_ma5"
        app:kc_d_color="@color/chart_ma10"
        app:kc_j_color="@color/chart_ma20"
        app:kc_rsi1_color="@color/chart_ma5"
        app:kc_rsi2_color="@color/chart_ma10"
        app:kc_ris3_color="@color/chart_ma20"
        app:kc_up_color="@color/chart_ma5"
        app:kc_mb_color="@color/chart_ma10"
        app:kc_dn_color="@color/chart_ma20"
        app:kc_ma5_color="@color/chart_ma5"
        app:kc_ma10_color="@color/chart_ma10"
        app:kc_ma20_color="@color/chart_ma20"
        app:kc_candle_line_width="1dp"
        app:kc_candle_width="4dp"
        app:kc_selector_background_color="#c8d0d0d0"
        app:kc_selector_text_size="@dimen/chart_selector_text_size"
        app:kc_tab_text_color="@color/tab_light_text_color_selector"
        app:kc_tab_indicator_color="#4473b1"
        app:kc_tab_background_color="#fff"
        app:kc_candle_solid="false">
    </com.github.tifezh.kchartlib.chart.KChartView>
```

##### 数据实体继承IKLine类 获取各个指标的值

```java
public class KLineEntity implements KLine {

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
```

##### 定义数据适配器

```java
public class KChartAdapter extends BaseKChartAdapter {
    private List<KLineEntity> datas = new ArrayList<>();
    public KChartAdapter() {
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public Date getDate(int position) {
        try {
            String s = datas.get(position).Date;
            String[] split = s.split("/");
            Date date = new Date();
            date.setYear(Integer.parseInt(split[0]) - 1900);
            date.setMonth(Integer.parseInt(split[1]) - 1);
            date.setDate(Integer.parseInt(split[2]));
            return date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 向头部添加数据
     */
    public void addHeaderData(List<KLineEntity> data) {
        if (data != null && !data.isEmpty()) {
            datas.addAll(data);
            notifyDataSetChanged();
        }
    }

    /**
     * 向尾部添加数据
     */
    public void addFooterData(List<KLineEntity> data) {
        if (data != null && !data.isEmpty()) {
            datas.addAll(0, data);
            notifyDataSetChanged();
        }
    }

    /**
     * 改变某个点的值
     * @param position 索引值
     */
    public void changeItem(int position,KLineEntity data)
    {
        datas.set(position,data);
        notifyDataSetChanged();
    }
}
```
##### 加载数据

```java
mKChartView.showLoading();
new Thread(new Runnable() {
    @Override
    public void run() {
        final List<KLineEntity> data = DataRequest.getALL(ExampleActivity.this);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //追加数据
                mAdapter.addFooterData(data);
                //开始动画
                mKChartView.startAnimation();
                //刷新完成
                mKChartView.refreshEnd();
            }
        });
    }
}).start();
```
##### 更多使用方法请下载demo查看

#### 扩展性

##### 添加其他指数 以添加kdj指标为例
* 定义kdj中的值
```java
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
```
* 实现IChartDraw接口
```java
public class KDJDraw implements IChartDraw<IKDJ> {

    private Paint mKPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mJPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public KDJDraw(BaseKChartView view) {
    }

    @Override
    public void drawTranslated(@Nullable IKDJ lastPoint, @NonNull IKDJ curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKChartView view, int position) {
        view.drawChildLine(canvas, mKPaint, lastX, lastPoint.getK(), curX, curPoint.getK());
        view.drawChildLine(canvas, mDPaint, lastX, lastPoint.getD(), curX, curPoint.getD());
        view.drawChildLine(canvas, mJPaint, lastX, lastPoint.getJ(), curX, curPoint.getJ());
    }

    @Override
    public void drawText(@NonNull Canvas canvas, @NonNull BaseKChartView view, int position, float x, float y) {
        String text = "";
        IKDJ point = (IKDJ) view.getItem(position);
        text = "K:" + view.formatValue(point.getK()) + " ";
        canvas.drawText(text, x, y, mKPaint);
        x += mKPaint.measureText(text);
        text = "D:" + view.formatValue(point.getD()) + " ";
        canvas.drawText(text, x, y, mDPaint);
        x += mDPaint.measureText(text);
        text = "J:" + view.formatValue(point.getJ()) + " ";
        canvas.drawText(text, x, y, mJPaint);
    }

    @Override
    public float getMaxValue(IKDJ point) {
        return Math.max(point.getK(), Math.max(point.getD(), point.getJ()));
    }

    @Override
    public float getMinValue(IKDJ point) {
        return Math.min(point.getK(), Math.min(point.getD(), point.getJ()));
    }

    /**
     * 设置K颜色
     */
    public void setKColor(int color) {
        mKPaint.setColor(color);
    }

    /**
     * 设置D颜色
     */
    public void setDColor(int color) {
        mDPaint.setColor(color);
    }

    /**
     * 设置J颜色
     */
    public void setJColor(int color) {
        mJPaint.setColor(color);
    }

    /**
     * 设置曲线宽度
     */
    public void setLineWidth(float width)
    {
        mKPaint.setStrokeWidth(width);
        mDPaint.setStrokeWidth(width);
        mJPaint.setStrokeWidth(width);
    }

    /**
     * 设置文字大小
     */
    public void setTextSize(float textSize)
    {
        mKPaint.setTextSize(textSize);
        mDPaint.setTextSize(textSize);
        mJPaint.setTextSize(textSize);
    }
}
```
* 添加kdj指标
```java
mKChartView.addChildDraw("KDJ", mKDJDraw);
```
##### 自定义画线，画柱体 以macd为例
* 重写 drawTranslated（在此方法中绘画的会滑动和缩放） 方法 ，如下所示：
```java
 @Override
public void drawTranslated(@Nullable IMACD lastPoint, @NonNull IMACD curPoint, float lastX, float curX, @NonNull Canvas canvas, @NonNull BaseKChartView view, int position) {
    drawMACD(canvas, view, curX, curPoint.getMacd());
    view.drawChildLine(canvas, mDIFPaint, lastX, lastPoint.getDea(), curX, curPoint.getDea());
    view.drawChildLine(canvas, mDEAPaint, lastX, lastPoint.getDif(), curX, curPoint.getDif());
    }
/**
 * 画macd
 * @param canvas
 * @param x
 * @param macd
 */
private void drawMACD(Canvas canvas, BaseKChartView view, float x, float macd) {
    float macdy = view.getChildY(macd);
    float r = mMACDWidth / 2;
    float zeroy=view.getChildY(0);
    if (macd > 0) {
        canvas.drawRect(x - r, macdy, x + r, zeroy, mRedPaint);
    } else {
        canvas.drawRect(x - r, zeroy, x + r, macdy, mGreenPaint);
    }
}
```

License
-------

    Copyright 2018 tifezh

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.