package com.ronaker.app.ui.addProduct

import android.Manifest
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Debug
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.model.Place
import com.ronaker.app.utils.view.IPagerFragment


class AddProductLocationFragment : BaseFragment(), IPagerFragment,
    AddProductLocationSearchDialog.OnDialogResultListener {



    private lateinit var binding: com.ronaker.app.databinding.FragmentProductAddLocationBinding
    private lateinit var baseViewModel: AddProductViewModel
    private lateinit var viewModel: AddProductLocationViewModel
    lateinit var mFusedLocationClient: FusedLocationProviderClient

    lateinit var mGoogleMap: GoogleMap


    var lastLocation: Location? = null


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
        activity?.let {
            baseViewModel = ViewModelProviders.of(it).get(AddProductViewModel::class.java)
            binding.parentViewModel = baseViewModel
        }

        viewModel = ViewModelProviders.of(this).get(AddProductLocationViewModel::class.java)
        binding.viewModel=viewModel





        activity?.let {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(it)
        };



        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMapAsync {
            mGoogleMap = it

            mGoogleMap.setOnCameraIdleListener {


              viewModel.updateLocation( mGoogleMap.cameraPosition.target)


            }

            chech()
        }

        binding.currentButton.setOnClickListener {
            lastLocation = null
            chech()

        }

        viewModel.loading.observe(this, Observer { value ->

                baseViewModel.loading.value=value
        })


        viewModel.newLocation.observe(this, Observer { value ->
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                value,
                17f
            )
            mGoogleMap.animateCamera(cameraUpdate)
        })



        binding.searchLayout.setOnClickListener {


            fragmentManager?.let { it1 -> AddProductLocationSearchDialog.DialogBuilder(it1).setListener(this@AddProductLocationFragment).show() }
        }



        return binding.root
    }


    override fun onDialogResult(
        result: AddProductLocationSearchDialog.DialogResultEnum,
        location: Place?
    ) {


        if(result==AddProductLocationSearchDialog.DialogResultEnum.OK)
            location?.let { viewModel.selectPlace(it) }

    }

    override fun onStart() {
        super.onStart()

    }


    fun moveCamera(location: Location) {


        if (mGoogleMap != null) {


            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                LatLng(location.latitude, location.longitude),
                17f
            )
            mGoogleMap.animateCamera(cameraUpdate)


        }


    }


    fun chech() {
        Dexter.withActivity(activity)
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

                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {

                        if (mGoogleMap != null)
                            mGoogleMap.isMyLocationEnabled = true

                        mFusedLocationClient.lastLocation.addOnSuccessListener {

                                location ->
                            if (location != null)

                                if (lastLocation == null)
                                    moveCamera(location)
                            lastLocation = location


                        }


                    }

                    if (report.isAnyPermissionPermanentlyDenied) {

                    }
                }


            }).check()
    }


    companion object {

        fun newInstance(): AddProductLocationFragment {
            return AddProductLocationFragment()
        }
    }

    override fun onSelect() {

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}