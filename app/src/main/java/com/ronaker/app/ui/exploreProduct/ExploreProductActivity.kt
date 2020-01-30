package com.ronaker.app.ui.exploreProduct

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.model.Product
import com.ronaker.app.ui.chackoutCalendar.CheckoutCalendarActivity
import com.ronaker.app.ui.orderMessage.OrderMessageActivity
import com.ronaker.app.utils.*
import com.ronaker.app.utils.extension.finishSafe
import com.ronaker.app.utils.extension.startActivityMakeSceneForResult
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.Branch
import io.branch.referral.BranchError
import io.branch.referral.SharingHelper
import io.branch.referral.util.ContentMetadata
import io.branch.referral.util.LinkProperties
import io.branch.referral.util.ShareSheetStyle
import java.util.*


class ExploreProductActivity : BaseActivity(), ViewTreeObserver.OnScrollChangedListener {


    private lateinit var binding: com.ronaker.app.databinding.ActivityProductExploreBinding
    private lateinit var viewModel: ExploreProductViewModel

    private lateinit var googleMap: GoogleMap

    companion object {

        const val REQUEST_CODE = 345
        const val SUID_KEY = "suid"
        const val PRODUCT_KEY = "product"
        const val IMAGE_TRANSITION_KEY = "image_transition"

        private val TAG = ExploreProductActivity::class.java.simpleName

        fun isHavePending(product: Product): Boolean {
            product.suid?.let { return isTAGInStack(TAG + it) } ?: run { return false }
        }

        fun newInstance(context: Context, suid: String): Intent {
            val intent = Intent(context, ExploreProductActivity::class.java)
            val boundle = Bundle()
            boundle.putString(SUID_KEY, suid)
            intent.putExtras(boundle)
            return intent
        }

        fun newInstance(context: Context, product: Product, transitionName: String?): Intent {
            val intent = Intent(context, ExploreProductActivity::class.java)
            val boundle = Bundle()
            boundle.putParcelable(PRODUCT_KEY, product)
            boundle.putString(IMAGE_TRANSITION_KEY, transitionName)
            intent.putExtras(boundle)
            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        AnimationHelper.setSlideTransition(this)
        super.onCreate(savedInstanceState)
        activityTag = TAG + getCurrentSUID()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_explore)



        binding.recycler.layoutManager = LinearLayoutManager(this)
        ViewCompat.setNestedScrollingEnabled(binding.recycler, false)


        binding.refreshLayout.setOnRefreshListener {


            viewModel.onRefresh()
        }


        viewModel = ViewModelProvider(this).get(ExploreProductViewModel::class.java)

        binding.viewModel = viewModel

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && getImageTransition() != null) {
//            val imageTransitionName = getImageTransition()
//            binding.avatarSlide.transitionName = imageTransitionName
//        }

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

        binding.toolbar.cancelClickListener = View.OnClickListener { onBackPressed() }



        binding.toolbar.action1BouttonClickListener = View.OnClickListener {


            viewModel.mProduct?.let {

                val buo = BranchUniversalObject()
                    .setCanonicalIdentifier("product/${it.suid}")
                    .setTitle(it.name.toString())
                    .setContentDescription(it.description)
                    .setContentImageUrl(BASE_URL+it.avatar)
                    .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                    .setLocalIndexMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                    .setContentMetadata(ContentMetadata().addCustomMetadata("product", it.suid))
                    .setContentMetadata(ContentMetadata().addCustomMetadata("action","show_product"))


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


//            getCurrentSUID()?.let {
//                getAnalytics()?.actionShareProduct(it)
//                IntentManeger.shareTextUrl(this, "Share Product:", SHARE_URL + it)
//            }


        }



        binding.toolbar.action2BouttonClickListener = View.OnClickListener {

            getCurrentSUID()?.let { viewModel.setFavorite(it) }

        }




        binding.scrollView.viewTreeObserver.addOnScrollChangedListener(this)




        (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMapAsync { map ->
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

        viewModel.productLocation.observe(this, Observer { suid ->

            addMarker(suid)
        })

        viewModel.checkout.observe(this, Observer { _ ->
            viewModel.mProduct?.let {
                startActivityMakeSceneForResult(
                    CheckoutCalendarActivity.newInstance(
                        this,
                        it
                    ), CheckoutCalendarActivity.REQUEST_CODE
                )

            }

        })


        val screenCalculator = ScreenCalculator(this)


        binding.avatarLayout.layoutParams.height = (screenCalculator.screenWidthPixel * 0.7).toInt()


    }

    override fun onStart() {
        super.onStart()

        if (isFistStart()) {


            getSUID()?.let {

                binding.loading.visibility = View.VISIBLE
                binding.loading.showLoading()
                viewModel.loadProduct(it, true)

            }

            getProduct()?.let {

                binding.loading.visibility = View.GONE
                binding.loading.hideLoading()
                viewModel.loadProduct(it)

            }




            Handler().postDelayed({
                binding.scrollView.smoothScrollTo(0, 0)
                binding.toolbar.isTransparent = true
                binding.toolbar.isBottomLine = false
            }, 500)

        } else {
            viewModel.onRefresh()
        }


    }

    override fun onStop() {
        super.onStop()
        binding.scrollView.viewTreeObserver.removeOnScrollChangedListener(this)
    }


    private fun addMarker(latLng: LatLng) {
        if (::googleMap.isInitialized) {
            googleMap.clear()
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                latLng,
                12f
            )
            googleMap.moveCamera(cameraUpdate)

//            googleMap.addMarker(
//                MarkerOptions().position(latLng)
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_map))
//            )

        }


    }


    private fun getSUID(): String? {
        return if (intent.hasExtra(SUID_KEY)) {
            intent.getStringExtra(SUID_KEY)
        } else {
            null
        }
    }


    private fun getCurrentSUID(): String? {
        return when {
            intent.hasExtra(SUID_KEY) -> {
                intent.getStringExtra(SUID_KEY)
            }
            intent.hasExtra(PRODUCT_KEY) -> {
                val p = intent.getParcelableExtra(PRODUCT_KEY) as Product?
                return p?.suid
            }
            else -> {
                null
            }
        }
    }


    private fun getImageTransition(): String? {
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        when (requestCode) {

            CheckoutCalendarActivity.REQUEST_CODE -> {
                data?.let {

                    if (resultCode == Activity.RESULT_OK) {

                        val start =
                            Date(data.getLongExtra(CheckoutCalendarActivity.STARTDATE_KEY, -1))
                        val end = Date(data.getLongExtra(CheckoutCalendarActivity.ENDDATE_KEY, -1))


                        Handler().postDelayed({
                            startActivityMakeSceneForResult(
                                OrderMessageActivity.newInstance(
                                    this,
                                    getProduct(),
                                    start,
                                    end
                                ), OrderMessageActivity.REQUEST_CODE
                            )
                        }, 100)
                    }
                }
            }


            OrderMessageActivity.REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    setResult(Activity.RESULT_OK)
                    finishSafe()
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

}