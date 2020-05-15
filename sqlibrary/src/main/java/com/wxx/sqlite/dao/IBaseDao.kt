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
    fun update(entity: T, where: T): Long


    /**
     * 删除
     */
    fun delete(id: Long): Long

    /**
     * 条件查询
     */
    fun query(where: T): List<T>

    /**
     * 根据id查询
     */
    fun query(id: Long): T?
}