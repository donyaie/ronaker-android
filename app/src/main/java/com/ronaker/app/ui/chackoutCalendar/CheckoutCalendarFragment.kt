package com.ronaker.app.ui.chackoutCalendar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.model.Product
import com.ronaker.app.utils.Alert
import com.ronaker.app.utils.extension.finishSafe
import com.savvi.rangedatepicker.CalendarPickerView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CheckoutCalendarFragment : BaseFragment() {

    private lateinit var binding: com.ronaker.app.databinding.FragmentCheckoutCalendarBinding
    private lateinit var viewModel: CheckoutCalendarViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_checkout_calendar, container, false)






        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(CheckoutCalendarViewModel::class.java)
        binding.viewModel = viewModel



        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            Alert.makeTextError(this, errorMessage)
        })



        viewModel.nextStep.observe(viewLifecycleOwner, Observer {

            //

            if (viewModel.startDate != null && viewModel.endDate != null) {

                val intent = Intent()


                viewModel.startDate?.let { date -> intent.putExtra(STARTDATE_KEY, date.time) }
                viewModel.endDate?.let { date -> intent.putExtra(ENDDATE_KEY, date.time) }

                requireActivity().setResult(Activity.RESULT_OK, intent)





                requireActivity().finishSafe()
            }


        })



        binding.toolbar.actionTextClickListener = View.OnClickListener {
            binding.calendarView.clearSelectedDates()

            viewModel.clearDates()
        }

        binding.toolbar.cancelClickListener = View.OnClickListener {

            activity?.finishSafe()
        }


        if (arguments?.containsKey(PRODUCT_KEY) == true) {


            viewModel.loadProduct()


        } else {
            activity?.finishSafe()
        }


        initCalendar()

    }


    private fun initCalendar() {


        val nextYear: Calendar = Calendar.getInstance()
        nextYear.add(Calendar.YEAR, 1)

        val lastYear: Calendar = Calendar.getInstance()
//        lastYear.add(Calendar.DAY_OF_MONTH, - 5)
//
//        val list = ArrayList<Int>()
//
//        list.add(2)
//
//        binding.calendarView.deactivateDates(list)
        val arrayList = ArrayList<Date>()
        try {
//            val dateformat = SimpleDateFormat("dd-MM-yyyy")

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


        binding.calendarView.init(
            lastYear.time,
            nextYear.time,
            SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        ) //
            .inMode(CalendarPickerView.SelectionMode.RANGE) //
//            .withDeactivateDates(list)
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


    fun getProduct(): Product? {
        return arguments?.getParcelable(PRODUCT_KEY)
    }


    companion object {

        const val PRODUCT_KEY = "mProduct"
        const val STARTDATE_KEY = "start_date"
        const val ENDDATE_KEY = "end_date"
        const val REQUEST_CODE = 346

        fun newBoundle(product: Product): Bundle {
            val boundle = Bundle()
            boundle.putParcelable(PRODUCT_KEY, product)

            return boundle
        }
    }


}
