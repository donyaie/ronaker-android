package com.ronaker.app.ui.profilePaymentList


import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.PaymentInfoRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.PaymentCard
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ProfilePaymentListViewModel(app: Application) : BaseViewModel(app) {


    @Inject
    lateinit
    var userRepository: UserRepository

    @Inject
    lateinit
    var paymentInfoRepository: PaymentInfoRepository

    @Inject
    lateinit
    var context: Context

    var dataList = ArrayList<PaymentCard>()

    val adapter = PaymentInfoAdapter(dataList)


    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val retry: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()


    private var subscription: Disposable? = null

    fun loadData() {

        subscription = paymentInfoRepository
            .getPaymentInfoList()

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


}