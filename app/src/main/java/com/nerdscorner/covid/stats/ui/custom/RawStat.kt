package com.nerdscorner.covid.stats.ui.custom

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.nerdscorner.covid.stats.R
import com.nerdscorner.covid.stats.domain.Stat

class RawStat @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val statValue: TextView
    private val chartIcon: ImageView
    var itemSelected = false
    var stat: Stat? = null
        set(value) {
            field = value
            chartIcon.visibility = if (value == null) {
                View.INVISIBLE
            } else {
                View.VISIBLE
            }
        }

    init {
        val viewToAdd = LayoutInflater.from(context).inflate(R.layout.raw_stat, null, false)
        viewToAdd.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
            addRule(CENTER_IN_PARENT)
        }
        addView(viewToAdd)

        setBackgroundResource(R.color.raw_stat_background)

        val a = context.obtainStyledAttributes(attrs, R.styleable.RawStat, 0, 0)
        val title = a.getString(R.styleable.RawStat_stat_Title)
        a.recycle()

        findViewById<TextView>(R.id.stat_title).text = title
        chartIcon = findViewById(R.id.chart_icon)
        chartIcon.visibility = View.INVISIBLE
        statValue = findViewById(R.id.stat_value)
    }

    fun setValue(value: String) {
        statValue.text = value
    }
    
    fun updateSelectedState() {
        chartIcon.setColorFilter(
            ContextCompat.getColor(
                context,
                if (itemSelected) {
                    R.color.raw_stat_buttons_selected_color
                } else {
                    R.color.raw_stat_buttons_unselected_color
                }
            ),
            PorterDuff.Mode.SRC_IN
        )
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        super.setOnClickListener {
            itemSelected = itemSelected.not()
            updateSelectedState()
            listener?.onClick(this)
        }
    }
}
