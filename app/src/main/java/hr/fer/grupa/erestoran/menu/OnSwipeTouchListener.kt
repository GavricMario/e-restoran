package hr.fer.grupa.erestoran.menu

import android.content.Context
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import kotlin.math.abs

open class OnSwipeTouchListener(ctx: Context) : OnTouchListener {

    private val gestureDetector: GestureDetector

    init {
        gestureDetector = GestureDetector(ctx, GestureListener())
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    open fun onSwipeUp() {}

    open fun onSwipeDown() {}

    private inner class GestureListener : SimpleOnGestureListener() {

        private val SWIPE_DISTANCE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val distanceX = e2.x - e1.x
            val distanceY = e2.y - e1.y
            if (abs(distanceY) > abs(distanceX) && abs(distanceY) > SWIPE_DISTANCE_THRESHOLD
                    && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD
            ) {
                if (distanceY > 0)
                    onSwipeDown()
                else
                    onSwipeUp()
                return true
            }
            return false
        }

    }
}