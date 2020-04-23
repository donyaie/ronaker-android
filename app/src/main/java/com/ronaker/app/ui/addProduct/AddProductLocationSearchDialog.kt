package com.ronaker.app.ui.addProduct

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.widget.RxTextView
import com.ronaker.app.R
import com.ronaker.app.base.BaseDialog
import com.ronaker.app.databinding.DialogAddProductLocationSearchBinding
import com.ronaker.app.model.Place
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class AddProductLocationSearchDialog : BaseDialog() {

    //region field
    private val TAG = AddProductLocationSearchDialog::class.java.simpleName

    lateinit var rootView: View

    var dialogResultListener: OnDialogResultListener? = null


    lateinit var binding: DialogAddProductLocationSearchBinding

    enum class DialogResultEnum {
        OK, CANCEL, NONE
    }


    private var dialogResult = DialogResultEnum.NONE


    var location: Place? = null

    lateinit var viewModel: AddProductLocationSearchViewModel


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
            R.layout.dialog_add_product_location_search,
            container,
            false
        )
        viewModel = ViewModelProvider(this).get(AddProductLocationSearchViewModel::class.java)

        binding.viewModel = viewModel

        rootView = binding.root
        binding.dialog = this




        binding.containerLayout.setOnClickListener { dismiss() }

        initilizeAdapter()
        viewModel.selectedPlace.observe(viewLifecycleOwner, Observer { value ->
            location = value
            dialogResult = DialogResultEnum.OK
            stop()
        })


        disposable = RxTextView.textChanges(binding.searchEdit)
            .debounce(1000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .subscribe {

                searchLocation(it.toString())
            }






        return rootView
    }


    internal fun initilizeAdapter() {

        val linearLayoutManager = LinearLayoutManager(context)
        binding.recycler.layoutManager = linearLayoutManager


    }


    private fun searchLocation(filter: String?) {


        if (!(filter == null || filter.trim { it <= ' ' }.isEmpty())) {

            viewModel.searchLocation(filter)
        }


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
        disposable?.dispose()
    }


    override fun onDismiss(dialog: DialogInterface) {

        dialogResultListener?.onDialogResult(dialogResult, location)




        super.onDismiss(dialog)

    }

    override fun onDestroyView() {
        dialog?.setDismissMessage(null)
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