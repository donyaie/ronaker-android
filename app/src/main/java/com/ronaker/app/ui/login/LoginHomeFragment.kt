package com.ronaker.app.ui.login

import android.animation.Animator
import android.content.Intent
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.ronaker.app.R
import com.ronaker.app.base.BaseFragment
import com.ronaker.app.ui.intro.Intro1Fragment
import com.ronaker.app.ui.intro.Intro2Fragment
import com.ronaker.app.ui.intro.Intro3Fragment
import com.ronaker.app.ui.language.LanguageDialog
import com.ronaker.app.ui.orders.OrdersFragment
import com.ronaker.app.utils.AppDebug
import com.ronaker.app.utils.GoogleSignManger
import com.ronaker.app.utils.KeyboardManager
import com.ronaker.app.utils.view.IPagerFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginHomeFragment : BaseFragment(), IPagerFragment {

   private val TAG=LoginHomeFragment::class.java.simpleName

    private lateinit var binding: com.ronaker.app.databinding.FragmentLoginHomeBinding
    private val viewModel: LoginViewModel by activityViewModels()

    private lateinit var adapter: ViewPagerAdapter

    private val RC_SIGN_IN = 243



    private lateinit var dots: Array<ImageView?>


    private val dotCount: Int = 3

    private lateinit var mGoogleSignInClient: GoogleSignInClient

//  private val screenLibrary=ScreenCalculator(requireContext())


    companion object {

        fun newInstance(): LoginHomeFragment {
            return LoginHomeFragment()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_home, container, false)

            binding.viewModel = viewModel


        binding.languageChange.setOnClickListener {
            activity?.let { it1 -> LanguageDialog.showDialog(it1) }
        }




        initGoogle()

        initViewPager()
        AppDebug.log("capture", "LoginEmailFragment : CreateView")

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        mGoogleSignInClient.silentSignIn()
            .addOnCompleteListener(
                requireActivity()
            ) { task -> handleSignInResult(task) }
    }


    private fun initGoogle() {


        binding.signInButton.setStyle(SignInButton.SIZE_WIDE, SignInButton.COLOR_DARK)

        binding.signInButton.setOnClickListener {
            signIn()

        }

        mGoogleSignInClient = GoogleSignManger.getClient(requireContext())


        GoogleSignManger.setGooglePlusButtonText(binding.signInButton,requireContext().getString(R.string.text_signin_with_google))


    }


    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)


    }


    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)


            val idToken = account?.idToken


            AppDebug.log(TAG, "idToken : $idToken")


            idToken?.let { viewModel.loginGoogle(it, account.photoUrl?.toString()) }

//            account?.let {firebaseAuthWithGoogle(it)  }
            // Signed in successfully, show authenticated UI.
            // viewModel.updateUserInfo(account)

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            AppDebug.log(TAG, "signInResult:failed code=" + e.statusCode, e)

//            getAnalytics()?.actionLoginFail(AnalyticsManager.Param.LOGIN_METHOD_GOOGLE)

//                Alert.makeTextError(this, e.message)


        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//             Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
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