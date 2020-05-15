package com.wxx.sqlite.annotation

/**
 * @author ：wuxinxi on 2020/5/15 .
 * @packages ：com.wxx.sqlite.annotation .
 * TODO: 标识实体类,表名为实体类名的大写形式
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Entity()