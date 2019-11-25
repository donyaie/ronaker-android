package com.ronaker.app.ui.addProduct

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.databinding.AdapterProductAddImageBinding
import com.ronaker.app.databinding.AdapterProductAddImageEmptyBinding
import com.ronaker.app.model.Product
import com.ronaker.app.utils.extension.getParentActivity

class AddProductImageAdapter(val baseViewModel: AddProductViewModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private  var productList: ArrayList<Product.ProductImage> = ArrayList()


    val EmptyType = 0
    val FullType = 1


    init {
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
        else if (holder is ViewHolder)

            holder.bind(productList[position])


    }

    override fun getItemCount(): Int {
        return  productList.size
    }

    fun updateproductList() {
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val item = productList[position]
        return if (item.url == null && item.uri == null)
            EmptyType
        else
            FullType
    }

    fun addLocalImage(uri: Uri?) {
        productList.add(Product.ProductImage(null, null, uri, true))
        notifyDataSetChanged()
    }


    fun addRemoteImage(imageList: List< Product.ProductImage>) {
        productList.addAll(imageList)
        notifyDataSetChanged()
    }

    fun removeItem(image: Product.ProductImage) {


        productList.remove(image)
        notifyDataSetChanged()
    }

    fun isAllUploaded(): Boolean {
        var result = true
        productList.forEach {
            if (it.isLocal)
                result = false
        }
        return result
    }

    fun getimages(): ArrayList<Product.ProductImage> {

        val list= ArrayList<Product.ProductImage>()

        if (productList.size <= 1)
            return list

        productList.forEach { list.add(it) }

        list.removeAt(0)

        return list
    }


    class ViewHolder(
        private val binding: AdapterProductAddImageBinding,
        val baseViewModel: AddProductViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        private val viewModel = AddProductImageAdapterViewModel(baseViewModel.getApplication())

        fun bind(product: Product.ProductImage) {
            binding.viewModel = viewModel
            binding.baseViewModel = baseViewModel
            viewModel.bind(product,binding.root.getParentActivity() as BaseActivity)


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