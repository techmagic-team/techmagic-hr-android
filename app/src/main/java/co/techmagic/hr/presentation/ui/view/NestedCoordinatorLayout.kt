package co.techmagic.hr.presentation.ui.view

import android.content.Context
import android.support.annotation.Nullable
import android.support.v4.view.NestedScrollingChildHelper
import android.support.v4.view.NestedScrollingChild2
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View


class NestedScrollCoordinatorLayout : CoordinatorLayout, NestedScrollingChild2 {

    private var mChildHelper: NestedScrollingChildHelper? = null

    constructor(context: Context) : super(context) {
        mChildHelper = NestedScrollingChildHelper(this)
        isNestedScrollingEnabled = true
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mChildHelper = NestedScrollingChildHelper(this)
        isNestedScrollingEnabled = true
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mChildHelper = NestedScrollingChildHelper(this)
        isNestedScrollingEnabled = true
    }

    override fun isNestedScrollingEnabled(): Boolean {
        return mChildHelper!!.isNestedScrollingEnabled
    }

    override fun setNestedScrollingEnabled(enabled: Boolean) {
        mChildHelper!!.isNestedScrollingEnabled = enabled
    }

    override fun hasNestedScrollingParent(): Boolean {
        return mChildHelper!!.hasNestedScrollingParent()
    }

    override fun hasNestedScrollingParent(type: Int): Boolean {
        return mChildHelper!!.hasNestedScrollingParent(type)
    }

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        val superResult = super.onStartNestedScroll(child, target, axes, type)
        return startNestedScroll(axes, type) || superResult
    }

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        val superResult = super.onStartNestedScroll(child, target, nestedScrollAxes)
        return startNestedScroll(nestedScrollAxes) || superResult
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        dispatchNestedPreScroll(dx, dy, consumed, null)
        if (consumed[1] == 0) {
            super.onNestedPreScroll(target, dx, dy, consumed, type)
        }
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        dispatchNestedPreScroll(dx, dy, consumed, null)
        if (consumed[1] == 0) {
            super.onNestedPreScroll(target, dx, dy, consumed)
        }
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, null, type)
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, null)
    }

    override     fun onStopNestedScroll(target: View, type: Int) {
        super.onStopNestedScroll(target, type)
        stopNestedScroll(type)
    }

    override   fun onStopNestedScroll(target: View) {
        super.onStopNestedScroll(target)
        stopNestedScroll()
    }

    override   fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        val superResult = super.onNestedPreFling(target, velocityX, velocityY)
        return dispatchNestedPreFling(velocityX, velocityY) || superResult
    }

    override     fun onNestedFling(target: View, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        val superResult = super.onNestedFling(target, velocityX, velocityY, consumed)
        return dispatchNestedFling(velocityX, velocityY, consumed) || superResult
    }

    override fun startNestedScroll(axes: Int, type: Int): Boolean {
        return mChildHelper!!.startNestedScroll(axes, type)
    }

    override fun startNestedScroll(axes: Int): Boolean {
        return mChildHelper!!.startNestedScroll(axes)
    }

    override fun stopNestedScroll() {
        mChildHelper!!.stopNestedScroll()
    }

    override fun stopNestedScroll(type: Int) {
        mChildHelper!!.stopNestedScroll(type)
    }

    override fun dispatchNestedScroll(dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, @Nullable offsetInWindow: IntArray?, type: Int): Boolean {
        return mChildHelper!!.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type)
    }

    override fun dispatchNestedScroll(dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, @Nullable offsetInWindow: IntArray?): Boolean {
        return mChildHelper!!.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow)
    }

    override fun dispatchNestedPreScroll(dx: Int, dy: Int, @Nullable consumed: IntArray?, @Nullable offsetInWindow: IntArray?): Boolean {
        return mChildHelper!!.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow)
    }

    override fun dispatchNestedPreScroll(dx: Int, dy: Int, @Nullable consumed: IntArray?, @Nullable offsetInWindow: IntArray?, type: Int): Boolean {
        return mChildHelper!!.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type)
    }

    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
        return mChildHelper!!.dispatchNestedPreFling(velocityX, velocityY)
    }

    override fun dispatchNestedFling(velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        return mChildHelper!!.dispatchNestedFling(velocityX, velocityY, consumed)
    }
}