package com.ronaker.app.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.ui.dashboard.DashboardActivity
import com.ronaker.app.utils.AnimationHelper
import com.ronaker.app.utils.Debug
import com.ronaker.app.utils.KeyboardManager
import com.ronaker.app.utils.ScreenCalcute
import com.ronaker.app.utils.view.IPagerFragment
import com.ronaker.app.utils.view.ToolbarComponent
import java.util.*


class LoginActivity : BaseActivity() {

    private val TAG = LoginActivity::class.java.simpleName

    private lateinit var binding: com.ronaker.app.databinding.ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    private lateinit var emailFragment: LoginEmailFragment
    private lateinit var homeFragment: LoginHomeFragment
    private lateinit var nameFragment: LoginNameFragment
    private lateinit var passwordFragment: LoginPasswordFragment
    private lateinit var signInFragment: LoginSignInFragment

    private lateinit var adapter: ViewPagerAdapter
    private lateinit var screenLibrary: ScreenCalcute



    internal var loginAction = LoginViewModel.LoginActionEnum.register
        set(value) {

            field = value
            when (loginAction) {
                LoginViewModel.LoginActionEnum.login -> {
                    initViewPagerLogin()

                }
                LoginViewModel.LoginActionEnum.register -> {
                    initViewPagerRegister()
                }
            }
        }

    internal var loginState = LoginViewModel.LoginStateEnum.home
        set(value) {
            field = value


            when (loginState) {

                LoginViewModel.LoginStateEnum.email -> {


//                    loginAction = LoginViewModel.LoginActionEnum.register
                    binding.viewpager.currentItem = LoginViewModel.LoginStateEnum.email.position
                }

                LoginViewModel.LoginStateEnum.login -> {

//                    loginAction = LoginViewModel.LoginActionEnum.login
                    binding.viewpager.currentItem = 1
                }
                LoginViewModel.LoginStateEnum.home -> {
                    binding.viewpager.currentItem = LoginViewModel.LoginStateEnum.home.position
                }
                LoginViewModel.LoginStateEnum.info -> {

                    binding.viewpager.currentItem = LoginViewModel.LoginStateEnum.info.position
                }
                LoginViewModel.LoginStateEnum.password -> {

                    binding.viewpager.currentItem = LoginViewModel.LoginStateEnum.password.position
                }
            }

        }

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AnimationHelper.setFadeTransition(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        binding.viewModel = viewModel



        init()

        viewModel.actionState.observe(this, Observer { action ->
            loginAction = action

        })

        viewModel.viewState.observe(this, Observer { state ->
            loginState = state

        })

        loginAction = LoginViewModel.LoginActionEnum.register
        loginState = LoginViewModel.LoginStateEnum.home
    }


    private fun init() {


        screenLibrary = ScreenCalcute(this)

        overlayShow(false)
        showBack(false)
        initViewPager()
        binding.toolbar.centerContainer = ToolbarComponent.CenterContainer.DOTS
        binding.toolbar.showNavigator(false, 0)


        binding.toolbar.cancelClickListener = View.OnClickListener { prePage() }


        binding.background.getLayoutParams().width = (screenLibrary.screenWidthPixel * 1.2).toInt()

        binding.scrollView.setOnTouchListener { _, _ -> true }

        viewModel.errorMessage.observe(this, Observer {
                errorMessage-> Toast.makeText(this,errorMessage,Toast.LENGTH_LONG).show()
        })

        viewModel.goNext.observe(this, Observer { value ->
            if (value == true) {
                    startActivityMakeScene(DashboardActivity.newInstance(this@LoginActivity))
                    finishSafe()
            }
        })

        viewModel.loading.observe(this, Observer { value ->
            if (value == true) {
               binding.loading.showLoading()
            }else
                binding.loading.hideLoading()
        })


        binding.loading.hideLoading()



    }


    internal fun prePage() {

        if (binding.viewpager.currentItem - 1 == LoginViewModel.LoginStateEnum.home.position)
            KeyboardManager.hideSoftKeyboard(this)

        if (binding.viewpager.currentItem == 0)
            finishSafe()


        if (binding.viewpager.currentItem > LoginViewModel.LoginStateEnum.home.position) {
            binding.viewpager.setCurrentItem(binding.viewpager.currentItem - 1, true)
        }

    }


