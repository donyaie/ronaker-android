package com.ronaker.app.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.model.Order

class OrdersFragment : BaseFragment() {

    private lateinit var binding: com.ronaker.app.databinding.FragmentOrdersBinding
    private lateinit var viewModel: OrdersViewModel


    private lateinit var adapter: ViewPagerAdapter


    private val tabList: ArrayList<TextView> = ArrayList()


    private var selectedTab = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orders, container, false)
        viewModel = ViewModelProvider(this).get(OrdersViewModel::class.java)

//
        binding.viewModel = viewModel


        tabList.add(binding.text1)
        tabList.add(binding.text2)
        tabList.add(binding.text3)
        tabList.add(binding.text4)


        binding.layout1.setOnClickListener { selectTab(0) }
        binding.layout2.setOnClickListener { selectTab(1) }
        binding.layout3.setOnClickListener { selectTab(2) }
        binding.layout4.setOnClickListener { selectTab(3) }


//        initNavigation(savedInstanceState)
        binding.viewpager.isUserInputEnabled = false

        initViewPager()


        return binding.root
    }


    override fun onStart() {
        super.onStart()
        selectTabUpdateView(selectedTab)

    }

    private fun selectTabUpdateView(index: Int) {


        selectedTab = index
        tabList.forEach {
            context?.let { it1 ->
                it.setTextColor(ContextCompat.getColor(it1, R.color.colorTextGreyLight))
                it.setBackgroundResource(0)

            }


        }
        context?.let { it1 ->

            tabList[index].setTextColor(ContextCompat.getColor(it1, R.color.colorTextLight))
            tabList[index].setBackgroundResource(R.drawable.background_prinery_corner)

        }
    }

    private fun selectTab(index: Int) {

        selectTabUpdateView(index)

        binding.viewpager.setCurrentItem(index, true)


    }


    companion object {

        fun newInstance(): OrdersFragment {
            return OrdersFragment()
        }
    }

    private val viewPager2PageChangeCallback = ViewPager2PageChangeCallback { position ->


//        binding.viewpager.setCurrentItem(position, false)
        selectTabUpdateView(position)

    }

    private fun initViewPager() {


        adapter = ViewPagerAdapter(this)



        binding.viewpager.adapter = adapter
        binding.viewpager.registerOnPageChangeCallback(viewPager2PageChangeCallback)


    }

    class ViewPager2PageChangeCallback(private val listener: (Int) -> Unit) :
        ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            listener.invoke(position)

        }
    }


    internal inner class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(
        fragment
    ) {
        private val mFragmentList = ArrayList<Fragment>()

        val dataList = ArrayList<Fragment>()


        init {
            dataList.clear()


            dataList.add(OrderListFragment.newInstance(null))
            dataList.add(OrderListFragment.newInstance(Order.OrderTypeEnum.Renting.key))
            dataList.add(OrderListFragment.newInstance(Order.OrderTypeEnum.Lending.key))
            dataList.add(OrderListFragment.newInstance("archive"))


        }

        fun clear() {
            mFragmentList.clear()
        }


        override fun getItemCount(): Int {
            return 4
        }

        override fun createFragment(position: Int): Fragment {


            return dataList[position]
//
//            when (position) {
//                dataList -> OrderListFragment.newInstance(null)
//                1 -> OrderListFragment.newInstance(Order.OrderTypeEnum.Renting.key)
//                2 -> OrderListFragment.newInstance(Order.OrderTypeEnum.Lending.key)
//                3 -> OrderListFragment.newInstance("archive")
//                else -> OrderListFragment.newInstance(null)
//            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.viewpager.unregisterOnPageChangeCallback(viewPager2PageChangeCallback)
    }





}