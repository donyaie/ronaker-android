package com.ronaker.app.ui.phoneNumberValidation

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
import com.ronaker.app.utils.AnimationHelper
import com.ronaker.app.utils.Debug
import com.ronaker.app.utils.KeyboardManager
import com.ronaker.app.utils.ScreenCalcute
import com.ronaker.app.utils.view.IPagerFragment


class PhoneNumberActivity : BaseActivity() {

    private val TAG = PhoneNumberActivity::class.java.simpleName

    private lateinit var binding: com.ronaker.app.databinding.ActivityPhoneNumberBinding
    private lateinit var viewModel: PhoneNumberViewModel

    private lateinit var numberFragment: PhoneNumberFragment
    private lateinit var verifyFragment: PhoneNumberVerifyFragment

    private lateinit var adapter: ViewPagerAdapter
    private lateinit var screenLibrary: ScreenCalcute



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

        AnimationHelper.animateActivityFade(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone_number)

        viewModel = ViewModelProviders.of(this).get(PhoneNumberViewModel::class.java)

        binding.viewModel = viewModel

        init()

        viewModel.viewState.observe(this, Observer { state ->
            loginState = state

        })




        loginState = PhoneNumberViewModel.StateEnum.number
    }


    private fun init() {


        screenLibrary = ScreenCalcute(this)

        initViewPager()
        binding.toolbar.showNavigator(false, 0)


        binding.toolbar.cancelClickListener = View.OnClickListener { finish() }


        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        })



        viewModel.loading.observe(this, Observer { value ->
            if (value == true) {
                binding.loading.showLoading()
            } else
                binding.loading.hideLoading()
        })



        viewModel.goNext.observe(this, Observer { value ->
           finish()
        })

        initViewPagerRegister()
        binding.loading.hideLoading()


    }


    internal fun prePage() {


        if (binding.viewpager.currentItem === 0)
            finish()


        if (binding.viewpager.getCurrentItem() > PhoneNumberViewModel.StateEnum.number.position) {
            binding.viewpager.setCurrentItem(binding.viewpager.getCurrentItem() - 1, true)
        }

    }


    fun initViewPagerRegister() {
        adapter.clear()
        adapter.addFragment(numberFragment)
        adapter.addFragment(verifyFragment)
        binding.viewpager.adapter?.notifyDataSetChanged()

    }

    override fun onBackPressed() {
        finish()
    }


    internal fun initViewPager() {

        binding.viewpager.setScrollDurationFactor(2.0)
        adapter = ViewPagerAdapter(getSupportFragmentManager())

        numberFragment = PhoneNumberFragment()
        verifyFragment = PhoneNumberVerifyFragment()



        binding.viewpager.setAdapter(adapter)

        binding.viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {


                loginState = PhoneNumberViewModel.StateEnum.get(position)


                Debug.Log(TAG, String.format("onSelect:%s", loginState.name))
                (adapter.getItem(position) as IPagerFragment).onSelect()




                binding.toolbar.showNavigator(true, position)

            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })

        KeyboardManager.hideSoftKeyboard(this)
    }










    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
    }




    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {
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