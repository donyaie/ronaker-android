package com.ronaker.app.ui.pdfViewer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.databinding.ActivityPdfViewerBinding

class PdfViewerActivity:BaseActivity() {

   lateinit var  binding :ActivityPdfViewerBinding


    companion object{

        val PDFURL_KEY="pdfurl"

        fun newInstance(context: Context,pdfUrl:String):Intent{
            val intent = Intent(context, PdfViewerActivity::class.java)
            val boundle = Bundle()
            boundle.putString(PDFURL_KEY, pdfUrl)
            intent.putExtras(boundle)


            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_pdf_viewer)

        getPdfUrl()?.let {


//            binding.pdfView.from

        }





    }


    fun getPdfUrl():String?{
        return intent.getStringExtra(PDFURL_KEY)
    }
}