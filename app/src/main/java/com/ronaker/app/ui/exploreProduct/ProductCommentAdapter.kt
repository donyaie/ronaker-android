package com.ronaker.app.ui.exploreProduct

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.R
import com.ronaker.app.databinding.AdapterProductCommentBinding
import com.ronaker.app.model.Product
import com.ronaker.app.utils.extension.getApplication
import com.ronaker.app.utils.extension.getParentActivity

class ProductCommentAdapter(
    dataList: ArrayList<Product.ProductRate>
) : RecyclerView.Adapter<ProductCommentAdapter.ViewHolder>() {
    private  var productList:List<Product.ProductRate> = dataList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: AdapterProductCommentBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_product_comment, parent, false)


        return ViewHolder(binding)
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
        private val binding: AdapterProductCommentBinding
    ):RecyclerView.ViewHolder(binding.root){

        private val viewModel = ProductCommentViewModel(binding.root.getApplication())

        fun bind(product:Product.ProductRate){
            viewModel.bind(product,binding)
            binding.viewModel = viewModel
        }
    }
}