package com.ronaker.app.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.General
import com.ronaker.app.R
import com.ronaker.app.databinding.AdapterSearchCategoryBinding
import com.ronaker.app.databinding.AdapterSearchItemBinding
import com.ronaker.app.model.Category
import com.ronaker.app.model.Product
import com.ronaker.app.ui.exploreProduct.ExploreProductActivity
import com.ronaker.app.utils.extension.getParentActivity
import com.ronaker.app.utils.itemSelect


class ItemSearchAdapter(val listener:Listener) :
    ListAdapter<Any, RecyclerView.ViewHolder>(ItemDiffCallback()) {


    private val TYPE_PRODUCT = 0
    private val TYPE_CATEGORY = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == TYPE_PRODUCT) {
            val binding: AdapterSearchItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.adapter_search_item,
                parent,
                false
            )
            return ProductViewHolder(binding,listener)
        } else {
            val binding: AdapterSearchCategoryBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.adapter_search_category,
                parent,
                false
            )

            return CategoryViewHolder(binding,listener)
        }

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is CategoryViewHolder)
            holder.bind(getItem(position) as Category)
        if (holder is ProductViewHolder)
            holder.bind(getItem(position) as Product)
    }

    override fun getItemViewType(position: Int): Int {

        return when {
            getItem(position) is Category -> return TYPE_CATEGORY
            getItem(position) is Product -> return TYPE_PRODUCT
            else -> super.getItemViewType(position)
        }


    }



    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {

        when (holder) {
            is ProductViewHolder -> holder.onViewRecycled()
            is CategoryViewHolder -> holder.onViewRecycled()
            else -> super.onViewRecycled(holder)
        }
    }


    class ProductViewHolder(
        private val binding: AdapterSearchItemBinding,val listener:Listener
    ) : RecyclerView.ViewHolder(binding.root) {

        private val viewModel = ItemSearchViewModel()

        fun bind(product: Product) {
            viewModel.bind(product, binding)
            binding.viewModel = viewModel


            binding.image.transitionName = "${product.avatar}"

            binding.root.setOnClickListener {

                listener.onItemSelect(product)


            }
        }

        fun onViewRecycled() {
            binding.image.setImageResource(0)

        }
    }


    class CategoryViewHolder(
        private val binding: AdapterSearchCategoryBinding,val listener:Listener
    ) : RecyclerView.ViewHolder(binding.root) {

        private val viewModel = CategorySearchViewModel()

        fun bind(item: Category) {
            viewModel.bind(item)
            binding.viewModel = viewModel


            binding.root.setOnClickListener {

                    listener.onCategorySelect(item)


            }
        }

        fun onViewRecycled() {
            binding.image.setImageResource(0)

        }
    }


    class ItemDiffCallback : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {

            return if (oldItem is Category && newItem is Category) {
                oldItem.suid == newItem.suid
            } else if (oldItem is Product && newItem is Product) {
                oldItem.suid == newItem.suid
            } else {
                false
            }


        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return if (oldItem is Category && newItem is Category) {
                oldItem.suid == newItem.suid
            } else if (oldItem is Product && newItem is Product) {
                oldItem.suid == newItem.suid
            } else {
                false
            }

        }

    }



    interface Listener{
        fun onItemSelect(item:Product)
        fun onCategorySelect(category: Category)
    }


}