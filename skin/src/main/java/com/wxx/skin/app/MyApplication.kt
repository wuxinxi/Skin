package com.wxx.skin.app

import android.app.Application
import com.wxx.skin.SkinManage

/**
 * @author ：wuxinxi on 2020/5/13 .
 * @packages ：com.wxx.skin .
 * TODO:一句话描述
 */
class MyApplication:Application() {
    override fun onCreate() {
        super.onCreate()

        SkinManage.instance.init(this)
    }
}