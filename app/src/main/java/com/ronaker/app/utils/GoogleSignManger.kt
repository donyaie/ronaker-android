package com.ronaker.app.utils

import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.ronaker.app.R

object GoogleSignManger{



   private fun loginOption(context:Context):GoogleSignInOptions{
       return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .requestId()
           .requestIdToken(context.getString(R.string.default_web_client_id))
            .build()

    }

    fun getClient(context:Context):GoogleSignInClient{
     return GoogleSignIn.getClient(context.applicationContext, loginOption(context))
    }



    fun setGooglePlusButtonText(signInButton: SignInButton, buttonText: String?) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (i in 0 until signInButton.childCount) {
            val v = signInButton.getChildAt(i)
            if (v is TextView) {
                v.text = buttonText
                TextViewCompat.setTextAppearance(v, R.style.TextAppearance_Medium_Regular)
                v.setTextColor(ContextCompat.getColor(v.context,R.color.colorTextLight))
                return
            }
        }
    }



}