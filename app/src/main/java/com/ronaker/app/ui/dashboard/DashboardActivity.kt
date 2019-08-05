package com.ronaker.app.ui.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.ncapdevi.fragnav.FragNavController
import com.ncapdevi.fragnav.FragNavLogger
import com.ncapdevi.fragnav.FragNavSwitchController
import com.ncapdevi.fragnav.FragNavTransactionOptions
import com.ncapdevi.fragnav.tabhistory.UniqueTabHistoryStrategy
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.ui.explore.ExploreFragment
import com.ronaker.app.ui.inbox.InboxFragment
import com.ronaker.app.ui.manageProduct.ManageProductListFragment
import com.ronaker.app.ui.orders.OrdersFragment
import com.ronaker.app.ui.profile.ProfileFragment
import com.ronaker.app.utils.AnimationHelper
import com.ronaker.app.utils.view.TabNavigationComponent


class DashboardActivity : BaseActivity(), FragNavController.TransactionListener,
    FragNavController.RootFragmentListener {


    private val fragNavController: FragNavController = FragNavController(supportFragmentManager, R.id.container)
    override val numberOfRootFragments: Int = 5


    override fun getRootFragment(index: Int): Fragment {
        return when (index) {
            INDEX_EXPLORE -> ExploreFragment.newInstance()
            INDEX_ITEMADD -> ManageProductListFragment.newInstance()
            INDEX_ORDERS -> OrdersFragment.newInstance()
            INDEX_PROFILE -> ProfileFragment.newInstance()
            INDEX_INBOX-> InboxFragment.newInstance()
            else -> ExploreFragment.newInstance()
        }
    }
    override fun onFragmentTransaction(fragment: Fragment?, transactionType: FragNavController.TransactionType) {

    }


    override fun onTabTransaction(fragment: Fragment?, index: Int) {
    }

    private val INDEX_EXPLORE = FragNavController.TAB1
    private val INDEX_ORDERS = FragNavController.TAB2
    private val INDEX_ITEMADD = FragNavController.TAB3
    private val INDEX_INBOX = FragNavController.TAB4
    private val INDEX_PROFILE = FragNavController.TAB5

    private lateinit var binding: com.ronaker.app.databinding.ActivityDashboardBinding
    private lateinit var viewModel: DashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        AnimationHelper.setFadeTransition(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, com.ronaker.app.R.layout.activity_dashboard)

        viewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)

        binding.viewModel = viewModel

        initNavigation(savedInstanceState)

    }



    override fun recreate() {
        super.recreate()
        binding.navigation.select(INDEX_EXPLORE)
    }


    fun initNavigation(savedInstanceState: Bundle?) {






        fragNavController.apply {
            transactionListener = this@DashboardActivity
            rootFragmentListener = this@DashboardActivity
            createEager = true
            fragNavLogger = object : FragNavLogger {
                override fun error(message: String, throwable: Throwable) {

                }
            }
            defaultTransactionOptions = FragNavTransactionOptions.newBuilder().customAnimations(android.R.anim.fade_in, R.anim.abc_fade_out,android.R.anim.fade_in, R.anim.abc_fade_out).build()
            fragmentHideStrategy = FragNavController.DETACH_ON_NAVIGATE_HIDE_ON_SWITCH

            navigationStrategy = UniqueTabHistoryStrategy(object : FragNavSwitchController {
                override fun switchTab(index: Int, transactionOptions: FragNavTransactionOptions?) {
                    binding.navigation.select(index)
                }
            })
        }

        fragNavController.initialize(INDEX_EXPLORE, savedInstanceState)


        val initial = savedInstanceState == null
        if (initial || isChangingConfigurations) {
            binding.navigation.select(INDEX_EXPLORE)
        }


        binding.navigation.selectListener = object : TabNavigationComponent.OnSelectItemListener {
            override fun onReSelected(index: Int) {
                fragNavController.clearStack()
            }

            override fun onSelect(index: Int) {

                when (index) {
                    0 -> fragNavController.switchTab(INDEX_EXPLORE)
                    1 ->  fragNavController.switchTab(INDEX_ORDERS)
                    2 -> fragNavController.switchTab(INDEX_ITEMADD)
                    3 -> fragNavController.switchTab(INDEX_INBOX)
                    4 -> fragNavController.switchTab(INDEX_PROFILE)


                }
            }
        }







    }

    companion object {
        fun newInstance(context: Context): Intent {
            var intent = Intent(context, DashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
            return intent
        }
    }

    fun pushFragment(fragment: Fragment) {

        fragNavController.pushFragment(fragment)

    }

    fun backFragment() {
        fragNavController.popFragment()

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        fragNavController.onSaveInstanceState(outState)

    }


    override fun onBackPressed() {
        if (!fragNavController.popFragment()) {
            super.onBackPressed()
        }
    }
}