    fun initViewPagerLogin() {
        adapter.clear()

        adapter.addFragment(homeFragment)
        adapter.addFragment(signInFragment)

        binding.viewpager.adapter?.notifyDataSetChanged()
    }

    fun initViewPagerRegister() {
        adapter.clear()
        adapter.addFragment(homeFragment)
        adapter.addFragment(emailFragment)
        adapter.addFragment(nameFragment)
        adapter.addFragment(passwordFragment)
        binding.viewpager.adapter?.notifyDataSetChanged()

    }

    override fun onBackPressed() {
       prePage()
    }


    internal fun initViewPager() {

        binding.viewpager.setScrollDurationFactor(2.0)
        adapter = ViewPagerAdapter(getSupportFragmentManager())

        homeFragment = LoginHomeFragment()
        passwordFragment = LoginPasswordFragment()
        signInFragment = LoginSignInFragment()
        nameFragment = LoginNameFragment()
        emailFragment = LoginEmailFragment()



        binding.viewpager.setAdapter(adapter)
        binding.viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                val x = ((binding.viewpager.getWidth() * position + positionOffsetPixels) * computeFactor()).toInt()
                binding.scrollView.scrollTo(x, 0)
            }

            override fun onPageSelected(position: Int) {

                if (position == 1) {
                    loginState =
                        if (loginAction == LoginViewModel.LoginActionEnum.login) LoginViewModel.LoginStateEnum.login else LoginViewModel.LoginStateEnum.email
                } else
                    loginState = LoginViewModel.LoginStateEnum.get(position)


                Debug.Log(TAG, String.format("onSelect:%s", loginState.name))
                (adapter.getItem(position) as IPagerFragment).onSelect()


                if (loginState == LoginViewModel.LoginStateEnum.home)
                    KeyboardManager.hideSoftKeyboard(this@LoginActivity)


                if (loginState == LoginViewModel.LoginStateEnum.home) {
                    overlayShow(false)

                    showBack(false)
                } else {
                    overlayShow(true)
                    showBack(true)
                }


                if (loginState == LoginViewModel.LoginStateEnum.home) {
                    binding.toolbar.showNavigator(false, 0)
                } else if (loginAction == LoginViewModel.LoginActionEnum.register) {

                    binding.toolbar.showNavigator(true, position - 1)
                } else if (loginAction == LoginViewModel.LoginActionEnum.login) {
                    binding.toolbar.showNavigator(false, 0)
                }


            }

            override fun onPageScrollStateChanged(state: Int) {

            }

            private fun computeFactor(): Float {
                return (binding.background.getWidth() - binding.viewpager.getWidth()) / (binding.viewpager.getWidth() * (binding.viewpager.getAdapter()?.getCount()!! * 1.0f - 1))
            }
        })

        KeyboardManager.hideSoftKeyboard(this)
    }


    internal fun showBack(visiable: Boolean) {
        if (visiable) {

//            binding.backButton.animate().scaleX(1f).scaleY(1f).setDuration(200).start()
//            binding.backButton.setClickable(true)
//
            binding.toolbar.cancelContainer = ToolbarComponent.CancelContainer.BACK
        } else {

            binding.toolbar.cancelContainer = ToolbarComponent.CancelContainer.NONE
//            binding.backButton.animate().scaleX(0f).scaleY(0f).setDuration(100).start()
//            binding.backButton.setClickable(false)
        }
    }


    internal fun overlayShow(visiable: Boolean) {
        if (visiable) {
            binding.overlayLayout.animate().alpha(0.8f).setDuration(500).start()

        } else {
            binding.overlayLayout.animate().alpha(0.2f).setDuration(500).start()

        }
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val mFragmentList = ArrayList<Fragment>()

        override fun getCount(): Int {
            return mFragmentList.size
        }

        override fun getItemPosition(`object`: Any): Int {

            return POSITION_NONE
        }


        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        fun addFragment(fragment: Fragment) {
            mFragmentList.add(fragment)
        }


        fun clear() {
            mFragmentList.clear()
        }

        override fun getPageTitle(position: Int): CharSequence {
            return ""
        }
    }


}