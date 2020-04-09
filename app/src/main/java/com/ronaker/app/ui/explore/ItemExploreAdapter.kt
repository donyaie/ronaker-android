package com.ronaker.app.ui.explore

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.R
import com.ronaker.app.databinding.AdapterExploreItemBinding
import com.ronaker.app.model.Product
import com.ronaker.app.utils.extension.getApplication
import com.ronaker.app.utils.extension.getParentActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class ItemExploreAdapter(
   val dataList: ArrayList<Product>
) : RecyclerView.Adapter<ItemExploreAdapter.ViewHolder>() {

    lateinit var context: Context
    private var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: AdapterExploreItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_explore_item,
            parent,
            false
        )
        context = parent.context

        return ViewHolder(binding)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val animation: Animation = AnimationUtils.loadAnimation(
            context,
            if (position > lastPosition) android.R.anim.fade_in else android.R.anim.fade_in
        )
        holder.itemView.startAnimation(animation)
        lastPosition = position


        holder.bind(dataList[position])
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


    fun updateList() {
        MainScope().launch {
            notifyDataSetChanged()
        }
    }


    fun addData(data:List<Product>) {
        MainScope().launch {
            var insertIndex = 0
            if (dataList.isNotEmpty())
                insertIndex = dataList.size

            dataList.addAll(data)
            notifyItemRangeInserted(insertIndex, data.size)


        }
    }

    class ViewHolder(
        private val binding: AdapterExploreItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val viewModel = ItemExploreViewModel(binding.root.getApplication())

        fun bind(product: Product) {
            viewModel.bind(product, binding, binding.root.getParentActivity())
            binding.viewModel = viewModel
        }

        fun onViewRecycled() {
            binding.image.setImageResource(0)

        }
    }
}