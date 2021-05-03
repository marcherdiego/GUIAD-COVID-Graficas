package com.nerdscorner.covid.stats.utils

import android.content.Context
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.nerdscorner.covid.stats.R

object ColorUtils {
    val graphColors = mutableListOf<@ColorInt Int>()

    fun init(context: Context) {
        graphColors.add(ContextCompat.getColor(context, R.color.graph1))
        graphColors.add(ContextCompat.getColor(context, R.color.graph2))
        graphColors.add(ContextCompat.getColor(context, R.color.graph3))
        graphColors.add(ContextCompat.getColor(context, R.color.graph4))
        graphColors.add(ContextCompat.getColor(context, R.color.graph5))
        graphColors.add(ContextCompat.getColor(context, R.color.graph6))
        graphColors.add(ContextCompat.getColor(context, R.color.graph7))
        graphColors.add(ContextCompat.getColor(context, R.color.graph8))
        graphColors.add(ContextCompat.getColor(context, R.color.graph9))
        graphColors.add(ContextCompat.getColor(context, R.color.graph10))
        graphColors.add(ContextCompat.getColor(context, R.color.graph11))
        graphColors.add(ContextCompat.getColor(context, R.color.graph12))
        graphColors.add(ContextCompat.getColor(context, R.color.graph13))
        graphColors.add(ContextCompat.getColor(context, R.color.graph14))
        graphColors.add(ContextCompat.getColor(context, R.color.graph15))
        graphColors.add(ContextCompat.getColor(context, R.color.graph16))
        graphColors.add(ContextCompat.getColor(context, R.color.graph17))
        graphColors.add(ContextCompat.getColor(context, R.color.graph18))
    }
}