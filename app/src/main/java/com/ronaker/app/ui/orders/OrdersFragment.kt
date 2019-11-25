package com.ronaker.app.ui.orders

import android.os.Binder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.CoreComponentFactory
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ncapdevi.fragnav.FragNavController
import com.ncapdevi.fragnav.FragNavLogger
import com.ncapdevi.fragnav.FragNavSwitchController
import com.ncapdevi.fragnav.FragNavTransactionOptions
import com.ncapdevi.fragnav.tabhistory.UniqueTabHistoryStrategy
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.model.Order
import com.ronaker.app.utils.view.EndlessRecyclerViewScrollListener
import com.ronaker.app.utils.view.LoadingComponent
import com.ronaker.app.utils.view.TabNavigationComponent

class OrdersFragment : BaseFragment(), FragNavController.TransactionListener,
    FragNavController.RootFragmentListener {

    private lateinit var binding: com.ronaker.app.databinding.FragmentOrdersBinding
    private lateinit var viewModel: OrdersViewModel



    private val INDEX_ALL = FragNavController.TAB1
    private val INDEX_Renting = FragNavController.TAB2
    private val INDEX_Lending = FragNavController.TAB3
    private val INDEX_Archive = FragNavController.TAB4


    private lateinit var fragNavController: FragNavController
    override val numberOfRootFragments: Int = 5


    override fun getRootFragment(index: Int): Fragment {
        return when (index) {
            INDEX_ALL -> OrderListFragment.newInstance(null)
            INDEX_Renting -> OrderListFragment.newInstance(Order.OrderTypeEnum.Renting.key)
            INDEX_Lending -> OrderListFragment.newInstance(Order.OrderTypeEnum.Lending.key)
            INDEX_Archive ->OrderListFragment.newInstance("archive")
            else -> OrderListFragment.newInstance(null)
        }
    }
    override fun onFragmentTransaction(fragment: Fragment?, transactionType: FragNavController.TransactionType) {

    }


    override fun onTabTransaction(fragment: Fragment?, index: Int) {
    }


    val tabList :ArrayList< TextView> =ArrayList()



    var selectedTab=0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orders, container, false)
        viewModel = ViewModelProviders.of(this).get(OrdersViewModel::class.java)

//
        binding.viewModel = viewModel

        fragNavController = FragNavController(childFragmentManager, R.id.container)


        tabList.add(binding.text1)
        tabList.add(binding.text2)
        tabList.add(binding.text3)
        tabList.add(binding.text4)


        binding.layout1.setOnClickListener { selectTab(0) }
        binding.layout2.setOnClickListener { selectTab(1) }
        binding.layout3.setOnClickListener { selectTab(2) }
        binding.layout4.setOnClickListener { selectTab(3) }


        initNavigation(savedInstanceState)


        return binding.root
    }



    fun initNavigation(savedInstanceState: Bundle?) {

        fragNavController.apply {
            transactionListener = this@OrdersFragment
            rootFragmentListener = this@OrdersFragment
            createEager = true
            fragNavLogger = object : FragNavLogger {
                override fun error(message: String, throwable: Throwable) {

                }
            }
            defaultTransactionOptions = FragNavTransactionOptions.newBuilder().customAnimations(android.R.anim.fade_in, android.R.anim.fade_out,android.R.anim.fade_in,android.R.anim.fade_out).build()
            fragmentHideStrategy = FragNavController.DETACH_ON_NAVIGATE_HIDE_ON_SWITCH

            navigationStrategy = UniqueTabHistoryStrategy(object : FragNavSwitchController {
                override fun switchTab(index: Int, transactionOptions: FragNavTransactionOptions?) {
                    selectTabUpdateView(index)
                }
            })
        }

        fragNavController.initialize(INDEX_ALL, savedInstanceState)


        val initial = savedInstanceState == null
        if (initial ) {
            selectTab(INDEX_ALL)
        }



    }


    override fun onStart() {
        super.onStart()
        selectTabUpdateView( selectedTab)

    }

    fun selectTabUpdateView(index:Int){


        selectedTab=index
        tabList.forEach {
            context?.let { it1 ->
                it.setTextColor(ContextCompat.getColor(it1,R.color.colorTextGreyLight) )
                it.setBackgroundResource(0 )

            }


        }
        context?.let { it1 ->

            tabList.get(index).setTextColor(ContextCompat.getColor(it1, R.color.colorTextLight))
            tabList.get(index).setBackgroundResource(R.drawable.background_prinery_corner)

        }
    }

    fun selectTab(index:Int){

        selectTabUpdateView(index)


        when (index) {
            0 -> fragNavController.switchTab(INDEX_ALL)
            1 ->  fragNavController.switchTab(INDEX_Renting)
            2 -> fragNavController.switchTab(INDEX_Lending)
            3 -> fragNavController.switchTab(INDEX_Archive)


        }


    }


    companion object {

        fun newInstance(): OrdersFragment {
            return OrdersFragment()
        }
    }


}