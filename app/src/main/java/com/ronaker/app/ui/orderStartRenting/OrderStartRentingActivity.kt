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
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.model.Order
import com.ronaker.app.ui.orderAuthorization.OrderAuthorizationActivity
import com.ronaker.app.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderStartRentingActivity : BaseActivity() {


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




        viewModel.finish.observe(this, { _ ->
            setResult(Activity.RESULT_OK)
            finish()
        })



        viewModel.doSignContract.observe(this, { _ ->
            getOrder()?.let {
                startActivityForResult(
                    OrderAuthorizationActivity.newInstance(
                        this@OrderStartRentingActivity,
                        it,
                        canSign = true,startRenting = true
                    ), OrderAuthorizationActivity.REQUEST_CODE
                )
            }
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



}