package com.ronaker.app.ui.orderCreate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.utils.view.IPagerFragment
import com.savvi.rangedatepicker.CalendarPickerView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderCheckoutFragment : BaseFragment(), IPagerFragment {


    private lateinit var binding: com.ronaker.app.databinding.FragmentOrderCheckoutBinding
    private lateinit var viewModel: OrderCreateViewModel



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_checkout, container, false)


        activity?.let {
            viewModel = ViewModelProvider(it).get(OrderCreateViewModel::class.java)
            binding.viewModel = viewModel

        }

//

        viewModel.clearData.observe(viewLifecycleOwner, Observer {
            binding.calendarView.clearSelectedDates()
        })





        initCalendar()
        return binding.root
    }




    private fun initCalendar() {


        val nextYear: Calendar = Calendar.getInstance()
        nextYear.add(Calendar.YEAR, 1)

        val lastYear: Calendar = Calendar.getInstance()
        val arrayList = ArrayList<Date>()
        try {
        } catch (e: ParseException) {
            e.printStackTrace()
        }


        binding.calendarView.init(
            lastYear.time,
            nextYear.time,
            SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        ) //
            .inMode(CalendarPickerView.SelectionMode.RANGE)
            .withHighlightedDates(arrayList)

        binding.calendarView.setOnDateSelectedListener(object :
            CalendarPickerView.OnDateSelectedListener {
            override fun onDateSelected(date: Date) {

                viewModel.updateDate(binding.calendarView.selectedDates)

            }

            override fun onDateUnselected(date: Date) {

                viewModel.updateDate(binding.calendarView.selectedDates)
            }

        })




        binding.calendarView.scrollToDate(Date())
    }



    companion object {

        fun newInstance(): OrderCheckoutFragment {
            return OrderCheckoutFragment()
        }
    }

    override fun onSelect() {

    }

}