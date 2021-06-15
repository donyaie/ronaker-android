package com.ronaker.app.ui.paypal

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.Toast
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.utils.view.LoadingComponent


class PaypalCheckoutActivity : BaseActivity() {

    companion object {
        val LINK_KEY = "link";
        val TOKEN_KEY = "token";
        val PAYER_ID_KEY = "payer_id";
        val LINK_Prefix = "https://www.paypal.ronaker.com/callback?";

        //https://www.paypal.ronaker.com/callback?token=1GM46052AP070372T&PayerID=GBPXG5E5PSWNE

        fun newInstance(context: Context, link: String): Intent {
            val intent = Intent(context, PaypalCheckoutActivity::class.java)
            val boundle = Bundle()
            boundle.putString(LINK_KEY, link)
            intent.putExtras(boundle)

            return intent
        }
    }

    val TAG: String = "DocusignActivity"

    lateinit var myWebView: WebView

    lateinit var loading: LoadingComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paypal)
        myWebView = findViewById(R.id.webView) as WebView
        loading = findViewById(R.id.loading) as LoadingComponent



        myWebView.clearHistory()
        myWebView.clearFormData()


        myWebView.settings.javaScriptCanOpenWindowsAutomatically
        myWebView.settings.javaScriptEnabled = true
        myWebView.settings.loadWithOverviewMode = true
        myWebView.settings.useWideViewPort = true

        myWebView.webViewClient = object : WebViewClient() {

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
//            super.onReceivedSslError(view, handler, error)
            }


            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                if (url?.startsWith(LINK_Prefix) == true) {
                    loading.visibility = View.VISIBLE
                    var token: String? = null
                    var payerID: String? = null

                    Log.d(TAG, url)
                    url.replace(LINK_Prefix, "").split('&').forEach {

                        Log.d(TAG, it)
                        if (it.contains("token"))
                            token = it.replace("token=", "")

                        if (it.contains("PayerID"))
                            payerID = it.replace("PayerID=", "")


                    }

                    val intent = Intent()
                    val boundle = Bundle()
                    boundle.putString(TOKEN_KEY, token)
                    boundle.putString(PAYER_ID_KEY, payerID)
                    intent.putExtras(boundle)

                    this@PaypalCheckoutActivity.setResult(Activity.RESULT_OK, intent)

                    this@PaypalCheckoutActivity.finish();


                } else {
                    loading.visibility = View.GONE
                }


            }


            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {
                if (request?.url.toString().startsWith(LINK_Prefix))
                    Toast.makeText(
                        this@PaypalCheckoutActivity,
                        "Something is wrong",
                        Toast.LENGTH_LONG
                    ).show()
                super.onReceivedHttpError(view, request, errorResponse)

            }


        }



        getLink()?.let {
            myWebView.loadUrl(
                it
            )

        }


    }


    private fun getLink(): String? {
        if (intent.hasExtra(LINK_KEY)) {

            return intent.getStringExtra(LINK_KEY)

        }
        return null
    }

}