package com.ronaker.app.ui.profileEmailVerify

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.LatLng
import com.jakewharton.rxbinding2.widget.RxTextView
import com.ronaker.app.R
import com.ronaker.app.base.BaseDialog
import com.ronaker.app.databinding.DialogAddProductLocationSearchBinding
import com.ronaker.app.databinding.DialogEmailVerifyBinding
import com.ronaker.app.model.Place
import com.ronaker.app.utils.Alert
import com.ronaker.app.utils.KeyboardManager
import com.ronaker.app.utils.extension.finishSafe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class EmailVerifyDialog : BaseDialog() {

    //region field
    private val TAG = EmailVerifyDialog::class.java.simpleName

    lateinit var rootView: View

    var dialogResultListener: OnDialogResultListener? = null


    lateinit var binding: DialogEmailVerifyBinding

    enum class DialogResultEnum {
        OK, CANCEL, NONE
    }


    private var dialogResult = DialogResultEnum.NONE



    lateinit var viewModel: ProfileEmailVerifyViewModel


    //endregion field

    //region override

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.NormalDialog)

    }

    var disposable:Disposable?=null

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
        viewModel = ViewModelProvider(this).get(ProfileEmailVerifyViewModel::class.java)

        binding.viewModel = viewModel

        rootView = binding.getRoot()
        binding.dialog = this



        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage != null) Alert.makeTextError(this, errorMessage)
        })
        viewModel.goNex.observe(viewLifecycleOwner, Observer {

            dialogResult=DialogResultEnum.OK
            stop()
        })

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


        viewModel.loadData()



    }


    fun stop() {
        val dialog = dialog


        dialog?.dismiss()
        disposable?.dispose()
    }


    override fun onResume() {
        super.onResume()

    }

    override fun onDismiss(dialog: DialogInterface) {

        dialogResultListener?.onDialogResult(dialogResult)




        super.onDismiss(dialog)

    }

    override fun onDestroy() {
        super.onDestroy()
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