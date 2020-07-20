package com.ronaker.app.utils.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.ronaker.app.R
import com.ronaker.app.utils.AppDebug
import com.ronaker.app.utils.ShapeDrawableHelper


class TabNavigationComponent constructor(context: Context, attrs: AttributeSet) :
    LinearLayout(context, attrs) {


    var layoutList: ArrayList<LinearLayout>
    var imageList: ArrayList<ImageView>
    var textList: ArrayList<TextView>

    var selectedIndex: Int? = null

    var selectListener: OnSelectItemListener? = null


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


        layoutList[0].setOnClickListener { select(0) }


        layoutList[1].setOnClickListener { select(1) }

        layoutList[2].setOnClickListener { select(2) }

        layoutList[3].setOnClickListener { select(3) }

        layoutList[4].setOnClickListener { select(4) }


        select(0)

    }


    fun select(index: Int) {

//        if(index==2) {
//            selectListener?.onSelect(index)
//            return
//        }

        if (index == selectedIndex)
            selectListener?.onReSelected(index)

        selectedIndex = index

        textList.forEach {
            it.setTextColor(ContextCompat.getColor(context, R.color.colorTextGreyLight))
        }
        imageList.forEach {
//            it.setBackgroundColor(ContextCompat.getColor(context,R.color.colorTextGreyLight))
            ShapeDrawableHelper.changeSvgDrawableColor(context, R.color.colorTextGreyLight, it)
        }


        textList[index].setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
//        imageList[index].setBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent))
        ShapeDrawableHelper.changeSvgDrawableColor(context, R.color.colorAccent, imageList[index])

        try {
            selectListener?.onSelect(index)
        } catch (ex: Exception) {
            AppDebug.log("Navigator", ex)
        }

    }


    interface OnSelectItemListener {

        fun onSelect(index: Int)

        fun onReSelected(index: Int)

    }


}