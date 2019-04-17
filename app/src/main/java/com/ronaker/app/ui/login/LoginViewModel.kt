package com.ronaker.app.ui.login

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.ronaker.app.base.BaseViewModel
import io.reactivex.disposables.Disposable

class LoginViewModel: BaseViewModel(){

    private lateinit var subscription: Disposable

    enum class LoginActionEnum {
        login,
        register
    }

    enum class LoginStateEnum  constructor(position: Int) {
        home(0),
        email(1),
        info(2),
        password(3),
        login(4);

        var position: Int = 0
            internal set

        init {
            this.position = position
        }

        companion object {
            operator fun get(position: Int): LoginStateEnum {
                var state = home
                for (stateEnum in LoginStateEnum.values()) {
                    if (position == stateEnum.position)
                        state = stateEnum
                }
                return state
            }
        }
    }

    val actionState: MutableLiveData<LoginActionEnum> = MutableLiveData()
    val viewState: MutableLiveData<LoginStateEnum> = MutableLiveData()

    val loginClickListener = View.OnClickListener {


        actionState.value=LoginActionEnum.login
        viewState.value=LoginStateEnum.login
    }

    val signupClickListener = View.OnClickListener {

        actionState.value=LoginActionEnum.register
        viewState.value=LoginStateEnum.email
    }


    val emailClickListener = View.OnClickListener {

        viewState.value=LoginStateEnum.info
    }

    val infoClickListener = View.OnClickListener {

        viewState.value=LoginStateEnum.password
    }


    val passwordClickListener = View.OnClickListener {

    }

    val signClickListener=View.OnClickListener {

    }



    init{

//        actionState.value=LoginActionEnum.register
//        viewState.value=LoginStateEnum.home


    }



    override fun onCleared() {
        super.onCleared()
//        subscription?.dispose()
    }

}