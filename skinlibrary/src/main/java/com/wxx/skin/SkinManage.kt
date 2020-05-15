package com.wxx.skin

import android.app.Application
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.util.Log
import com.wxx.skin.utils.SkinResources
import java.io.File
import java.util.*

/**
 * @author ：wuxinxi on 2020/5/13 .
 * @packages ：com.wxx.skin .
 * TODO:换肤初始化
 */
class SkinManage private constructor() : Observable() {

    private lateinit var application: Application

    companion object {
        val instance: SkinManage by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            SkinManage()
        }
    }

    fun init(application: Application) {
        this.application = application
        //初始话资源管理器
        SkinResources.instance.init(application)
        this.application.registerActivityLifecycleCallbacks(SkinActivityLifecycleCallbacks())

        loadLocalSkin("")
    }


    fun loadLocalSkin(path: String) {
        val skinFile = File(path)
        if (!skinFile.exists()) {
            SkinResources.instance.reset()
            setChanged()
            notifyObservers()
        } else {
            try {
                val assetManager = AssetManager::class.java.newInstance()
                val addAssetPath = assetManager.javaClass.getMethod("addAssetPath", String::class.java)
                addAssetPath.invoke(assetManager, path)

                val appResources = this.application.resources
                val skinResources = Resources(assetManager, appResources.displayMetrics, appResources.configuration)

                val packageManager = this.application.packageManager
                val packageArchiveInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES)
                val packageName = packageArchiveInfo?.packageName
                SkinResources.instance.applySkin(skinResources, packageName)

                Log.d("wuxinxi", "loadLocalSkin: packageName=$packageName")
                setChanged()
                notifyObservers()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}
