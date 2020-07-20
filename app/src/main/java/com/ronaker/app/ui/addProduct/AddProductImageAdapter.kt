package com.ronaker.app.ui.addProduct

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.R
import com.ronaker.app.databinding.AdapterProductAddImageBinding
import com.ronaker.app.databinding.AdapterProductAddImageEmptyBinding
import com.ronaker.app.model.Image
import com.ronaker.app.utils.extension.getParentActivity

class AddProductImageAdapter(private val baseViewModel: AddProductViewModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var list: ArrayList<Image> = ArrayList()


    private val EmptyType = 0
    private val FullType = 1


    init {
        list.add(Image())
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

            holder.bind(list[position])


    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateproductList() {
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val item = list[position]
        return if (item.url == null && item.uri == null)
            EmptyType
        else
            FullType
    }

    fun addLocalImage(uri: Uri?) {
        list.add(
            Image(
                null,
                null,
                uri,
                true
            )
        )
        notifyDataSetChanged()
    }


    fun addRemoteImage(imageList: List<Image>) {
        list.addAll(imageList)
        notifyDataSetChanged()
    }

    fun removeItem(image: Image) {


        list.remove(image)
        notifyDataSetChanged()
    }

    fun isAllUploaded(): Boolean {
        var result = true
        list.forEach {
            if (it.isLocal)
                result = false
        }
        return result
    }

    fun getimages(): ArrayList<Image> {

        val list = ArrayList<Image>()

        if (this.list.size <= 1)
            return list

        this.list.forEach { list.add(it) }

        list.removeAt(0)

        return list
    }


    class ViewHolder(
        private val binding: AdapterProductAddImageBinding,
        private val baseViewModel: AddProductViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        private val viewModel = AddProductImageAdapterViewModel(baseViewModel.getApplication())

        fun bind(product: Image) {
            binding.viewModel = viewModel
            binding.baseViewModel = baseViewModel
            viewModel.bind(product, binding.root.getParentActivity())


        }


    }

    class EmptyViewHolder(
        private val binding: AdapterProductAddImageEmptyBinding,
        private val baseViewModel: AddProductViewModel
    ) : RecyclerView.ViewHolder(binding.root) {


        fun bind() {
            binding.baseViewModel = baseViewModel
        }


    }
}