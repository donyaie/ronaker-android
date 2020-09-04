package com.ronaker.app.ui.support

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.ronaker.app.R
import com.ronaker.app.base.BaseDialog
import com.ronaker.app.databinding.DialogSupportBinding
import com.ronaker.app.utils.*

class SupportDialog : BaseDialog() {

    //region field
    private val TAG = SupportDialog::class.java.simpleName

    lateinit var rootView: View

    var dialogResultListener: OnDialogResultListener? = null

    lateinit var binding: DialogSupportBinding

    enum class DialogResultEnum {
        OK, CANCEL, NONE
    }


    private var dialogResult =
        DialogResultEnum.NONE


    //endregion field

    //region override

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.NormalDialog)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_support,
            container,
            false
        )


        rootView = binding.root
        binding.dialog = this




        binding.phone.setOnClickListener {

            IntentManeger.makeCall(requireActivity(), SUPPORT_PHONE)

        }


        binding.mail.setOnClickListener {

            IntentManeger.sendMail(requireActivity(), SUPPORT_URL)

        }


        binding.masenger.setOnClickListener {

            IntentManeger.openFacebook(requireActivity(), SUPPORT_FACEBOOK_ID, SUPPORT_FACEBOOK_NAME)

        }



        binding.containerLayout.setOnClickListener { dismiss() }





        return rootView
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = super.onCreateDialog(savedInstanceState)

        // request a window without the title

        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )





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


    }


    fun stop() {
        val dialog = dialog


        dialog?.dismiss()
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


    class DialogBuilder(private val fragmentManager: FragmentManager) {
        internal val dialog: SupportDialog =
            SupportDialog()


        fun setListener(listener: OnDialogResultListener): DialogBuilder {

            dialog.dialogResultListener = listener
            return this

        }


        fun show(): SupportDialog {

            dialog.show(fragmentManager, "SupportDialog")
            return dialog
        }


    }

}