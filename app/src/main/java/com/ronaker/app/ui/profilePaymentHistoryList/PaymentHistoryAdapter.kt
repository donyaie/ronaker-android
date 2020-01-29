package com.ronaker.app.ui.profilePaymentHistoryList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.R
import com.ronaker.app.databinding.AdapterPaymentHistoryBinding
import com.ronaker.app.databinding.AdapterPaymentSelectBinding
import com.ronaker.app.model.PaymentCard
import com.ronaker.app.model.Transaction
import com.ronaker.app.ui.orderPreview.OrderPreviewActivity
import com.ronaker.app.utils.extension.getApplication
import com.ronaker.app.utils.extension.getParentActivity
import com.ronaker.app.utils.extension.startActivityMakeScene

class PaymentHistoryAdapter(
    dataList: ArrayList<Transaction>
) : RecyclerView.Adapter<PaymentHistoryAdapter.ViewHolder>() {
    private var productList: List<Transaction> = dataList


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val binding: AdapterPaymentHistoryBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.adapter_payment_history,
                parent,
                false
            )
        return ViewHolder(binding, this)


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(productList[position])
    }

    override fun getItemCount(): Int {
        return productList.size
    }


    fun updateList() {
        notifyDataSetChanged()
    }


    override fun onViewRecycled(holder: ViewHolder) {

        holder.onRecycled()
        super.onViewRecycled(holder)
    }


    class ViewHolder(
        private val binding: AdapterPaymentHistoryBinding,
        val adapter: PaymentHistoryAdapter
    ) : RecyclerView.ViewHolder(binding.root) {

        private val viewModel = PaymentHistoryViewModel(binding.root.getApplication())

        fun bind(item: Transaction) {
            viewModel.bind(item)
            binding.viewModel = viewModel

            binding.container.setOnClickListener{

               item.OrderSuid?.let {   binding.root.getParentActivity()?.startActivityMakeScene(OrderPreviewActivity.newInstance(binding.root.getApplication(),it))}

            }



        }



        fun onRecycled() {


        }


    }





}