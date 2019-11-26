package com.ronaker.app.ui.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.R
import com.ronaker.app.databinding.AdapterExploreItemBinding
import com.ronaker.app.model.Product
import com.ronaker.app.utils.extension.getApplication
import com.ronaker.app.utils.extension.getParentActivity

class ItemExploreAdapter(
    dataList: ArrayList<Product>
) : RecyclerView.Adapter<ItemExploreAdapter.ViewHolder>() {
    private  var productList:List<Product> = dataList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: AdapterExploreItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_explore_item, parent, false)


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
        private val binding: AdapterExploreItemBinding
    ):RecyclerView.ViewHolder(binding.root){

        private val viewModel = ItemExploreViewModel(binding.root.getApplication())

        fun bind(product:Product){
            viewModel.bind(product,binding,binding.root.getParentActivity())
            binding.viewModel = viewModel
        }
    }
}