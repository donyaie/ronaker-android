package com.ronaker.app.utils.view

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.ronaker.app.R
import com.wang.avi.AVLoadingIndicatorView

class LoadingComponent @JvmOverloads constructor(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    lateinit var loadinLayout: ConstraintLayout

    var retry_layout: ConstraintLayout
    var retry: ImageView


    var oClickRetryListener: OnClickRetryListener? = null
        set

    init {

        LayoutInflater.from(context)
            .inflate(R.layout.component_loading, this, true)



        loadinLayout = findViewById(R.id.loading_layout)
        retry_layout = findViewById(R.id.retry_layout)
        retry=findViewById(R.id.retry)


        orientation = VERTICAL

        retry.setOnClickListener(object:View.OnClickListener{
            override fun onClick(v: View?) {
                oClickRetryListener?.onClick()
            }

        })

        showLoading()
        hideRetry()

    }

    fun showLoading() {
        loadinLayout.visibility = View.VISIBLE

        loadinLayout.animate().alpha(1f).setDuration(200).start()

    }

    fun showRetry() {
        retry.isClickable= true

        retry_layout.visibility = View.VISIBLE

        retry_layout.animate().alpha(1f).setDuration(200).start()
    }

    fun hideRetry() {
        retry_layout.animate().alpha(0f).setDuration(200).setListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator?) {
                retry_layout.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }


        }).start()

        retry.isClickable= false
    }


    fun hideLoading() {

        loadinLayout.animate().alpha(0f).setDuration(200).setListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator?) {
                loadinLayout.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }


        }).start()


    }


    interface OnClickRetryListener {

        fun onClick()


    }

}