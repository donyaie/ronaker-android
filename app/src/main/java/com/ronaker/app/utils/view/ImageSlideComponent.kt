package com.ronaker.app.utils.view

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.ronaker.app.R
import com.ronaker.app.injection.module.GlideApp
import com.ronaker.app.utils.ScreenCalculator
import java.util.*


class ImageSlideComponent constructor(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs),
    ViewPager.OnPageChangeListener {


    var dotCount: Int = 1
        set(value) {
            field = value
            initDotCount()
        }

    private var countDots: LinearLayout

    private lateinit var dots: Array<ImageView?>


    private var screenLibrary: ScreenCalculator


    private var containerLayout: ConstraintLayout
    private var viewPager: ViewPager

    private var adapter: ImagePagerAdapter


    private val dataList = ArrayList<String>()


    init {

        LayoutInflater.from(context)
            .inflate(R.layout.component_image_slide, this, true)

        containerLayout = findViewById(R.id.container_layout)
        viewPager = findViewById(R.id.viewpager)

        countDots = findViewById(R.id.countDots)
        adapter = ImagePagerAdapter(context, dataList)

        screenLibrary = ScreenCalculator(context)
        viewPager.adapter = adapter

        viewPager.addOnPageChangeListener(this)




        attrs.let {
            val typedArray = context.obtainStyledAttributes(
                it,
                R.styleable.ImageSlideComponent, 0, 0
            )

            typedArray.recycle()
        }

    }


    private fun initDotCount() {

        if (dotCount > 1) {
            dots = arrayOfNulls(dotCount)
            countDots.removeAllViewsInLayout()
            for (i in 0 until dotCount) {
                dots[i] = ImageView(context)
                dots[i]?.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.navigate_dot_normal
                    )
                )

                val params = LinearLayout.LayoutParams(
                    screenLibrary.convertDPtoPixel(9),
                    screenLibrary.convertDPtoPixel(9)
                )

                params.setMargins(21, 0, 21, 0)

                dots[i]?.scaleType = ImageView.ScaleType.FIT_CENTER

                countDots.addView(dots[i], params)
            }

            dots[0]?.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.navigate_dot_select
                )
            )
        }
    }


//    fun addImageUrl(image:String){
//        dataList.add(image)
//        adapter.notifyDataSetChanged()
//    }

    fun addImagesUrl(image: ArrayList<String>) {

        dataList.addAll(image)
        adapter.notifyDataSetChanged()
        dotCount = dataList.size

        showNavigator(true, viewPager.currentItem)
    }

    fun clearImage() {
        dataList.clear()
        adapter.notifyDataSetChanged()
    }


    private inner class ImagePagerAdapter(var context: Context, val dataList: ArrayList<String>) :
        PagerAdapter() {


        override fun getCount(): Int {
            return dataList.size
        }


        override fun isViewFromObject(view: View, Object: Any): Boolean {
            return view === Object
        }

        //        @SuppressLint("InflateParams")
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = View.inflate(context, R.layout.component_image_slide_item, null)


            val imageView = view.findViewById(R.id.image) as ImageView
            GlideApp.with(context).load(dataList[position]).into(imageView)
            container.addView(view)
            return view
        }


        override fun destroyItem(container: View, position: Int, Object: Any) {
            (container as ViewPager).removeView(Object as View)

        }

    }


    fun showNavigator(visiable: Boolean, position: Int) {

        if (dotCount <= 1)
            return

        if (visiable) {

            for (i in 0 until dotCount) {
                if (i == position) {


                    dots[position]?.animate()?.scaleX(0f)?.scaleY(0f)?.setDuration(100)
                        ?.setListener(object : Animator.AnimatorListener {
                            override fun onAnimationStart(animation: Animator) {

                            }

                            override fun onAnimationEnd(animation: Animator) {

                                dots[position]?.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        context,
                                        R.drawable.navigate_dot_select
                                    )
                                )
                                dots[position]?.setPadding(0, 0, 0, 0)
                                dots[position]?.animate()?.scaleX(1f)?.scaleY(1f)?.setDuration(200)
                                    ?.setListener(null)
                                    ?.start()
                            }

                            override fun onAnimationCancel(animation: Animator) {

                            }

                            override fun onAnimationRepeat(animation: Animator) {

                            }
                        })?.start()
                } else {
                    dots[i]?.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.navigate_dot_normal
                        )
                    )
                    dots[i]?.setPadding(3, 3, 3, 3)
                    dots[i]?.animate()?.scaleX(1f)?.scaleY(1f)?.setDuration(200)?.setListener(null)
                        ?.start()

                }


            }


        } else {

            for (i in 0 until dotCount) {
                dots[i]?.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.navigate_dot_normal
                    )
                )
                dots[i]?.setPadding(3, 3, 3, 3)
                dots[i]?.animate()?.scaleX(0f)?.scaleY(0f)?.setDuration(100)?.setListener(null)
                    ?.start()

            }


        }


    }


    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        showNavigator(true, position)
    }


}