package com.nerdscorner.covid.stats.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import android.widget.TextView
import com.nerdscorner.covid.stats.R


class RawStat @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    
    private val statTitle: TextView
    
    init {
        LayoutInflater.from(context).inflate(R.layout.raw_stat, this, true)
        
        val a = context.obtainStyledAttributes(attrs, R.styleable.RawStat, 0, 0)
        val title = a.getString(R.styleable.RawStat_stat_Title)
        val value = a.getString(R.styleable.RawStat_stat_Value)
        a.recycle()

        findViewById<TextView>(R.id.stat_title).text = title
        statTitle = findViewById(R.id.stat_value)
        statTitle.text = value
    }
    
    fun setValue(value: String) {
        statTitle.text = value
    }
}
