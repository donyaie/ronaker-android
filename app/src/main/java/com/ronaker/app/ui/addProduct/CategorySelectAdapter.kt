package com.ronaker.app.ui.addProduct

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.R
import com.ronaker.app.databinding.AdapterCategorySelectBinding
import com.ronaker.app.databinding.AdapterExploreItemBinding
import com.ronaker.app.databinding.AdapterLocationSearchBinding
import com.ronaker.app.model.Category
import com.ronaker.app.model.Place

class CategorySelectAdapter(dataList: ArrayList<Category>, viewModel:AddProductCategorySelectViewModel) : RecyclerView.Adapter<CategorySelectAdapter.ViewHolder>() {
    private  var productList:List<Category> = dataList

    private  var mViewModel:AddProductCategorySelectViewModel= viewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: AdapterCategorySelectBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_category_select, parent, false)


        return ViewHolder(binding,mViewModel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int {
        return  productList.size
    }

    fun updateproductList(){
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: AdapterCategorySelectBinding,
       var parentViewModel: AddProductCategorySelectViewModel
    ):RecyclerView.ViewHolder(binding.root){

        private val viewModel = CategorySelectViewModel(parentViewModel.getApplication())

        fun bind(product:Category){
            viewModel.bind(product,binding,parentViewModel)
            binding.viewModel = viewModel
        }
    }
}