package com.ronaker.app.ui.manageProduct

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.model.Product
import com.ronaker.app.ui.addProduct.AddProductActivity
import com.ronaker.app.ui.addProduct.AddProductViewModel
import com.ronaker.app.utils.Alert
import com.ronaker.app.utils.ScreenCalculator

class ManageProductActivity : BaseActivity(), ViewTreeObserver.OnScrollChangedListener {


    private lateinit var binding: com.ronaker.app.databinding.ActivityManageProductBinding
    private lateinit var viewModel: ManageProductViewModel


    companion object {

        private var SUID_KEY = "suid"
        private var PRODUCT_KEY = "product"

        var REQUEST_CODE = 349


        fun newInstance(context: Context, product: Product?): Intent {
            val intent = Intent(context, ManageProductActivity::class.java)
            val boundle = Bundle()
            boundle.putParcelable(PRODUCT_KEY, product)
            intent.putExtras(boundle)

            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_product)

        viewModel = ViewModelProvider(this).get(ManageProductViewModel::class.java)

        binding.viewModel = viewModel


        viewModel.activeState.observe(this, Observer { active ->

            unregisterActiveListener()
            binding.activeSwitch.isChecked = active
            registerActiveListener()

        })

        viewModel.loading.observe(this, Observer { loading ->

            binding.refreshLayout.isRefreshing = loading

        })


        binding.refreshLayout.setOnRefreshListener {

            viewModel.retry()
        }


        viewModel.loadingAction.observe(this, Observer { loading ->


            if (loading) {
                binding.loading.visibility = View.VISIBLE
                binding.loading.showLoading()
            } else
                binding.loading.hideLoading()

        })


//        viewModel.retry.observe(this, Observer { loading ->
//
//            loading?.let { binding.loading.showRetry(it) } ?: run { binding.loading.hideRetry() }
//        })

//        binding.loading.oClickRetryListener = View.OnClickListener {
//
//            viewModel.retry()
//        }


        binding.toolbar.cancelClickListener = View.OnClickListener {

            onBackPressed()
        }

        viewModel.errorMessage.observe(this, Observer { errorMessage ->

            Alert.makeTextError(this, errorMessage)

        })


        binding.imageLayout.setOnClickListener {

            getCurrentSuid()?.let {
                this.startActivity(
                    AddProductActivity.newInstance(
                        this,
                        it, AddProductViewModel.StateEnum.Image
                    )

                )
            }


        }

        binding.locationLayout.setOnClickListener {
            getCurrentSuid()?.let {
                this.startActivity(
                    AddProductActivity.newInstance(
                        this,
                        it, AddProductViewModel.StateEnum.Location
                    )

                )
            }
        }

        binding.nameLayout.setOnClickListener {


            getCurrentSuid()?.let {
                this.startActivity(
                    AddProductActivity.newInstance(
                        this,
                        it, AddProductViewModel.StateEnum.Info
                    )

                )
            }
        }

        binding.priceLayout.setOnClickListener {


            getCurrentSuid()?.let {
                this.startActivity(
                    AddProductActivity.newInstance(
                        this,
                        it, AddProductViewModel.StateEnum.Price
                    )

                )
            }
        }


        binding.categoryLayout.setOnClickListener {

            getCurrentSuid()?.let {
                this.startActivity(
                    AddProductActivity.newInstance(
                        this,
                        it, AddProductViewModel.StateEnum.Category
                    )

                )
            }

        }


        val screenCalculator = ScreenCalculator(this)


        binding.avatarLayout.layoutParams.height = (screenCalculator.screenWidthPixel * 0.7).toInt()


        binding.scrollView.viewTreeObserver.addOnScrollChangedListener(this)

        registerActiveListener()


    }


    override fun onStart() {
        super.onStart()

        if (isFistStart()) {

            fill()
        } else {
            viewModel.retry()
        }
    }


    private fun registerActiveListener() {
        binding.activeSwitch.setOnCheckedChangeListener { _, active ->

            viewModel.updateActiveState(active)

        }

    }

    private fun unregisterActiveListener() {
        binding.activeSwitch.setOnCheckedChangeListener(null)
    }


    private fun fill() {

        getSuid()?.let { viewModel.loadProduct(it) }

        getProduct()?.let { viewModel.loadProduct(it) }


    }

    private fun getSuid(): String? {
        return this.intent?.getStringExtra(SUID_KEY)
    }

    fun getProduct(): Product? {
        return this.intent?.getParcelableExtra(PRODUCT_KEY)
    }


    fun getCurrentSuid(): String? {
        var suid: String? = null

        getProduct()?.let {
            suid = it.suid
        } ?: run { suid = getSuid() }

        return suid

    }

    override fun onScrollChanged() {
        try {
            val scrollY = binding.scrollView.scrollY

            if (scrollY <= binding.avatarImage.height / 1.2 - binding.toolbar.bottom) {

                binding.toolbar.isTransparent = true
                binding.toolbar.isBottomLine = false


            } else {

                binding.toolbar.isTransparent = false
                binding.toolbar.isBottomLine = true

            }

        } catch (ex: Exception) {

        }

    }

}