package com.ronaker.app.ui.selectCategory

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ronaker.app.R
import com.ronaker.app.base.BaseDialog
import com.ronaker.app.databinding.DialogAddProductSelectCategoryBinding
import com.ronaker.app.model.Category

class AddProductCategorySelectDialog : BaseDialog() {

    //region field
    private val TAG = AddProductCategorySelectDialog::class.java.simpleName

    lateinit var rootView: View

    var dialogResultListener: OnDialogResultListener? = null
    lateinit var categoryList: List<Category>
    var parent: Category? = null


    lateinit var binding: DialogAddProductSelectCategoryBinding

    enum class DialogResultEnum {
        OK, CANCEL, NONE
    }


    private var dialogResult =
        DialogResultEnum.NONE


    var location: Category? = null

    lateinit var viewModel: AddProductCategorySelectViewModel


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
            R.layout.dialog_add_product_select_category,
            container,
            false
        )
        viewModel = ViewModelProvider(this).get(AddProductCategorySelectViewModel::class.java)

        binding.viewModel = viewModel

        rootView = binding.root
        binding.dialog = this



        viewModel.selectedPlace.observe(viewLifecycleOwner, Observer { value ->
            location = value
            dialogResult =
                DialogResultEnum.OK
            stop()
        })



        binding.containerLayout.setOnClickListener { dismiss() }

        initilizeAdapter()



        categoryList.let { viewModel.searchLocation(it) }



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

        dialogResultListener?.onDialogResult(dialogResult,parent, location)




        super.onDismiss(dialog)

    }

    override fun onDestroyView() {
        dialog?.setDismissMessage(null)
        super.onDestroyView()
    }


    //endregion static function


    interface OnDialogResultListener {
        fun onDialogResult(result: DialogResultEnum,parent:Category?, selectedCategory: Category?)

    }


    class DialogBuilder(internal val fragmentManager: FragmentManager) {
        internal val dialog: AddProductCategorySelectDialog =
            AddProductCategorySelectDialog()


        fun setListener(listener: OnDialogResultListener): DialogBuilder {

            dialog.dialogResultListener = listener
            return this

        }

        fun setParent(parent:Category?): DialogBuilder {

            dialog.parent = parent
            return this

        }

        fun setCategories(categories: List<Category>): DialogBuilder {

            dialog.categoryList = categories
            return this

        }

        fun show(): AddProductCategorySelectDialog {

            dialog.show(fragmentManager, "AddProductLocationSearchDialog")
            return dialog
        }


    }

}