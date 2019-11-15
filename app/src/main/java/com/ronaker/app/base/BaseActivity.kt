package com.ronaker.app.base

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.ronaker.app.utils.LocaleHelper
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import me.imid.swipebacklayout.lib.SwipeBackLayout
import me.imid.swipebacklayout.lib.app.SwipeBackActivity


abstract class BaseActivity: SwipeBackActivity() {


    var activityTag:String?=null

   fun setSwipeCloseDisable(){
       swipeBackLayout.setEnableGesture(false)
   }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addActivityStack(this)
        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT)

    }

    var startCount=0


    companion object {

        fun addActivityStack(activity:Activity){
            if(!activityList.contains(activity))
                activityList.add(activity)
            refreshActivityStack()
        }

        fun removeActivityStack(activity:Activity){
            if(activityList.contains(activity))
                activityList.remove(activity)

            refreshActivityStack()
        }



        fun isTAGInStack(tag:String):Boolean{
            var find=false
            activityList.forEach {

                if(tag .compareTo((it as BaseActivity).activityTag?:"")==0)
                    find=true

            }
            return find
        }

        fun refreshActivityStack(){
            var temp=ArrayList<Activity>()

            activityList.forEach {

                if(it.isDestroyed || it.isFinishing)
                temp.add(it)
            }

            temp.forEach {
                if(activityList.contains(it))
                    activityList.remove(it)



            }

        }

       private var activityList: ArrayList<Activity> = ArrayList()


    }

    override fun onDestroy() {
        super.onDestroy()
        removeActivityStack(this)

    }

    override fun onStart() {
        super.onStart()
        startCount += 1

    }


    fun isFistStart():Boolean{
        return startCount<=1
    }


    protected override fun attachBaseContext(newBase: Context) {



        super.attachBaseContext(ViewPumpContextWrapper.wrap(LocaleHelper.onAttach(newBase)))
    }


    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
    }




    fun startActivityMakeScene(intent: Intent?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }else{
            super.startActivity(intent)
        }

    }


    fun startActivityMakeSceneForResult(intent: Intent?,requestCode:Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.startActivityForResult(intent,requestCode, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }else{
            super.startActivityForResult(intent,requestCode)
        }

    }


    fun finishSafe() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.finishAfterTransition();
        }

        else
         super.finish();
    }


}