package com.seriousmonkey.greendao_db.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

public class VersionChangeHelper extends DaoMaster.DevOpenHelper {

    public VersionChangeHelper(Context context, String name) {
        this(context, name, null);
    }

    public VersionChangeHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        //super.onUpgrade(db, oldVersion, newVersion);

        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {

            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                Log.e("dongyiming", "onCreateAllTables");
                //DaoMaster.createAllTables(db, ifNotExists);
            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                Log.e("dongyiming", "onDropAllTables");
                //DaoMaster.dropAllTables(db, ifExists);
            }
        }, ShortMessageDao.class);

        //MigrationHelper.migrate(db, MenuInfoDao.class);
        Log.e("dongyiming", "onUpgrade");
//        SingerDao.dropTable(db, true);
    }
}