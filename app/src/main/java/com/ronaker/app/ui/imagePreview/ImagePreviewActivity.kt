package com.ronaker.app.ui.imagePreview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.utils.AnimationHelper


class ImagePreviewActivity : BaseActivity() {

    private val TAG = ImagePreviewActivity::class.java.simpleName

    private lateinit var binding: com.ronaker.app.databinding.ActivityImagePreviewBinding


    companion object {
        const val IMAGE_KEY = "images"
        const val INDEX_KEY = "index"
        const val ResultCode = 339

        fun newInstance(context: Context ,images:ArrayList<String>,selectedIndex:Int): Intent {
            val intent = Intent(context, ImagePreviewActivity::class.java)
            val boundle = Bundle()
            boundle.putStringArrayList(IMAGE_KEY, images)
            boundle.putInt(INDEX_KEY, selectedIndex)
            intent.putExtras(boundle)

            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_preview)


        binding.toolbar.cancelClickListener= View.OnClickListener {
            finish()
        }

        getImages()?.let {

            binding.avatarSlide.fullScreen=true
            binding.avatarSlide.addImagesUrl(it)

            binding.avatarSlide.currentItem=getIndex()

        }?:run { finish() }


    }

    override fun onBackPressed() {
        finishAfterTransition()
    }


    override fun finish() {
        super.finish()
        AnimationHelper.clearTransition(this)
    }


    private fun getIndex(): Int {
        if (intent.hasExtra(INDEX_KEY)) {
            return intent.getIntExtra(INDEX_KEY,0)
        }
        return 0
    }



    private fun getImages(): ArrayList<String>? {
        if (intent.hasExtra(IMAGE_KEY)) {
            return intent.getStringArrayListExtra(IMAGE_KEY)
        }
        return null
    }

}