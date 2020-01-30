package com.ronaker.app.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.model.Order

class OrdersFragment : BaseFragment() {

    private lateinit var binding: com.ronaker.app.databinding.FragmentOrdersBinding
    private lateinit var viewModel: OrdersViewModel


    private lateinit var adapter: ViewPagerAdapter




    private val tabList :ArrayList< TextView> =ArrayList()



    private var selectedTab=0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

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

        initViewPager()


        return binding.root
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

            tabList[index].setTextColor(ContextCompat.getColor(it1, R.color.colorTextLight))
            tabList[index].setBackgroundResource(R.drawable.background_prinery_corner)

        }
    }

    private fun selectTab(index:Int){

        selectTabUpdateView(index)

        binding.viewpager.currentItem=index

    }


    companion object {

        fun newInstance(): OrdersFragment {
            return OrdersFragment()
        }
    }



    private fun initViewPager() {

        adapter = ViewPagerAdapter(childFragmentManager)

        binding.viewpager.adapter = adapter

        adapter.clear()
        adapter.addFragment(OrderListFragment.newInstance(null))
        adapter.addFragment(OrderListFragment.newInstance(Order.OrderTypeEnum.Renting.key))
        adapter.addFragment( OrderListFragment.newInstance(Order.OrderTypeEnum.Lending.key))
        adapter.addFragment(OrderListFragment.newInstance("archive"))
        binding.viewpager.adapter?.notifyDataSetChanged()


        binding.viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {

                selectTabUpdateView(position)


                binding.toolbar.showNavigator(true, position)

            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })

    }



    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager,
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