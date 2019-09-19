package com.ronaker.app.base

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.fragment.app.Fragment

public abstract class  BaseFragment :Fragment(){


    fun finishSafe(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity?.finishAfterTransition();
        } else activity?.finish();
    }



    fun startActivityMakeScene(intent: Intent?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity?.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle())
        }else{

            activity?.startActivity(intent)
        }

    }


}