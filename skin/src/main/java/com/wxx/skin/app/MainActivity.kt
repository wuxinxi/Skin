package com.wxx.skin.app

import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.wxx.skin.SkinManage
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        change.setOnClickListener {
            Toast.makeText(applicationContext,"change",Toast.LENGTH_SHORT).show()
            val path = Environment.getExternalStorageDirectory().toString() + File.separator + "skin.apk"
            SkinManage.instance.loadLocalSkin(path)
        }

        reset.setOnClickListener {
            Toast.makeText(applicationContext,"reset",Toast.LENGTH_SHORT).show()
            SkinManage.instance.loadLocalSkin("")
        }

    }


}
