package com.ronaker.app.ui.docusign

import android.content.Context
import android.content.Intent
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.Toast
import com.docusign.androidsdk.DocuSign
import com.docusign.androidsdk.exceptions.DSAuthenticationException
import com.docusign.androidsdk.exceptions.DocuSignNotInitializedException
import com.docusign.androidsdk.listeners.DSAuthenticationListener
import com.docusign.androidsdk.models.DSUser
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.utils.view.LoadingComponent


class DocusignActivity : BaseActivity() {
    companion object {
        fun newInstance(context: Context): Intent {
            val intent = Intent(context, DocusignActivity::class.java)
            val boundle = Bundle()
            intent.putExtras(boundle)

            return intent
        }
    }

    val TAG:String ="DocusignActivity"

   lateinit var  myWebView:WebView

    lateinit var  loading: LoadingComponent

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

//        myWebView=WebView(this)

        setContentView(R.layout.activity_docusgin)
        myWebView=findViewById(R.id.webView)as WebView
        loading=findViewById(R.id.loading) as LoadingComponent



        myWebView.clearHistory()
        myWebView.clearFormData()


        myWebView.settings.javaScriptCanOpenWindowsAutomatically
        myWebView.settings.javaScriptEnabled=true
        myWebView.settings.loadWithOverviewMode=true
        myWebView.settings.useWideViewPort=true

        myWebView.webViewClient=object : WebViewClient(){

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
//            super.onReceivedSslError(view, handler, error)
            }
//
//            override fun shouldOverrideUrlLoading(
//                view: WebView?,
//                request: WebResourceRequest?
//            ): Boolean {
//
//
//            }



            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                loading.visibility= View.GONE

            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {

                if(request?.url?.toString()?.startsWith("ronaker://docusign/")==true){
                    loading.visibility=View.VISIBLE
                }
                return super.shouldOverrideUrlLoading(view, request)
            }



            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {


                if (request?.isForMainFrame ==true && error != null) {
                    loading.visibility=View.VISIBLE
                }
//                super.onReceivedError(view, request, error)
            }

            override fun onLoadResource(view: WebView?, url: String?) {

                Log.d(TAG, url + "")

                if(url?.startsWith("ronaker://docusign/")==true){
                    loading.visibility= View.VISIBLE
                    var accessToken:String?=null
                    var expires_in:Int?=null
                    var state:String?=null

                    Log.d(TAG,url)
                    url.replace("ronaker://docusign/#", "").split('&').forEach{

                        Log.d(TAG, it)
                        if(it.contains("access_token"))
                            accessToken=it.replace("access_token=", "")

                        if(it.contains("expires_in"))
                            expires_in= it.replace("expires_in=", "").toIntOrNull()


                        if(it.contains("state"))
                            state=it.replace("state=", "")

                    }



                    if(accessToken!=null &&expires_in!=null  ){


                        startDocusign(accessToken!!, expires_in!!)

                    }


                }
                else{
//                    loading.visibility= View.GONE
                }


                super.onLoadResource(view, url)
            }

        }




        myWebView.loadUrl(
            "https://account-d.docusign.com/oauth/auth?" +
                    "response_type=code" +
                    "&scope=signature" +
                    "&client_id=f8cd5587-58e6-408d-abc6-b4ad0f088207" +
                    "&state=YOUR_CUSTOM_STATE" +
                    "&redirect_uri=ronaker://docusign"+
                    "&login_hint=alidonyaie@gmail.com"
        )

//        "https://account-d.docusign.com/oauth/auth?\n" +
//                "\n" +
//                "   response_type=code\n" +
//                "\n" +
//                "   &scope=YOUR_REQUESTED_SCOPES\n" +
//                "\n" +
//                "   &client_id=YOUR_INTEGRATION_KEY\n" +
//                "\n" +
//                "   &state=YOUR_CUSTOM_STATE\n" +
//                "\n" +
//                "   &redirect_uri=YOUR_REDIRECT_URI\n" +
//                "\n" +
//                "   &login_hint=YOUR_LOGIN_HINT"
//
//http://example.com/callback/?code=eyJ0eXAi.....81QFsje43QVZ_gw

    }


    val docusignAuthDelegate = DocuSign.getInstance().getAuthenticationDelegate()

    val requestCode=4535


    fun startDocusign(token: String, expire: Int){

        try {

            docusignAuthDelegate.login(token, null, expire, this,

                object : DSAuthenticationListener {
                    override fun onSuccess(user: DSUser) {
                        // TODO: handle successful authentication here
                        Toast.makeText(this@DocusignActivity, "Success $user", Toast.LENGTH_LONG)
                            .show()

                        Log.d(TAG, user.toString());

                        this@DocusignActivity.finish()

                    }

                    override fun onError(exception: DSAuthenticationException) {

                        Toast.makeText(
                            this@DocusignActivity,
                            "Error " + exception.message,
                            Toast.LENGTH_LONG
                        ).show()
                        // TODO: handle authentication failure here
                    }
                }
            )
        } catch (exception: DocuSignNotInitializedException) {
            // TODO: handle error. This means the SDK object was not properly initialized

            Toast.makeText(
                this@DocusignActivity,
                "Exception " + exception.toString(),
                Toast.LENGTH_LONG
            ).show()
        }


    }

}