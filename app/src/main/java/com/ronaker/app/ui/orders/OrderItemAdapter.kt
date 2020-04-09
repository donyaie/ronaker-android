package com.ronaker.app.ui.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.R
import com.ronaker.app.databinding.AdapterOrdreItemBinding
import com.ronaker.app.model.Order
import com.ronaker.app.utils.extension.getApplication
import com.ronaker.app.utils.extension.getParentActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class OrderItemAdapter(dataList: ArrayList<Order>) : RecyclerView.Adapter<OrderItemAdapter.ViewHolder>() {
    private  var datalist:List<Order> = dataList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: AdapterOrdreItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_ordre_item, parent, false)


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datalist[position])
    }

    override fun getItemCount(): Int {
        return  datalist.size
    }

    fun updateList(){
        MainScope().launch {

            notifyDataSetChanged()
        }
    }

    class ViewHolder(
        private val binding: AdapterOrdreItemBinding
    ):RecyclerView.ViewHolder(binding.root){

        private val viewModel = OrderItemViewModel(binding.root.getApplication())

        fun bind(item:Order){
            viewModel.bind(item,binding.root.getParentActivity())
            binding.viewModel = viewModel
        }
    }
}