package com.ronaker.app.ui.notificationHistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.onesignal.OSNotificationAction
import com.onesignal.OneSignal
import com.onesignal.OneSignalHelper
import com.ronaker.app.R
import com.ronaker.app.databinding.AdapterNotificationHistoryBinding
import com.ronaker.app.utils.extension.getParentActivity
import org.json.JSONObject

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

                item.ANDROID_NOTIFICATION_ID?.let { it1 -> OneSignal.cancelNotification(it1) }
                val data = try {
                    item.FULL_DATA?.let {

                        val custom=JSONObject(item.FULL_DATA).getString("custom")
                        JSONObject(custom).getJSONObject("a")
                    }

                } catch (ex: Exception) {
                    null
                }
                binding.container.getParentActivity()?.let {activity->

                    data?.let {

                        OneSignalHelper.handleNotificationClick(
                            context = activity,
                            data = it,
                            OSNotificationAction.ActionType.Opened,
                            isLogin = true
                        )

                    }



                }



            }


        }


        fun onRecycled() {


        }


    }


}