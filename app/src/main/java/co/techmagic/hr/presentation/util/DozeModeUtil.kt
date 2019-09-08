package co.techmagic.hr.presentation.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings

fun turnOffBatteryOptimization(context: Context) {
    val packageName = context.packageName
    if (!isIgnoreBatteryOptimizationTurnedOn(context)) {
        context.startActivity(Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, Uri.parse("package:" + packageName)))
    }
}

fun isIgnoreBatteryOptimizationTurnedOn(context: Context): Boolean {
    val packageName = context.packageName
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        return pm.isIgnoringBatteryOptimizations(packageName)
    }
    return false
}