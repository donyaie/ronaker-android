package com.ronaker.app.ui.docusignSign

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DocusignSignActivity : BaseActivity() {
    companion object {

        const val SUID_KEY = "suid"

        fun newInstance(context: Context,id:String): Intent {
            val intent = Intent(context, DocusignSignActivity::class.java)
            val boundle = Bundle()
            boundle.putString(SUID_KEY,id)
            intent.putExtras(boundle)

            return intent
        }
    }

    private lateinit var binding: com.ronaker.app.databinding.ActivityDocusginSignBinding

    private val viewModel: DocusignSignViewModel by viewModels()

    val TAG:String ="DocusignActivity"

    val baseUrl="https://api.ronaker.com/api/v1/"



    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_docusgin_sign)
        binding.viewModel = viewModel

        binding.webView.clearHistory()
        binding.webView.clearCache(true)
        binding.webView.clearFormData()


        binding.webView.settings.javaScriptCanOpenWindowsAutomatically
        binding.webView.settings.javaScriptEnabled=true
        binding.webView.settings.loadWithOverviewMode=true
        binding.webView.settings.useWideViewPort=true

        binding.webView.webViewClient=object : WebViewClient(){

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
//            super.onReceivedSslError(view, handler, error)
            }



            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)


                if(url?.startsWith(baseUrl)==true){
                    binding.loading.visibility= View.VISIBLE
                    setResult(Activity.RESULT_OK)

                    this@DocusignSignActivity.finish()


                }
                else{
                    binding.loading.visibility= View.GONE
                }

            }



            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {
                if(request?.url.toString().startsWith(baseUrl))
                Toast.makeText(
                    this@DocusignSignActivity,
                    "Something is wrong",
                    Toast.LENGTH_LONG
                ).show()

                super.onReceivedHttpError(view, request, errorResponse)
            }



            override fun onLoadResource(view: WebView?, url: String?) {

                Log.d(TAG, url + "")

                super.onLoadResource(view, url)
            }

        }

        viewModel.error.observe(this, { message ->

            Toast.makeText(this,message,Toast.LENGTH_LONG).show()
            this.finish();

        })

        viewModel.loadUrl.observe(this, { url ->


            binding.webView.loadUrl(url.replace("172.98.197.76","staging.ronaker.com"))


        })

        getSUID()?.let {


            viewModel.getUrl(it)
        }


    }

    private fun getSUID(): String? {
        return intent?.getStringExtra(SUID_KEY)

    }



}