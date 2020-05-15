package com.wxx.skin

import android.app.Activity
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.wxx.skin.utils.SkinResources
import com.wxx.skin.utils.SkinViewSupport

/**
 * @author ：wuxinxi on 2020/5/13 .
 * @packages ：com.wxx.skin .
 * TODO:View属性处理类
 */
class SkinAttributeManage(private var typeface: Typeface) {
    private val mAttributes = mutableListOf<String>()

    companion object {
        const val background = "background"
        const val src = "src"
        const val textColor = "textColor"
        const val drawableLeft = "drawableLeft"
        const val drawableTop = "drawableTop"
        const val drawableRight = "drawableRight"
        const val drawableBottom = "drawableBottom"
        //自定义字体属性
        const val skinTypeface = "skinTypeface"

        val APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS = intArrayOf(androidx.appcompat.R.attr.colorPrimaryDark)

        val TYPEfACE_ATTRS = intArrayOf(R.attr.skinTypeface)
    }

    init {
        //可能需要处理的属性
        mAttributes.add(background)
        mAttributes.add(src)
        mAttributes.add(textColor)
        mAttributes.add(drawableLeft)
        mAttributes.add(drawableTop)
        mAttributes.add(drawableRight)
        mAttributes.add(drawableBottom)
        mAttributes.add(skinTypeface)
    }

    private val mSkinViews = mutableListOf<SkinView>()

    fun load(view: View, attributeSet: AttributeSet) {
        val skinAttributes = mutableListOf<SkinAttribute>()
        val count = attributeSet.attributeCount
        for (i in 0 until count) {
            //属性名
            val attributeName = attributeSet.getAttributeName(i)
            //如果是我们需要的属性
            if (attributeName in mAttributes) {
                val attributeValue = attributeSet.getAttributeValue(i)
                var resId: Int
                val attrId = attributeValue.substring(1).toInt()
                //如果是系统属性值
                resId = if (attributeValue.startsWith("?")) {
                    SkinResources.instance.parseResId(view.context, intArrayOf(attrId))[0]
                } else {
                    attrId
                }
                if (resId != 0) {
                    //保存当前属性名对应的属性信息
                    val skinAttribute = SkinAttribute(attributeName, resId)
                    skinAttributes.add(skinAttribute)
                }
            }
        }
        //属性不为空、继承自或者是TextView、实现了SkinViewSupport
        if (skinAttributes.isNotEmpty() || view is TextView || view is SkinViewSupport) {
            //保存当前view的信息
            val skinView = SkinView(view, skinAttributes)
            skinView.applySkin(typeface)
            mSkinViews.add(skinView)
        }
    }


    /**
     * 应用皮肤
     */
    fun applySkin() {
        for (skinView in mSkinViews) {
            skinView.applySkin(typeface)
        }
    }

    /**
     * 应用状态栏
     */
    fun applyStatusBar(activity: Activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return
        }
        val statusBarColorAttrs = intArrayOf(android.R.attr.statusBarColor, android.R.attr.navigationBarColor)
        val statusBarColorResId = SkinResources.instance.parseResId(activity, statusBarColorAttrs)
        //如果换肤资源中配置了statusBarColor(也就是配置了colorPrimaryDark)
        if (statusBarColorResId[0] != 0) {
            //color资源中的colorPrimaryDark
            activity.window.statusBarColor = SkinResources.instance.getColor(statusBarColorResId[0])
        } else {
            val colorPrimaryDarkResId =
                SkinResources.instance.parseResId(activity, APPCOMPAT_COLOR_PRIMARY_DARK_ATTRS)[0]
            if (colorPrimaryDarkResId != 0) {
                activity.window.statusBarColor = SkinResources.instance.getColor(colorPrimaryDarkResId)
            }
        }

        if (statusBarColorResId[1] != 0) {
            //设置底部虚拟导航的颜色
            activity.window.navigationBarColor = SkinResources.instance.getColor(statusBarColorResId[1])
        }
    }


    fun setTypeface(typeface: Typeface) {
        this.typeface = typeface
    }
}