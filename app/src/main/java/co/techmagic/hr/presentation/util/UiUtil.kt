package co.techmagic.hr.presentation.util

import android.content.Context
import android.content.res.Resources

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
}
