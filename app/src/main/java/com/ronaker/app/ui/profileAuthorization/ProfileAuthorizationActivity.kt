package com.ronaker.app.ui.profileAuthorization

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.utils.Alert
import com.ronaker.app.utils.AppDebug
import com.ronaker.app.utils.KeyboardManager
import com.ronaker.app.utils.view.IPagerFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileAuthorizationActivity : BaseActivity() {

    private val TAG = ProfileAuthorizationActivity::class.java.simpleName

    private lateinit var binding: com.ronaker.app.databinding.ActivityProfileAuthorizationBinding
    private val viewModel: ProfileAuthorizationViewModel by viewModels()

    private lateinit var checkoutFragment: SmartIDPersonalCodeFragment
    private lateinit var messageFragment: SmartIDAuthFragment

    private lateinit var adapter: ViewPagerAdapter


    internal var fragmentState = ProfileAuthorizationViewModel.StateEnum.PersonalCode
        set(value) {
            field = value


            when (fragmentState) {

                ProfileAuthorizationViewModel.StateEnum.PersonalCode -> {
                    binding.viewpager.currentItem =
                        ProfileAuthorizationViewModel.StateEnum.PersonalCode.position
                }

                ProfileAuthorizationViewModel.StateEnum.Auth -> {
                    binding.viewpager.currentItem =
                        ProfileAuthorizationViewModel.StateEnum.Auth.position
                }
            }

        }

    companion object {
        const val REQUEST_CODE = 360


        fun newInstance(
            context: Context
        ): Intent {
            val intent = Intent(context, ProfileAuthorizationActivity::class.java)
            val bundle = Bundle()

            intent.putExtras(bundle)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableKeyboardAnimator()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_authorization)

        binding.viewModel = viewModel

        init()

        viewModel.viewState.observe(this, {state ->
            fragmentState = state

        })


        viewModel.goNext.observe(this, {
            finish()

        })

        fragmentState = ProfileAuthorizationViewModel.StateEnum.PersonalCode


    }


    private fun init() {


        initViewPager()


        viewModel.errorMessage.observe(this, {errorMessage ->
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


                fragmentState = ProfileAuthorizationViewModel.StateEnum[position]


                AppDebug.log(TAG, String.format("onSelect:%s", fragmentState.name))
                (adapter.getItem(position) as IPagerFragment).onSelect()

                when (fragmentState) {
                    ProfileAuthorizationViewModel.StateEnum.PersonalCode -> {
//                      binding.toolbar.actionContainer=ToolbarComponent.ActionContainer.TEXT
//                      KeyboardManager.hideSoftKeyboard(this@ProfileAuthorizationActivity)
                    }
                    else -> {
//                      binding.toolbar.actionContainer=ToolbarComponent.ActionContainer.NONE

                        KeyboardManager.hideSoftKeyboard(this@ProfileAuthorizationActivity)
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