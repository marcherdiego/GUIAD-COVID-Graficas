package com.nerdscorner.guiad.stats.ui.custom

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.FragmentActivity
import com.nerdscorner.guiad.stats.ui.fragment.DatePickerFragment
import com.nerdscorner.guiad.stats.utils.DateUtils
import java.util.*

class DatePicker @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    AppCompatTextView(context, attrs, defStyleAttr), View.OnClickListener {

    init {
        setOnClickListener(this)
    }

    private var editable = true
    private var datePickedListener: ((date: Date) -> Unit)? = null
    private var date: Date? = null
    private var minDate: Date? = null
    private var maxDate: Date? = null

    override fun onClick(v: View) {
        if (editable) {
            val datePickerFragment = DatePickerFragment()
            datePickerFragment.setDatePickedListener { date ->
                setDate(date)
            }
            val arguments = Bundle()
            arguments.putSerializable(DatePickerFragment.DATE, date)
            arguments.putSerializable(DatePickerFragment.MIN_DATE, minDate)
            arguments.putSerializable(DatePickerFragment.MAX_DATE, maxDate)
            datePickerFragment.arguments = arguments
            datePickerFragment.show((context as FragmentActivity).supportFragmentManager, "datePicker")
        }
    }

    fun setDate(date: Date, manualUpdate: Boolean = false) {
        this.date = date
        text = DateUtils.formatDate(date)
        if (manualUpdate.not()) {
            datePickedListener?.invoke(date)
        }
    }

    fun setDatePickedListener(datePickedListener: ((date: Date) -> Unit)?) {
        this.datePickedListener = datePickedListener
    }

    fun setMinDate(minDate: Date?) {
        this.minDate = minDate
    }

    fun setMaxDate(maxDate: Date?) {
        this.maxDate = maxDate
    }
}
