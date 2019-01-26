package co.techmagic.hr.presentation.util

import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

fun TextView.setTextColorRes(@ColorRes color: Int) {
    setTextColor(ContextCompat.getColor(context, color))
}

fun ImageView.setImageDrawableRes(@DrawableRes color: Int) {
    setImageDrawable(ContextCompat.getDrawable(context, color))
}

fun ViewGroup.setBackgroundColorRes(@ColorRes color: Int) {
    setBackgroundColor(ContextCompat.getColor(context, color))
}

fun ViewGroup.setBackgroundDrawableRes(@DrawableRes drawable: Int) {
    background = ContextCompat.getDrawable(context, drawable)
}
