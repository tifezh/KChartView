package com.github.tifezh.kchartlib.chart;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.tifezh.kchartlib.R;

import java.util.ArrayList;
import java.util.List;

import static com.github.tifezh.kchartlib.utils.ViewUtil.dp2Px;

/**
 * 五档、明细图
 * Created by silladus on 2017/3/3.
 */
public class OrderView extends RelativeLayout {
    private int mTopPadding = 0;
    private boolean isGone;
    private static final int SHOW_ORDER = 0;
    private static final int HIDE_ORDER = 1;
    private String[] btnText = new String[]{};
    private int[] btnWidth = null;
    private ListView mStockListView;
    private int mMoveViewWidth = 50;
    private int mItemViewHeight = 30;
    private int layoutWidth;
    private int layoutHight;
    private LinearLayout linearLayout;
    private int layoutCount = 0;

    public OrderView(Context context) {
        this(context, null);
        initView();
    }

    public OrderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initView();
    }

    public OrderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isGone) {
            //禁止父容器拦截当前事件
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        layoutWidth = getWidth();
        layoutHight = getHeight();
//        if (layoutCount == 0) {
//            initView();
//        }
//        layoutCount++;
    }

    private void initView() {
        if (sells == null || buys == null) {
            sells = new ArrayList<>();
            buys = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                String[] o = new String[2];
                o[0] = "0.00";
                o[1] = "0";
                sells.add(o);
                buys.add(o);
            }
        }
        linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(0, dp2Px(getContext(), mTopPadding), 0, 0);
        linearLayout.addView(buildHeadLayout());
        initOrderView();
        if (btnText.length > 1) {
            linearLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isGone) {
                        vs(SHOW_ORDER);
                    } else {
                        vs(HIDE_ORDER);
                    }
                }
            });
            buildOrderDealListView().setVisibility(View.GONE);
        }
        this.addView(linearLayout, new LayoutParams(LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void initOrderView() {
        for (int i = 0; i < sells.size(); i++) {
            String x = "卖" + (sells.size() - i);
            linearLayout.addView(buildOrderLayout(x, sells.get(sells.size() - 1 - i)));
        }
        View line = new View(getContext());
        line.setBackgroundColor(Color.GRAY);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, dp2Px(getContext(), 1));
//        lp.setMargins(0, dp2Px(getContext(), 5), 0, dp2Px(getContext(), 5));
        line.setLayoutParams(lp);
        linearLayout.addView(line);
        for (int i = 0; i < buys.size(); i++) {
            String x = "买" + (i + 1);
            linearLayout.addView(buildOrderLayout(x, buys.get(i)));
        }
    }

    private View buildOrderLayout(String x, String[] order) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1.0f);
        LinearLayout headLayout = new LinearLayout(getContext());
        headLayout.setLayoutParams(lp);
        headLayout.setGravity(Gravity.CENTER);
        addListHeaderTextView(x, 70, headLayout);
        for (int i = 0; i < order.length; i++) {
            String ord = order[i].equals("0.00") ? "－" : order[i];
            TextView tv = addListHeaderTextView(ord, (layoutWidth - 70) / 2, headLayout);
            if (i == order.length - 1) {
                tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
                if (order[i].length() > 5) {
                    tv.setTextSize(10);
                }
            } else {
                if (!order[0].equals("0.00")) {
                    if (Float.valueOf(order[0]) > close) {
                        tv.setTextColor(getResources().getColor(R.color.price_up));
                    } else if (Float.valueOf(order[0]) == close) {
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(getResources().getColor(R.color.price_down));
                    }
                }
            }
        }
        return headLayout;
    }

    private View buildHeadLayout() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2Px(getContext(), mItemViewHeight));
        LinearLayout headLayout = new LinearLayout(getContext());
        headLayout.setLayoutParams(lp);
        headLayout.setGravity(Gravity.CENTER);
        headLayout.setPadding(4, 4, 4, 4);
        headLayout.setBackgroundColor(getResources().getColor(R.color.order_tab_select));
        for (int i = 0; i < btnText.length; i++) {
            TextView textView = addListHeaderTextView(btnText[i], (layoutWidth - 8) / 2, headLayout);
            textView.setTag("xbTab" + i);
            if (i == 0) {
                textView.setTextColor(getResources().getColor(R.color.white));
                textView.setBackgroundColor(getResources().getColor(R.color.order_tab_select));
            } else {
                textView.setTextColor(getResources().getColor(R.color.order_tab_select));
                textView.setBackgroundColor(getResources().getColor(R.color.white));
            }
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch ((String) v.getTag()) {
                        case "xbTab0":
                            vs(SHOW_ORDER);
                            break;
                        case "xbTab1":
                            vs(HIDE_ORDER);
                            break;
                    }
                }
            });
        }
        return headLayout;
    }

    private void showTab(int index) {
        TextView xbTab0 = (TextView) findViewWithTag("xbTab0");
        TextView xbTab1 = (TextView) findViewWithTag("xbTab1");
        switch (index) {
            case SHOW_ORDER:
                xbTab0.setTextColor(getResources().getColor(R.color.white));
                xbTab0.setBackgroundColor(getResources().getColor(R.color.order_tab_select));
                xbTab1.setTextColor(getResources().getColor(R.color.order_tab_select));
                xbTab1.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case HIDE_ORDER:
                xbTab1.setTextColor(getResources().getColor(R.color.white));
                xbTab1.setBackgroundColor(getResources().getColor(R.color.order_tab_select));
                xbTab0.setTextColor(getResources().getColor(R.color.order_tab_select));
                xbTab0.setBackgroundColor(getResources().getColor(R.color.white));
                break;
        }
    }

    private void vs(int index) {
        switch (index) {
            case SHOW_ORDER:
                if (isGone) {
                    for (int i = 1; i < linearLayout.getChildCount() - 1; i++) {
                        linearLayout.getChildAt(i).setVisibility(View.VISIBLE);
                    }
                    linearLayout.getChildAt(linearLayout.getChildCount() - 1).setVisibility(View.GONE);
                    isGone = false;

                    showTab(index);
                }
                break;
            case HIDE_ORDER:
                if (!isGone) {
                    for (int i = 1; i < linearLayout.getChildCount() - 1; i++) {
                        linearLayout.getChildAt(i).setVisibility(View.GONE);
                    }
                    linearLayout.getChildAt(linearLayout.getChildCount() - 1).setVisibility(View.VISIBLE);
                    isGone = true;

                    showTab(index);
                }
                break;
        }
    }

    private ListView buildOrderDealListView() {
        mStockListView = new ListView(getContext());
        mStockListView.setDivider(null);
        mStockListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                vs(SHOW_ORDER);
            }
        });
        mStockAdapter = new StockAdapter();
        mStockListView.setAdapter(mStockAdapter);
        mStockListView.setSelection(99);
        linearLayout.addView(mStockListView, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        return mStockListView;
    }

    class StockAdapter extends BaseAdapter {
//        @Override
//        public boolean areAllItemsEnabled() {
//            return false;
//        }
//
//        @Override
//        public boolean isEnabled(int position) {
//            return false;
//        }

        @Override
        public int getCount() {
            return 100;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mHolder = null;
            if (convertView == null) {
                mHolder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_adapter, null);
                mHolder.time = (TextView) convertView.findViewById(R.id.order_deal_time);
                mHolder.price = (TextView) convertView.findViewById(R.id.order_deal_price);
                mHolder.count = (TextView) convertView.findViewById(R.id.order_deal_count);
                convertView.setTag(mHolder);
            } else {
                mHolder = (StockAdapter.ViewHolder) convertView.getTag();
            }
            mHolder.time.setText("666");
            mHolder.price.setText("888");
            mHolder.count.setText("999");
            return convertView;
        }

        class ViewHolder {
            TextView time;
            TextView price;
            TextView count;
        }
    }

    private StockAdapter mStockAdapter;

    public void setOrderDealData() {
        initView();
    }

    private TextView addListHeaderTextView(String headerName, int AWidth, LinearLayout fixHeadLayout) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(AWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        TextView textView = new TextView(getContext());
        textView.setLayoutParams(lp);
        textView.setText(headerName);
        textView.setTextColor(0xff717171);
        textView.setGravity(Gravity.CENTER);
        fixHeadLayout.addView(textView);
        return textView;
    }

    public void setTopPadding(int value) {
        mTopPadding = value;
    }

    /**
     * 必须先初始化顶部标题栏
     *
     * @param btnListData
     */
    public void setBtnListData(String[] btnListData) {
        if (btnListData == null) {
            return;
        }
        this.btnText = btnListData;
        btnWidth = new int[btnListData.length];
        for (int i = 0; i < btnListData.length; i++) {
            btnWidth[i] = dp2Px(getContext(), mMoveViewWidth);
        }
    }

    private float close;

    public void setClose(float close) {
        this.close = close;
    }

    private List<String[]> buys;
    private List<String[]> sells;

    public void setOrder(List<String[]> buys, List<String[]> sells) {
        this.buys = buys;
        this.sells = sells;
//        linearLayout.removeViews(1, linearLayout.getChildCount() - 2);
        this.removeAllViews();
//        initOrderView();
        initView();
    }
}

