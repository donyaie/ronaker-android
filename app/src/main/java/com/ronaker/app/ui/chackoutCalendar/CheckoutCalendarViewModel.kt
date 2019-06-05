package com.ronaker.app.ui.chackoutCalendar


import android.view.View
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.data.UserRepository
import io.reactivex.disposables.Disposable
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class CheckoutCalendarViewModel : BaseViewModel() {

    @Inject
    lateinit
    var productRepository: ProductRepository


    @Inject
    lateinit
    var userRepository: UserRepository


    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()


    val firstDayVisibility: MutableLiveData<Int> = MutableLiveData()
    val lastDayVisibility: MutableLiveData<Int> = MutableLiveData()

    val firstDay: MutableLiveData<String> = MutableLiveData()
    val firstDate: MutableLiveData<String> = MutableLiveData()
    val lastDay: MutableLiveData<String> = MutableLiveData()
    val lastDate: MutableLiveData<String> = MutableLiveData()

    val setDatesVisibility: MutableLiveData<Int> = MutableLiveData()


    lateinit var suid: String


    private var subscription: Disposable? = null

    init {
        clearDates()

    }


    fun loadProduct(suid: String) {

        this.suid = suid


    }

    fun onClickSetDays(){

    }

    fun unSelectFirstDate() {

        firstDayVisibility.value= View.GONE

        firstDate.value="Borrow Day"

    }

    fun unSelectLastDate() {


        lastDayVisibility.value= View.GONE

        lastDate.value="Return Day"

        setDatesVisibility.value=View.GONE

    }

    fun selectFirst(date: Date) {

        firstDayVisibility.value= View.VISIBLE

        firstDay.value= SimpleDateFormat("EEEE,").format(date)
        firstDate.value=SimpleDateFormat("dd MMMM").format(date)

    }

    fun selectLast(date: Date) {

        lastDayVisibility.value= View.VISIBLE

        lastDay.value= SimpleDateFormat("EEEE").format(date)
        lastDate.value=SimpleDateFormat("dd MMMM").format(date)

        setDatesVisibility.value=View.VISIBLE
    }

    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }

    fun UpdateDate(selectedDates: List<Date>) {

        if (selectedDates.isEmpty()) {
            unSelectFirstDate()
            unSelectLastDate()
        } else if (selectedDates.size == 1) {
            selectFirst(selectedDates[0])
            unSelectLastDate()
        } else {
            selectFirst(selectedDates[0])
            selectLast(selectedDates[selectedDates.size - 1])
        }


    }

    fun clearDates() {
        UpdateDate(ArrayList())

    }


}