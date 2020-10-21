package com.ronaker.app.ui.search

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.transition.Fade
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.widget.RxTextView
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.ui.explore.ItemExploreAdapter
import com.ronaker.app.utils.Alert
import com.ronaker.app.utils.AnimationHelper
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class SearchActivity : BaseActivity() {

    private val TAG = SearchActivity::class.java.simpleName

    private lateinit var binding: com.ronaker.app.databinding.ActivitySearchBinding
    private  val viewModel: SearchViewModel by viewModels()


    companion object {
        const val ResultCode = 333
        const val Search_KEY = "searchValue"

        fun newInstance(context: Context): Intent {
            val intent = Intent(context, SearchActivity::class.java)
            val boundle = Bundle()
            intent.putExtras(boundle)

            return intent
        }
    }


    var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableKeyboardAnimator()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)

        binding.viewModel = viewModel


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val fade = Fade()
            fade.excludeTarget(android.R.id.statusBarBackground, true)
            fade.excludeTarget(android.R.id.navigationBarBackground, true)
            window.enterTransition = fade
            window.exitTransition = fade
        }





        viewModel.errorMessage.observe(this, {errorMessage ->
            Alert.makeTextError(this, errorMessage)
        })



        viewModel.loading.observe(this, {value ->
            if (value == true) {
                binding.loading.showLoading()
            } else
                binding.loading.hideLoading()
        })


        binding.backImage.setOnClickListener {
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




        disposable = RxTextView.textChanges(binding.searchEdit)
            .debounce(1000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .subscribe {

                viewModel.search(it.toString())
            }



        binding.clearText.setOnClickListener {
            binding.searchEdit.text.clear()
        }


        val mnager = LinearLayoutManager(this)

        val productAdapter = ItemSearchAdapter()
        binding.itemRecycler.layoutManager = mnager

        binding.itemRecycler.adapter = productAdapter


        viewModel.listView.observe(this, {


            productAdapter.submitList(it.toList())
        })
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

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

}