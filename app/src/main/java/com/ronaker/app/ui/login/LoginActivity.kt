package com.ronaker.app.ui.login

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.ui.dashboard.DashboardActivity
import com.ronaker.app.utils.Alert
import com.ronaker.app.utils.AnimationHelper
import com.ronaker.app.utils.KeyboardManager
import com.ronaker.app.utils.ScreenCalculator
import com.ronaker.app.utils.view.ToolbarComponent
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : BaseActivity() {

//    private val TAG = LoginActivity::class.java.simpleName

    private lateinit var binding: com.ronaker.app.databinding.ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private val Max_size = 6

    private lateinit var screenLibrary: ScreenCalculator


    private var loginAction = LoginViewModel.LoginActionEnum.register
        set(value) {

            field = value
            when (loginAction) {
                LoginViewModel.LoginActionEnum.login -> {


                }
                LoginViewModel.LoginActionEnum.register -> {

                }
            }
        }

    private var loginState = LoginViewModel.LoginStateEnum.home
        set(value) {
            field = value

            when (loginState) {

                LoginViewModel.LoginStateEnum.email -> {


//                    loginAction = LoginViewModel.LoginActionEnum.register
                    currentPosition = LoginViewModel.LoginStateEnum.email.position
                }

                LoginViewModel.LoginStateEnum.login -> {

//                    loginAction = LoginViewModel.LoginActionEnum.login
                    currentPosition = LoginViewModel.LoginStateEnum.login.position
                }
                LoginViewModel.LoginStateEnum.home -> {
                    currentPosition = LoginViewModel.LoginStateEnum.home.position
                }
                LoginViewModel.LoginStateEnum.info -> {

                    currentPosition = LoginViewModel.LoginStateEnum.info.position
                }
                LoginViewModel.LoginStateEnum.password -> {

                    currentPosition = LoginViewModel.LoginStateEnum.password.position
                }
                LoginViewModel.LoginStateEnum.forget -> {

                    currentPosition = LoginViewModel.LoginStateEnum.forget.position
                }
            }

        }

    companion object {

        var inviteCode: String? = null

        fun newInstance(context: Context): Intent {
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            return intent
        }


    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSwipeCloseDisable()
        enableKeyboardAnimator()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        screenLibrary = ScreenCalculator(this)

        binding.viewModel = viewModel



        viewModel.actionState.observe(this, { action ->
            loginAction = action

        })

        viewModel.viewState.observe(this, { state ->
            loginState = state

        })

        binding.toolbar.centerContainer = ToolbarComponent.CenterContainer.DOTS
        binding.toolbar.showNavigator(false, 0)


        binding.toolbar.cancelClickListener = View.OnClickListener { prePage() }







        binding.background.layoutParams.width = (screenLibrary.screenWidthPixel * 1.0).toInt()
        binding.background.layoutParams.height = (screenLibrary.screenHeightPixel).toInt()
        binding.bgCon.layoutParams.width = (screenLibrary.screenWidthPixel * 1.0).toInt()
        binding.bgCon.layoutParams.height = (screenLibrary.screenHeightPixel).toInt()



        binding.scrollView.setOnTouchListener { _, _ -> true }


        viewModel.errorMessage.observe(this, { errorMessage ->
            Alert.makeTextError(this, errorMessage)
        })


        viewModel.successMessage.observe(this, { errorMessage ->
            Alert.makeTextSuccess(this, errorMessage)
        })

        viewModel.goNext.observe(this, { value ->
            if (value == true) {
                startActivity(DashboardActivity.newInstance(this@LoginActivity))
                AnimationHelper.setFadeTransition(this)
                finish()
                AnimationHelper.setFadeTransition(this)
            }
        })

        viewModel.gotoSignUp.observe(this, {
            loginAction = LoginViewModel.LoginActionEnum.register
            currentPosition = 1
        })

        viewModel.checkInviteCode.observe(this, {
            inviteCode?.let {
                viewModel.setInviteCode(it)
            }
        })



        viewModel.keyboardDown.observe(this, {

            if (it) {
                KeyboardManager.hideSoftKeyboard(this@LoginActivity)
            }
        })



        viewModel.gotoSignIn.observe(this, {

            loginAction = LoginViewModel.LoginActionEnum.login
            currentPosition = LoginViewModel.LoginStateEnum.login.position
        })

        viewModel.loading.observe(this, { value ->
            if (value == true) {
                binding.loading.visibility = View.VISIBLE
                binding.loading.showLoading()
            } else
                binding.loading.hideLoading()
        })

        init()
        loginAction = LoginViewModel.LoginActionEnum.register
        loginState = LoginViewModel.LoginStateEnum.home
    }


    override fun onStart() {
        super.onStart()


        inviteCode?.let {
            viewModel.setInviteCode(it)
        }
    }

    private fun init() {


        overlayShow(false)
        showBack(false)
        binding.loading.hideLoading()

    }


    private fun prePage() {

        if (currentPosition - 1 == LoginViewModel.LoginStateEnum.home.position)
            KeyboardManager.hideSoftKeyboard(this)


        when {
            currentPosition == LoginViewModel.LoginStateEnum.home.position -> finish()
            currentPosition == LoginViewModel.LoginStateEnum.login.position -> {
                currentPosition = LoginViewModel.LoginStateEnum.home.position
            }
            currentPosition > LoginViewModel.LoginStateEnum.home.position -> {
                currentPosition -= 1
            }
        }

    }


    private fun getFragment(state: LoginViewModel.LoginStateEnum): Fragment {
        return when (state) {
            LoginViewModel.LoginStateEnum.home -> LoginHomeFragment()
            LoginViewModel.LoginStateEnum.email -> LoginEmailFragment()
            LoginViewModel.LoginStateEnum.password -> LoginPasswordFragment()
            LoginViewModel.LoginStateEnum.info -> LoginNameFragment()
            LoginViewModel.LoginStateEnum.login -> LoginSignInFragment()
            LoginViewModel.LoginStateEnum.forget -> LoginForgetFragment()
        }
    }

    private var currentPosition: Int = 0
        set(value) {

            try {


                val state = LoginViewModel.LoginStateEnum[value]
                val fm = supportFragmentManager
                val ft = fm.beginTransaction()

                when {
                    value > field -> {//next
                        ft.setCustomAnimations(
                            R.anim.fragment_right_enter,
                            R.anim.fragment_right_exit,
                            R.anim.fragment_right_pop_enter,
                            R.anim.fragment_right_pop_exit
                        )
                        ft.replace(R.id.frame_container, getFragment(state), state.name)
                            .addToBackStack(state.name)
                        ft.commit()
                    }
                    value < field -> //back
                    {

                        if (value == LoginViewModel.LoginStateEnum.home.position && field == LoginViewModel.LoginStateEnum.login.position) {
                            fm.popBackStack(state.name, 0)
                            ft.commit()
                        } else if ((field - value) > 1) {
                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            ft.replace(R.id.frame_container, getFragment(state), state.name)
                                .addToBackStack(state.name)

                            ft.commit()
                        } else {
                            fm.popBackStack(state.name, 0)
                            ft.commit()
                        }


                    }
                    else -> {
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        ft.replace(R.id.frame_container, getFragment(state), state.name)
                            .addToBackStack(state.name)
                        ft.commit()
                    }
                }

                val x = ((binding.frameContainer.width * value) * computeFactor()).toInt()


                val animator =
                    ObjectAnimator.ofInt(binding.scrollView, "scrollX", x)
                animator.duration = 800
                animator.start()

//                    ((binding.viewpager.width * position + positionOffsetPixels) * computeFactor()).toInt()
//                binding.scrollView.scrollTo(x, 0)

                when (loginAction) {
                    LoginViewModel.LoginActionEnum.register -> {
                        binding.toolbar.showNavigator(
                            true,
                            value - 1
                        )
                    }
                    LoginViewModel.LoginActionEnum.login -> {
                        binding.toolbar.showNavigator(
                            false,
                            0
                        )

                    }
                }



                if (state == LoginViewModel.LoginStateEnum.home) {
                    overlayShow(false)
                    KeyboardManager.hideSoftKeyboard(this@LoginActivity)
                    showBack(false)
                    binding.toolbar.showNavigator(
                        false,
                        0
                    )
                } else {
                    overlayShow(true)
                    showBack(true)
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            field = value
        }


    private fun computeFactor(): Float {
        return (binding.background.width - binding.frameContainer.width) / ((binding.frameContainer.width * (Max_size - 1)) * 1.0f)
    }


    override fun onBackPressed() {
        prePage()
    }


    private fun showBack(visiable: Boolean) {
        if (visiable) {
            binding.toolbar.cancelContainer = ToolbarComponent.CancelContainer.BACK
        } else {

            binding.toolbar.cancelContainer = ToolbarComponent.CancelContainer.NONE
        }
    }


    private fun overlayShow(visiable: Boolean) {
        if (visiable) {
            binding.overlayLayout.animate().alpha(0.8f).setDuration(500).start()

        } else {
            binding.overlayLayout.animate().alpha(0.5f).setDuration(500).start()

        }
    }


}