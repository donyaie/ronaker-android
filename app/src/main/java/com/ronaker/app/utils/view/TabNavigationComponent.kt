package com.ronaker.app.utils.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEach
import com.ronaker.app.R


class TabNavigationComponent @JvmOverloads constructor(context: Context, attrs: AttributeSet) :
    LinearLayout(context, attrs) {


    lateinit var layoutList: ArrayList<LinearLayout>
    lateinit var imageList: ArrayList<ImageView>
    lateinit var textList: ArrayList<TextView>

    var selectedIndex:Int?=null

    var selectListener: OnSelectItemListener? = null
        set


    init {

        LayoutInflater.from(context)
            .inflate(R.layout.component_tab_navigation, this, true)

        layoutList = ArrayList()
        imageList = ArrayList()
        textList = ArrayList()

        imageList.add(findViewById(R.id.explore_image))
        layoutList.add(findViewById(R.id.explore_layout))
        textList.add(findViewById(R.id.explore_text))


        imageList.add(findViewById(R.id.history_image))
        layoutList.add(findViewById(R.id.history_layout))
        textList.add(findViewById(R.id.history_text))


        imageList.add(findViewById(R.id.addItem_image))
        layoutList.add(findViewById(R.id.addItem_layout))
        textList.add(findViewById(R.id.addItem_text))


        imageList.add(findViewById(R.id.inbox_image))
        layoutList.add(findViewById(R.id.inbox_layout))
        textList.add(findViewById(R.id.inbox_text))


        imageList.add(findViewById(R.id.profile_image))
        layoutList.add(findViewById(R.id.profile_layout))
        textList.add(findViewById(R.id.profile_text))


        orientation = VERTICAL



        layoutList.get(0).setOnClickListener(object : View.OnClickListener {

            override fun onClick(v: View?) {
                select(0)
            }
        })


        layoutList.get(1).setOnClickListener(object : View.OnClickListener {

            override fun onClick(v: View?) {
                select(1)
            }
        })

        layoutList.get(2).setOnClickListener(object : View.OnClickListener {

            override fun onClick(v: View?) {
                select(2)
            }
        })

        layoutList.get(3).setOnClickListener(object : View.OnClickListener {

            override fun onClick(v: View?) {
                select(3)
            }
        })



        layoutList.get(4).setOnClickListener(object : View.OnClickListener {

            override fun onClick(v: View?) {
                select(4)
            }
        })


        select(0)


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


    fun select(index: Int) {



        if(index==selectedIndex)
            selectListener?.onReSelected(index)

        selectedIndex=index

        textList.forEach {
            it.setTextColor(resources.getColor(R.color.colorTextGreyLight))
        }
        imageList.forEach {
            it.setBackgroundColor(resources.getColor(R.color.colorTextGreyLight))
        }


        textList.get(index).setTextColor(resources.getColor(R.color.colorAccent))
        imageList.get(index).setBackgroundColor(resources.getColor(R.color.colorAccent))
        selectListener?.onSelect(index)

    }


    interface OnSelectItemListener {

        fun onSelect(index: Int)

        fun onReSelected(index: Int)

    }


}