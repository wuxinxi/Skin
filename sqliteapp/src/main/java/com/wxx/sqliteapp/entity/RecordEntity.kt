package com.wxx.sqliteapp.entity

import com.wxx.sqlite.annotation.Entity
import com.wxx.sqlite.annotation.Id
import com.wxx.sqlite.annotation.Property

/**
 * @author ：wuxinxi on 2020/5/15 .
 * @packages ：com.wxx.sqliteapp.entity .
 * TODO:一句话描述
 */
@Entity
class RecordEntity(
    @Id(autoincrement = true) val id: Long?,
    val name: String,
    @Property("save_time") val time: String,
    val prices: Double,
    val status: Int
)