package com.silladus.stock;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.btn_min)
    TextView btnMin;
    @Bind(R.id.btn_day)
    TextView btnDay;
    @Bind(R.id.btn_5day)
    TextView btn5Day;
    @Bind(R.id.btn_week)
    TextView btnWeek;
    @Bind(R.id.btn_month)
    TextView btnMonth;
    @Bind(R.id.btn_mins)
    TextView btnMins;
    private Fragment[] fragments;
    private int currentIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        boolean isVertical = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        if (!isVertical) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fragments = new Fragment[3];
        btnMin.setBackgroundColor(Color.WHITE);
        showFragment(0);
        if (!isVertical) {
            showOnly(R.id.xxxxx);
        }
    }

    @OnClick({R.id.btn_min, R.id.btn_5day, R.id.btn_day, R.id.btn_week, R.id.btn_month, R.id.btn_mins})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_min:
                showFragment(0);
                btnMin.setBackgroundColor(Color.WHITE);
                btnDay.setBackgroundColor(0x00000000);
                btn5Day.setBackgroundColor(0x00000000);
                btnWeek.setBackgroundColor(0x00000000);
                btnMonth.setBackgroundColor(0x00000000);
                btnMins.setText("分钟v");
                btnMins.setBackgroundColor(0x00000000);
                break;
            case R.id.btn_5day:
                showFragment(1);
                btnMin.setBackgroundColor(0x00000000);
                btnDay.setBackgroundColor(0x00000000);
                btn5Day.setBackgroundColor(Color.WHITE);
                btnWeek.setBackgroundColor(0x00000000);
                btnMonth.setBackgroundColor(0x00000000);
                btnMins.setText("分钟v");
                btnMins.setBackgroundColor(0x00000000);
                break;
            case R.id.btn_day:
                showFragment(2);
                btnMin.setBackgroundColor(0x00000000);
                btnDay.setBackgroundColor(Color.WHITE);
                btn5Day.setBackgroundColor(0x00000000);
                btnWeek.setBackgroundColor(0x00000000);
                btnMonth.setBackgroundColor(0x00000000);
                btnMins.setText("分钟v");
                btnMins.setBackgroundColor(0x00000000);
                break;
            case R.id.btn_week:
                btnMin.setBackgroundColor(0x00000000);
                btnDay.setBackgroundColor(0x00000000);
                btn5Day.setBackgroundColor(0x00000000);
                btnWeek.setBackgroundColor(Color.WHITE);
                btnMonth.setBackgroundColor(0x00000000);
                btnMins.setText("分钟v");
                btnMins.setBackgroundColor(0x00000000);
                break;
            case R.id.btn_month:
                btnMin.setBackgroundColor(0x00000000);
                btnDay.setBackgroundColor(0x00000000);
                btn5Day.setBackgroundColor(0x00000000);
                btnWeek.setBackgroundColor(0x00000000);
                btnMonth.setBackgroundColor(Color.WHITE);
                btnMins.setText("分钟v");
                btnMins.setBackgroundColor(0x00000000);
                break;
            case R.id.btn_mins:
                showMinMenu(view);
