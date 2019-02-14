package co.techmagic.hr.presentation.ui.view

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View
import co.techmagic.hr.presentation.util.UiUtil
import java.lang.Exception

class BottomNavigationFabBehavior(context: Context?, attrs: AttributeSet?) : CoordinatorLayout.Behavior<View>(context, attrs) {
    
    companion object {
        const val ANIMATION_TRANSLATION_HIDE_Y_DP = 100f
        const val ANIMATION_TRANSLATION_SHOW_Y_DP = 0f
        const val ANIMATION_DURATION = 100L
        const val ANIMATION_ALPHA_HIDE = 0f
        const val ANIMATION_ALPHA_SHOW = 1f
    }
    
    private var isFabHiding = false
    private var isFabShowing = false

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: View, directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        return true
    }

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
    try {
        if (dy > 0) {
            if (!isFabHiding) {
                child
                        .animate()
                        .translationY(UiUtil.dp2Px(ANIMATION_TRANSLATION_HIDE_Y_DP).toFloat())
                        .alpha(ANIMATION_ALPHA_HIDE)
                        .setDuration(ANIMATION_DURATION)
                        .start()
                isFabHiding = true
                isFabShowing = false
            }
        } else {
            if (!isFabShowing) {
                child
                        .animate()
                        .translationY(UiUtil.dp2Px(ANIMATION_TRANSLATION_SHOW_Y_DP).toFloat())
                        .alpha(ANIMATION_ALPHA_SHOW)
                        .setDuration(ANIMATION_DURATION)
                        .start()
                isFabShowing = true
                isFabHiding = false
            }
        }
    } catch (ex : Exception){
        ex.printStackTrace()
    }


    }
}