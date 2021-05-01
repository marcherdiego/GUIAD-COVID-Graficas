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
    private var primaryActionCallback: (() -> Unit)? = null
    private var secondaryActionCallback: (() -> Unit)? = null
    private var dismissListener: (() -> Unit)? = null

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
                message = getString(MESSAGE),
                isPrimaryActionVisible = primaryActionCallback != null,
                isSecondaryActionVisible = secondaryActionCallback != null,
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

    fun onPrimaryActionButtonClicked() {
        dismiss()
        primaryActionCallback?.invoke()
    }

    fun onSecondaryActionButtonClicked() {
        dismiss()
        secondaryActionCallback?.invoke()
    }

    override fun dismiss() {
        dismissListener?.invoke()
        super.dismiss()
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
            primaryActionCallback: (() -> Unit)? = null,
            secondaryActionCallback: (() -> Unit)? = null,
            dismissListener: (() -> Unit)? = null
        ): MessageDialogFragment {
            return searchFragment(fragmentManager) ?: newInstance(text = text, cancelable = cancelable).apply {
                this.primaryActionCallback = primaryActionCallback
                this.secondaryActionCallback = secondaryActionCallback
                this.dismissListener = dismissListener
                show(fragmentManager, TAG)
            }
        }
    }
}
