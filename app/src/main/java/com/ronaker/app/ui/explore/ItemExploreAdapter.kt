package com.ronaker.app.ui.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.R
import com.ronaker.app.databinding.AdapterExploreItemBinding
import com.ronaker.app.model.Product
import com.ronaker.app.ui.exploreProduct.ExploreProductActivity
import com.ronaker.app.utils.DiffUtils
import com.ronaker.app.utils.DiffUtils.createCombinedPayload
import com.ronaker.app.utils.extension.getApplication
import com.ronaker.app.utils.extension.getParentActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


@Suppress("UNCHECKED_CAST")
class ItemExploreAdapter : RecyclerView.Adapter<ItemExploreAdapter.ViewHolder>() {
    val dataList = ArrayList<Product>()


    private var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: AdapterExploreItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_explore_item,
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        val animation: Animation = AnimationUtils.loadAnimation(
//            context,
//            if (position > lastPosition) android.R.anim.fade_in else android.R.anim.fade_in
//        )
//        holder.itemView.startAnimation(animation)
        lastPosition = position


        holder.bind(dataList[position])
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else if (payloads[0] is Product) {
            val combinedChange = createCombinedPayload(payloads as List<DiffUtils.Change<Product>>)
//            val oldData = combinedChange.oldData
            val newData = combinedChange.newData
            holder.bind(newData)

        }
    }


    fun reset() {
        lastPosition = -1
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onViewRecycled(holder: ViewHolder) {
        holder.onViewRecycled()
        super.onViewRecycled(holder)
    }


    fun updateList(newItems: List<Product>) {

        MainScope().launch {

            val result = DiffUtil.calculateDiff(DiffUtilCallback(dataList, newItems))
            dataList.clear()
            dataList.addAll(newItems)
            result.dispatchUpdatesTo(this@ItemExploreAdapter)
        }
    }


    class ViewHolder(
        private val binding: AdapterExploreItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val viewModel = ItemExploreViewModel(binding.root.getApplication())

        fun bind(product: Product) {
            viewModel.bind(product)
            binding.viewModel = viewModel

            binding.root.setOnClickListener {
                binding.root.getParentActivity()?.let {activity->

                    activity.startActivityForResult(
                        ExploreProductActivity.newInstance(activity,product ),
                        ExploreProductActivity.REQUEST_CODE
                    )

                }
            }





        }

        fun onViewRecycled() {
            binding.image.setImageResource(0)

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