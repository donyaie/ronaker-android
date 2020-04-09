package com.ronaker.app.ui.manageProduct

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.R
import com.ronaker.app.databinding.AdapterManageProductBinding
import com.ronaker.app.model.Product
import com.ronaker.app.utils.extension.getApplication
import com.ronaker.app.utils.extension.getParentActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class ManageProductAdapter( val dataList: ArrayList<Product>) : RecyclerView.Adapter<ManageProductAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: AdapterManageProductBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_manage_product, parent, false)


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(this.dataList[position])
    }

    override fun getItemCount(): Int {
        return this.dataList.size
    }

    fun updateproductList(){
        MainScope().launch {
            notifyDataSetChanged()
        }
    }


    fun addData(data:List<Product>) {
        MainScope().launch {
            var insertIndex = 0
            if (dataList.isNotEmpty())
                insertIndex = dataList.size

            dataList.addAll(data)
            notifyItemRangeInserted(insertIndex, data.size)


        }
    }


    class ViewHolder(
        private val binding: AdapterManageProductBinding
    ):RecyclerView.ViewHolder(binding.root){

        private val viewModel = ManageProductAdapterViewModel(binding.root.getApplication())

        fun bind(product:Product){
            viewModel.bind(product,binding.root.getParentActivity())
            binding.viewModel = viewModel
        }
    }
}