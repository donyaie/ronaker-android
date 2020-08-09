package com.ronaker.app.utils.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.ronaker.app.R
import com.ronaker.app.utils.extension.getParentActivity
import com.wang.avi.AVLoadingIndicatorView
import kotlinx.android.synthetic.main.component_button.view.*


@BindingAdapter("mutableButtonLoading")
fun setMutableRadioChecked(view: ButtonComponent, url: MutableLiveData<Boolean>?) {

    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && url != null) {
        url.observe(parentActivity, Observer { value -> view.loading = value })

    }
}


class ButtonComponent constructor(context: Context, attrs: AttributeSet) :
    RelativeLayout(context, attrs) {


    var imageView: ImageView
    var textView: TextView
    var loadinView: AVLoadingIndicatorView


    var text: String? = null
        set(value) {
            textView.text = value
            field = value
        }

    var textColor: Int = Color.WHITE
        set(value) {
            textView.setTextColor(value)
            field = value
        }
    var src: Drawable? = null
        set(value) {
            imageView.setImageDrawable(value)
            field = value
        }

    var loadingColor: Int = Color.WHITE
        set(value) {
            progress.setIndicatorColor(value)
            field = value
        }

    var loading: Boolean = false
        set(value) {

            if (value) {
                this.isEnabled = false
                this.isClickable = false
                progress.visibility = View.VISIBLE
                imageView.visibility = View.GONE
                textView.visibility = View.GONE
            } else {
                progress.visibility = View.GONE

                this.isEnabled = true
                this.isClickable = true

                when (buttonType) {
                    ButtonType.Text -> {

                        textView.visibility = View.VISIBLE
                    }
                    ButtonType.Image -> {

                        imageView.visibility = View.VISIBLE
                    }
                }
            }


            field = value
        }


    var buttonType: ButtonType = ButtonType.Text
        set(value) {

            when (value) {
                ButtonType.Text -> {
                    imageView.visibility = View.GONE
                    textView.visibility = View.VISIBLE
                }
                ButtonType.Image -> {

                    imageView.visibility = View.VISIBLE
                    textView.visibility = View.GONE
                }
            }

            field = value
        }


    enum class ButtonType {

        Text,
        Image
    }


    init {


        LayoutInflater.from(context)
            .inflate(R.layout.component_button, this, true)


        isClickable = true

        imageView = findViewById(R.id.image)
        textView = findViewById(R.id.text)
        loadinView = findViewById(R.id.progress)

        textView.text


        attrs.let {
            val typedArray = context.obtainStyledAttributes(
                it,
                R.styleable.ButtonComponent, 0, 0
            )



            text = typedArray.getString(
                R.styleable.ButtonComponent_android_text
            )


            textColor =
                typedArray.getColor(R.styleable.ButtonComponent_android_textColor, Color.WHITE)

            src = typedArray.getDrawable(R.styleable.ButtonComponent_buttonSrc)


            loadingColor =
                typedArray.getColor(R.styleable.ButtonComponent_loadingColor, Color.WHITE)


            buttonType = ButtonType.values()[typedArray
                .getInt(
                    R.styleable
                        .ButtonComponent_buttonType,
                    0
                )]



            loading = false



            typedArray.recycle()
        }
    }


}