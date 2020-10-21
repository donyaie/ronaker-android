package com.ronaker.app.ui.search

import androidx.lifecycle.MutableLiveData
import com.ronaker.app.model.Category


class CategorySearchViewModel {
    private val title = MutableLiveData<String>()
    lateinit var data: Category


    fun bind(
        post: Category
    ) {
        data = post


    }



    fun getTitle(): MutableLiveData<String> {
        return title
    }

}