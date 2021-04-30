package com.ronaker.app.ui.orderStartRenting

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.model.Order
import com.ronaker.app.ui.orderAuthorization.OrderAuthorizationActivity
import com.ronaker.app.ui.profilePayment.ProfilePaymentActivity
import com.ronaker.app.utils.*
import com.stripe.android.ApiResultCallback
import com.stripe.android.PaymentConfiguration
import com.stripe.android.PaymentIntentResult
import com.stripe.android.Stripe
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.StripeIntent
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference

@AndroidEntryPoint
class OrderStartRentingActivity : BaseActivity() {
   val TAG :String="stripe_test"

    private lateinit var binding: com.ronaker.app.databinding.ActivityOrderStartRentingBinding
    private val viewModel: OrderStartRentingViewModel by viewModels()

    private lateinit var stripe: Stripe

    companion object {
        var Order_KEY = "order"

        var REQUEST_CODE = 352

        fun newInstance(context: Context, order: Order?): Intent {
            val intent = Intent(context, OrderStartRentingActivity::class.java)
            val boundle = Bundle()
            boundle.putParcelable(Order_KEY, order)
            intent.putExtras(boundle)

            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_start_renting)

        binding.viewModel = viewModel


        stripe = Stripe(applicationContext, PaymentConfiguration.getInstance(applicationContext).publishableKey)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        ViewCompat.setNestedScrollingEnabled(binding.recyclerView, false)


        binding.cardRecyclerView.layoutManager = LinearLayoutManager(this)
        ViewCompat.setNestedScrollingEnabled(binding.cardRecyclerView, false)



        viewModel.errorMessage.observe(this, { errorMessage ->
            Alert.makeTextError(this, errorMessage)
        })

        viewModel.loading.observe(this, { value ->
            if (value == true) {
                binding.loading.showLoading()
            } else
                binding.loading.hideLoading()
        })




        viewModel.finish.observe(this, {

            if(it){
                succeccSend()
            }else {
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        })



        viewModel.doSignContract.observe(this, { _ ->
            getOrder()?.let {
                startActivityForResult(
                    OrderAuthorizationActivity.newInstance(
                        this@OrderStartRentingActivity,
                        it,
                        canSign = true,startRenting = false
                    ), OrderAuthorizationActivity.REQUEST_CODE
                )
            }
        })




        viewModel.startRentingConfirm.observe(this, {
            startRentingConfirmation(it)
        })


        viewModel.contractPreview.observe(this, { _ ->

            startActivity(getOrder()?.let {
                OrderAuthorizationActivity.newInstance(
                    this@OrderStartRentingActivity,
                    it, true
                )
            })
        })



        binding.toolbar.cancelClickListener = View.OnClickListener {

            finish()
        }


        binding.signButton.setOnClickListener {

            viewModel.onClickSign()

        }

        binding.acceptButton.setOnClickListener {



            viewModel.onClickAccept()

        }

        binding.addPayment.setOnClickListener {

                    startActivity(ProfilePaymentActivity.newInstance(this))

        }

        viewModel.paymentInit.observe(this, { secret ->

            if(binding.cardInputWidget.paymentMethodCreateParams!=null && secret!=null){



                val confirmParams = ConfirmPaymentIntentParams//.createWithPaymentMethodCreateParams()
                    .createWithPaymentMethodCreateParams(binding.cardInputWidget.paymentMethodCreateParams!!, secret)


//                stripe = Stripe(applicationContext, PaymentConfiguration.getInstance(applicationContext).publishableKey)

                stripe.confirmPayment(this, confirmParams)
            }


        })





        initLink()


        getOrder()?.let { viewModel.loadData(it) } ?: run { finish() }


    }

    override fun onStart() {
        super.onStart()
        getOrder()?.suid?.let {

            viewModel.loadData(it)
        }


    }


    private fun getOrder(): Order? {
        if (intent.hasExtra(Order_KEY)) {

            return intent.getParcelableExtra<Order?>(Order_KEY)

        }
        return null
    }

    
    
    fun initLink(){

        val text = getString(R.string.text_payments_terms)

        val ss = SpannableString(text)


        val privacy = "Privacy Policy"
        val privacy_lt = "Privatumo politika"


        val term = "Terms & Conditions"
        val term_lt = "Naudojimosi sąlygomis"

        if (text.contains(privacy))
            ss.setSpan(
                object : ClickableSpan() {
                    override fun onClick(p0: View) {

                        IntentManeger.openUrl(this@OrderStartRentingActivity, PRIVACY_URL)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false
                    }

                },
                text.indexOf(privacy, 0, true),
                text.indexOf(privacy, 0, true) + privacy.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )



        if (text.contains(privacy_lt))
            ss.setSpan(
                object : ClickableSpan() {
                    override fun onClick(p0: View) {

                        IntentManeger.openUrl(this@OrderStartRentingActivity, PRIVACY_URL_LT)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false
                    }

                },
                text.indexOf(privacy_lt, 0, true),
                text.indexOf(privacy_lt, 0, true) + privacy_lt.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )


        if (text.contains(term))
            ss.setSpan(
                object : ClickableSpan() {
                    override fun onClick(p0: View) {

                        IntentManeger.openUrl(this@OrderStartRentingActivity, TERM_URL)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false
                    }

                },
                text.indexOf(term, 0, true),
                text.indexOf(term, 0, true) + term.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )


        if (text.contains(term_lt))
            ss.setSpan(
                object : ClickableSpan() {
                    override fun onClick(p0: View) {

                        IntentManeger.openUrl(this@OrderStartRentingActivity, TERM_URL_LT)
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false
                    }

                },
                text.indexOf(term_lt, 0, true),
                text.indexOf(term_lt, 0, true) + term_lt.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )


        binding.privecyText.text = ss
        binding.privecyText.movementMethod = LinkMovementMethod.getInstance()
        binding.privecyText.highlightColor = Color.TRANSPARENT



        binding.privecyText.setOnClickListener {
            binding.acceptTerm.isChecked = !binding.acceptTerm.isChecked
        }



        binding.acceptButton.isEnabled = false
        binding.acceptTerm.setOnCheckedChangeListener { _, isChecked ->


            binding.acceptButton.isEnabled = isChecked


            if(isChecked){
                viewModel.checkedAgreement()
            }


        }





    }





    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {

            OrderAuthorizationActivity.REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    setResult(Activity.RESULT_OK)
                    this.finish()
                }


            }


        }

        val weakActivity = WeakReference<Activity>(this)

        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, object : ApiResultCallback<PaymentIntentResult> {
            override fun onSuccess(result: PaymentIntentResult) {
                val paymentIntent = result.intent
                val status = paymentIntent.status
                if (status == StripeIntent.Status.RequiresCapture ||status == StripeIntent.Status.Succeeded ) {
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    paymentIntent.id?.let { viewModel.chechPayment(it) };

                    AppDebug.log(TAG,"Payment succeeded "+ gson.toJson(paymentIntent))
//                    displayAlert(weakActivity.get(), "Payment succeeded", gson.toJson(paymentIntent), restartDemo = true)
                } else {
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    AppDebug.log(TAG, "Payment failed "+ gson.toJson(paymentIntent) ?: "")
//                    displayAlert(weakActivity.get(), "Payment failed", paymentIntent.lastPaymentError?.message ?: "")
                }
            }

            override fun onError(e: Exception) {
                AppDebug.log(TAG, "Payment failed "+ e.toString())
            }
        })

        super.onActivityResult(requestCode, resultCode, data)
    }




    private fun succeccSend() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        builder.setMessage(getString(R.string.text_success_sign_and_start_renting))
        builder.setPositiveButton(
            getString(android.R.string.ok)

        ) { dialog, _ ->
            this.setResult(Activity.RESULT_OK)
            dialog?.dismiss()
        }


        builder.setOnDismissListener {
            this.setResult(Activity.RESULT_OK)
            this.finish()
        }

        builder.setCancelable(true)

        builder.show()
    }


    private fun startRentingConfirmation(address:String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        builder.setMessage(String.format( getString(R.string.text_confirm_start_renting),address))
        builder.setPositiveButton(
            getString(android.R.string.ok)

        ) { dialog, _ ->
            dialog?.dismiss()
        }

        builder.setCancelable(true)

        builder.show()
    }




}