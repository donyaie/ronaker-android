package com.ronaker.app.ui.manageProduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.model.Product
import com.ronaker.app.ui.addProduct.AddProductActivity
import com.ronaker.app.ui.addProduct.AddProductViewModel
import com.ronaker.app.ui.dashboard.DashboardActivity
import com.ronaker.app.utils.extension.startActivityMakeScene


class ManageProductFragment : BaseFragment(), ViewTreeObserver.OnScrollChangedListener {

    private lateinit var binding: com.ronaker.app.databinding.FragmentManageProductBinding
    private lateinit var productViewModel: ManageProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_manage_product, container, false)
        productViewModel = ViewModelProviders.of(this).get(ManageProductViewModel::class.java)

        binding.viewModel = productViewModel
        productViewModel.activeState.observe(this, Observer { active ->

            unregisterActiveListener()
            binding.activeSwitch.isChecked = active
            registerActiveListener()

        })

        productViewModel.loading.observe(this, Observer { loading ->
            if (loading) binding.loading.showLoading() else binding.loading.hideLoading()
        })
        productViewModel.retry.observe(this, Observer { loading ->

            loading?.let { binding.loading.showRetry(it) } ?: run { binding.loading.hideRetry() }
        })

        binding.loading.oClickRetryListener = View.OnClickListener {

            fill()
        }





        binding.toolbar.cancelClickListener = View.OnClickListener {

            if (activity is DashboardActivity) (activity as DashboardActivity).backFragment()
        }

        productViewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null)
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()

        })


        binding.imageLayout.setOnClickListener {
            activity?.startActivityMakeScene(context?.let { it1 ->
                productViewModel.mProduct.suid?.let { it2 ->
                    AddProductActivity.newInstance(
                        it1,
                        it2, AddProductViewModel.StateEnum.image
                    )
                }
            })
        }

        binding.locationLayout.setOnClickListener {
            activity?.startActivityMakeScene(context?.let { it1 ->
                productViewModel.mProduct.suid?.let { it2 ->
                    AddProductActivity.newInstance(
                        it1,
                        it2, AddProductViewModel.StateEnum.location
                    )
                }
            })
        }

        binding.nameLayout.setOnClickListener {
            activity?.startActivityMakeScene(context?.let { it1 ->
                productViewModel.mProduct.suid?.let { it2 ->
                    AddProductActivity.newInstance(
                        it1,
                        it2, AddProductViewModel.StateEnum.info
                    )
                }
            })
        }

        binding.priceLayout.setOnClickListener {
            activity?.startActivityMakeScene(context?.let { it1 ->
                productViewModel.mProduct.suid?.let { it2 ->
                    AddProductActivity.newInstance(
                        it1,
                        it2, AddProductViewModel.StateEnum.price
                    )
                }
            })
        }


        binding.categoryLayout.setOnClickListener {
            activity?.startActivityMakeScene(context?.let { it1 ->
                productViewModel.mProduct.suid?.let { it2 ->
                    AddProductActivity.newInstance(
                        it1,
                        it2, AddProductViewModel.StateEnum.category
                    )
                }
            })
        }


        binding.scrollView.viewTreeObserver.addOnScrollChangedListener(this)

        registerActiveListener()

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        binding.scrollView.viewTreeObserver.removeOnScrollChangedListener(this)
    }


    private fun registerActiveListener() {
        binding.activeSwitch.setOnCheckedChangeListener { _, active ->

            productViewModel.updateActiveState(active)

        }

    }

    private fun unregisterActiveListener() {
        binding.activeSwitch.setOnCheckedChangeListener(null)
    }

    override fun onStart() {
        super.onStart()

        fill()
    }


    private fun fill() {

        getSuid()?.let { productViewModel.loadProduct(it) }

        getProduct()?.let { productViewModel.loadProduct(it) }

    }

    private fun getSuid(): String? {
        return this.arguments?.getString(SUID_KEY)
    }

    fun getProduct(): Product? {
        return this.arguments?.getParcelable(PRODUCT_KEY)
    }


    companion object {

        private var SUID_KEY = "suid"
        private var PRODUCT_KEY = "product"

        fun newInstance(suid: String?): ManageProductFragment {
            val bundle = Bundle()
            bundle.putString(SUID_KEY, suid)
            val fragment = ManageProductFragment()
            fragment.arguments = bundle
            return fragment
        }


        fun newInstance(product: Product?): ManageProductFragment {
            val bundle = Bundle()
            bundle.putParcelable(PRODUCT_KEY, product)
            val fragment = ManageProductFragment()
            fragment.arguments = bundle
            return fragment
        }

    }

    override fun onScrollChanged() {
        try {
            val scrollY = binding.scrollView.scrollY

            if (scrollY <= binding.avatarImage.height / 1.2 - binding.toolbar.bottom) {

                binding.toolbar.isTransparent = true
                binding.toolbar.isBottomLine = false


            } else {

                binding.toolbar.isTransparent = false
                binding.toolbar.isBottomLine = true

            }

        } catch (ex: Exception) {

        }

    }


}
