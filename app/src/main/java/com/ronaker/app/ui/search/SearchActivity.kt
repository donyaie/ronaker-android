package com.ronaker.app.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.utils.AnimationHelper
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : BaseActivity() {

    private val TAG = SearchActivity::class.java.simpleName

    private lateinit var binding: com.ronaker.app.databinding.ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel



    companion object {
        var SUID_KEY = "suid"
        val ResultCode=333
        var Search_KEY = "searchValue"

        fun newInstance(context: Context): Intent {
            var intent = Intent(context, SearchActivity::class.java)
            var boundle = Bundle()
//            boundle.putString(SUID_KEY, suid)
            intent.putExtras(boundle)

            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AnimationHelper.animateActivityFade(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)

        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)

        binding.viewModel = viewModel






        viewModel.errorMessage.observe(this, Observer { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        })



        viewModel.loading.observe(this, Observer { value ->
            if (value == true) {
                binding.loading.showLoading()
            } else
                binding.loading.hideLoading()
        })


        binding.cancelSearch.setOnClickListener{
            finish()
        }

        binding.searchEdit.setOnEditorActionListener{v, actionId, event->

            if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH){

                var intent=Intent()
                intent.putExtra(Search_KEY,binding.searchEdit.text.toString())
                setResult(0,intent)
                finish()
                true
            } else {
                false
            }

        }


        binding.clearText.setOnClickListener{
            binding.searchEdit.text.clear()
        }



    }




    fun getSearchValue():String?
    {
        if ( intent.hasExtra(SUID_KEY)) {
            var value = intent.getStringExtra(SUID_KEY)

            return value



        }
        return null
    }





    override fun onBackPressed() {
        finish()
    }





}