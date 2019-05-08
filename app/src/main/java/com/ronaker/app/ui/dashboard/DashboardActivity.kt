package com.ronaker.app.ui.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.ncapdevi.fragnav.FragNavController
import com.ncapdevi.fragnav.tabhistory.FragNavTabHistoryController
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.ui.explore.ExploreFragment
import com.ronaker.app.ui.manageProduct.ManageProductListFragment
import com.ronaker.app.utils.view.TabNavigationComponent


class DashboardActivity : BaseActivity(), FragNavController.TransactionListener,
    FragNavController.RootFragmentListener {


    override fun getRootFragment(index: Int): Fragment {
       return when(index){
           INDEX_EXPLORE->ExploreFragment.newInstance()
           INDEX_ITEMADD->ManageProductListFragment.newInstance()
           else ->ExploreFragment.newInstance()
       }
    }

    override fun onFragmentTransaction(fragment: Fragment?, transactionType: FragNavController.TransactionType?) {


    }

    override fun onTabTransaction(fragment: Fragment?, index: Int) {
    }

    private val INDEX_EXPLORE = FragNavController.TAB1
    private val INDEX_HISTORY = FragNavController.TAB2
    private val INDEX_ITEMADD = FragNavController.TAB3
    private val INDEX_INBOX = FragNavController.TAB4
    private val INDEX_PROFILE = FragNavController.TAB5

    lateinit var mNavController: FragNavController
    lateinit var builder: FragNavController.Builder

    private lateinit var binding: com.ronaker.app.databinding.ActivityDashboardBinding
    private lateinit var viewModel: DashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, com.ronaker.app.R.layout.activity_dashboard)

        viewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)

        binding.viewModel = viewModel

        initNavigation(savedInstanceState)

    }


    fun initNavigation(savedInstanceState: Bundle?) {


        val initial = savedInstanceState == null
        if (initial) {
            binding.navigation.select(INDEX_EXPLORE)
        }

        builder = FragNavController.newBuilder(savedInstanceState, supportFragmentManager, com.ronaker.app.R.id.container)
            .transactionListener(this)
            .rootFragmentListener(this, 5)
            .popStrategy(FragNavTabHistoryController.UNIQUE_TAB_HISTORY)
            .switchController { index, transactionOptions -> binding.navigation.select(index) }


        mNavController=builder.build()


        binding.navigation.selectListener= object : TabNavigationComponent.OnSelectItemListener {
            override fun onReSelected(index: Int) {
                mNavController.clearStack()
            }

            override fun onSelect(index: Int) {

                when(index){
                    0->  mNavController.switchTab(INDEX_EXPLORE)
                    1->  Toast.makeText(this@DashboardActivity,"History",Toast.LENGTH_LONG).show()
                    2->  mNavController.switchTab(INDEX_ITEMADD)
//                    2->  startActivity(PhoneNumberActivity.newInstance(this@DashboardActivity ))
                    3->  Toast.makeText(this@DashboardActivity,"Inbox",Toast.LENGTH_LONG).show()
                    4->  Toast.makeText(this@DashboardActivity,"Profile",Toast.LENGTH_LONG).show()


                }
            }
        }



    }

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, DashboardActivity::class.java)
        }
    }

    fun pushFragment(fragment: Fragment) {
        if (mNavController != null) {
            mNavController.pushFragment(fragment)
        }
    }

    fun backFragment() {
        if (mNavController != null) {
            mNavController.popFragment()
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (mNavController != null) {
            mNavController.onSaveInstanceState(outState)
        }
    }


    override fun onBackPressed() {
        if (!mNavController.popFragment()) {
            super.onBackPressed()
        }
    }
}