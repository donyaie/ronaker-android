package com.ronaker.app.ui.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.databinding.AdapterOrdreItemBinding
import com.ronaker.app.model.Order
import com.ronaker.app.ui.dashboard.DashboardActivity
import com.ronaker.app.utils.extension.getParentActivity

class OrderItemAdapter(dataList: ArrayList<Order>,val viewModel: BaseViewModel) : RecyclerView.Adapter<OrderItemAdapter.ViewHolder>() {
    private  var datalist:List<Order> = dataList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: AdapterOrdreItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_ordre_item, parent, false)


        return ViewHolder(binding,parent.getParentActivity()as DashboardActivity,viewModel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datalist[position])
    }

    override fun getItemCount(): Int {
        return  datalist.size
    }

    fun updateproductList(){
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: AdapterOrdreItemBinding,
        var context: DashboardActivity,
        parentViewModel: BaseViewModel
    ):RecyclerView.ViewHolder(binding.root){

        private val viewModel = OrderItemViewModel(parentViewModel.getApplication())

        fun bind(item:Order){
            viewModel.bind(item,context)
            binding.viewModel = viewModel
        }
    }
}