package com.nerdscorner.covid.stats.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.nerdscorner.covid.stats.R
import com.nerdscorner.covid.stats.ui.mvp.model.MessageDialogModel
import com.nerdscorner.covid.stats.ui.mvp.presenter.MessageDialogPresenter
import com.nerdscorner.covid.stats.ui.mvp.view.MessageDialogView
import com.nerdscorner.mvplib.events.bus.Bus

class MessageDialogFragment : DialogFragment() {
    private val bus = Bus.newInstance
    private lateinit var presenter: MessageDialogPresenter
    private var actionCallback: () -> Unit = {}

    private val customLayoutId by lazy {
        arguments?.getInt(CUSTOM_VIEW) ?: throw IllegalStateException("ProgressDialogFragment created with no layout id")
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        revealRoundedCorners()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        isCancelable = arguments?.getBoolean(CANCELABLE) ?: true
        return inflater.inflate(customLayoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter = MessageDialogPresenter(
            MessageDialogView(this),
            MessageDialogModel(),
            bus
        )

        arguments?.apply {
            presenter.setConfiguration(
                message = getString(MESSAGE)
            )
        }
    }

    override fun onResume() {
        super.onResume()
        bus.register(presenter)
    }

    override fun onPause() {
        bus.unregister(presenter)
        super.onPause()
    }

    private fun revealRoundedCorners() {
        var parentView = view?.parent as? View
        do {
            parentView?.background = null
            parentView = parentView?.parent as? View
        } while (parentView != null)
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    fun onActionButtonClicked() {
        dismiss()
        actionCallback()
    }

    companion object {
        private const val TAG = "ProgressDialogFragment"
        private const val MESSAGE = "dialog_message"
        private const val CUSTOM_VIEW = "custom_view"
        private const val CANCELABLE = "cancelable"

        private fun newInstance(
            text: String? = null,
            cancelable: Boolean = true,
            @LayoutRes customView: Int = R.layout.message_dialog
        ): MessageDialogFragment {
            return MessageDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(MESSAGE, text)
                    putInt(CUSTOM_VIEW, customView)
                    putBoolean(CANCELABLE, cancelable)
                }
            }
        }

        private fun searchFragment(fm: FragmentManager) = fm.findFragmentByTag(TAG) as? MessageDialogFragment

        fun showProgressDialog(
            fragmentManager: FragmentManager,
            text: String? = null,
            cancelable: Boolean = true,
            actionCallback: () -> Unit = {}
        ): MessageDialogFragment {
            val existingFragment = searchFragment(fragmentManager)
            return if (existingFragment == null) {
                val result = newInstance(
                    text = text,
                    cancelable = cancelable
                )
                result.actionCallback = actionCallback
                result.show(fragmentManager)
                result
            } else {
                existingFragment
            }
        }
    }
}
