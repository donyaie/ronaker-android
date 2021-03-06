package com.ronaker.app.ui.explore

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.ui.dashboard.DashboardActivity
import com.ronaker.app.ui.search.SearchActivity
import com.ronaker.app.utils.Alert
import com.ronaker.app.utils.AppDebug
import com.ronaker.app.utils.ShapeDrawableHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreFragment : BaseFragment(), DashboardActivity.MainaAtivityListener {

    private val TAG = ExploreFragment::class.java.simpleName
    private lateinit var binding: com.ronaker.app.databinding.FragmentExploreBinding

    lateinit var mFusedLocationClient: FusedLocationProviderClient

    private val viewModel: ExploreViewModel by viewModels()

    var lastLocation: LatLng? = null

    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    private var pastVisiblesItems: Int = 0
    private lateinit var locationCallback: LocationCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_explore, container, false)


        binding.viewModel = viewModel



        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemsize = requireContext().resources.getDimensionPixelSize(R.dimen.adapter_width)
        var screensize = binding.container.measuredWidth
        AppDebug.log("manager", "screenSize : $screensize")
        var count = screensize / itemsize

        if (count < 2)
            count = 2

        var mnager = GridLayoutManager(context, count)

        val productAdapter = ItemExploreAdapter()
        binding.recycler.layoutManager = mnager

        binding.recycler.adapter = productAdapter

        val vtObserver = binding.root.viewTreeObserver
        vtObserver.addOnGlobalLayoutListener {

            if (screensize == 0) {
                screensize = binding.container.measuredWidth

                AppDebug.log("manager", "screenSize 2 : $screensize")
                count = screensize / itemsize

                if (count < 2)
                    count = 2
                mnager = GridLayoutManager(context, count)
                binding.recycler.layoutManager = mnager
            }

        }


        val managerCategory = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.categoryRecycler.layoutManager = managerCategory






        binding.loading.hideLoading()

        ViewCompat.setNestedScrollingEnabled(binding.recycler, false)
        ViewCompat.setNestedScrollingEnabled(binding.categoryRecycler, false)

        viewModel.loading.observe(viewLifecycleOwner, { loading ->


            if (binding.refreshLayout.isRefreshing != loading)
                binding.refreshLayout.isRefreshing = loading


        })


        viewModel.loadingEnd.observe(viewLifecycleOwner, { loading ->


            if (loading) {
                if (binding.loadingEnd.visibility == View.GONE)
                    binding.loadingEnd.visibility = View.VISIBLE
            } else
                if (binding.loadingEnd.visibility == View.VISIBLE)
                    binding.loadingEnd.visibility = View.GONE

        })



        viewModel.listView.observe(viewLifecycleOwner, {
            productAdapter.submitList(it.toList())
        })


        viewModel.retry.observe(viewLifecycleOwner, { loading ->
            loading?.let { binding.loading.showRetry(it) } ?: run { binding.loading.hideRetry() }

        })


        viewModel.locationCheck.observe(viewLifecycleOwner, {
            chech()

        })


        viewModel.nearByChecked.observe(viewLifecycleOwner, {
            if (it) {

                ShapeDrawableHelper.changeSvgDrawableColor(
                    requireContext(),
                    R.color.colorAccent,
                    binding.nearByImage
                )
            } else {

                ShapeDrawableHelper.changeSvgDrawableColor(
                    requireContext(),
                    R.color.colorTextGreyLight,
                    binding.nearByImage
                )
            }

        })


        viewModel.scrollCategoryPosition.observe(viewLifecycleOwner, { position ->
            binding.categoryRecycler.scrollToPosition(position)


        })





        viewModel.searchText.observe(viewLifecycleOwner, { text ->


            if (text.isNullOrBlank()) {

                binding.searchText.text = getString(R.string.title_search_here)
                binding.backImage.visibility = View.VISIBLE
                binding.backImage.isClickable = false
                binding.backImage.setImageResource(R.drawable.ic_search_white)

            } else {
                binding.searchText.text = text
                binding.backImage.isClickable = true

                binding.backImage.visibility = View.VISIBLE
                binding.backImage.setImageResource(R.drawable.ic_back_white)
            }

        })


        binding.refreshLayout.setOnRefreshListener {


            viewModel.retry()
        }


        viewModel.searchValue.observe(viewLifecycleOwner, { _ ->


            val p1 =
                androidx.core.util.Pair<View, String>(binding.searchLayout, "search")
//            val p2 = androidx.core.util.Pair<View, String>(binding.cancelSearch, "searchCancel")
            val options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(activity as Activity, p1)//, p2)



            activity?.let {
                startActivityForResult(
                    SearchActivity.newInstance(it),
                    SearchActivity.ResultCode,
                    options.toBundle()
                )
            }


        })





        viewModel.findNearBy.observe(viewLifecycleOwner, {

            if (lastLocation != null)
                viewModel.setLocation(lastLocation)
            else
                chech()
        })







        viewModel.errorMessage.observe(viewLifecycleOwner, { errorMessage ->

            Alert.makeTextError(this, errorMessage)

        })

        binding.loading.oClickRetryListener = View.OnClickListener {

            viewModel.retry()


        }




        binding.scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, oldScrollY ->

            visibleItemCount = mnager.childCount
            totalItemCount = mnager.itemCount
            pastVisiblesItems = mnager.findFirstVisibleItemPosition()
            AppDebug.log(
                TAG,
                "visibleItemCount: $visibleItemCount, totalItemCount: $totalItemCount , pastVisiblesItems: $pastVisiblesItems "
            )


            if (v.getChildAt(v.childCount - 1) != null) {
                AppDebug.log(
                    TAG,
                    "scrollY: $scrollY, current: ${v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight} "
                )


                if (scrollY >= v.getChildAt(v.childCount - 1)
                        .measuredHeight - v.measuredHeight &&
                    scrollY > oldScrollY
                ) {


                    if (visibleItemCount + pastVisiblesItems >= totalItemCount) {

                        AppDebug.log(TAG, "loadMore")

                        viewModel.loadMore()
                    }


                }


            }

            if (!v.canScrollVertically(-1)) {

                if (binding.header.elevation != 0f)
                    binding.header.elevation = 0f
            } else {
                if (binding.header.elevation != 10f)
                    binding.header.elevation = 10f
            }

        })






        binding.categoryRecycler.addItemDecoration(object : RecyclerView.ItemDecoration() {

            private val mEndOffset =
                context?.resources?.getDimensionPixelSize(R.dimen.margin_default) ?: 0


            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {


                super.getItemOffsets(outRect, view, parent, state)

                val dataSize = state.itemCount
                val position: Int = parent.getChildAdapterPosition(view)
                if (dataSize > 0 && position == 0) {
                    outRect.set(mEndOffset, 0, 0, 0)
                } else {

                    outRect.set(0, 0, 0, 0)
                }
            }

        })



        binding.backImage.setOnClickListener {

            viewModel.clearSearch()
        }
        Handler(Looper.getMainLooper()).postDelayed({ binding.header.elevation = 0f }, 16)


        activity?.let {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(it)
        }


        if (activity is DashboardActivity)
            (activity as DashboardActivity).addFragmentListener(this, this)


        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?.lastLocation ?: return

                val newLocation = LatLng(locationResult.lastLocation.latitude,locationResult.lastLocation.longitude)
                lastLocation = newLocation
                viewModel.setLocation(lastLocation)
                stopLocationUpdates()

