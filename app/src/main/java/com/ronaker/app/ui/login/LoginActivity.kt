package com.ronaker.app.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.ui.dashboard.DashboardActivity
import com.ronaker.app.ui.splash.SplashActivity
import com.ronaker.app.utils.AnimationHelper
import com.ronaker.app.utils.KeyboardManager
import com.ronaker.app.utils.ScreenCalcute
import com.ronaker.app.utils.view.ToolbarComponent


class LoginActivity : BaseActivity() {

    private val TAG = LoginActivity::class.java.simpleName

    private lateinit var binding: com.ronaker.app.databinding.ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    var Max_size = 5

    private lateinit var screenLibrary: ScreenCalcute


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
            }

        }

    companion object {
        fun newInstance(context: Context): Intent {
            var intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_SINGLE_TOP
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AnimationHelper.setFadeTransition(this)
        super.onCreate(savedInstanceState)



        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        screenLibrary = ScreenCalcute(this)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        binding.viewModel = viewModel



        viewModel.actionState.observe(this, Observer { action ->
            loginAction = action

        })

        viewModel.viewState.observe(this, Observer { state ->
            loginState = state

        })

        binding.toolbar.centerContainer = ToolbarComponent.CenterContainer.DOTS
        binding.toolbar.showNavigator(false, 0)


        binding.toolbar.cancelClickListener = View.OnClickListener { prePage() }


        binding.background.layoutParams.width = (screenLibrary.screenWidthPixel * 1.2).toInt()

        binding.scrollView.setOnTouchListener { _, _ -> true }


        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        })

        viewModel.goNext.observe(this, Observer { value ->
            if (value == true) {
                startActivity(DashboardActivity.newInstance(this@LoginActivity))
                finish()
            }
        })

        viewModel.loading.observe(this, Observer { value ->
            if (value == true) {
                binding.loading.visibility=View.VISIBLE
                binding.loading.showLoading()
            } else
                binding.loading.hideLoading()
        })


        init()
        loginAction = LoginViewModel.LoginActionEnum.register
        loginState = LoginViewModel.LoginStateEnum.home
    }



    private fun init() {


        overlayShow(false)
        showBack(false)
        initViewPager()


        binding.loading.hideLoading()


    }


    private fun prePage() {

        if (currentPosition - 1 == LoginViewModel.LoginStateEnum.home.position)
            KeyboardManager.hideSoftKeyboard(this)


        if (currentPosition == 0)
            finishSafe()
        else if (currentPosition == LoginViewModel.LoginStateEnum.login.position) {
            currentPosition = 0
        } else if (currentPosition > LoginViewModel.LoginStateEnum.home.position) {
            currentPosition -= 1
        }

    }


   private fun getFragment(state: LoginViewModel.LoginStateEnum): Fragment {
        return when (state) {
            LoginViewModel.LoginStateEnum.home -> LoginHomeFragment()
            LoginViewModel.LoginStateEnum.email -> LoginEmailFragment()
            LoginViewModel.LoginStateEnum.password -> LoginPasswordFragment()
            LoginViewModel.LoginStateEnum.info -> LoginNameFragment()
            LoginViewModel.LoginStateEnum.login -> LoginSignInFragment()
        }
    }

   private var currentPosition: Int = 0
        get() = field
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
                        );
                        ft.replace(R.id.frame_container, getFragment(state), state.name)
                            .addToBackStack(state.name)
                        ft.commit()
                    }
                    value < field -> //back
                        fm.popBackStack(state.name, 0)
                    else -> {
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        ft.replace(R.id.frame_container, getFragment(state), state.name)
                            .addToBackStack(state.name)
                        ft.commit()
                    }
                }

//                val x = ((binding.frameContainer.width*value) * computeFactor()).toInt()

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


    private fun initViewPager() {

//
//        homeFragment = LoginHomeFragment()
//        passwordFragment = LoginPasswordFragment()
//        signInFragment = LoginSignInFragment()
//        nameFragment = LoginNameFragment()
//        emailFragment = LoginEmailFragment()
//
//        fragmentList = ArrayList()
//
//        fragmentList.add(homeFragment)
//        fragmentList.add(emailFragment)
//        fragmentList.add(nameFragment)
//        fragmentList.add(passwordFragment)
//        fragmentList.add(signInFragment)


//
//        binding.viewpager.adapter = adapter
//        binding.viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//            override fun onPageScrolled(
//                position: Int,
//                positionOffset: Float,
//                positionOffsetPixels: Int
//            ) {
//                val x =
//                    ((binding.viewpager.width * position + positionOffsetPixels) * computeFactor()).toInt()
//                binding.scrollView.scrollTo(x, 0)
//            }
//
//            override fun onPageSelected(position: Int) {
//
//                loginState = if (position == 1) {
//                    if (loginAction == LoginViewModel.LoginActionEnum.login) LoginViewModel.LoginStateEnum.login else LoginViewModel.LoginStateEnum.email
//                } else
//                    LoginViewModel.LoginStateEnum.get(position)
//
//
//                Debug.Log(TAG, String.format("onSelect:%s", loginState.name))
//                (adapter.getItem(position) as IPagerFragment).onSelect()
//
//
//
//                if (loginState == LoginViewModel.LoginStateEnum.home) {
//                    overlayShow(false)
//                    KeyboardManager.hideSoftKeyboard(this@LoginActivity)
//                    showBack(false)
//                } else {
//                    overlayShow(true)
//                    showBack(true)
//                }
//
//
//                when {
//                    loginState == LoginViewModel.LoginStateEnum.home -> binding.toolbar.showNavigator(
//                        false,
//                        0
//                    )
//                    loginAction == LoginViewModel.LoginActionEnum.register -> binding.toolbar.showNavigator(
//                        true,
//                        position - 1
//                    )
//                    loginAction == LoginViewModel.LoginActionEnum.login -> binding.toolbar.showNavigator(
//                        false,
//                        0
//                    )
//                }
//
//
//            }
//
//            override fun onPageScrollStateChanged(state: Int) {
//
//            }
//
//            private fun computeFactor(): Float {
//                return (binding.background.width - binding.frameContainer.width) / ((binding.frameContainer.width * ((binding.viewpager.adapter?.count
//                    ?: 0) - 1)) * 1.0f)
//            }
//        })

//        KeyboardManager.hideSoftKeyboard(this)
    }

    override fun onDestroy() {
        super.onDestroy()
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
            binding.overlayLayout.animate().alpha(0.2f).setDuration(500).start()

        }
    }


}