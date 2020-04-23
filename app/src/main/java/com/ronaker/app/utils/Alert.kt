package com.ronaker.app.utils

import android.app.Activity
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.irozon.sneaker.Sneaker
import com.ronaker.app.R

class Alert {




    enum class Type {
        Warning,
        Error,
        Success
    }


    companion object {


        private val duration=3000

        private fun makeText(context: Activity?, message: String?, type: Type) {


            if (!message.isNullOrBlank())
                context?.let {


                    val sneaker = Sneaker.with(it) // Activity, Fragment or ViewGroup
                        .setMessage(message)

                        .setDuration(duration)
                        .autoHide(true)
                        .setCornerRadius(5,5)

                    when (type) {
                        Type.Warning -> {
                            sneaker.setTitle("Warning!!")
                            sneaker.sneakWarning()

                        }
                        Type.Error -> {
//                            sneaker.setTitle("Error!!")
//                            sneaker.sneakError()




                            sneaker.setTitle("Error!!",R.color.colorTextDark)


                            sneaker.setIcon(R.drawable.ic_guide_red)
                            sneaker .setMessage(message,R.color.colorTextLink)
                            sneaker.sneak( R.color.white)

                        }
                        Type.Success -> {
                            sneaker.setTitle("Success!!")
                            sneaker.sneakSuccess()

                        }

                    }

                }
        }


        private fun makeText(context: Fragment?, message: String?, type: Type) {
            if (!message.isNullOrBlank())
                context?.let {
                    val sneaker = Sneaker.with(it) // Activity, Fragment or ViewGroup

                        .setDuration(duration)
                        .autoHide(true)
                        .setCornerRadius(5,5)



                    when (type) {
                        Type.Warning -> {
                            sneaker .setMessage(message)
                            sneaker.setTitle("Warning!!")
                            sneaker.sneakWarning()

                        }
                        Type.Error -> {

                            sneaker.setTitle("Error!!",R.color.colorTextDark)


                            sneaker.setIcon(R.drawable.ic_guide_red)
                            sneaker .setMessage(message,R.color.colorTextLink)
                            sneaker.sneak( R.color.white)

                        }
                        Type.Success -> {
                            sneaker.setTitle("Success!!")
                            sneaker .setMessage(message)
                            sneaker.sneakSuccess()

                        }

                    }

                }
        }


        fun makeTextError(context: Activity?, message: String?) {
            makeText(context, message, Type.Error)
        }


        fun makeTextError(context: Fragment?, message: String?) {


            makeText(context, message, Type.Error)
        }


        fun makeTextWarning(context: Activity?, message: String?) {
            makeText(context, message, Type.Warning)
        }


        fun makeTextWarning(context: Fragment?, message: String?) {
            makeText(context, message, Type.Warning)
        }


        fun makeTextSuccess(context: Activity?, message: String?) {
            makeText(context, message, Type.Success)
        }


        fun makeTextSuccess(context: Fragment?, message: String?) {
            makeText(context, message, Type.Success)
        }


    }


}