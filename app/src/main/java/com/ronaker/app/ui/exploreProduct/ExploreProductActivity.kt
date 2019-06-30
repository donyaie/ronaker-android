package com.ronaker.app.ui.exploreProduct

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.utils.AnimationHelper
import com.ronaker.app.utils.view.LoadingComponent

class ExploreProductActivity : BaseActivity() {

    private val TAG = ExploreProductActivity::class.java.simpleName

    private lateinit var binding: com.ronaker.app.databinding.ActivityProductExploreBinding
    private lateinit var viewModel: ExploreProductViewModel

    companion object {
        var SUID_KEY = "suid"

        fun newInstance(context: Context, suid: String): Intent {
            var intent = Intent(context, ExploreProductActivity::class.java)
            var boundle = Bundle()
            boundle.putString(SUID_KEY, suid)
            intent.putExtras(boundle)

            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AnimationHelper.animateActivityFade(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_explore)

        viewModel = ViewModelProviders.of(this).get(ExploreProductViewModel::class.java)

        binding.viewModel = viewModel






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

        binding.loading.oClickRetryListener=View.OnClickListener {

            viewModel.onRetry()
        }


//        viewModel.goNext.observe(this, Observer { value ->
//            if (value)
//                startActivity(PhoneNumberActivity.newInstance(this@ExploreProductActivity))
//            else
//                finish()
//        })


        if ( intent.hasExtra(SUID_KEY)) {
            var suid = intent.getStringExtra(SUID_KEY)

            viewModel.loadProduct(suid)



        } else {
            finish()
        }


    }

    override fun onStart() {

        AnimationHelper.animateActivityFade(this)
        super.onStart()
    }



    override fun onBackPressed() {
        finish()
    }





}