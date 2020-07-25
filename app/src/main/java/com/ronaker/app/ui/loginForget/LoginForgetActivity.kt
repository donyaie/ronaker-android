package com.ronaker.app.ui.loginForget

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.ui.dashboard.DashboardActivity
import com.ronaker.app.utils.Alert
import com.ronaker.app.utils.AnimationHelper
import com.ronaker.app.utils.KeyboardManager

class LoginForgetActivity :BaseActivity() {

    private lateinit var binding: com.ronaker.app.databinding.ActivityLoginForgetBinding
    private lateinit var viewModel: LoginForgetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSwipeCloseDisable()
        enableKeyboardAnimator()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_forget)
        viewModel = ViewModelProvider(this).get(LoginForgetViewModel::class.java)
        binding.viewModel=viewModel

        viewModel.goNext.observe(this, Observer { value ->
            if (value == true) {
                startActivity(DashboardActivity.newInstance(this@LoginForgetActivity))
                AnimationHelper.setFadeTransition(this)
                finish()
                AnimationHelper.setFadeTransition(this)
            }
        })


        binding.toolbar.cancelClickListener= View.OnClickListener {

            finish()
        }


        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            Alert.makeTextError(this, errorMessage)
        })



        viewModel.keyboardDown.observe(this, Observer {

            if (it) {
                KeyboardManager.hideSoftKeyboard(this@LoginForgetActivity)
            }
        })


        handleIntent(intent)


    }



    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        intent?.let { handleIntent(it) }
    }


    private fun handleIntent(intent: Intent) {



        intent.data?.encodedQuery?.let {
           val token= it.replace("token=","").trim()


            viewModel.setToken(token)

        }


    }
}