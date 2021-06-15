package com.ronaker.app.ui.profileCompleteEdit

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.browser.customtabs.CustomTabsIntent
import androidx.databinding.DataBindingUtil
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.model.Order
import com.ronaker.app.ui.docusign.DocusignActivity
import com.ronaker.app.ui.orderPreview.OrderPreviewActivity
import com.ronaker.app.ui.phoneNumberValidation.PhoneNumberActivity
import com.ronaker.app.ui.profileAuthorization.ProfileAuthorizationActivity
import com.ronaker.app.ui.profileEmailVerify.EmailVerifyDialog
import com.ronaker.app.ui.profileIdentify.ProfileIdentifyActivity
import com.ronaker.app.ui.profileImage.ProfileImageActivity
import com.ronaker.app.ui.profilePaymentList.ProfilePaymentListActivity
import com.ronaker.app.utils.Alert
import com.ronaker.app.utils.extension.setEndDrawableRes
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileCompleteActivity : BaseActivity(), EmailVerifyDialog.OnDialogResultListener {


    private lateinit var binding: com.ronaker.app.databinding.ActivityProfileCompleteBinding
    private val viewModel: ProfileCompleteViewModel by viewModels()


    companion object {

        var Stripe_KEY = "stripeLink"

        fun newInstance(context: Context): Intent {
            val intent = Intent(context, ProfileCompleteActivity::class.java)
            val boundle = Bundle()

            intent.putExtras(boundle)

            return intent
        }


//        fun newInstance(context: Context ,stripeLink:String?): Intent {
//            val intent = Intent(context, ProfileCompleteActivity::class.java)
//            val boundle = Bundle()
//            boundle.putString(Stripe_KEY,stripeLink)
//            intent.putExtras(boundle)
//
//            return intent
//        }

    }
//
//    private fun getStripeLink(): String? {
//        if (intent.hasExtra(Stripe_KEY)) {
//
//            return intent.getStringExtra(Stripe_KEY)
//
//        }
//        return null
//    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_complete)

        binding.viewModel = viewModel


//        if(getStripeLink().isNullOrEmpty()) {
//            binding.stripeLayout.visibility = View.GONE
//            binding.stripeDivider.visibility=View.GONE
//        }
//        else{
//            binding.stripeLayout.visibility=View.VISIBLE
//            binding.stripeLayout.isClickable = true
//            binding.stripeLayout.setEndDrawableRes(R.drawable.ic_guid_warning)
//
//            binding.stripeDivider.visibility=View.VISIBLE
//        }



        viewModel.errorMessage.observe(this, { errorMessage ->
            Alert.makeTextError(this, errorMessage)
        })

        viewModel.loading.observe(this, { value ->
            if (value == true) {
                binding.loading.showLoading()
            } else
                binding.loading.hideLoading()
        })



        viewModel.retry.observe(this, {value ->


            value?.let { binding.loading.showRetry(it) } ?: run { binding.loading.hideRetry() }
        })

        binding.loading.oClickRetryListener = View.OnClickListener {

            viewModel.onRetry()
        }



        viewModel.imageComplete.observe(this, {value ->
            if (value == true) {
//                binding.imageLayout.setText(R.string.title_add_profile_image_edit)

                binding.imageLayout.isClickable = true
                binding.imageLayout.setEndDrawableRes(R.drawable.ic_complete)
            } else {

                binding.imageLayout.isClickable = true
//                binding.imageLayout.setText(R.string.title_add_profile_image)
                binding.imageLayout.setEndDrawableRes(R.drawable.ic_guid_warning)
            }
        })


        viewModel.stripeComplete.observe(this, {value ->
            if (value == true) {
//                binding.imageLayout.setText(R.string.title_add_profile_image_edit)

                binding.stripeLayout.isClickable = true
                binding.stripeLayout.setEndDrawableRes(R.drawable.ic_complete)
            } else {

                binding.stripeLayout.isClickable = true
//                binding.imageLayout.setText(R.string.title_add_profile_image)
                binding.stripeLayout.setEndDrawableRes(R.drawable.ic_guid_warning)
            }
        })


        viewModel.docusignComplete.observe(this, {value ->
            if (value == true) {
//                binding.imageLayout.setText(R.string.title_add_profile_image_edit)

                binding.docusignLayout.isClickable = true
                binding.docusignLayout.setEndDrawableRes(R.drawable.ic_complete)
            } else {

                binding.docusignLayout.isClickable = true
//                binding.imageLayout.setText(R.string.title_add_profile_image)
                binding.docusignLayout.setEndDrawableRes(R.drawable.ic_guid_warning)
            }
        })



        viewModel.signComplete.observe(this, {value ->
            if (value == true) {


                binding.signLayout.isClickable = false
                binding.signLayout.setEndDrawableRes(R.drawable.ic_complete)

            } else {

                binding.signLayout.isClickable = true
                binding.signLayout.setEndDrawableRes(R.drawable.ic_guid_warning)
            }
        })


        viewModel.phoneComplete.observe(this, {value ->
            if (value == true) {

                binding.phoneLayout.isClickable = false
//                binding.phoneLayout.setText(R.string.title_add_and_verify_phone_number_edit)
                binding.phoneLayout.setEndDrawableRes(R.drawable.ic_complete)
            } else {

                binding.phoneLayout.isClickable = true
//                binding.phoneLayout.setText(R.string.title_add_and_verify_phone_number)
                binding.phoneLayout.setEndDrawableRes(R.drawable.ic_guid_warning)
            }
        })


        viewModel.identityComplete.observe(this, {value ->
            if (value == true) {

                binding.identityLayout.isClickable = false
                binding.identityLayout.setEndDrawableRes(R.drawable.ic_complete)
            } else {

                binding.identityLayout.isClickable = true
                binding.identityLayout.setEndDrawableRes(R.drawable.ic_guid_warning)
            }
        })


        viewModel.smartIDComplete.observe(this, {value ->
            if (value == true) {

                binding.authLayout.isClickable = false
                binding.authImage.setImageResource(R.drawable.ic_complete)
            } else {

                binding.authLayout.isClickable = true
                binding.authImage.setImageResource(R.drawable.ic_guid_warning)
            }
        })

        viewModel.peymentComplete.observe(this, {value ->
            if (value == true) {
                binding.paymentLayout.setEndDrawableRes(R.drawable.ic_complete)

                binding.paymentLayout.isClickable = false
//                binding.paymentLayout.setText(R.string.title_add_a_payment_method_edit)
            } else {
                binding.paymentLayout.setEndDrawableRes(R.drawable.ic_guid_warning)

                binding.paymentLayout.isClickable = true
//                binding.paymentLayout.setText(R.string.title_add_a_payment_method)
            }
        })



        binding.identityLayout.setOnClickListener {


            startActivity(ProfileIdentifyActivity.newInstance(this))


        }


        binding.signLayout.setOnClickListener {

            EmailVerifyDialog.DialogBuilder(supportFragmentManager).setListener(this).show()


        }


//        binding.stripeLayout.setOnClickListener {
//            getStripeLink()?.let {url->
//                val builder: CustomTabsIntent.Builder = CustomTabsIntent.Builder()
//                val customTabsIntent = builder.build()
//                customTabsIntent.launchUrl(this, Uri.parse(url))
//            }
//        }


        binding.imageLayout.setOnClickListener {


            startActivity(ProfileImageActivity.newInstance(this, viewModel.getAvatar()))
        }


        binding.authLayout.setOnClickListener {


            startActivity(ProfileAuthorizationActivity.newInstance(this))
        }

        binding.docusignLayout.setOnClickListener {


            startActivity(DocusignActivity.newInstance(this))
        }

        binding.phoneLayout.setOnClickListener {


            startActivity(PhoneNumberActivity.newInstance(this))

        }

        binding.paymentLayout.setOnClickListener {

            startActivity(ProfilePaymentListActivity.newInstance(this))

        }





        binding.toolbar.cancelClickListener = View.OnClickListener { onBackPressed() }


    }

    override fun onStart() {

        super.onStart()
        viewModel.loadData()


    }

    override fun onDialogResult(result: EmailVerifyDialog.DialogResultEnum) {
    }


}