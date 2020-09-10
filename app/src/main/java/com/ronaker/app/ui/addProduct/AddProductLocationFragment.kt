package com.ronaker.app.ui.addProduct

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.model.Place
import com.ronaker.app.ui.searchLocationDialog.AddProductLocationSearchDialog
import com.ronaker.app.utils.DEFULT_LOCATION
import com.ronaker.app.utils.KeyboardManager
import com.ronaker.app.utils.MAP_ZOOM
import com.ronaker.app.utils.ScreenCalculator
import com.ronaker.app.utils.view.IPagerFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddProductLocationFragment : BaseFragment(), IPagerFragment,
    AddProductLocationSearchDialog.OnDialogResultListener, GoogleMap.OnCameraIdleListener {

    var isFirstIdle = false
    var isFirstSelected = false

    override fun onCameraIdle() {
        if (isFirstIdle) {
            viewModel.updateLocation(mGoogleMap.cameraPosition.target)
        } else if (isFirstSelected) {
            isFirstIdle = true
        }
    }

//    private lateinit var screenLibrary: ScreenCalculator

    private lateinit var binding: com.ronaker.app.databinding.FragmentProductAddLocationBinding


    private val viewModel: AddProductLocationViewModel by viewModels()
    private val baseViewModel: AddProductViewModel by activityViewModels()

    lateinit var mFusedLocationClient: FusedLocationProviderClient

    lateinit var mGoogleMap: GoogleMap


    var lastLocation: LatLng? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_product_add_location,
            container,
            false
        )

        binding.parentViewModel = baseViewModel


        binding.viewModel = viewModel





        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        var mheight = 0

        val vtObserver = binding.root.viewTreeObserver
           vtObserver.addOnGlobalLayoutListener {

               activity?.let {
               if (mheight != it.window.decorView.measuredHeight) {
                   mheight = it.window.decorView.measuredHeight
                   val mstatusSize = ScreenCalculator.getStatusBarSize(requireActivity())

                   binding.containerMap.layoutParams.height =
                       (mheight - mstatusSize - requireContext().resources.getDimension(R.dimen.toolbar_size)).toInt()
                   binding.scrollView.layoutParams.height =
                       (mheight - mstatusSize - requireContext().resources.getDimension(R.dimen.toolbar_size)).toInt()

               }
               }

           }



        activity?.let {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(it)
        }


        val mapFragment = SupportMapFragment()

        childFragmentManager
            .beginTransaction()
            .replace(R.id.mapFragment, mapFragment)
            .commit()


        mapFragment.getMapAsync {
            mGoogleMap = it
            mGoogleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    context, R.raw.style_json
                )
            )

            baseViewModel.productLocation.value?.let { it3 ->

                isFirstIdle = true
                isFirstSelected = true
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                    it3,
                    17f
                )
                mGoogleMap.moveCamera(cameraUpdate)


            } ?: run {
//                isFirstSelected = true
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                    DEFULT_LOCATION,
                    10f
                )
                mGoogleMap.moveCamera(cameraUpdate)

            }


            chech(false)
            mGoogleMap.setOnCameraIdleListener(this@AddProductLocationFragment)


        }

        binding.currentButton.setOnClickListener {
            lastLocation = null
            chech(true)

        }

//        viewModel.loading.observe(this, {value ->
//
//            baseViewModel.loading.value = value
//        })


        baseViewModel.productLocation.observe(viewLifecycleOwner, { value ->
            moveCamera(value)

        })


        viewModel.newLocation.observe(viewLifecycleOwner, { value ->
            moveCamera(value)
        })



        binding.searchLayout.setOnClickListener {


            childFragmentManager.let { it1 ->
                AddProductLocationSearchDialog.DialogBuilder(it1)
                    .setListener(this@AddProductLocationFragment).show()
            }
        }







    }

    override fun onDialogResult(
        result: AddProductLocationSearchDialog.DialogResultEnum,
        location: Place?
    ) {


        if (result == AddProductLocationSearchDialog.DialogResultEnum.OK)
            location?.let { viewModel.selectPlace(it) }

    }

    override fun onStart() {
        super.onStart()

        activity?.let { KeyboardManager.hideSoftKeyboard(it) }


    }


    fun moveCamera(location: LatLng) {


        if (::mGoogleMap.isInitialized) {


            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                location,
                MAP_ZOOM
            )
            mGoogleMap.animateCamera(cameraUpdate)


        }


    }


    fun chech(move: Boolean) {
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
                    token?.continuePermissionRequest()
                }

                @SuppressLint("MissingPermission")
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {

                        if (::mGoogleMap.isInitialized) {
                            mGoogleMap.isMyLocationEnabled = true
                            mGoogleMap.uiSettings.isMyLocationButtonEnabled = false
                        }

                        mFusedLocationClient.lastLocation.addOnSuccessListener {

                                location ->
                            if (location != null) {


                                val newLocation = LatLng(location.latitude, location.longitude)


                                if (lastLocation == null)
                                    if (move)
                                        moveCamera(newLocation)

                                lastLocation = newLocation
                            }


                        }


                    }

//                    if (report.isAnyPermissionPermanentlyDenied) {
//
//                    }
                }


            }).check()
    }


    companion object {

        fun newInstance(): AddProductLocationFragment {
            return AddProductLocationFragment()
        }
    }


    override fun onSelect() {
        isFirstSelected = true


    }


}