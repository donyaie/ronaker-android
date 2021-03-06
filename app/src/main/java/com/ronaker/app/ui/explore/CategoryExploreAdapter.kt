package com.ronaker.app.ui.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.General
import com.ronaker.app.R
import com.ronaker.app.databinding.AdapterExploreCategoryBinding
import com.ronaker.app.model.Category
import com.ronaker.app.utils.categorySelect
import com.ronaker.app.utils.extension.getApplication
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class CategoryExploreAdapter(
    private val dataList: ArrayList<Category>,
    val listener: AdapterListener?
) : RecyclerView.Adapter<CategoryExploreAdapter.ViewHolder>() {
    private var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: AdapterExploreCategoryBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_explore_category,
            parent,
            false
        )

        return ViewHolder(binding, listener)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val animation: Animation = AnimationUtils.loadAnimation(
            holder.itemView.context,
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

    fun itemChanged(item: Category) {
        MainScope().launch {
            notifyItemChanged(dataList.indexOf(item))
        }
    }

    fun addData(data: ArrayList<Category>) {
        MainScope().launch {
            var insertIndex = 0
            if (dataList.isNotEmpty())
                insertIndex = dataList.size

            dataList.addAll(data)
            notifyItemRangeInserted(insertIndex, data.size)


        }
    }

    interface AdapterListener {

        fun onSelectCategory(selected: Category)

    }

    class ViewHolder(
        private val binding: AdapterExploreCategoryBinding,

        private val listener: AdapterListener?

    ) : RecyclerView.ViewHolder(binding.root) {

        private val viewModel = CategoryExploreViewModel()

        fun bind(product: Category) {
            viewModel.bind(product, binding)
            binding.viewModel = viewModel


            binding.root.setOnClickListener {


                (binding.root.getApplication() as General).analytics.categorySelect(product)


                listener?.onSelectCategory(product)
            }


        }

        fun onViewRecycled() {
            binding.image.setImageResource(0)

        }
    }
}