//                btnMin.setBackgroundColor(0x00000000);
//                btnDay.setBackgroundColor(0x00000000);
//                btn5Day.setBackgroundColor(0x00000000);
//                btnWeek.setBackgroundColor(0x00000000);
//                btnMonth.setBackgroundColor(0x00000000);
//                btnMins.setBackgroundColor(Color.WHITE);
                break;
        }
    }

    public void showOnly(int layoutResource) {
        ViewGroup layout = (ViewGroup) findViewById(layoutResource).getParent();
        for (int i = 0; i < layout.getChildCount(); i++) {
            if (layout.getChildAt(i) != findViewById(layoutResource) && layout.getChildAt(i) != findViewById(R.id.trend)) {
                layout.getChildAt(i).setVisibility(View.GONE);
            }
        }
        LinearLayout ll = new LinearLayout(this);
        ll.setBackgroundColor(0xff00ff00);
        ll.addView(newTextView("上证指数 SH1A001"));
        ll.addView(newTextView("3200.86(-0.24%)↓"));
        ll.addView(newTextView("振幅 0.86%↓"));
        layout.addView(ll, 0);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1);
        findViewById(R.id.trend).setLayoutParams(lp);
    }

    private TextView newTextView(String text) {
        TextView tv = new TextView(this);
        tv.setGravity(Gravity.CENTER);
        tv.setText(text);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, 100, 1);
        tv.setLayoutParams(lp);
        return tv;
    }

    private int[] calculatePopWindowPos(final View anchorView, final View contentView) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        // 获取屏幕的高宽
        final int screenHeight = getResources().getDisplayMetrics().heightPixels;
        final int screenWidth = getResources().getDisplayMetrics().widthPixels;
        // 测量contentView
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getMeasuredWidth();
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = screenHeight - anchorLoc[1] - anchorHeight < windowHeight;
        if (isNeedShowUp) {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] - windowHeight;
        } else {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] + anchorHeight;
        }
        return windowPos;
    }

    private View getPopupWindowContentView() {
        // 一个自定义的布局，作为显示的内容
        int layoutId = R.layout.popup_menu_layout;   // 布局ID
        View contentView = LayoutInflater.from(this).inflate(layoutId, null);
        View.OnClickListener menuItemOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Click " + ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
                btnMins.setBackgroundColor(Color.WHITE);
                switch (v.getId()) {
                    case R.id.menu_60min:
                        btnMin.setBackgroundColor(0x00000000);
                        btnDay.setBackgroundColor(0x00000000);
                        btn5Day.setBackgroundColor(0x00000000);
                        btnWeek.setBackgroundColor(0x00000000);
                        btnMonth.setBackgroundColor(0x00000000);
                        btnMins.setText(60 + "分v");
                        break;
                    case R.id.menu_30min:
                        btnMin.setBackgroundColor(0x00000000);
                        btnDay.setBackgroundColor(0x00000000);
                        btn5Day.setBackgroundColor(0x00000000);
                        btnWeek.setBackgroundColor(0x00000000);
                        btnMonth.setBackgroundColor(0x00000000);
                        btnMins.setText(30 + "分v");
                        break;
                    case R.id.menu_15min:
                        btnMin.setBackgroundColor(0x00000000);
                        btnDay.setBackgroundColor(0x00000000);
                        btn5Day.setBackgroundColor(0x00000000);
                        btnWeek.setBackgroundColor(0x00000000);
                        btnMonth.setBackgroundColor(0x00000000);
                        btnMins.setText(15 + "分v");
                        break;
                    case R.id.menu_5min:
                        btnMin.setBackgroundColor(0x00000000);
                        btnDay.setBackgroundColor(0x00000000);
                        btn5Day.setBackgroundColor(0x00000000);
                        btnWeek.setBackgroundColor(0x00000000);
                        btnMonth.setBackgroundColor(0x00000000);
                        btnMins.setText(5 + "分v");
                        break;
                }
                if (mPopupWindow != null) {
                    mPopupWindow.dismiss();
                }
            }
        };
        contentView.findViewById(R.id.menu_60min).setOnClickListener(menuItemOnClickListener);
        contentView.findViewById(R.id.menu_30min).setOnClickListener(menuItemOnClickListener);
        contentView.findViewById(R.id.menu_15min).setOnClickListener(menuItemOnClickListener);
        contentView.findViewById(R.id.menu_5min).setOnClickListener(menuItemOnClickListener);
        return contentView;
    }

    private PopupWindow mPopupWindow;
    private int[] windowPos;

    private void showMinMenu(View anchorView) {
        if (mPopupWindow == null) {
            View contentView = getPopupWindowContentView();
            mPopupWindow = new PopupWindow(contentView, getResources().getDisplayMetrics().widthPixels / 6, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            // 如果不设置PopupWindow的背景，有些版本就会出现一个问题：无论是点击外部区域还是Back键都无法dismiss弹框
            mPopupWindow.setBackgroundDrawable(new ColorDrawable());
            // 设置好参数之后再show
            windowPos = calculatePopWindowPos(anchorView, contentView);

//            WindowManager.LayoutParams params = getWindow().getAttributes();//创建当前界面的一个参数对象
//            params.alpha = 0.8f;
//            getWindow().setAttributes(params);//把该参数对象设置进当前界面中
//            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                @Override
//                public void onDismiss() {
//                    WindowManager.LayoutParams params = getWindow().getAttributes();
//                    params.alpha = 1.0f;//设置为不透明，即恢复原来的界面
//                    getWindow().setAttributes(params);
//                }
//            });
        }
//        int xOff = 20; // 可以自己调整偏移
//        windowPos[0] -= xOff;
        mPopupWindow.showAtLocation(anchorView, Gravity.TOP | Gravity.START, windowPos[0], windowPos[1]);
    }

    private void showFragment(int index) {
        if (index != currentIndex) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            //隐藏当前显示的
            if (currentIndex != -1) {
                ft.hide(fragments[currentIndex]);
            }
            //显示新的内容
            if (fragments[index] != null) {
                ft.show(fragments[index]);
            } else {
                //创建并且添加进来
                fragments[index] = createFragment(index);
                ft.add(R.id.trend, fragments[index]);
            }
            ft.commit();
            currentIndex = index;
        }
    }

    private Fragment createFragment(int index) {
        switch (index) {
            case 0:
                return new FragmentMT();
            case 1:
                return new FragmentMT5();
            case 2:
                return new FragmentK();
        }
        return null;
    }

//    @Override
//    protected void onDestroy() {
//        int len = fragments.length;
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        for (int i = 0; i < len; i++) {
//            if (fragments[i] != null) {
//                ft.remove(fragments[i]);
//            }
//        }
//        ft.commitAllowingStateLoss();
//        super.onDestroy();
//    }
}
