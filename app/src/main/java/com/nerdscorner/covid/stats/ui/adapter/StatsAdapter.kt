package com.nerdscorner.covid.stats.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import com.nerdscorner.covid.stats.R
import com.nerdscorner.covid.stats.domain.Stat

class StatsAdapter(private val ctx: Context, private val stats: List<Stat>) :
    ArrayAdapter<Stat?>(ctx, R.layout.simple_spinner_layout, stats) {

    private var itemsChangedListener: (selectedValues: ArrayList<Stat>) -> Unit = {}
    private var isFromView = false

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView)
    }

    private fun getCustomView(position: Int, convertViewParam: View?): View {
        var convertView = convertViewParam
        val holder: ViewHolder
        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(ctx)
            convertView = layoutInflater.inflate(R.layout.spinner_item, null)
            holder = ViewHolder()
            holder.textView = convertView.findViewById(R.id.text)
            holder.checkBox = convertView.findViewById(R.id.checkbox)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        with(holder.textView) {
            text = stats[position].toString()
            val stat = stats[position]
            if (stat.isDummy().not()) {
                setOnClickListener {
                    stats[position].toggle()
                    isFromView = true
                    holder.checkBox.isChecked = stats[position].selected
                    isFromView = false
                    itemsChangedListener(ArrayList(stats.filter { it.selected }))
                }
            }
        }

        with(holder.checkBox) {
            isFromView = true
            isChecked = stats[position].selected
            isFromView = false
            visibility = if (position == 0) {
                View.INVISIBLE
            } else {
                View.VISIBLE
            }
            tag = position
            setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                if (isFromView.not()) {
                    val stat = stats[position]
                    if (stat.isDummy().not()) {
                        stats[position].selected = isChecked
                        itemsChangedListener(ArrayList(stats.filter { it.selected }))
                    }
                }
            }
        }
        return convertView!!
    }

    fun setSelectedItemsChangedListener(itemsChangedListener: (selectedValues: ArrayList<Stat>) -> Unit) {
        this.itemsChangedListener = itemsChangedListener
    }

    private class ViewHolder {
        lateinit var textView: TextView
        lateinit var checkBox: CheckBox
    }
}
