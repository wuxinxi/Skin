package com.wxx.skin.utils

import android.os.Environment
import java.io.File

/**
 * @author ：wuxinxi on 2020/5/13 .
 * @packages ：com.wxx.skin.utils .
 * TODO:一句话描述
 */
class SkinPreference private constructor() {
    companion object {
        val instance: SkinPreference by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            SkinPreference()
        }
    }

//    lateinit var sp: SharedPreferences
//    fun init(context: Context) {
//        sp = context.getSharedPreferences("skins", Context.MODE_PRIVATE)
//    }

    fun getSkinPath(): String {
        return Environment.getExternalStorageDirectory().toString() + File.separator + "skin.apk"
    }
}