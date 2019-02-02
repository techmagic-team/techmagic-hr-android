package co.techmagic.hr.presentation.util

import android.content.res.Resources

object UiUtil {
    fun Px2Dp(px: Int): Float {
        val metrics = Resources.getSystem().displayMetrics
        val dp = px / (metrics.densityDpi / 160f)
        return Math.round(dp).toFloat()
    }

    fun Dp2Px(dp: Float): Int {
        val metrics = Resources.getSystem().displayMetrics
        val px = dp * (metrics.densityDpi / 160f)
        return Math.round(px)
    }
}
