package com.ronaker.app.ui.chackoutCalendar

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.ronaker.app.utils.Alert
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.model.Product
import com.ronaker.app.ui.profileCompleteEdit.ProfileCompleteActivity
import com.ronaker.app.utils.AnimationHelper
import com.ronaker.app.utils.extension.finishSafe
import com.ronaker.app.utils.extension.startActivityMakeScene
import com.savvi.rangedatepicker.CalendarPickerView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CheckoutCalendarActivity : BaseActivity() {


    private lateinit var binding: com.ronaker.app.databinding.ActivityCheckoutCalendarBinding
    private lateinit var viewModel: CheckoutCalendarViewModel



    companion object {

        const val PRODUCT_KEY = "mProduct"
        const val STARTDATE_KEY = "start_date"
        const val ENDDATE_KEY = "end_date"
        const val REQUEST_CODE = 346


        fun newInstance(context: Context,
                        product: Product): Intent {
            val intent = Intent(context, CheckoutCalendarActivity::class.java)
            val boundle = Bundle()
            boundle.putParcelable(PRODUCT_KEY, product)
            intent.putExtras(boundle)

            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        AnimationHelper.setSlideTransition(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_checkout_calendar)

        viewModel = ViewModelProvider(this).get(CheckoutCalendarViewModel::class.java)

        binding.viewModel = viewModel



        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            Alert.makeTextError(this, errorMessage)
        })



        viewModel.nextStep.observe(this, Observer {

            //

            if (viewModel.startDate != null && viewModel.endDate != null) {

                val intent = Intent()


                viewModel.startDate?.let { date -> intent.putExtra(STARTDATE_KEY, date.time) }
                viewModel.endDate?.let { date -> intent.putExtra(ENDDATE_KEY, date.time) }

                this.setResult(Activity.RESULT_OK, intent)





                this.finishSafe()
            }


        })



        binding.toolbar.actionTextClickListener = View.OnClickListener {
            binding.calendarView.clearSelectedDates()

            viewModel.clearDates()
        }

        binding.toolbar.cancelClickListener = View.OnClickListener {

            finishSafe()
        }


        if (intent?.hasExtra(PRODUCT_KEY) == true) {


            viewModel.loadProduct()


        } else {
            finishSafe()
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
        return intent?.getParcelableExtra(PRODUCT_KEY)
    }

}