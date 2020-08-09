package com.ronaker.app.ui.container

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity

class ContainerActivity : BaseActivity() {


    lateinit var fragment: Fragment


    private lateinit var binding: com.ronaker.app.databinding.ActivityContainerBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_container)

        getClassName()?.let {

            fragment = supportFragmentManager.fragmentFactory.instantiate(classLoader, it)

            fragment.arguments = intent.extras

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commitNow()
        }

    }


    private fun getClassName(): String? {
        if (intent.hasExtra(FragmentKey)) {
            return intent.getStringExtra(FragmentKey)
        }
        return null
    }


    companion object {
        val FragmentKey = "fragmentName"
        fun <T> newInstance(
            context: Context,
            className: Class<T>,
            inputBoundle: Bundle? = null
        ): Intent {
            val intent = Intent(context, ContainerActivity::class.java)
            val boundle = Bundle()
            inputBoundle?.let { boundle.putAll(it) }


            boundle.putString(FragmentKey, className.name)
            intent.putExtras(boundle)
            return intent
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        fragment.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }


}