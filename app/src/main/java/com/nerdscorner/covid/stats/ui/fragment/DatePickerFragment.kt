package com.nerdscorner.covid.stats.ui.fragment

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment : DialogFragment(), OnDateSetListener {
    private var datePickedListener: ((date: Date) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val date = arguments?.getSerializable(DATE) as? Date?
        val minDate = arguments?.getSerializable(MIN_DATE) as? Date?
        val maxDate = arguments?.getSerializable(MAX_DATE) as? Date?
        val calendar = Calendar.getInstance()
        if (date != null) {
            calendar.time = date
        }
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]
        val dialog = DatePickerDialog(activity!!, this, year, month, day)
        dialog.datePicker.maxDate = Date().time
        if (maxDate != null) {
            dialog.datePicker.maxDate = maxDate.time
        }
        if (minDate != null) {
            dialog.datePicker.minDate = minDate.time
        }
        return dialog
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        val calendar: Calendar = GregorianCalendar()
        calendar[year, month] = day
        datePickedListener?.invoke(calendar.time)
    }

    fun setDatePickedListener(datePickedListener: ((date: Date) -> Unit)?) {
        this.datePickedListener = datePickedListener
    }

    companion object {
        const val DATE = "date"
        const val MIN_DATE = "min_date"
        const val MAX_DATE = "max_date"
    }
}
