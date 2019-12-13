package com.ronaker.app.ui.profilePayment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
//import io.card.payment.CardIOActivity
//import io.card.payment.CreditCard
import android.text.style.ReplacementSpan
import androidx.annotation.NonNull
import android.text.Spanned
import android.view.ViewTreeObserver
import com.jakewharton.rxbinding2.widget.RxTextView
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.ronaker.app.utils.AnimationHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit


class ProfilePaymentActivity : BaseActivity(), ViewTreeObserver.OnScrollChangedListener {


    private lateinit var binding: com.ronaker.app.databinding.ActivityProfilePaymentBinding
    private lateinit var viewModel: ProfilePaymentViewModel

    var disposable:Disposable?=null

    companion object {

        const val MY_SCAN_REQUEST_CODE=769
        fun newInstance(context: Context): Intent {
            val intent = Intent(context, ProfilePaymentActivity::class.java)
            val boundle = Bundle()
            intent.putExtras(boundle)

            return intent
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {

        AnimationHelper.setSlideTransition(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_payment)

        viewModel = ViewModelProviders.of(this).get(ProfilePaymentViewModel::class.java)

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


        binding.scrollView.viewTreeObserver.addOnScrollChangedListener(this)


        viewModel.retry.observe(this, Observer { value ->



            value?.let {   binding.loading.showRetry(it) }?:run{binding.loading.hideRetry()}
        })

        binding.loading.oClickRetryListener = View.OnClickListener {

            viewModel.onRetry()
        }



        binding.cardScan.setOnClickListener {
            onScanCard()

        }


        binding.cardEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                val paddingSpans = editable?.getSpans(0, editable.length, SpaceSpan::class.java)
                if (paddingSpans != null) {
                    for (span in paddingSpans) {
                        editable.removeSpan(span)
                    }
                }

                editable?.let { addSpans(it) }


                   editable?.let { if(it.length>=16)
                       binding.expireInput.requestFocus() }

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            private val GROUP_SIZE = 4

            private fun addSpans(editable: Editable) {

                val length = editable.length
                var i = 1
                while (i * GROUP_SIZE < length) {
                    val index = i * GROUP_SIZE
                    editable.setSpan(
                        SpaceSpan(), index - 1, index,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    i++
                }
            }

        })




        disposable=  RxTextView.textChanges(binding.cardEdit)
            .debounce(400, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .subscribe {

                viewModel.changeCardNumber(it.toString())
            }








        binding.toolbar.cancelClickListener = View.OnClickListener { onBackPressed() }


    }

    override fun onStop() {
        super.onStop()
        disposable?.dispose()
    }




    inner class SpaceSpan : ReplacementSpan() {



        override fun getSize(
            paint: Paint,
            text: CharSequence?,
            start: Int,
            end: Int,
            fm: Paint.FontMetricsInt?
        ): Int {

            val padding = paint.measureText(" ", 0, 1)
            val textSize = paint.measureText(text, start, end)
            return (padding + textSize).toInt()
        }




        override fun draw(
            canvas: Canvas,
            text: CharSequence?,
            start: Int,
            end: Int,
            x: Float,
            top: Int,
            y: Int,
            bottom: Int, @NonNull paint: Paint
        ) {
            canvas.drawText(text?.subSequence(start, end).toString() + " ", x, y.toFloat(), paint)
        }
    }


//
//    fun scanCard(){
//        val scanIntent = Intent(this, CardIOActivity::class.java)
//
//        // customize these values to suit your needs.
//        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, false)
//        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false)
//        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false)
//        scanIntent.putExtra(CardIOActivity.EXTRA_SCAN_EXPIRY, true)
//        scanIntent.putExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON, false)
//
//        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
//        startActivityForResult(scanIntent,MY_SCAN_REQUEST_CODE )
//    }

    override fun onStart() {

        super.onStart()

        if (isFistStart()) {

           viewModel.loadData()


        }

    }


    private fun onScanCard() {
        Dexter.withActivity(this)
            .withPermissions(Manifest.permission.CAMERA)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
//                    if (report.areAllPermissionsGranted()) {
//////                        scanCard()
////                    }
////
////                    if (report.isAnyPermissionPermanentlyDenied) {
////
////                    }
                }


            }).check()
    }

    override fun onScrollChanged() {



        binding.toolbar.isBottomLine = binding.scrollView.canScrollVertically(-1)

    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//
//
//
//        super.onActivityResult(requestCode, resultCode, data)

//
//        if (requestCode == MY_SCAN_REQUEST_CODE) {
//            var resultDisplayStr: String=""
//            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
//                val mscanResult:CreditCard? = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT)
//                mscanResult?.let {scanResult->
//
//
//                    // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
//                    resultDisplayStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n"
//
//                    scanResult.cardNumber?.let {  binding.cardEdit.setText(it) }
//                    // Do something with the raw number, e.g.:
//                    // myService.setCardNumber( scanResult.cardNumber );
//
//                    if (scanResult.isExpiryValid) {
//                        resultDisplayStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n"
//
//                        binding.expireInput.text= "${scanResult.expiryMonth}/${scanResult.expiryYear}"
//                    }
//
//                    if (scanResult.cvv != null) {
//                        // Never log or display a CVV
//                        resultDisplayStr += "CVV has " + scanResult.cvv.length + " digits.\n"
//
//
//                        binding.cvvInput.text=scanResult.cvv
//                    }
//
//                    if (scanResult.postalCode != null) {
//                        resultDisplayStr += "Postal Code: " + scanResult.postalCode + "\n"
//                    }
//
//                }
//
//            } else {
//                resultDisplayStr = "Scan was canceled."
//            }
//            // do something with resultDisplayStr, maybe display it in a textView
//            // resultTextView.setText(resultDisplayStr);
//
//
//            AppDebug.Log("Card",resultDisplayStr)
//        }
//    }



}