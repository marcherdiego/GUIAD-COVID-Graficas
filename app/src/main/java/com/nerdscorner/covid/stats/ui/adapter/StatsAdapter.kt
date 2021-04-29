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

class StatsAdapter(private val ctx: Context, private val listState: List<Stat>) :
    ArrayAdapter<Stat?>(ctx, R.layout.simple_spinner_layout, listState) {

    private var itemsChangedListener: (selectedValues: List<Stat>) -> Unit = {}
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
        holder.textView.text = listState[position].toString()

        // To check weather checked event fire from getview() or user input
        isFromView = true
        holder.checkBox.isChecked = listState[position].selected
        isFromView = false
        if (position == 0) {
            holder.checkBox.visibility = View.INVISIBLE
        } else {
            holder.checkBox.visibility = View.VISIBLE
        }
        holder.checkBox.tag = position
        holder.checkBox.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (isFromView.not()) {
                listState[position].selected = isChecked
                itemsChangedListener(listState.filter { it.selected })
            }
        }
        return convertView!!
    }

    fun setSelectedItemsChangedListener(itemsChangedListener: (selectedValues: List<Stat>) -> Unit) {
        this.itemsChangedListener = itemsChangedListener
    }

    private class ViewHolder {
        lateinit var textView: TextView
        lateinit var checkBox: CheckBox
    }
}
