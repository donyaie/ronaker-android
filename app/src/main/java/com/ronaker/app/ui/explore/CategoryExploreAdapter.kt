package com.ronaker.app.ui.explore

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.R
import com.ronaker.app.databinding.AdapterExploreCategoryBinding
import com.ronaker.app.model.Category
import com.ronaker.app.utils.extension.getApplication
import com.ronaker.app.utils.extension.getParentActivity


class CategoryExploreAdapter(
    dataList: ArrayList<Category>,
    val listener: AdapterListener?
) : RecyclerView.Adapter<CategoryExploreAdapter.ViewHolder>() {
    private var productList: List<Category> = dataList

    lateinit var context: Context
    private var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: AdapterExploreCategoryBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_explore_category,
            parent,
            false
        )
        context = parent.context

        return ViewHolder(binding, listener)
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


        holder.bind(productList[position])
    }

    fun reset() {
        lastPosition = -1
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onViewRecycled(holder: ViewHolder) {
        holder.onViewRecycled()
        super.onViewRecycled(holder)
    }


    fun updateList() {
        notifyDataSetChanged()
    }


    interface AdapterListener {

        fun onSelectCategory(selected: Category)

    }

    class ViewHolder(
        private val binding: AdapterExploreCategoryBinding,

        private val listener: AdapterListener?

    ) : RecyclerView.ViewHolder(binding.root) {

        private val viewModel = CategoryExploreViewModel(binding.root.getApplication())

        fun bind(product: Category) {
            viewModel.bind(product, binding, binding.root.getParentActivity())
            binding.viewModel = viewModel


            binding.root.setOnClickListener {
                listener?.onSelectCategory(product)
            }


        }

        fun onViewRecycled() {
            binding.image.setImageResource(0)

        }
    }
}