package com.ronaker.app.ui.addProduct

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.R
import com.ronaker.app.databinding.AdapterProductAddImageBinding
import com.ronaker.app.databinding.AdapterProductAddImageEmptyBinding
import com.ronaker.app.model.Product

class AddProductImageAdapter(val baseViewModel: AddProductViewModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var productList: ArrayList<Product.ProductImage>


    val EmptyType = 0;
    val FullType = 1;


    init {
        productList = ArrayList()
        productList.add(Product.ProductImage())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == FullType) {
            val binding: AdapterProductAddImageBinding =
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.adapter_product_add_image,
                    parent,
                    false
                )
            return ViewHolder(binding, baseViewModel)
        } else {
            val binding: AdapterProductAddImageEmptyBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.adapter_product_add_image_empty,
                parent,
                false
            )

            return EmptyViewHolder(binding, baseViewModel)
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is EmptyViewHolder)
            holder.bind()
        else if (holder is AddProductImageAdapter.ViewHolder)

            holder.bind(productList[position])


    }

    override fun getItemCount(): Int {
        return if (::productList.isInitialized) productList.size else 0
    }

    fun updateproductList() {
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        var item = productList.get(position)
        if (item.url == null && item.suid == null)
            return EmptyType
        else
            return FullType
    }


    class ViewHolder(
        private val binding: AdapterProductAddImageBinding,
        val baseViewModel: AddProductViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        private val viewModel = AddProductImageAdapterViewModel()

        fun bind(product: Product.ProductImage) {
            viewModel.bind(product)


            binding.viewModel = viewModel
            binding.baseViewModel = baseViewModel
        }
    }

    class EmptyViewHolder(
        private val binding: AdapterProductAddImageEmptyBinding,
        val baseViewModel: AddProductViewModel
    ) : RecyclerView.ViewHolder(binding.root) {


        fun bind() {
            binding.baseViewModel = baseViewModel
        }
    }
}