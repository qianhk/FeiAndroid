package com.njnu.kai.mockclick;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar mToolbar;
    private View mLayoutContent;
    private TextView mTvResult;

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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                if (SystemClock.elapsedRealtime() - mLastElapsedTime > 600) {
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
