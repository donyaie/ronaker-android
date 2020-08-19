package com.ronaker.app.ui.orderAuthorization

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.model.Order
import com.ronaker.app.utils.Alert
import com.ronaker.app.utils.AppDebug
import com.ronaker.app.utils.KeyboardManager
import com.ronaker.app.utils.view.IPagerFragment
import java.util.*
import kotlin.collections.ArrayList


class OrderAuthorizationActivity : BaseActivity() {

    private val TAG = OrderAuthorizationActivity::class.java.simpleName

    private lateinit var binding: com.ronaker.app.databinding.ActivityOrderAuthorizationBinding
    private lateinit var viewModel: OrderAuthorizationViewModel

    private lateinit var checkoutFragment: SmartIDPersonalCodeFragment
    private lateinit var messageFragment: SmartIDAuthFragment

    private lateinit var adapter: ViewPagerAdapter


    internal var fragmentState = OrderAuthorizationViewModel.StateEnum.PersonalCode
        set(value) {
            field = value


            when (fragmentState) {

                OrderAuthorizationViewModel.StateEnum.PersonalCode -> {
                    binding.viewpager.currentItem =
                        OrderAuthorizationViewModel.StateEnum.PersonalCode.position
                }

                OrderAuthorizationViewModel.StateEnum.Auth -> {
                    binding.viewpager.currentItem =
                        OrderAuthorizationViewModel.StateEnum.Auth.position
                }
            }

        }

    companion object {
        const val REQUEST_CODE = 364

        var Order_KEY = "order"
        var CANSIGN_KEY = "can_sign"
        var START_RENTING_KEY = "start_renting"


        fun newInstance(
            context: Context, order: Order, canSign: Boolean = true, startRenting: Boolean = false
        ): Intent {
            val intent = Intent(context, OrderAuthorizationActivity::class.java)
            val boundle = Bundle()
            boundle.putParcelable(Order_KEY, order)
            boundle.putBoolean(CANSIGN_KEY, canSign)
            boundle.putBoolean(START_RENTING_KEY, startRenting)
            intent.putExtras(boundle)

            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableKeyboardAnimator()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_authorization)

        viewModel = ViewModelProvider(this).get(OrderAuthorizationViewModel::class.java)

        binding.viewModel = viewModel

        init()

        viewModel.viewState.observe(this, { state ->
            fragmentState = state

        })


        viewModel.goNext.observe(this, {

            if(it)
                setResult(RESULT_OK)
            else
                setResult(RESULT_CANCELED)

            finish()

        })


        viewModel.setOrder(getOrder())

        fragmentState = OrderAuthorizationViewModel.StateEnum.PersonalCode


        viewModel.language.observe(this, {
            if (it.trim().toLowerCase(Locale.ROOT).compareTo("lt") == 0)
                binding.toolbar.action2Src = R.drawable.ic_en_text
            else
                binding.toolbar.action2Src = R.drawable.ic_lt_text
        })



        binding.toolbar.action2BouttonClickListener=View.OnClickListener { viewModel.changeLanguage() }


        viewModel.canStartRenting(canStartRenting())


    }


    private fun init() {


        initViewPager()


        viewModel.errorMessage.observe(this, { errorMessage ->
            Alert.makeTextError(this, errorMessage)
        })


        binding.toolbar.cancelClickListener = View.OnClickListener {

            finish()
        }


        binding.toolbar.actionTextClickListener = View.OnClickListener {


        }


        initViewPagerRegister()


    }


    private fun prePage() {


        if (binding.viewpager.currentItem == 0)
            finish()


        if (binding.viewpager.currentItem > 0) {
            binding.viewpager.setCurrentItem(binding.viewpager.currentItem - 1, true)
        }

    }


    private fun initViewPagerRegister() {
        adapter.clear()
        adapter.addFragment(checkoutFragment)
        adapter.addFragment(messageFragment)
        binding.viewpager.adapter?.notifyDataSetChanged()

    }


    private fun initViewPager() {

        binding.viewpager.setScrollDurationFactor(2.0)
        adapter = ViewPagerAdapter(supportFragmentManager)

        checkoutFragment = SmartIDPersonalCodeFragment()
        messageFragment = SmartIDAuthFragment()



        binding.viewpager.adapter = adapter

        binding.viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {


                fragmentState = OrderAuthorizationViewModel.StateEnum[position]


                AppDebug.log(TAG, String.format("onSelect:%s", fragmentState.name))
                (adapter.getItem(position) as IPagerFragment).onSelect()

                when (fragmentState) {
                    OrderAuthorizationViewModel.StateEnum.PersonalCode -> {
//                      binding.toolbar.actionContainer=ToolbarComponent.ActionContainer.TEXT
//                      KeyboardManager.hideSoftKeyboard(this@ProfileAuthorizationActivity)
                    }
                    else -> {
//                      binding.toolbar.actionContainer=ToolbarComponent.ActionContainer.NONE

                        KeyboardManager.hideSoftKeyboard(this@OrderAuthorizationActivity)
                    }
                }


            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })

        KeyboardManager.hideSoftKeyboard(this)
    }

    override fun onBackPressed() {
        prePage()
    }


    private fun isCanSign(): Boolean {
        if (intent.hasExtra(CANSIGN_KEY)) {

            return intent.getBooleanExtra(
                CANSIGN_KEY,
                true
            )

        }
        return true
    }


    private fun canStartRenting(): Boolean {
        if (intent.hasExtra(START_RENTING_KEY)) {

            return intent.getBooleanExtra(
                START_RENTING_KEY,
                false
            )

        }
        return true
    }



    private fun getOrder(): Order? {
        if (intent.hasExtra(Order_KEY)) {

            return intent.getParcelableExtra<Order?>(Order_KEY)

        }
        return null
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