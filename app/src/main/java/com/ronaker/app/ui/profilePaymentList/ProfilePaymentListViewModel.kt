package com.ronaker.app.ui.profilePaymentList


import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.PaymentInfoRepository
import com.ronaker.app.model.PaymentCard
import io.reactivex.disposables.Disposable

class ProfilePaymentListViewModel @ViewModelInject constructor(
    private val paymentInfoRepository: PaymentInfoRepository
)  : BaseViewModel() {


    var dataList = ArrayList<PaymentCard>()

    val adapter = PaymentInfoAdapter(dataList)


    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val retry: MutableLiveData<String?> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()


    private var subscription: Disposable? = null

    fun loadData() {

        subscription = paymentInfoRepository
            .getPaymentInfoList()

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