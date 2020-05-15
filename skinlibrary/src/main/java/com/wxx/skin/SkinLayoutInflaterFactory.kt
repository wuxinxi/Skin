package com.wxx.skin

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.collection.ArrayMap
import com.wxx.skin.utils.SkinResources
import java.lang.reflect.Constructor
import java.util.*

/**
 * @author ：wuxinxi on 2020/5/13 .
 * @packages ：com.wxx.skin .
 * TODO:创建view的自定义工厂
 */
class SkinLayoutInflaterFactory(private val activity: Activity, typeface: Typeface) : LayoutInflater.Factory2,
    Observer {
    override fun update(o: Observable?, arg: Any?) {
        //应用状态栏
        skinAttributeManage.applyStatusBar(activity)
        val typeface = SkinResources.instance.getTypefaceByActivity(activity)
        //设置字体
        skinAttributeManage.setTypeface(typeface)
        //应用皮肤
        skinAttributeManage.applySkin()
    }

    //构造函数签名
    private val sConstructorSignature = arrayOf(Context::class.java, AttributeSet::class.java)
    //前缀列表
    private val sClassPrefixList = arrayOf("android.widget.", "android.view.", "android.webkit.")
    //构造函数缓存字典
    private val sConstructorMap = ArrayMap<String, Constructor<out View>>()
    //构造函数参数
    private val mConstructorArgs = arrayOfNulls<Any>(2)

    private val skinAttributeManage = SkinAttributeManage(typeface)

    private val TAG = "wuxinxi"


    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        Log.d(TAG, "onCreateView: name=$name")
        val view = createViewFromTag(context, name, attrs)

        if (view != null) {
            skinAttributeManage.load(view, attrs)
        }
        return view
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return null
    }


    /**
     * @param name 控件名
     */
    private fun createViewFromTag(context: Context, name: String, attrs: AttributeSet): View? {
        var newName = name
        if (newName == "view") {
            newName = attrs.getAttributeValue(null, "class")
        }
        try {
            mConstructorArgs[0] = context
            mConstructorArgs[1] = attrs
            //系统控件
            if (-1 == name.indexOf('.')) {
                for (i in sClassPrefixList.indices) {
                    val view = createViewByPrefix(context, newName, sClassPrefixList[i])
                    if (view != null) {
                        Log.d("wuxinxi", "createViewFromTag: pack=${sClassPrefixList[i]}  name=$name")
                        return view
                    }
                }
                return null
            } else {
                //自定义控件
                return createViewByPrefix(context, newName, null)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            mConstructorArgs[0] = null
            mConstructorArgs[1] = null
        }
    }

    /**
     * @param name 控件名
     * @param prefix 前缀
     */
    private fun createViewByPrefix(context: Context, name: String, prefix: String?): View? {
        var constructor = sConstructorMap[name]
        try {
            if (constructor == null) {
                val clazz = Class.forName(if (prefix != null) (prefix + name) else name, false, context.classLoader)
                    .asSubclass(View::class.java)
                //不固定参数 *
                constructor = clazz.getConstructor(*sConstructorSignature)
                sConstructorMap[name] = constructor
            }
            constructor.isAccessible = true
            return constructor.newInstance(*mConstructorArgs)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}