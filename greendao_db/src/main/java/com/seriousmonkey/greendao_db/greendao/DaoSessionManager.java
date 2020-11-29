package com.seriousmonkey.greendao_db.greendao;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.os.FileUtils;

import java.io.File;


public class DaoSessionManager {
    public enum IoState {
        Readable, Writable;
    }

    private final String DB_NAME = "message_sync.db";
//    private final String DB_PATH = "AndroidDevelopment/nc/miss08/database";
    private DaoMaster.DevOpenHelper mHelper;
    private DaoSession daoSessionReadable;
    private DaoSession daoSessionWritable;

    private DaoSessionManager() {
    }

    /**
     * Singleton MODE
     */
    public static DaoSessionManager mInstance = new DaoSessionManager();

    public static DaoSessionManager getInstace() {

        return mInstance;
    }

    public DaoMaster getDaoMaster(Context mContext, IoState ioState) {
        if (mHelper == null) {
            mHelper = new DaoMaster.DevOpenHelper(mContext, DB_NAME, null);
        }
        switch (ioState) {
            case Readable:
                return new DaoMaster(mHelper.getReadableDatabase());
            case Writable:
                return new DaoMaster(mHelper.getWritableDatabase());
            default:
                return null;
        }
    }

//    /**
//     * 自定义数据库文件存储位置
//     * @param mContext
//     * @return
//     */
//    public DaoMaster getDaoMaster(Context mContext, final String path) {
//
//        DaoMaster.DevOpenHelper mHelper = new VersionChangeHelper(new ContextWrapper(mContext) {
//
//            @Override
//            public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
//                return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
//            }
//
//            @Override
//            public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
//                return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
//            }
//
//            @Override
//            public File getDatabasePath(String name) {
//                File file = FileUtils.buildDataBasePath(path, name);
//                return file != null ? file : super.getDatabasePath(name);
//            }
//        }, DB_NAME);
//        daoMaster = new DaoMaster(mHelper.getWritableDatabase());
//        return daoMaster;
//    }

    public DaoSession getDaoSessionWritable(Context mContext) {

        if (daoSessionWritable == null) {
            daoSessionWritable = getDaoMaster(mContext, IoState.Writable).newSession();
        }
        return daoSessionWritable;
    }

    public DaoSession getDaoSessionReadable(Context mContext) {

        if (daoSessionReadable == null) {
            daoSessionReadable = getDaoMaster(mContext, IoState.Readable).newSession();
        }
        return daoSessionReadable;
    }

}