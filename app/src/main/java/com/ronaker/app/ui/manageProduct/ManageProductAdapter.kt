@file:Suppress("UNCHECKED_CAST")

package com.ronaker.app.ui.manageProduct

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.R
import com.ronaker.app.databinding.AdapterManageProductBinding
import com.ronaker.app.model.Product
import com.ronaker.app.utils.extension.getParentActivity

class ManageProductAdapter :
    ListAdapter<Product, ManageProductAdapter.ViewHolder>(ItemDiffCallback()) {


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
        holder.bind(getItem(position))
    }


    class ViewHolder(
        private val binding: AdapterManageProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val viewModel = ManageProductAdapterViewModel()

        fun bind(product: Product) {
            viewModel.bind(product, binding)


            binding.root.setOnClickListener {

                binding.root.getParentActivity()?.let { activity ->

                    activity.startActivity(ManageProductActivity.newInstance(activity, product))

                }


            }


            binding.viewModel = viewModel
        }
    }


    class ItemDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem.suid == newItem.suid

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem == newItem

    }

}