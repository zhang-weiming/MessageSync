package com.seriousmonkey.messagesync.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.ButterKnife;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.google.android.material.navigation.NavigationView;
import com.seriousmonkey.greendao_db.entity.ShortMessage;
import com.seriousmonkey.messagesync.BaseApplication;
import com.seriousmonkey.messagesync.R;
import com.seriousmonkey.messagesync.adapter.MessageAdapter;
import com.seriousmonkey.messagesync.dao.SMSDaoUtilImpl;
import com.seriousmonkey.messagesync.entity.ShortMessageItem;
import com.seriousmonkey.messagesync.ui.activity.AboutActivity;
import com.seriousmonkey.messagesync.ui.activity.DbManagementActivity;
import com.seriousmonkey.messagesync.ui.activity.DebugActivity;
import com.seriousmonkey.messagesync.ui.activity.SettingsActivity;
import com.seriousmonkey.messagesync.ui.view.RecycleViewDivider;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**每一页展示多少条数据*/
    private static final int REQUEST_COUNT = 10;
    /**已经获取到多少条数据了*/
    private static int mCurrentCounter = 0;

    private List<ShortMessageItem> messageList;

    private LRecyclerView messageListView;
    private LRecyclerViewAdapter lMessageAdapter;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    // refresh recyclerview
                    messageListView.refreshComplete(REQUEST_COUNT);
                    break;
                case 2:
                    // loadmore recyclerview
                    break;
            }

            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 隐藏默认actionbar
        setTheme(R.style.GadgetbridgeTheme_NoActionBar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // 使用自定义actionbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.controlcenter_navigation_drawer_open, R.string.controlcenter_navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // 左侧菜单栏
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        initData();
        initView();


        //申请写的权限
        String[] permissions = {
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.RECEIVE_MMS,
                Manifest.permission.RECEIVE_WAP_PUSH,
                Manifest.permission.ACCESS_NOTIFICATION_POLICY,
            };
        if(PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            requestPermissions(permissions,200);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        SMSDaoUtilImpl.getInstance().initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_update:
                Toast.makeText(this, "action update", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    private void initData() {
        List<ShortMessage> shortMsgList = SMSDaoUtilImpl.getInstance().queryAllData();
        messageList = new ArrayList<>();
        for (ShortMessage msg : shortMsgList) {
            messageList.add(ShortMessageItem.fromShortMessage(msg));
        }
    }

    private void initView() {
        messageListView = findViewById(R.id.messageListView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        messageListView.setLayoutManager(layoutManager);

        lMessageAdapter = new LRecyclerViewAdapter(new MessageAdapter(messageList));
        messageListView.setAdapter(lMessageAdapter);
        messageListView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL));

        // 设置下拉刷新样式
        messageListView.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);
        messageListView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
        messageListView.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);

        // 滑动事件
        messageListView.setLScrollListener(new LRecyclerView.LScrollListener() {
            @Override
            public void onScrollUp() {
            }

            @Override
            public void onScrollDown() {
            }

            @Override
            public void onScrolled(int distanceX, int distanceY) {
            }
            @Override
            public void onScrollStateChanged(int state) {

            }

        });

        // 下拉刷新
        messageListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getBaseContext(), "refresh", Toast.LENGTH_SHORT).show();

                // 子线程处理刷新
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        messageList.clear();
                        lMessageAdapter.notifyDataSetChanged();//必须调用此方法
                        refreshData();
                        handler.sendEmptyMessage(1);
                    }
                }).start();
            }
        });

        // 关闭加载更多
        messageListView.setLoadMoreEnabled(false);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.action_db_management:
                Intent dbIntent = new Intent(this, DbManagementActivity.class);
                startActivity(dbIntent);
                return true;
            case R.id.action_debug:
                Intent deIntent = new Intent(this, DebugActivity.class);
                startActivity(deIntent);
                return true;
            case R.id.action_quit:
                BaseApplication.quit();
                return true;
            case R.id.about:
                Intent aboutIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutIntent);
                return true;
        }

        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void refreshData() {
        List<ShortMessage> data = SMSDaoUtilImpl.getInstance().query(messageList.size(), REQUEST_COUNT);
        for (ShortMessage msg : data) {
            messageList.add(ShortMessageItem.fromShortMessage(msg));
        }
    }
}
