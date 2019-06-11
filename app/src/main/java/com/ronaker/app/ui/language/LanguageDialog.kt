package com.ronaker.app.ui.language

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.ronaker.app.R
import com.ronaker.app.utils.LocaleHelper
import kotlinx.android.synthetic.main.dialog_language.view.*

class LanguageDialog {


    companion object {
        fun showDialog(context: Activity): AlertDialog? {


            val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_language, null)
            //AlertDialogBuilder
            val mBuilder = AlertDialog.Builder(context)
                .setView(mDialogView)
                .setTitle("Select Language")
            //show dialog
            val mAlertDialog = mBuilder.show()
            //login button click of custom layout
            mDialogView.englishLan.setOnClickListener {
                LocaleHelper.setLocale(context, "en")
                mAlertDialog.dismiss()
                context.recreate()
            }
            mDialogView.lithuaniaLan.setOnClickListener {
                LocaleHelper.setLocale(context, "lt")
                mAlertDialog.dismiss()
                context.recreate()
            }



            return mAlertDialog
        }
    }

}