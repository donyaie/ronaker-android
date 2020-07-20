package com.ronaker.app.ui.orderCreate

import android.app.Application
import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.R
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.OrderRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Product
import com.ronaker.app.utils.toCurrencyFormat
import io.reactivex.disposables.Disposable
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class OrderCreateViewModel(app: Application) : BaseViewModel(app) {


    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var orderRepository: OrderRepository

    val viewState: MutableLiveData<StateEnum> = MutableLiveData()


    val goNext: MutableLiveData<Boolean> = MutableLiveData()

    val goValidate: MutableLiveData<Boolean> = MutableLiveData()


    val loadingButton: MutableLiveData<Boolean> = MutableLiveData()


    @Inject
    lateinit
    var context: Context

    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()


    val clearData: MutableLiveData<Boolean> = MutableLiveData()


    val firstDayVisibility: MutableLiveData<Int> = MutableLiveData()
    val lastDayVisibility: MutableLiveData<Int> = MutableLiveData()

    val firstDay: MutableLiveData<String> = MutableLiveData()
    val firstDate: MutableLiveData<String> = MutableLiveData()
    val lastDay: MutableLiveData<String> = MutableLiveData()
    val lastDate: MutableLiveData<String> = MutableLiveData()


    val setDatesVisibility: MutableLiveData<Int> = MutableLiveData()


    val successMessage: MutableLiveData<Boolean> = MutableLiveData()


    val productPrice: MutableLiveData<String> = MutableLiveData()
    val orderMessage: MutableLiveData<String> = MutableLiveData()

    val productPriceTitle: MutableLiveData<String> = MutableLiveData()


    private lateinit var mProduct: Product

    private var mPrice = 0.0

    var startDate: Date? = null
    var endDate: Date? = null

    private var subscription: Disposable? = null

    init {
        clearDates()

    }

    fun init(product: Product) {

        mProduct = product

    }


    enum class StateEnum constructor(position: Int) {
        Checkout(0),
        Message(1);

        var position: Int = 0
            internal set

        init {
            this.position = position
        }

        companion object {
            operator fun get(position: Int): StateEnum {
                var state = Checkout
                for (stateEnum in values()) {
                    if (position == stateEnum.position)
                        state = stateEnum
                }
                return state
            }
        }
    }


    fun loadProduct() {


        val days = TimeUnit.DAYS.convert(
            (endDate?.time ?: 0) -
                    (startDate?.time ?: 0),
            TimeUnit.MILLISECONDS
        )

        mPrice = (mProduct.price_per_day ?: 0.toDouble()) * days

        productPriceTitle.value = "for $days days"
        productPrice.value = mPrice.toCurrencyFormat()


        val user = userRepository.getUserInfo()

        orderMessage.value = context.getString(
            R.string.text_order_message,
            user?.first_name,
            user?.last_name,
            mProduct.name,
            days.toString()
        )


    }


    fun checkOut(message: String) {


        subscription?.dispose()
        mProduct.suid?.let {
            subscription =
                orderRepository.createOrder(
                    userRepository.getUserToken(),
                    it,
                    startDate!!,
                    endDate!!,
                    message,
                    mPrice * 100
                )
                    .doOnSubscribe { loadingButton.value = true }
                    .doOnTerminate { loadingButton.value = false }
                    .subscribe { result ->
                        if (result.isSuccess() || result.isAcceptable()) {
                            successMessage.value = true


                        } else {

                            if (result.error?.responseCode == 406)
                                goValidate.value = true
                            else
                                errorMessage.value = result.error?.message
                        }
                    }


        }


    }


    fun onClickSetDays() {

        viewState.value = StateEnum.Message



        loadProduct()


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

        startDate = date

    }

    private fun selectLast(date: Date) {

        lastDayVisibility.value = View.VISIBLE

        lastDay.value = SimpleDateFormat("EEEE", Locale.getDefault()).format(date)
        lastDate.value = SimpleDateFormat("dd MMMM", Locale.getDefault()).format(date)

        setDatesVisibility.value = View.VISIBLE


        endDate = date
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
        clearData.value = true

    }


    override fun onCleared() {
        super.onCleared()

        subscription?.dispose()
    }


}