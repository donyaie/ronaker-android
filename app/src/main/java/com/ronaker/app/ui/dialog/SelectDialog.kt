package com.ronaker.app.ui.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ronaker.app.R
import com.ronaker.app.base.BaseDialog
import com.ronaker.app.databinding.DialogSelectBinding
import com.ronaker.app.model.Category

class SelectDialog : BaseDialog() {

    //region field
    private val TAG = SelectDialog::class.java.simpleName

    lateinit var rootView: View

    var dialogResultListener: OnDialogResultListener? = null

    lateinit var binding: DialogSelectBinding

    enum class DialogResultEnum {
        OK, CANCEL, NONE
    }


    var itemList: List<SelectItem> = ArrayList()
    var selectedItem: SelectItem ?=null



    var title: String?=null



    private var dialogResult = DialogResultEnum.NONE


    var location: Category? = null

    lateinit var viewModel: SelectDialogViewModel


    //endregion field

    //region override

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.NormalDialog)
    }


    data class SelectItem(var id :String,var title:String)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_select,
            container,
            false
        )
        viewModel = ViewModelProvider(this).get(SelectDialogViewModel::class.java)

        binding.viewModel = viewModel

        rootView = binding.root



        viewModel.selectedPlace.observe(viewLifecycleOwner, Observer { value ->
            selectedItem=value
            dialogResult= DialogResultEnum.OK
            stop()
        })



        binding.containerLayout.setOnClickListener { dismiss() }

        initilizeAdapter()



        viewModel.searchLocation(itemList)

        viewModel.title.value=title


        return rootView
    }






    internal fun initilizeAdapter() {

        val linearLayoutManager = LinearLayoutManager(context)
        binding.recycler.layoutManager = linearLayoutManager


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

        dialogResultListener?.onDialogResult(dialogResult,selectedItem)




        super.onDismiss(dialog)

    }

    override fun onDestroyView() {
        dialog?.setDismissMessage(null)
        super.onDestroyView()
    }


    //endregion static function


    interface OnDialogResultListener {
        fun onDialogResult(result: DialogResultEnum,selectedItem:SelectItem?)

    }


    class DialogBuilder(internal val fragmentManager: FragmentManager) {
        internal val dialog: SelectDialog = SelectDialog()


        fun setListener(listener: OnDialogResultListener): DialogBuilder {

            dialog.dialogResultListener = listener
            return this

        }

        fun setTitle(title:String ): DialogBuilder {

            dialog.title = title
            return this

        }

        fun setItems(items:List<SelectItem> ): DialogBuilder {

            dialog.itemList = items
            return this

        }

        fun show(): SelectDialog {

            dialog.show(fragmentManager, "AddProductLocationSearchDialog")
            return dialog
        }


    }

}