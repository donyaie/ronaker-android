package com.ronaker.app.ui.profileAuthorization

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jakewharton.rxbinding2.widget.RxTextView
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.ui.orders.OrdersFragment
import com.ronaker.app.ui.smartIdIntro.SmartIDIntro1Fragment
import com.ronaker.app.ui.smartIdIntro.SmartIDIntro2Fragment
import com.ronaker.app.ui.smartIdIntro.SmartIDIntro3Fragment
import com.ronaker.app.utils.KeyboardManager
import com.ronaker.app.utils.view.IPagerFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable

@AndroidEntryPoint
class SmartIDPersonalCodeFragment : BaseFragment(), IPagerFragment {


    private lateinit var binding: com.ronaker.app.databinding.FragmentSmartidPersonalCodeBinding
    private val viewModel: ProfileAuthorizationViewModel by activityViewModels()

    var disposable: Disposable? = null



    private lateinit var adapter: ViewPagerAdapter



    private lateinit var dots: Array<ImageView?>


    private val dotCount: Int = 3


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_smartid_personal_code,
            container,
            false
        )

        binding.viewModel = viewModel



        binding.view = this




        initViewPager()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        binding.ccp.setOnCountryChangeListener {
            KeyboardManager.showSoftKeyboard(context, binding.codeEditText)
        }

        disposable = RxTextView.textChanges(binding.codeEditText).subscribe {

            if (it.isNotEmpty())
                showClear(true)
            else
                showClear(false)

            validateNumber(it.toString())


        }


        binding.codeValidate.setOnClickListener { binding.codeEditText.setText("") }


    }


    fun onPersonalCodeNext() {
        viewModel.onNextPersonalCode(
            binding.ccp.selectedCountryNameCode,
            binding.codeEditText.text.toString().trim()
        )

    }


    private fun validateNumber(phNumber: String?): Boolean {
        try {


            if (phNumber.isNullOrBlank()) {
                showValidate(false)
                return false
            }

            val isValid = phNumber.trim().length == 11
            return if (isValid) {
                showValidate(true)
                true
            } else {
                showValidate(false)
                false
            }

        } catch (ex: Exception) {


            showValidate(false)
            return false
        }

    }


    private fun showClear(show: Boolean) {
        if (show) {
            binding.codeValidate.animate().alpha(1f).setDuration(300).start()
            binding.codeValidate.isClickable = true

        } else {

            binding.codeValidate.animate().alpha(0f).setDuration(300).start()
            binding.codeValidate.isClickable = false
        }
    }

    private fun showValidate(valid: Boolean) {
        if (valid) {
            activeNext(true)

        } else {
            activeNext(false)
        }
    }


    private fun activeNext(active: Boolean) {
        if (active) {

            binding.nextButton.isEnabled = active
        } else {

            binding.nextButton.isEnabled = active
        }
    }


    companion object {

        fun newInstance(): SmartIDPersonalCodeFragment {
            return SmartIDPersonalCodeFragment()
        }
    }

    override fun onSelect() {

    }

    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }





    private fun initViewPager() {
        initDotCount()

        adapter = ViewPagerAdapter(this)
        binding.viewpager.adapter = adapter

        binding.viewpager.registerOnPageChangeCallback(viewPager2PageChangeCallback)
        binding.viewpager.currentItem = 0
//        showNavigator(0)


    }

    internal class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(
        fragment
    ) {
        private val mFragmentList = ArrayList<Fragment>()

        val dataList = ArrayList<Fragment>()


        init {
            dataList.clear()


            dataList.add(SmartIDIntro1Fragment.newInstance())
            dataList.add(SmartIDIntro2Fragment.newInstance())
            dataList.add(SmartIDIntro3Fragment.newInstance())


        }

        fun clear() {
            mFragmentList.clear()
        }


        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            return dataList[position]
        }
    }



    private val viewPager2PageChangeCallback =
        OrdersFragment.ViewPager2PageChangeCallback { position ->


            showNavigator(position)

        }




    private fun initDotCount() {

        dots = arrayOfNulls(dotCount)
        binding.countDots.removeAllViewsInLayout()
        for (i in 0 until dotCount) {
            dots[i] = ImageView(context)
            dots[i]?.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.navigate_dot_normal
                )
            )

            val params = LinearLayout.LayoutParams(
                requireContext().resources.getDimensionPixelSize(R.dimen.dot_size),
                requireContext().resources.getDimensionPixelSize(R.dimen.dot_size)
            )

            params.setMargins(21, 0, 21, 0)

            dots[i]?.scaleType = ImageView.ScaleType.FIT_CENTER

            binding.countDots.addView(dots[i], params)
        }

        dots[0]?.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.navigate_dot_select
            )
        )
    }





    fun showNavigator(position: Int) {


        for (i in 0 until dotCount) {
            if (i == position) {


                dots[position]?.animate()?.scaleX(0f)?.scaleY(0f)?.setDuration(100)
                    ?.setListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator) {

                        }

                        override fun onAnimationEnd(animation: Animator) {

                            dots[position]?.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.navigate_dot_select
                                )
                            )
                            dots[position]?.setPadding(0, 0, 0, 0)
                            dots[position]?.animate()?.scaleX(1f)?.scaleY(1f)?.setDuration(200)
                                ?.setListener(null)
                                ?.start()
                        }

                        override fun onAnimationCancel(animation: Animator) {

                        }

                        override fun onAnimationRepeat(animation: Animator) {

                        }
                    })?.start()
            } else {
                dots[i]?.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.navigate_dot_normal
                    )
                )
                dots[i]?.setPadding(3, 3, 3, 3)
                dots[i]?.animate()?.scaleX(1f)?.scaleY(1f)?.setDuration(200)?.setListener(null)
                    ?.start()

            }


        }


    }


}