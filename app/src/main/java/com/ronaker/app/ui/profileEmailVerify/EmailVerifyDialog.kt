package com.ronaker.app.ui.profileEmailVerify

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.ronaker.app.R
import com.ronaker.app.base.BaseDialog
import com.ronaker.app.databinding.DialogEmailVerifyBinding
import com.ronaker.app.utils.Alert
import com.ronaker.app.utils.IntentManeger
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable

@AndroidEntryPoint
class EmailVerifyDialog : BaseDialog() {


    lateinit var rootView: View

    var dialogResultListener: OnDialogResultListener? = null


    lateinit var binding: DialogEmailVerifyBinding

    enum class DialogResultEnum {
        OK, CANCEL, NONE
    }


    private var dialogResult = DialogResultEnum.NONE


   val viewModel: ProfileEmailVerifyViewModel by viewModels()


    //endregion field

    //region override

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.NormalDialog)

    }

    var disposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_email_verify,
            container,
            false
        )

        binding.viewModel = viewModel

        rootView = binding.root
        binding.dialog = this



        viewModel.errorMessage.observe(viewLifecycleOwner, {errorMessage ->
            if (errorMessage != null) Alert.makeTextError(this, errorMessage)
        })
        viewModel.goNex.observe(viewLifecycleOwner, {

            dialogResult = DialogResultEnum.OK
            stop()
        })

        viewModel.openInbox.observe(viewLifecycleOwner, {

            context?.let { it1 -> IntentManeger.openMailBox(it1) }
        })




        return rootView
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = super.onCreateDialog(savedInstanceState)

        // request a window without the title

        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
//        dialog.setCanceledOnTouchOutside(true)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

//        dialog.setOnKeyListener { _, keyCode, event ->
//            if (keyCode == KeyEvent.KEYCODE_BACK
//                && event.action == KeyEvent.ACTION_UP
//            ) {
//                return@setOnKeyListener true
//            }
//             false
//        }


        return dialog

    }


    override fun onStart() {

        super.onStart()

        val dialog = dialog
        if (dialog != null) {

            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            dialog.window?.setGravity(Gravity.TOP)


        }


        viewModel.loadData()


    }


    fun stop() {
        val dialog = dialog


        dialog?.dismiss()
        disposable?.dispose()
    }


    override fun onDismiss(dialog: DialogInterface) {

        dialogResultListener?.onDialogResult(dialogResult)




        super.onDismiss(dialog)

    }

    override fun onDestroyView() {
        dialog?.setDismissMessage(null)
        super.onDestroyView()
    }


    //endregion static function


    interface OnDialogResultListener {
        fun onDialogResult(result: DialogResultEnum)

    }


    class DialogBuilder(internal val fragmentManager: FragmentManager) {
        internal val dialog: EmailVerifyDialog = EmailVerifyDialog()


        fun setListener(listener: OnDialogResultListener): DialogBuilder {

            dialog.dialogResultListener = listener
            return this

        }

        fun show(): EmailVerifyDialog {

            dialog.show(fragmentManager, "EmailVerifyDialog")
            return dialog
        }


    }


}