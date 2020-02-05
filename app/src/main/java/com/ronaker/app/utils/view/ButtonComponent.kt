package com.ronaker.app.utils.view

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.ronaker.app.R
import com.ronaker.app.utils.extension.getParentActivity



class ButtonComponent constructor(context: Context, attrs: AttributeSet) :
    RelativeLayout(context, attrs){



    init {

        LayoutInflater.from(context)
            .inflate(R.layout.component_button, this, true)




        attrs.let {
            val typedArray = context.obtainStyledAttributes(
                it,
                R.styleable.ButtonComponent, 0, 0
            )




            typedArray.recycle()
        }
    }



}