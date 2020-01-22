package com.ronaker.app.ui.profilePaymentHistoryList


import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.PaymentInfoRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.PaymentCard
import com.ronaker.app.model.Transaction
import com.ronaker.app.ui.profilePaymentList.PaymentInfoAdapter
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ProfilePaymentHistoryListViewModel (app: Application): BaseViewModel(app) {


    @Inject
    lateinit
    var userRepository: UserRepository

    @Inject
    lateinit
    var paymentInfoRepository: PaymentInfoRepository

    @Inject
    lateinit
    var context: Context


    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val retry: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()


    var dataList=ArrayList<Transaction>()

    val adapter= PaymentHistoryAdapter(dataList)



    private var subscription: Disposable? = null

    fun loadData() {
        subscription = paymentInfoRepository
            .getFinancialTransactions(userRepository.getUserToken())

            .doOnSubscribe {
                retry.value = null
                loading.value = true
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


    fun logout(){
        userRepository.clearLogin()
    }



}