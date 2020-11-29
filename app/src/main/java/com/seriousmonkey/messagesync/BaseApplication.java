package com.seriousmonkey.messagesync;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * 初始化：<br/>
 * - 保存所有现有短信
 *
 * 新短信到达时，
 * - 本地存储数据副本
 * - 上传服务器保存
 */
public class BaseApplication extends Application {
    public static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

//        SMSDaoUtilImpl.getInstance().initData();
    }

    public static void quit() {
        Log.d(BaseApplication.class.getName(), "未实现");
    }
}
