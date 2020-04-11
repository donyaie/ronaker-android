package com.ronaker.app.ui.exploreProduct

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.model.Product
import com.ronaker.app.ui.chackoutCalendar.CheckoutCalendarActivity
import com.ronaker.app.ui.orderMessage.OrderMessageActivity
import com.ronaker.app.utils.*
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.util.ContentMetadata
import io.branch.referral.util.LinkProperties
import java.util.*
import kotlin.system.measureTimeMillis

class ExploreProductActivity : BaseActivity(), ViewTreeObserver.OnScrollChangedListener {


    private lateinit var binding: com.ronaker.app.databinding.ActivityProductExploreBinding
    private lateinit var viewModel: ExploreProductViewModel


    private lateinit var googleMap: GoogleMap

    companion object {

        private val TAG = ExploreProductActivity::class.java.simpleName

        const val REQUEST_CODE = 345
        const val SUID_KEY = "suid"
        const val PRODUCT_KEY = "product"

        fun newInstance(
            context: Context,
            product: Product
        ): Intent {
            val intent = Intent(context, ExploreProductActivity::class.java)
            val boundle = Bundle()
            boundle.putParcelable(PRODUCT_KEY, product)

            intent.putExtras(boundle)

            return intent
        }


        fun newInstance(
            context: Context,
            suid: String
        ): Intent {
            val intent = Intent(context, ExploreProductActivity::class.java)
            val boundle = Bundle()
            boundle.putString(SUID_KEY, suid)
            intent.putExtras(boundle)

            return intent
        }


    }


    override fun onNewIntent(newIntent: Intent?) {

        newIntent?.let {

            val suid = getCurrentSUID(intent)
            val newSuid = getCurrentSUID(it)
            if (suid != newSuid) {

                val intent = Intent(this, ExploreProductActivity::class.java)

                intent.putExtras(newIntent)

                this.finish()
                startActivity(intent)

            }

        }



        super.onNewIntent(newIntent)

    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_explore)

        viewModel = ViewModelProvider(this).get(ExploreProductViewModel::class.java)

        binding.viewModel = viewModel


        val time = measureTimeMillis {
            //calcut image height
            val screenCalculator = ScreenCalculator(this)
            binding.avatarSlide.layoutParams.height =
                (screenCalculator.screenWidthPixel * 0.7).toInt()



            viewModel.errorMessage.observe(this, Observer { errorMessage ->
                Alert.makeTextError(this, errorMessage)
            })

            binding.recycler.layoutManager = LinearLayoutManager(this)
            ViewCompat.setNestedScrollingEnabled(binding.recycler, false)


            binding.refreshLayout.setOnRefreshListener {


                viewModel.onRefresh()
            }

            viewModel.errorMessage.observe(this, Observer { errorMessage ->
                if (errorMessage != null) Alert.makeTextError(this, errorMessage)
            })

            viewModel.imageList.observe(this, Observer { images ->

                binding.avatarSlide.clearImage()
                binding.avatarSlide.addImagesUrl(images)
            })

            viewModel.loading.observe(this, Observer { value ->
                if (value == true) {
                    binding.loading.visibility = View.VISIBLE
                    binding.loading.showLoading()
                } else
                    binding.loading.hideLoading()
            })

            viewModel.loadingComment.observe(this, Observer { value ->
                if (value == true) {
                    binding.commentLoading.visibility = View.VISIBLE
                    binding.commentLoading.showLoading()
                } else
                    binding.commentLoading.hideLoading()
            })

            viewModel.isFavorite.observe(this, Observer { value ->
                if (value == true) {
                    binding.toolbar.action2Src = R.drawable.ic_fave_primery
                } else
                    binding.toolbar.action2Src = R.drawable.ic_fave_white
            })


            viewModel.loadingRefresh.observe(this, Observer { loading ->
                binding.refreshLayout.isRefreshing = loading

            })

            viewModel.retry.observe(this, Observer { value ->

                value?.let { binding.loading.showRetry(it) } ?: run { binding.loading.hideRetry() }

            })

            binding.loading.oClickRetryListener = View.OnClickListener {

                viewModel.onRetry()
            }

            binding.toolbar.cancelClickListener =
                View.OnClickListener { this.onBackPressed() }



            binding.toolbar.action1BouttonClickListener = View.OnClickListener {


                viewModel.mProduct?.let {

                    generateShareLink(it)

                }


            }



            binding.toolbar.action2BouttonClickListener = View.OnClickListener {

                getCurrentSUID(intent)?.let { viewModel.setFavorite(it) }

            }


            binding.scrollView.viewTreeObserver.addOnScrollChangedListener(this)


            viewModel.productLocation.observe(this, Observer { suid ->

                addMarker(suid)
            })

            viewModel.checkout.observe(this, Observer { _ ->
                viewModel.mProduct?.let {


                    this.startActivityForResult(
                        CheckoutCalendarActivity.newInstance(this, it)
                        , CheckoutCalendarActivity.REQUEST_CODE
                    )

                }

            })

        }

        AppDebug.log(TAG, "event time : $time")




        Thread(Runnable {
            try {
                val mv =
                    MapView(applicationContext)
                mv.onCreate(null)
                mv.onPause()
                mv.onDestroy()
            } catch (ignored:Exception) {
            }
        }).start()



