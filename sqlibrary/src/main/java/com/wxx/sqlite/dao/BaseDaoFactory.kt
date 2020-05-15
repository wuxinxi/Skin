package com.wxx.sqlite.dao

import android.database.sqlite.SQLiteDatabase
import android.os.Environment
import java.io.File

/**
 * @author ：wuxinxi on 2020/5/15 .
 * @packages ：com.wxx.sqlite.dao .
 * TODO:一句话描述
 */
class BaseDaoFactory private constructor() {

    companion object {
        val instance: BaseDaoFactory by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            BaseDaoFactory()
        }
    }

    private var mSQLiteDatabase: SQLiteDatabase

    init {
        val path = Environment.getExternalStorageDirectory().toString() + File.separator + "dao.db"
        mSQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(path, null)
    }

    fun <T> getBaseDao(entityClass: Class<T>): BaseDao<T> {
        val baseDao = BaseDao<T>().javaClass.newInstance()
        baseDao.init(mSQLiteDatabase, entityClass,log = true)
        return baseDao
    }


}