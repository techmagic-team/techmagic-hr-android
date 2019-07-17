package co.techmagic.hr.presentation.ui.view

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View
import co.techmagic.hr.presentation.util.UiUtil

class BottomNavigationFabBehavior(context: Context?, attrs: AttributeSet?) : VerticalScrollingBehavior<View>(context, attrs) {

    companion object {

        const val ANIMATION_TRANSLATION_SHOW_Y_DP = 0f
        const val ANIMATION_DURATION = 100L
    }

    private var isFabHiding = false
    private var isFabShowing = false

    private var hidden = false

    override fun onNestedVerticalOverScroll(coordinatorLayout: CoordinatorLayout?, child: View?, direction: Int, currentOverScroll: Int, totalOverScroll: Int) {

    }

    override fun onDirectionNestedPreScroll(coordinatorLayout: CoordinatorLayout?, child: View?, target: View?, dx: Int, dy: Int, consumed: IntArray?, scrollDirection: Int) {
        handleDirection(child, scrollDirection)
    }

    override fun onNestedDirectionFling(coordinatorLayout: CoordinatorLayout?, child: View?, target: View?, velocityX: Float, velocityY: Float, scrollDirection: Int): Boolean {
        handleDirection(child, scrollDirection)
        return true
    }

    private fun handleDirection(child: View?, @ScrollDirection scrollDirection: Int) {
        if (scrollDirection == VerticalScrollingBehavior.ScrollDirection.SCROLL_DIRECTION_DOWN && hidden) {
            hidden = false
            showView(child)
        } else if (scrollDirection == VerticalScrollingBehavior.ScrollDirection.SCROLL_DIRECTION_UP && !hidden) {
            hidden = true
            hideView(child)
        }
    }

    private fun showView(child: View?) {
        if (!isFabShowing) {
            translateByY(child, ANIMATION_TRANSLATION_SHOW_Y_DP)
            isFabShowing = true
            isFabHiding = false
        }
    }

    private fun hideView(child: View?) {
        child ?: return
        if (!isFabHiding) {
            translateByY(child, UiUtil.getBottomNavigationHeigth(child.context).toFloat())
            isFabHiding = true
            isFabShowing = false
        }
    }

    private fun translateByY(child: View?, translationY: Float) {
        with(child) {
            this ?: return
            animate()
                    .translationY(translationY)
                    .setDuration(ANIMATION_DURATION)
                    .start()
        }
    }

}