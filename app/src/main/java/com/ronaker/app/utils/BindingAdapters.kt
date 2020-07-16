package com.ronaker.app.utils

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.makeramen.roundedimageview.RoundedImageView
import com.ronaker.app.injection.module.GlideApp
import com.ronaker.app.utils.extension.getParentActivity


@BindingAdapter("mutableVisibility")
fun setMutableVisibility(view: View, visibility: MutableLiveData<Int>?) {
    val duration = 200L
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && visibility != null) {
        visibility.observe(parentActivity, Observer { value ->


            when (value) {

                View.GONE -> {
                    view.animate().alpha(0f).setDuration(duration).setListener(object :
                        AnimatorListener {
                        override fun onAnimationRepeat(animation: Animator?) {
                        }

                        override fun onAnimationEnd(animation: Animator?) {
                            view.visibility = View.GONE
                        }

                        override fun onAnimationCancel(animation: Animator?) {
                        }

                        override fun onAnimationStart(animation: Animator?) {
                        }

                    })
                        .start()
                }
                View.VISIBLE -> {

                    view.animate().alpha(1f).setDuration(duration).setListener(object :
                        AnimatorListener {
                        override fun onAnimationRepeat(animation: Animator?) {
                        }

                        override fun onAnimationEnd(animation: Animator?) {

                        }

                        override fun onAnimationCancel(animation: Animator?) {
                        }

                        override fun onAnimationStart(animation: Animator?) {
                            view.visibility = View.VISIBLE
                        }

                    })
                        .start()

                }
                View.INVISIBLE -> {
                    view.animate().alpha(0f).setDuration(duration).setListener(object :
                        AnimatorListener {
                        override fun onAnimationRepeat(animation: Animator?) {
                        }

                        override fun onAnimationEnd(animation: Animator?) {
                            try {
                                view.visibility = View.INVISIBLE
                            } catch (ex: Exception) {
                                AppDebug.log("setMutableVisibility", ex)
                            }

                        }

                        override fun onAnimationCancel(animation: Animator?) {
                        }

                        override fun onAnimationStart(animation: Animator?) {
                        }

                    })
                        .start()
                }
                else -> {
                    view.visibility = value ?: View.VISIBLE
                }

            }


        })
    }
}

@BindingAdapter("mutableText")
fun setMutableText(view: TextView, text: MutableLiveData<String>?) {

    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && text != null) {
        text.observe(parentActivity, Observer { value -> view.setText(value ?: "") })
    }
}


@BindingAdapter("mutableTextRes")
fun setMutableTextRes(view: TextView, text: MutableLiveData<Int>?) {

    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && text != null) {
        text.observe(
            parentActivity,
            Observer { value -> view.text = parentActivity.getString(value) })
    }
}

@BindingAdapter("mutableTextColorRes")
fun setMutableTextColorRes(view: TextView, text: MutableLiveData<Int>?) {

    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && text != null) {
        text.observe(
            parentActivity,
            Observer { value -> view.setTextColor(ContextCompat.getColor(parentActivity, value)) })
    }
}


@BindingAdapter("mutableBackgroundRes")
fun setMutableBackgroundRes(view: View, text: MutableLiveData<Int>?) {

    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && text != null) {
        text.observe(parentActivity, Observer { value -> view.setBackgroundResource(value) })
    }
}


@BindingAdapter("mutableProgress")
fun setMutableText(view: ProgressBar, text: MutableLiveData<Int>?) {

    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && text != null) {
        text.observe(parentActivity, Observer { value -> view.progress = value ?: 0 })
    }
}


@BindingAdapter("mutableRate")
fun setMutableRate(view: RatingBar, text: MutableLiveData<Float>?) {

    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && text != null) {
        text.observe(parentActivity, Observer { value -> view.rating = value ?: 0f })
    }
}


@BindingAdapter("mutableImage")
fun setMutableImage(view: ImageView, url: MutableLiveData<String>?) {

    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && url != null) {
        url.observe(parentActivity, Observer { value ->
            GlideApp.with(parentActivity)
                .load(value)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view)
        })

    }
}


@BindingAdapter("mutableRadioChecked")
fun setMutableRadioChecked(view: RadioButton, url: MutableLiveData<Boolean>?) {

    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && url != null) {
        url.observe(parentActivity, Observer { value -> view.isChecked = value })

    }
}


@BindingAdapter("mutableImageSrc")
fun setMutableImageSrc(view: ImageView, src: MutableLiveData<Int>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && src != null) {
        src.observe(parentActivity, Observer { value -> view.setImageResource(value) })
    }
}

@BindingAdapter("adapter")
fun setAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    view.adapter = adapter
}