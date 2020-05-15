package com.wxx.sqlite.annotation

/**
 * @author ：wuxinxi on 2020/5/15 .
 * @packages ：com.wxx.sqlite.annotation .
 * TODO: 标识主键，该字段的类型为long或Long类型，autoincrement设置是否自动增长,默认否
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Id(val autoincrement: Boolean = false)