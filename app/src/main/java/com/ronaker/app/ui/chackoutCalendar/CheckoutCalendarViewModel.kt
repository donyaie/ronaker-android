package com.ronaker.app.ui.chackoutCalendar


import android.app.Application
import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.data.UserRepository
import io.reactivex.disposables.Disposable
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class CheckoutCalendarViewModel (app: Application): BaseViewModel(app) {

    @Inject
    lateinit
    var productRepository: ProductRepository


    @Inject
    lateinit
    var userRepository: UserRepository

    @Inject
    lateinit
    var context: Context

    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()


    val firstDayVisibility: MutableLiveData<Int> = MutableLiveData()
    val lastDayVisibility: MutableLiveData<Int> = MutableLiveData()

    val firstDay: MutableLiveData<String> = MutableLiveData()
    val firstDate: MutableLiveData<String> = MutableLiveData()
    val lastDay: MutableLiveData<String> = MutableLiveData()
    val lastDate: MutableLiveData<String> = MutableLiveData()

    val nextStep: MutableLiveData<String> = MutableLiveData()

    val setDatesVisibility: MutableLiveData<Int> = MutableLiveData()


    var startDate:Date?=null
    var endDate:Date?=null





    private var subscription: Disposable? = null

    init {
        clearDates()

    }


    fun loadProduct() {

    }

    fun onClickSetDays() {

        nextStep.value=""
    }

    private fun unSelectFirstDate() {

        firstDayVisibility.value = View.GONE

        firstDate.value = context.getString(R.string.title_borrow_day)

    }

    private fun unSelectLastDate() {


        lastDayVisibility.value = View.GONE

        lastDate.value = context.getString(R.string.title_return_day)

        setDatesVisibility.value = View.GONE

    }

    private fun selectFirst(date: Date) {

        firstDayVisibility.value = View.VISIBLE

        firstDay.value = SimpleDateFormat("EEEE,", Locale.getDefault()).format(date)
        firstDate.value = SimpleDateFormat("dd MMMM", Locale.getDefault()).format(date)

        startDate=date

    }

    private fun selectLast(date: Date) {

        lastDayVisibility.value = View.VISIBLE

        lastDay.value = SimpleDateFormat("EEEE", Locale.getDefault()).format(date)
        lastDate.value = SimpleDateFormat("dd MMMM", Locale.getDefault()).format(date)

        setDatesVisibility.value = View.VISIBLE


        endDate=date
    }

    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }

    fun updateDate(selectedDates: List<Date>) {

        when {
            selectedDates.isEmpty() -> {
                unSelectFirstDate()
                unSelectLastDate()
            }
            selectedDates.size == 1 -> {
                selectFirst(selectedDates[0])
                unSelectLastDate()
            }
            else -> {
                selectFirst(selectedDates[0])
                selectLast(selectedDates[selectedDates.size - 1])
            }
        }


    }

    fun clearDates() {
        updateDate(ArrayList())

    }


}