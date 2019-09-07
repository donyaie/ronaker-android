package com.ronaker.app.ui.addProduct

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.LatLng
import com.jakewharton.rxbinding2.widget.RxTextView
import com.ronaker.app.R
import com.ronaker.app.base.BaseDialog
import com.ronaker.app.databinding.DialogAddProductLocationSearchBinding
import com.ronaker.app.model.Place
import com.ronaker.app.utils.KeyboardManager
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class AddProductLocationSearchDialog : BaseDialog() {

    //region field
    private val TAG = AddProductLocationSearchDialog::class.java.getSimpleName()

    lateinit var rootView: View

    var dialogResultListener: OnDialogResultListener? = null


    lateinit var binding: DialogAddProductLocationSearchBinding

    enum class DialogResultEnum {
        OK, CANCEL, NONE
    }


    private var dialogResult = DialogResultEnum.NONE

    internal var context: Context? = null

    var location: Place? = null

    lateinit var viewModel: AddProductLocationSearchViewModel


    //endregion field

    //region override

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.NormalDialog)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_add_product_location_search,
            container,
            false
        )
        viewModel = ViewModelProviders.of(this).get(AddProductLocationSearchViewModel::class.java)

        binding.viewModel = viewModel

        rootView = binding.getRoot()
        binding.dialog = this
        context = rootView.context

        binding.containerLayout.setOnClickListener { dismiss() }

        initilizeAdapter()

//        RxTextView.textChanges(binding.searchEdit)
//            .debounce(400, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
//            .subscribe(object : Action1<CharSequence>() {
//                fun call(value: CharSequence) {
//
//
//                    searchLocation(value.toString())
//
//
//                }
//            })



        viewModel.selectedPlace.observe(this, Observer { value ->


            location=value
            dialogResult=DialogResultEnum.OK
            stop()


        })


        RxTextView.textChanges(binding.searchEdit)
            .debounce(400, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .subscribe {

                searchLocation(it.toString())
            }


        return rootView
    }


    internal fun initilizeAdapter() {

        val linearLayoutManager = LinearLayoutManager(context)
        binding.recycler.setLayoutManager(linearLayoutManager)


    }


    private fun searchLocation(filter: String?) {


        if (filter == null || filter.trim { it <= ' ' }.isEmpty()) {

        } else

          viewModel.searchLocation(filter)

    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = super.onCreateDialog(savedInstanceState)

        // request a window without the title

        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )





        return dialog

    }


    override fun onStart() {

        super.onStart()

        val dialog = dialog
        if (dialog != null) {

            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            dialog.window!!.setGravity(Gravity.TOP)


        }


    }


    fun stop() {
        val dialog = dialog
        dialog?.dismiss()
    }


    override fun onResume() {
        super.onResume()

    }

    override fun onDismiss(dialog: DialogInterface) {

        dialogResultListener?.onDialogResult(dialogResult, location)

        context?.let { KeyboardManager.hideSoftKeyboard(it, binding.searchEdit) }
        super.onDismiss(dialog)

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDestroyView() {
        if (dialog != null) {
            dialog.setDismissMessage(null)
        }
        super.onDestroyView()
    }


    //endregion static function


    interface OnDialogResultListener {
        fun onDialogResult(result: DialogResultEnum, location: Place?)

    }


    class DialogBuilder(internal val fragmentManager: FragmentManager) {
        internal val dialog: AddProductLocationSearchDialog = AddProductLocationSearchDialog()


        fun setListener(listener: OnDialogResultListener): DialogBuilder {

            dialog.dialogResultListener = listener
            return this

        }

        fun show(): AddProductLocationSearchDialog {

            dialog.show(fragmentManager, "AddProductLocationSearchDialog")
            return dialog
        }


    }

}