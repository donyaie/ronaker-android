package com.ronaker.app.ui.profilePaymentList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.R
import com.ronaker.app.databinding.AdapterPaymentSelectBinding
import com.ronaker.app.model.PaymentCard
import com.ronaker.app.utils.extension.getApplication

class PaymentSelectAdapter(
    dataList: ArrayList<PaymentCard>
) : RecyclerView.Adapter<PaymentSelectAdapter.ViewHolder>() {
    private var productList: List<PaymentCard> = dataList


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val binding: AdapterPaymentSelectBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.adapter_payment_select,
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

    fun selectItem(item: PaymentCard) {


        productList.forEach {

            it.selected = false
        }

        item.selected = true

        updateList()

    }

    override fun onViewRecycled(holder: ViewHolder) {

        holder.onRecycled()
        super.onViewRecycled(holder)
    }


    class ViewHolder(
        private val binding: AdapterPaymentSelectBinding,
        val adapter: PaymentSelectAdapter
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private val viewModel = PaymentSelectViewModel(binding.root.getApplication())

        fun bind(item: PaymentCard) {
            viewModel.bind(item)
            binding.viewModel = viewModel

            binding.container.setOnClickListener(this)


            binding.checkbox.setOnCheckedChangeListener {_,ischeck->

                if(ischeck)
                adapter.selectItem(item)
            }
        }



        fun onRecycled() {


        }

        override fun onClick(v: View?) {
//            binding.checkbox.isChecked = !binding.checkbox.isChecked
            binding.checkbox.performClick()
        }


    }





}