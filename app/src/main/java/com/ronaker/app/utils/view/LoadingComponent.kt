package com.ronaker.app.utils.view

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.ronaker.app.R

class LoadingComponent @JvmOverloads constructor(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs){

    lateinit var loadinLayout: ConstraintLayout


    init {

        LayoutInflater.from(context)
            .inflate(R.layout.component_loading, this, true)



        loadinLayout = findViewById(R.id.loading_layout)



        orientation = VERTICAL

//        attrs?.let {
//            val typedArray = context.obtainStyledAttributes(
//                it,
//                R.styleable.input_component_attributes, 0, 0
//            )
//
//            title =
//                typedArray
//                    .getString(
//                        R.styleable
//                            .input_component_attributes_input_title
//                    )
//
//
//
//            typedArray.recycle()
//        }
    }

    fun showLoading() {


        loadinLayout.visibility= View.VISIBLE

        loadinLayout.animate().alpha(1f).setDuration(200).start()

    }


    fun hideLoading() {

        loadinLayout.animate().alpha(0f).setDuration(200).setListener(object :Animator.AnimatorListener{
            override fun onAnimationEnd(animation: Animator?) {
                loadinLayout.visibility= View.GONE
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }


        }).start()


    }

}