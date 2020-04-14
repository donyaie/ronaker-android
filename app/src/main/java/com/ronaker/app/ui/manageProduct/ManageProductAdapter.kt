package com.ronaker.app.ui.manageProduct

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.R
import com.ronaker.app.databinding.AdapterManageProductBinding
import com.ronaker.app.model.Product
import com.ronaker.app.utils.DiffUtils
import com.ronaker.app.utils.extension.getApplication
import com.ronaker.app.utils.extension.getParentActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class ManageProductAdapter() : RecyclerView.Adapter<ManageProductAdapter.ViewHolder>() {
    val dataList = ArrayList<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: AdapterManageProductBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_manage_product,
            parent,
            false
        )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(this.dataList[position])
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            try {

                val combinedChange =
                    DiffUtils.createCombinedPayload(payloads as List<DiffUtils.Change<Product>>)
//            val oldData = combinedChange.oldData
                val newData = combinedChange.newData
                holder.bind(newData)

            } catch (ex: Exception) {

            }

        }




    }


    override fun getItemCount(): Int {
        return this.dataList.size
    }

    fun updateList(newItems: List<Product>) {

        MainScope().launch {

            val result = DiffUtil.calculateDiff(
                DiffUtilCallback(
                    dataList,
                    newItems
                )
            )
            dataList.clear()
            dataList.addAll(newItems)
            result.dispatchUpdatesTo(this@ManageProductAdapter)
        }
    }


    class ViewHolder(
        private val binding: AdapterManageProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val viewModel = ManageProductAdapterViewModel(binding.root.getApplication())

        fun bind(product: Product) {
            viewModel.bind(product, binding.root.getParentActivity())
            binding.viewModel = viewModel
        }
    }


    class DiffUtilCallback(
        private var oldItems: List<Product>,
        private var newItems: List<Product>
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