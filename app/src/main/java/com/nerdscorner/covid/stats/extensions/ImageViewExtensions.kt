package com.nerdscorner.covid.stats.extensions

import android.animation.Animator
import android.view.animation.AccelerateDecelerateInterpolator
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
