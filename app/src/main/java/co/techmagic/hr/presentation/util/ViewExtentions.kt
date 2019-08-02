package co.techmagic.hr.presentation.util

import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView


fun ImageView.setImageDrawableRes(@DrawableRes color: Int) {
    setImageDrawable(ContextCompat.getDrawable(context, color))
}


fun View.changeShapeStrokeColor(@DimenRes strokeWidth: Int, @ColorRes colorRes: Int) {
    val drawable: GradientDrawable = when (background) {
        is GradientDrawable -> background.mutate() as GradientDrawable
        is RippleDrawable -> (background.mutate() as RippleDrawable).getDrawable(0) as GradientDrawable
        else -> throw IllegalArgumentException()
    }

    drawable.setStroke(
            resources.getDimensionPixelOffset(strokeWidth),
            ContextCompat.getColor(context, colorRes)
    )
}