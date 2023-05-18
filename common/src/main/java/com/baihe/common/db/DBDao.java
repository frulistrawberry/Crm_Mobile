package com.baihe.common.db;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.orhanobut.logger.Logger;

import java.sql.SQLException;

/**
 * Author：xubo
 * Time：2019-05-14
 * Description：数据库持久类
 */

public class DBDao<T, ID> extends RuntimeExceptionDao<T, ID> {
    public DBDao(Dao<T, ID> dao) {
        super(dao);
        // TODO Auto-generated constructor stub
    }

    public static <T, ID> DBDao<T, ID> getDBDao(Context context, Class<T> t) {
        try {
            Dao<T, ID> dao = OpenHelperManager.getHelper(context, DBHelper.class).getDao(t);
            return new DBDao<T, ID>(dao);
        } catch (SQLException e) {
            e.printStackTrace();
            Logger.w("数据库模型持久化失败", e);
        }
        return null;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        OpenHelperManager.releaseHelper();
    }
}
