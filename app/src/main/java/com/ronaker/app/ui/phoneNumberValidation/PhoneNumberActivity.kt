package com.ronaker.app.ui.phoneNumberValidation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.utils.Alert
import com.ronaker.app.utils.AppDebug
import com.ronaker.app.utils.KeyboardManager
import com.ronaker.app.utils.view.IPagerFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PhoneNumberActivity : BaseActivity() {

    private val TAG = PhoneNumberActivity::class.java.simpleName

    private lateinit var binding: com.ronaker.app.databinding.ActivityPhoneNumberBinding
    private val viewModel: PhoneNumberViewModel by viewModels()

    private lateinit var numberFragment: PhoneNumberFragment
    private lateinit var verifyFragment: PhoneNumberVerifyFragment

    private lateinit var adapter: ViewPagerAdapter


    internal var loginState = PhoneNumberViewModel.StateEnum.number
        set(value) {
            field = value


            when (loginState) {

                PhoneNumberViewModel.StateEnum.number -> {
                    binding.viewpager.currentItem = PhoneNumberViewModel.StateEnum.number.position
                }

                PhoneNumberViewModel.StateEnum.validate -> {
                    binding.viewpager.currentItem = PhoneNumberViewModel.StateEnum.validate.position
                }
            }

        }

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, PhoneNumberActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableKeyboardAnimator()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone_number)

        binding.viewModel = viewModel

        init()

        viewModel.viewState.observe(this, {state ->
            loginState = state

        })


        loginState = PhoneNumberViewModel.StateEnum.number


    }


    private fun init() {



        initViewPager()
        binding.toolbar.showNavigator(false, 0)


        binding.toolbar.cancelClickListener = View.OnClickListener { finish() }


        viewModel.errorMessage.observe(this, {errorMessage ->
            Alert.makeTextError(this, errorMessage)
        })



        viewModel.loading.observe(this, {value ->
            if (value == true) {

                binding.loading.visibility = View.VISIBLE
                binding.loading.showLoading()
            } else
                binding.loading.hideLoading()
        })



        viewModel.goNext.observe(this, {
            finish()
        })

        initViewPagerRegister()


    }


    internal fun prePage() {


        if (binding.viewpager.currentItem == 0)
            finish()


        if (binding.viewpager.currentItem > PhoneNumberViewModel.StateEnum.number.position) {
            binding.viewpager.setCurrentItem(binding.viewpager.currentItem - 1, true)
        }

    }


    private fun initViewPagerRegister() {
        adapter.clear()
        adapter.addFragment(numberFragment)
        adapter.addFragment(verifyFragment)
        binding.viewpager.adapter?.notifyDataSetChanged()

    }


    private fun initViewPager() {

        binding.viewpager.setScrollDurationFactor(2.0)
        adapter = ViewPagerAdapter(supportFragmentManager)

        numberFragment = PhoneNumberFragment()
        verifyFragment = PhoneNumberVerifyFragment()



        binding.viewpager.adapter = adapter

        binding.viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {


                loginState = PhoneNumberViewModel.StateEnum[position]


                AppDebug.log(TAG, String.format("onSelect:%s", loginState.name))
                (adapter.getItem(position) as IPagerFragment).onSelect()




                binding.toolbar.showNavigator(true, position)

            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })

        KeyboardManager.hideSoftKeyboard(this)
    }


    internal class ViewPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(
        manager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
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