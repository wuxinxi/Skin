package com.wxx.sqliteapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wxx.sqlite.dao.BaseDaoFactory
import com.wxx.sqliteapp.entity.RecordEntity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn.setOnClickListener {
            BaseDaoFactory.instance.getBaseDao(RecordEntity::class.java)
        }
    }
}
