package com.ronaker.app.ui.notificationHistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.onesignal.OneSignalHelper
import com.ronaker.app.R
import com.ronaker.app.databinding.AdapterNotificationHistoryBinding
import com.ronaker.app.databinding.AdapterPaymentHistoryBinding
import com.ronaker.app.model.Transaction
import com.ronaker.app.ui.orderPreview.OrderPreviewActivity
import com.ronaker.app.utils.extension.getApplication
import com.ronaker.app.utils.extension.getParentActivity

class NotificationHistoryAdapter(
    dataList: ArrayList<OneSignalHelper.Notifications>
) : RecyclerView.Adapter<NotificationHistoryAdapter.ViewHolder>() {
    private var productList: List<OneSignalHelper.Notifications> = dataList


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val binding: AdapterNotificationHistoryBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.adapter_notification_history,
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
        private val binding: AdapterNotificationHistoryBinding,
        val adapter: NotificationHistoryAdapter
    ) : RecyclerView.ViewHolder(binding.root) {

        private val viewModel = NotificationHistoryAdapterViewModel()

        fun bind(item: OneSignalHelper.Notifications) {
            viewModel.bind(item)
            binding.viewModel = viewModel

            binding.container.setOnClickListener {

//                item.OrderSuid?.let {
//                    binding.root.getParentActivity()?.startActivity(
//                        OrderPreviewActivity.newInstance(
//                            binding.root.getApplication(),
//                            it
//                        )
//                    )
//                }

            }


        }


        fun onRecycled() {


        }


    }


}