//                for (location in locationResult.locations){
//                    // Update UI with location data
//                    // ...
//                    locationResult.
//
//                }
            }
        }


    }

    private fun stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000

            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        mFusedLocationClient.requestLocationUpdates(locationRequest,
            locationCallback,
            Looper.getMainLooper())
    }


    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }


    companion object {

        fun newInstance(): ExploreFragment {
            return ExploreFragment()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == SearchActivity.ResultCode && resultCode == 0) {

            if (data != null) {
                val searchValue = data.getStringExtra(SearchActivity.Search_KEY)
                val categorySuid = data.getStringExtra(SearchActivity.Category_KEY)


                val nearBy = data.getBooleanExtra(SearchActivity.Near_KEY,false)

                if(nearBy)
                    viewModel.locationCheck.postValue(true)



                viewModel.search(searchValue, categorySuid)


            }
        }

        super.onActivityResult(requestCode, resultCode, data)


    }


    fun showLocationEnableDialog() {

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setMessage(getString(R.string.text_location_disabled))
        builder.setPositiveButton(
            getString(android.R.string.ok)
        ) { dialog, _ ->
            dialog?.cancel()
            startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
        builder.setOnDismissListener {

        }

        builder.show()
    }


    fun showLocationPermissionDialog(token: PermissionToken?) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setMessage(getString(R.string.text_location_near_by))
        builder.setPositiveButton(
            getString(android.R.string.ok)
        ) { dialog, _ ->
            dialog?.cancel()
            token?.continuePermissionRequest()

            if (token == null)
                chech()

        }
        builder.setOnDismissListener {
            token?.cancelPermissionRequest()
        }

        builder.show()
    }


    fun chech() {
        Dexter.withContext(activity)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    viewModel.emptyVisibility.postValue(View.VISIBLE)
                    showLocationPermissionDialog(token)
                }

                @SuppressLint("MissingPermission")
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {


                        mFusedLocationClient.lastLocation.addOnSuccessListener {

                                location ->
                            if (location != null) {

                                val newLocation = LatLng(location.latitude, location.longitude)
                                if (lastLocation == null) {
                                    lastLocation = newLocation

                                    viewModel.setLocation(lastLocation)
                                }
                                lastLocation = newLocation


                            } else if (lastLocation != null) {




                                viewModel.setLocation(lastLocation)
                            } else {
//                                showLocationEnableDialog()
                                startLocationUpdates()

//                                viewModel.emptyVisibility.postValue(View.VISIBLE)
                            }


                        }.addOnCanceledListener {
//                            viewModel.emptyVisibility.postValue(View.VISIBLE)

                        }


                    } else {
//                        viewModel.emptyVisibility.postValue(View.VISIBLE)
                    }
                }


            }).check()
    }





    override fun onDetach() {
        try {
//


            if (activity is DashboardActivity)
                (activity as DashboardActivity).removeFragmentListener(this)
        } catch (e: Exception) {

        }
        super.onDetach()
    }

    override fun onBackPressed(): Boolean {
        return viewModel.backPress()

    }


}