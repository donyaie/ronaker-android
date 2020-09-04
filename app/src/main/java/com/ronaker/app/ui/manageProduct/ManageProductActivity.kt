package com.ronaker.app.ui.manageProduct

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.model.Product
import com.ronaker.app.ui.addProduct.AddProductActivity
import com.ronaker.app.ui.addProduct.AddProductViewModel
import com.ronaker.app.ui.exploreProduct.ExploreProductActivity
import com.ronaker.app.utils.Alert
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManageProductActivity : BaseActivity(), ViewTreeObserver.OnScrollChangedListener {


    private lateinit var binding: com.ronaker.app.databinding.ActivityManageProductBinding
    private val viewModel: ManageProductViewModel by viewModels()


    companion object {

        private var SUID_KEY = "suid"
        private var PRODUCT_KEY = "product"

        var REQUEST_CODE = 349


        fun newInstance(context: Context, product: Product): Intent {
            val intent = Intent(context, ManageProductActivity::class.java)
            val boundle = Bundle()
            boundle.putParcelable(PRODUCT_KEY, product)
            intent.putExtras(boundle)
            return intent
        }


        fun newInstance(context: Activity, suid: String): Intent {
            val intent = Intent(context, ManageProductActivity::class.java)
            val boundle = Bundle()
            boundle.putString(SUID_KEY, suid)
            intent.putExtras(boundle)

            return intent
        }


        fun newInstance(context: Application, suid: String): Intent {
            val intent = Intent(context, ManageProductActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK
            val boundle = Bundle()
            boundle.putString(SUID_KEY, suid)
            intent.putExtras(boundle)
            return intent
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_product)

        binding.viewModel = viewModel


        viewModel.activeState.observe(this, { active ->

            unregisterActiveListener()
            binding.activeSwitch.isChecked = active
            registerActiveListener()

        })

        viewModel.loading.observe(this, { loading ->

            binding.refreshLayout.isRefreshing = loading

        })


        binding.refreshLayout.setOnRefreshListener {

            viewModel.retry()
        }


        binding.toolbar.actionTextClickListener=View.OnClickListener {

            getCurrentSuid()?.let {

                startActivity(ExploreProductActivity.newInstance(this,suid = it))

            }

        }


        viewModel.loadingAction.observe(this, { loading ->


            if (loading) {
                binding.loading.visibility = View.VISIBLE
                binding.loading.showLoading()
            } else
                binding.loading.hideLoading()

        })


//        viewModel.retry.observe(this, {loading ->
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

        viewModel.errorMessage.observe(this, { errorMessage ->

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

        val vtObserver = binding.root.viewTreeObserver
        vtObserver.addOnGlobalLayoutListener {

            val width=  binding.root.measuredWidth

            binding.avatarLayout.layoutParams.height = (width * 0.8).toInt()

        }





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