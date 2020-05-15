package com.wxx.skin.utils

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Typeface
import com.wxx.skin.SkinAttributeManage

/**
 * @author ：wuxinxi on 2020/5/13 .
 * @packages ：com.wxx.skin.utils .
 * TODO:资源管理器
 */
class SkinResources private constructor() {

    private var mSkinResources: Resources? = null
    private lateinit var mAppResources: Resources
    private var mSkinPkgName: String? = null
    private var isDefaultSkin = true

    companion object {
        val instance: SkinResources by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            SkinResources()
        }
    }

    fun init(context: Context) {
        mAppResources = context.resources
    }

    fun reset() {
        checkNull()
        mSkinResources = null
        mSkinPkgName = null
        isDefaultSkin = true

    }

    fun applySkin(resources: Resources?, pkgName: String?) {
        checkNull()
        mSkinResources = resources
        mSkinPkgName = pkgName
        isDefaultSkin = pkgName.isNullOrEmpty() || resources == null
    }


    fun getTypefaceByActivity(activity: Activity): Typeface {
        val typefaceId = parseResId(activity, SkinAttributeManage.TYPEfACE_ATTRS)[0]
        return getTypeface(typefaceId)
    }


    fun parseResId(context: Context, attrs: IntArray): IntArray {
        val ints = IntArray(attrs.size)
        val typeArray = context.obtainStyledAttributes(attrs)
        for (i in 0 until attrs.size) {
            ints[i] = typeArray.getResourceId(i, 0)
        }
        typeArray.recycle()
        return ints
    }

    /**
     * 获取资源标识符即资源ID,如果是0表示无效资源
     */
    private fun getIdentifier(resId: Int): Int {
        if (isDefaultSkin) {
            return resId
        }

        //比如当前获取的是颜色R.color.colorPrimaryDark
        //resName=color  The name of the desired resource.
        //resType=colorPrimaryDark  Optional default resource type to find, if "type/" is
        //                          not included in the name.  Can be null to require an
        //                          explicit type.
        val resName = mAppResources.getResourceEntryName(resId)
        val resType = mAppResources.getResourceTypeName(resId)
        return mSkinResources?.getIdentifier(resName, resType, mSkinPkgName) ?: 0

    }

    fun getColor(resId: Int): Int {
        checkNull()
        if (isDefaultSkin) {
            return mAppResources.getColor(resId)
        }
        //从皮肤包中加载
        val skinId = getIdentifier(resId)
        //皮肤包中不存在从本地加载
        if (skinId == 0) {
            return mAppResources.getColor(resId)
        }
        return mSkinResources?.getColor(skinId) ?: mAppResources.getColor(resId)
    }

    fun getColorStateList(resId: Int): ColorStateList {
        checkNull()
        if (isDefaultSkin) {
            return mAppResources.getColorStateList(resId)
        }
        val skinId = getIdentifier(resId)
        if (skinId == 0) {
            return mAppResources.getColorStateList(resId)
        }
        return mSkinResources?.getColorStateList(resId) ?: mAppResources.getColorStateList(resId)
    }


    fun getBackground(resId: Int): Any {
        checkNull()
        val typeName = mAppResources.getResourceTypeName(resId)
        if (typeName == "color") {
            return getColor(resId)
        }
        return getDrawable(resId)
    }

    fun getDrawable(resId: Int): Any {
        checkNull()
        if (isDefaultSkin) {
            return mAppResources.getDrawable(resId)
        }
        val skinId = getIdentifier(resId)
        if (skinId == 0) {
            return mAppResources.getDrawable(resId)
        }
        return mSkinResources?.getDrawable(skinId) ?: mAppResources.getDrawable(resId)
    }


    fun getTypeface(typefaceId: Int): Typeface {
        //获取路径：font/xxx.ttf
        val typefacePath = getString(typefaceId)
        if (typefacePath.isNullOrEmpty()) {
            return Typeface.DEFAULT
        }
        try {
            if (isDefaultSkin) {
                return Typeface.createFromAsset(mAppResources.assets, typefacePath)
            }
            return Typeface.createFromAsset(mSkinResources?.assets ?: mAppResources.assets, typefacePath)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Typeface.DEFAULT
    }

    private fun getString(typeFaceId: Int): String? {
        checkNull()
        try {
            if (isDefaultSkin) {
                return mAppResources.getString(typeFaceId)
            }

            val skinId = getIdentifier(typeFaceId)
            if (skinId == 0) {
                return mAppResources.getString(typeFaceId)
            }
            return mSkinResources?.getString(skinId) ?: return mAppResources.getString(typeFaceId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun checkNull() {
        if (!this::mAppResources.isInitialized) {
            throw ExceptionInInitializerError("SkinResource Not initialized")
        }
    }

}