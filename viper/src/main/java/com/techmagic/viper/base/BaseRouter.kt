package com.techmagic.viper.base

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.techmagic.viper.Router

class BaseRouter<T : FragmentActivity>(protected var activity: T) : Router {

    protected fun addFragment(@IdRes containerId: Int, fragment: Fragment, addToBackStack: Boolean) {
        val fragmentManager = activity.supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
                .hide(fragmentManager.findFragmentById(containerId))
                .add(containerId, fragment, fragment.javaClass.canonicalName)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    protected fun replaceFragment(@IdRes containerId: Int, fragment: Fragment, addToBackStack: Boolean) {
        val transaction = activity.supportFragmentManager.beginTransaction()
                .replace(containerId, fragment, fragment.javaClass.canonicalName)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }
}