package com.baihe.common.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.orhanobut.logger.Logger;


/**
 * Author：xubo
 * Time：2019-05-14
 * Description：数据库帮助类
 */

public class DBHelper extends OrmLiteSqliteOpenHelper {
    private static int databaseVersion;
    private static String databaseName;
    private static Class<?>[] databaseClasses;
    private static UpgradeListener upgradeListener;

    public DBHelper(Context context) {
        super(context, databaseName, null, databaseVersion);
    }

    /**
     * 数据库初始化
     * @param dbName 数据库名
     * @param dbVersion 数据库版本
     * @param dbClasses 表集合
     * @param upgradeListener 升级监听
     */
    public static void init(String dbName, int dbVersion, Class<?>[] dbClasses, UpgradeListener upgradeListener) {
        DBHelper.databaseName = dbName;
        DBHelper.databaseVersion = dbVersion;
        DBHelper.databaseClasses = dbClasses;
        DBHelper.upgradeListener = upgradeListener;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            if (this.databaseClasses != null) {
                for (Class<?> databaseClass : databaseClasses) {
                    TableUtils.createTable(connectionSource, databaseClass);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Logger.w("数据库建表错误", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int databaseVersion, int configFileId) {
        if (DBHelper.databaseVersion > databaseVersion && upgradeListener != null) {
            upgradeListener.onUpgrade(sqLiteDatabase, connectionSource, databaseVersion, configFileId);
        }
    }

    @Override
    public void close() {
        super.close();
        try {
            if (databaseClasses != null) {
                for (Class<?> databaseClass : databaseClasses) {
                    getDao(databaseClass).clearObjectCache();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Logger.w("数据库关闭异常", e);
        }
    }
}
