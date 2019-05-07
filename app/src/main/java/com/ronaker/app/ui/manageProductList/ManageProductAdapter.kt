package com.ronaker.app.ui.manageProductList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.R
import com.ronaker.app.databinding.AdapterExploreItemBinding
import com.ronaker.app.databinding.AdapterManageProductBinding
import com.ronaker.app.model.Product
import com.ronaker.app.ui.dashboard.DashboardActivity
import com.ronaker.app.utils.extension.getParentActivity

class ManageProductAdapter(dataList: ArrayList<Product>) : RecyclerView.Adapter<ManageProductAdapter.ViewHolder>() {
    private lateinit var productList:List<Product>

    init {
        productList=dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: AdapterManageProductBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_manage_product, parent, false)


        return ViewHolder(binding,parent.getParentActivity()as DashboardActivity)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int {
        return if(::productList.isInitialized) productList.size else 0
    }

    fun updateproductList(){
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: AdapterManageProductBinding,
       var context: DashboardActivity
    ):RecyclerView.ViewHolder(binding.root){

        private val viewModel = ManageproductAdapterViewModel()

        fun bind(product:Product){
            viewModel.bind(product,context)
            binding.viewModel = viewModel
        }
    }
}