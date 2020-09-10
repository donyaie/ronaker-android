package com.ronaker.app.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.General
import com.ronaker.app.R
import com.ronaker.app.databinding.AdapterExploreItemBinding
import com.ronaker.app.databinding.AdapterSearchItemBinding
import com.ronaker.app.model.Product
import com.ronaker.app.ui.exploreProduct.ExploreProductActivity
import com.ronaker.app.utils.extension.getParentActivity
import com.ronaker.app.utils.itemSelect


class ItemSearchAdapter :
    ListAdapter<Product, ItemSearchAdapter.ViewHolder>(ItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: AdapterSearchItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_search_item,
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    override fun onViewRecycled(holder: ViewHolder) {
        holder.onViewRecycled()
        super.onViewRecycled(holder)
    }


    class ViewHolder(
        private val binding: AdapterSearchItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val viewModel = ItemSearchViewModel()

        fun bind(product: Product) {
            viewModel.bind(product, binding)
            binding.viewModel = viewModel


            binding.image.transitionName = "${product.avatar}"
//            binding.title.transitionName = "${product.name}"


            binding.root.setOnClickListener {
                binding.root.getParentActivity()?.let { activity ->

//
//                    val pair1: androidx.core.util.Pair<View, String> = androidx.core.util.Pair<View, String> (
//                        binding.image as View,
//                        binding.image.transitionName
//                    )
//
////                    val pair2: androidx.core.util.Pair<View, String> = androidx.core.util.Pair<View, String> (
////                        binding.title as View,
////                        binding.title.transitionName
////                    )
//
//                    val optionsCompat: ActivityOptionsCompat =
//                        ActivityOptionsCompat.makeSceneTransitionAnimation(
//                            activity,
//                            pair1
//                           // ,pair2
//                        )
//                    activity.startActivityForResult( ExploreProductActivity.newInstance(activity, product), ExploreProductActivity.REQUEST_CODE, optionsCompat.toBundle(),)

                    (activity.application as General).analytics.itemSelect(product)

                    activity.startActivityForResult(
                        ExploreProductActivity.newInstance(
                            activity,
                            product
                        ), ExploreProductActivity.REQUEST_CODE
                    )


                }
            }
        }

        fun onViewRecycled() {
            binding.image.setImageResource(0)

        }
    }


    class ItemDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem.suid == newItem.suid

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem == newItem

    }


}