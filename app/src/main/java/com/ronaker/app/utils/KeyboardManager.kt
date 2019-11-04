package com.ronaker.app.utils

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat.getSystemService




object KeyboardManager {


    private val TAG = KeyboardManager::class.java.simpleName

    fun hideSoftKeyboard(context: Context, view: View) {


        val inputMethodManager =
            context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)


        Debug.Log(TAG, "hideSoftKeyboardView")

    }

    fun showSoftKeyboard(context: Context?, view: View) {
        val inputMethodManager =
            context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        view.requestFocus()
        inputMethodManager.showSoftInput(view, 0)
        Debug.Log(TAG, "showSoftKeyboardView")
    }


    fun hideSoftKeyboard(activity: Activity) {


        val view = activity.currentFocus
        if (view != null) {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        } else {
//            var imm =
//                activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager;
//            if (imm.isActive)
//                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // hide
        }

        Debug.Log(TAG, "hideSoftKeyboard")

    }

    fun hideSoftKeyboard(activity: Fragment) {


        val view = activity.view?.rootView?.windowToken
        if (view != null) {

            val imm = activity.context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view, 0)
        }

        Debug.Log(TAG, "hideSoftKeyboard")

    }


    fun showSoftKeyboard(activity: Context) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        ) // show

        Debug.Log(TAG, "showSoftKeyboard")
    }
}
