package com.ronaker.app.ui.dashboard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.ncapdevi.fragnav.FragNavController
import com.ncapdevi.fragnav.FragNavLogger
import com.ncapdevi.fragnav.FragNavSwitchController
import com.ncapdevi.fragnav.FragNavTransactionOptions
import com.ncapdevi.fragnav.tabhistory.UniqueTabHistoryStrategy
import com.ronaker.app.R
import com.ronaker.app.base.BaseActivity
import com.ronaker.app.ui.explore.ExploreFragment
import com.ronaker.app.ui.exploreProduct.ExploreProductActivity
import com.ronaker.app.ui.inbox.InboxFragment
import com.ronaker.app.ui.login.LoginActivity
import com.ronaker.app.ui.manageProduct.ManageProductListFragment
import com.ronaker.app.ui.orders.OrdersFragment
import com.ronaker.app.ui.profile.ProfileFragment
import com.ronaker.app.ui.profileEmailVerify.EmailVerifyDialog
import com.ronaker.app.utils.AnimationHelper
import com.ronaker.app.utils.AppDebug
import com.ronaker.app.utils.view.TabNavigationComponent
import dagger.hilt.android.AndroidEntryPoint
import io.branch.referral.Branch
import io.branch.referral.Branch.BranchReferralInitListener


