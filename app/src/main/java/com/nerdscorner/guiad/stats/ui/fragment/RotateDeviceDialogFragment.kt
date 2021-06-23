package com.nerdscorner.guiad.stats.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.nerdscorner.guiad.stats.R
import com.nerdscorner.guiad.stats.extensions.rotateCounterClockwise

class RotateDeviceDialogFragment : DialogFragment() {
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        var parentView = view?.parent as? View
        do {
            parentView?.background = null
            parentView = parentView?.parent as? View
        } while (parentView != null)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        isCancelable = true
        return inflater.inflate(R.layout.rotate_device_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<View>(R.id.primary_action_button).setOnClickListener {
            dismiss()
        }
        val rotateDeviceIcon: ImageView = view.findViewById(R.id.rotate_device_icon)
        rotateDeviceIcon.rotateCounterClockwise()
    }

    companion object {
        private const val TAG = "RotateDeviceDialogFragment"

        private fun searchFragment(fm: FragmentManager) = fm.findFragmentByTag(TAG) as? RotateDeviceDialogFragment

        fun show(fragmentManager: FragmentManager): RotateDeviceDialogFragment {
            return searchFragment(fragmentManager) ?: RotateDeviceDialogFragment().apply {
                show(fragmentManager, TAG)
            }
        }
    }
}
