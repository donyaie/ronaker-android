package com.ronaker.app.ui.orderPreview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.R
import com.ronaker.app.databinding.AdapterOrderPreviewPriceBinding
import com.ronaker.app.databinding.AdapterOrderPreviewPriceTotalBinding
import com.ronaker.app.model.Order
import com.ronaker.app.utils.extension.getApplication

class OrderPreviewPriceAdapter(
    dataList: ArrayList<Order.OrderPrices>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var productList: List<Order.OrderPrices> = dataList


    private val PriceType = 0
    private val TotalType = 1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        if (viewType == PriceType) {
            val binding: AdapterOrderPreviewPriceBinding =
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.adapter_order_preview_price,
                    parent,
                    false
                )
            return ViewHolder(binding)
        } else {
            val binding: AdapterOrderPreviewPriceTotalBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.adapter_order_preview_price_total,
                parent,
                false
            )

            return TotalViewHolder(binding)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TotalViewHolder)
            holder.bind(productList[position])
        else if (holder is ViewHolder)
            holder.bind(productList[position])
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun updateproductList() {
        notifyDataSetChanged()
    }


    override fun getItemViewType(position: Int): Int {
        val item = productList[position]
        return if (Order.OrderPriceEnum[item.key] == Order.OrderPriceEnum.Total)
            TotalType
        else
            PriceType
    }


    class ViewHolder(
        private val binding: AdapterOrderPreviewPriceBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val viewModel = OrderPreviewPriceViewModel(binding.root.getApplication())

        fun bind(product: Order.OrderPrices) {
            viewModel.bind(product)
            binding.viewModel = viewModel
        }
    }


    class TotalViewHolder(
        private val binding: AdapterOrderPreviewPriceTotalBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val viewModel = OrderPreviewPriceViewModel(binding.root.getApplication())

        fun bind(product: Order.OrderPrices) {
            viewModel.bind(product)
            binding.viewModel = viewModel
        }

    }
}