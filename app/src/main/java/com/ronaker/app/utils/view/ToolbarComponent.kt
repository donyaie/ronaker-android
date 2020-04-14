package com.ronaker.app.utils.view

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.ronaker.app.R
import com.ronaker.app.utils.ScreenCalculator
import com.ronaker.app.utils.ShapeDrawableHelper


class ToolbarComponent constructor(context: Context, attrs: AttributeSet) :
    LinearLayout(context, attrs) {

    val screenCalculator= ScreenCalculator(context)

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

            when (value) {
                CancelContainer.NONE -> {
                    cancelButton.visibility = View.GONE
                }
                CancelContainer.BACK -> {

                    cancelButton.visibility = View.VISIBLE

//                    cancelButton.setImageResource(R.drawable.ic_back_white)

                    if (field != value) {
                        ShapeDrawableHelper.changeSvgDrawableColor(
                            context,
                            R.drawable.ic_back_white,
                            R.color.colorIconLight,
                            cancelLightImage
                        )
                        ShapeDrawableHelper.changeSvgDrawableColor(
                            context,
                            R.drawable.ic_back_white,
                            R.color.colorIconDark,
                            cancelDarkImage
                        )
                    }


                    if (isTransparent) {
                        cancelLightImage.visibility = View.VISIBLE
                        cancelDarkImage.visibility = View.GONE
                    } else {
                        cancelLightImage.visibility = View.GONE
                        cancelDarkImage.visibility = View.VISIBLE
                    }

                }
                CancelContainer.CLOSE -> {

                    cancelButton.visibility = View.VISIBLE
//                    cancelButton.setImageResource(R.drawable.ic_close)

                    if (field != value) {
                        ShapeDrawableHelper.changeSvgDrawableColor(
                            context,
                            R.drawable.ic_close,
                            R.color.colorIconLight,
                            cancelLightImage
                        )
                        ShapeDrawableHelper.changeSvgDrawableColor(
                            context,
                            R.drawable.ic_close,
                            R.color.colorIconDark,
                            cancelDarkImage
                        )
                    }




                    if (isTransparent) {
                        cancelLightImage.visibility = View.VISIBLE
                        cancelDarkImage.visibility = View.GONE
                    } else {
                        cancelLightImage.visibility = View.GONE
                        cancelDarkImage.visibility = View.VISIBLE
                    }
                }

            }



            field = value
        }

    var actionContainer: ActionContainer = ActionContainer.NONE
        set(value) {

            field = value

            when (value) {
                ActionContainer.NONE -> {
                    actionLayout.visibility = View.GONE
                }
                ActionContainer.TEXT -> {
                    actionLayout.visibility = View.VISIBLE
                    actionText.visibility = View.VISIBLE

                    action1Button.visibility = View.GONE
                    action2Button.visibility = View.GONE


                    if (isTransparent) {
                        actionText.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.colorTextLight
                            )
                        )

                    } else {
                        actionText.setTextColor(
                            ContextCompat.getColor(
                                context,
                                R.color.colorTextDark
                            )
                        )

                    }


                }
                ActionContainer.TWO_BUTTON -> {
                    actionLayout.visibility = View.VISIBLE
                    actionText.visibility = View.GONE
                    action1Button.visibility = View.VISIBLE
                    action2Button.visibility = View.VISIBLE


//                    action1Button.setImageResource(action1Src)
//                    action2Button.setImageResource(action2Src)


                    if (isTransparent) {


                        action1LightImage.visibility = View.VISIBLE
                        action1DarkImage.visibility = View.GONE


                        action2LightImage.visibility = View.VISIBLE
                        action2DarkImage.visibility = View.GONE


                    } else {


                        action1LightImage.visibility = View.GONE
                        action1DarkImage.visibility = View.VISIBLE


                        action2LightImage.visibility = View.GONE
                        action2DarkImage.visibility = View.VISIBLE


                    }
                }

                else -> {
                    actionLayout.visibility = View.GONE
                }
            }

        }

    private var screenLibrary: ScreenCalculator


    var isTransparent: Boolean = false
        set(value) {

            field = value


            if (value) {
                containerLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.transparent
                    )
                )
                titleText.setTextColor(ContextCompat.getColor(context, R.color.colorTextLight))

                statusBar.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent))

            } else {
                containerLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.white))

                titleText.setTextColor(ContextCompat.getColor(context, R.color.colorTextDark))

                statusBar.setBackgroundColor(ContextCompat.getColor(context, R.color.white))


            }


            cancelContainer = cancelContainer

            actionContainer = actionContainer

        }

    var isBottomLine: Boolean = false
        set(value) {
            field = value
            lineLayout.visibility = if (isBottomLine) View.VISIBLE else View.GONE

//            containerLayout.elevation=  if (value) screenCalculator.DP2Pixel(2).toFloat() else 0f
        }

    var title: String? = null
        set(value) {
            field = value
            titleText.text = value
        }


    var action1Src: Int = 0
        set(value) {

                ShapeDrawableHelper.changeSvgDrawableColor(
                    context,
                    value,
                    R.color.colorIconLight,
                    action1LightImage
                )
                ShapeDrawableHelper.changeSvgDrawableColor(
                    context,
                    value,
                    R.color.colorIconDark,
                    action1DarkImage
                )


            field = value
        }

    var action2Src: Int = 0
        set(value) {

                ShapeDrawableHelper.changeSvgDrawableColor(
                    context,
                    value,
                    R.color.colorIconLight,
                    action2LightImage
                )
                ShapeDrawableHelper.changeSvgDrawableColor(
                    context,
                    value,
                    R.color.colorIconDark,
                    action2DarkImage
                )


            field = value
        }


    var actionTitle: String? = null
        set(value) {
            field = value
            actionText.text = value
        }

    var dotCount: Int = 3
        set(value) {
            field = value
            initDotCount()
        }


    private lateinit var dots: Array<ImageView?>

    private var titleText: TextView

    private var lineLayout: RelativeLayout
    private var actionText: Button

    private var action1Button: RelativeLayout
    private var action1DarkImage: ImageView
    private var action1LightImage: ImageView


    private var action2Button: RelativeLayout
    private var action2DarkImage: ImageView
    private var action2LightImage: ImageView


    private var actionLayout: LinearLayout

    private var cancelButton: RelativeLayout
    private var cancelDarkImage: ImageView
    private var cancelLightImage: ImageView
    private var statusBar: StatusBarSizeView

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
            actionText.setOnClickListener(value)

        }

    var action1BouttonClickListener: OnClickListener? = null
        set(value) {

            field = value
            action1Button.setOnClickListener(value)

        }

    var action2BouttonClickListener: OnClickListener? = null
        set(value) {

            field = value
            action2Button.setOnClickListener(value)

        }


    init {

        LayoutInflater.from(context)
            .inflate(R.layout.component_toolbar, this, true)

        titleText = findViewById(R.id.title)
        lineLayout = findViewById(R.id.line)
        actionText = findViewById(R.id.action_text)
        actionLayout = findViewById(R.id.action_Layout)
        cancelButton = findViewById(R.id.cancel_button)
        cancelLightImage = findViewById(R.id.cancelLite_image)
        cancelDarkImage = findViewById(R.id.cancelDark_image)
        countDots = findViewById(R.id.countDots)
        containerLayout = findViewById(R.id.container_layout)
        statusBar = findViewById(R.id.statusBar)

        action1Button = findViewById(R.id.action1_button)
        action2Button = findViewById(R.id.action2_button)


        action1LightImage = findViewById(R.id.action1Light_image)
        action1DarkImage = findViewById(R.id.action1Dark_image)

        action2LightImage = findViewById(R.id.action2Light_image)
        action2DarkImage = findViewById(R.id.action2Dark_image)



        orientation = VERTICAL

        screenLibrary = ScreenCalculator(context)

        attrs.let {
            val typedArray = context.obtainStyledAttributes(
                it,
                R.styleable.ToolbarComponent, 0, 0
            )




            actionTitle = typedArray
                .getString(
                    R.styleable
                        .ToolbarComponent_toolbar_component_action_text
                )


            isBottomLine = typedArray
                .getBoolean(
                    R.styleable
                        .ToolbarComponent_toolbar_component_bottom_line,
                    false
                )



            isTransparent = typedArray
                .getBoolean(
                    R.styleable
                        .ToolbarComponent_toolbar_component_transparent,
                    false
                )


            centerContainer = CenterContainer.values()[typedArray
                .getInt(
                    R.styleable
                        .ToolbarComponent_toolbar_component_center_container,
                    0
                )]


            actionContainer = ActionContainer.values()[typedArray
                .getInt(
                    R.styleable
                        .ToolbarComponent_toolbar_component_action_container,
                    2
                )]





            cancelContainer = CancelContainer.values()[typedArray
                .getInt(
                    R.styleable
                        .ToolbarComponent_toolbar_component_cancel_container,
                    1
                )]



            title = typedArray
                .getString(
                    R.styleable
                        .ToolbarComponent_toolbar_component_title
                )


            action1Src = typedArray
                .getResourceId(
                    R.styleable
                        .ToolbarComponent_toolbar_component_action1src,
                    R.drawable.ic_share_white
                )

            action2Src = typedArray
                .getResourceId(
                    R.styleable
                        .ToolbarComponent_toolbar_component_action2src,
                    R.drawable.ic_fave_white
                )

            dotCount = typedArray
                .getInt(
                    R.styleable
                        .ToolbarComponent_toolbar_component_dot_count,
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
                dots[i]?.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.navigate_dot_normal
                    )
                )

                val params = LayoutParams(
                    screenLibrary.DP2Pixel(9),
                    screenLibrary.DP2Pixel(9)
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


    fun showNavigator(visiable: Boolean, position: Int) {

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


}