package com.wxx.sqlite.annotation

/**
 * @author ：wuxinxi on 2020/5/15 .
 * @packages ：com.wxx.sqlite.annotation .
 * TODO: 标识该属性在表中对应的列名称
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Property(val name: String)