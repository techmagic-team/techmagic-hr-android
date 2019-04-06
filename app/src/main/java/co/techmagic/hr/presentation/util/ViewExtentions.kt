package co.techmagic.hr.presentation.util

import android.graphics.drawable.GradientDrawable
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.view.View
import co.techmagic.hr.R
import android.widget.ImageView


fun ImageView.setImageDrawableRes(@DrawableRes color: Int) {
    setImageDrawable(ContextCompat.getDrawable(context, color))
}



fun View.changeRippleShapeStrokeColor(@DimenRes strokeWidth : Int, @ColorRes colorRes: Int) {
    val drawable = background as GradientDrawable
    drawable.setStroke(
            resources.getDimensionPixelOffset(strokeWidth),
            ContextCompat.getColor(context, colorRes)
    )
}
