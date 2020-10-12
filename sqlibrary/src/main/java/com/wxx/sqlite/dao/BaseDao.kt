package com.wxx.sqlite.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.wxx.sqlite.annotation.Entity
import com.wxx.sqlite.annotation.Id
import com.wxx.sqlite.annotation.Property
import java.lang.reflect.Field

/**
 * @author ：wuxinxi on 2020/5/15 .
 * @packages ：com.wxx.sqlite.dao .
 * TODO:一句话描述
 */
class BaseDao<T> : IBaseDao<T> {

    private lateinit var mSlqSQLiteDatabase: SQLiteDatabase
    private lateinit var mTableName: String
    private lateinit var mEntityClass: Class<T>
    private var isInit = false
    private var isPrintLog = false
    /**
     * 用来缓存字段名与成员变量
     */
    private var fieldCache = HashMap<String, CacheProperty>()

    @Synchronized
    fun  init(slqSQLiteDatabase: SQLiteDatabase, entityClass: Class<T>, log: Boolean = false) {
        mSlqSQLiteDatabase = slqSQLiteDatabase
        mEntityClass = entityClass
        isPrintLog = log
        if (!isInit) {
            createTable(slqSQLiteDatabase, entityClass)
            initFieldCache()
            isInit = true
        }
    }

    /**
     * 创建表
     */
    private fun createTable(slqSQLiteDatabase: SQLiteDatabase, entityClass: Class<T>) {
        mTableName = if (entityClass.getAnnotation(Entity::class.java) == null) {
            throw IllegalAccessException("${entityClass.simpleName} 未添加@Entity注解")
        } else {
            entityClass.simpleName.toUpperCase()
        }

        val createTableSql = getCreateTableSql()
        printLog(createTableSql)
        slqSQLiteDatabase.execSQL(createTableSql)
    }

    /**
     * 生成创建表的Sql语句
     * 通过注解+反射
     */
    private fun getCreateTableSql(): String {
        val sql = StringBuilder()

        sql.append("CREATE TABLE IF NOT EXISTS ")
        sql.append(mTableName).append(" (")
        val declaredFields = mEntityClass.declaredFields
        var primaryKeyExists = false
        for (field in declaredFields) {
            var propertyName =
                field.getAnnotation(Property::class.java)?.name?.toUpperCase() ?: field.name.toUpperCase()
            //Kotlin 自动生成的UID
            if (propertyName == "SERIALVERSIONUID") {
                continue
            }
            when (field.type) {
                String::class.java -> sql.append(propertyName).append(" TEXT,")

                Int::class.java, java.lang.Integer::class.java -> {
                    sql.append(propertyName).append(" INTEGER,")
                }

                Long::class.java, java.lang.Long::class.java -> {
                    printLog("Long 类型")
                    var primaryKeySql = ""
                    var type = "BIGINT"
                    //判断主键
                    if (field.getAnnotation(Id::class.java) != null) {
                        if (primaryKeyExists) {
                            throw IllegalAccessException("primaryKey existing")
                        }
                        val autoincrement = field.getAnnotation(Id::class.java)!!.autoincrement
                        primaryKeyExists = true
                        primaryKeySql = " PRIMARY KEY ${if (autoincrement) "AUTOINCREMENT" else ""}"
                        //如果是主键必须是Integer类型
                        type = "INTEGER"
                        propertyName = "_id"
                    }
                    sql.append(propertyName).append(" $type$primaryKeySql,")
                }

                Double::class.java, java.lang.Double::class.java -> {
                    sql.append(propertyName).append(" DOUBLE,")
                }

            }
        }

        //去除最后一个符号
        if (sql[sql.length - 1] == ',') {
            sql.deleteCharAt(sql.length - 1)
        }
        sql.append(")")
        return sql.toString()
    }

    private fun initFieldCache() {
        //空表查询
        val queryTableColumnSql = "SELECT * FROM $mTableName limit 1 , 0"
        val cursor = mSlqSQLiteDatabase.rawQuery(queryTableColumnSql, null)
        //获取所有列名
        val columnNames = cursor.columnNames
        //类 属性
        val declaredFields = mEntityClass.declaredFields
        for (columnName in columnNames) {
            var cacheProperty: CacheProperty? = null
            for (field in declaredFields) {
                field.isAccessible = true
                //1. 判断是否有Id注解
                val dbColumnName = if (field.getAnnotation(Id::class.java) != null) {
                    "_id"
                } else {
                    //2.判断是否存在Property注解
                    field.getAnnotation(Property::class.java)?.name?.toUpperCase() ?: field.name.toUpperCase()
                }


                if (columnName == dbColumnName) {
                    cacheProperty = CacheProperty(field, columnName)
                }
            }
            if (cacheProperty != null) {
                fieldCache[columnName] = cacheProperty
            }
        }
        cursor.close()
    }

