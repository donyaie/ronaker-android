package com.ronaker.app.utils.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.ronaker.app.R

class InputComponent @JvmOverloads constructor(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {



    init {

        LayoutInflater.from(context)
            .inflate(R.layout.component_input, this, true)


        orientation = VERTICAL
//
//        attrs?.let {
//            val typedArray = context.obtainStyledAttributes(
//                it,
//                R.styleable.toolbar_component_attributes, 0, 0
//            )
//
//
//
//
//            typedArray.recycle()
//        }
    }



}