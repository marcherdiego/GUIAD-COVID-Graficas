package com.nerdscorner.guiad.stats.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.nerdscorner.guiad.stats.R
import com.nerdscorner.guiad.stats.domain.Stat
import com.nerdscorner.guiad.stats.ui.adapter.StatsAdapter

class StatPickerDialogFragment : DialogFragment() {
    private var itemsChangedListener: (statsList: ArrayList<Stat>) -> Unit = {}
    
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        var parentView = view?.parent as? View
        do {
            parentView?.setBackgroundResource(R.drawable.loading_dialog_background)
            parentView = parentView?.parent as? View
        } while (parentView != null)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        isCancelable = true
        return inflater.inflate(R.layout.stat_picker_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<View>(R.id.primary_action_button).setOnClickListener {
            dismiss()
        }
        view.findViewById<RecyclerView>(R.id.stats_list).apply {
            val statsList = arguments?.getSerializable(STATS_LIST_KEY) as ArrayList<Stat>
            adapter = StatsAdapter(statsList).apply { 
                setItemsChangesListener(itemsChangedListener)
            }
        }
    }

    fun setItemsChangedListener(itemsChangedListener: (statsList: ArrayList<Stat>) -> Unit = {}) {
        this.itemsChangedListener = itemsChangedListener
    }

    companion object {
        private const val TAG = "StatPickerDialogFragment"
        private const val STATS_LIST_KEY = "stats_list"

        private fun searchFragment(fm: FragmentManager) = fm.findFragmentByTag(TAG) as? StatPickerDialogFragment

        fun show(fragmentManager: FragmentManager, statsList: ArrayList<Stat>): StatPickerDialogFragment {
            val statPickerDialogFragment = searchFragment(fragmentManager) ?: StatPickerDialogFragment().apply {
                show(fragmentManager, TAG)
            }
            statPickerDialogFragment.apply { 
                arguments = bundleOf(STATS_LIST_KEY to statsList)
            }
            return statPickerDialogFragment
        }
    }
}
