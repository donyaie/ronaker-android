package com.ronaker.app.ui.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.R
import com.ronaker.app.databinding.AdapterOrdreItemBinding
import com.ronaker.app.model.Order

class OrderItemAdapter(val listener: OrderItemListener) :
    ListAdapter<Order,OrderItemAdapter.ViewHolder>(ItemDiffCallback())  {


    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val binding: AdapterOrdreItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_ordre_item,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }



    override fun onViewRecycled(holder: ViewHolder) {
        holder.onViewRecycled()
        super.onViewRecycled(holder)
    }


    class ViewHolder(
        private val binding: AdapterOrdreItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {


        private val viewModel = OrderItemViewModel(binding)

        fun bind(item: Order, listener: OrderItemListener) {
            viewModel.bind(item, listener)
            binding.viewModel = viewModel
        }

        fun onViewRecycled() {
            viewModel.onCleared()
        }


    }


    interface OrderItemListener {
        fun onClickItem(order: Order)
        fun onClickItemArchive(order: Order)
        fun onClickItemRate(order: Order)
    }





    class ItemDiffCallback : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean = oldItem.suid == newItem.suid

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean = oldItem == newItem

    }


}