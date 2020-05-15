package com.wxx.sqlite.dao

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.wxx.sqlite.annotation.Entity
import com.wxx.sqlite.annotation.Id
import com.wxx.sqlite.annotation.Property

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

    @Synchronized
    fun init(slqSQLiteDatabase: SQLiteDatabase, entityClass: Class<T>, log: Boolean = false) {
        mSlqSQLiteDatabase = slqSQLiteDatabase
        mEntityClass = entityClass
        isPrintLog = log
        if (!isInit) {
            createTable(slqSQLiteDatabase, entityClass)
            isInit = true
        }
    }

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

    private fun getCreateTableSql(): String {
        val sql = StringBuilder()

        sql.append("CREATE TABLE IF NOT EXISTS ")
        sql.append(mTableName).append(" (")
        val declaredFields = mEntityClass.declaredFields
        var primaryKeyExists = false
        for (field in declaredFields) {
            val propertyName =
                field.getAnnotation(Property::class.java)?.name?.toUpperCase() ?: field.name.toUpperCase()
            //Kotlin 自动生成的UID
            if (propertyName == "SERIALVERSIONUID") {
                continue
            }

            when (field.type) {
                String::class.java -> sql.append(propertyName).append(" TEXT,")

                Int::class.java -> {
                    sql.append(propertyName).append(" INTEGER,")
                }

                Long::class.java -> {
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
                    }
                    sql.append(propertyName).append(" $type$primaryKeySql,")
                }

                Double::class.java -> {
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


    override fun insert(entity: T): Long {
        return 0
    }

    override fun update(entity: T, where: T): Long {
        return 0
    }

    override fun delete(id: Long): Long {
        return 0
    }

    override fun query(where: T): List<T> {
        return arrayListOf()
    }

    override fun query(id: Long): T? {
        return null
    }


    private fun printLog(log: String) {
        if (isPrintLog) {
            Log.d(this::class.java.simpleName, log)
        }
    }
}