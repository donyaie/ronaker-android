package com.ronaker.app.ui.search

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.transition.Fade
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.utils.Alert
import com.ronaker.app.utils.AnimationHelper


class SearchActivity : BaseActivity() {

    private val TAG = SearchActivity::class.java.simpleName

    private lateinit var binding: com.ronaker.app.databinding.ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel


    companion object {
        const val SUID_KEY = "suid"
        const val ResultCode = 333
        const val Search_KEY = "searchValue"

        fun newInstance(context: Context): Intent {
            val intent = Intent(context, SearchActivity::class.java)
            val boundle = Bundle()
//            boundle.putString(SUID_KEY, suid)
            intent.putExtras(boundle)

            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableKeyboardAnimator()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)

        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        binding.viewModel = viewModel


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val fade = Fade()
            fade.excludeTarget(android.R.id.statusBarBackground, true)
            fade.excludeTarget(android.R.id.navigationBarBackground, true)
            window.enterTransition = fade
            window.exitTransition = fade
        }





        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            Alert.makeTextError(this, errorMessage)
        })



        viewModel.loading.observe(this, Observer { value ->
            if (value == true) {
                binding.loading.showLoading()
            } else
                binding.loading.hideLoading()
        })


        binding.cancelSearch.setOnClickListener {
            finishAfterTransition()
        }

        binding.searchEdit.setOnEditorActionListener { _, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH) {

                val intent = Intent()
                val value = binding.searchEdit.text.toString()
                intent.putExtra(Search_KEY, value)
                setResult(0, intent)
                finishAfterTransition()
                true
            } else {
                false
            }

        }


        binding.clearText.setOnClickListener {
            binding.searchEdit.text.clear()
        }


    }

    override fun onBackPressed() {
        finishAfterTransition()
    }


    override fun finish() {
        super.finish()
        AnimationHelper.clearTransition(this)
    }


    private fun getSearchValue(): String? {
        if (intent.hasExtra(Search_KEY)) {
            return intent.getStringExtra(Search_KEY)
        }
        return null
    }


}