package com.silladus.stock;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

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
                break;
            case R.id.btn_5day:
                showFragment(1);
                btnMin.setBackgroundColor(0x00000000);
                btnDay.setBackgroundColor(0x00000000);
                btn5Day.setBackgroundColor(Color.WHITE);
                btnWeek.setBackgroundColor(0x00000000);
                btnMonth.setBackgroundColor(0x00000000);
                break;
            case R.id.btn_day:
                showFragment(2);
                btnMin.setBackgroundColor(0x00000000);
                btnDay.setBackgroundColor(Color.WHITE);
                btn5Day.setBackgroundColor(0x00000000);
                btnWeek.setBackgroundColor(0x00000000);
                btnMonth.setBackgroundColor(0x00000000);
                break;
            case R.id.btn_week:
                btnMin.setBackgroundColor(0x00000000);
                btnDay.setBackgroundColor(0x00000000);
                btn5Day.setBackgroundColor(0x00000000);
                btnWeek.setBackgroundColor(Color.WHITE);
                btnMonth.setBackgroundColor(0x00000000);
                break;
            case R.id.btn_month:
                btnMin.setBackgroundColor(0x00000000);
                btnDay.setBackgroundColor(0x00000000);
                btn5Day.setBackgroundColor(0x00000000);
                btnWeek.setBackgroundColor(0x00000000);
                btnMonth.setBackgroundColor(Color.WHITE);
                break;
            case R.id.btn_mins:
                showMinMenu();
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

    private PopupMenu menu;

    private void showMinMenu() {
        if (menu == null) {
            menu = new PopupMenu(this, btnMins);
            menu.inflate(R.menu.stock_tab_menu);
            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.item_60min:
                            Log.e("onMenuItemClick: ", "--------60min-------");
                            return true;
                        case R.id.item_30min:
                            Log.e("onMenuItemClick: ", "--------30min-------");
                            return true;
                        case R.id.item_15min:
                            Log.e("onMenuItemClick: ", "--------15min-------");
                            return true;
                        case R.id.item_5min:
                            Log.e("onMenuItemClick: ", "--------5min-------");
                            return true;
                    }
                    return false;
                }
            });
        }
        menu.show();
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
