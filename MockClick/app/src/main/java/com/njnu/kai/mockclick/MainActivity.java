package com.njnu.kai.mockclick;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.njnu.kai.mockclick.util.Injector;
import com.njnu.kai.mockclick.util.ToastUtils;
import com.njnu.kai.mockclick.win.FloatViewController;

import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar mToolbar;
    private View mLayoutContent;
    private TextView mTvResult;

    private HandlerThread mHandlerThread;
    private ClickHandler mClickHandler;

    private FloatViewController mFloatViewController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mLayoutContent = findViewById(R.id.content_main);
        mTvResult = (TextView) mLayoutContent.findViewById(R.id.tv_result);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mHandlerThread = new HandlerThread("kai-mock-click–thread");
        mHandlerThread.start();
        mClickHandler = new ClickHandler(mHandlerThread.getLooper());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_float_window);
        menuItem.setTitle(mFloatViewController != null ? "隐藏悬浮按钮" : "显示悬浮按钮");
        return true;
    }

    private FloatViewController.ViewDismissHandler mFloatViewDismissHandler = new FloatViewController.ViewDismissHandler() {
        @Override
        public void onViewDismiss() {
            mFloatViewController = null;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        boolean result = true;
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
        } else if (id == R.id.action_float_window) {
            if (mFloatViewController == null) {
                mFloatViewController = new FloatViewController(this);
                mFloatViewController.setViewDismissHandler(mFloatViewDismissHandler);
                mFloatViewController.show();
            } else {
                mFloatViewController.removePoppedViewAndClear();
                mFloatViewController = null;
            }
        } else {
            result = super.onOptionsItemSelected(item);
        }

        return result;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {

        mLayoutContent.postDelayed(new Runnable() {
            @Override
            public void run() {
                executeMenu(item.getItemId());
            }
        }, 500);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void executeMenu(final int menuId) {
        if (menuId == R.id.nav_manage) {

        } else {
            if (menuId == R.id.nav_su_input_click
                    || menuId == R.id.nav_su_input_click_multi_10
                    || menuId == R.id.nav_su_input_click_multi_100
                    || menuId == R.id.nav_su_input_click_multi_1000
                    ) {
                if (!Injector.canLargeExecuteCommand()) {
                    ToastUtils.showToast(this, "请等待现有命令执行完毕");
                    return;
                }
                if (menuId == R.id.nav_su_input_click_multi_100 || menuId == R.id.nav_su_input_click_multi_1000) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("大循环次数确认");
                    builder.setMessage(String.format(Locale.getDefault()
                            , "即将循环 %d 次点击, 中途不可停止, 会影响手机使用, 确定吗?"
                            , menuId == R.id.nav_su_input_click_multi_100 ? 100 : 1000));
                    builder.setNegativeButton("放弃", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ToastUtils.showToast(MainActivity.this, "已取消循环点击任务");
                        }
                    });
                    builder.setPositiveButton("走起", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mClickHandler.sendEmptyMessage(menuId);
                        }
                    });
                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            ToastUtils.showToast(MainActivity.this, "自动取消循环点击");
                        }
                    });
                    builder.show();
                    return;
                }
            }
            mClickHandler.sendEmptyMessage(menuId);
        }
    }

    @Override
    protected void onDestroy() {
        mClickHandler.removeCallbacksAndMessages(null);
        if (mHandlerThread != null) {
            mHandlerThread.quit();
        }
        super.onDestroy();
    }

    private StringBuilder mBuilder = new StringBuilder();
    private int mEventMoveCount;
    private int mEventOtherCount;

    private int mClickCount;

    private long mLastElapsedTime;

    private long mFirstElapsedTime;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int actionMasked = event.getActionMasked();
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                if (SystemClock.elapsedRealtime() - mLastElapsedTime > 300) {
                    mClickCount = 0;
                    mFirstElapsedTime = SystemClock.elapsedRealtime();
                }
                mBuilder.setLength(0);
                mEventMoveCount = 0;
                mEventOtherCount = 0;
                mBuilder.append(String.format(Locale.getDefault(), "down:[%d,%d]", rawX, rawY));
                break;

            case MotionEvent.ACTION_MOVE:
                ++mEventMoveCount;
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                ++mClickCount;
                mLastElapsedTime = SystemClock.elapsedRealtime();
                mBuilder.append(String.format(Locale.getDefault(), " move:%d other:%d", mEventMoveCount, mEventOtherCount));
                mBuilder.append(String.format(Locale.getDefault()
                        , " %s:[%d,%d] clicked:%d elapse:%d"
                        , actionMasked == MotionEvent.ACTION_UP ? "up" : "cancel"
                        , rawX, rawY, mClickCount, mLastElapsedTime - mFirstElapsedTime));
                mTvResult.setText(mBuilder.toString());
                break;

            default:
                ++mEventOtherCount;
                break;
        }
        return super.dispatchTouchEvent(event);
    }
}
