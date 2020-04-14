package com.ronaker.app.ui.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.R
import com.ronaker.app.databinding.AdapterOrdreItemBinding
import com.ronaker.app.model.Order
import com.ronaker.app.model.Product
import com.ronaker.app.ui.explore.ItemExploreAdapter
import com.ronaker.app.utils.DiffUtils
import com.ronaker.app.utils.extension.getApplication
import com.ronaker.app.utils.extension.getParentActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class OrderItemAdapter() : RecyclerView.Adapter<OrderItemAdapter.ViewHolder>() {
    private  var datalist= ArrayList<Order>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: AdapterOrdreItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_ordre_item, parent, false)


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datalist[position])
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val combinedChange =
                DiffUtils.createCombinedPayload(payloads as List<DiffUtils.Change<Order>>)
//            val oldData = combinedChange.oldData
            val newData = combinedChange.newData
            holder.bind(newData)

        }
    }


    override fun getItemCount(): Int {
        return  datalist.size
    }



    fun updateList(newItems: List<Order>) {

        MainScope().launch {

            val result = DiffUtil.calculateDiff(
                DiffUtilCallback(
                    datalist,
                    newItems
                )
            )
            datalist.clear()
            datalist.addAll(newItems)
            result.dispatchUpdatesTo(this@OrderItemAdapter)
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


    class DiffUtilCallback(
        private var oldItems: List<Order>,
        private var newItems: List<Order>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition].suid == newItems[newItemPosition].suid

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition] == newItems[newItemPosition]

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return DiffUtils.Change(
                oldItem,
                newItem
            )
        }
    }
}