package com.nerdscorner.covid.stats.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.nerdscorner.covid.stats.R
import com.nerdscorner.covid.stats.domain.Stat


class RawStat @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val statTitle: TextView
    private val chartIcon: ImageView
    var itemSelected = false
    var stat: Stat? = null

    init {
        val viewToAdd = LayoutInflater.from(context).inflate(R.layout.raw_stat, null, false)
        viewToAdd.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
            addRule(CENTER_IN_PARENT)
        }
        addView(viewToAdd)

        setBackgroundResource(R.color.raw_stat_background)

        val a = context.obtainStyledAttributes(attrs, R.styleable.RawStat, 0, 0)
        val title = a.getString(R.styleable.RawStat_stat_Title)
        val value = a.getString(R.styleable.RawStat_stat_Value)
        a.recycle()

        findViewById<TextView>(R.id.stat_title).text = title
        chartIcon = findViewById(R.id.chart_icon)
        statTitle = findViewById(R.id.stat_value)
        statTitle.text = value
    }

    fun setValue(value: String) {
        statTitle.text = value
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        super.setOnClickListener {
            itemSelected  = itemSelected.not()
            chartIcon.visibility = if (itemSelected) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
            listener?.onClick(this)
        }
    }
}
