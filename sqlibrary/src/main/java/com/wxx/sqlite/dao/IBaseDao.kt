package com.wxx.sqlite.dao

/**
 * @author ：wuxinxi on 2020/5/15 .
 * @packages ：com.wxx.sqlite.dao .
 * TODO:一句话描述
 */
interface IBaseDao<T> {
    /**
     * 插入
     */
    fun insert(entity: T): Long

    /**
     * 修改
     */
    fun update(entity: T, where: T): Int


    /**
     * 删除
     */
    fun delete(where: T?=null): Int

    /**
     * 条件查询
     */
    fun query(where: T? = null): List<T>

    /**
     * @param where 条件
     * @param orderBy 排序
     * @param startIndex 开始位置
     * @param limit 条数，可配合startIndex使用 ,limit m,n 从第m条记录开始，往后取n条记录
     */
    fun query(where: T? = null, orderBy: String? = null, startIndex: Int = 1, limit: Int = -1): List<T>
}