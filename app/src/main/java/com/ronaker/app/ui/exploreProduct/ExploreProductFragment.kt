package com.ronaker.app.ui.exploreProduct

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
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
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.model.Product
import com.ronaker.app.ui.chackoutCalendar.CheckoutCalendarFragment
import com.ronaker.app.ui.container.ContainerActivity
import com.ronaker.app.ui.orderMessage.OrderMessageFragment
import com.ronaker.app.utils.*
import com.ronaker.app.utils.extension.finishSafe
import com.ronaker.app.utils.extension.startActivityMakeSceneForResult
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.util.ContentMetadata
import io.branch.referral.util.LinkProperties
import java.util.*


class ExploreProductFragment : BaseFragment(), ViewTreeObserver.OnScrollChangedListener {

    private lateinit var binding: com.ronaker.app.databinding.FragmentProductExploreBinding
    private lateinit var viewModel: ExploreProductViewModel


    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_product_explore, container, false)



        //calcut image height
        val screenCalculator = ScreenCalculator(requireContext())
        binding.avatarLayout.layoutParams.height = (screenCalculator.screenWidthPixel * 0.7).toInt()




        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(ExploreProductViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            Alert.makeTextError(this, errorMessage)
        })




        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        ViewCompat.setNestedScrollingEnabled(binding.recycler, false)


        binding.refreshLayout.setOnRefreshListener {


            viewModel.onRefresh()
        }

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            if (errorMessage != null) Alert.makeTextError(this, errorMessage)
        })

        viewModel.imageList.observe(viewLifecycleOwner, Observer { images ->

            binding.avatarSlide.clearImage()
            binding.avatarSlide.addImagesUrl(images)
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { value ->
            if (value == true) {
                binding.loading.visibility = View.VISIBLE
                binding.loading.showLoading()
            } else
                binding.loading.hideLoading()
        })

        viewModel.loadingComment.observe(viewLifecycleOwner, Observer { value ->
            if (value == true) {
                binding.commentLoading.visibility = View.VISIBLE
                binding.commentLoading.showLoading()
            } else
                binding.commentLoading.hideLoading()
        })

        viewModel.isFavorite.observe(viewLifecycleOwner, Observer { value ->
            if (value == true) {
                binding.toolbar.action2Src = R.drawable.ic_fave_primery
            } else
                binding.toolbar.action2Src = R.drawable.ic_fave_white
        })


        viewModel.loadingRefresh.observe(viewLifecycleOwner, Observer { loading ->
            binding.refreshLayout.isRefreshing = loading

        })

        viewModel.retry.observe(viewLifecycleOwner, Observer { value ->

            value?.let { binding.loading.showRetry(it) } ?: run { binding.loading.hideRetry() }

        })

        binding.loading.oClickRetryListener = View.OnClickListener {

            viewModel.onRetry()
        }

        binding.toolbar.cancelClickListener =
            View.OnClickListener { requireActivity().onBackPressed() }



        binding.toolbar.action1BouttonClickListener = View.OnClickListener {


            viewModel.mProduct?.let {

                generateShareLink(it)

            }


        }



        binding.toolbar.action2BouttonClickListener = View.OnClickListener {

            getCurrentSUID()?.let { viewModel.setFavorite(it) }

        }


        binding.scrollView.viewTreeObserver.addOnScrollChangedListener(this)


        viewModel.productLocation.observe(viewLifecycleOwner, Observer { suid ->

            addMarker(suid)
        })

        viewModel.checkout.observe(viewLifecycleOwner, Observer { _ ->
            viewModel.mProduct?.let {


                requireActivity().startActivityMakeSceneForResult(
                    ContainerActivity.newInstance(
                        requireContext(),
                        CheckoutCalendarFragment::class.java,
                        CheckoutCalendarFragment.newBoundle(it)
                    )
                    , CheckoutCalendarFragment.REQUEST_CODE
                )

            }

        })



        initMap()



    }


    override fun onStart() {
        super.onStart()



        if ((requireActivity() as BaseActivity).isFistStart()) {


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


    private fun initMap() {

        val mapFragment = SupportMapFragment()
        childFragmentManager
            .beginTransaction()
            .replace(R.id.map, mapFragment)
            .commit()




        mapFragment.getMapAsync { map ->
            googleMap = map
            googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(), R.raw.style_json
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
        return arguments?.getString(SUID_KEY)

    }


    private fun getCurrentSUID(): String? {
        return when {
            arguments?.containsKey(SUID_KEY) == true -> {
                arguments?.getString(SUID_KEY)
            }
            arguments?.containsKey(PRODUCT_KEY) == true -> {
                val p = arguments?.getParcelable(PRODUCT_KEY) as Product?
                return p?.suid
            }
            else -> {
                null
            }
        }
    }


    fun getProduct(): Product? {
        return arguments?.getParcelable(PRODUCT_KEY)
    }


    companion object {


        const val REQUEST_CODE = 345
        const val SUID_KEY = "suid"
        const val PRODUCT_KEY = "product"


        fun newBoundle(product: Product): Bundle {
            val boundle = Bundle()
            boundle.putParcelable(PRODUCT_KEY, product)

            return boundle
        }

        private val TAG = ExploreProductFragment::class.java.simpleName

        fun isHavePending(product: Product): Boolean {
            product.suid?.let { return BaseActivity.isTAGInStack(TAG + it) } ?: run { return false }
        }

        fun newBoundle(suid: String): Bundle {
            val boundle = Bundle()
            boundle.putString(SUID_KEY, suid)
            return boundle
        }


    }





    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {

            CheckoutCalendarFragment.REQUEST_CODE -> {
                data?.let {

                    if (resultCode == Activity.RESULT_OK) {

                        val start =
                            Date(data.getLongExtra(CheckoutCalendarFragment.STARTDATE_KEY, -1))
                        val end = Date(data.getLongExtra(CheckoutCalendarFragment.ENDDATE_KEY, -1))


                        Handler().postDelayed({
//                            requireActivity().startActivityMakeSceneForResult(
//                                OrderMessageActivity.newInstance(
//                                    requireContext(),
//                                    getProduct(),
//                                    start,
//                                    end
//                                ), OrderMessageActivity.REQUEST_CODE
//                            )


                            requireActivity().startActivityMakeSceneForResult(
                                ContainerActivity.newInstance(
                                    requireContext(),
                                    OrderMessageFragment::class.java,
                                    OrderMessageFragment.newBundle(
                                        getProduct(),
                                        start,
                                        end)
                                )
                                , OrderMessageFragment.REQUEST_CODE
                            )


                        }, 100)
                    }
                }
            }


            OrderMessageFragment.REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    requireActivity().setResult(Activity.RESULT_OK)
                    requireActivity().finishSafe()
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


        buo.generateShortUrl(requireContext(), lp) { url, error ->
            if (error == null) {
                Log.i("BRANCH SDK", "got my Branch link to share: $url")
                it.suid?.let { it1 -> getAnalytics()?.actionShareProduct(it1) }
                IntentManeger.shareTextUrl(requireContext(), "Share Product:", url)
            }
        }

        buo.listOnGoogleSearch(requireContext())

    }


}
