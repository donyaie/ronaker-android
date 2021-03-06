package com.ronaker.app.ui.profilePaymentHistoryList


import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.PaymentInfoRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.Transaction
import com.ronaker.app.utils.toCurrencyFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@HiltViewModel
class ProfilePaymentHistoryListViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val paymentInfoRepository: PaymentInfoRepository
)  : BaseViewModel() {


    val errorMessage: MutableLiveData<String> = MutableLiveData()

    val walletBalance: MutableLiveData<String> = MutableLiveData()

    val retry: MutableLiveData<String?> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()


    var dataList = ArrayList<Transaction>()

    val adapter = PaymentHistoryAdapter(dataList)


    private var subscription: Disposable? = null

    fun loadData() {

        userRepository.getUserInfo()?.let {
            walletBalance.value = ((it.balance?:0.0) / 100.0).toCurrencyFormat()

        }


        subscription = paymentInfoRepository
            .getFinancialTransactions()

            .doOnSubscribe {
                retry.postValue( "")
                loading.postValue( true)
            }
            .doOnTerminate {
                loading.value = false
            }

            .subscribe { result ->
                if (result.isSuccess()) {


                    dataList.clear()
                    result.data?.let { dataList.addAll(it) }
                    adapter.notifyDataSetChanged()


                } else {
                    retry.value = result.error?.message
                }
            }

    }


    fun onRetry() {
        loadData()
    }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }


}