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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.model.Order
import com.ronaker.app.utils.view.EndlessRecyclerViewScrollListener
import com.ronaker.app.utils.view.LoadingComponent

class OrdersFragment : BaseFragment() {

    private lateinit var binding: com.ronaker.app.databinding.FragmentOrdersBinding
    private lateinit var viewModel: OrdersViewModel



    val tabList :ArrayList< TextView> =ArrayList()


  lateinit var scrollListener: EndlessRecyclerViewScrollListener
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orders, container, false)
        viewModel = ViewModelProviders.of(this).get(OrdersViewModel::class.java)

//
        binding.viewModel = viewModel
        var mnager = LinearLayoutManager(context )
        binding.recycler.layoutManager = mnager


        viewModel.loading.observe(this, Observer { loading ->
            if (loading) binding.loading.showLoading() else binding.loading.hideLoading()
        })
        viewModel.retry.observe(this, Observer { loading ->
            if (loading) binding.loading.showRetry() else binding.loading.hideRetry()
        })


        viewModel.resetList.observe(this, Observer {
            scrollListener.resetState()
        })


        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
//                binding.loading.showRetry()
            } else {
//                binding.loading.hideRetry()
            }
        })

        binding.loading.oClickRetryListener = View.OnClickListener {

                viewModel.retry()


        }




        tabList.add(binding.text1)
        tabList.add(binding.text2)
        tabList.add(binding.text3)
        tabList.add(binding.text4)


        binding.layout1.setOnClickListener { selectTab(0) }
        binding.layout2.setOnClickListener { selectTab(1) }
        binding.layout3.setOnClickListener { selectTab(2) }
        binding.layout4.setOnClickListener { selectTab(3) }



        selectTab(0)



        return binding.root
    }


    fun selectTab(index:Int){

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


        var filter:String?=null

        when(index){
            0->filter=null
            1->filter= Order.OrderTypeEnum.Renting.key
            2->filter= Order.OrderTypeEnum.Lending.key
            3->filter=null
        }



        viewModel.loadData(filter)


    }


    companion object {

        fun newInstance(): OrdersFragment {
            return OrdersFragment()
        }
    }


}