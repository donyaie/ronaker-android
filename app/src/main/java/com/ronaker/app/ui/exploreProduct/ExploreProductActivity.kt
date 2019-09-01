package com.ronaker.app.ui.exploreProduct

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.model.Product
import com.ronaker.app.ui.chackoutCalendar.CheckoutCalendarActivity
import com.ronaker.app.utils.AnimationHelper


class ExploreProductActivity : BaseActivity() {

    private val TAG = ExploreProductActivity::class.java.simpleName

    private lateinit var binding: com.ronaker.app.databinding.ActivityProductExploreBinding
    private lateinit var viewModel: ExploreProductViewModel

    companion object {
        var SUID_KEY = "suid"
        var PRODUCT_KEY = "product"

        var IMAGE_TRANSITION_KEY = "image_transition"

        fun newInstance(context: Context, suid: String): Intent {
            var intent = Intent(context, ExploreProductActivity::class.java)
            var boundle = Bundle()
            boundle.putString(SUID_KEY, suid)
            intent.putExtras(boundle)

            return intent
        }

        fun newInstance(context: Context, product: Product, transitionName: String?): Intent {
            var intent = Intent(context, ExploreProductActivity::class.java)
            var boundle = Bundle()
            boundle.putParcelable(PRODUCT_KEY, product)
            boundle.putString(IMAGE_TRANSITION_KEY, transitionName)


            intent.putExtras(boundle)

            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (getImageTransition() == null)
            AnimationHelper.setSlideTransition(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_explore)

        viewModel = ViewModelProviders.of(this).get(ExploreProductViewModel::class.java)

        binding.viewModel = viewModel





        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && getImageTransition() !=null) {
            val imageTransitionName = getImageTransition()
            binding.avatarImage.setTransitionName(imageTransitionName)
        }






        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        })

        viewModel.loading.observe(this, Observer { value ->
            if (value == true) {
                binding.loading.showLoading()
            } else
                binding.loading.hideLoading()
        })



        viewModel.retry.observe(this, Observer { value ->
            if (value == true) {
                binding.loading.showRetry()
            } else
                binding.loading.hideRetry()
        })

        binding.loading.oClickRetryListener = View.OnClickListener {

            viewModel.onRetry()
        }



        binding.toolbar.cancelClickListener = View.OnClickListener { onBackPressed() }



        binding.scrollView.viewTreeObserver.addOnScrollChangedListener {

            try {
                val scrollY = binding.scrollView.scrollY

                if (scrollY <= binding.avatarImage.height / 2 - binding.toolbar.bottom) {

                    binding.toolbar.isTransparent = true
                    binding.toolbar.isBottomLine = false




                } else {

                    binding.toolbar.isTransparent = false
                    binding.toolbar.isBottomLine = true

                }

            } catch (ex: Exception) {

            }
        };



        viewModel.checkout.observe(this, Observer { suid ->

            startActivityMakeScene(CheckoutCalendarActivity.newInstance(this, viewModel.mProduct))

        })


    }

    override fun onStart() {

//        AnimationHelper.setFadeTransition(this)
        super.onStart()

        if (isFistStart()) {

            getSUID()?.let { viewModel.loadProduct(it) }

            getProduct()?.let { viewModel.loadProduct(it) }


            Handler().postDelayed({
                binding.scrollView.smoothScrollTo(0,0)
                binding.toolbar.isTransparent = true
                binding.toolbar.isBottomLine = false
            },500)

        }


    }


    fun getSUID(): String? {
        return if (intent.hasExtra(SUID_KEY)) {
            intent.getStringExtra(SUID_KEY)
        } else {
            null
        }
    }


    fun getImageTransition(): String? {
        return if (intent.hasExtra(IMAGE_TRANSITION_KEY)) {
            intent.getStringExtra(IMAGE_TRANSITION_KEY)
        } else {
            null
        }
    }

    fun getProduct(): Product? {
        return if (intent.hasExtra(PRODUCT_KEY)) {
            intent.getParcelableExtra(PRODUCT_KEY)
        } else {
            null
        }
    }


    override fun onBackPressed() {
        super.onBackPressed();
    }


}