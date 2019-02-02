package co.techmagic.hr.presentation.util

import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.widget.ImageView

fun ImageView.setImageDrawableRes(@DrawableRes color: Int) {
    setImageDrawable(ContextCompat.getDrawable(context, color))
}
