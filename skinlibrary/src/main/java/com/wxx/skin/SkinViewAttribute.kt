package com.wxx.skin

import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import com.wxx.skin.utils.SkinResources
import com.wxx.skin.utils.SkinViewSupport

/**
 * @author ：wuxinxi on 2020/5/13 .
 * @packages ：com.wxx.skin .
 * TODO:保存View以及view的属性集合
 */
class SkinView(private val view: View, private val skinAttributes: List<SkinAttribute>) {

    fun applySkin(typeface: Typeface?) {
        applyTypeface(typeface)
        applyViewSupport()
        for (skinAttribute in skinAttributes) {
            when (skinAttribute.attributeName) {
                SkinAttributeManage.background -> {
                    val background = SkinResources.instance.getBackground(skinAttribute.resId)
                    if (background is Int) {
                        view.setBackgroundColor(background)
                    } else {
                        ViewCompat.setBackground(view, background as Drawable)
                    }
                }

                SkinAttributeManage.src -> {
                    val background = SkinResources.instance.getBackground(skinAttribute.resId)
                    if (background is Int) {
                        (view as ImageView).setImageDrawable(ColorDrawable(background))
                    } else {
                        (view as ImageView).setImageDrawable(background as Drawable)
                    }
                }

                SkinAttributeManage.textColor -> {
                    (view as TextView).setTextColor(SkinResources.instance.getColorStateList(skinAttribute.resId))
                }

                SkinAttributeManage.skinTypeface -> {
                    (SkinResources.instance.getTypeface(skinAttribute.resId))
                }
            }
        }
    }

    /**
     * 自定义View换肤
     */
    private fun applyViewSupport() {
        if (view is SkinViewSupport) {
            view.applySkin()
        }
    }

    /**
     * 设置当前字体
     */
    private fun applyTypeface(typeface: Typeface?) {
        if (typeface == null) {
            return
        }
        //如果是TextView或者继承自TextView的控件
        if (view is TextView) {
            view.typeface = typeface
        }

    }

}


/**
 * @author ：wuxinxi on 2020/5/13 .
 * @packages ：com.wxx.skin .
 * TODO:保存View属性
 */
class SkinAttribute(val attributeName: String, val resId: Int)