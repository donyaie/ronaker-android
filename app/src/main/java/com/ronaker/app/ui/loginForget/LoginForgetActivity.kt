package com.ronaker.app.ui.loginForget

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.ui.dashboard.DashboardActivity
import com.ronaker.app.ui.login.LoginViewModel
import com.ronaker.app.utils.Alert
import com.ronaker.app.utils.AnimationHelper
import com.ronaker.app.utils.KeyboardManager
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginForgetActivity : BaseActivity() {

    private lateinit var binding: com.ronaker.app.databinding.ActivityLoginForgetBinding
    private val viewModel: LoginForgetViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSwipeCloseDisable()
        enableKeyboardAnimator()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_forget)
        binding.viewModel = viewModel

        viewModel.goNext.observe(this, {value ->
            if (value == true) {
                startActivity(DashboardActivity.newInstance(this@LoginForgetActivity))
                AnimationHelper.setFadeTransition(this)
                finish()
                AnimationHelper.setFadeTransition(this)
            }
        })


        binding.toolbar.cancelClickListener = View.OnClickListener {

            finish()
        }


        viewModel.errorMessage.observe(this, {errorMessage ->
            Alert.makeTextError(this, errorMessage)
        })



        viewModel.keyboardDown.observe(this, {

            if (it) {
                KeyboardManager.hideSoftKeyboard(this@LoginForgetActivity)
            }
        })


        binding.passwordInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                binding.nextButton.isEnabled=viewModel.validatePassword(p0?.toString(),(binding.repeatPasswordInput.text?:""))
            }

        })


        binding.repeatPasswordInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                binding.nextButton.isEnabled=viewModel.validatePassword(binding.passwordInput.text,(p0?.toString()?:""))
            }

        })

        binding.nextButton.isEnabled=false


        handleIntent(intent)


    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        intent?.let { handleIntent(it) }
    }


    private fun handleIntent(intent: Intent) {


        intent.data?.encodedQuery?.let {
            val token = it.replace("token=", "").trim()


            viewModel.setToken(token)

        }


    }
}