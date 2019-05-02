package com.ronaker.app.ui.addProduct

import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import com.ronaker.app.data.PostRepository
import com.ronaker.app.data.ProductRepository
import com.ronaker.app.data.UserRepository
import com.ronaker.app.model.User
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class AddProductViewModel : BaseViewModel() {

    internal val TAG = AddProductViewModel::class.java.name


    @Inject
    lateinit var userRepository: UserRepository


    @Inject
    lateinit var productRepository: ProductRepository

    private var createPostSubscription: Disposable? = null

    var adapter: AddProductImageAdapter


    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()

    val goNext: MutableLiveData<Boolean> = MutableLiveData()


    val showPickerNext: MutableLiveData<Boolean> = MutableLiveData()

    var userInfo: User = User()

    var emailError = MutableLiveData<Boolean?>()

    enum class StateEnum constructor(position: Int) {
        image(0),
        info(1),
        price(2),
        location(3);

        var position: Int = 0
            internal set

        init {
            this.position = position
        }

        companion object {
            operator fun get(position: Int): StateEnum {
                var state = image
                for (stateEnum in StateEnum.values()) {
                    if (position == stateEnum.position)
                        state = stateEnum
                }
                return state
            }
        }
    }

    fun onClickImageNext() {
        viewState.value = StateEnum.info


    }

    fun onClickInfoNext() {

        viewState.value = StateEnum.price
    }

    fun onClickPriceNext() {

        viewState.value = StateEnum.location

    }

    fun onClickLocationNext() {

    }


    fun onClickRemoveImage(id: String?) {


    }

    fun onClickAddImage() {
        showPickerNext.value=true
    }


    val viewState: MutableLiveData<StateEnum> = MutableLiveData()


    init {
        adapter = AddProductImageAdapter(this)

    }


    override fun onCleared() {
        super.onCleared()
        createPostSubscription?.dispose()
    }

}