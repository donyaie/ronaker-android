package com.ronaker.app.ui.language

import android.app.Activity
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.ronaker.app.R
import com.ronaker.app.ui.dashboard.DashboardActivity
import com.ronaker.app.utils.LocaleHelper
import kotlinx.android.synthetic.main.dialog_language.view.*

class LanguageDialog {


    companion object {
        fun showDialog(context: Activity): AlertDialog? {

            val mDialogView = View.inflate(context, R.layout.dialog_language, null)
            //AlertDialogBuilder
            val mBuilder = AlertDialog.Builder(context)
                .setView(mDialogView)
                .setTitle("Select Language")
            //show dialog
            val mAlertDialog = mBuilder.show()
            //login button click of custom layout
            mDialogView.englishLan.setOnClickListener {
                LocaleHelper.clear()
                LocaleHelper.setLocale(context, "en")

                mAlertDialog.dismiss()

                context.startActivity(DashboardActivity.newInstance(context))
            }
            mDialogView.lithuaniaLan.setOnClickListener {
                LocaleHelper.clear()
                LocaleHelper.setLocale(context, "lt")
                mAlertDialog.dismiss()
                context.startActivity(DashboardActivity.newInstance(context))
            }
//            mDialogView.DutchLan.setOnClickListener {
//                LocaleHelper.setLocale(context, "nl")
//                mAlertDialog.dismiss()
//                context.startActivity(DashboardActivity.newInstance(context))
//            }


            return mAlertDialog
        }
    }

}