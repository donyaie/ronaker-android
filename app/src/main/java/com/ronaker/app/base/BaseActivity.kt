package com.ronaker.app.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.crashlytics.android.Crashlytics
import com.google.firebase.analytics.FirebaseAnalytics
import com.ronaker.app.General
import com.ronaker.app.R
import com.ronaker.app.utils.AnimationHelper
import com.ronaker.app.utils.LocaleHelper

import com.ronaker.app.utils.kayboardAnimator.BaseKeyboardAnimator
import com.ronaker.app.utils.kayboardAnimator.SimpleKeyboardAnimator
import io.fabric.sdk.android.Fabric
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import me.imid.swipebacklayout.lib.app.SwipeBackActivity


abstract class BaseActivity : AppCompatActivity()/*SwipeBackActivity() */{


    fun getAnalytics(): FirebaseAnalytics? {



        return if (applicationContext is General)
            (applicationContext as General).analytics
        else
            null
    }





    private val animator: BaseKeyboardAnimator by lazy(LazyThreadSafetyMode.NONE) {
        SimpleKeyboardAnimator(
            window
        )
    }


    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
        AnimationHelper.setStartSlideTransition(this)
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
        AnimationHelper.setStartSlideTransition(this)
    }


    fun enableKeyboardAnimator() {
            animator.start()

    }

//    var activityTag: String? = null

    fun setSwipeCloseDisable() {
//       swipeBackLayout.setEnableGesture(false)


    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
//        addActivityStack(this)
//        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT)
        Fabric.with(this, Crashlytics())

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP)
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black))


    }

    private var startCount = 0


    companion object {
//
//        private fun addActivityStack(activity: Activity) {
//            if (!activityList.contains(activity))
//                activityList.add(activity)
//            refreshActivityStack()
//        }
//
//        private fun removeActivityStack(activity: Activity) {
//            if (activityList.contains(activity))
//                activityList.remove(activity)
//
//            refreshActivityStack()
//        }

//
//        fun isTAGInStack(tag: String): Boolean {
//            var find = false
//            activityList.forEach {
//
//                if (tag.compareTo((it as BaseActivity).activityTag ?: "") == 0)
//                    find = true
//
//            }
//            return find
//        }

//        private fun refreshActivityStack() {
//            val temp = ArrayList<Activity>()
//
//            activityList.forEach {
//
//                if (it.isDestroyed || it.isFinishing)
//                    temp.add(it)
//            }
//
//            temp.forEach {
//                if (activityList.contains(it))
//                    activityList.remove(it)
//
//
//            }
//
//        }
//
//        private var activityList: ArrayList<Activity> = ArrayList()


    }

    override fun onDestroy() {
        super.onDestroy()
//        removeActivityStack(this)

    }

    override fun onStart() {
        super.onStart()
        startCount += 1


    }

    override fun finish() {

        super.finish()
        AnimationHelper.setEndSlideTransition(this)


    }




    fun isFistStart(): Boolean {
        return startCount <= 1
    }


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(LocaleHelper.onAttach(newBase)))
    }


}



