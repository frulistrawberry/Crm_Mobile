package com.baihe.common.db;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.support.ConnectionSource;

/**
 * Author：xubo
 * Time：2019-05-15
 * Description：数据库升级回调监听
 */
public interface UpgradeListener {

    /**
     * 升级操作
     * @param sqLiteDatabase
     * @param connectionSource
     * @param databaseVersion
     * @param configFileId
     */
    void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int databaseVersion, int configFileId);
}
