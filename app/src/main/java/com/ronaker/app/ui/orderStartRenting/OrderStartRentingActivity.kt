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
import android.util.AttributeSet
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.model.Order
import com.ronaker.app.ui.docusignSign.DocusignSignActivity
import com.ronaker.app.ui.orderAuthorization.OrderAuthorizationActivity
import com.ronaker.app.ui.paypal.PaypalCheckoutActivity
import com.ronaker.app.ui.profilePayment.ProfilePaymentActivity
import com.ronaker.app.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderStartRentingActivity : BaseActivity() {
   val TAG :String="stripe_test"

    private lateinit var binding: com.ronaker.app.databinding.ActivityOrderStartRentingBinding
    private val viewModel: OrderStartRentingViewModel by viewModels()

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
    val paypalLink =
        this.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { it ->
            // Handle the returned Uri
            if (it.resultCode == Activity.RESULT_OK) {
                val token: String? = it.data?.getStringExtra(PaypalCheckoutActivity.TOKEN_KEY)

                viewModel.payByPaypal(token);


            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_start_renting)

        binding.viewModel = viewModel


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

        viewModel.paypalPayLink.observe(this, { value ->

            paypalLink.launch(PaypalCheckoutActivity.newInstance(this@OrderStartRentingActivity,value))

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

               startActivity(DocusignSignActivity.newInstance(this@OrderStartRentingActivity,it.suid))


//                startActivityForResult(
//                    OrderAuthorizationActivity.newInstance(
//                        this@OrderStartRentingActivity,
//                        it,
//                        canSign = true,startRenting = false
//                    ), OrderAuthorizationActivity.REQUEST_CODE
//                )
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






        initLink()


        getOrder()?.let { viewModel.loadData(it) } ?: run { finish() }




    }


    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)




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
        val term_lt = "Naudojimosi s??lygomis"

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