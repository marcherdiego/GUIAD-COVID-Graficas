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
import com.nerdscorner.covid.stats.ui.mvp.model.ProgressDialogModel
import com.nerdscorner.covid.stats.ui.mvp.presenter.ProgressDialogPresenter
import com.nerdscorner.covid.stats.ui.mvp.view.ProgressDialogView

class ProgressDialogFragment : DialogFragment() {
    private lateinit var presenter: ProgressDialogPresenter

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
        presenter = ProgressDialogPresenter(
            ProgressDialogView(this),
            ProgressDialogModel()
        )

        arguments?.apply {
            presenter.setConfiguration(
                progressbarIsVisible = getBoolean(LOADING, true),
                message = getString(MESSAGE)
            )
        }
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

    companion object {
        private const val TAG = "ProgressDialogFragment"
        private const val MESSAGE = "dialog_message"
        private const val LOADING = "loading"
        private const val CUSTOM_VIEW = "custom_view"
        private const val CANCELABLE = "cancelable"

        private fun newInstance(
            text: String? = null,
            showProgress: Boolean,
            cancelable: Boolean = true,
            @LayoutRes customView: Int = R.layout.progress_dialog
        ): ProgressDialogFragment {
            return ProgressDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(MESSAGE, text)
                    putBoolean(LOADING, showProgress)
                    putInt(CUSTOM_VIEW, customView)
                    putBoolean(CANCELABLE, cancelable)
                }
            }
        }

        private fun searchFragment(fm: FragmentManager) = fm.findFragmentByTag(TAG) as? ProgressDialogFragment

        fun showProgressDialog(
            fragmentManager: FragmentManager,
            text: String? = null,
            showProgress: Boolean,
            cancelable: Boolean = true
        ): ProgressDialogFragment {
            val existingFragment = searchFragment(fragmentManager)
            return if (existingFragment == null) {
                val result = newInstance(
                    text = text,
                    showProgress = showProgress,
                    cancelable = cancelable
                )
                result.show(fragmentManager)
                result
            } else {
                existingFragment
            }
        }
    }
}
