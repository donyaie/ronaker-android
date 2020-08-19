package com.ronaker.app.ui.orderCreate

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.model.Product
import com.ronaker.app.ui.profileCompleteEdit.ProfileCompleteActivity
import com.ronaker.app.utils.Alert
import com.ronaker.app.utils.AppDebug
import com.ronaker.app.utils.KeyboardManager
import com.ronaker.app.utils.view.IPagerFragment
import com.ronaker.app.utils.view.ToolbarComponent
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrderCreateActivity : BaseActivity() {

    private val TAG = OrderCreateActivity::class.java.simpleName

    private lateinit var binding: com.ronaker.app.databinding.ActivityOrderCreateBinding
    private val viewModel: OrderCreateViewModel by viewModels()

    private lateinit var checkoutFragment: OrderCheckoutFragment
    private lateinit var messageFragment: OrderMessageFragment

    private lateinit var adapter: ViewPagerAdapter


    internal var fragmentState = OrderCreateViewModel.StateEnum.Checkout
        set(value) {
            field = value


            when (fragmentState) {

                OrderCreateViewModel.StateEnum.Checkout -> {
                    binding.viewpager.currentItem = OrderCreateViewModel.StateEnum.Checkout.position
                }

                OrderCreateViewModel.StateEnum.Message -> {
                    binding.viewpager.currentItem = OrderCreateViewModel.StateEnum.Message.position
                }
            }

        }

    companion object {
        const val REQUEST_CODE = 360


        const val PRODUCT_KEY = "mProduct"
        fun newInstance(
            context: Context,
            product: Product
        ): Intent {
            val intent = Intent(context, OrderCreateActivity::class.java)
            val bundle = Bundle()

            bundle.putParcelable(PRODUCT_KEY, product)
            intent.putExtras(bundle)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableKeyboardAnimator()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_create)

        binding.viewModel = viewModel

        init()

        viewModel.viewState.observe(this, {state ->
            fragmentState = state

        })

        getProduct()?.let { viewModel.init(it) } ?: run { finish() }


        fragmentState = OrderCreateViewModel.StateEnum.Checkout


        binding.toolbar.cancelClickListener = View.OnClickListener {

            finish()
        }


        binding.toolbar.actionTextClickListener = View.OnClickListener {


            viewModel.clearDates()
        }


    }


    private fun init() {


        initViewPager()


        viewModel.errorMessage.observe(this, {errorMessage ->
            Alert.makeTextError(this, errorMessage)
        })



        viewModel.successMessage.observe(this, {
            succeccSend()
        })



        viewModel.goNext.observe(this, {
            finish()
        })


        viewModel.goValidate.observe(this, {
            startActivity(
                ProfileCompleteActivity.newInstance(
                    this
                )
            )
        })

        initViewPagerRegister()


    }


    private fun prePage() {


        if (binding.viewpager.currentItem == 0)
            finish()


        if (binding.viewpager.currentItem > 0) {
            binding.viewpager.setCurrentItem(binding.viewpager.currentItem - 1, true)
        }

    }


    private fun initViewPagerRegister() {
        adapter.clear()
        adapter.addFragment(checkoutFragment)
        adapter.addFragment(messageFragment)
        binding.viewpager.adapter?.notifyDataSetChanged()

    }


    fun getProduct(): Product? {
        return intent?.getParcelableExtra(PRODUCT_KEY)
    }


    private fun initViewPager() {

        binding.viewpager.setScrollDurationFactor(2.0)
        adapter = ViewPagerAdapter(supportFragmentManager)

        checkoutFragment = OrderCheckoutFragment()
        messageFragment = OrderMessageFragment()



        binding.viewpager.adapter = adapter

        binding.viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {


                fragmentState = OrderCreateViewModel.StateEnum[position]


                AppDebug.log(TAG, String.format("onSelect:%s", fragmentState.name))
                (adapter.getItem(position) as IPagerFragment).onSelect()

                when (fragmentState) {
                    OrderCreateViewModel.StateEnum.Checkout -> {
                        binding.toolbar.actionContainer = ToolbarComponent.ActionContainer.TEXT
                    }
                    else -> {
                        binding.toolbar.actionContainer = ToolbarComponent.ActionContainer.NONE
                    }
                }


            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })

        KeyboardManager.hideSoftKeyboard(this)
    }

    override fun onBackPressed() {
        prePage()
    }


    internal class ViewPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(
        manager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
        private val mFragmentList = ArrayList<Fragment>()

        override fun getCount(): Int {
            return mFragmentList.size
        }

        override fun getItemPosition(`object`: Any): Int {

            return POSITION_NONE
        }


        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        fun addFragment(fragment: Fragment) {
            mFragmentList.add(fragment)
        }


        fun clear() {
            mFragmentList.clear()
        }

        override fun getPageTitle(position: Int): CharSequence {
            return ""
        }
    }


    private fun succeccSend() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        builder.setMessage(getString(R.string.text_your_request_sent))
        builder.setPositiveButton(
            getString(android.R.string.ok)

        ) { dialog, _ ->
            dialog?.cancel()
            this.setResult(Activity.RESULT_OK)
            this.finish()


        }
        builder.setCancelable(false)

        builder.show()
    }


}