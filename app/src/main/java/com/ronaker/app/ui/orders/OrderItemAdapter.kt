package com.ronaker.app.ui.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.R
import com.ronaker.app.databinding.AdapterExploreItemBinding
import com.ronaker.app.databinding.AdapterOrdreItemBinding
import com.ronaker.app.model.Order
import com.ronaker.app.model.Product
import com.ronaker.app.ui.dashboard.DashboardActivity
import com.ronaker.app.utils.extension.getParentActivity

class OrderItemAdapter(dataList: ArrayList<Order>) : RecyclerView.Adapter<OrderItemAdapter.ViewHolder>() {
    private lateinit var datalist:List<Order>

    init {
        this.datalist=dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: AdapterOrdreItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_ordre_item, parent, false)


        return ViewHolder(binding,parent.getParentActivity()as DashboardActivity)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datalist[position])
    }

    override fun getItemCount(): Int {
        return if(::datalist.isInitialized) datalist.size else 0
    }

    fun updateproductList(){
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: AdapterOrdreItemBinding,
       var context: DashboardActivity
    ):RecyclerView.ViewHolder(binding.root){

        private val viewModel = OrderItemViewModel()

        fun bind(item:Order){
            viewModel.bind(item,context)
            binding.viewModel = viewModel
        }
    }
}