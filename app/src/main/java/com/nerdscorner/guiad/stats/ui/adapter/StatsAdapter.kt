package com.nerdscorner.guiad.stats.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nerdscorner.guiad.stats.R
import com.nerdscorner.guiad.stats.domain.Stat

class StatsAdapter(private val statsList: List<Stat>) : RecyclerView.Adapter<StatsAdapter.ViewHolder>() {
    private var itemsChangedListener: (statsList: ArrayList<Stat>) -> Unit = {}
    private var isFromView = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.spinner_item, null))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stat = statsList[position]
        with(holder) {
            text.text = stat.toString()
            text.setOnClickListener {
                stat.toggle()
                isFromView = true
                holder.checkBox.isChecked = stat.selected
                isFromView = false
                itemsChangedListener(ArrayList(statsList.filter { it.selected }))
            }
            with(checkBox) {
                isFromView = true
                isChecked = stat.selected
                isFromView = false
                tag = position
                setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                    if (isFromView.not()) {
                        stat.selected = isChecked
                        itemsChangedListener(ArrayList(statsList.filter { it.selected }))
                    }
                }
            }
        }
    }

    override fun getItemCount() = statsList.size

    fun setItemsChangesListener(itemsChangedListener: (statsList: ArrayList<Stat>) -> Unit = {}) {
        this.itemsChangedListener = itemsChangedListener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.checkbox)
        val text: TextView = view.findViewById(R.id.text)
    }
}
