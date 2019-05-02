package com.ronaker.app.utils.view

import android.animation.Animator
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ronaker.app.R
import com.ronaker.app.utils.ScreenCalcute
import com.ronaker.app.utils.ShapeDrawableHelper


class ToolbarComponent @JvmOverloads constructor(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {


    enum class CenterContainer {
        NONE, DOTS, TITLE
    }

    enum class CancelContainer {
        NONE, BACK, CLOSE
    }

    enum class ActionContainer {
        NONE, TEXT, TWO_BUTTON, ONE_BUTTON
    }

     var centerContainer: CenterContainer = CenterContainer.TITLE
        set(value) {

            field = value
            when (centerContainer) {
                CenterContainer.NONE -> {
                    titleText.visibility = View.GONE
                    countDots.visibility = View.GONE
                }

                CenterContainer.TITLE -> {
                    titleText.visibility = View.VISIBLE
                    countDots.visibility = View.GONE
                }
                CenterContainer.DOTS -> {

                    titleText.visibility = View.GONE
                    countDots.visibility = View.VISIBLE

                    initDotCount()
                }
            }
        }

     var cancelContainer: CancelContainer = CancelContainer.BACK
    set(value) {

        field = value
        when (cancelContainer) {
            CancelContainer.NONE -> {
                cancelButton.visibility = View.GONE
            }
            CancelContainer.BACK -> {

                cancelButton.visibility = View.VISIBLE

                if (isTransparent)
                    cancelButton.setImageResource(R.drawable.ic_back_white)
                else
                    ShapeDrawableHelper.setIconSVGColorRes(
                        context,
                        cancelButton,
                        R.drawable.ic_back_white,
                        R.color.colorIconDark
                    )


            }
            CancelContainer.CLOSE -> {

                cancelButton.visibility = View.VISIBLE

                if (isTransparent)
                    cancelButton.setImageResource(R.drawable.ic_close)
                else
                    ShapeDrawableHelper.setIconSVGColorRes(
                        context,
                        cancelButton,
                        R.drawable.ic_close,
                        R.color.colorIconDark
                    )
            }

        }
    }

     var actionContainer: ActionContainer = ActionContainer.NONE
    set(value) {

        field = value

        when (actionContainer) {
            ActionContainer.NONE -> {
                actionLayout.visibility = View.GONE
            }
            ActionContainer.TEXT -> {
                actionLayout.visibility = View.VISIBLE
                actionText.visibility = View.VISIBLE
            }
            else -> {
                actionLayout.visibility = View.GONE
            }
        }

    }

    private lateinit var screenLibrary: ScreenCalcute

     var isTransparent: Boolean = false
        set(value) {

            field = value
            if (isTransparent) {
                containerLayout.setBackgroundColor(resources.getColor(R.color.transparent))
                actionText.setTextColor(resources.getColor(R.color.colorTextLight))
                titleText.setTextColor(resources.getColor(R.color.colorTextLight))

            } else {
                containerLayout.setBackgroundColor(resources.getColor(R.color.white))
                actionText.setTextColor(resources.getColor(R.color.colorTextDark))
                titleText.setTextColor(resources.getColor(R.color.colorTextDark))
            }
        }

     var isBottomLine: Boolean = false
        set(value) {
            field = value
            lineLayout.visibility = if (isBottomLine) View.VISIBLE else View.GONE
        }

     var title: String? = null
        set(value) {
            field = value
            titleText.setText(value)
        }
     var actionTitle: String? = null
        set(value) {
            field = value
            actionText.setText(value)
        }

     var dotCount: Int = 3
        set(value) {
            field = value
            initDotCount()
        }


    private lateinit var dots: Array<ImageView?>

    private var titleText: TextView

    private var lineLayout: RelativeLayout
    private var actionText: TextView
    private var actionLayout: LinearLayout

    private var cancelButton: ImageView

    private var countDots: LinearLayout

    private var containerLayout: ConstraintLayout

     var cancelClickListener: OnClickListener? = null
        set(value) {
            field = value
            cancelButton.setOnClickListener(cancelClickListener)

        }

     var actionTextClickListener: OnClickListener? = null
        set(value) {

            field = value
            actionText.setOnClickListener(actionTextClickListener)

        }


    init {

        LayoutInflater.from(context)
            .inflate(R.layout.component_toolbar, this, true)

        titleText = findViewById(R.id.title)
        lineLayout = findViewById(R.id.line)
        actionText = findViewById(R.id.action_text)
        actionLayout = findViewById(R.id.action_Layout)
        cancelButton = findViewById(R.id.cancel_button)
        countDots = findViewById(R.id.countDots)
        containerLayout = findViewById(R.id.container_layout)

        orientation = VERTICAL

        screenLibrary = ScreenCalcute(context)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(
                it,
                R.styleable.toolbar_component_attributes, 0, 0
            )




            actionTitle = typedArray
                .getString(
                    R.styleable
                        .toolbar_component_attributes_toolbar_component_action_text
                )


            isBottomLine = typedArray
                .getBoolean(
                    R.styleable
                        .toolbar_component_attributes_toolbar_component_bottom_line,
                    false
                )



            isTransparent = typedArray
                .getBoolean(
                    R.styleable
                        .toolbar_component_attributes_toolbar_component_transparent,
                    false
                )


            centerContainer = CenterContainer.values()[typedArray
                .getInt(
                    R.styleable
                        .toolbar_component_attributes_toolbar_component_center_container,
                    0
                )];


            actionContainer = ActionContainer.values()[typedArray
                .getInt(
                    R.styleable
                        .toolbar_component_attributes_toolbar_component_action_container,
                    2
                )];



            cancelContainer = CancelContainer.values()[typedArray
                .getInt(
                    R.styleable
                        .toolbar_component_attributes_toolbar_component_cancel_container,
                    1
                )];



            title = typedArray
                .getString(
                    R.styleable
                        .toolbar_component_attributes_toolbar_component_title
                )



            dotCount = typedArray
                .getInt(
                    R.styleable
                        .toolbar_component_attributes_toolbar_component_dot_count,
                    3
                )


            typedArray.recycle()
        }
    }


    private fun initDotCount() {

        if (dotCount > 0) {
            dots = arrayOfNulls(dotCount)
            countDots.removeAllViewsInLayout()
            for (i in 0 until dotCount) {
                dots[i] = ImageView(context)
                dots[i]!!.setImageDrawable(getResources().getDrawable(com.ronaker.app.R.drawable.navigate_dot_normal))

                val params = LinearLayout.LayoutParams(
                    screenLibrary.DP2Pixel(9),
                    screenLibrary.DP2Pixel(9)
                )

                params.setMargins(21, 0, 21, 0)

                dots[i]!!.scaleType = ImageView.ScaleType.FIT_CENTER

                countDots.addView(dots[i], params)
            }

            dots[0]!!.setImageDrawable(getResources().getDrawable(com.ronaker.app.R.drawable.navigate_dot_select))
        }
    }


    public fun showNavigator(visiable: Boolean, position: Int) {

        if (visiable) {

            for (i in 0 until dotCount) {
                if (i == position) {


                    dots[position]!!.animate().scaleX(0f).scaleY(0f).setDuration(100)
                        .setListener(object : Animator.AnimatorListener {
                            override fun onAnimationStart(animation: Animator) {

                            }

                            override fun onAnimationEnd(animation: Animator) {

                                dots[position]!!.setImageDrawable(resources.getDrawable(com.ronaker.app.R.drawable.navigate_dot_select))
                                dots[position]!!.setPadding(0, 0, 0, 0)
                                dots[position]!!.animate().scaleX(1f).scaleY(1f).setDuration(200).setListener(null)
                                    .start()
                            }

                            override fun onAnimationCancel(animation: Animator) {

                            }

                            override fun onAnimationRepeat(animation: Animator) {

                            }
                        }).start()
                } else {
                    dots[i]!!.setImageDrawable(resources.getDrawable(com.ronaker.app.R.drawable.navigate_dot_normal))
                    dots[i]!!.setPadding(3, 3, 3, 3)
                    dots[i]!!.animate().scaleX(1f).scaleY(1f).setDuration(200).setListener(null).start()

                }


            }


        } else {

            for (i in 0 until dotCount) {
                dots[i]!!.setImageDrawable(resources.getDrawable(com.ronaker.app.R.drawable.navigate_dot_normal))
                dots[i]!!.setPadding(3, 3, 3, 3)
                dots[i]!!.animate().scaleX(0f).scaleY(0f).setDuration(100).setListener(null).start()

            }


        }


    }


}