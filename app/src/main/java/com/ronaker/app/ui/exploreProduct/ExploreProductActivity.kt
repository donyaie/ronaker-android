package com.ronaker.app.ui.exploreProduct

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.model.Product
import com.ronaker.app.ui.chackoutCalendar.CheckoutCalendarActivity
import com.ronaker.app.utils.AnimationHelper
import com.ronaker.app.utils.DEFULT_LOCATION
import com.google.android.gms.maps.model.MarkerOptions
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class ExploreProductActivity : BaseActivity() {

    private val TAG = ExploreProductActivity::class.java.simpleName

    private lateinit var binding: com.ronaker.app.databinding.ActivityProductExploreBinding
    private lateinit var viewModel: ExploreProductViewModel

    lateinit var googleMap: GoogleMap

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
        AnimationHelper.setFadeTransition(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_explore)

        viewModel = ViewModelProviders.of(this).get(ExploreProductViewModel::class.java)

        binding.viewModel = viewModel





        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && getImageTransition() != null) {
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



        (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMapAsync {
            googleMap = it


            googleMap.uiSettings.setAllGesturesEnabled(false)

            viewModel.productLocation.value?.let {
                addMarker(it)

            } ?: run {
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                    DEFULT_LOCATION,
                    10f
                )
                googleMap.moveCamera(cameraUpdate)
            }


        }


        viewModel.productLocation.observe(this, Observer { suid ->

            addMarker(suid)


        })

        viewModel.checkout.observe(this, Observer { _ ->

            startActivityMakeScene(viewModel.mProduct?.let {
                CheckoutCalendarActivity.newInstance(this,
                    it
                )
            })

        })


    }

    override fun onStart() {

//        AnimationHelper.setFadeTransition(this)
        super.onStart()

        if (isFistStart()) {

            getSUID()?.let { viewModel.loadProduct(it) }

            getProduct()?.let { viewModel.loadProduct(it) }


            Handler().postDelayed({
                binding.scrollView.smoothScrollTo(0, 0)
                binding.toolbar.isTransparent = true
                binding.toolbar.isBottomLine = false
            }, 500)

        }


    }


    fun addMarker(latLng: LatLng) {
        if(::googleMap.isInitialized) {
            googleMap.clear()
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                latLng,
                17f
            )
            googleMap.moveCamera(cameraUpdate)

            googleMap.addMarker(
                MarkerOptions().position(latLng)
            )

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