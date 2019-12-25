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


class ItemExploreAdapter(
    dataList: ArrayList<Product>
) : RecyclerView.Adapter<ItemExploreAdapter.ViewHolder>() {
    private  var productList:List<Product> = dataList

   lateinit var context:Context
    private var lastPosition=-1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: AdapterExploreItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_explore_item, parent, false)
        context=parent.context

        return ViewHolder(binding)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val animation: Animation = AnimationUtils.loadAnimation(
            context,
            if (position > lastPosition) R.anim.adapter_up_from_bottom else R.anim.adapter_down_from_top
        )
        holder.itemView.startAnimation(animation)
        lastPosition = position


        holder.bind(productList[position])
    }

    fun reset(){
        lastPosition=-1
    }

    override fun getItemCount(): Int {
        return  productList.size
    }

    override fun onViewRecycled(holder: ViewHolder) {
        holder.onViewRecycled()
        super.onViewRecycled(holder)
    }


    fun updateList(){
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: AdapterExploreItemBinding
    ):RecyclerView.ViewHolder(binding.root){

        private val viewModel = ItemExploreViewModel(binding.root.getApplication())

        fun bind(product:Product){
            viewModel.bind(product,binding,binding.root.getParentActivity())
            binding.viewModel = viewModel
        }

        fun onViewRecycled() {
            binding.image.setImageResource(0)

        }
    }
}