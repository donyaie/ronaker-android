package com.ronaker.app.utils.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.ronaker.app.utils.ScreenCalcute
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import java.util.ArrayList
import com.bumptech.glide.Glide
import com.ronaker.app.R
import com.ronaker.app.injection.module.GlideApp


class ImageSlideComponent  constructor(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {




    private var screenLibrary: ScreenCalcute


    private var containerLayout: ConstraintLayout
    private var viewPager: ViewPager

    private var adapter:ImagePagerAdapter


    private val dataList = ArrayList<String>()


    init {

        LayoutInflater.from(context)
            .inflate(R.layout.component_image_slide, this, true)

        containerLayout = findViewById(R.id.container_layout)
        viewPager = findViewById(R.id.viewpager);
        adapter=ImagePagerAdapter(context,dataList)
        orientation = VERTICAL

        screenLibrary = ScreenCalcute(context)
        viewPager.adapter=adapter


        attrs.let {
            val typedArray = context.obtainStyledAttributes(
                it,
                R.styleable.image_slide_attributes, 0, 0
            )

            typedArray.recycle()
        }

    }


    fun addImageUrl(image:String){
        dataList.add(image)
        adapter.notifyDataSetChanged()
    }

    fun addImagesUrl(image:ArrayList<String>){
        dataList.addAll(image)
        adapter.notifyDataSetChanged()
    }

    fun clearImage(){
        dataList.clear()
        adapter.notifyDataSetChanged()
    }







    private inner class ImagePagerAdapter(var context: Context,val dataList :ArrayList<String>) : PagerAdapter() {




        override fun getCount(): Int {
            return dataList.size
        }


        override fun isViewFromObject(view: View, Object: Any): Boolean {
            return view === Object
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = LayoutInflater.from(context).inflate(R.layout.component_image_slide_item, null)
            val imageView = view.findViewById(R.id.image) as ImageView
            GlideApp.with(context).load(dataList[position]).into(imageView)
            container.addView(view)
            return view
        }


        override fun destroyItem(container: View, position: Int, Object: Any) {
            (container as ViewPager).removeView(Object as View)

        }

    }


}