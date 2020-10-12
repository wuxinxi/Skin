package com.wxx.sqliteapp

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.wxx.sqlite.dao.BaseDaoFactory
import com.wxx.sqliteapp.entity.TestEntity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        create.setOnClickListener {
            BaseDaoFactory.instance.getBaseDao(TestEntity::class.java)
        }


        insert.setOnClickListener {
            val testEntity = TestEntity()
            testEntity.key = System.currentTimeMillis().toString()
            testEntity.value = SystemClock.elapsedRealtime().toString()
            BaseDaoFactory.instance.getBaseDao(TestEntity::class.java).insert(testEntity)
        }

        update.setOnClickListener {
            val testEntity = TestEntity()
            testEntity.key = "New${System.currentTimeMillis()}"
            testEntity.value = "New${SystemClock.elapsedRealtime()}"
            val where = TestEntity()
            where.id = edit.text.toString().toLong()
            BaseDaoFactory.instance.getBaseDao(TestEntity::class.java).update(testEntity, where)
        }


        query.setOnClickListener {
            val list = BaseDaoFactory.instance.getBaseDao(TestEntity::class.java).query()
            for (testEntity in list) {
                Log.d("wuxinxi", "onCreate: $testEntity")
            }
            println("------------------------------------------")
        }

        queryLimit.setOnClickListener {
            val list = BaseDaoFactory.instance.getBaseDao(TestEntity::class.java).query(limit = 5)
            for (testEntity in list) {
                Log.d("wuxinxi", "前5条: $testEntity")
            }
            println("------------------------------------------")
        }


        queryCondition.setOnClickListener {
            val content = edit.text.toString()
            val testEntity = TestEntity()
            testEntity.id = if (content.isBlank()) 0L else content.toLong()
            val list = BaseDaoFactory.instance.getBaseDao(TestEntity::class.java).query(where = testEntity)
            for (testEntity2 in list) {
                Log.d("wuxinxi", "id=${testEntity.id}: $testEntity2")
            }
            println("------------------------------------------")
        }

        deleteCondition.setOnClickListener {
            val content = edit.text.toString()
            val testEntity = TestEntity()
            testEntity.id = if (content.isBlank()) 0L else content.toLong()
            val delete = BaseDaoFactory.instance.getBaseDao(TestEntity::class.java).delete(testEntity)
            println("删除了数据id=${testEntity.id},delete=$delete")
        }


        delete.setOnClickListener {
            val delete = BaseDaoFactory.instance.getBaseDao(TestEntity::class.java).delete()
            println("删除了全部数据,delete=$delete")
        }


    }
}
