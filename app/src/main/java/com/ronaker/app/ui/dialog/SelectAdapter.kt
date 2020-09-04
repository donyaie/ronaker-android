package com.ronaker.app.ui.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.R
import com.ronaker.app.databinding.AdapterSelectBinding

class SelectAdapter(
    dataList: ArrayList<SelectDialog.SelectItem>,
    viewModel: SelectDialogViewModel
) : RecyclerView.Adapter<SelectAdapter.ViewHolder>() {
    private var productList: List<SelectDialog.SelectItem> = dataList

    private var mViewModel: SelectDialogViewModel = viewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: AdapterSelectBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_select,
            parent,
            false
        )


        return ViewHolder(binding, mViewModel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    class ViewHolder(
        private val binding: AdapterSelectBinding,
        var parentViewModel: SelectDialogViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        private val viewModel = SelectAdapterViewModel()

        fun bind(product: SelectDialog.SelectItem) {
            viewModel.bind(product, binding, parentViewModel)
            binding.viewModel = viewModel
        }
    }
}