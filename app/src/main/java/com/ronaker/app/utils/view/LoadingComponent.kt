package com.ronaker.app.utils.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.ronaker.app.R
import com.wang.avi.AVLoadingIndicatorView

class LoadingComponent constructor(context: Context, attrs: AttributeSet) :
    LinearLayout(context, attrs) {

    private var loadinLayout: ConstraintLayout

    private var retry_layout: ConstraintLayout
    private var message_title: TextView
    private var retryAction_layout: ConstraintLayout
    private var retry: ImageView
    private var progress: AVLoadingIndicatorView

    var isTransparent: Boolean = true
        set(value) {

            field = value
            if (isTransparent) {

                loadinLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorLoading
                    )
                )

                retry_layout.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorLoading
                    )
                )
                progress.setIndicatorColor(ContextCompat.getColor(context, R.color.colorPlatinGrey))

            } else {

                loadinLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.white))

                retry_layout.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                progress.setIndicatorColor(ContextCompat.getColor(context, R.color.colorPlatinGrey))
            }


        }

    var oClickRetryListener: OnClickListener? = null
        set(value) {

            field = value
            retryAction_layout.setOnClickListener(value)
        }

    init {

        LayoutInflater.from(context)
            .inflate(R.layout.component_loading, this, true)



        loadinLayout = findViewById(R.id.loading_layout)
        retry_layout = findViewById(R.id.retry_layout)
        retryAction_layout = findViewById(R.id.retryAction_layout)
        retry = findViewById(R.id.retry)
        message_title = findViewById(R.id.message_title)
        progress = findViewById(R.id.progress)

        orientation = VERTICAL

        showLoading()
//        hideRetry()


        attrs.let {
            val typedArray = context.obtainStyledAttributes(
                it,
                R.styleable.LoadingComponent, 0, 0
            )


            isTransparent = typedArray
                .getBoolean(
                    R.styleable
                        .LoadingComponent_loading_transparent,
                    true
                )

            typedArray.recycle()
        }


    }

    fun showLoading() {

//        loadinLayout.animate().cancel()
        loadinLayout.visibility = View.VISIBLE
        loadinLayout.isClickable = true
        loadinLayout.isFocusable = true
        loadinLayout.isEnabled = true
        loadinLayout.animate().alpha(1f).setDuration(50).start()

    }

    fun showRetry(message: String) {
        retryAction_layout.isClickable = true
        message_title.text = message
//        retry_layout.animate().cancel()
        retry_layout.visibility = View.VISIBLE
        retry_layout.isClickable = true
        retry_layout.isFocusable = true
        retry_layout.isEnabled = true

        retry_layout.animate().alpha(1f).setDuration(50).start()
    }


//    fun showRetry() {
//        retry.isClickable = true
//        message_title.text=""
////        retry_layout.animate().cancel()
//        retry_layout.visibility = View.VISIBLE
//        retry_layout.isClickable=true
//        retry_layout.isFocusable=true
//        retry_layout.isEnabled=true
//
//        retry_layout.animate().alpha(1f).setDuration(50).start()
//    }

    fun hideRetry() {
//        retry_layout.animate().cancel()
        message_title.text = ""
        retry_layout.animate().alpha(0f).setDuration(200).start()
        retry_layout.isClickable = false
        retry_layout.isFocusable = false
        retry_layout.isEnabled = false

        retryAction_layout.isClickable = false
    }


    fun hideLoading() {

//        loadinLayout.animate().cancel()
        loadinLayout.animate().alpha(0f).setDuration(200).start()
        loadinLayout.isClickable = false
        loadinLayout.isFocusable = false
        loadinLayout.isEnabled = false


    }


}