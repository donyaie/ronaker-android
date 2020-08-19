package com.ronaker.app.ui.login

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
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.ui.intro.Intro1Fragment
import com.ronaker.app.ui.intro.Intro2Fragment
import com.ronaker.app.ui.intro.Intro3Fragment
import com.ronaker.app.ui.language.LanguageDialog
import com.ronaker.app.ui.orders.OrdersFragment
import com.ronaker.app.utils.AppDebug
import com.ronaker.app.utils.KeyboardManager
import com.ronaker.app.utils.view.IPagerFragment

class LoginHomeFragment : BaseFragment(), IPagerFragment {


    private lateinit var binding: com.ronaker.app.databinding.FragmentLoginHomeBinding
    private lateinit var viewModel: LoginViewModel

    private lateinit var adapter: ViewPagerAdapter

//  private val screenLibrary=ScreenCalculator(requireContext())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_home, container, false)


        activity?.let {
            viewModel = ViewModelProvider(it).get(LoginViewModel::class.java)
            binding.viewModel = viewModel
        }

        binding.languageChange.setOnClickListener {
            activity?.let { it1 -> LanguageDialog.showDialog(it1) }
        }

        initViewPager()
        AppDebug.log("capture", "LoginEmailFragment : CreateView")

        return binding.root
    }


    companion object {

        fun newInstance(): LoginHomeFragment {
            return LoginHomeFragment()
        }
    }

    override fun onSelect() {

        activity?.let { KeyboardManager.hideSoftKeyboard(it) }
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


            dataList.add(Intro1Fragment.newInstance())
            dataList.add(Intro2Fragment.newInstance())
            dataList.add(Intro3Fragment.newInstance())


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


    private lateinit var dots: Array<ImageView?>


    private val dotCount: Int = 3


    private val viewPager2PageChangeCallback =
        OrdersFragment.ViewPager2PageChangeCallback { position ->


            showNavigator(position)
//            selectTabUpdateView(position)

        }

    class ViewPager2PageChangeCallback(private val listener: (Int) -> Unit) :
        ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            listener.invoke(position)

        }
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