    override fun insert(entity: T): Long {
        val map = getValues(entity)
        val contentValues = getContentValues(map)

        return try {
            return mSlqSQLiteDatabase.insert(mTableName, null, contentValues)
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }

    private fun getValues(entity: T): HashMap<String, String> {
        val map = HashMap<String, String>()
        fieldCache.forEach {
            val field = it.value.field
            field.isAccessible = true
            //获取对象的属性值
            val value = field.get(entity)?.toString() ?: return@forEach
            val key = it.value.columnName
            map[key] = value
        }
        return map
    }

    private fun getContentValues(map: HashMap<String, String>): ContentValues {
        val contentValues = ContentValues()
        map.forEach {
            it.key
            contentValues.put(it.key, it.value)
        }
        return contentValues
    }

    /**
     * @param entity 新的数据
     * @param where 条件
     */
    override fun update(entity: T, where: T): Int {
        val map = getValues(entity)
        val contentValues = getContentValues(map)
        val whereMap = getValues(where)
        val condition = Condition(whereMap)
        return try {
            mSlqSQLiteDatabase.update(
                mTableName,
                contentValues,
                condition.whereClauseBuild.toString(),
                condition.whereArgs.toTypedArray()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            -1
        }
    }

    override fun delete(where: T?): Int {
        return if (where == null) {
            mSlqSQLiteDatabase.delete(mTableName, null, null)
        } else {
            val whereMap = getValues(where)
            val condition = Condition(whereMap)
            mSlqSQLiteDatabase.delete(
                mTableName,
                condition.whereClauseBuild.toString(),
                condition.whereArgs.toTypedArray()
            )
        }
    }

    override fun query(where: T?): List<T> {
        return query(where = where, orderBy = null, startIndex = -1, limit = -1)
    }


    override fun query(where: T?, orderBy: String?, startIndex: Int, limit: Int): List<T> {
        val limitStr = if (startIndex > 0 && limit > 0) "$startIndex,$limit" else null
        if (where == null) {
            val cursor =
                mSlqSQLiteDatabase.query(false, mTableName, null, null, null, null, null, orderBy, limitStr)
            cursor.use {
                return queryByCursor(it)
            }
        }
        val whereMap = getValues(where)
        val condition = Condition(whereMap)

        val cursor = mSlqSQLiteDatabase.query(
            false,
            mTableName,
            null,
            condition.whereClauseBuild.toString(),
            condition.whereArgs.toTypedArray(),
            null,
            null,
            orderBy,
            limitStr
        )
        cursor.use {
            return queryByCursor(it)
        }
    }

    private fun queryByCursor(cursor: Cursor): List<T> {
        val list = mutableListOf<T>()
        while (cursor.moveToNext()) {
            val obj = mEntityClass.newInstance()
            fieldCache.forEach {
                //数据库列名
                val columnName = it.value.columnName
                //通过列名取得下标
                val columnIndex = cursor.getColumnIndex(columnName)
                val field = it.value.field
                val type = field.type
                if (columnIndex != -1) {
                    when (type) {
                        //通过下标设置值
                        String::class.java -> field.set(obj, cursor.getString(columnIndex))
                        Double::class.java, java.lang.Double::class.java -> field.set(
                            obj,
                            cursor.getDouble(columnIndex)
                        )
                        Long::class.java, java.lang.Long::class.java -> field.set(
                            obj,
                            cursor.getLong(columnIndex)
                        )
                        else -> return@forEach
                    }
                }
            }
            list.add(obj)
        }
        return list
    }

    /**
     * 缓存属性信息
     */
    class CacheProperty(val field: Field, val columnName: String)

    class Condition(whereMap: HashMap<String, String>) {
        val whereArgs = mutableListOf<String>()
        val whereClauseBuild = StringBuilder("1=1")

        init {
            whereMap.forEach {
                val key = it.key
                //拼接条件
                whereClauseBuild.append(" and ").append(key).append("=?")
                whereArgs.add(it.value)
            }
        }
    }


    private fun printLog(log: String) {
        if (isPrintLog) {
            Log.d(this::class.java.simpleName, log)
        }
    }

}