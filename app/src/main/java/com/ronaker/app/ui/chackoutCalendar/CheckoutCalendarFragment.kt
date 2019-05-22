package com.ronaker.app.ui.chackoutCalendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.savvi.rangedatepicker.CalendarPickerView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CheckoutCalendarFragment : BaseFragment() {

    private lateinit var binding: com.ronaker.app.databinding.FragmentCheckoutCalendarBinding
    private lateinit var viewModel: CheckoutCalendarViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_checkout_calendar, container, false)

        activity?.let {
            viewModel = ViewModelProviders.of(it).get(CheckoutCalendarViewModel::class.java)
            binding.viewModel = viewModel
        }






        var nextYear: Calendar = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

         var lastYear:Calendar = Calendar.getInstance();
//        lastYear.add(Calendar.DAY_OF_MONTH, - 5);
//
//        val list = ArrayList<Int>()
//
//        list.add(2)
//
//        binding.calendarView.deactivateDates(list)
        val arrayList = ArrayList<Date>()
        try {
            val dateformat = SimpleDateFormat("dd-MM-yyyy")

//            val strdate = "22-5-2019"
//            val strdate2 = "26-5-2019"
//
//            val newdate = dateformat.parse(strdate)
//            val newdate2 = dateformat.parse(strdate2)
//            arrayList.add(newdate)
//            arrayList.add(newdate2)
        } catch (e: ParseException) {
            e.printStackTrace()
        }


        binding.calendarView.init(lastYear.time, nextYear.time,SimpleDateFormat("MMMM yyyy", Locale.getDefault())) //
            .inMode(CalendarPickerView.SelectionMode.RANGE) //
//            .withDeactivateDates(list)
            .withHighlightedDates(arrayList)

        binding.calendarView.scrollToDate(Date())


        return binding.root
    }



}
