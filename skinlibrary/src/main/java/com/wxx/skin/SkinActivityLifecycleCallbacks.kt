package com.wxx.skin

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import com.wxx.skin.utils.SkinResources

/**
 * @author ：wuxinxi on 2020/5/13 .
 * @packages ：com.wxx.skin .
 * TODO:监听所有Activity的生命周期
 */
class SkinActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
    private val factoryMap = HashMap<Activity, SkinLayoutInflaterFactory>()
    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        val factory = factoryMap.remove(activity)
        if (factory != null) {
            SkinManage.instance.deleteObserver(factory)
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        //使用自定义的Factory工厂
        val layoutInflater = activity.layoutInflater
        try {
            val mFactorySet = LayoutInflater::class.java.getDeclaredField("mFactorySet")
            mFactorySet.isAccessible = true
            //防止第二次设置factory报错
            mFactorySet.setBoolean(layoutInflater, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //加载字体
        val typeface = SkinResources.instance.getTypefaceByActivity(activity)
        val factory = SkinLayoutInflaterFactory(activity, typeface)
        layoutInflater.factory2 = factory
        SkinManage.instance.addObserver(factory)
        factoryMap[activity] = factory
    }

    override fun onActivityResumed(activity: Activity) {

    }

}