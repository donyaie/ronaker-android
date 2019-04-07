package com.ronaker.app.ui.post

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity

class PostListActivity: BaseActivity() {
    private lateinit var binding: com.ronaker.app.databinding.ActivityPostListBinding
    private lateinit var viewModel: PostListViewModel
    private var errorSnackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_list)
        binding.postList.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        viewModel = ViewModelProviders.of(this).get(PostListViewModel::class.java)

        viewModel.errorMessage.observe(this, Observer {
            errorMessage-> if (errorMessage!=null)showError(errorMessage)else hideError()
        })
        binding.viewModel = viewModel


    }

    private fun showError( errorMessage:String){

        errorSnackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        errorSnackbar?.setAction(R.string.retry, viewModel.errorClickListener)
        errorSnackbar?.show()
    }

    private fun hideError(){
        errorSnackbar?.dismiss()
    }

    companion object {
        fun newInstance(context: Context): Intent {
            return  Intent(context, PostListActivity::class.java)
        }
    }
}