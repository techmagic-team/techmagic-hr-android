package co.techmagic.hr.presentation.util

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

object UiUtil {
    fun px2Dp(px: Int): Float {
        val metrics = Resources.getSystem().displayMetrics
        val dp = px / (metrics.densityDpi / 160f)
        return Math.round(dp).toFloat()
    }

    fun dp2Px(dp: Float): Int {
        val metrics = Resources.getSystem().displayMetrics
        val px = dp * (metrics.densityDpi / 160f)
        return Math.round(px)
    }

    fun getBottomNavigationHeigth(context: Context): Int {
        val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return context.resources.getDimensionPixelSize(resourceId)
    }

    @JvmStatic
    fun setupHideKeyboardOnTouchRecursively(view: View?) {
        if (view == null) {
            return
        }
        if (view !is EditText) {
            val existTouchListener = getOnTouchListener(view)
            val existClickListener = getOnClickListener(view)
            if (existTouchListener != null) {
                view.setOnTouchListener { v, event ->
                    val onTouch = existTouchListener.onTouch(v, event)
                    hideKeyboard(v)
                    onTouch
                }
            } else if (existClickListener != null) {
                view.setOnClickListener { v ->
                    existClickListener!!.onClick(v)
                    hideKeyboard(v)
                }
            } else {
                view.setOnTouchListener { v, event ->
                    hideKeyboard(v)
                    false
                }
            }
        }

        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupHideKeyboardOnTouchRecursively(innerView)
            }
        }

    }

    private fun getOnTouchListener(view: View): View.OnTouchListener? {
        var retrievedListener: View.OnTouchListener? = null
        val viewStr = "android.view.View"
        val lInfoStr = "android.view.View\$ListenerInfo"
        try {

            val listenerField = Class.forName(viewStr).getDeclaredField("mListenerInfo")
            var listenerInfo: Any? = null

            if (listenerField != null) {
                listenerField.isAccessible = true
                listenerInfo = listenerField.get(view)
            }

            val clickListenerField = Class.forName(lInfoStr).getDeclaredField("mOnTouchListener")

            if (clickListenerField != null && listenerInfo != null) {
                clickListenerField.isAccessible = true
                retrievedListener = clickListenerField.get(listenerInfo) as View.OnTouchListener
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return retrievedListener
    }

    private fun getOnClickListener(view: View): View.OnClickListener? {
        var retrievedListener: View.OnClickListener? = null
        val viewStr = "android.view.View"
        val lInfoStr = "android.view.View\$ListenerInfo"
        try {

            val listenerField = Class.forName(viewStr).getDeclaredField("mListenerInfo")
            var listenerInfo: Any? = null

            if (listenerField != null) {
                listenerField.isAccessible = true
                listenerInfo = listenerField.get(view)
            }

            val clickListenerField = Class.forName(lInfoStr).getDeclaredField("mOnClickListener")

            if (clickListenerField != null && listenerInfo != null) {
                clickListenerField.isAccessible = true
                retrievedListener = clickListenerField.get(listenerInfo) as View.OnClickListener
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return retrievedListener
    }

    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}
