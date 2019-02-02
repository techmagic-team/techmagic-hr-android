package co.techmagic.hr.presentation.ui.view

import android.support.v7.widget.RecyclerView
import android.view.View
import co.techmagic.hr.presentation.util.UiUtil

class FabFillScrollListener(val fillOnScrollView: View) : RecyclerView.OnScrollListener() {
    companion object {
        const val ANIMATION_TRANSLATION_HIDE_Y_DP = 100f
        const val ANIMATION_TRANSLATION_SHOW_Y_DP = 0f
        const val ANIMATION_DURATION = 100L
        const val ANIMATION_ALPHA_HIDE = 0f
        const val ANIMATION_ALPHA_SHOW = 1f
    }

    private var isFabHiding = false
    private var isFabShowing = false

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        if (dy > 0) {
            if (!isFabHiding) {
                fillOnScrollView
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
                fillOnScrollView
                        .animate()
                        .translationY(UiUtil.dp2Px(ANIMATION_TRANSLATION_SHOW_Y_DP).toFloat())
                        .alpha(ANIMATION_ALPHA_SHOW)
                        .setDuration(ANIMATION_DURATION)
                        .start()
                isFabShowing = true
                isFabHiding = false
            }
        }
    }
}
