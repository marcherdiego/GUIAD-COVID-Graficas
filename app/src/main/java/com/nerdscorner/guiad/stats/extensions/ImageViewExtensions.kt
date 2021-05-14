package com.nerdscorner.guiad.stats.extensions

import android.animation.Animator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView

fun ImageView.rotateCounterClockwise(degrees: Float = 90f) {
    animate()
        .rotationBy(-degrees)
        .setInterpolator(AccelerateDecelerateInterpolator())
        .setDuration(800L)
        .setListener(object : BaseAnimatorListener() {
            override fun onAnimationEnd(animation: Animator?) {
                rotateClockwise()
            }
        })
}

fun ImageView.rotateClockwise(degrees: Float = 90f) {
    animate()
        .rotationBy(degrees)
        .setInterpolator(AccelerateDecelerateInterpolator())
        .setDuration(300L)
        .setListener(object : BaseAnimatorListener() {
            override fun onAnimationEnd(animation: Animator?) {
                postDelayed({ rotateCounterClockwise() }, 1000L)
            }
        })
}

fun View.animateScaleUp(duration: Long = 350L, scaleValue: Float = 0.3f, animationEndListener: () -> Unit = {}) {
    animate()
        .scaleXBy(scaleValue)
        .scaleYBy(scaleValue)
        .setInterpolator(AccelerateInterpolator())
        .setDuration(duration)
        .setListener(object : BaseAnimatorListener() {
            override fun onAnimationEnd(animation: Animator?) {
                animateRestoreScale(duration, -scaleValue, animationEndListener)
            }
        })
}

fun View.animateRestoreScale(duration: Long = 350L, scaleValue: Float = 0.3f, animationEndListener: () -> Unit = {}) {
    animate()
        .scaleXBy(scaleValue)
        .scaleYBy(scaleValue)
        .setInterpolator(DecelerateInterpolator())
        .setDuration(duration)
        .setListener(object : BaseAnimatorListener() {
            override fun onAnimationEnd(animation: Animator?) {
                animationEndListener()
            }
        })
}

private open class BaseAnimatorListener : Animator.AnimatorListener {
    override fun onAnimationStart(animation: Animator?) {
    }

    override fun onAnimationEnd(animation: Animator?) {
    }

    override fun onAnimationCancel(animation: Animator?) {
    }

    override fun onAnimationRepeat(animation: Animator?) {
    }
}