        val time2 = measureTimeMillis {
            initMap()
        }
        AppDebug.log(TAG, "map time : $time2")
//        getData()


    }

    override fun onStart() {
        super.onStart()

        if ((this as BaseActivity).isFistStart()) {
            val time = measureTimeMillis {
                getData()
            }

            AppDebug.log(TAG, "onStart time : $time")


        } else {
            viewModel.onRefresh()
        }

    }


    private fun getData() {


        getSUID()?.let {

            binding.loading.visibility = View.VISIBLE
            binding.loading.showLoading()


            val time2 = measureTimeMillis {

                viewModel.loadProduct(it)

            }
            AppDebug.log(TAG, "loadProduct getSUID time : $time2")

        }

        getProduct()?.let {

            binding.loading.visibility = View.GONE
            binding.loading.hideLoading()


            val time2 = measureTimeMillis {
                viewModel.loadProduct(it)
            }

            AppDebug.log(TAG, "loadProduct getProduct time : $time2")

        }




        Handler().postDelayed({
            binding.scrollView.smoothScrollTo(0, 0)
            binding.toolbar.isTransparent = true
            binding.toolbar.isBottomLine = false
        }, 500)

    }

    private fun initMap() {




        val mapFragment = SupportMapFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.map, mapFragment)
            .commit()




        mapFragment.getMapAsync { map ->
            googleMap = map
            googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.style_json
                )
            )

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
    }


    private fun addMarker(latLng: LatLng) {
        if (::googleMap.isInitialized) {
            googleMap.clear()
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                latLng,
                12f
            )
            googleMap.moveCamera(cameraUpdate)
//
//            googleMap.addMarker(
//                MarkerOptions().position(latLng)
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_map))
//            )

        }


    }

    private fun getSUID(): String? {
        return intent?.getStringExtra(SUID_KEY)

    }


    private fun getCurrentSUID(mIntent: Intent?): String? {
        return when {
            mIntent?.hasExtra(SUID_KEY) == true -> {
                mIntent.getStringExtra(SUID_KEY)
            }
            mIntent?.hasExtra(PRODUCT_KEY) == true -> {
                val p = mIntent.getParcelableExtra(PRODUCT_KEY) as Product?
                return p?.suid
            }
            else -> {
                null
            }
        }
    }


    fun getProduct(): Product? {
        return intent?.getParcelableExtra(PRODUCT_KEY)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {

            CheckoutCalendarActivity.REQUEST_CODE -> {
                data?.let {

                    if (resultCode == Activity.RESULT_OK) {

                        val start =
                            Date(data.getLongExtra(CheckoutCalendarActivity.STARTDATE_KEY, -1))
                        val end = Date(data.getLongExtra(CheckoutCalendarActivity.ENDDATE_KEY, -1))


                        Handler().postDelayed({
//                            this.startActivityMakeSceneForResult(
//                                OrderMessageActivity.newInstance(
//                                    this,
//                                    getProduct(),
//                                    start,
//                                    end
//                                ), OrderMessageActivity.REQUEST_CODE
//                            )


                            this.startActivityForResult(
                                OrderMessageActivity.newInstance(
                                    this,
                                    getProduct(),
                                    start,
                                    end
                                )
                                ,
                                OrderMessageActivity.REQUEST_CODE
                            )


                        }, 200)
                    }
                }
            }


            OrderMessageActivity.REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    this.setResult(Activity.RESULT_OK)
                    this.finish()
                }


            }


        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onScrollChanged() {
        try {
            val scrollY = binding.scrollView.scrollY

            if (scrollY <= binding.avatarSlide.height / 1.2 - binding.toolbar.bottom) {

                binding.toolbar.isTransparent = true
                binding.toolbar.isBottomLine = false


            } else {

                binding.toolbar.isTransparent = false
                binding.toolbar.isBottomLine = true

            }

        } catch (ex: Exception) {

        }
    }


    private fun generateShareLink(it: Product) {
        val buo = BranchUniversalObject()
            .setCanonicalIdentifier("product/${it.suid}")
            .setTitle(it.name.toString())
            .setContentDescription(it.description)
            .setContentImageUrl(BASE_URL + it.avatar)
            .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
            .setLocalIndexMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
            .setContentMetadata(ContentMetadata().addCustomMetadata("product", it.suid))
            .setContentMetadata(
                ContentMetadata().addCustomMetadata(
                    "action",
                    "show_product"
                )
            )


        val lp = LinkProperties()
            .setChannel("androidApp")
            .setFeature("product sharing")

            .setCampaign("content product id")
//                .addControlParameter("$desktop_url", "http://example.com/home")
            .addControlParameter("product", it.suid)
            .addControlParameter(
                "custom_random",
                Calendar.getInstance().timeInMillis.toString()
            )


        buo.generateShortUrl(this, lp) { url, error ->
            if (error == null) {
                Log.i("BRANCH SDK", "got my Branch link to share: $url")
                it.suid?.let { it1 -> getAnalytics()?.actionShareProduct(it1) }
                IntentManeger.shareTextUrl(this, "Share Product:", url)
            }
        }

        buo.listOnGoogleSearch(this)

    }


    override fun onDestroy() {
        super.onDestroy()
        binding.avatarSlide.clearImage()

        binding.unbind()
    }

}