@AndroidEntryPoint
class DashboardActivity : BaseActivity(), FragNavController.TransactionListener,
    FragNavController.RootFragmentListener, EmailVerifyDialog.OnDialogResultListener,
    InstallStateUpdatedListener {


    var availableVersionCode = 0
    lateinit var appUpdateManager: AppUpdateManager


    private val viewModel: DashboardViewModel by viewModels()

    private val fragNavController: FragNavController =
        FragNavController(supportFragmentManager, R.id.container)
    override val numberOfRootFragments: Int = 4

    private val UPDATE_REQUEST_CODE = 645

    private val INDEX_EXPLORE = FragNavController.TAB1
    private val INDEX_ORDERS = FragNavController.TAB2
    private val INDEX_ITEMADD = FragNavController.TAB3
    private val INDEX_INBOX = FragNavController.TAB5
    private val INDEX_PROFILE = FragNavController.TAB4

    private lateinit var binding: com.ronaker.app.databinding.ActivityDashboardBinding


    var savedInstanceState: Bundle? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setSwipeCloseDisable()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        binding.viewModel = viewModel
        binding.root.setBackgroundResource(R.color.white)
//        ( window.decorView.findViewById<View>(Window.ID_ANDROID_CONTENT)?.parent as? ViewGroup)?.setBackgroundResource(R.color.white)
//        window.setBackgroundDrawableResource(R.color.white)
//


        viewModel.goLogin.observe(this, { value ->
            if (value == true) {

                startActivity(LoginActivity.newInstance(this@DashboardActivity))
                AnimationHelper.setFadeTransition(this)
                finish()

            }
        })


        viewModel.goEmail.observe(this, { value ->
            if (value == true) {

//                startActivity(ProfileEmailVerifyActivity.newInstance(this@DashboardActivity))


                EmailVerifyDialog.DialogBuilder(supportFragmentManager).setListener(this).show()

            }
        })


        window.setBackgroundDrawable(ColorDrawable(Color.WHITE))


        if (viewModel.checklogin()) {

            initNavigation(savedInstanceState)

            newIntentHandler(intent)
        }


        checkForUpdate()


    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        viewModel.checklogin()
        Branch.sessionBuilder(this).withCallback(branchReferralInitListener).reInit()


        newIntentHandler(intent)


    }


    fun newIntentHandler(intent: Intent?){

        val action: String? = intent?.action
        val data: Uri? = intent?.data


        AppDebug.log("DeepLink","action: $action , data : ${data.toString()}")

    }






    override fun onStart() {
        super.onStart()
        Branch.sessionBuilder(this).withCallback(branchReferralInitListener)
            .withData(if (intent != null) intent.data else null).init()


    }


    override fun onResume() {
        super.onResume()

        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                // If the update is downloaded but not installed,
                // notify the user to complete the update.
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate()
                }
            }
    }


    private val branchReferralInitListener =
        BranchReferralInitListener { linkProperties, error ->
            if (error == null) {
                AppDebug.log("BRANCH SDK", linkProperties.toString())

                if (linkProperties?.has("product") == true) {
                    val suid = linkProperties.getString("product")
                    if (suid.isNotBlank() && viewModel.islogin) {
                        startActivity(ExploreProductActivity.newInstance(this, suid.trim()))
                    }

                }

                if (linkProperties?.has("invite-code") == true) {
                    LoginActivity.inviteCode = linkProperties.getString("invite-code")


                }

            } else {
                AppDebug.log("BRANCH SDK", error.message)
            }
        }


    override fun getRootFragment(index: Int): Fragment {
        return when (index) {
            INDEX_EXPLORE -> ExploreFragment.newInstance()
            INDEX_ITEMADD -> ManageProductListFragment.newInstance()
            INDEX_ORDERS -> OrdersFragment.newInstance()
            INDEX_PROFILE -> ProfileFragment.newInstance()
            INDEX_INBOX -> InboxFragment.newInstance()
            else -> ExploreFragment.newInstance()
        }
    }

    override fun onFragmentTransaction(
        fragment: Fragment?,
        transactionType: FragNavController.TransactionType
    ) {

    }


    override fun onTabTransaction(fragment: Fragment?, index: Int) {
    }


    override fun recreate() {
        super.recreate()
        binding.navigation.select(INDEX_EXPLORE)
    }


    private fun initNavigation(savedInstanceState: Bundle?) {

        fragNavController.apply {
            transactionListener = this@DashboardActivity
            rootFragmentListener = this@DashboardActivity
            createEager = true
            fragNavLogger = object : FragNavLogger {
                override fun error(message: String, throwable: Throwable) {

                }
            }
            defaultTransactionOptions = FragNavTransactionOptions.newBuilder()
//                .customAnimations(
//                    android.R.anim.fade_in,
//                    android.R.anim.fade_out,
//                    android.R.anim.fade_in,
//                    android.R.anim.fade_out
//                )
                .build()
            fragmentHideStrategy = FragNavController.DETACH_ON_NAVIGATE_HIDE_ON_SWITCH

            navigationStrategy = UniqueTabHistoryStrategy(object : FragNavSwitchController {
                override fun switchTab(index: Int, transactionOptions: FragNavTransactionOptions?) {
                    binding.navigation.select(index)
                }
            })
        }

        fragNavController.initialize(INDEX_EXPLORE, savedInstanceState)


        val initial = savedInstanceState == null
        if (initial || isChangingConfigurations) {
            binding.navigation.select(INDEX_EXPLORE)
        }


        binding.navigation.selectListener = object : TabNavigationComponent.OnSelectItemListener {
            override fun onReSelected(index: Int) {
                fragNavController.clearStack()
            }

            override fun onSelect(index: Int) {

                when (index) {
                    0 -> fragNavController.switchTab(INDEX_EXPLORE)
                    1 -> fragNavController.switchTab(INDEX_ORDERS)
                    2 -> fragNavController.switchTab(INDEX_ITEMADD)
                    3 -> fragNavController.switchTab(INDEX_INBOX)
                    4 -> fragNavController.switchTab(INDEX_PROFILE)


                }
            }
        }

    }

    companion object {
        fun newInstance(context: Context): Intent {
            val intent = Intent(context, DashboardActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
            return intent
        }
    }

    fun pushFragment(fragment: Fragment) {

        fragNavController.pushFragment(fragment)

    }

    fun backFragment() {
        fragNavController.popFragment()

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        fragNavController.onSaveInstanceState(outState)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        when (requestCode) {
            ExploreProductActivity.REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    binding.navigation.postDelayed({ binding.navigation.select(1) }, 50)


                }
            }
            UPDATE_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {

                    appUpdateManager.registerListener(this)
//                    appUpdateManager.completeUpdate()
                } else if (resultCode == RESULT_CANCELED) {

                    viewModel.setSkipVersion(availableVersionCode)

                }

            }


        }

        super.onActivityResult(requestCode, resultCode, data)
    }


    override fun onBackPressed() {


        if (handleFragmentBakeListener(fragNavController.currentFrag) == true) {
            return
        }



        if (!fragNavController.popFragment()) {
            super.onBackPressed()
        }
    }


    override fun onDialogResult(result: EmailVerifyDialog.DialogResultEnum) {


    }

    private var mainListener: HashMap<Fragment, MainaAtivityListener> = HashMap()


    fun addFragmentListener(fragment: Fragment, listener: MainaAtivityListener) {

        mainListener[fragment] = listener

    }

    override fun finish() {
        super.finish()
        AnimationHelper.clearTransition(this)
    }


    fun removeFragmentListener(fragment: Fragment) {

        mainListener.remove(fragment)

    }

    private fun handleFragmentBakeListener(fragment: Fragment?): Boolean? {

        if (mainListener.containsKey(fragment))
            return mainListener[fragment]?.onBackPressed()

        return false
    }


    private fun checkForUpdate() {

        // Creates instance of the manager.
        appUpdateManager = AppUpdateManagerFactory.create(this)

        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                // For a flexible update, use AppUpdateType.FLEXIBLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {


                availableVersionCode = appUpdateInfo.availableVersionCode()

                if (!viewModel.isSkipVersion(appUpdateInfo.availableVersionCode())) {


                    appUpdateManager.startUpdateFlowForResult(
                        // Pass the intent that is returned by 'getAppUpdateInfo()'.
                        appUpdateInfo,
                        // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                        AppUpdateType.FLEXIBLE,
                        // The current activity making the update request.
                        this,
                        // Include a request code to later monitor this update request.
                        UPDATE_REQUEST_CODE
                    )

                }
            }
        }

    }


    interface MainaAtivityListener {
        fun onBackPressed(): Boolean
    }

    override fun onStateUpdate(state: InstallState) {


        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            popupSnackbarForCompleteUpdate()

        }


    }


   private fun popupSnackbarForCompleteUpdate() {


        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage( "An update has just been downloaded.")
        builder.setPositiveButton(
            "RESTART"
        ) { dialog, _ ->
            appUpdateManager.completeUpdate()

            dialog?.cancel()
        }

        builder.show()


    }

}