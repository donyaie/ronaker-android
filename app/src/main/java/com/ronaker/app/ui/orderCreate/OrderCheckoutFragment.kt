package com.ronaker.app.ui.orderCreate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.utils.view.IPagerFragment
import com.savvi.rangedatepicker.CalendarPickerView
import dagger.hilt.android.AndroidEntryPoint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class OrderCheckoutFragment : BaseFragment(), IPagerFragment {


    private lateinit var binding: com.ronaker.app.databinding.FragmentOrderCheckoutBinding
    private val viewModel: OrderCreateViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_order_checkout, container, false)

        binding.viewModel = viewModel

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        viewModel.clearData.observe(viewLifecycleOwner, {
            binding.calendarView.clearSelectedDates()

            viewModel.clearSelection()
        })





        initCalendar()


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