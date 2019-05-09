package com.ronaker.app.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ronaker.app.utils.extension.getParentActivity

@BindingAdapter("mutableVisibility")
fun setMutableVisibility(view: View, visibility: MutableLiveData<Int>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if(parentActivity != null && visibility != null) {
        visibility.observe(parentActivity, Observer { value -> view.visibility = value?:View.VISIBLE})
    }
}

@BindingAdapter("mutableText")
fun setMutableText(view: TextView, text: MutableLiveData<String>?) {

    val parentActivity:AppCompatActivity? = view.getParentActivity()
    if(parentActivity != null && text != null) {
        text.observe(parentActivity, Observer { value -> view.text = value?:""})
    }
}



@BindingAdapter("mutableImage")
fun setMutableImage(view: ImageView, url: MutableLiveData<String>?) {

    val parentActivity:AppCompatActivity? = view.getParentActivity()
    if(parentActivity != null && url != null) {
        url.observe(parentActivity, Observer {value ->  Glide.with(parentActivity).load(value).into(view)})
    }
}

//@BindingAdapter("mutableImage")
//fun setMutableRoundedImage(view: ImageView, url: MutableLiveData<String>?) {
//
//    val parentActivity:AppCompatActivity? = view.getParentActivity()
//    if(parentActivity != null && url != null) {
//        url.observe(parentActivity, Observer {value ->  Glide.with(parentActivity).load(value).into(view)})
//    }
//}

@BindingAdapter("adapter")
fun setAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    view.adapter = adapter
}